package ca.gc.hc.siteDemo.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.gc.hc.siteDemo.constants.Constants;

@Controller
public class HomeController extends BaseController {

  @RequestMapping(Constants.ROOT_URL_MAPPING)
  public String welcome(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session,
	      Locale locale) {
	    
 	  setUserPermissionToSession(session, 1); // test data
		
      // check if we can display operation success message
      displaySuccessMessage(model, session, locale);
      displayErrorMessage(model, session, locale);

	  return Constants.MAIN_MENU_VIEW;  
  }

}
