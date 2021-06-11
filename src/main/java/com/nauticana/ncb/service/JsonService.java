package com.nauticana.ncb.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nauticana.ncb.model.FieldType;
import com.nauticana.ncb.model.JauntPath;
import com.nauticana.ncb.model.Labels;
import com.nauticana.ncb.model.TableDefinition;
import com.nauticana.ncb.model.TableRelation;
import com.nauticana.ncb.model.Utils;
import com.nauticana.ncb.repository.BasisRepository;
import com.nauticana.ncb.repository.DataServer;

@Service
public class JsonService {

	public static final HashMap<String, JauntPath> jauntPaths = new HashMap<String, JauntPath>(0);
	public static final HashMap<String, JauntPath> basePaths = new HashMap<String, JauntPath>(0);
	public static final HashMap<String, JauntPath> firstLevelPaths = new HashMap<String, JauntPath>(0);
	
	@Autowired
	BasisRepository basisRepository;

	@Autowired
	TableService tableService;
	
    @Autowired
    protected DataServer server;
 
	public JauntPath getScenarioPath(String scenarioId) throws SQLException {
		JauntPath jauntPath = jauntPaths.get(scenarioId);
		if (jauntPath != null) return jauntPath;
		ArrayList<String[]> pathData = basisRepository.retreiveScenarioPath(scenarioId);
		ArrayList<JauntPath> paths = new ArrayList<JauntPath>();
		JauntPath master = null;
		for (String[] row : pathData) {
			if (master == null) {
				master = new JauntPath(scenarioId, tableService.getTableDefinition(row[1]), row[0], 'M', null, null);
				paths.add(master);
			}
			JauntPath parent = master;
			try {
				short parentSeq = Short.valueOf(row[6]);
				if (parentSeq > 0)
					parent = paths.get(parentSeq);
				TableRelation relation = tableService.getRelation(row[2]);
				TableDefinition controller = tableService.getTableDefinition(row[3]);
				String caption = row[4];
				char   accessType = row[5].charAt(0);
				paths.add(new JauntPath(scenarioId, controller, caption, accessType, relation, parent));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (paths.isEmpty()) return null;
		jauntPath = paths.get(0);
		if (jauntPath != null)
			jauntPaths.put(scenarioId, jauntPath);
		return jauntPath;
	}

	public JauntPath getBasePath(String tableName, String caption, String exemptParent) {
		JauntPath jauntPath = basePaths.get(tableName);
		if (jauntPath == null) {
			jauntPath = makeBasePath(tableName, caption, exemptParent);
			basePaths.put(caption, jauntPath);
		}
		return jauntPath;
	}
	
	private JauntPath makeBasePath(String tableName, String caption, String exemptParent) {
		TableDefinition controller;
		try {
			controller = tableService.getTableDefinition(tableName);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		ArrayList<JauntPath> paths = new ArrayList<JauntPath>();
		JauntPath master = new JauntPath("", controller, caption, 'M', null, null);
		paths.add(master);
		for (TableRelation relation : controller.getParents().values())
			if (!relation.parent.getTableName().equals(exemptParent)) {
			paths.add(new JauntPath("", relation.parent, relation.relationName, 'P', relation, master));
		}
		return paths.get(0);
	}
	
	public JauntPath getFirstLevelPath(String tableName) {
		JauntPath jauntPath = firstLevelPaths.get(tableName);
		if (jauntPath == null) {
			jauntPath = makeBasePath(tableName, tableName, "");
			TableDefinition controller;
			try {
				controller = tableService.getTableDefinition(tableName);
				for (String child : basisRepository.retreiveChildTables(tableName)) {
					tableService.getTableDefinition(child);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			for (TableRelation relation : controller.getChildren().values()) {
				JauntPath child = makeBasePath(relation.child.getTableName(), relation.relationName, tableName);
				child.setParent(jauntPath);
				child.setPathType('L');
				child.relation = relation;
			}
			firstLevelPaths.put(tableName, jauntPath);
			String p1st = tableName + "_1ST";
			if (jauntPaths.get(p1st) == null) {
				jauntPaths.put(p1st, jauntPath);
			}
		}
		return jauntPath;
	}
    
	public JSONObject get(JauntPath path, Object[] id) throws Exception {
		Connection conn = server.getConnection();
		ResultSet record = server.findByIdReadonly(conn, path.tableDefinition, id);
		if (record == null) return null;
		if (!record.next()) return null;
		JSONObject object = jsonRecord(path.tableDefinition.getFields(), record);
		record.close();
		return jsonTree(conn, path, object);
	}
	
	public JSONObject get(JauntPath path, String id) throws Exception {
		Object[] _id = path.tableDefinition.strToId(id);
		return get(path, _id);
	}

	public JSONArray getAll(JauntPath path, int client) throws Exception {
		ResultSet records;
		Connection conn = server.getConnection();
		if (path.tableDefinition.isClientDependent())
			records = server.findByClient(conn, path.tableDefinition, client);
		else
			records = server.findAll(conn, path.tableDefinition);
		if (records == null) return null;
		ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
		while (records.next()) {
			jsons.add(jsonRecord(path.tableDefinition.getFields(), records));
		}
		records.close();
		JSONArray jsonArray = new JSONArray();
		for (JSONObject json : jsons) {
			jsonArray.put(jsonTree(conn, path, json));
		}
		return jsonArray;
	}
	
	public JSONArray listAll(JauntPath path, int client) throws Exception {
		ResultSet records;
		Connection conn = server.getConnection();
		if (path.tableDefinition.isClientDependent())
			records = server.findByClient(conn, path.tableDefinition, client);
		else
			records = server.findAll(conn, path.tableDefinition);
		JSONArray jsonArray = new JSONArray();
		if (records == null) return null;
		while (records.next()) {
			jsonArray.put(jsonRecord(path.tableDefinition.getFields(), records));
		}
		records.close();
		return jsonArray;
	}
	
	public boolean delete(JauntPath path, String id) throws Exception {
		server.delete(path.tableDefinition, path.tableDefinition.strToId(id));
		return true;
	}

	public long put(JauntPath path, JSONObject object) throws Exception {
		Connection conn = server.getConnection();
		return saveTree(conn, path, object, null, -1);
	}

	public long saveTree(Connection conn, JauntPath path, JSONObject object, String parentSequenceField, long parentSequenceValue) throws Exception {
		String mySequenceField = "";
		long   mySequenceValue = -1;
		char oc = object.getString("oc").charAt(0);
		if (oc != 'R') {
			Object[] id   = new Object[path.tableDefinition.getKeys().length];
			Object[] vals = new Object[path.tableDefinition.getFields().length];
			Object[] nonid = new Object[path.tableDefinition.getFields().length-path.tableDefinition.getKeys().length];
			int idpos  = 0;
			int nonpos = 0;
			for (int i = 0; i < path.tableDefinition.getFields().length; i++) {
				FieldType field = path.tableDefinition.getFields()[i];
				try {
					switch (field.getType()) {
						case FieldType.T_SHORT  : vals[i] = object.getInt(field.camelName);   break;
						case FieldType.T_INT    : vals[i] = object.getInt(field.camelName);   break;
						case FieldType.T_LONG   : vals[i] = object.getLong(field.camelName);   break;
						case FieldType.T_FLT    : vals[i] = object.getDouble(field.camelName);   break;
//						case FieldType.T_DATE   : vals[i] = object.get(path.tableDefinition.getKeys()[i].camelName);   break;
						default                 : vals[i] = object.getString(field.camelName);   break;
						}
				} catch (Exception e) {
					vals[i] = null;
				}
				if (field.isKeyField()) {
					id[idpos] = vals[i];
					idpos++;
				} else {
					nonid[nonpos] = vals[i];
					nonpos++;
				}
			}
			
			if (oc == 'D') {
				server.delete(path.tableDefinition, id);
				return -1;
			}
			
			ResultSet rs;
			if (oc == 'U') {
				rs = server.findByIdUpdatable(conn, path.tableDefinition, id);
				if (rs.next()) {
					setNonidVals(rs, path.tableDefinition.getFields(), nonid);
					rs.updateRow();
					rs.close();
				} else {
					throw new Exception("Record not found");
				}
			} else {
				rs = server.emptyRow(conn, path.tableDefinition);
				setAllVals(rs, path.tableDefinition.getFields(), vals);
				if (!Utils.emptyStr(parentSequenceField)) {
					FieldType f = path.tableDefinition.getField(parentSequenceField);
					if (f != null) {
						if (f.getType() == FieldType.T_LONG)
							rs.updateLong(f.getOrder(), parentSequenceValue);
						else
							rs.updateInt(f.getOrder(), (int) parentSequenceValue);
					}
				}
				if (path.tableDefinition.getKeys()[0].getEditStyle().equals("S")) {
					mySequenceField = path.tableDefinition.getKeys()[0].fieldName;
					mySequenceValue = basisRepository.nextSequenceValue(path.tableDefinition.getSequenceName());
					if (path.tableDefinition.getKeys()[0].getType() == FieldType.T_LONG)
							rs.updateLong(path.tableDefinition.getKeys()[0].getOrder(), mySequenceValue);
						else
							rs.updateInt(path.tableDefinition.getKeys()[0].getOrder(), (int) mySequenceValue);
				}
				rs.insertRow();
				rs.close();
			}
			if (mySequenceValue < 1) {
				mySequenceField = parentSequenceField;
				mySequenceValue = parentSequenceValue;
			}
		}
		if (path.children != null)
		for (JauntPath childPath : path.children) {
			if (childPath.pathType == 'P') {
				JSONObject childObject = null;
				try {
					childObject = object.getJSONObject(childPath.camelName);
				} catch (JSONException e) {
				} finally {
					if (childObject != null)
						saveTree(conn, childPath, childObject, mySequenceField, mySequenceValue);
				}
			} else {
				JSONArray childObjects = null;
				try {
					childObjects = object.getJSONArray(childPath.camelName);
				} catch (JSONException e) {
				} finally {
					if (childObjects != null)
						for (int i = 0; i < childObjects.length(); i++) {
							saveTree(conn, childPath, childObjects.getJSONObject(i), mySequenceField, mySequenceValue);
						}
				}
			}
		}
		return mySequenceValue;
	}
	
	public static void setAllVals(ResultSet rs, FieldType[] fields, Object[] vals) throws ParseException {
		try {
			for (int i = 0; i < fields.length; i++) {
				if (vals[i] == null) {
					rs.updateNull(i + 1);
				} else {
					try {
						switch (fields[i].getType()) {
							case FieldType.T_BYTE  : rs.updateByte(i + 1,   (byte)   vals[i]); break;
							case FieldType.T_SHORT : rs.updateShort(i + 1,  (short)(int) vals[i]); break;
							case FieldType.T_INT   : rs.updateInt(i + 1,    (int)    vals[i]); break;
							case FieldType.T_LONG  : rs.updateLong(i + 1,   (long)   vals[i]); break;
							case FieldType.T_FLT   : rs.updateDouble(i + 1, (double) vals[i]); break;
							case FieldType.T_STR   : rs.updateString(i + 1, (String) vals[i]); break;
							case FieldType.T_DATE  :
								java.sql.Date ud = new Date(Labels.dmyDF.parse((String) vals[i]).getTime());
								rs.updateDate(i + 1, ud);  break;
							default                : rs.updateString(i + 1, (String) vals[i]); break;
						}
					} catch (SQLException e) {
						rs.updateNull(i + 1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public static void setNonidVals(ResultSet rs, FieldType[] fields, Object[] vals) {
		try {
			int j = -1;
			for (int i = 0; i < fields.length; i++)
			if (!fields[i].isKeyField()) {
				j++;
				if (vals[j] == null) {
					rs.updateNull(i + 1);
				} else {
					try {
						switch (fields[i].getType()) {
							case FieldType.T_BYTE  : rs.updateByte(i + 1,   (byte)   vals[j]); break;
							case FieldType.T_SHORT : rs.updateShort(i + 1,  (short)(int) vals[j]); break;
							case FieldType.T_INT   : rs.updateInt(i + 1,    (int)    vals[j]); break;
							case FieldType.T_LONG  : rs.updateLong(i + 1,   (long)   vals[j]); break;
							case FieldType.T_FLT   : rs.updateDouble(i + 1, (double) vals[j]); break;
							case FieldType.T_STR   : rs.updateString(i + 1, (String) vals[j]); break;
							case FieldType.T_DATE  : rs.updateDate(i + 1,   (java.sql.Date) vals[j]); break;
							default                : rs.updateString(i + 1, (String) vals[j]); break;
						}
					} catch (SQLException e) {
						rs.updateNull(i + 1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public static JSONObject jsonRecord(FieldType[] fields, ResultSet record) throws SQLException, JSONException {
		JSONObject json = new JSONObject();
		json.put("oc", "R");
		for (int i = 0; i < fields.length; i++) {
			switch (fields[i].getType()) {
				case FieldType.T_BYTE :
					json.put(fields[i].camelName, record.getInt(i+1));
					break;
				case FieldType.T_SHORT :
					json.put(fields[i].camelName, record.getInt(i+1));
					break;
				case FieldType.T_INT :
					json.put(fields[i].camelName, record.getInt(i+1));
					break;
				case FieldType.T_LONG :
					json.put(fields[i].camelName, record.getLong(i+1));
					break;
				case FieldType.T_FLT :
					json.put(fields[i].camelName, record.getDouble(i+1));
					break;
				case FieldType.T_DATE :
					json.put(fields[i].camelName, record.getDate(i+1));
					break;
				case FieldType.T_STR :
					json.put(fields[i].camelName, record.getString(i+1));
					break;
				default:
					json.put(fields[i].camelName, record.getString(i+1));
					break;
			}
		}
		return json;
	}
	
	public static Object[] getId(FieldType[] fields, ResultSet record) throws SQLException {
		Object[] id = new Object[fields.length];
		for (int i = 0; i < id.length; i++) {
			FieldType field = fields[i];
			switch (field.getType()) {
				case FieldType.T_BYTE :
					id[i] = record.getByte(i+1);
					break;
				case FieldType.T_SHORT :
					id[i] = record.getShort(i+1);
					break;
				case FieldType.T_INT :
					id[i] = record.getInt(i+1);
					break;
				case FieldType.T_LONG :
					id[i] = record.getLong(i+1);
					break;
				case FieldType.T_FLT :
					id[i] = record.getDouble(i+1);
					break;
				case FieldType.T_DATE :
					id[i] = record.getDate(i+1);
					break;
				case FieldType.T_STR :
					id[i] = record.getString(i+1);
					break;
				default:
					id[i] = record.getString(i+1);
					break;
			}
		}
		return id;
	}
	
	public static Object[] getId(FieldType[] fields, JSONObject record) throws SQLException, JSONException, ParseException {
		Object[] id = new Object[fields.length];
		for (int i = 0; i < id.length; i++) {
			FieldType field = fields[i];
			switch (field.getType()) {
				case FieldType.T_BYTE :
					id[i] = (byte) record.getInt(field.camelName);
					break;
				case FieldType.T_SHORT :
					id[i] = (short) record.getInt(field.camelName);
					break;
				case FieldType.T_INT :
					id[i] = record.getInt(field.camelName);
					break;
				case FieldType.T_LONG :
					id[i] = record.getLong(field.camelName);
					break;
				case FieldType.T_FLT :
					id[i] = record.getDouble(field.camelName);
					break;
				case FieldType.T_DATE :
					id[i] = Labels.ymdDF.parse(record.getString(field.camelName));
					break;
				case FieldType.T_STR :
					id[i] = record.getString(field.camelName);
					break;
				default:
					id[i] = record.getString(field.camelName);
					break;
			}
		}
		return id;
	}
	
	public JSONObject jsonTree(Connection conn, JauntPath path, JSONObject json) throws SQLException, JSONException, ParseException {
		if (json == null) return null;
		if (path.children != null) {
			for (JauntPath childPath : path.children) {
				if (childPath.pathType == 'P') {
					Object[] id = getId(childPath.relation.childKeys, json);
					JSONObject parentJson = null;
					ResultSet parentRecord = server.findByIdReadonly(conn, childPath.tableDefinition, id);
					if (parentRecord.next()) {
						parentJson = jsonRecord(childPath.tableDefinition.getFields(), parentRecord);
					}
					parentRecord.close();
					json.put(childPath.caption, jsonTree(conn, childPath, parentJson));
				}
			}
			for (JauntPath childPath : path.children) {
				if (childPath.pathType == 'L') {
					JSONArray jsonArray = new JSONArray();
					Object[] id = getId(childPath.relation.parentKeys, json);
					ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
					ResultSet childRecords = server.readOnlyQuery(conn, childPath.relation.childSql, id);
					while (childRecords.next()) {
						jsons.add(jsonRecord(childPath.tableDefinition.getFields(), childRecords));
					}
					childRecords.close();
					for (JSONObject childJson : jsons) {
						jsonArray.put(jsonTree(conn, childPath, childJson));
					}
					json.put(childPath.relation.camelName, jsonArray);
				}
			}
		}
		return json;
	}

	public List<JauntPath> getScenarios() throws SQLException {
		List<String> ids = basisRepository.retreiveScenarios();
		if (ids.isEmpty()) return null;
		ArrayList<JauntPath> l = new ArrayList<JauntPath>();
		for (String id : ids) {
			l.add(getScenarioPath(id));
		}
		return l;
	}
	
}
