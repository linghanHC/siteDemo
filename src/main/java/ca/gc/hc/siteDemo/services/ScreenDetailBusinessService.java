package ca.gc.hc.siteDemo.services;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScreenDetailBusinessService {

  @Autowired
  private MessageSource messageSource;

  public String getResourceMessage(String code, Locale locale) {
    try {
      MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource, locale);
      return accessor.getMessage(code);
    } catch (Exception ex) {
      log.error(getClass().getName() + "::getResourceMessage: Unable to get resource string.");
      return null;
    }
  }

  public String getPageTitle(String code, Locale locale) {
    return getResourceMessage(code, locale) + " - " +
        getResourceMessage("application.title", locale);
  }

  public String getResourceString(String code, Locale locale) {
	    return getResourceMessage(code, locale);
	  }

}
