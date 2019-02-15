package com.aerow.config;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config extends Properties {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 115825897658461240L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

	private static final String EXPORT_CONFIG_PROPERTIES = "config.properties";

	private static final String REDMINE_PROJECT = "redmine.project";

	private static final String REDMINE_URL = "redmine.url";

	private static final String REDMINE_QUERY_ID = "redmine.query.id";

	private static final String REDMINE_API_KEY = "redmine.apikey";
	
	private static final String JIRA_LOGIN = "jira.login";
	
	private static final String JIRA_URL = "jira.url";
	
	private static final String JIRA_PWD = "jira.password";

	private static final String JIRA_START_ISSUE_KEY = "jira.start_issue_key";

	private static final String EXPORT_FOLDER = "export.folder";

	private static final String EXPORT_ATTACHMENTS = "export.attachments";

	private static final String FORCE_STATUS = "force.status";

	private static Config _INSTANCE;
	
	private Config() {
		super();
		try {
			InputStream input = null;
			input = this.getClass().getClassLoader().getResourceAsStream(EXPORT_CONFIG_PROPERTIES);
			load(input);
		} catch (Exception e) {
			LOGGER.error("Failed to load properties", e);
		}
	}
	
	public static Config getInstance() {
		
		if (_INSTANCE == null)
			synchronized ("") {
				if (_INSTANCE == null)
				{
					_INSTANCE = new Config();
				}
			}
		return _INSTANCE;
		
	}

	public File getExportFolder()
	{
		String strFolder = this.getProperty(EXPORT_FOLDER);
		
		File folder = new File(strFolder);
		if (!folder.exists())
			folder.mkdirs();
		
		return folder;
	}

	public String getRedmineUrl()
	{
		return getProperty(REDMINE_URL);
	}
	
	public String getRedmineApiKey()
	{
		return getProperty(REDMINE_API_KEY);
	}
	
	public String getRedmineProject()
	{
		return getProperty(REDMINE_PROJECT);
	}
	
	public String getExportFilter()
	{
		return getProperty(REDMINE_PROJECT);
	}
	
	public String getJiraUrl()
	{
		return getProperty(JIRA_URL);
	}
	
	public String getJiraLogin()
	{
		return getProperty(JIRA_LOGIN);
	}
	
	public String getJiraPassword()
	{
		return getProperty(JIRA_PWD);
	}
	
	public Integer getJiraStartIssueKey()
	{
		return Integer.valueOf(getProperty(JIRA_START_ISSUE_KEY));
	}

	public Integer getQueryId() {
		String queryId = getProperty(REDMINE_QUERY_ID);
		if (null != queryId)
			return Integer.valueOf(getProperty(REDMINE_QUERY_ID));
		else 
			return null;
	}
	
	public boolean exportAttachments() {
		return Boolean.parseBoolean(getProperty(EXPORT_ATTACHMENTS));
	}

	public String getForceStatus() {
		return getProperty(FORCE_STATUS);
	}
}
