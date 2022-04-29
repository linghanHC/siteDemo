package ca.gc.hc.siteDemo.controllers.examples;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ca.gc.hc.siteDemo.constants.Constants;
import ca.gc.hc.siteDemo.constants.testData.InputFieldsTestData;
import ca.gc.hc.siteDemo.controllers.BaseController;
import ca.gc.hc.siteDemo.forms.InputFieldsForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class InputsController extends BaseController {

	@RequestMapping(Constants.INPUT_FIELDS_URL_MAPPING)
	public String displayPage(Model model, HttpSession session, Locale locale) {

		try {

			// check for valid access permission
			int permission = getUserPermissionFromSession(session);      
			if (!securityBusinessService.hasValidUserPermission(permission)) {
				log.error(getClass().getName() + "::displayPage: Invalid user permission " + permission);
				return getError500View(model, locale);    	  
			}     
									
			// get test data
			InputFieldsForm inputFieldsForm = InputFieldsTestData.getBasicInputFiledsTestData();
				
			// add style sheet for CSS print
			addCustomCssToModel(model);

			// reset display success message flag
			setOperationSuccessToSession(session, false);

			model.addAttribute(Constants.PAGE_TITLE, screenDetailBusinessService.getPageTitle("label.examples.inputFields.pageTitle", locale));
			model.addAttribute(Constants.INCLUDE_TIMEOUT, true);
			model.addAttribute(Constants.IS_USER_READONLY_ACCESS, securityBusinessService.isUserAccessReadonly(permission));
			model.addAttribute(Constants.MODEL_FORM, inputFieldsForm);
			
			return Constants.INPUT_FIELDS_VIEW;

		} catch (Exception ex) {
			log.error(getClass().getName() + "::displayPage: " + ex.getMessage());
			return getError500View(model, locale);
		}
	}
	
	@RequestMapping(Constants.DO_INPUT_FIELDS_URL_MAPPING)
	public ModelAndView submit(Model model, HttpSession session, Locale locale,
			@ModelAttribute InputFieldsForm modelForm) {

		try {

			// check for valid access permission
			int permission = getUserPermissionFromSession(session);      
			if (!securityBusinessService.hasValidUserPermission(permission)) {
				log.error(getClass().getName() + "::submit: Invalid user permission " + permission);
				return redirectError500View(model, locale);    	  
			}     
	
			// Save data
			
			// add style sheet for CSS print
			addCustomCssToModel(model);
			
			// display operation success message
			setOperationSuccessToSession(session, true);
			setOperationSuccessMessageToSession(session, screenDetailBusinessService.getResourceString("success.inputFields.updated", locale));

			model.addAttribute(Constants.MODEL_FORM, modelForm);
			
			return redirect(Constants.UP_1_LEVEL_URL_MAPPING);

		} catch (Exception ex) {
			log.error(getClass().getName() + "::submit: " + ex.getMessage());
			return redirectError500View(model, locale);
		}
	}

}
