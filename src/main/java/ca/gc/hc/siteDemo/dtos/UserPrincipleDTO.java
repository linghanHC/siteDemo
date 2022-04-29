package ca.gc.hc.siteDemo.dtos;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserPrincipleDTO implements Serializable {

	private static final long serialVersionUID = -8863295979937674913L;
	
	private int userId;
	
	private String username;
	
	private String salt;
	
	private String systemPassword;
	
	private int permission;
	
}