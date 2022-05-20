package ca.gc.hc.siteDemo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ca.gc.hc.siteDemo.models.DrugClass;
import ca.gc.hc.siteDemo.models.ExternalStatus;
//import org.apache.struts.Globals;
//import org.apache.struts.util.LabelValueBean;
//
//import ca.gc.hc.dao.SearchDrugDao;
//import ca.gc.hc.model.DrugClass;
//import ca.gc.hc.model.ExternalStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*******************************************************************************
 * An object used to store constants and other relatively static values that
 * are used throughout the application. "relatively static values" are lazy
 * initialized and are provided with a clear() method should an action occur
 * where their values could be outdated. They will require an active Hibernate
 * Session when initializing. Note that this is implemented as a Singleton.
 */
public final class ApplicationGlobals {

	//todo move to Constants.java
	public static final String APP_VERSION = "1.0 beta1";
	public static final String LANG_EN = Locale.ENGLISH.getLanguage();
	public static final String LANG_FR = Locale.FRENCH.getLanguage();
	public static final String SELECT_ALL_EN = "Select all";		//todo move to messages.properties
	public static final String SELECT_ALL_FR = "Aucune restriction";
	public static final String ACTIVE_DRUG_STATUS_ID = "2"; //This needs to equal the wqry_status.status_code of 'Marketed (Notified)',
															// or any equivalent description that means "active"
	public final static String APPROVED = "99";
	
	
	//Keys used to store instances in Application, Session, or Request attributes
	public static final String APP_GLOBALS = "appGlobals"; //Application
	public static final String DRUG_CLASS_NAMES = "drugClassNames";
	public static final String ERROR_PSWD_EXCEED_TIME_CHANGE = "error_pswd_exceed_time_change";
	public static final String ERROR_OBSERVATION_COMMENT = "error.observation_comment";
	public static final String ERROR_FILE_NOT_EXIST_ACCOUNT_LOCKED = "error.file_not_exist_account_lock";
	public static final String ERROR_EMPTY_FIELD = "error.empty_file";
	public static final String ERROR_TOTAL_REQUESTED = "error.total_requested";
	public static final String ERROR_RECORD_NO_MATCH = "error.no_results";
	public static final String ERROR_OPENING_PDF_DOCUMENT = "error_opening_pdf_document"; 
	public static final String ERROR_OPENING_IMAGE = "error_opening_image";
	public static final String ERROR_CONNECTING_URL = "error_connecting_url";
	public static final String ERROR_PDF_NOT_FOUND = "error_pdf_not_found";
	public static final String ERROR_IMAGE_NOT_FOUND = "error_image_not_found";
	
	
	/**
	* The bean name for the toggle locale URL bean and the toggle locale display
	* cache bean.  String constants defined for the locales for french and
	* english.
	*/
	static public final String TOGGLE_LOCALE_URL_BEAN = "toggleURL";
	static public final String TOGGLE_LOCALE_DISPLAY_CACHE_BEAN = "displayCache";
	static public final String LOCALE_STRING_FRENCH = "FR_CA";
	static public final String LOCALE_STRING_ENGLISH = "EN_CA";
	static public final String PARAM_SELECTED_LANGUAGE = "selectedLanguage";

	/**
	 * The name of the object in the HttpSession holding the search result object
	 * and search criteria object.
	 */
	static public final String SEARCH_RESULT_KEY = "dpdSearchResults";
	static public final String USER_SEARCH_CRITERIA = "dpdSearchCriteria"; // as typed by user: used in GUI
	static public final String LAST_SEARCH_CRITERIA = "dpd.last.search.criteria"; // last full search criteria to patch back button issue
	static public final String SELECTED_PRODUCT = "dpdSelectedProduct";
	static public final String PAGER_FORM = "dpd.pager";
	static public final String SELECTED_STATUS = "dpd.selected.status";

	//DataTables
	static public final String QUERY_SEARCH_CRITERIA = "dpd.query.search.criteria"; // with quotes doubled: used for queries
	static public final String PAGE_LENGTH_KEY = "page_length";
	static public final int INITIAL_PAGE_LENGTH_VALUE = 25;
	static public final String RESULT_COUNT_KEY = "result_count";
	static public final String DATA_TABLES_PROCESSING_TOGGLE_KEY = "DT.server.processing.results.toggle";
	static public final String DATA_TABLES_PROCESSING_TOGGLE_VALUE = "dataTables.toggle.value";
	static public final String AJAX_BEAN = "ajax.bean";
	static public final String AJAX_STATUS = "ajax.status"; 
//	private int dataTablesProcessingToggleLevel = 0;
	/* index of the Brand name column in the DataTable */
	public static final String DATA_TABLE_BRAND_NAME_COLUMN = "3";
	
	
	public final static String ACTIVE_CODE = "A";
	public final static String DISCONTINUE_CODE = "D";
	public final static Long APPROVED_STATUS_CODE = 1L;

	
	/**
	* String constants defined for the resource bundle for the redirect page.
	*/
	public static final String REDIRECT_FILE = "Redirect";
	public static final String REDIRECT_ROOT = "root";
	public static final String SQL_QUERY = "";

	/**
	* Hide the DIN for radiopharmaceutical products
	* ADR0183 October 2014
	*/
	public final static Long RADIOPHARMACEUTICAL_CLASS_CODE = 9L;
	public final static String NOT_APPLICABLE_E = "Not Applicable";
	public final static String NOT_APPLICABLE_F = "Sans objet";
	
	/**
	* Hide the DIN for radiopharmaceutical products which do not have a Schedule C DIN
	* ADR0286  January 2018
	*/
	public final static Long SCHDEULE_C_DIN_NOT_ISSUED = 4L;

	/**
	* If DIN has Special Identifier "Biosimilar" and has the schedule "D"
	* It is biosimilar biologic drug
	* ADR1338-1920  January 2019
	*/
	public final static int BIOSIMILAR_SCHDEULE_D_CODE = 7;
	public final static Long BIOSIMILAR_SPECIAL_IDENTIFIER_CODE = 6L;
	
	// Only Veterinary Class will have species
	public final static Long VETERINARY_CLASS_CODE = 8L;
	
	private static Logger log = LoggerFactory.getLogger(ApplicationGlobals.class);
	

}
