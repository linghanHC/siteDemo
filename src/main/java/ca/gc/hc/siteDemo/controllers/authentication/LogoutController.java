package ca.gc.hc.siteDemo.controllers.authentication;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.gc.hc.siteDemo.constants.Constants;
import ca.gc.hc.siteDemo.controllers.BaseController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LogoutController extends BaseController {

  @RequestMapping(Constants.LOGOUT_URL_MAPPING)
  public String displayPage(Model model, HttpSession session, Locale locale) {

    try {

  	  // reset display operation success flag
  	  setOperationSuccessToSession(session, false);
  	  setOperationErrorToSession(session, false);

      setUserPermissionToSession(session, 0);
      
      // add style sheet for CSS print
      addCustomCssToModel(model);

      model.addAttribute(Constants.PAGE_TITLE, screenDetailBusinessService.getPageTitle("label.authentication.logout.pageTitle", locale));
      model.addAttribute(Constants.INCLUDE_TIMEOUT, false);

      return Constants.LOGOUT_VIEW;

    } catch (Exception ex) {
      log.error(getClass().getName() + "::displayPage: " + ex.getMessage());
      return getError500View(model, locale);
    }
  }

}
