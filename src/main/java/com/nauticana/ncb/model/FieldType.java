package com.nauticana.ncb.model;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.json.JSONException;

import org.json.JSONObject;

public class FieldType implements Serializable {

	private static final long serialVersionUID = 7145960328577210114L;
	public static final int T_BYTE  = 1;
	public static final int T_SHORT = 2;
	public static final int T_INT   = 3;
	public static final int T_LONG  = 4;
	public static final int T_FLT   = 5;
	public static final int T_STR   = 6;
	public static final int T_DATE  = 7;
	
	public static final String [] JAVA_NAMES = {"unknown", "byte",   "short",  "int",     "long",   "double", "String", "Date"}; 
	public static final String [] JSON_NAMES = {"unknown", "number", "number", "number",  "number", "number", "string", "Date"}; 
	
	public String					fieldName;
	public String					camelName;
	private int						type;
	private int						size;
	private int						scale;
	private int						order;
	private boolean					required			= false;
	private boolean					translated			= false;
	private boolean					keyField			= false;
	private String					editStyle			= null;
	private String					editJstlPath		= null;
	private String					viewJstlPath		= null;
	private String					searchStyle			= null;
	private String					lookupStyle			= null;
	private String					minValue			= null;
	private String					maxValue			= null;
	private Method					idMethod			= null;
	private Method					getMethod			= null;
	private Method					setMethod			= null;
	public boolean					compositeId			= false;
	public boolean					compositeObj		= false;
	
	public FieldType(String fieldName, int type, int size, int scale, int order, boolean required) {
		super();
		this.fieldName = fieldName.toUpperCase();
		this.camelName = Utils.camelCase(this.fieldName);
		this.type = type;
		this.size = size;
		this.scale = scale;
		this.order = order;
		this.required = required;
		this.translated = fieldName.equals("CAPTION");
	}
	
	public int getType() {
		switch (type) {
		case java.sql.Types.CHAR        : return T_STR;
		case java.sql.Types.VARCHAR     : return T_STR;
		case java.sql.Types.VARBINARY   : return T_STR;
		case java.sql.Types.NCHAR       : return T_STR;
		case java.sql.Types.NVARCHAR    : return T_STR;
		case java.sql.Types.LONGVARCHAR : return T_STR;
		case java.sql.Types.LONGNVARCHAR: return T_STR;
		case java.sql.Types.DATE        : return T_DATE;
		case java.sql.Types.TIME        : return T_DATE;
		case java.sql.Types.TIMESTAMP   : return T_DATE;
		case java.sql.Types.NUMERIC     : return T_FLT;
		case java.sql.Types.DECIMAL     : return T_FLT;
		case java.sql.Types.FLOAT       : return T_FLT;
		case java.sql.Types.REAL        : return T_FLT;
		case java.sql.Types.DOUBLE      : return T_FLT;
		case java.sql.Types.TINYINT     : return T_BYTE;
		case java.sql.Types.SMALLINT    : return T_SHORT;
		case java.sql.Types.INTEGER     : return T_INT;
		case java.sql.Types.BIGINT      : return T_LONG;
		default: return 0;
		}
	}
	
	public static int getType(int sqlType) {
		switch (sqlType) {
		case java.sql.Types.CHAR        : return T_STR;
		case java.sql.Types.VARCHAR     : return T_STR;
		case java.sql.Types.VARBINARY   : return T_STR;
		case java.sql.Types.NCHAR       : return T_STR;
		case java.sql.Types.NVARCHAR    : return T_STR;
		case java.sql.Types.LONGVARCHAR : return T_STR;
		case java.sql.Types.LONGNVARCHAR: return T_STR;
		case java.sql.Types.DATE        : return T_DATE;
		case java.sql.Types.TIME        : return T_DATE;
		case java.sql.Types.TIMESTAMP   : return T_DATE;
		case java.sql.Types.NUMERIC     : return T_FLT;
		case java.sql.Types.DECIMAL     : return T_FLT;
		case java.sql.Types.FLOAT       : return T_FLT;
		case java.sql.Types.REAL        : return T_FLT;
		case java.sql.Types.DOUBLE      : return T_FLT;
		case java.sql.Types.TINYINT     : return T_INT;
		case java.sql.Types.BIGINT      : return T_INT;
		case java.sql.Types.INTEGER     : return T_INT;
		case java.sql.Types.SMALLINT    : return T_INT;
		default: return 0;
		}
	}
	
	public String getJavaType() {
		try {
			return JAVA_NAMES[getType()];
		} catch (Exception e) {
			return "";
		}
	}

	public String getJsonType() {
		try {
			return JSON_NAMES[getType()];
		} catch (Exception e) {
			return "";
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public int getSize() {
		return size;
	}

	public int getScale() {
		return scale;
	}

	public int getOrder() {
		return order;
	}

	public String getRequired() {
		if (required) return Labels.REQUIRED;
		return "";
	}

	public boolean isRequired() {
		return required;
	}

	public boolean isTranslated() {
		return translated;
	}

	public void setTranslated(boolean translated) {
		this.translated = translated;
	}

	public String getEditStyle() {
		return editStyle;
	}

	public void setEditStyle(String editStyle) {
		this.editStyle = editStyle;
		String[] sl = editStyle.split(".");
		if (sl.length > 1) {
			if (sl[0].equals("id"))
				compositeId = true;
			else
				compositeObj = true;				
		}
	}

	public String getEditJstlPath() {
		return editJstlPath;
	}

	public void setEditJstlPath(String editJstlPath) {
		this.editJstlPath = editJstlPath;
	}

	public String getViewJstlPath() {
		return viewJstlPath;
	}

	public void setViewJstlPath(String viewJstlPath) {
		this.viewJstlPath = viewJstlPath;
	}

	public String getSearchStyle() {
		return searchStyle;
	}

	public void setSearchStyle(String searchStyle) {
		this.searchStyle = searchStyle;
	}

	public String getLookupStyle() {
		return lookupStyle;
	}

	public void setLookupStyle(String lookupStyle) {
		this.lookupStyle = lookupStyle;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public Method getGetMethod() {
		return getMethod;
	}

	public void setGetMethod(Method getMethod) {
		this.getMethod = getMethod;
	}

	public Method getSetMethod() {
		return setMethod;
	}

	public void setSetMethod(Method setMethod) {
		this.setMethod = setMethod;
	}

	public Method getIdMethod() {
		return idMethod;
	}

	public void setIdMethod(Method idMethod) {
		this.idMethod = idMethod;
	}

	public boolean isKeyField() {
		return keyField;
	}

	public void setKeyField(boolean keyField) {
		this.keyField = keyField;
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("fieldName",   fieldName);
		o.put("camelName",   camelName);
		o.put("type",        getType());
		o.put("size",        size);
		o.put("scale",       scale);
		o.put("order",       order);
		o.put("required",    required);
		o.put("translated",  translated);
		o.put("keyField",    keyField);
		o.put("editStyle",   editStyle);
		o.put("searchStyle", searchStyle);
		o.put("lookupStyle", lookupStyle);
		o.put("minValue",    minValue);
		o.put("maxValue",    maxValue);
		return o;
	}
	
}
