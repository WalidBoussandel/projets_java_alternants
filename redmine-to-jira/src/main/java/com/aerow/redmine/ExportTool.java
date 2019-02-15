package com.aerow.redmine;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aerow.config.Config;
import com.aerow.utils.CSVUtils;
import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.User;

public class ExportTool {

	private static final Logger LOGGER = Logger.getLogger(ExportTool.class);

	private RedmineManager mgr;
	
	public RedmineManager getRedmineManager() {
		return mgr;
	}

	public ExportTool() {
		connect();
	}

	public void connect() {
		String uri = Config.getInstance().getRedmineUrl();
		String apiAccessKey = Config.getInstance().getRedmineApiKey();

		mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
	}
	
	public Map<String, RedmineUser> getUsers()
	{
		Map<String, RedmineUser> result = new HashMap<>();
		
		try {
			List<User> users = this.mgr.getUserManager().getUsers();
			for (User user : users) {
				RedmineUser redmineUser = new RedmineUser();
				redmineUser.setId(user.getId());
				redmineUser.setMailAddress(user.getMail());
				redmineUser.setUserName(user.getFullName());
				result.put(user.getFullName(), redmineUser);
			}
			
		} catch (RedmineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {

		LOGGER.info("********** START PROCESSING **********");

		
		String projectKey = Config.getInstance().getRedmineProject();
		Integer queryId = Config.getInstance().getQueryId(); // any
		
		ExportTool exportTool = new ExportTool();
		
	    IssueManager issueManager = exportTool.getRedmineManager().getIssueManager();
	    List<Issue> issues = issueManager.getIssues(projectKey, queryId);
	    
        int iNbMaxComment = 0;
        List<RedmineTicket> redmineTickets = new ArrayList<RedmineTicket>();
        
        Map<String, RedmineUser> usersMap = exportTool.getUsers();
        
		for (Issue issue : issues) {
			LOGGER.info(issue.getId()+","+issue.getSubject()+","+issue.getAuthorName()+","+issue.getAssigneeName());
			issue = issueManager.getIssueById(issue.getId(), Include.journals, Include.attachments);
			RedmineTicket redmineTicket = new RedmineTicket(exportTool.getRedmineManager(), issue, usersMap);
			iNbMaxComment = redmineTicket.nbComments() > iNbMaxComment ? redmineTicket.nbComments() : iNbMaxComment;
			redmineTickets.add(redmineTicket);
		}
		
		List<String> header = new ArrayList<>();
		Collections.addAll(header, "jira_id","redmine_id","issueType","requestType","status","created","created_by","modified","resolved","resolution","priority","summary","version", "category", "Environment", "type_of_incident", "nb_requests", "organization","description");


		
		/** Create CSV file without comments*/
	    File csvFile = File.createTempFile("redmine_no_comment_", ".csv", Config.getInstance().getExportFolder());
        FileWriter writer = new FileWriter(csvFile);
		CSVUtils.writeLine(writer, header);
		int jiraStartIssueId = Config.getInstance().getJiraStartIssueKey();
		for (RedmineTicket redmineTicket : redmineTickets) {
			CSVUtils.writeLine(writer, (List<String>) redmineTicket.getCsvValueList(jiraStartIssueId, 0, usersMap));
			jiraStartIssueId++;
		}
		writer.flush();
		writer.close();

		/** Create FULL CSV file with comments*/
		for (int i = 0; i < iNbMaxComment; i++) {
			header.add("comments");
		}
		
	    File csvNoCommentFile = File.createTempFile("redmine_full_", ".csv", Config.getInstance().getExportFolder());
        writer = new FileWriter(csvNoCommentFile);
		CSVUtils.writeLine(writer, header);
		jiraStartIssueId = Config.getInstance().getJiraStartIssueKey();
		String forceStatus = Config.getInstance().getForceStatus();
		
		for (RedmineTicket redmineTicket : redmineTickets) {
			CSVUtils.writeLine(writer, (List<String>) redmineTicket.getCsvValueList(jiraStartIssueId, iNbMaxComment, usersMap, forceStatus));
			jiraStartIssueId++;
		}
		writer.flush();
		writer.close();
		
		System.out.println(csvFile.getPath());

		LOGGER.info("************* PROCESSING END *************");
	}
	
	
}
