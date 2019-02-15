package com.aerow.redmine;

public class RedmineUser {

	private int id;
	private String userName;
	private String mailAddress;
	
	public RedmineUser() {
	}
	
	public RedmineUser(int id, String userName, String mailAddress)
	{
		this.id = id;
		this.mailAddress = mailAddress;
		this.userName = userName;
	}
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
