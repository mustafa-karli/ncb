package com.nauticana.ncb.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JauntPath {
	public String                   scenarioId;
	public TableDefinition			tableDefinition;
	public String					caption;
	public String					camelName;
	public char						pathType;
	public TableRelation			relation;
	public JauntPath				parent;
	public ArrayList<JauntPath>	    children;
	
	private JSONObject              json = null;

	public JauntPath(String scenarioId, TableDefinition tableDefinition, String caption, char pathType, TableRelation relation, JauntPath parent) {
		this.scenarioId = scenarioId;
		this.tableDefinition = tableDefinition;
		this.caption = caption;
		this.camelName = Utils.camelCase(caption);
		this.pathType = pathType;
		this.relation = relation;
		setParent(parent);
	}

	public void setParent(JauntPath parent) {
		this.parent = parent;
		if (parent != null) {
			if (parent.children == null)
				parent.children = new ArrayList<JauntPath>();
			parent.children.add(this);
		}
	}
	
	public void setPathType(char pathType) {
		this.pathType = pathType;
	}
	
	public JSONObject toJson() throws JSONException {
		if (json != null) return json;
		json = new JSONObject();
		json.put("scenarioId",  scenarioId);
		json.put("camelName",   camelName);
		json.put("caption",     caption);
		json.put("pathType",    pathType+"");
		json.put("table",       tableDefinition.toJson());
		if (relation != null) {
			JSONArray pk = new JSONArray();
			for (int i = 0; i < relation.parentKeys.length; i++) {
				pk.put(relation.parentKeys[i].toJson());
			}
			JSONArray ck = new JSONArray();
			for (int i = 0; i < relation.childKeys.length; i++) {
				ck.put(relation.childKeys[i].toJson());
			}
			json.put("parentKeys",  pk);
			json.put("childKeys",   ck);
		}
		if (children != null) {
			JSONArray ch = new JSONArray();
			for (int i = 0; i < children.size(); i++) {
				ch.put(children.get(i).toJson());
			}
			json.put("children",    ch);
		}
		return json;
	}

}
