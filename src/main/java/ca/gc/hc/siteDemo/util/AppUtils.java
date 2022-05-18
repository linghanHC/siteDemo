package ca.gc.hc.siteDemo.util;

import ca.gc.hc.siteDemo.services.SearchDrugService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AppUtils {

	private static Logger log = LoggerFactory.getLogger(AppUtils.class);

	public boolean isLanguageFrench(Locale locale) {
		return ApplicationGlobals.LANG_FR.equals(locale.getLanguage());
	}

	public String getLanguageOfPart(String englishProperty, String frenchProperty, boolean isFrench) {
		String result = "";

		if (englishProperty == null && frenchProperty == null) {
			//result will be an empty String
			log.debug("Language of part for " + englishProperty + ", " + frenchProperty + " is an empty string");
		}else if (isFrench) {
			if (frenchProperty == null) {
				result = ApplicationGlobals.LANG_EN;
			}
		}else if (englishProperty == null) {
			result = ApplicationGlobals.LANG_FR;
		}
		log.debug("Language of part for " + englishProperty + ", " + frenchProperty + "= " + result);
		return result;
	}
}
