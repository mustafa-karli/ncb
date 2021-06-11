package com.nauticana.ncb.model;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TableRelation implements Serializable {
	
	private static final long serialVersionUID = 3155409105407126324L;

//	public static final int NO_LOOKUP          = 0;
	public static final int LOOKUP_COMBO       = 1;
	public static final int LOOKUP_POPUP       = 2;
	public static final int LOOKUP_CUSTOM      = 3;

//	public static final int MD_NODETAIL        = 0;
	public static final int MD_SHOWDETAIL      = 1;

//	private String selectScript = "";
//	private String callScript = "";
	
	public String relationName;
	public String camelName;
	public String childSql;
	public String detailMeaning;
	public boolean masterDetailView;
	public TableDefinition parent;
	public TableDefinition child;
	public FieldType[] parentKeys;
	public FieldType[] childKeys;
	
	public TableRelation(String relationName, TableDefinition parent, TableDefinition child, FieldType[] parentKeys, FieldType[] childKeys) {
		this.relationName = relationName;
		this.camelName = Utils.camelCase(relationName);
		this.parent = parent;
		this.child = child;
		this.parentKeys = parentKeys;
		this.childKeys = childKeys;
		this.detailMeaning = relationName;
		this.masterDetailView = true;
		String w = " WHERE " + childKeys[0].fieldName + " = ?";
		for (int i = 1; i < childKeys.length; i++) {
			w = " AND " + childKeys[i].fieldName + " = ?";
		}
		this.childSql = "SELECT * FROM " + child.getTableName() + w + child.getOrderBy();
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("camelName", camelName);
		o.put("relationName", relationName);
		o.put("child", child.toJson());
		JSONArray pk = new JSONArray();
		for (int i = 0; i < parentKeys.length; i++) {
			pk.put(parentKeys[i].toJson());
		}
		o.put("parentKeys", pk);
		JSONArray ck = new JSONArray();
		for (int i = 0; i < childKeys.length; i++) {
			ck.put(childKeys[i].toJson());
		}
		o.put("childKeys", ck);
		return o;
	}
	
	public void setDetailMeaning(String detailMeaning) {
		this.detailMeaning = detailMeaning;
	}
	
	public void disableMasterDetailView() {
		this.masterDetailView = false;
	}

	public boolean isMasterDetailView() {
		return masterDetailView;
	}
}