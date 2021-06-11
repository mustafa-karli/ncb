package com.nauticana.ncb.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nauticana.ncb.model.AuthorizationValue;
import com.nauticana.ncb.model.FieldType;
import com.nauticana.ncb.model.NtcnSession;
import com.nauticana.ncb.model.TableDefinition;
import com.nauticana.ncb.model.UserMenu;
import com.nauticana.ncb.model.Utils;

@Repository
public class BasisRepository {
	
	@Value("nauticana.database.type")
	public static int DBTYPE;

	public static final int DT_H2SQL = 0;
	public static final int DT_PGSQL = 1;
	

	private static final String userAccountEmployee = //"SELECT * FROM USER_ACCOUNT_OWNER WHERE USERNAME=? ORDER BY BEGDA DESC, EMPLOYMENT DESC, ASSIGNMENT DESC";
			"SELECT O.USERNAME, O.BEGDA, P.PERSON_ID, P.FIRST_NAME, P.LAST_NAME, P.CELL_PHONE, E.EMPLOYMENT, E.OWNER_ID, A.ORGANIZATION_ID, A.POSITION, A.BEGDA AS ASSIGNMENT" +
			"  FROM USER_ACCOUNT_OWNER AS O, PERSON P, EMPLOYEE E, POSITION_ASSIGNMENT A" +
			" WHERE O.USERNAME=?" +
			"   AND P.PERSON_ID=O.PERSON_ID" +
			"   AND E.PERSON_ID=P.PERSON_ID" +
			"   AND A.PERSON_ID=E.PERSON_ID" +
			"   AND A.EMPLOYMENT=E.EMPLOYMENT" +
			" ORDER BY 2 DESC, 7 DESC, 11 DESC";
	
	private static final String userMenuSql       = "SELECT * FROM USER_MENU_PERMISSION WHERE USERNAME=? ORDER BY MENU_ORDER, PAGE_ORDER";
	private static final String tableDomainSql    = "SELECT DISTINCT DOMAIN FROM DOMAIN_LOOKUP WHERE TABLENAME=?";
	private static final String authObjectType    = "SELECT OBJECT_TYPE FROM AUTHORITY_OBJECT ORDER BY 1";
	private static final String authObjectAct     = "SELECT ACTION FROM AUTHORITY_OBJECT_ACTION WHERE OBJECT_TYPE=?";
	private static final String authorityUsers    =
			"SELECT O.OBJECT_TYPE, O.ACTION, O.KEY_VALUE FROM OBJECT_AUTHORIZATION O, USER_AUTHORIZATION U" + 
			" WHERE O.AUTHORITY_GROUP=U.AUTHORITY_GROUP" + 
			"   AND U.USERNAME=?";
	
	private static final String authorityChk      =
			"SELECT COUNT(*) FROM OBJECT_AUTHORIZATION O, USER_AUTHORIZATION U" + 
			" WHERE O.AUTHORITY_GROUP=U.AUTHORITY_GROUP" + 
			"   AND U.USERNAME=?" + 
			"   AND O.OBJECT_TYPE=?" + 
			"   AND O.ACTION=?" + 
			"   AND O.KEY_VALUE IN ('*', ?)";
	
	private static final String authorityChkE     =
			"SELECT COUNT(*) FROM OBJECT_AUTHORIZATION O, USER_AUTHORIZATION U" + 
			" WHERE O.AUTHORITY_GROUP=U.AUTHORITY_GROUP" + 
			"   AND U.USERNAME=?" + 
			"   AND O.OBJECT_TYPE=?" + 
			"   AND O.ACTION=?";
	
	private static final String authorizedObjects =
			"SELECT DISTINCT KEY_VALUE" + 
			"  FROM USER_AUTHORIZATION U, AUTHORITY_GROUP G, OBJECT_AUTHORIZATION O" + 
			" WHERE U.AUTHORITY_GROUP=G.AUTHORITY_GROUP" + 
			"   AND O.AUTHORITY_GROUP=G.AUTHORITY_GROUP" + 
			"   AND U.USERNAME=?" + 
			"   AND O.OBJECT_TYPE=?" + 
			"   AND O.ACTION=?";
	
	public static final String userFavoritesSql          = "SELECT T.OBJECT_ID FROM USER_FAVORITE T WHERE T.USERNAME=? AND T.FAVORITE_TYPE=?";
	public static final String addUserFavoritesSql       = "INSERT INTO USER_FAVORITE VALUES (?, ?, ?, ?)";

	public static final String tableDefinitionSql        = "SELECT * FROM TABLE_CONTROLLER_STATIC WHERE TABLENAME= ?";
	public static final String tableFieldSql             = "SELECT * FROM TABLE_FIELD_FACE WHERE TABLENAME= ?";
	public static final String tableActionSql            = "SELECT * FROM TABLE_ACTION WHERE TABLENAME= ?";
	public static final String tableContentSql           = "SELECT * FROM TABLE_CONTENT_TYPE WHERE TABLENAME= ?";
//	public static final String tableDetailSql            = "SELECT * FROM MASTER_DETAIL_RELATION WHERE MASTER_TABLE= ?";
	
	public static final String notificationSql = 
			  "SELECT A.NOTIFICATION_TYPE_ID, B.USERNAME"
			+ "  FROM NOTIFICATION_TYPE A, NOTIFICATION_RECIPIENT B"
			+ " WHERE A.NOTIFICATION_TYPE_ID = B.NOTIFICATION_TYPE_ID"
			+ "   AND A.OWNER_ID=?"
			+ "   AND A.TABLENAME=?"
			+ "   AND B.EVENT=?"
			+ "   AND B.ENABLE='Y'";
	
	public static final String tableViewScenarios =
			"SELECT S.SCENARIO_ID FROM TABLE_VIEW_SCENARIO S ORDER BY 1";

	public static final String tableViewScenarioItemSql =
			"SELECT S.CAPTION, S.TABLENAME AS MASTER, I.SEQ, I.FKNAME, I.TABLENAME, I.CAPTION, I.ACCESS_TYPE, I.PARENT_SEQ FROM TABLE_VIEW_SCENARIO S, TABLE_VIEW_SCENARIO_ITEM I WHERE S.SCENARIO_ID=? AND I.SCENARIO_ID=S.SCENARIO_ID ORDER BY I.SEQ";

	@Autowired
    private JdbcTemplate j;
	
	class StringRowMapper implements RowMapper<String> {
	    @Override
	    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return rs.getString(1);
	    }
	}

	class IntegerRowMapper implements RowMapper<Integer> {
	    @Override
	    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return rs.getInt(1);
	    }
	}

	@Transactional(readOnly=true)
	public List<String> allTables() throws SQLException {
		return j.query("SELECT TABLENAME FROM TABLE_CONTROLLER_STATIC", new StringRowMapper());
	}

	
	@Transactional(readOnly=true)
    public AuthorizationValue loadUserAuthorizations(String username) {
		AuthorizationValue k = j.query(authorityUsers, new ArgumentPreparedStatementSetter(new Object[]{username}), new ResultSetExtractor<AuthorizationValue>() {
			@Override
			public AuthorizationValue extractData(ResultSet rs) throws SQLException, DataAccessException {
				AuthorizationValue authorizationValue = new AuthorizationValue();
		    	while (rs.next()) {
		    		authorizationValue.put(rs.getString(1), rs.getString(2), rs.getString(3));
		    	}
		        return authorizationValue;
			}
		});
		return k;
    }
	
	@Transactional(readOnly=true)
    public List<UserMenu> userMenu(String username) {
		ArrayList<UserMenu> m = j.query(userMenuSql, new ArgumentPreparedStatementSetter(new Object[]{username}), new ResultSetExtractor<ArrayList<UserMenu>>() {
			@Override
			public ArrayList<UserMenu> extractData(ResultSet rs) throws SQLException, DataAccessException {
		    	ArrayList<UserMenu> m = new ArrayList<UserMenu>();
		    	while (rs.next()) {
		    		m.add(new UserMenu(rs.getString(6), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getInt(10)));
		    	}
		        return m;
			}
		});
		return m;
    }

	@Transactional(readOnly=true)
    public NtcnSession sessionWithEmployee(String username, String language) {
		NtcnSession m = j.query(userAccountEmployee, new ArgumentPreparedStatementSetter(new Object[]{username}), new ResultSetExtractor<NtcnSession>() {
			@Override
			public NtcnSession extractData(ResultSet rs) throws SQLException, DataAccessException {
				Date now = Utils.onlyDate();
				List<UserMenu> menu = userMenu(username);
				AuthorizationValue authorizationValue = loadUserAuthorizations(username);
		    	while (rs.next()) {
		    		Date begda = rs.getDate(2);
		    		int personId = rs.getInt(3);
		    		String firstName = rs.getString(4);
		    		String lastName = rs.getString(5);
		    		String cellPhone = rs.getString(6);
		    		Date employment = rs.getDate(7);
		    		int client = rs.getInt(8);
		    		int organizationId = rs.getInt(9);
		    		String position = rs.getString(10);
		    		Date assignment = rs.getDate(11);
		    				
		    		if (begda.getTime() <= now.getTime() && employment.getTime() <= now.getTime() && assignment.getTime() <= now.getTime()) {
		    			return new NtcnSession(username, personId, client, organizationId, position, firstName, lastName, cellPhone, language, menu, authorizationValue);
		    		}
		    	}
    			return new NtcnSession(username, -1, -1, -1, "", "", "", "", language, menu, authorizationValue);
			}
		});
		return m;
    }

	@Transactional(readOnly=true)
    public List<String[]> notificationUser(int client, String tablename, String event) {
		ArrayList<String[]> m = j.query(notificationSql, new ArgumentPreparedStatementSetter(new Object[]{client, tablename, event}), new ResultSetExtractor<ArrayList<String[]>>() {
			@Override
			public ArrayList<String[]> extractData(ResultSet rs) throws SQLException, DataAccessException {
		    	ArrayList<String[]> m = new ArrayList<String[]>();
		    	while (rs.next()) {
		    		String[] line = new String[]{rs.getString(1), rs.getString(2)};
		    		m.add(line);
		    	}
		        return m;
			}
		});
		return m;
    }

	@Transactional(readOnly=true)
    public boolean authorityChk(String username, String objectType, String action, String keyValue) {
		Integer k = j.query(authorityChk, new ArgumentPreparedStatementSetter(new Object[]{username, objectType, action, keyValue}), new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int i = 0;
				if (rs.next()) {
					i = rs.getInt(1);
				} else {
//					System.out.println("Authority check for " + username + " to " + action + " on " + objectType + "(" + keyValue + ") failed !!!");
				}
				return Integer.valueOf(i);
			}
		});
		return k.intValue() > 0;
    }
	
	@Transactional(readOnly=true)
    public boolean authorityChk(String username, String objectType, String action) {
		Integer k = j.query(authorityChkE, new ArgumentPreparedStatementSetter(new Object[]{username, objectType, action}), new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int i = 0;
				if (rs.next()) {
					i = rs.getInt(1);
				} else {
//					System.out.println("Authority check for " + username + " to " + action + " on " + objectType + " failed !!!");
				}
				return Integer.valueOf(i);
			}
		});
		return k.intValue() > 0;
    }
	
	@Transactional(readOnly=true)
    public FieldType[] retreiveFieldTypes(String tableName) {
		return j.query("SELECT * FROM " + tableName + " WHERE 1=2", new ResultSetExtractor<FieldType[]>() {
			@Override
			public FieldType[] extractData(ResultSet rs) throws SQLException, DataAccessException {
				ResultSetMetaData md = rs.getMetaData();
				FieldType[] fields = new FieldType[md.getColumnCount()];
				for (int i = 1; i <= md.getColumnCount(); i++) {
					boolean required = (md.isNullable(i) == 0);
					FieldType field = new FieldType(md.getColumnName(i).toUpperCase(Locale.ENGLISH), md.getColumnType(i), md.getColumnDisplaySize(i), md.getScale(i), i, required);
					fields[i-1] = field;
				}
				
				return fields;
			}
		});
    }
	
	@Transactional(readOnly=true)
	public List<String> tableDomainsList(String tableName) {
    	return j.query(tableDomainSql, new ArgumentPreparedStatementSetter(new Object[]{tableName}), new StringRowMapper());
	}

	@Transactional(readOnly=true)
	public String[] tableDomains(String tableName) {
		ArrayList<String> s = j.query(tableDomainSql, new ArgumentPreparedStatementSetter(new Object[]{tableName}), new ResultSetExtractor<ArrayList<String>>() {
			@Override
			public ArrayList<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				ArrayList<String> s = new ArrayList<String>();
				while (rs.next()) {
					s.add(rs.getString(1));
				}return s;
			}
		});
		String[] sl = new String[s.size()];
		for (int i = 0; i < sl.length; i++) {
			sl[i] = s.get(i);
		}
		return sl;
	}

	@Transactional(readOnly=true)
	public String[] userFavorites(String username, String favType) {
		ArrayList<String> s = j.query(userFavoritesSql, new ArgumentPreparedStatementSetter(new Object[]{username, favType}), new ResultSetExtractor<ArrayList<String>>() {
			@Override
			public ArrayList<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				ArrayList<String> s = new ArrayList<String>();
				while (rs.next()) {
					s.add(rs.getString(1));
				}return s;
			}
		});
		String[] sl = new String[s.size()];
		for (int i = 0; i < sl.length; i++) {
			sl[i] = s.get(i);
		}
		return sl;
	}

	@Transactional
	public int addUserFavorite(String username, String favType, String objid, String description) {
		return j.update(addUserFavoritesSql, username, favType, objid, description);
	}
	
	@Transactional(readOnly=true)
	public List<String> authorityObjectTypes() {
    	return j.query(authObjectType, new StringRowMapper());
	}

	@Transactional(readOnly=true)
	public List<String> authorityObjectActions(String authorityObject) {
    	return j.query(authObjectAct, new ArgumentPreparedStatementSetter(new Object[]{authorityObject}), new StringRowMapper());
	}

	@Transactional(readOnly=true)
	public List<String> authorizedObjects(String username, String authorityObject, String action) {
    	return j.query(authorizedObjects, new ArgumentPreparedStatementSetter(new Object[]{username, authorityObject, action}), new StringRowMapper());
	}

	@Transactional(readOnly=true)
	public TableDefinition retreiveTableDefinition(String tableName) throws SQLException {
		return j.query(tableDefinitionSql, new ArgumentPreparedStatementSetter(new Object[]{tableName}), new ResultSetExtractor<TableDefinition>() {
			@Override
			public TableDefinition extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					String tableName          = rs.getString(1);
					String module             = rs.getString(3);
					boolean clientSpecific    = (rs.getString(4).charAt(0) == 'Y');
					boolean organizationCheck = (rs.getString(5).charAt(0) == 'Y');
					boolean cacheInHash       = (rs.getString(6).charAt(0) == 'Y');
					String searchView         = rs.getString(7);
					String listView           = rs.getString(8);
					String editView           = rs.getString(9);
					String showView           = rs.getString(10);
					String selectView         = rs.getString(11);
					String orderBy            = rs.getString(12);
					String sequenceName       = rs.getString(13);
					return new TableDefinition(tableName, module, null, null, listView, searchView, editView, showView, selectView, orderBy, sequenceName, clientSpecific, organizationCheck, cacheInHash);
				}
				return null;
			}
		});
	}
	
	@Transactional(readOnly=true)
	public ArrayList<String[]> retreiveTableFields(String tableName) throws SQLException {
		ArrayList<String[]> fields = j.query(tableFieldSql, new ArgumentPreparedStatementSetter(new Object[]{tableName}), new ResultSetExtractor<ArrayList<String[]>>() {
		@Override
		public ArrayList<String[]> extractData(ResultSet rs) throws SQLException, DataAccessException {
			ArrayList<String[]> f = new ArrayList<String[]>();
			while (rs.next()) {
				String [] s = new String[] {rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10) };
				f.add(s);
			}
			return f;
		}
		});
		return fields;
	}

	@Transactional(readOnly=true)
	public void setTableFields(TableDefinition tableDefinition) throws SQLException {
		ArrayList<String[]> fields = retreiveTableFields(tableDefinition.getTableName());
		FieldType[] fieldTypes = retreiveFieldTypes(tableDefinition.getTableName());
		
		for (int i = 0; i < fields.size(); i++) {
			String[] list = fields.get(i);
			for (int j = 0; j < fieldTypes.length; j++) {
				if (fieldTypes[j].fieldName.equals(list[0])) {
					fieldTypes[j].setEditStyle(list[1]);
					fieldTypes[j].setEditJstlPath(list[2]);
					fieldTypes[j].setViewJstlPath(list[3]);
					fieldTypes[j].setSearchStyle(list[4]);
					fieldTypes[j].setLookupStyle(list[5]);
					fieldTypes[j].setTranslated(list[6].charAt(0) == 'Y');
					fieldTypes[j].setMinValue(list[7]);
					fieldTypes[j].setMaxValue(list[8]);
				}
			}
		}

		Connection conn = DataSourceUtils.getConnection(j.getDataSource());
		ResultSet pk;
		String pkColumnName;
		if (DBTYPE == DT_PGSQL)
			pk = conn.getMetaData().getPrimaryKeys(null, null, tableDefinition.getTableName().toLowerCase(Locale.ENGLISH));
		else
			pk = conn.getMetaData().getPrimaryKeys(null, null, tableDefinition.getTableName());
		int keyCount = 0;
		while(pk.next()) {
			keyCount++;
			if (DBTYPE == DT_PGSQL)
				pkColumnName = pk.getString("column_name").toUpperCase(Locale.ENGLISH);
			else
				pkColumnName = pk.getString("COLUMN_NAME").toUpperCase(Locale.ENGLISH);
			for (int i = 0; i < fieldTypes.length; i++) {
				if (fieldTypes[i].fieldName.equals(pkColumnName))
					fieldTypes[i].setKeyField(true);
			}
		}
		
		FieldType[] keyFields = new FieldType[keyCount];
		keyCount=0;
		for (int i = 0; i < fieldTypes.length; i++) {
			if (fieldTypes[i].isKeyField()) {
				keyFields[keyCount] = fieldTypes[i];
				keyCount++;
			}
		}
		
		tableDefinition.setFields(fieldTypes);
		tableDefinition.setKeys(keyFields);
		tableDefinition.setStaticSql();
	}	
	
	@Transactional(readOnly=true)
	public ArrayList<String[]> retreiveContentTypes(String tableName) throws SQLException {
		return j.query(tableContentSql, new ArgumentPreparedStatementSetter(new Object[]{tableName}), new ResultSetExtractor<ArrayList<String[]>>() {
			@Override
			public ArrayList<String[]> extractData(ResultSet rs) throws SQLException, DataAccessException {
				ArrayList<String[]> f = new ArrayList<String[]>();
				while (rs.next()) {
					String [] s = new String[] {rs.getString(2), rs.getString(3), rs.getString(4) };
					f.add(s);
				}
				return f;
			}
		});
	}

	@Transactional(readOnly=true)
	public ArrayList<String[]> retreiveTableActions(String tableName) throws SQLException {
		return j.query(tableActionSql, new ArgumentPreparedStatementSetter(new Object[]{tableName}), new ResultSetExtractor<ArrayList<String[]>>() {
			@Override
			public ArrayList<String[]> extractData(ResultSet rs) throws SQLException, DataAccessException {
				ArrayList<String[]> f = new ArrayList<String[]>();
				while (rs.next()) {
					String [] s = new String[] {rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7) };
					f.add(s);
				}
				return f;
			}
		});
	}
	
	public void retreiveParentForeignKeys(
			TableDefinition tableDefinition,
			ArrayList<String> parents,
			ArrayList<String> forkeys,
			ArrayList<ArrayList<String>> pKeys,
			ArrayList<ArrayList<String>> cKeys) throws SQLException {
		Connection conn = DataSourceUtils.getConnection(j.getDataSource());
		String foreignKey = "";
		ArrayList<String> pKey = null;
		ArrayList<String> cKey = null;
		ResultSet fk = conn.getMetaData().getImportedKeys(null, null, tableDefinition.getTableName());
		if (!fk.first())
			fk = conn.getMetaData().getImportedKeys(null, null, tableDefinition.getTableName().toLowerCase(Locale.ENGLISH));
		fk.beforeFirst();
		while (fk.next()) {
			String pkTab = fk.getString(3).toUpperCase(Locale.ENGLISH);  // PKTABLE_NAME
			String pkCol = fk.getString(4).toUpperCase(Locale.ENGLISH);  // PKCOLUMN_NAME
			String fkCol = fk.getString(8).toUpperCase(Locale.ENGLISH);  // FKCOLUMN_NAME
			String fkNam = fk.getString(12).toUpperCase(Locale.ENGLISH); // FK_NAME
			if (!fkNam.equals(foreignKey)) {
				foreignKey = fkNam;
				parents.add(pkTab);
				forkeys.add(foreignKey);
				pKey = new ArrayList<String>();
				cKey = new ArrayList<String>();
				pKeys.add(pKey);
				cKeys.add(cKey);
			}
			pKey.add(pkCol);
			cKey.add(fkCol);
		}
	}
	
	@Transactional(readOnly=true)
	public List<String> retreiveChildTables(String tableName) throws SQLException {
		Connection conn = DataSourceUtils.getConnection(j.getDataSource());
		ArrayList<String> children = new ArrayList<String>();
		ResultSet fk = conn.getMetaData().getExportedKeys(null, null, tableName);
		if (!fk.first())
			fk = conn.getMetaData().getExportedKeys(null, null, tableName.toLowerCase(Locale.ENGLISH));
		fk.beforeFirst();
		String child = "";
		while (fk.next()) {
			String fkTab = fk.getString(7).toUpperCase(Locale.ENGLISH);  // FKTABLE_NAME
			if (!fkTab.equals(child)) {
				child = fkTab;
				children.add(fkTab);
			}
		}
		return children;
	}

	@Transactional(readOnly=true)
	public List<String> retreiveScenarios() {
		return j.query(tableViewScenarios, new StringRowMapper());
	}
	
	@Transactional(readOnly=true)
	public ArrayList<String[]> retreiveScenarioPath(String scenarioId) {
		return j.query(tableViewScenarioItemSql, new ArgumentPreparedStatementSetter(new Object[] { scenarioId }), new ResultSetExtractor<ArrayList<String[]>>() {
			@Override
			public ArrayList<String[]> extractData(ResultSet rs) throws SQLException, DataAccessException {
				ArrayList<String[]> paths = new ArrayList<String[]>();
				while (rs.next()) {
					paths.add(new String[] {rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)}); 
				}
				if (paths.isEmpty()) return null;
				return paths;
			}
		});
	}


	
	public long nextSequenceValue(String sequenceName) {
//		String sql = "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";
		String sql = "SELECT NEXTVAL('" + sequenceName + "')";

		long v = j.query(sql, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rs.getLong(1);
				else
					return (long) -1;
			}
		});
		return v;
	}

	public List<String[]> retreiveModuleTables() {
		return j.query("SELECT TABLENAME, MODULE FROM TABLE_CONTROLLER_STATIC ORDER BY MODULE, TABLENAME", new ResultSetExtractor<ArrayList<String[]>>() {
			@Override
			public ArrayList<String[]> extractData(ResultSet rs) throws SQLException, DataAccessException {
				ArrayList<String[]> records = new ArrayList<String[]>();
				while (rs.next()) {
					records.add(new String[] {rs.getString(1), rs.getString(2)});
				}
				return records;
			}
		});
	}
	
	
	
}
