package com.nauticana.ncb.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nauticana.ncb.model.Domain;
import com.nauticana.ncb.model.Icons;
import com.nauticana.ncb.model.NtcnSession;
import com.nauticana.ncb.model.PortalLanguage;
import com.nauticana.ncb.model.Utils;
import com.nauticana.ncb.repository.DataServer;

@Service
public class DataCache {

	private static HashMap<String, Domain>						domains				= null;
	private static HashMap<String, PortalLanguage>				languages			= null;
	private static HashMap<String, NtcnSession>					sessions			= null;
	private static HashMap<String, HashMap<String, String[]>>	fieldHeaders		= null;
//	private static HashMap<String, ApplicationConfig>			applicationConfigs	= new HashMap<String, ApplicationConfig>(0);
//	private static HashMap<Integer, ApplicationConfig>			applicationConfigi	= new HashMap<Integer, ApplicationConfig>(0);

	@Value("nauticana.default.language")
	public static String										defaultLanguage="EN";

	@Value("nauticana.session.lifetime")
	public static long											SESSION_LIFE_TIME=300000; // Default session life time 5 minutes (5 * 60 * 1000 milliseconds)

	@Autowired
	private DataServer											server;

	@Autowired
	private TableService										tableService;

	private DataCache() {
	}

	public void loadDomains() throws SQLException {
		if (domains == null)
			domains = new HashMap<String, Domain>();
		else
			domains.clear();
		Connection conn = server.getConnection();
		ResultSet rs = server.readOnlyQuery(conn, "SELECT DOMAIN, CAPTION, KEYSIZE, SORT_BY FROM DOMAIN_NAME");
		while (rs.next()) {
			Domain domain = new Domain(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4));
			domains.put(domain.getId(), domain);
			ResultSet val = server.readOnlyQuery(conn, "SELECT REF_VALUE, CAPTION FROM DOMAIN_VALUE WHERE DOMAIN=?", new Object[] { domain.getId() });
			while (val.next()) {
				domain.addOption(val.getString(1), val.getString(2));
			}
		}
	}

	public Map<String, PortalLanguage> loadLanguages() throws SQLException, IOException {
		if (languages == null)
			languages = new HashMap<String, PortalLanguage>();
		else
			languages.clear();
		Connection conn = server.getConnection();
		ResultSet rs = server.readOnlyQuery(conn, "SELECT LANGCODE, CAPTION, LOCALE_STR, DIRECTION, FLAG FROM LANGUAGE");
		while (rs.next()) {
			String langcode = rs.getString(1);
			PortalLanguage language = new PortalLanguage(langcode, rs.getString(2), rs.getString(3));
			languages.put(langcode, language);
			ResultSet val = server.readOnlyQuery(conn, "SELECT CAPTION, LABELUPPER, LABELLOWER FROM CAPTION_TRANSLATION WHERE LANGCODE=?", new Object[] { langcode });
			while (val.next()) {
				String caption = rs.getString(1);
				// String labelUpper = rs.getString(2);
				String labelLower = rs.getString(3);
				language.translations.put(caption, labelLower);
				String icon = Icons.getIcon(caption + "_ICON");
				if (icon != null) {
					language.iconText.put(caption, "<i class=\"" + icon + "\"> " + labelLower + " </i>");
					language.icon.put(caption, "<i class=\"" + icon + "\"></i>");
				}
			}
		}
		return languages;
	}

	public Domain getDomain(String domainName) throws SQLException {
		if (domains == null)
			loadDomains();
		return domains.get(domainName);
	}

	public Map<String, String> getDomainOptions(String domainName) throws SQLException {
		if (domains == null)
			loadDomains();
		Domain d = domains.get(domainName);
		if (d == null)
			return null;
		return d.getOptions();
	}

	public Map<String, String> getDomainOptions(String domainName, PortalLanguage language) throws SQLException {
		if (domains == null)
			loadDomains();
		Domain d = domains.get(domainName);
		if (d == null)
			return null;
		return d.getOptions(language);
	}

	public Map<String, String> getLookupOptions(String tableName, int client) throws SQLException {
		String[][] list = server.findAllForLookup(tableService.getTableDefinition(tableName), client);
		if (list == null)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < list.length; i++) {
			map.put(list[i][0], list[i][1]);
		}
		return map;
	}

	public PortalLanguage getLanguage(String language) throws SQLException, IOException {
		if (languages == null) {
			// System.out.println("\n Languages null, loading:\n");
			loadLanguages();
		}
		if (Utils.emptyStr(language))
			return languages.get(defaultLanguage);
		else
			return languages.get(language);

	}

	public Map<String, PortalLanguage> getLanguages() throws SQLException, IOException {
		if (languages == null) {
			// System.out.println("\n Languages null, loading:\n");
			loadLanguages();
		}
		return languages;
	}

	public NtcnSession getSession(String key) {
		if (sessions == null) {
			sessions = new HashMap<String, NtcnSession>();
			return null;
		}
		NtcnSession ses = sessions.get(key);
		if (ses == null)
			return null;
		if (ses.checkTime() > SESSION_LIFE_TIME) {
			sessions.remove(key);
			return null;
		}
		return ses;
	}

	public void delSession(String key) {
		if (sessions == null) {
			sessions = new HashMap<String, NtcnSession>();
		} else {
			NtcnSession ses = sessions.get(key);
			if (ses != null)
				sessions.remove(key);
		}
	}

	public NtcnSession putSession(NtcnSession session) {
		if (sessions == null) {
			sessions = new HashMap<String, NtcnSession>();
		}
		
		for (NtcnSession s : sessions.values()) {
			if (s.getUser().equals(session.getUser())) {
				sessions.remove(s.getKey());
			}
		}
		sessions.put(session.getKey(), session);
		return session;
	}

	public String[] getFieldHeaders(String tableName, String langcode) {
		if (fieldHeaders == null)
			fieldHeaders = new HashMap<String, HashMap<String, String[]>>();
		HashMap<String, String[]> headers = fieldHeaders.get(tableName);
		if (headers == null)
			return null;
		return headers.get(langcode);
	}

	public void putFieldHeaders(String tableName, HashMap<String, String[]> headers) {
		if (fieldHeaders == null)
			fieldHeaders = new HashMap<String, HashMap<String, String[]>>();
		HashMap<String, String[]> oldHeaders = fieldHeaders.get(tableName);
		if (oldHeaders != null) {
			fieldHeaders.remove(tableName);
		}
		fieldHeaders.put(tableName, headers);
	}

//	public ApplicationConfig getApplicationConfig(int ownerId) throws SQLException {
//		ApplicationConfig applicationConfig = applicationConfigi.get(ownerId);
//		if (applicationConfig != null)
//			return applicationConfig;
//
//		TableDefinition table = tableService.getTableDefinition("APPLICATION_CONFIG");
//		ResultSet rs = server.findByIdReadonly(server.getConnection(), table, new Object[] { ownerId });
//		if (rs.next()) {
//			applicationConfig = new ApplicationConfig(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
//			applicationConfigi.put(ownerId, applicationConfig);
//			applicationConfigs.put(applicationConfig.getDomain(), applicationConfig);
//		}
//		return applicationConfig;
//	}
//
//	public ApplicationConfig getApplicationConfig(String domain) throws ParseException, SQLException {
//		ApplicationConfig applicationConfig = applicationConfigs.get(domain);
//		if (applicationConfig != null)
//			return applicationConfig;
//		ArrayList<String> fields = new ArrayList<String>();
//		ArrayList<String> filters = new ArrayList<String>();
//		fields.add("DOMAIN");
//		filters.add(domain);
//		TableDefinition table = tableService.getTableDefinition("APPLICATION_CONFIG");
//		ResultSet rs = server.search(server.getConnection(), table, fields, filters);
//		if (rs.next()) {
//			applicationConfig = new ApplicationConfig(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
//			applicationConfigi.put(applicationConfig.getId(), applicationConfig);
//			applicationConfigs.put(applicationConfig.getDomain(), applicationConfig);
//		}
//		return applicationConfig;
//	}
}
