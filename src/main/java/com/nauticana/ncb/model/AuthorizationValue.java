package com.nauticana.ncb.model;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationValue {
	
	public static String X = "X";
	Map<String, Map<String, Map<String, String>>> objectTypes = new HashMap<String, Map<String, Map<String, String>>>();
	
	public Map<String, String> getAllowedValues(String objectType, String action) {
		Map<String, Map<String, String>> oType= objectTypes.get(objectType);
		if (oType == null) return null;
		return oType.get(action);
	}

	public void put(String objectType, String action, String keyValue) {
    	Map<String, Map<String, String>> o = objectTypes.get(objectType);
    	if (o == null) {
    		o = new HashMap<String, Map<String, String>>();
    		objectTypes.put(objectType, o);
    	}
    	Map<String, String> a = o.get(action);
    	if (a == null) {
    		a = new HashMap<String, String>();
    		o.put(action, new HashMap<String, String>());
    	}
		a.put(keyValue, X);
	}
	
    public boolean authorityChk(String objectType, String action, String keyValue) {
    	Map<String, Map<String, String>> o = objectTypes.get(objectType);
    	if (o == null) return false;
    	Map<String, String> a = o.get(action);
    	if (a == null) return false;
    	String s = a.get("*");
    	if (Utils.emptyStr(s))
    		s = a.get(keyValue);
    	return Utils.emptyStr(s);
	}
    
    public boolean authorityChk(String objectType, String action) {
    	Map<String, Map<String, String>> o = objectTypes.get(objectType);
    	if (o == null) return false;
    	Map<String, String> a = o.get(action);
    	if (a == null) return false;
    	return true;
	}
    
    public void clear( ) {
    	objectTypes.clear();
    }
}
