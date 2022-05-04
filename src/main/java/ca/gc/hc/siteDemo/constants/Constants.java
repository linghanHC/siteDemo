package ca.gc.hc.siteDemo.constants;

public class Constants {

    //java script
    public static final String JS_INCLUDES_FILE = "/js/main.js";
    
    //CSS
    public static final String CSS_INCLUDES_FILE = "/css/custom.css";
        
	//Session attribute names
    public static final String USER_PERMISSION = "userPermission";
	public static final String IS_OPERATION_SUCCESS = "isOperationSuccess";
	public static final String OPERATION_SUCCESS_MESSAGE = "operationSuccessMessage";	
	public static final String IS_OPERATION_ERROR = "isOperationError";
	public static final String OPERATION_ERROR_MESSAGE = "operationErrorMessage";
	public static final String SELECTED_ADVISOR = "selectedAdvisor";
		
    public static final String ROOT_URL_MAPPING = "/";
       
	//Request view pages
	public static final String MAIN_MENU_VIEW = "home";

    public static final String LOGIN_VIEW = "authentication/login";
	public static final String LOGOUT_VIEW = "authentication/logout";
	public static final String DO_LOGIN_VIEW = "authentication/doLogin";

	public static final String SELECTION_LIST_VIEW = "examples/selectionList";
	public static final String DO_SELECT_EXPERT_VIEW = "examples/doSelectExpert";

	public static final String INPUT_FIELDS_VIEW = "examples/inputs";
	public static final String DO_INPUT_FIELDS_VIEW = "examples/doInputs";

	public static final String ERROR_VIEW = "error";
    public static final String ERROR_404_VIEW = "error404";
    public static final String ERROR_409_VIEW = "error409";
    public static final String ERROR_500_VIEW = "error500";
	
    //Request mappings
    public static final String UP_1_LEVEL_URL_MAPPING = "../";
       
    public static final String LOGIN_URL_MAPPING = ROOT_URL_MAPPING + LOGIN_VIEW;
    public static final String LOGOUT_URL_MAPPING = ROOT_URL_MAPPING + LOGOUT_VIEW;
    public static final String DO_LOGIN_URL_MAPPING = ROOT_URL_MAPPING + DO_LOGIN_VIEW;

    public static final String SELECTION_LIST_URL_MAPPING = ROOT_URL_MAPPING + SELECTION_LIST_VIEW;
    public static final String DO_SELECT_EXPERT_URL_MAPPING = ROOT_URL_MAPPING + DO_SELECT_EXPERT_VIEW;

    public static final String INPUT_FIELDS_URL_MAPPING = ROOT_URL_MAPPING + INPUT_FIELDS_VIEW;
    public static final String DO_INPUT_FIELDS_URL_MAPPING = ROOT_URL_MAPPING + DO_INPUT_FIELDS_VIEW;

    //Model attribute names
    public static final String JS_INCLUDES = "jsIncludes";
    public static final String CSS_INCLUDES = "cssIncludes";
    public static final String PAGE_TITLE = "pageTitle";
    public static final String INCLUDE_TIMEOUT = "includeTimeout";
    public static final String IS_USER_READONLY_ACCESS = "userReadonlyAccess";
    public static final String IS_USER_READWRITE_ACCESS = "userReadWriteAccess";
    
    public static final String ADVISOR_LIST = "advisorList";
    
    public static final String MODEL_LOGIN_FORM = "userLoginForm";
    public static final String MODEL_OPERATION_SUCCESS = "operationSuccess";
    public static final String MODEL_OPERATION_ERROR = "operationError";
    public static final String MODEL_SUCCESS_TITLE = "successTitle";
    public static final String MODEL_SUCCESS_MESSAGE = "successMessage";
    public static final String MODEL_ERROR_TITLE = "errorTitle";
    public static final String MODEL_ERROR_MESSAGE = "errorMessage";
    public static final String MODEL_FORM = "modelForm";

    //Cookies
    public static final String LANGUAGE_COOKIE = "lang";
    public static final String ENGLISH = "eng";
    public static final String FRENCH = "fra";
    
    //Switch Language
    public static final String FRENCH_TWO_CHARS = "fr";

	//=====================
	public static final String REDIRECT = "redirect:";

	public static final String SEARCH_VIEW = "search";
	public static final String SEARCH_URL_MAPPING = ROOT_URL_MAPPING + SEARCH_VIEW;

	public static final String SEARCH_RESULTS_VIEW = "search_results";
	public static final String SEARCH_RESULTS_URL_MAPPING = ROOT_URL_MAPPING + SEARCH_RESULTS_VIEW;
}