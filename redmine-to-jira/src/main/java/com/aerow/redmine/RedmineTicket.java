package com.aerow.redmine;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aerow.config.Config;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Journal;
import com.taskadapter.redmineapi.bean.User;

public class RedmineTicket extends Issue{

	private Integer id;
	private String summary;
	private String description;
	private String issueType;
	private String requestType;
	private String customField2;
	private String customField3;
	private String customField1;
	private String priority;
	private String status;
	private List<String> comments;
	private Date created;
	private Date resolved;
	private Date modified;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
	private String createdBy;
	private String version;
	private String category;
	
	public RedmineTicket() {
		
	}

	public RedmineTicket(RedmineManager redmineManager, Issue issue, Map<String, RedmineUser> usersMap) {
		this.id = issue.getId();
		this.summary = issue.getSubject();
		this.description = issue.getDescription();
		this.created = issue.getCreatedOn();
		this.createdBy = issue.getAuthorName();
		this.version = issue.getTargetVersion() != null ? issue.getTargetVersion().getName() : null;
		this.resolved = issue.getClosedOn();
		this.category = issue.getCategory() != null ? issue.getCategory().getName() : null;
		this.modified = issue.getUpdatedOn();
		this.issueType = issue.getTracker().getName();
		setRequestType(this.issueType);
		this.status = issue.getStatusName();
		this.priority = issue.getPriorityText();
		this.customField1 = issue.getCustomFieldByName("Environment").getValue();
		switch (this.issueType) {
		case "Incident":
			this.customField2 = issue.getCustomFieldByName("Type of incident").getValue();
			break;
		case "Request":
		case "Templates":
			this.customField2 = issue.getCustomFieldByName("Type of request").getValue();
			break;
		default:
			break;
		}
		this.customField3 = issue.getCustomFieldByName("Number of requests").getValue();
	
		this.comments = extractComments (issue, usersMap);
		
		if (Config.getInstance().exportAttachments())
		{
			for (Attachment attachment : issue.getAttachments()) {
				try {
					File exportFolder = Config.getInstance().getExportFolder();
					File dirTicket = new File(exportFolder.getPath()+File.separator+this.id);
					if (!dirTicket.exists())
					{
						dirTicket.mkdirs();
					}
					File attachmentFile = new File(dirTicket.getPath()+File.separator+attachment.getFileName());
					if (!attachmentFile.exists())
					{
						FileOutputStream fos = new FileOutputStream(attachmentFile);
						redmineManager.getAttachmentManager().downloadAttachmentContent(attachment, fos);
						fos.flush();
						fos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setRequestType(String issueType) {
		switch (issueType) {

//		case "ALSTOM-Incident (NewECM)":
//			this.requestType = "Declare an incident";
//			break;
//		case "ALSTOM-Service Request(NewECM)":
//			this.requestType = "Declare a Service Request";
//			break;
//		case "ALSTOM-Change Request":
//			this.requestType = "Declare a Change Request";
//			break;
//		default:
//			break;
//		}
		case "Incident":
			this.requestType = "Declare an Incident";
			break;
		case "Request":
			this.requestType = "Declare a Service Request";
			break;
		case "Templates":
			this.requestType = "Declare a Change Request";
			break;
		default:
			break;
		}
	}

	private List<String> extractComments(Issue issue, Map<String, RedmineUser> usersMap) {

		if (issue.getJournals() != null)
		{
			comments = new ArrayList<String>();
			for (Journal journal : issue.getJournals()) {
				if (journal.getNotes() != null)
				{
					User user = journal.getUser();
					comments.add(formatDate(journal.getCreatedOn())+";"+formatUser(usersMap, user.getFullName())+";"+journal.getNotes());
				}
			}
		}
		return comments;
	}
	
	public int nbComments()
	{
		return comments != null ? comments.size() : 0;
	}

	private String formatDate(Date date) {
		return date != null ? sdf.format(created) : "";
	}
	
	public List<String> getCsvValueList(int jiraId, int iNbComments, Map<String, RedmineUser> usersMap) {
		return getCsvValueList(jiraId, iNbComments, usersMap, null);
	}

	public List<String> getCsvValueList(int jiraId, int iNbComments, Map<String, RedmineUser> usersMap, String forceStatus) {
		List<String> listResult = new ArrayList<String>();
		listResult.add(String.valueOf(jiraId));
		listResult.add(this.id.toString());
		listResult.add(this.issueType);
		listResult.add(this.requestType);
		listResult.add(forceStatus != null ? forceStatus : this.status);
		listResult.add(formatDate(this.created));
		listResult.add(formatUser(usersMap, this.createdBy));
		listResult.add(formatDate(modified));
		listResult.add(formatDate(resolved));
		listResult.add(resolved != null ? "resolved" : getResolution());
		listResult.add(priority);
		listResult.add(this.summary);
		listResult.add(version);
		listResult.add(category);
		listResult.add(customField1);
		listResult.add(customField2);
		listResult.add(customField3);
		listResult.add(Config.getInstance().getProperty("organization"));
		listResult.add(description);
		if (iNbComments != 0)
		{
			int nbDummyComment = iNbComments - nbComments();
			if (nbComments() > 0)
			{
				for (String comment : this.comments) {
					listResult.add(comment);
				}
			}
			if (nbDummyComment > 0){
				for (int i = 0; i < nbDummyComment; i++) {
					listResult.add("");
				}
			}
		}
		return listResult;
	}
	
	private String getResolution() {
		String resolution = null;
		
		switch (this.status) {
		case "Refused":
			resolution="Won't do";
			break;
		case "Canceled":
			resolution="Cannot be impleted";
			break;
		case "Rejected":
			resolution="Refused";
			break;

		default:
			resolution = "None";
			break;
		}
		return resolution;
	}

	@SuppressWarnings("unused")
	private String formatUser(Map<String, RedmineUser> map, String strUserName) {
		RedmineUser user = map.get(strUserName);
		if (user != null && (user.getMailAddress().endsWith("aerow.fr") || user.getMailAddress().endsWith("aerowecmworld.com")))
		{
			return user.getMailAddress().substring(0, user.getMailAddress().indexOf("@"));
		} else if (user != null){
			return user.getMailAddress();
		}
		return strUserName;
	}

	@Override
	public String toString() {
	
		return id + ","+summary+","+issueType+","+status+","+formatDate(this.created)+","+formatDate(modified)+","+formatDate(resolved)+","+priority+","+customField3+","+customField2+","+customField1;
	}

}
