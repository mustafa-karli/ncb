package com.nauticana.ncb.model;

import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserMenu {

	private String menu;
	private String menuCaption;
	private String pageCaption;
	private String menuIcon;
	private String pageIcon;
	private String url;
	private int menuOrder;
	private int pageOrder;
	
	public UserMenu(String menu, String pageCaption, String pageIcon, String url, String menuCaption, String menuIcon, int menuOrder, int pageOrder) {
		super();
		this.menu = menu;
		this.menuCaption = menuCaption;
		this.pageCaption = pageCaption;
		this.menuIcon = menuIcon;
		this.pageIcon = pageIcon;
		this.url = url;
		this.menuOrder = menuOrder;
		this.pageOrder = pageOrder;
	}

	public String getPageCaption() {
		return pageCaption;
	}

	public void setPageCaption(String pageCaption) {
		this.pageCaption = pageCaption;
	}

	public String getPageIcon() {
		return pageIcon;
	}

	public void setPageIcon(String pageIcon) {
		this.pageIcon = pageIcon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getMenuCaption() {
		return menuCaption;
	}

	public void setMenuCaption(String menuCaption) {
		this.menuCaption = menuCaption;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public int getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}

	public int getPageOrder() {
		return pageOrder;
	}

	public void setPageOrder(int pageOrder) {
		this.pageOrder = pageOrder;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("menuCaption", menuCaption);
		o.put("pageCaption", pageCaption);
		o.put("menuIcon", menuIcon);
		o.put("pageIcon", pageIcon);
		o.put("url", url);
		return o;
	}

	public static JSONArray toJson(List<UserMenu> list) throws JSONException {
        JSONArray jMenus = new JSONArray();
        JSONObject jMenu = null;
        JSONArray jPages = null;
        String menuCaption = null;
        String menu = null;
        for (UserMenu userMenu : list) {
        	if (!userMenu.getMenu().equals(menu)) {
        		if (jMenu != null) {
        			jMenu.put("pages", jPages);
        			jMenus.put(jMenu);
        		}
        		jMenu = new JSONObject();
        		menu = userMenu.getMenu();
        		menuCaption = userMenu.getMenuCaption();
        		jMenu.put("menu", menu.toLowerCase(Locale.ENGLISH));
        		jMenu.put("menuCaption", menuCaption);
        		jMenu.put("menuIcon", userMenu.getMenuIcon());
        		jPages = new JSONArray();
        	}
            JSONObject jPage = new JSONObject();
            jPage.put("pageCaption", userMenu.getPageCaption());
            jPage.put("pageIcon", userMenu.getPageIcon());
            jPage.put("url", userMenu.getUrl());
            jPages.put(jPage);
        }
		if (jMenu != null) {
			jMenu.put("pages", jPages);
			jMenus.put(jMenu);
		}
		return jMenus;
	}
}
