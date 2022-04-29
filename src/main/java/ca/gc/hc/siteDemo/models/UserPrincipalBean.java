package ca.gc.hc.siteDemo.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserPrincipalBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4672236454110831874L;

	private int userId;
	
	private String username;
	
	private String salt;
	
	private String systemPassword;
	
	private int permission;

	
}
