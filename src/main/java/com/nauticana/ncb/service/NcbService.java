package com.nauticana.ncb.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.nauticana.ncb.exception.NotAuthorized;
import com.nauticana.ncb.exception.ScenarioDataNotFound;
import com.nauticana.ncb.exception.ScenarioNotFound;
import com.nauticana.ncb.model.JauntPath;
import com.nauticana.ncb.model.Labels;
import com.nauticana.ncb.model.NtcnSession;
import com.nauticana.ncb.model.PortalLanguage;
import com.nauticana.ncb.model.UserMenu;
import com.nauticana.ncb.model.Utils;
import com.nauticana.ncb.repository.BasisRepository;

@Service
public class NcbService {

    @Autowired
    protected BasisRepository					basisRepository;

    @Autowired
    protected JsonService						jsonService;

	@Autowired
	TableService 								tableService;

	@Autowired
    protected DataCache							dataCache;

    public JSONArray listAll(String username, String scenarioId) throws Exception {
        if (Utils.emptyStr(username)) throw new Exception(Labels.ERR_UNAUTHORIZED);
    	JauntPath path = null;
		try {
			path = jsonService.getScenarioPath(scenarioId);
		} catch (Exception e) {
			path = jsonService.getBasePath(scenarioId, scenarioId, "");
		}
        if (path == null) throw new Exception("Scenario path not found for " + scenarioId);
    	JSONArray records = jsonService.listAll(path, 1);
        if (records == null) throw new Exception("Data not found for scenario " + scenarioId);
        return records;
    }

    public JSONArray getAll(String username, String scenarioId) throws Exception {
        if (Utils.emptyStr(username)) throw new NotAuthorized(username, scenarioId, "");
    	JauntPath path = null;
		try {
			path = jsonService.getScenarioPath(scenarioId);
		} catch (Exception e) {
			path = jsonService.getFirstLevelPath(scenarioId);
		}
        if (path == null) throw new ScenarioNotFound(scenarioId);
    	JSONArray records = jsonService.getAll(path, 1);
        if (records == null) throw new ScenarioDataNotFound(scenarioId, "");
        return records;
    }

    public JSONObject getById(String username, String scenarioId, String id) throws Exception {
        if (Utils.emptyStr(username)) throw new NotAuthorized(username, scenarioId, id);
    	JauntPath path = null;
		try {
			path = jsonService.getScenarioPath(scenarioId);
		} catch (Exception e) {
			path = jsonService.getFirstLevelPath(scenarioId);
		}
        if (path == null) throw new ScenarioNotFound(scenarioId);
        JSONObject entity = jsonService.get(path, id);
        if (entity == null) throw new ScenarioDataNotFound(scenarioId, id);
        return entity;
    }

    public String delete(String username, String scenarioId, String id) throws Exception {
        if (Utils.emptyStr(username)) throw new NotAuthorized(username, scenarioId, id);
    	JauntPath path = null;
		try {
			path = jsonService.getScenarioPath(scenarioId);
		} catch (Exception e) {
			path = jsonService.getFirstLevelPath(scenarioId);
		}
        boolean deleted = jsonService.delete(path, id);
        if (deleted) return "Record deleted successfully";
        throw new ScenarioDataNotFound(scenarioId, id);
    }

    public JSONObject update(String username, String scenarioId, String jsonAsString) throws Exception {
        if (Utils.emptyStr(username)) throw new NotAuthorized(username, scenarioId, "");
    	JauntPath path = null;
    	try {
    		path = jsonService.getScenarioPath(scenarioId);
    	} catch (Exception e) {
    		path = jsonService.getFirstLevelPath(scenarioId);
    	}
        JSONObject entity = new JSONObject(jsonAsString);
        JSONObject newEntity = null;
        long newId = jsonService.put(path, entity);
        if (newId != -1)
        	newEntity = jsonService.get(path, newId+"");
        else {
        	newEntity = jsonService.get(path, JsonService.getId(path.tableDefinition.getKeys(), entity));
        }
        if (newEntity == null) throw new Exception("INTERNAL_SERVER_ERROR");
        return newEntity;
    }

    public String classes(String baseClass) throws Exception {
    	List<String> m = new ArrayList<String>();
    	List<String> n = new ArrayList<String>();
    	List<String> l = tableService.allTypeScriptClasses(baseClass, m, n);
//    	for (int i = 0; i < l.size(); i++) {
//			String folderName=m.get(i);
//			String fileName=n.get(i);
//	    	Utils.saveToFile(l.get(i), "export/separate/"+folderName+"/"+fileName+".ts", false);
//		}
    	
    	String expRoot="/data/CODE/ELIPS/ncf/src/app/shared/model/";
//    	List<String> x = new ArrayList<String>();
//    	String moduleName = "";
//    	for (int i = 0; i < l.size(); i++) {
//			String folderName=m.get(i);
//			if (folderName.equals(moduleName)) {
//				x.add("");
//				x.add(l.get(i));
//			} else {
//		    	Utils.saveToFile(x, expRoot+moduleName+".ts", false);
//		    	x.clear();
//				moduleName=folderName;
//			}
//		}
//    	Utils.saveToFile(x, expRoot+moduleName+".ts", false);
    	Utils.saveToFile(l, expRoot+"nca.ts", false);
    	return "Saved to nca.ts";
    }
/*
md finance
md personnel
md material
md request
md sales
md purchase
md production
md maintenance
md shipment
md commerce
md motifArge
md project
md helpdesk
md business
md settings    	
 */

    public String classDef(String baseClass, String tableName) throws Exception {
    	String tableClass = tableService.typeScriptClass(baseClass, tableName);
        if (Utils.emptyStr(tableClass)) throw new Exception(Labels.ERR_PARAMETER_MISSING);
        return tableClass;
    }

    public String relations() throws Exception {
    	List<String> l = tableService.allRelations();
    	Utils.saveToFile(l, "relations.txt", false);
    	return "Saved to relations.txt";
    }

    public JSONArray jauntPaths() throws Exception {
        List<JauntPath> paths = jsonService.getScenarios();
        if (paths == null) throw new ScenarioNotFound("");
        JSONArray entities = new JSONArray();
        for (JauntPath path : paths) {
        	entities.put(path.toJson());
        }
        return entities;
    }

    public JSONObject jauntPath(@PathVariable String scenarioId) throws Exception {
    	JauntPath path = null;
		try {
			path = jsonService.getScenarioPath(scenarioId);
		} catch (Exception e) {
			path = jsonService.getFirstLevelPath(scenarioId);
		}
        if (path == null) throw new ScenarioNotFound(scenarioId);
        JSONObject entity = path.toJson();
        if (entity == null) throw new Exception("path to json conversion error");
        return entity;
    }

    public JSONObject basePath(String tableName) throws Exception {
        JauntPath path = jsonService.getBasePath(tableName, tableName, "");
        if (path == null) throw new ScenarioNotFound(tableName);;
        JSONObject entity = path.toJson();
        if (entity == null) throw new Exception("HttpStatus.NOT_FOUND");
        return entity;
    }

    public JSONObject firstLevelPath(String tableName) throws Exception {
        JauntPath path = jsonService.getFirstLevelPath(tableName);
        if (path == null) throw new ScenarioNotFound(tableName);
        JSONObject entity = path.toJson();
        if (entity == null) throw new Exception("HttpStatus.NOT_FOUND");
        return entity;
    }

	public JSONObject loginByPassword(String user, String pass, String lang) throws Exception {
        if (Utils.emptyStr(user)) throw new Exception("NO USER SPECIFIED");
        user = user.toUpperCase(Locale.ENGLISH);
        tableService.loadAllTables();
		JauntPath path = jsonService.getFirstLevelPath("USER_ACCOUNT");
        if (path == null) throw new ScenarioNotFound("USER_ACCOUNT");
        JSONObject entity = jsonService.get(path, user);
        if (entity == null) throw new Exception("USER NOT FOUND");
        String secret = entity.getString("passtext");
        if (!Utils.emptyStr(secret) && !secret.equals(pass)) throw new Exception("PASSWORD MISMATCH");
        entity.remove("passtext");
        entity.put("passtext", "VALID");

        NtcnSession ntcnSession = basisRepository.sessionWithEmployee(user,lang);
        entity.put("client", ntcnSession.getClient());
        entity.put("organizationId", ntcnSession.getOrganizationId());
        entity.put("personId", ntcnSession.getPersonId());
        entity.put("position", ntcnSession.getPosition());
        entity.put("cellPhone", ntcnSession.getCellPhone());
        entity.put("sessionKey", ntcnSession.getKey());
        entity.put("language", ntcnSession.getLang());
        if (!ntcnSession.getMenu().isEmpty())
        	entity.put("userMenus", UserMenu.toJson(ntcnSession.getMenu()));
        dataCache.putSession(ntcnSession);
        return entity;
	}

	public String captionTranslations(String langcode) throws Exception {
		String s = "";
		PortalLanguage language = dataCache.getLanguage(langcode);
		Hashtable<String, String> t = language.translations;
		Set<Entry<String, String>> entrySet = t.entrySet();
        for(Entry<String, String> entry : entrySet) {
        	s = s + "," + "{caption:'" + entry.getKey() + "' labellower:'" + entry.getValue() + "'}";
        }
		return "[" + s.substring(1) + "]";
	}

	public String domainValues(String domain, String langcode) throws SQLException, IOException {
		String s = "";
		PortalLanguage language = dataCache.getLanguage(langcode);
		
		for (Entry<String, String> entry :  dataCache.getDomainOptions(domain, language).entrySet()) {
        	s = s + "," + "{value:'" + entry.getKey() + "' caption:'" + entry.getValue() + "'}";
		}
		return "[" + s.substring(1) + "]";
	}
}
