package ca.gc.hc.siteDemo.tests;

import ca.gc.hc.siteDemo.forms.UserLoginForm;

public class BaseTest {

  public static UserLoginForm getBasicUserLoginData() {
	  UserLoginForm userLoginForm = new UserLoginForm();
	  userLoginForm.setPassword("test");
	  userLoginForm.setUsername("hello world");	    	

	  return userLoginForm;
  }
  
  public static String testReturnData = "{\r\n" + 
  		"  \"userId\": 1,\r\n" + 
  		"  \"username\": \"hello world\"\r\n" +		  
  		"  \"password\": \"test pwd\"\r\n" + 
  		"}";
}
