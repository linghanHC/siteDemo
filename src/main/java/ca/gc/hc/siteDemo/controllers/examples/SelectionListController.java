package ca.gc.hc.siteDemo.controllers.examples;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ca.gc.hc.siteDemo.constants.Constants;
import ca.gc.hc.siteDemo.constants.testData.AdvisorTestData;
import ca.gc.hc.siteDemo.controllers.BaseController;
import ca.gc.hc.siteDemo.models.SelectAdvisorBean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SelectionListController extends BaseController {

	private List<SelectAdvisorBean> advisorList;

	@RequestMapping(Constants.SELECTION_LIST_URL_MAPPING)
	public String displayPage(Model model, HttpSession session, Locale locale) {

		try {

			// reset display operation success flag
			setOperationSuccessToSession(session, false);
			setOperationErrorToSession(session, false);

			// check for valid access permission
			int permission = getUserPermissionFromSession(session);      
			if (!securityBusinessService.hasValidUserPermission(permission)) {
				log.error(getClass().getName() + "::displayPage: Invalid user permission " + permission);
				return getError500View(model, locale);    	  
			}     

			advisorList = AdvisorTestData.getBasicAdvisorListTestData(false); // test data
										
			// add style sheet for CSS print
			addCustomCssToModel(model);
		
			model.addAttribute(Constants.PAGE_TITLE, screenDetailBusinessService.getPageTitle("label.examples.selectionList.pageTitle", locale));
			model.addAttribute(Constants.INCLUDE_TIMEOUT, true);
			model.addAttribute(Constants.IS_USER_READONLY_ACCESS, securityBusinessService.isUserAccessReadonly(permission));
			model.addAttribute(Constants.ADVISOR_LIST, advisorList);

			// check if we can display operation success message
			displaySuccessMessage(model, session, locale);
			displayErrorMessage(model, session, locale);

			return Constants.SELECTION_LIST_VIEW;

		} catch (Exception ex) {
			log.error(getClass().getName() + "::displayPage: " + ex.getMessage());
			return getError500View(model, locale);
		}
	}

	@RequestMapping(Constants.DO_SELECT_EXPERT_URL_MAPPING)
	public ModelAndView submit(Model model, HttpSession session, Locale locale,
			@RequestParam(value = "advisorTblIndex", required = true) String advisorTblIndex) {

		try {

			// check for valid access permission
			int permission = getUserPermissionFromSession(session);      
			if (!securityBusinessService.hasValidUserPermission(permission)) {
				log.error(getClass().getName() + "::submit: Invalid user permission " + permission);
				return redirectError500View(model, locale);    	  
			}     

			// Record the advisor selected
			int tableIndex = Integer.parseInt(advisorTblIndex);
			setSelectedAdvisorToSession(session, advisorList.get(tableIndex));

			// reset display operation success flag
			setOperationSuccessToSession(session, false);

			// Moving onto advisor menu
			return redirect(Constants.UP_1_LEVEL_URL_MAPPING);

		} catch (Exception ex) {
			log.error(getClass().getName() + "::submit: " + ex.getMessage());
			return redirectError500View(model, locale);
		}
	}

}
