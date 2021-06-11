package com.nauticana.ncb.exception;

public class ScenarioDataNotFound extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final String BASE_TEXT = "Data not found for scenario ID ";
	public String scenarioId;
	public String dataId;
	
	public ScenarioDataNotFound(String scenarioId, String dataId) {
		super(BASE_TEXT + scenarioId + " KEY " + dataId);
		this.scenarioId = scenarioId;
		this.dataId = dataId;
	};

}
