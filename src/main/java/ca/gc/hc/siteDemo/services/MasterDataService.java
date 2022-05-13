package ca.gc.hc.siteDemo.services;

import ca.gc.hc.siteDemo.bean.LabelValueBean;
import ca.gc.hc.siteDemo.bean.MasterData;
import ca.gc.hc.siteDemo.dao.SearchDrugDao;
import ca.gc.hc.siteDemo.models.DrugClass;
import ca.gc.hc.siteDemo.models.ExternalStatus;
import ca.gc.hc.siteDemo.util.ApplicationGlobals;
import ca.gc.hc.siteDemo.util.ExternalStatusComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

// copied from ApplicationGlobals.java	todo remove the comment

@Service
public class MasterDataService {

	private static Logger log = LoggerFactory.getLogger(MasterDataService.class);

	@Autowired
	private SearchDrugDao dao;

	/***************************************************************************
	 * Gets the language of the user based on a previous call to
	 * initLocalization(). If it has not been set, this defaults it to ApplicationGlobals.ApplicationGlobals.LANG_FR.
	 * @return the language previously set by a call to initLocalization().
	 */
//	public String getUserLanguage() {
//		String language = (String)userLanguage.get();
//
//		if (language == null) {
//			log.error("User Language requested without Localization being properly initialized.");
//			language = ApplicationGlobals.LANG_FR; //Default value
//			userLanguage.set(language);
//		}
//
//		return language;
//	}

	/***************************************************************************
	 * Gets the locale of the user based on a previous call to initLocalization().
	 * If it has not been set, this defaults it to Canadian English.
	 * @return the locale previously set by a call to initLocalization().
	 */
//	public Locale getUserLocale() {
//		Locale locale = (Locale)userLocale.get();
//
//		if (locale == null) {
//			log.info("User Locale requested without Localization being properly initialized.");
//			locale = Locale.CANADA;//Default value
//			userLocale.set(locale);
//		}
//
//		return locale;
//	}

	/***************************************************************************
	 * A method borrowed from org.apache.struts.util.RequestUtils (not available
	 * until version 1.2).
	 * -Dwight Hubley, 2006-05-08
	 * <p>Look up and return current user locale, based on the specified
	 * parameters.</p>
	 *
	 * @param request The request used to lookup the Locale
	 * @param localKey  Name of the session attribute for our user's Locale. If
	 *        this is <code>null</code>, the default locale key is used for the
	 *        lookup.
	 * @return current user locale
	 */
	public Locale getUserLocale(HttpServletRequest request, String localKey) {
		// TODO to be removed, no use
		Locale userLocale = null;
//		HttpSession session = request.getSession(false);
//
//		if (localKey == null) {
//			localKey = Globals.LOCALE_KEY;
//		}
//
//		// Only check session if sessions are enabled
//		if (session != null) {
//			userLocale = (Locale) session.getAttribute(localKey);
//		}
//
//		if (userLocale == null) {
//			// Returns Locale based on Accept-Language header or the server default
//			userLocale = request.getLocale();
//		}
//
		return userLocale;
	}

	/***************************************************************************
	 * This should be called prior to any requests for collections of language-
	 * specific items so that they will be returned in the correct language.
	 * Typically called by Actions or at the top of a JSP, this stores the
	 * language in a ThreadLocal object so that subsequent access to these
	 * collections during the current Request will be in the appropriate
	 * language. It determines the language requested by the user based on their
	 * Locale. All Locales with a language other than French will use English.
	 * @param request the servlet request we are processing.
	 */
//	public void initLocalization(HttpServletRequest request) {
//
//		Locale locale = getUserLocale(request, null);
//		setUserLocale (locale);
//
//	}

	/* 
	 * - Set locale to English if locale is null
	 * - Set userLanguage to English and locale to Locale.Canada 
	 *   if locale language is not French. 
	 */
//	public static void setUserLocale(Locale locale) {
//
//
//	    if (locale == null)
//			locale = Locale.CANADA; //Default value
//		userLocale.set(locale);
//
//		if (ApplicationGlobals.LANG_FR.equals(locale.getLanguage()))
//			userLanguage.set(ApplicationGlobals.LANG_FR);
//		else userLanguage.set(ApplicationGlobals.LANG_FR); //Default value
//
//	}
//	/***************************************************************************
//	 * Gets a collection of all of the status that are in the system. These
//	 * are Locale dependant, so initLocalization(request) must have already been
//	 * called during the processing of this request.
//	 * @return a collection of valid status descriptions.
//	 */
//	public List getStatus() throws Exception {
//		if (statusMap.isEmpty()) {
//			this.loadUniqueStatuses();
//		}
//		return (List)statusMap.get(getUserLanguage());
//	}
//
//	public HashMap getStatusMap() throws Exception {
//		if (this.statusMap.isEmpty()) {
//			this.loadUniqueStatuses();
//		}
//		return statusMap;
//	}


//	public List<String> getUniqueRoutes() throws Exception {
//		if (uniqueRoutesMap.isEmpty()) {
//			loadUniqueRoutes();
//		}
//		return (List)uniqueRoutesMap.get(getUserLanguage());
//	}
//
//	public List getUniqueForms() throws Exception {
//		if (this.uniqueFormsMap.isEmpty()) {
//			loadUniquePharmaceuticalForms();
//		}
//		return (List)uniqueFormsMap.get(getUserLanguage());
//	}
//
//	public List<LabelValueBean> getUniqueDrugClasses() throws Exception {
//		if (this.drugClassMap.isEmpty()) {
//			this.loadUniqueDrugClasses();
//		}
//		return (List)drugClassMap.get(getUserLanguage());
//	}
//
//	public List getUniqueSchedules() throws Exception {
//		if (uniqueSchedulesMap.isEmpty()) {
//			loadUniqueSchedules();
//		}
//		return (List)uniqueSchedulesMap.get(getUserLanguage());
//	}

//	public List getUniqueSpecies() throws Exception {
//		if (uniqueSpeciesMap.isEmpty()) {
//			loadUniqueSpecies();
//		}
//		return (List)uniqueSpeciesMap.get(getUserLanguage());
//	}

	/**
	 * Populates distinct routes of administration for use in combo box. 
	 * @author Sylvain Larivière 2009-09-10
	 * Updated 2009-10-26 to allow English and French lists
	 */
	private HashMap<String, List<LabelValueBean>> loadUniqueRoutes() throws Exception {

		try {
			HashMap<String,List<String>> routesMap = dao.retrieveUniqueRoutes();

			if (routesMap != null) {
				HashMap<String, List<LabelValueBean>> uniqueRoutesMap = new HashMap<String,List<LabelValueBean>>();
				populateDrugSearchCriteriaList(routesMap, uniqueRoutesMap);
				return uniqueRoutesMap;
			}
		}catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("loadUniqueRoutes [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return null;
	}

	/**
	 * Populates distinct pharmaceutical forms for use in combo box. 
	 * @author Sylvain Larivière 2009-09-10
	 * Updated 2009-10-26 to allow English and French lists
	 */
	private HashMap<String, List<LabelValueBean>> loadUniquePharmaceuticalForms() throws Exception {

		try {
			HashMap<String,List<String>> formsMap = dao.retrieveUniqueForms();
			if (formsMap != null) {
				HashMap<String, List<LabelValueBean>> uniqueFormsMap = new HashMap<String,List<LabelValueBean>>();
				populateDrugSearchCriteriaList(formsMap, uniqueFormsMap);
				return uniqueFormsMap;
			}

		} catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("loadUniquePharmaceuticalForms [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return null;
	}

	/**
	 * Populates distinct schedules for use in combo box. 
	 * @author Sylvain Larivière 2009-09-10
	 * Updated 2009-10-26 to allow English and French lists
	 */
	private HashMap<String, List<LabelValueBean>> loadUniqueSchedules() throws Exception {

		try {
			HashMap<String,List<String>> schedulesMap = dao.retrieveUniqueSchedules();

			if (schedulesMap != null) {
				HashMap<String, List<LabelValueBean>> uniqueSchedulesMap = new HashMap<String,List<LabelValueBean>>();
				populateDrugSearchCriteriaList(schedulesMap, uniqueSchedulesMap);
				return uniqueSchedulesMap;
			}
		}catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("loadUniqueSchedules [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return null;
	}

	/**
	 * Populates distinct schedules for use in combo box. 
	 * @author Sunny Lo 2017-10-01
	 * Updated 2009-10-26 to allow English and French lists
	 */
	private HashMap<String, List<LabelValueBean>> loadUniqueSpecies() throws Exception {

		try {
			HashMap<String,List<String>> speciesMap = dao.retrieveUniqueSpecies();
			HashMap<String, List<LabelValueBean>> uniqueSpeciesMap = new HashMap<String,List<LabelValueBean>>();
			if (speciesMap != null) {
				populateDrugSearchCriteriaList(speciesMap, uniqueSpeciesMap);
				return uniqueSpeciesMap;
			}
		}catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("loadUniqueSpecies [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return null;
	}
// todo move to ScreenDetailBusinessService  or a util class?
//	public String getSelectAllLabel() {
//		String selectAllLabel = "";
//
//		if (ApplicationGlobals.LANG_FR.equals(getUserLocale().getLanguage())) {
//			selectAllLabel = ApplicationGlobals.SELECT_ALL_FR;
//		}else {
//			selectAllLabel = ApplicationGlobals.SELECT_ALL_EN;
//		}
//		return selectAllLabel;
//	}
	public String getSelectAllLabel(String language) {
		String selectAllLabel = "";
		
		if (language.equals(ApplicationGlobals.LANG_FR)) {
			selectAllLabel = ApplicationGlobals.SELECT_ALL_FR;
		}else {
			selectAllLabel = ApplicationGlobals.SELECT_ALL_EN;
		}
		return selectAllLabel;
	}
	
	
	private void populateDrugSearchCriteriaList(HashMap mapList, HashMap uniqueMap)
		throws Exception {

		try {
			uniqueMap.clear();

			if(mapList.containsKey(ApplicationGlobals.LANG_EN)) {
				generateUniqueMapforLanguage(ApplicationGlobals.LANG_EN, mapList, uniqueMap);

			}
			if(mapList.containsKey(ApplicationGlobals.LANG_FR)) {
				generateUniqueMapforLanguage(ApplicationGlobals.LANG_FR, mapList, uniqueMap);

			}
		} catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("populateDrugSearchCriteriaList [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
	}
	
	private void generateUniqueMapforLanguage(String language, HashMap mapList, HashMap uniqueMap) throws Exception {
		ArrayList<LabelValueBean> uniqueListItems = new ArrayList<LabelValueBean>();

         //add the "SelectAll label"
		uniqueListItems.add(new LabelValueBean(getSelectAllLabel(language), "0"));
		//populate the rest of the list
		ArrayList<String> itemsList = (ArrayList<String>)mapList.get(language);
		Iterator it = itemsList.iterator();

		try {

			for (String item: itemsList) {
				if(item != null) {
					String itemLabel=item.replace("/Hc","/HC");
					uniqueListItems.add(new LabelValueBean(itemLabel, item));
				}

			}
			uniqueMap.put(language, uniqueListItems);
		} catch (Exception e) {
			StringBuffer message = new StringBuffer("generateUniqueMapforLanguage [");
			message.append(language);
			message.append("] failed");
			throw new Exception(message.toString());
		}
	}
	
	/**
	 * Populates distinct statuses for use in combo box.
	 * 
	 * @author Sylvain Larivière 2010-01-25 Updated SL/2014-12-24 ADR0183 to
	 *         implement external status codes
	 */
	public HashMap<String, List<LabelValueBean>> loadUniqueStatuses() throws Exception { log.debug("  ** in service loadUniqueStatuses");
		try {
			List<ExternalStatus> allStatuses = dao.retrieveAllExternalStatuses();

			if (allStatuses != null) {
				return populateDrugStatusSearchCriteria(allStatuses);
			}
		} catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("loadUniqueStatuses [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return null;
	}

	private HashMap<String, List<LabelValueBean>> populateDrugStatusSearchCriteria(
			List<ExternalStatus> statusList) throws Exception {
		HashMap<String, List<LabelValueBean>> statusMap = new HashMap<String,List<LabelValueBean>>();
		statusMap.put(ApplicationGlobals.LANG_FR, generateUniqueStatusMapForLanguage(ApplicationGlobals.LANG_FR,
				statusList));
		statusMap.put(ApplicationGlobals.LANG_EN, generateUniqueStatusMapForLanguage(ApplicationGlobals.LANG_EN,
				statusList));
		return statusMap;
	}

	private ArrayList<LabelValueBean> generateUniqueStatusMapForLanguage(String language,
			List<ExternalStatus> statusList) {
		ArrayList<LabelValueBean> uniqueListItems = new ArrayList<LabelValueBean>();

		// sort statusList based on language
		Collections.sort(statusList, new ExternalStatusComparator(language));

		for (ExternalStatus s : statusList) {
			String localizedStatus = "";
			if (language.equals(ApplicationGlobals.LANG_FR)) {
				localizedStatus = s.getExternalStatusF();
			} else {
				localizedStatus = s.getExternalStatusE();
			}
			uniqueListItems.add(new LabelValueBean(localizedStatus, s
					.getExternalStatusId().toString()));
		}

		// add the "Select ALL" label
		uniqueListItems.add(0, new LabelValueBean(getSelectAllLabel(language),
				"0"));
		return uniqueListItems;
	}

	public MasterData refreshAllSearchPageLists() throws Exception{ log.debug("  * in service refreshAllSearchPageLists");
		MasterData md = new MasterData();
//		statusMap.clear();
		md.setStatusMap(loadUniqueStatuses());
//		uniqueRoutesMap.clear();
		md.setUniqueRoutesMap(loadUniqueRoutes());
//		uniqueFormsMap.clear();
		md.setUniqueFormsMap(loadUniquePharmaceuticalForms());
//		uniqueSchedulesMap.clear();
		md.setUniqueSchedulesMap(loadUniqueSchedules());
//		uniqueSpeciesMap.clear();
		md.setUniqueSpeciesMap(loadUniqueSpecies());
//		drugClassMap.clear();
		md.setDrugClassMap(loadUniqueDrugClasses());
		return md;
	}

	/**
	 * Populates distinct drug classes for use in combo box. 
	 * @author Sylvain Larivière 2014-10-15
	 */
	public HashMap<String,List<LabelValueBean>> loadUniqueDrugClasses() throws Exception {
		try {

			List<DrugClass> allDrugClasses = dao.retrieveUniqueDrugClasses();

			if (allDrugClasses != null) {
				return populateDrugClassSearchCriteria(allDrugClasses);
			}
		}catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("loadUniqueDrugClasses [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return null;
	}

	private HashMap<String,List<LabelValueBean>> populateDrugClassSearchCriteria(List<DrugClass> classList)
			throws Exception {
		HashMap<String,List<LabelValueBean>> drugClassMap = new HashMap<String,List<LabelValueBean>>();
		drugClassMap.put(ApplicationGlobals.LANG_EN, generateUniqueDrugClassMapForLanguage(ApplicationGlobals.LANG_EN, classList));
		drugClassMap.put(ApplicationGlobals.LANG_FR, generateUniqueDrugClassMapForLanguage(ApplicationGlobals.LANG_FR, classList));
		return drugClassMap;
	}

	/**
	 * @param language A String corresponding to either the ApplicationGlobals.ApplicationGlobals.LANG_FR or LANG_FR global constants
	 * @param drugClassList A List of all distinct DrugClass instances in the database
	 * <p>Generates a list of LabelValueBean's containing the localized drug class descriptions and their ID,
	 * and adds it to a Map keyed on the language constant</p>
	 */
	private ArrayList<LabelValueBean> generateUniqueDrugClassMapForLanguage(String language, List<DrugClass> drugClassList) {
		ArrayList<LabelValueBean> uniqueListItems = new ArrayList<LabelValueBean>();
		String localizedDrugClass;

//		add the "SelectAll label"
		uniqueListItems.add(new LabelValueBean(getSelectAllLabel(language), "0"));

		//populate the rest of the list
		for (DrugClass c : drugClassList){
			localizedDrugClass = "";
			if (language.equals(ApplicationGlobals.LANG_FR)) {
				localizedDrugClass = c.getDrugClassF();
			}else{
				localizedDrugClass = c.getDrugClassE();
			}
			uniqueListItems.add(new LabelValueBean(localizedDrugClass, c.getDrugClassId().toString()));
		}
		return uniqueListItems;
	}
}
