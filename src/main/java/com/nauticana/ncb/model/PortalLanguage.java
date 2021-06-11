package com.nauticana.ncb.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Locale;

public class PortalLanguage {

	private static final String sep = System.getProperty("line.separator");
	private static PrintWriter writer = null;
	public static boolean showIconText = false;

	public String code;
	public String caption;
	public String localeStr;
	public Locale locale;
	
	
	public Hashtable<String, String> translations;
	public Hashtable<String, String> iconText;
	public Hashtable<String, String> icon;
	
	public PortalLanguage(String code, String caption, String localeStr) throws IOException {
		this.code = code;
		this.caption = caption;
		this.localeStr = localeStr;
		String[] sl = localeStr.split("_");
		this.locale = new Locale(sl[0], sl[1]);
		this.translations = new Hashtable<String, String>(); 
		this.iconText = new Hashtable<String, String>(); 
		this.icon = new Hashtable<String, String>(); 
		if (writer == null)
		writer = new PrintWriter(new FileWriter(new File("MissingTranslations.log")));
	}
	
	public static void insertMissing(String code, String caption) {
		writer.write(code + " " + caption + sep);
		writer.flush();
	}
	
	public void loadTranslations(Connection conn) {
		String sql ="SELECT CAPTION, LABELLOWER FROM CAPTION_TRANSLATIONS WHERE LANGUAGE='" + this.code + "' ORDER BY 1";
		Statement stmt1 = null;
		ResultSet rs1 = null;
		try {
			stmt1 = conn.createStatement();
			rs1 = stmt1.executeQuery(sql);
			translations.clear();
			iconText.clear();
			icon.clear();

			while (rs1.next()) {
				String baslik = rs1.getString(1);
				String text   = rs1.getString(2);
				translations.put(baslik, text);
				String nb = baslik + "_ICON";
				String icon = Icons.getIcon(nb);
//				System.out.println("ICON for " + nb + " is " + icon);
				if (icon != null) {
					this.iconText.put(baslik, "<i class=\"" + icon + "\"> " + text + " </i>");
					this.icon.put(baslik, "<i class=\"" + icon + "\"></i>");
				}
			}
			rs1.close();
			rs1 = null;
			stmt1.close();
			stmt1 = null;
		} catch (Exception e) {
			System.out.println(sql);
			e.printStackTrace();
		} finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs1 = null;
			}
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stmt1 = null;
			}
		}
	}
	
	public String getTextUpper(String caption) {
		String text = translations.get(caption);
		if (Utils.emptyStr(text)) {
			insertMissing(code, caption);
			text = caption;
		}
		return text.toUpperCase(locale);
	}

	public String getText(String caption) {
		String text = translations.get(caption);
		if (Utils.emptyStr(text)) {
			insertMissing(code, caption);
			text = caption;
		}
		return text;
	}

	public String getIconText(String caption) {
		String text = iconText.get(caption);
		if (Utils.emptyStr(text)) return getText(caption);
		return text;
	}

	public String getIcon(String caption) {
		String text = icon.get(caption);
		if (Utils.emptyStr(text)) return getText(caption);
		return text;
	}

	public String getLocaleStr() {
		return localeStr;
	}

	public void setLocaleStr(String localeStr) {
		this.localeStr = localeStr;
	}

}
