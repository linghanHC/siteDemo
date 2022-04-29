package ca.gc.hc.siteDemo.constants.testData;

import ca.gc.hc.siteDemo.forms.UserLoginForm;
import ca.gc.hc.siteDemo.models.UserPrincipalBean;

public class LoginTestData {

	public static UserLoginForm getBasicLoginTestData() {
		UserLoginForm userLoginForm = new UserLoginForm();
		userLoginForm.setUsername("hcuser");
		userLoginForm.setPassword("Canada");
		
		return userLoginForm;
	}

	public static UserPrincipalBean getFullAccessUserInfoTestData() {
		UserPrincipalBean userInfo = new UserPrincipalBean();
		userInfo.setPermission(1);
		userInfo.setUsername("hcuser");
		
		return userInfo;
	}

	public static UserPrincipalBean getReadWriteUserInfoTestData() {
		UserPrincipalBean userInfo = new UserPrincipalBean();
		userInfo.setPermission(2);
		userInfo.setUsername("hcuser");
		
		return userInfo;
	}

	public static UserPrincipalBean getReadonlyAccessUserInfoTestData() {
		UserPrincipalBean userInfo = new UserPrincipalBean();
		userInfo.setPermission(3);
		userInfo.setUsername("hcuser");
		
		return userInfo;
	}

	public static UserPrincipalBean getLoginInfoTestData(UserLoginForm userLoginForm) {

		// Test data
		UserPrincipalBean userInfo;
		if (userLoginForm.getUsername().equals("1")) {
			userInfo = LoginTestData.getFullAccessUserInfoTestData();
		} else if (userLoginForm.getUsername().equals("2")) {
			userInfo = LoginTestData.getReadWriteUserInfoTestData();
		} else {
			userInfo = LoginTestData.getReadonlyAccessUserInfoTestData();
		}
		userInfo.setUsername(userLoginForm.getUsername());

		return userInfo;

	}

}
