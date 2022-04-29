package ca.gc.hc.siteDemo.controllers.authentication;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ca.gc.hc.siteDemo.constants.Constants;
import ca.gc.hc.siteDemo.constants.testData.LoginTestData;
import ca.gc.hc.siteDemo.controllers.BaseController;
import ca.gc.hc.siteDemo.forms.UserLoginForm;
import ca.gc.hc.siteDemo.models.UserPrincipalBean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController extends BaseController {

  private boolean hasError = false;
  
  @RequestMapping(Constants.LOGIN_URL_MAPPING)
  public String displayPage(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session,
	      Locale locale) {
	  
	  // reset display operation success flag
	  setOperationSuccessToSession(session, false);
	  setOperationErrorToSession(session, false);

      // add style sheet for CSS print
      addCustomCssToModel(model);

      if (hasError) {
    	  model.addAttribute(Constants.MODEL_OPERATION_ERROR, hasError);
    	  model.addAttribute(Constants.MODEL_ERROR_TITLE, screenDetailBusinessService.getResourceString("error.title", locale));
    	  model.addAttribute(Constants.MODEL_ERROR_MESSAGE, screenDetailBusinessService.getResourceString("error.login.message", locale));
      }

      model.addAttribute(Constants.PAGE_TITLE, screenDetailBusinessService.getPageTitle("label.authentication.login.pageTitle", locale));
	  model.addAttribute(Constants.MODEL_LOGIN_FORM, new UserLoginForm());
	  
	  return Constants.LOGIN_VIEW;  
  }

  @RequestMapping(Constants.DO_LOGIN_URL_MAPPING)
  public ModelAndView submit(Model model, HttpSession session, Locale locale, @ModelAttribute UserLoginForm userLoginForm) {
	  try {
		  
		  hasError = false;
		  if (!isValidLoginForm(userLoginForm)) {
			  // Missing user login information
			  log.error(getClass().getName()
					  + "::submit: Missing user login information. Unable to continue user authentication.");
			  return redirectError500View(model, locale);
		  }

		  UserPrincipalBean userPrincipal = new UserPrincipalBean();
		  userPrincipal = LoginTestData.getLoginInfoTestData(userLoginForm); // test data
			
		  // validate user name with password
		  if (canPassAuthentication(session, userLoginForm, userPrincipal)) {
			  
			  return redirect(Constants.UP_1_LEVEL_URL_MAPPING);

		  }
		  
		  hasError = true;
		  
		  return redirect(Constants.ROOT_URL_MAPPING);
		  
	  } catch (Exception ex) {
		  log.error(getClass().getName() + "::submit: " + ex.getMessage());
		  return redirectError500View(model, locale);
	  }
  }
  
  private boolean canPassAuthentication(HttpSession session, UserLoginForm userLoginForm, UserPrincipalBean systemUserInfo) {

	  try {
	  
		  if (systemUserInfo == null) {
			  return false;
		  }

		  boolean isAuthenticationPassed = false;
		  Integer permission = null;
		  
		  // get user information by user name
		  permission = systemUserInfo.getPermission(); // test data
		  		  
		  if (permission != null) {
			  // Record user permission to session
			  setUserPermissionToSession(session, permission);

			  // continue to main menu
			  isAuthenticationPassed = true;
		  }

		  // invalid user credential, reject login request
		  return isAuthenticationPassed;

	  } catch (Exception e) {
		  log.error(getClass().getName() + "::canPassAuthentication: " + e.getMessage());
		  return false;
	  }

  }
  
  private boolean isValidLoginForm(UserLoginForm userLoginForm) {
 
	  if (userLoginForm == null) {
		  return false;
	  }
	  
	  if (userLoginForm.getUsername() == null || userLoginForm.getPassword() == null) {
		  return false;
	  }
	  
	  return true;
  }
    
}
