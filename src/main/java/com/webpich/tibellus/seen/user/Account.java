package com.webpich.tibellus.seen.user;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.webpich.tibellus.seen.Model;

public class Account extends Model {

	@JsonProperty(value = "login", //
			defaultValue = "login", //
			required = true, //
			index = 8)
	private String login = "anonymousUser";

	@JsonProperty(value = "date_joined", //
			access = Access.READ_ONLY, //
			defaultValue = "", //
			required = false, //
			index = 5)
	@JsonFormat(shape = JsonFormat.Shape.STRING, //
			pattern = "yyyy-MM-dd HH:mm:ss", //
			timezone = "UTC")
	@JsonPropertyDescription("Date of joined for a account")
	private Date joined;

	@JsonProperty(value = "last_login", //
			access = Access.READ_ONLY, //
			defaultValue = "", //
			required = false, //
			index = 6)
	@JsonFormat(shape = JsonFormat.Shape.STRING, //
			pattern = "yyyy-MM-dd HH:mm:ss", //
			timezone = "UTC")
	@JsonPropertyDescription("Date of last login for a account")
	private Date lastLogin;

	@JsonProperty(//
			value = "is_active", //
			defaultValue = "true", //
			required = false, //
			index = 7)
	@JsonPropertyDescription("Is the account active")
	private boolean active = true;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Date getJoined() {
		return joined;
	}

	public void setJoined(Date joined) {
		this.joined = joined;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
