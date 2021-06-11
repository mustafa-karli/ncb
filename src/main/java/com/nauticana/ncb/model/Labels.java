package com.nauticana.ncb.model;

import java.text.SimpleDateFormat;

public class Labels {

	// Common used label names
	public static final String APPLICATION_TITLE = "NAUTICANA_CLOUD_APPLICATIONS";
	
	// Labels for actions
	public static final String NEW               = "NEW";
	public static final String EDIT              = "EDIT";
	public static final String SAVE              = "SAVE";
	public static final String LIST              = "LIST";
	public static final String SEARCH            = "SEARCH";
	public static final String FIND              = "FIND";
	public static final String USERNAME          = "USERNAME";
	public static final String LANGUAGE          = "LANGUAGE";
	public static final String PAGETITLE         = "PAGETITLE";
	public static final String MENU              = "MENU";
	public static final String CHOOSE            = "CHOOSE";
	public static final String SELECT            = "SELECT";
	public static final String INSERT            = "INSERT";
	public static final String UPDATE            = "UPDATE";
	public static final String DELETE            = "DELETE";
	public static final String CONFIRM_DELETE    = "CONFIRM_DELETE";
	public static final String ACCESS            = "ACCESS";
	public static final String CANCEL            = "CANCEL";
	public static final String OK                = "OK";
	public static final String PREVPAGE          = "prevpage";
	public static final String NEXTPAGE          = "nextpage";
	public static final String TOTAL             = "TOTAL";
	public static final String POSTLINK          = "postlink";
	public static final String GETLINK           = "getlink";
	public static final String LAST              = "LAST";
	public static final String SELECT_ALL        = "SELECT_ALL";
	public static final String DE_SELECT         = "DE_SELECT";
	public static final String TOGGLE_SELECT     = "TOGGLE_SELECT";
	public static final String ADD_SIBLINGS      = "ADD_SIBLINGS";
	public static final String DESCRIPTION       = "DESCRIPTION";
	public static final String COMPLETE      	 = "COMPLETE";
	public static final String DETAILS      	 = "DETAILS";
	
	
	public static final String TABLE             = "TABLE";
	public static final String PAGE              = "PAGE";
	public static final String PROJECT           = "PROJECT";
	public static final String PROJECT_WBS       = "PROJECT_WBS";
	public static final String PROJECT_TEAM      = "PROJECT_TEAM";
	public static final String PROJECT_MANHOUR   = "PROJECT_MANHOUR";
	public static final String PROJECT_QUANTITY  = "PROJECT_QUANTITY";

	
	public static final String SELECT_ALLOWED    = "SELECT_ALLOWED";
	public static final String INSERT_ALLOWED    = "INSERT_ALLOWED";
	public static final String UPDATE_ALLOWED    = "UPDATE_ALLOWED";
	public static final String DELETE_ALLOWED    = "DELETE_ALLOWED";
	
	public static final String CODE              = "CODE";
	public static final String CAPTION           = "CAPTION";
	public static final String DATE              = "DATE";
	public static final String REPORTS           = "REPORTS";
	public static final String PERSONNEL         = "PERSONNEL";
	public static final String SUBCONTRACTOR     = "SUBCONTRACTOR";
	public static final String FOREIGN           = "FOREIGN";
	public static final String LOCALMH           = "LOCALMH";
	public static final String TRMH              = "TRMH";
	public static final String MANHOUR           = "MANHOUR";
	public static final String TENDER            = "TENDER";
	public static final String PROJECT_MANHOUR_1 = "PROJECT_MANHOUR_1";
	public static final String PROJECT_MANHOUR_N = "PROJECT_MANHOUR_N";
	public static final String PERSON_ID         = "PERSON_ID";
	public static final String SUBCONTRACTOR_ID  = "SUBCONTRACTOR_ID";
	public static final String BEGDA             = "BEGDA";
	public static final String ENDDA             = "ENDDA";
	public static final String PARENTKEY         = "parentKey";
	public static final String CAPTION_FILTER    = "CAPTION_FILTER";
	public static final String PROJECT_FILTER    = "PROJECT_FILTER";
	public static final String APPROVED_MANHOUR  = "APPROVED_MANHOUR";
	public static final String MANHOUR_TO_APPROVE= "MANHOUR_TO_APPROVE";
	public static final String PUP               = "PUP";
	public static final String PLANNED           = "PLANNED";
	public static final String REALISED          = "REALISED";
	public static final String LANGUAGE_DIRECTION= "LANGUAGE_DIRECTION";
	public static final String PERIOD            = "PERIOD";
	public static final String PROJECT_EXECUTION_PLAN = "PROJECT_EXECUTION_PLAN";
	public static final String CHANGE_PASSWORD   = "CHANGE_PASSWORD";

	public static final String INITIAL           = "INITIAL";
	public static final String APPROVE           = "APPROVE";
	public static final String APPROVE_ALL       = "APPROVE_ALL";
	public static final String APPROVE_WBS       = "APPROVE_WBS";
	public static final String APPROVE_FINAL     = "APPROVE_FINAL";
	public static final String APPROVE_WITHDRAW  = "APPROVE_WITHDRAW";
	public static final String APPROVE_QUANTITY  = "APPROVE_QUANTITY";
	public static final String APPROVE_MANHOUR   = "APPROVE_MANHOUR";
	public static final String APPROVE_PROGRESS  = "APPROVE_PROGRESS";
	public static final String APPROVED          = "APPROVED";
	public static final String TO_APPROVE        = "TO_APPROVE";
	public static final String REQUIRED          = "REQUIRED";
	public static final String MODEL_MAP         = "modelMap";

	public static final String ERR_UNAUTHORIZED      = "ERR_UNAUTHORIZED";
	public static final String ERR_WRONGDATA         = "ERR_WRONGDATA";
	public static final String ERR_PARAMETER_MISSING = "ERR_PARAMETER_MISSING";
	public static final String ERR_WRONG_PARAMETER   = "ERR_WRONG_PARAMETER";
	public static final String ERR_WORKERNOTFOUND    = "ERR_WORKERNOTFOUND";
	public static final String ERR_TEAMLEADNOTFOUND  = "ERR_TEAMLEADNOTFOUND";
	public static final String ERR_RECORDNOTFOUND    = "ERR_RECORDNOTFOUND";
	public static final String ERR_BINDING           = "ERR_BINDING";
	public static final String ERR_ALREADY_APPROVED  = "ERR_ALREADY_APPROVED";
	public static final String ERR_DATABASE_ERROR    = "DATABASE_ERROR";
	
	
	public static final String dmyDFStr          = "dd-MM-yyyy";
	public static final String ymdDFStr          = "yyyy-MM-dd";
	public static final String dmyFullStr        = "dd-MM-yyyy hh:mm:ss";
	public static final SimpleDateFormat dmyDF   = new SimpleDateFormat(dmyDFStr);
	public static final SimpleDateFormat ymdDF   = new SimpleDateFormat(ymdDFStr);
	public static final SimpleDateFormat fullDMYdf  = new SimpleDateFormat(dmyFullStr);
	public static final SimpleDateFormat fullYMDdf  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public static final String dataTableSetting1 =
			"<script> $(function () { $('#dataTable1').DataTable({'paging':false, 'scrollY': '50vh', 'lengthChange':false, 'searching':true, 'ordering':true, 'info':true, 'autoWidth': true, 'responsive': true, 'scrollCollapse': true }) }) </script>";
	
	public static final String dataTableSetting2 =
			"<script> $(function () { $('#dataTable2').DataTable({'paging':false, 'scrollY': '50vh', 'lengthChange':false, 'searching':true, 'ordering':true, 'info':true, 'autoWidth': true, 'responsive': true, 'scrollCollapse': true }) }) </script>";
	
	public static final String dataTableSetting3 =
			"<script> $(function () { $('#dataTable3').DataTable({'paging':false, 'scrollY': '50vh', 'lengthChange':false, 'searching':true, 'ordering':true, 'info':true, 'autoWidth': true, 'responsive': true, 'scrollCollapse': true }) }) </script>";
	
	public static final String dataTableSetting4 =
			"<script> $(function () { $('#dataTable4').DataTable({'paging':false, 'scrollY': '50vh', 'lengthChange':false, 'searching':true, 'ordering':true, 'info':true, 'autoWidth': true, 'responsive': true, 'scrollCollapse': true }) }) </script>";
	
	public static final String[] dataTableNames    = new String[] {"DATATABLE1","DATATABLE2","DATATABLE3","DATATABLE4"};
	public static final String[] dataTableSettings = new String[] {dataTableSetting1, dataTableSetting2, dataTableSetting3, dataTableSetting4};



	public static final String OWNER_ID             = "OWNER_ID";
	public static final String SERVICE_CUSTOMER_ID  = "SERVICE_CUSTOMER_ID";
	public static final String SELECT_OPTIONS       = "selectOptions";
	public static final String SESSION_KEY          = "sessionKey";

	public static final String ADD_TO_CART          = "ADD_TO_CART";
	public static final String CHECK_OUT            = "CHECK_OUT";
	public static final String MY_FAVORITE_ORDERS   = "MY_FAVORITE_ORDERS";
	public static final String MY_FAVORITE_PRODUCTS = "MY_FAVORITE_PRODUCTS";
	
	

	public static final String ORGANIZATION         = "ORGANIZATION";
	public static final String SHIPMENT             = "SHIPMENT";
	public static final String PURCHASE             = "PURCHASE";
	public static final String SALES                = "SALES";
	public static final String PRODUCTION           = "PRODUCTION";

	public static final String ORGANIZATION_IDS     = "ORGANIZATION_IDS";
	public static final String ORGANIZATION_ID      = "ORGANIZATION_ID";
	public static final String POSITION             = "POSITION";

	public static final String DATAFILTER           = "DATAFILTER";

	public static final String DOMAIN_OPTIONS       = "DOMAIN_OPTIONS";





}
