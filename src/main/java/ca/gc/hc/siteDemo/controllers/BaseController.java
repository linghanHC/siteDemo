package ca.gc.hc.siteDemo.controllers;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ca.gc.hc.siteDemo.constants.Constants;
import ca.gc.hc.siteDemo.models.SelectAdvisorBean;
import ca.gc.hc.siteDemo.services.ScreenDetailBusinessService;
import ca.gc.hc.siteDemo.services.SecurityBusinessService;

public class BaseController {

	@Autowired
	protected ScreenDetailBusinessService screenDetailBusinessService;

	@Autowired
	protected SecurityBusinessService securityBusinessService;
	
	protected void addCustomJsToModel(Model model) {
		model.addAttribute(Constants.JS_INCLUDES, Constants.JS_INCLUDES_FILE);
	}

	protected void addCustomCssToModel(Model model) {
		model.addAttribute(Constants.CSS_INCLUDES, Constants.CSS_INCLUDES_FILE);
	}

	protected ModelAndView redirect(String redirectUrlMapping) {
		RedirectView view = new RedirectView(redirectUrlMapping, true);
		view.setExposeModelAttributes(false);
		return new ModelAndView(view);    
	}

	protected String getError404View(Model model, Locale locale) {
		model.addAttribute(Constants.PAGE_TITLE, screenDetailBusinessService.getPageTitle("error.404.title", locale));
		return Constants.ERROR_404_VIEW;
	}

	protected ModelAndView redirectError404View(Model model, Locale locale) {
		model.addAttribute(Constants.PAGE_TITLE, screenDetailBusinessService.getPageTitle("error.404.title", locale));
		return redirect(Constants.ERROR_404_VIEW);
	}

	protected String getError409View(Model model, Locale locale) {
		model.addAttribute(Constants.PAGE_TITLE, screenDetailBusinessService.getPageTitle("error.409.title", locale));
		return Constants.ERROR_409_VIEW;
	}

	protected ModelAndView redirectError409View(Model model, Locale locale) {
		model.addAttribute(Constants.PAGE_TITLE, screenDetailBusinessService.getPageTitle("error.409.title", locale));
		return redirect(Constants.UP_1_LEVEL_URL_MAPPING + Constants.ERROR_409_VIEW);
	}

	protected String getError500View(Model model, Locale locale) {
		model.addAttribute(Constants.PAGE_TITLE, screenDetailBusinessService.getPageTitle("error.500.title", locale));
		return Constants.ERROR_500_VIEW;
	}

	protected ModelAndView redirectError500View(Model model, Locale locale) {
		model.addAttribute(Constants.PAGE_TITLE, screenDetailBusinessService.getPageTitle("error.500.title", locale));
		return redirect(Constants.UP_1_LEVEL_URL_MAPPING + Constants.ERROR_500_VIEW);
	}

	protected int getUserPermissionFromSession(HttpSession session) {
		return (int) session.getAttribute(Constants.USER_PERMISSION);
	}

	protected void setUserPermissionToSession(HttpSession session, int userPermission) {
		session.setAttribute(Constants.USER_PERMISSION, userPermission);
	}

	protected boolean getOperationSuccessFromSession(HttpSession session) {
		
		Boolean flag = (Boolean) session.getAttribute(Constants.IS_OPERATION_SUCCESS);
		if (flag != null) {
			return flag;
		}
			
		return false;
		
	}

	protected void setOperationSuccessToSession(HttpSession session, boolean isOperationSuccess) {
		session.setAttribute(Constants.IS_OPERATION_SUCCESS, isOperationSuccess);
	}

	protected String getOperationSuccessMessageFromSession(HttpSession session) {
		return (String) session.getAttribute(Constants.OPERATION_SUCCESS_MESSAGE);
	}

	protected void setOperationSuccessMessageToSession(HttpSession session, String operationSuccessMessage) {
		session.setAttribute(Constants.OPERATION_SUCCESS_MESSAGE, operationSuccessMessage);
	}

	protected boolean getOperationErrorFromSession(HttpSession session) {
		
		Boolean flag = (Boolean) session.getAttribute(Constants.IS_OPERATION_ERROR);
		if (flag != null) {
			return flag;
		}
			
		return false;
		
	}

	protected void setOperationErrorToSession(HttpSession session, boolean isOperationError) {
		session.setAttribute(Constants.IS_OPERATION_ERROR, isOperationError);
	}

	protected String getOperationErrorMessageFromSession(HttpSession session) {
		return (String) session.getAttribute(Constants.OPERATION_ERROR_MESSAGE);
	}

	protected void setOperationErrorMessageToSession(HttpSession session, String operationSuccessMessage) {
		session.setAttribute(Constants.OPERATION_ERROR_MESSAGE, operationSuccessMessage);
	}

	protected SelectAdvisorBean getSelectedAdvisorFromSession(HttpSession session) {
		return (SelectAdvisorBean) session.getAttribute(Constants.SELECTED_ADVISOR);
	}

	protected void setSelectedAdvisorToSession(HttpSession session,
			SelectAdvisorBean selectedAdvisor) {
		session.setAttribute(Constants.SELECTED_ADVISOR, selectedAdvisor);
	}

	protected void displaySuccessMessage(Model model, HttpSession session, Locale locale) {

		boolean isSuccessful = getOperationSuccessFromSession(session);
		if (isSuccessful) {
			model.addAttribute(Constants.MODEL_SUCCESS_TITLE, screenDetailBusinessService.getResourceString("success.title", locale));
			model.addAttribute(Constants.MODEL_SUCCESS_MESSAGE, getOperationSuccessMessageFromSession(session));
		} else {
			setOperationSuccessMessageToSession(session, null);
		}

		model.addAttribute(Constants.MODEL_OPERATION_SUCCESS, isSuccessful);

	}
	
	protected void displayErrorMessage(Model model, HttpSession session, Locale locale) {

		boolean hasError = getOperationErrorFromSession(session);
		if (hasError) {
			model.addAttribute(Constants.MODEL_ERROR_TITLE, screenDetailBusinessService.getResourceString("error.title", locale));
			model.addAttribute(Constants.MODEL_ERROR_MESSAGE, getOperationErrorMessageFromSession(session));
		} else {
			setOperationErrorMessageToSession(session, null);
		}

		model.addAttribute(Constants.MODEL_OPERATION_ERROR, hasError);

	}

}
