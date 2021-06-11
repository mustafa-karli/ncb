package com.nauticana.ncb.model;

import java.util.HashMap;

public class Icons {

	public static final String ADMINISTRATION    = "ADMINISTRATION_ICON";
	public static final String ADMINISTRATOR     = "ADMINISTRATOR_ICON";
	public static final String ALLOW_DELETE      = "ALLOW_DELETE_ICON";
	public static final String ALLOW_INSERT      = "ALLOW_INSERT_ICON";
	public static final String ALLOW_SELECT      = "ALLOW_SELECT_ICON";
	public static final String ALLOW_UPDATE      = "ALLOW_UPDATE_ICON";
	public static final String AUTHORITY_GROUPS  = "AUTHORITY_GROUPS_ICON";
	public static final String AUTHORS           = "AUTHORS_ICON";
	public static final String OK                = "OK_ICON";
	public static final String CANCEL            = "CANCEL_ICON";
	public static final String CHANGE_PASSWORD   = "CHANGE_PASSWORD_ICON";
	public static final String CHILD             = "CHILD_ICON";
	public static final String CHILD_TABLE       = "CHILD_TABLE_ICON";
	public static final String CHILDREN          = "CHILDREN_ICON";
	public static final String CHOOSE            = "CHOOSE_ICON";
	public static final String DATE              = "DATE_ICON";
	public static final String DAYS              = "DAYS_ICON";
	public static final String DELETE            = "DELETE_ICON";
	public static final String DELETION          = "DELETION_ICON";
	public static final String DESCRIPTION       = "DESCRIPTION_ICON";
	public static final String DURATION          = "DURATION_ICON";
	public static final String EDIT              = "EDIT_ICON";
	public static final String EMPLOYEE          = "EMPLOYEE_ICON";
	public static final String FAX               = "FAX_ICON";
	public static final String FIND              = "FIND_ICON";
	public static final String HELP              = "HELP_ICON";
	public static final String INSERT            = "INSERT_ICON";
	public static final String LANGUAGES         = "LANGUAGES_ICON";
	public static final String LIST              = "LIST_ICON";
	public static final String LOGIN             = "LOGIN_ICON";
	public static final String LOGOFF            = "LOGOFF_ICON";
	public static final String MAIN_PAGE         = "MAIN_PAGE_ICON";
	public static final String NEW_RECORD        = "NEW_RECORD_ICON";
	public static final String NEW               = "NEW_ICON";
	public static final String NO                = "NO_ICON";
	public static final String ORDER             = "ORDER_ICON";
	public static final String ORDERBY           = "ORDERBY_ICON";
	public static final String PASSTEXT          = "PASSTEXT_ICON";
	public static final String PASSWORD          = "PASSWORD_ICON";
	public static final String PEOPLE            = "PEOPLE_ICON";
	public static final String PHONE             = "PHONE_ICON";
	public static final String PHONEBOOK         = "PHONEBOOK_ICON";
	public static final String READ              = "READ_ICON";
	public static final String SAVE              = "SAVE_ICON";
	public static final String SEARCH            = "SEARCH_ICON";
	public static final String SELECT            = "SELECT_ICON";
	public static final String SEQUENCE          = "SEQUENCE_ICON";
	public static final String TABLE             = "TABLE_ICON";
	public static final String TABLES            = "TABLES_ICON";
	public static final String TEXT              = "TEXT_ICON";
	public static final String TRAINING          = "TRAINING_ICON";
	public static final String TREE              = "TREE_ICON";
	public static final String UPDATE            = "UPDATE_ICON";
	public static final String USERNAME          = "USERNAME_ICON";
	public static final String WRITE             = "WRITE_ICON";
	public static final String WRONG_PASSWORD    = "WRONG_PASSWORD_ICON";
	public static final String YES               = "YES_ICON";
	public static final String APPROVE           = "APPROVE_ICON";
	public static final String COMPLETE          = "COMPLETE_ICON";
	public static final String DETAILS           = "DETAILS_ICON";
	

	public static final String MANHOUR           = "MANHOUR_ICON";
	public static final String PROJECT           = "PROJECT_ICON";
	public static final String CATEGORY          = "CATEGORY_ICON";
	public static final String SUBCONTRACTOR     = "SUBCONTRACTOR_ICON";
	public static final String PROJECT_MANHOUR   = "PROJECT_MANHOUR_ICON";
	public static final String PROJECT_MANHOUR_1 = "PROJECT_MANHOUR_1_ICON";
	public static final String PROJECT_MANHOUR_N = "PROJECT_MANHOUR_N_ICON";
	public static final String PROJECT_QUANTITY  = "PROJECT_QUANTITY_ICON";
	public static final String REPORT            = "REPORT_ICON";
	public static final String SETTINGS          = "SETTINGS_ICON";
	public static final String AUTHORITY_GROUP   = "AUTHORITY_GROUP_ICON";
	public static final String LANGUAGE          = "LANGUAGE_ICON";
	public static final String MENU              = "MENU_ICON";
	public static final String USER_ACCOUNT      = "USER_ACCOUNT_ICON";
	public static final String DOMAIN            = "DOMAIN_ICON";

	
	private static final HashMap<String, String> favicons = loadFavIcons();
	
//	private static Icons instance = null;
//	
//	public static Icons getInstance() {
//		if (instance == null) {
//			instance = new Icons();
//		}
//		return instance;
//	}
//	
//	private Icons() {
//	}
//	

	private static HashMap<String, String> loadFavIcons() {
		HashMap<String, String> favicons = new HashMap<String, String>();
		favicons.put(ADMINISTRATION, "fa fa-gears");
		favicons.put(ADMINISTRATOR, "fa fa-gear");
		favicons.put(ALLOW_DELETE, "fa fa-minus");
		favicons.put(ALLOW_INSERT, "fa fa-plus");
		favicons.put(ALLOW_SELECT, "fa fa-file-text");
		favicons.put(ALLOW_UPDATE, "fa fa-check");
		favicons.put(AUTHORITY_GROUP, "fa fa-users");
		favicons.put(AUTHORITY_GROUPS, "fa fa-users");
		favicons.put(AUTHORS, "fa fa-group");
		favicons.put(OK, "fa fa-check");
		favicons.put(CANCEL, "fa fa-times");
		favicons.put(CHANGE_PASSWORD, "fa fa-unlock");
		favicons.put(CHILD, "fa fa-child");
		favicons.put(CHILD_TABLE, "fa fa-child");
		favicons.put(CHILDREN, "fa fa-child");
		favicons.put(CHOOSE, "fa fa-hand-rock-o");
		favicons.put(DATE, "fa fa-calendar");
		favicons.put(DAYS, "fa fa-calendar");
		favicons.put(DELETE, "fa fa-trash");
		favicons.put(DELETION, "fa fa-minus");
		favicons.put(DESCRIPTION, "fa fa-file-text-o");
		favicons.put(DURATION, "fa fa-hourglass");
		favicons.put(EDIT, "fa fa-pencil");
		favicons.put(EMPLOYEE, "fa fa-male");
		favicons.put(FAX, "fa fa-fax");
		favicons.put(FIND, "fa fa-search");
		favicons.put(HELP, "fa fa-question");
		favicons.put(INSERT, "fa fa-plus");
		favicons.put(LANGUAGE, "fa fa-language");
		favicons.put(LANGUAGES, "fa fa-language");
		favicons.put(LIST, "fa fa-file-text-o");
		favicons.put(LOGIN, "fa fa-key");
		favicons.put(LOGOFF, "fa fa-power-off");
		favicons.put(MAIN_PAGE, "fa fa-DEFAULT_HOME");
		favicons.put(MENU, "fa fa-bars");
		favicons.put(NEW_RECORD, "fa fa-plus");
		favicons.put(NEW, "fa fa-plus");
		favicons.put(NO, "fa fa-times");
		favicons.put(ORDER, "fa fa-sort-amount-asc");
		favicons.put(ORDERBY, "fa fa-sort-amount-asc");
		favicons.put(PASSTEXT, "fa fa-lock");
		favicons.put(PASSWORD, "fa fa-lock");
		favicons.put(PEOPLE, "fa fa-group");
		favicons.put(PHONE, "fa fa-phone");
		favicons.put(PHONEBOOK, "fa fa-book");
		favicons.put(READ, "fa fa-file-text-o");
		favicons.put(SAVE, "fa fa-check");
		favicons.put(SEARCH, "fa fa-search");
		favicons.put(SELECT, "fa fa-search");
		favicons.put(SEQUENCE, "fa fa-sort-amount-asc");
		favicons.put(SETTINGS, "fa fa-gear");
		favicons.put(TABLE, "fa fa-table");
		favicons.put(TABLES, "fa fa-table");
		favicons.put(TEXT, "fa fa-file-text-o");
		favicons.put(TRAINING, "fa fa-graduation-cap");
		favicons.put(TREE, "fa fa-sitemap");
		favicons.put(UPDATE, "fa fa-check");
		favicons.put(USERNAME, "fa fa-user");
		favicons.put(WRITE, "fa fa-pencil");
		favicons.put(WRONG_PASSWORD, "fa fa-lock");
		favicons.put(YES, "fa fa-check");
		favicons.put(USER_ACCOUNT, "fa fa-user");
		favicons.put(DOMAIN, "fa fa-server");
		favicons.put(APPROVE, "fa fa-thumbs-up");
		favicons.put(REPORT, "fa fa-file-text");
		favicons.put(COMPLETE, "fa fa-thumbs-up");
		favicons.put(DETAILS, "fa  fa-file-o");
		
		

		favicons.put(MANHOUR, "fa fa-calendar");
		favicons.put(CATEGORY, "fa fa-sitemap");
		favicons.put(PROJECT, "fa fa-building");
		favicons.put(SUBCONTRACTOR, "fa fa-male");
		favicons.put(PROJECT_MANHOUR, "fa fa-calendar-check-o");
		favicons.put(PROJECT_MANHOUR_1, "fa fa-calendar-check-o");
		favicons.put(PROJECT_MANHOUR_N, "fa fa-calendar-check-o");
		favicons.put(PROJECT_QUANTITY, "fa fa-industry");
		return favicons;
	}

	public static String getIcon(String iconid) {
		return favicons.get(iconid);
	}
}
