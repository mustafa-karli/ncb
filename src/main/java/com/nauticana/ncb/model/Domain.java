package com.nauticana.ncb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Domain {
	private String id;
	private String caption;
	private int size;
	private String sortBy;
	Map<String, String> options = new HashMap<String, String>();
	Map<String, Map<String, String>> languageOptions = new HashMap<String, Map<String, String>>();
	
	
	public Domain(String id, String caption, int size, String sortBy) {
		super();
		this.id = id;
		this.caption = caption;
		this.size = size;
		this.sortBy = sortBy;
//		options.put("", " - ");
	}

	public String getId() {
		return id;
	}

	public String getCaption() {
		return caption;
	}

	public String getSortBy() {
		return sortBy;
	}

	public int size() {
		return size;
	}

	public void addOption(String value,String caption) {
		options.put(value, caption);
	}
	
	public Map<String, String> getOptions() {
		return options;
	}

	public Map<String, String> getOptions(PortalLanguage language) {
		Map<String, String> o = languageOptions.get(language.code);
		if (o != null) return o;
		o = new LinkedHashMap<String, String>();
		o.put("", "");
		for (String key : options.keySet()) {
			o.put(key, language.getText(options.get(key)));
		}
		List<Entry<String, String>> ol = new ArrayList<Entry<String, String>>(o.entrySet());
		if ("K".equals(this.sortBy))
			Collections.sort(ol, new MapKeyComparator());
		else
			Collections.sort(ol, new MapValueComparator());
		o.clear();
		for (Entry<String, String> entry : ol) {
			o.put(entry.getKey(), entry.getValue());
		}
		languageOptions.put(language.code, o);
		return o;
	}

	public void println() {
		System.out.println("DOMAIN: " + id + " KEY SIZE: " + size);
		for (String s:options.keySet()) {
			System.out.println("  " + s + " -> " + options.get(s));
		}
	}

	public void println(PortalLanguage l) {
		System.out.println("DOMAIN: " + id + " KEY SIZE: " + size + " LANGUAGE: " + l.caption);
		Map<String, String> o = languageOptions.get(l.code);
		for (String s:o.keySet()) {
			System.out.println("  " + s + " -> " + o.get(s));
		}
	}
	
	public void clear() {
		options.clear();
		languageOptions.clear();
	}
}
