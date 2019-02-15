package com.aerow.jira;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

public class JIRARestTool {


	private static URI jiraServerUri = URI.create("https://jiraerow.atlassian.net");

	public static void main(String[] args) throws IOException {
		final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, "****", "****");

		try {
			final IssueRestClient issueClient = restClient.getIssueClient();

			String redmineId = "";
			final SearchResult searchResult = restClient.getSearchClient().searchJql("\"[MIGR] Issue ID\" ~ \""+redmineId+"\"").claim();
			for (Issue issue : searchResult.getIssues()) {
				System.out.println(issue.getAttachmentsUri());
				File file = new File("C:\\test.jpg");
				FileInputStream fis = new FileInputStream(file);
				Promise<Void> attach = issueClient.addAttachment(issue.getAttachmentsUri(), fis, file.getName());
				attach.claim();
			}
			
		} finally {
			restClient.close();
		}
	}
}
