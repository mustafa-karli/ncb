package com.nauticana.ncb.model;

public class ApplicationConfig implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int				ownerId;
	private String			domain;
	private String			homepage;
	private String			applicationTitle;

	public ApplicationConfig() {
	}

	public ApplicationConfig(int id, String domain, String homepage, String applicationTitle) {
		this.ownerId = id;
		this.domain = domain;
		this.homepage = homepage;
		this.applicationTitle = applicationTitle;
	}

	public int getId() {
		return this.ownerId;
	}

	public void setId(int ownerId) {
		this.ownerId = ownerId;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getHomepage() {
		return this.homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getApplicationTitle() {
		return this.applicationTitle;
	}

	public void setApplicationTitle(String applicationTitle) {
		this.applicationTitle = applicationTitle;
	}

}
