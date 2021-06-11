package com.nauticana.ncb.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nauticana.ncb.service.DataCache;
import com.nauticana.ncb.service.JsonService;
import com.nauticana.ncb.service.NcbService;

public class NlbHandler implements INlbHandler {

	public static final String SESSION_KEY = "sessionKey";
	public static final String TABLE_NAME = "tableName";
	public static final String BASE_CLASS = "baseClass";
	public static final String SCENARIO_ID = "scenarioId";
	public static final String DATA = "data";
	public static final String ID = "id";
	public static final String USERNAME = "username";
	public static final String PASSTEXT = "passtext";
	public static final String LANGCODE = "langcode";

	@Autowired
    protected JsonService						jsonService;

    @Autowired
    protected NcbService						ncbService;

    @Autowired
    protected DataCache							dataCache;

    public ResponseEntity<String> list(Map<String,String> requestParameters) {
		String sessionKey=requestParameters.get(SESSION_KEY);
		String scenarioId=requestParameters.get(SCENARIO_ID);
		try {
	        return new ResponseEntity<String>(ncbService.listAll(dataCache.getSession(sessionKey).getUser(), scenarioId).toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public ResponseEntity<String> getAll(Map<String,String> requestParameters) {
		String sessionKey=requestParameters.get(SESSION_KEY);
		String scenarioId=requestParameters.get(SCENARIO_ID);
		try {
	        return new ResponseEntity<String>(ncbService.getAll(dataCache.getSession(sessionKey).getUser(), scenarioId).toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public ResponseEntity<String> get(Map<String,String> requestParameters) {
		String sessionKey=requestParameters.get(SESSION_KEY);
		String scenarioId=requestParameters.get(SCENARIO_ID);
		String id=requestParameters.get(ID);
		try {
	        return new ResponseEntity<String>(ncbService.getById(dataCache.getSession(sessionKey).getUser(), scenarioId, id).toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public ResponseEntity<String> delete(Map<String,String> requestParameters) {
		String sessionKey=requestParameters.get(SESSION_KEY);
		String scenarioId=requestParameters.get(SCENARIO_ID);
		String id=requestParameters.get(ID);
    	try {
			return new ResponseEntity<String>(ncbService.delete(dataCache.getSession(sessionKey).getUser(), scenarioId, id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public ResponseEntity<String> update(Map<String,String> requestParameters, String requestBody) {
		String sessionKey=requestParameters.get(SESSION_KEY);
		String scenarioId=requestParameters.get(SCENARIO_ID);
		try {
	        return new ResponseEntity<String>(ncbService.update(dataCache.getSession(sessionKey).getUser(), scenarioId, requestBody).toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public ResponseEntity<String> classes(Map<String,String> requestParameters) {
		String baseClass=requestParameters.get(BASE_CLASS);
    	try {
			return new ResponseEntity<String>(ncbService.classes(baseClass), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public ResponseEntity<String> tableDefinition(Map<String,String> requestParameters) {
		String baseClass=requestParameters.get(BASE_CLASS);
		String tableName=requestParameters.get(TABLE_NAME);
    	try {
			return new ResponseEntity<String>(ncbService.classDef(baseClass, tableName), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public ResponseEntity<String> relations() {
    	try {
			return new ResponseEntity<String>(ncbService.relations(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public ResponseEntity<String> jauntPaths() {
        try {
			return new ResponseEntity<String>(ncbService.jauntPaths().toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public ResponseEntity<String> jauntPath(Map<String,String> requestParameters) {
		String scenarioId=requestParameters.get(SCENARIO_ID);
        try {
			return new ResponseEntity<String>(ncbService.jauntPath(scenarioId).toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public ResponseEntity<String> basePath(Map<String,String> requestParameters) {
		String tableName=requestParameters.get(TABLE_NAME);
        try {
			return new ResponseEntity<String>(ncbService.basePath(tableName).toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public ResponseEntity<String> firstLevelPath(Map<String,String> requestParameters) {
		String tableName=requestParameters.get(TABLE_NAME);
        try {
			return new ResponseEntity<String>(ncbService.firstLevelPath(tableName).toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

	public ResponseEntity<String> login(Map<String,String> requestParameters) {
		String username=requestParameters.get(USERNAME);
		String passtext=requestParameters.get(PASSTEXT);
		String langcode=requestParameters.get(LANGCODE);
		try {
			return new ResponseEntity<String>(ncbService.loginByPassword(username, passtext, langcode).toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}
}