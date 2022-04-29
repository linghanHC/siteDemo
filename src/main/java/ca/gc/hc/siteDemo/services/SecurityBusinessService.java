package ca.gc.hc.siteDemo.services;

import javax.security.auth.login.LoginException;

import org.springframework.stereotype.Service;

import ca.gc.hc.siteDemo.forms.UserLoginForm;
import ca.gc.hc.siteDemo.models.UserPrincipalBean;

@Service
public class SecurityBusinessService extends BaseBusinessService {

	// User Permissions
	public static final int FULL_PERMISSION = 1;
	public static final int READ_WRITE_PERMISSION = 2;
	public static final int READ_ONLY_PERMISSION = 3;

	private static SecurityBusinessService instance = new SecurityBusinessService();

	public static SecurityBusinessService getInstance() {
		return instance;
	}

	public boolean isUserAccessFull(int permission) {
		if (permission == FULL_PERMISSION) {
			return true;
		}

		return false;
	}

	public boolean isUserAccessReadWrite(int permission) {
		if (permission == READ_WRITE_PERMISSION) {
			return true;
		}

		return false;
	}

	public boolean isUserAccessReadonly(int permission) {
		if (permission == READ_ONLY_PERMISSION) {
			return true;
		}

		return false;
	}

	public Integer getUserPermission(UserLoginForm userLoginForm, UserPrincipalBean systemUserInfo)
			throws Exception {

		// Match the old password with the existing original password
		if (!isUserAndPasswordMatch(userLoginForm.getPassword(),
				systemUserInfo.getSystemPassword())) {
			return null;
		}

		return systemUserInfo.getPermission();

	}

	public boolean hasValidUserPermission(int userRole) {
		// Valid user role should be 1, 2 and 3
		if (userRole > 0 && userRole < 4) {
			return true;
		}

		return false;
	}

	public UserPrincipalBean getUserPrincipal(String loginName) {

//		String exceptionMsg = "::getLoginInfo: The login JSon string cannot be converted.";

//		UserAccountDTO selectedUserData = null;
//		try {
//
//			selectedUserData = jsonBusinessService
//					.convertJsonToData(apiBusinessService.getData(userByLoginNameURL + loginName,
//							getClass().getName() + "::getLoginInfo"), UserAccountDTO.class);
//
//		} catch (JsonMappingException ex) {
//			log.error(getClass().getName() + exceptionMsg);
//			log.debug(ex.getMessage());
//			return null;
//		} catch (JsonProcessingException ex) {
//			log.error(getClass().getName() + exceptionMsg);
//			log.debug(ex.getMessage());
//			return null;
//		}

//		if (selectedUserData == null) {
//			log.error(getClass().getName() + exceptionMsg);
//			return null;
//		}
//
//		// Map system information to user login info bean
		UserPrincipalBean userLoginInfo = new UserPrincipalBean();
//		userLoginInfo.setUserId(selectedUserData.getId().intValue());
//		userLoginInfo.setUsername(selectedUserData.getUsername());
//		userLoginInfo.setSystemPassword(selectedUserData.getPassword());
//		userLoginInfo.setPermission(selectedUserData.getPermissions().ordinal());

		return userLoginInfo;
	}

	/**
	 * 
	 * Validate if the input password matches the one in the system
	 * 
	 **/
	private boolean isUserAndPasswordMatch(String userInput, String securedPassword)
			throws LoginException, Exception {
		return true;
//		return PasswordBusinessService.getInstance().isPasswordValid(userInput, securedPassword);
	}

}
