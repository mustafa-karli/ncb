package com.nauticana.ncb.model;

import java.util.Comparator;
import java.util.Map.Entry;

public class MapValueComparator implements Comparator<Entry<String, String>> {

	@Override
	public int compare(Entry<String, String> e1, Entry<String, String> e2) {
		return e1.getValue().compareTo(e2.getValue());
	}

}
