package com.nauticana.ncb.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.nauticana.ncb.model.FieldType;
import com.nauticana.ncb.model.Labels;
import com.nauticana.ncb.model.TableDefinition;
import com.nauticana.ncb.model.Utils;

@Repository
public class DataServer {

	@Autowired
	private JdbcTemplate j;

	public Connection getConnection() {
		return DataSourceUtils.getConnection(j.getDataSource());
	}
	
//	@Transactional(readOnly = false)
	public ResultSet updatableQuery(Connection conn, String sql, Object[] args) throws SQLException {
		ArgumentPreparedStatementSetter pss = new ArgumentPreparedStatementSetter(args);
		PreparedStatement ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		pss.setValues(ps);
		return ps.executeQuery();
	}

//	@Transactional(readOnly = false)
	public ResultSet updatableQuery(Connection conn, String sql) throws SQLException {
		return conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(sql);
	}

	public ResultSet readOnlyQuery(Connection conn, String sql, Object[] args) throws SQLException {
		ArgumentPreparedStatementSetter pss = new ArgumentPreparedStatementSetter(args);
		PreparedStatement ps = conn.prepareStatement(sql);
		pss.setValues(ps);
		ResultSet rs = ps.executeQuery();
		return rs;
	}

	public ResultSet readOnlyQuery(Connection conn, String sql) throws SQLException {
		return conn.createStatement().executeQuery(sql);
	}

	public ResultSet findAll(Connection conn, TableDefinition table) throws SQLException {
		return readOnlyQuery(conn, table.getFindAllSql());
	}

	public ResultSet findByIdReadonly(Connection conn, TableDefinition table, Object[] id) throws SQLException {
		if (id == null) return null;
		return readOnlyQuery(conn, table.getFindByIdSql(), id);
	}

	public ResultSet findByIdUpdatable(Connection conn, TableDefinition table, Object[] id) throws SQLException {
		if (id == null) return null;
		return updatableQuery(conn, table.getFindByIdSql(), id);
	}

	public ResultSet findByIdReadonly(Connection conn, TableDefinition table, String id) throws SQLException, ParseException {
		if (id == null) return null;
		return readOnlyQuery(conn, table.getFindByIdSql(), table.strToId(id));
	}

	public ResultSet findByIdUpdatable(Connection conn, TableDefinition table, String id) throws SQLException, ParseException {
		if (id == null) return null;
		return updatableQuery(conn, table.getFindByIdSql(), table.strToId(id));
	}

	public ResultSet findByClient(Connection conn, TableDefinition table, int client) throws SQLException {
		return readOnlyQuery(conn, table.getFindByClientSql(), new Object[] { client });
	}

	public ResultSet findByOrganization(Connection conn, TableDefinition table, int client, int organization) throws SQLException {
		return readOnlyQuery(conn, table.getFindByOrganizationSql(), new Object[] { client, organization });
	}

	public ResultSet findByOrganization(Connection conn, TableDefinition table, int client, String organizations) throws SQLException {
		if (Utils.emptyStr(organizations)) return null;
		String sql;
		if (table.isClientDependent())
			sql = "SELECT T.* FROM " + table.getTableName() + " T WHERE OWNER_ID=" + client + " AND ORGANIZATION_ID IN (" + organizations + ")" + table.getOrderBy();
		else
			sql = "SELECT T.* FROM " + table.getTableName() + " T WHERE ORGANIZATION_ID IN (" + organizations + ")" + table.getOrderBy();
		return readOnlyQuery(conn, sql);
	}

	public ResultSet search(Connection conn, TableDefinition table, ArrayList<String> fields, ArrayList<String> filters) throws ParseException, SQLException {
		String sql;
		String whr = "";
		for (int i = 0; i < fields.size(); i++) {
			String filter = filters.get(i);
			if (filter.contains("*") || filter.contains("%")) {
				filters.set(i, filter.replaceAll("\\*", "\\%"));
				whr = whr + " AND " + fields.get(i) + " LIKE  ?";
			} else
				whr = whr + " AND " + fields.get(i) + " =  ?";
		}
		if (Utils.emptyStr(whr))
			sql = "SELECT * FROM " + table.getTableName();
		else
			sql = "SELECT * FROM " + table.getTableName() + " WHERE " + whr.substring(5);
		Object[] filterObjects = new Object[filters.size()];
		for (int i = 0; i < filters.size(); i++) {
			FieldType field = table.getField(fields.get(i));
			switch (field.getType()) {
				case FieldType.T_SHORT:
					filterObjects[i] = Short.parseShort(filters.get(i));
					break;
				case FieldType.T_INT:
					filterObjects[i] = Integer.parseInt(filters.get(i));
					break;
				case FieldType.T_LONG:
					filterObjects[i] = Long.parseLong(filters.get(i));
					break;
				case FieldType.T_FLT:
					filterObjects[i] = Double.parseDouble(filters.get(i));
					break;
				case FieldType.T_DATE:
					filterObjects[i] = Labels.dmyDF.parse(filters.get(i));
					break;
				default:
					filterObjects[i] = filters.get(i);
			}
		}
		return readOnlyQuery(conn, sql, filterObjects);
	}

	public void setId(ResultSet rs, FieldType[] keys, Object[] values) throws SQLException {
		for (int i = 0; i < keys.length; i++) {
			switch (keys[i].getType()) {
				case FieldType.T_SHORT:
					rs.updateShort(i + 1, (short) values[i]);
					break;
				case FieldType.T_INT:
					rs.updateInt(i + 1, (int) values[i]);
					break;
				case FieldType.T_LONG:
					rs.updateLong(i + 1, (long) values[i]);
					break;
				case FieldType.T_FLT:
					rs.updateDouble(i + 1, (double) values[i]);
					break;
				case FieldType.T_DATE:
					rs.updateTimestamp(i + 1, (Timestamp) values[i]);
					break;
				default:
					rs.updateString(i + 1, (String) values[i]);
					break;
			}
		}
	}

	public int insert(TableDefinition table, Object[] values) {
		return j.update(table.getInsertSql(), values);
	}

	public ResultSet emptyRow(Connection conn, TableDefinition table) throws SQLException {
		String sql = "SELECT * FROM " + table.getTableName() + " WHERE 1=2";
		ResultSet rs = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(sql);
		rs.next();
		rs.moveToInsertRow();
		return rs;
	}

	public int update(TableDefinition table, Object[] values) {
		Object[] v2 = new Object[values.length];
		int keycnt = 0;
		for (int i = 0; i < table.getFields().length; i++) {
			if (table.getFields()[i].isKeyField())
				keycnt++;
		}

		for (int i = keycnt - 1; i < values.length; i++) {
			v2[i - keycnt] = values[i];
		}
		for (int i = values.length - keycnt; i < values.length; i++) {
			v2[i] = values[values.length - keycnt + i];
		}
		return j.update(table.getUpdateSql(), v2);
	}

	public int delete(TableDefinition table, Object[] id) {
		return j.update(table.getDeleteSql(), id);
	}

	public String[][] findAllForLookup(TableDefinition table, int client) {
		if (Utils.emptyStr(table.getFindForLookupSql()))
			return null;
		ArgumentPreparedStatementSetter filters;
		if (table.isClientDependent())
			filters = new ArgumentPreparedStatementSetter(new Object[] { client });
		else
			filters = new ArgumentPreparedStatementSetter(new Object[] {});
		String[][] result = j.query(table.getFindForLookupSql(), filters, new ResultSetExtractor<String[][]>() {
			@Override
			public String[][] extractData(ResultSet rs) throws SQLException, DataAccessException {
				ArrayList<String> c1 = new ArrayList<String>();
				ArrayList<String> c2 = new ArrayList<String>();
				while (rs.next()) {
					c1.add(rs.getString(1));
					c2.add(rs.getString(1));
				}
				String[][] r = new String[c1.size()][2];
				for (int i = 0; i < c1.size(); i++) {
					r[i][0] = c1.get(i);
					r[i][1] = c2.get(i);
				}
				return r;
			}
		});
		return result;
	}
}
