package com.nauticana.ncb.model;

import java.text.ParseException;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONException;

import org.json.JSONArray;
import org.json.JSONObject;

public class TableDefinition {

	private String							tableName;
	private String							camelName;
	private String							TitleName;
	private String							module;
	private Class<Object>					modelBeanClass;
	private Class<Object>					modelIdClass;
	private String							listView;
	private String							searchView;
	private String							editView;
	private String							showView;
	private String							selectview;
	private String							sequenceName;
	private String							orderBy;
	private FieldType[]						keys;
	private FieldType[]						fields;
//	private TableDetail[]					details					= null;
	private TableContentType[]				contentTypes			= null;
	private TableNavAction[]				actions					= null;
	private boolean							cashInHash;
	private boolean							clientDependent;
	private boolean							organizationDependent;
	private String							findAllSql				= null;
	private String							findByIdSql				= null;
	private String							findByClientSql			= null;
	private String							findByOrganizationSql	= null;
	private String							findForLookupSql		= null;
	private String							insertSql				= null;
	private String							updateSql				= null;
	private String							deleteSql				= null;
	private Hashtable<String, TableRelation>	parents					= new Hashtable<String, TableRelation>();
	private Hashtable<String, TableRelation>	children				= new Hashtable<String, TableRelation>();
	private Hashtable<String, String>		cache					= new Hashtable<String, String>();

	public TableDefinition(String tableName, String module, Class<Object> modelBeanClass, Class<Object> modelIdClass, String listView, String searchView, String editView, String showView, String selectview, String orderBy,
			String sequenceName, boolean clientDependent, boolean organizationDependent, boolean cashInHash) {
		super();
		this.tableName = tableName;
		this.camelName = Utils.camelCase(tableName);
		this.TitleName = Utils.titleCase(tableName);
		this.module = module;
		this.modelBeanClass = modelBeanClass;
		this.modelIdClass = modelIdClass;
		this.listView = listView;
		this.searchView = searchView;
		this.editView = editView;
		this.showView = showView;
		this.selectview = selectview;
		this.orderBy = orderBy;
		this.sequenceName = sequenceName;
		this.cashInHash = cashInHash;
		this.clientDependent = clientDependent;
		this.organizationDependent = organizationDependent;
	}
	
	public void setStaticSql() {
		this.findAllSql = "SELECT * FROM " + tableName;
		if (clientDependent)
			this.findByClientSql = this.findAllSql + " WHERE OWNER_ID = ?" + getOrderBy();
		if (organizationDependent)
			this.findByOrganizationSql = this.findAllSql + " WHERE OWNER_ID = ? AND ORGANIZATION_ID = ?" + getOrderBy();
		String s = " WHERE " + fields[0].fieldName + " = ?";
		int i = 1;
		while (i < fields.length && fields[i].isKeyField()) {
			s = s + " AND " + fields[i].fieldName + " = ?";
			i++;
		}
		this.findByIdSql = this.findAllSql + s + getOrderBy();
		this.findAllSql = this.findAllSql + getOrderBy();

		s = "INSERT INTO " + tableName + " (" + fields[0].fieldName;
		String l = "(?";

		for (int j = 1; j < fields.length; j++) {
			s = s + ", " + fields[j].fieldName;
			l = l + ", ?";
		}

		this.insertSql = s + ") VALUES " + l + ")";

		s = "";
		l = " WHERE " + fields[0].fieldName + " = ?";
		boolean first = true;
		for (int j = 1; j < fields.length; j++) {
			if (fields[j].isKeyField()) {
				l = l + " AND " + fields[j].fieldName + " = ?";
			} else if (first) {
				s = "UPDATE " + tableName + " SET " + fields[0].fieldName + " = ?";
				first = false;
			} else {
				s = s + ", " + fields[0].fieldName + " = ?";
			}
		}
		this.updateSql = s + l;
		this.deleteSql = "DELETE FROM " + tableName + l;
	}

	public String getTableName() {
		return tableName;
	}

	public String getCamelName() {
		return camelName;
	}

	public String getTitleName() {
		return TitleName;
	}

	public String getModule() {
		return module;
	}

	public Class<Object> getModelBeanClass() {
		return modelBeanClass;
	}

	public Class<Object> getModelIdClass() {
		return modelIdClass;
	}

	public String getListView() {
		return listView;
	}

	public void setListView(String listView) {
		this.listView = listView;
	}

	public String getSearchView() {
		return searchView;
	}

	public void setSearchView(String searchView) {
		this.searchView = searchView;
	}

	public String getEditView() {
		return editView;
	}

	public void setEditView(String editView) {
		this.editView = editView;
	}

	public String getShowView() {
		return showView;
	}

	public void setShowView(String showView) {
		this.showView = showView;
	}

	public String getSelectview() {
		return selectview;
	}

	public void setSelectview(String selectview) {
		this.selectview = selectview;
	}

	public FieldType[] getKeys() {
		return keys;
	}

	public void setKeys(FieldType[] keys) {
		this.keys = keys;
	}

	public FieldType[] getFields() {
		return fields;
	}

	public void setFields(FieldType[] fields) {
		this.fields = fields;
	}

	public boolean isCashInHash() {
		return cashInHash;
	}

	public void setCashInHash(boolean cashInHash) {
		this.cashInHash = cashInHash;
	}

	public boolean isClientDependent() {
		return clientDependent;
	}

	public void setClientDependent(boolean clientDependent) {
		this.clientDependent = clientDependent;
	}

	public boolean isOrganizationDependent() {
		return organizationDependent;
	}

	public void setOrganizationDependent(boolean organizationDependent) {
		this.organizationDependent = organizationDependent;
	}

	public String getFindAllSql() {
		return findAllSql;
	}

	public void setFindAllSql(String findAllSql) {
		this.findAllSql = findAllSql;
	}

	public String getFindByIdSql() {
		return findByIdSql;
	}

	public void setFindByIdSql(String findByIdSql) {
		this.findByIdSql = findByIdSql;
	}

	public String getFindByClientSql() {
		return findByClientSql;
	}

	public void setFindByClientSql(String findByClientSql) {
		this.findByClientSql = findByClientSql;
	}

	public String getFindByOrganizationSql() {
		return findByOrganizationSql;
	}

	public void setFindByOrganizationSql(String findByOrganizationSql) {
		this.findByOrganizationSql = findByOrganizationSql;
	}

	public String getFindForLookupSql() {
		return findForLookupSql;
	}

	public void setFindForLookupSql(String findForLookupSql) {
		this.findForLookupSql = findForLookupSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	public String getDeleteSql() {
		return deleteSql;
	}

	public void setDeleteSql(String deleteSql) {
		this.deleteSql = deleteSql;
	}

	public Hashtable<String, TableRelation> getParents() {
		return parents;
	}

	public void setParents(Hashtable<String, TableRelation> parents) {
		this.parents = parents;
	}

	public Hashtable<String, TableRelation> getChildren() {
		return children;
	}

	public void setChildren(Hashtable<String, TableRelation> children) {
		this.children = children;
	}

	public Hashtable<String, String> getCache() {
		return cache;
	}

	public void setCache(Hashtable<String, String> cache) {
		this.cache = cache;
	}

	public TableContentType[] getContentTypes() {
		return contentTypes;
	}

	public void setContentTypes(List<String[]> contentTypes) {
		this.contentTypes = new TableContentType[contentTypes.size()];
		for (int i = 0; i < contentTypes.size(); i++) {
			this.contentTypes[i] = new TableContentType();
			this.contentTypes[i].objectType = contentTypes.get(i)[0];
			this.contentTypes[i].caption = contentTypes.get(i)[1];
			this.contentTypes[i].mimetype = contentTypes.get(i)[2];
		}
	}

	public TableNavAction[] getActions() {
		return actions;
	}

	public void setActions(List<String[]> actions) {
		this.actions = new TableNavAction[actions.size()];
		for (int i = 0; i < actions.size(); i++) {
			this.actions[i] = new TableNavAction();
			this.actions[i].action = actions.get(i)[0];
			this.actions[i].caption = actions.get(i)[1];
			this.actions[i].method = actions.get(i)[2];
			this.actions[i].enable = actions.get(i)[3];
			this.actions[i].authorityCheck = actions.get(i)[4];
			this.actions[i].recordSpecific = actions.get(i)[5];
			this.actions[i].longName = tableName + "_" + this.actions[i].action + "_ALLOWED";
		}
	}

//	public TableDetail[] getDetails() {
//		return details;
//	}

//	public void setDetails(List<String[]> details, List<TableDefinition> detailStatics) {
//		this.details = new TableDetail[details.size()];
//		for (int i = 0; i < details.size(); i++) {
//			this.details[i] = new TableDetail();
//			this.details[i].detailAttribute = details.get(i)[0];
//			this.details[i].detailTable = details.get(i)[1];
//			this.details[i].enable = details.get(i)[2].charAt(0);
//			this.details[i].paging = details.get(i)[3];
//			this.details[i].filter = details.get(i)[4];
//			this.details[i].order = details.get(i)[5];
//			this.details[i].detailStatic = detailStatics.get(i);
//		}
//	}

	public void loadCashInHash() {
	}

	public void addParent(TableRelation parentRelation) {
		parents.put(parentRelation.relationName, parentRelation);
	}

	public void addChild(TableRelation childRelation) {
		children.put(childRelation.relationName, childRelation);
	}

	public String getURL() {
		if (cashInHash)
			return listView + "?TABNAME=" + tableName;
		else
			return searchView + "?TABNAME=" + tableName;
	}

	public String getOrderBy() {
		return this.orderBy;
	}

	public String getSequenceName() {
		return this.sequenceName;
	}

	public FieldType getField(String fieldName) {
		for (int i = 0; i < fields.length; i++) {
			if (fieldName.equals(fields[i].fieldName)) {
				return fields[i];
			}
		}
		return null;
	}

	public Object[] strToId(String id) throws ParseException {
		if (Utils.emptyStr(id)) return null;
		String[] values = id.split(",");
		if (values.length != keys.length)
			return null;
		Object[] keyList = new Object[keys.length];
		for (int i = 0; i < keys.length; i++) {
			switch (keys[i].getType()) {
				case FieldType.T_SHORT:
					keyList[i] = Short.parseShort(values[i]);
					break;
				case FieldType.T_INT:
					keyList[i] = Integer.parseInt(values[i]);
					break;
				case FieldType.T_LONG:
					keyList[i] = Long.parseLong(values[i]);
					break;
				case FieldType.T_FLT:
					keyList[i] = Double.parseDouble(values[i]);
					break;
				case FieldType.T_DATE:
					keyList[i] = Labels.dmyDF.parse(values[i]);
					break;
				default:
					keyList[i] = values[i];
			}
		}
		return keyList;
	}

	public String idToStr(Object[] id) {
		String s = "";
		for (int i = 0; i < keys.length; i++) {
			switch (keys[i].getType()) {
				case FieldType.T_SHORT:
					s = s + "," + id[i];
					break;
				case FieldType.T_INT:
					s = s + "," + id[i];
					break;
				case FieldType.T_LONG:
					s = s + "," + id[i];
					break;
				case FieldType.T_FLT:
					s = s + "," + id[i];
					break;
				case FieldType.T_DATE:
					s = s + "," + Labels.dmyDF.format(id[i]);
					break;
				default:
					s = s + "," + id[i];
			}
		}
		return s.substring(1);
	}
	
	public String typeScriptClass(String baseClass) {
		String sep = System.lineSeparator();
		String b = "";
		if (!Utils.emptyStr(baseClass)) b = " extends " + baseClass;
		String s = "export interface " + Utils.titleCase(tableName) + b + " {" + sep;
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isRequired())
				s = s + "    " + fields[i].camelName + ": " + "                              ".substring(fields[i].camelName.length()) + fields[i].getJsonType() + ";" + sep;
			else
				s = s + "    " + fields[i].camelName + "?:" + "                              ".substring(fields[i].camelName.length()) + fields[i].getJsonType() + ";" + sep;
		}
		for (String key : parents.keySet()) {
			TableRelation relation = parents.get(key);
			s = s + "    " + relation.parent.camelName + "?:" + "                              ".substring(relation.parent.camelName.length()) + relation.parent.TitleName + ";" + sep;
		}
		for (String key : children.keySet()) {
			TableRelation relation = children.get(key);
			s = s + "    " + relation.camelName + "?:" + "                              ".substring(relation.camelName.length()) + relation.child.TitleName + "[];" + sep;
		}
		return s + "}" + sep;
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("camelName", camelName);
		o.put("tableName", tableName);
		o.put("titleName", TitleName);
		o.put("module", module);
		o.put("orderBy", orderBy);
		o.put("clientDependent", clientDependent);
		o.put("organizationDependent", organizationDependent);
		o.put("cashInHash", cashInHash);

		JSONArray pk = new JSONArray();
		for (int i = 0; i < keys.length; i++) {
			pk.put(keys[i].toJson());
		}
		o.put("keys", pk);

		JSONArray fk = new JSONArray();
		for (int i = 0; i < fields.length; i++) {
			fk.put(fields[i].toJson());
		}
		o.put("fields", fk);

		if (actions != null && actions.length>0) {
			JSONArray ac = new JSONArray();
			for (int i = 0; i < actions.length; i++) {
				JSONObject a = new JSONObject();
				a.put("tablename", tableName);
				a.put("action", actions[i].action);
				a.put("caption", actions[i].caption);
				a.put("method", actions[i].method);
				a.put("enable", actions[i].enable);
				a.put("authorityCheck", actions[i].authorityCheck);
				a.put("recordSpecific", actions[i].recordSpecific);
				ac.put(a);
			}
			o.put("actions", ac);
		}

		if (contentTypes != null && contentTypes.length>0) {
			JSONArray ct = new JSONArray();
			for (int i = 0; i < contentTypes.length; i++) {
				JSONObject a = new JSONObject();
				a.put("tablename", tableName);
				a.put("objectType", contentTypes[i].objectType);
				a.put("caption", contentTypes[i].caption);
				a.put("mimetype", contentTypes[i].mimetype);
				ct.put(a);
			}
			o.put("contentTypes", ct);
		}

		return o;
	}
	
}
