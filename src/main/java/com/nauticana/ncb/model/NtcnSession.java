package com.nauticana.ncb.model;

import java.util.List;

public class NtcnSession {

	private String key;
	private long   created;
	private long   lastAccess;
	private String user;
	private int    personId;
	private int    client;
	private int    organizationId;
	private String position;
	private String firstName;
	private String lastName;
	private String cellPhone;
	private String lang;
	private AuthorizationValue authorizationValue;
	
	private List<UserMenu> menu;
	
	private static final byte[] PRIMES = new byte[] {19, 11, 23, 13, 17};
	
	public NtcnSession(String user, int personId, int client, int organizationId, String position, String firstName, String lastName, String cellPhone, String lang, List<UserMenu> menu, AuthorizationValue authorizationValue) {
		this.created = System.currentTimeMillis();
		this.lastAccess = this.created;
		this.key = keyGenerator(this.created);
		this.user = user;
		this.personId = personId;
		this.client = client;
		this.organizationId = organizationId;
		this.position = position;
		this.firstName = firstName;
		this.lastName = lastName;
		this.cellPhone = cellPhone;
		this.lang = lang;
		this.menu = menu;
		this.authorizationValue = authorizationValue;
	}

	private static String keyGenerator(long timestamp) {
		long t = timestamp;
		byte[]  c = new byte[32];
		int i = 0;
		while (t > 1 && i < 32) {
			int p = (int) (t % 5);
			c[i] = (byte) (t % PRIMES[p] + 65);
			t = t / PRIMES[p];
			i++;
		}
		String s = new String(c).trim();
		return s ;
	}
	
	public String getKey() {
		return key;
	}

	public synchronized long checkTime() {
		long t = System.currentTimeMillis();
		long elapsed = t - lastAccess;
		this.lastAccess = t;
		return elapsed;
	}

	public String getUser() {
		return user;
	}

	public int getPersonId() {
		return personId;
	}

	public int getClient() {
		return client;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public String getPosition() {
		return position;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public List<UserMenu> getMenu() {
		return menu;
	}

	public AuthorizationValue getAuthorizationValue() {
		return this.authorizationValue;
	}
}
