package ca.gc.hc.siteDemo.forms;

import java.io.Serializable;

public class UserLoginForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5097687752816461188L;

	private String username;
	
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
