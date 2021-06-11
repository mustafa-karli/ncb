package com.nauticana.ncb.exception;

public class NotAuthorized extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final String BASE_TEXT = "User not Authorized ID ";
	public String scenarioId;
	public String dataId;
	public String username;
	
	public NotAuthorized(String username, String scenarioId, String dataId) {
		super(BASE_TEXT + username + " scenario " + scenarioId + " KEY " + dataId);
		this.scenarioId = scenarioId;
		this.dataId = dataId;
		this.username = username;
	};

}
