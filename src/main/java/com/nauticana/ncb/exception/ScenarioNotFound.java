package com.nauticana.ncb.exception;

public class ScenarioNotFound extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final String BASE_TEXT = "Scenario not found with ID ";
	public String scenarioId; 
	
	public ScenarioNotFound(String scenarioId) {
		super(BASE_TEXT + scenarioId);
		this.scenarioId = scenarioId;
	};

}
