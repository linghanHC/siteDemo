package ca.gc.hc.siteDemo.controllers;

import ca.gc.hc.siteDemo.bean.*;
import ca.gc.hc.siteDemo.constants.Constants;
import ca.gc.hc.siteDemo.forms.SearchForm;
import ca.gc.hc.siteDemo.services.JsonBusinessService;
import ca.gc.hc.siteDemo.services.MasterDataService;
import ca.gc.hc.siteDemo.services.SearchDrugService;
import ca.gc.hc.siteDemo.util.AppUtils;
import ca.gc.hc.siteDemo.util.ApplicationGlobals;
import ca.gc.hc.siteDemo.util.StringsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class SearchController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	private SearchDrugService searchService;

	@Autowired
	private MasterDataService mdService;

	@Autowired
	private JsonBusinessService jsonBusinessService;
	
	@Autowired
	private AppUtils appUtils;
	
//	@RequestMapping("test")
//	public String displaytest(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session,
//						  Locale locale) {
//		return "test";
//	}

	@RequestMapping(Constants.SEARCH_URL_MAPPING)
	public String displaySearchPage(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session,
						  Locale locale) {
		log.debug("==display searchpage");
		try {
			SearchForm searchForm = new SearchForm();

			// add style sheet for CSS print
			addCustomCssToModel(model);

			// check if we can display operation success message
//      displaySuccessMessage(model, session, locale);
//      displayErrorMessage(model, session, locale);

			model.addAttribute(Constants.SEARCH_FORM, searchForm);

			logAllSessionAttributes(session);

			MasterData md = null;
			if (session.getAttribute("masterData") == null) {
				md = mdService.refreshAllSearchPageLists();
				session.setAttribute("masterData", md);
			} else {
				md = (MasterData) session.getAttribute("masterData");
			}
			model.addAttribute("statuses", appUtils.isLanguageFrench(locale) ? md.getStatusMap().get(ApplicationGlobals.LANG_FR) :
					md.getStatusMap().get(ApplicationGlobals.LANG_EN));
			model.addAttribute("drugClasses", appUtils.isLanguageFrench(locale) ? md.getDrugClassMap().get(ApplicationGlobals.LANG_FR) :
					md.getDrugClassMap().get(ApplicationGlobals.LANG_EN));
			model.addAttribute("routes", appUtils.isLanguageFrench(locale) ? md.getUniqueRoutesMap().get(ApplicationGlobals.LANG_FR) :
					md.getUniqueRoutesMap().get(ApplicationGlobals.LANG_EN));
			model.addAttribute("dosages", appUtils.isLanguageFrench(locale) ? md.getUniqueFormsMap().get(ApplicationGlobals.LANG_FR) :
					md.getUniqueRoutesMap().get(ApplicationGlobals.LANG_EN));
			model.addAttribute("schedules", appUtils.isLanguageFrench(locale) ? md.getUniqueSchedulesMap().get(ApplicationGlobals.LANG_FR) :
					md.getUniqueSchedulesMap().get(ApplicationGlobals.LANG_EN));
			model.addAttribute("species", appUtils.isLanguageFrench(locale) ? md.getUniqueSpeciesMap().get(ApplicationGlobals.LANG_FR) :
					md.getUniqueSpeciesMap().get(ApplicationGlobals.LANG_EN));

		} catch (Exception e) {
			e.printStackTrace();
			return Constants.ERROR_500_VIEW;
		}

		return Constants.SEARCH_VIEW;
	}

	@PostMapping("/search")
	public String doSearch(Model model, HttpServletRequest request, Locale locale,
						   @ModelAttribute SearchForm searchForm, RedirectAttributes redirectAttributes) {

//		public String doSearch(Model model, HttpSession session, Locale locale,
//				@Valid @ModelAttribute InputFieldsForm modelForm, BindingResult result) {
//			if (result.hasErrors()) {
//				List<ObjectError> allErrors =  result.getAllErrors();
//		for (ObjectError err : allErrors) {
//			logger.debug(err);
//		}
//			}
//		}
		log.debug("==doSearch "+searchForm.toString());

		request.getSession().setAttribute("searchForm", searchForm);
		// copied from SearchAction.execute()
//		HttpSession session = request.getSession();
//		ActionForward forward = new ActionForward();
//		ActionMessages messages = new ActionMessages();
//
//		if (session.getAttribute("sessionActive") != null) {
//
//			ActionUtil.setRequestLocale(request);
//			if (request.getAttribute(CANCEL_PROPERTY_NAME) != null) {
//				request.removeAttribute("SearchForm");
//				return (mapping.findForward("SearchPage"));
//			}
//
//			/*
//			 * !! IMPORTANT !! : On the Tomcat server in the HRE environment,
//			 * when a search returns too many results, it takes too long for the
//			 * DataTable to load all results and while this is happening, (many
//			 * seconds) users cannot interact with it.
//			 *
//			 * When the number of results exceeds the limit set in
//			 * ApplicationResources, only the first 25 results will be returned
//			 * initially and the DataTable will request more results as
//			 * required, somewhat similar to the pre-WET4 implementation (no
//			 * DataTable) where the data was always paged to 25 results per
//			 * page.
//			 *
//			 * The AjaxBean is used to capture the DataTable's request
//			 * parameters. See also method processAjaxRequest() herein.
//			 *
//			 * See http://legacy.datatables.net/usage/server-side (until version
//			 * 10 or later of DataTables starts being used)
//			 */
			AjaxBean ajaxBean = null;
			SearchCriteriaBean criteria = null;
//			SearchForm thisForm = (SearchForm) form;
		String forward = Constants.SEARCH_RESULTS_URL_MAPPING;

		List list = new ArrayList();

		/*
		 * See if we are in server-processing mode. Parameter sEcho is
		 * always generated by the DataTable when it is in server-processing
		 * mode, and its value is incremented with each request.
		 *
		 * See http://legacy.datatables.net/usage/server-side (until version
		 * 10 or later of DataTables starts being used)
		 */
//			String echo = request.getParameter("sEcho");
//			boolean isDataTableServerSideProcessing = echo != null
//					&& !echo.equals("0");
//
		try {
//				if (isDataTableServerSideProcessing) {
//					ajaxBean = (AjaxBean) session
//							.getAttribute(ApplicationGlobals.AJAX_BEAN);
//					SearchCriteriaBean safeCrit = (SearchCriteriaBean) session
//							.getAttribute(ApplicationGlobals.QUERY_SEARCH_CRITERIA);
//					ActionUtil.processAjaxRequest(ajaxBean, request, safeCrit,
//							response, messages);
//					session.setAttribute(ApplicationGlobals.AJAX_BEAN, ajaxBean);
//
//				} else {
					// Not an Ajax request: process for normal JSP

					criteria = new SearchCriteriaBean();

					BeanUtils.copyProperties(searchForm, criteria);
//					session.setAttribute(ApplicationGlobals.SELECTED_STATUS,
//							criteria.getStatusCode());
//					/*
//					 * Map the selected drug class codes to their descriptions,
//					 * save these descriptions in a single comma-separated
//					 * String, and store in a session attribute for displaying
//					 * back to the user in the gui.
//					 */
//					ActionUtil.mapDrugClassNames(criteria.getDrugClass(),
//							session);
//
					/*
					 * Determine type of search and process accordingly
					 */
					if ((StringsUtil.hasData(searchForm.getAtc()) || (StringsUtil
							.hasData(searchForm.getDin())))) {
						list = searchService.processSearchByAtcOrDin(request, criteria);
//						/*
//						 * SL/2012-09-04: If searching by DIN or ATC: remove
//						 * default status value since this product may be
//						 * inactive, in which case ShortcutSearchAction, if run
//						 * for an AIG search, will be using an incorrect status
//						 * as part of its criteria
//						 */
//						//thisForm.setStatus(null);
					} else {
						list = searchService.processSearchByNames(request, criteria, locale);
					}

			log.debug("Total match found: [" + list.size() + "].");

//			if (list.size() == 0) {
//						request.getSession().setAttribute(
//								ApplicationGlobals.RESULT_COUNT_KEY, 0);
//						// A "No match found" message will display in the
//						// results page
//						return mapping.findForward("multiplematch");

//			} else
			if (list.size() == 1) {
				DrugBean bean = (DrugBean) list.get(0);
				redirectAttributes.addFlashAttribute(ApplicationGlobals.SELECTED_PRODUCT, bean);
				forward = Constants.PRODUCT_INFO_URL_MAPPING;
			} else {
				redirectAttributes.addFlashAttribute(ApplicationGlobals.SEARCH_RESULT_KEY, list);
				forward = Constants.SEARCH_RESULTS_URL_MAPPING;
			}

//				}
		} catch (Exception e) {
			log.error(e.getMessage());
//				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
//						"error.failure.system"));
			forward = Constants.ERROR_500_VIEW;

		}

		// Report any errors we have discovered back to the original form
//			if (!messages.isEmpty()) {
//				saveMessages(request, messages);
//				forward = (mapping.getInputForward());
//			}

			if (ajaxBean != null ){
//			if (ajaxBean != null
//					&& AjaxRequestStatus.ACTIVE == ajaxBean.getAjaxStatus()) {
				forward = null;		//todo
			} else if (list.size() > 1) {
//				request.getSession().setAttribute(
//						ApplicationGlobals.SEARCH_RESULT_KEY, list);
//				forward = (mapping.findForward("multiplematch"));
//				forward = Constants.SEARCH_RESULTS_URL_MAPPING;
			} else if (list.size() == 1) {
//				forward = (mapping.findForward("onematch"));
//				forward = Constants.PRODUCT_INFO_URL_MAPPING;
			} else {
//				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
//						"error.failure.system"));
//				saveMessages(request, messages);
//				forward = (mapping.getInputForward());
			}
//
//		} else {
//			// inactive session
//			forward = mapping.findForward("sessionTimeout");
//		}


		return redirectTo(forward);
	}

	@RequestMapping(Constants.SEARCH_RESULTS_URL_MAPPING)
	public String displaySearchResult(Model model, Locale locale, HttpServletRequest request) throws Exception {
		SearchForm searchForm = (SearchForm) request.getSession().getAttribute("searchForm");
//		searchForm.setSelectedStatusText(getSelectedStatusText(searchForm.getStatus(),locale, ((MasterData)request.getSession().getAttribute("masterData")).getStatusMap().get(locale.getLanguage())));

		log.debug("==displaySearchResult");
		if (model.asMap().get(ApplicationGlobals.SEARCH_RESULT_KEY) == null) {
			log.debug("drug bean is not in the redirect flash attribute");
			return redirectTo(Constants.SEARCH_VIEW);
		} else {
			List<DrugSummary> list = (List<DrugSummary>) model.asMap().get(ApplicationGlobals.SEARCH_RESULT_KEY);
			log.debug(list.size()+"");
		}

		return Constants.SEARCH_RESULTS_URL_MAPPING;
	}

	@RequestMapping(Constants.PRODUCT_INFO_URL_MAPPING)
	public String displayProductInfo(Model model, Locale locale, HttpServletRequest request) throws Exception {
		SearchForm searchForm = (SearchForm) request.getSession().getAttribute("searchForm");
		searchForm.setSelectedStatusText(getSelectedStatusText(searchForm.getStatus(),locale, ((MasterData)request.getSession().getAttribute("masterData")).getStatusMap().get(locale.getLanguage())));

		log.debug("==displayProductInfo");
		if (model.asMap().get(ApplicationGlobals.SELECTED_PRODUCT) == null) {
			log.debug("drug bean is not in the redirect flash attribute");
		} else {
			DrugBean bean = (DrugBean) model.asMap().get(ApplicationGlobals.SELECTED_PRODUCT);
			log.debug(bean.toString());
			model.addAttribute(ApplicationGlobals.SELECTED_PRODUCT, bean);
		}

		return Constants.PRODUCT_INFO_URL_MAPPING;
	}

	@RequestMapping(Constants.INFO_URL_MAPPING+"{code}")
	public String searchByDrugCode(Model model, HttpServletRequest request, Locale locale, @PathVariable Long code) throws Exception {
		log.debug("==searchByDrugCode code="+code);

//		ActionForward forward = new ActionForward();
//		ActionMessages errors = new ActionMessages();
//		HttpSession session = request.getSession();
//
//		initializeUserLocale(request, session);
//
//		if (session.getAttribute("sessionActive") == null) {
//			// There is no session here... create one
//			session = request.getSession(true);
//			session.setAttribute("sessionActive", "Yes");
//
//			// Run startup action
//			initializeGocTemplate(request, session);
//			initializeDataTableServerProcessing(request);
//		}
//
//		session.removeAttribute(ApplicationGlobals.SELECTED_PRODUCT);
//		session.removeAttribute("product_label");
//		session.removeAttribute("approvedLabel");
//		session.removeAttribute("marketedLabel");
//
		try {
			SearchCriteriaBean criteria = new SearchCriteriaBean();
//			criteria.setDrugCode(new Long((String)request.getParameter("code")));
			criteria.setDrugCode(code);
//			SearchDrugDao search = new SearchDrugDao();
			List list = searchService.processSearchByAtcOrDin(request, criteria);
//
//			log.debug("Search by Drug Code: [" + criteria.getDrugCode()	+ "]");
//
			if (list.size() == 0) {
//				log.error("Null Response bean returned.");
//				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.failure.search"));
			} else if (list.size() != 1) {
//				log.error("More than one product bean returned..");
//				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.failure.search"));
			} else {
				log.debug("Total match found: [" + list.size() + "].");
				DrugBean bean = (DrugBean) list.get(0);
//				session.setAttribute(ApplicationGlobals.SELECTED_PRODUCT, ActionUtil.postProcessDrugBean(bean, request));
				model.addAttribute(ApplicationGlobals.SELECTED_PRODUCT, bean);
			}
		} catch (Exception e) {
//			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.failure.system"));
		}
//
//		// Report any errors we have discovered back to the original form
//		if (!errors.isEmpty()) {
//			saveMessages(request, errors);
//			forward = mapping.getInputForward();
//		} else {
//			forward = mapping.findForward("success");
//		}

		return Constants.PRODUCT_INFO_URL_MAPPING;
	}

	//TODO: from SearchCriteriaBean
	private String getSelectedStatusText(String[] status, Locale locale, List<LabelValueBean> statusList) throws Exception{
		String[] statusEnumeration = new String[status.length];
		int i = 0;
		for (String s : status) {
			for (LabelValueBean statusLabel : statusList) {
				if (s.equals(statusLabel.getValue())) {
					statusEnumeration[i] = statusLabel.getLabel();
					i++;
				}
			}
		}
		return enumerateThisStringArray(statusEnumeration, locale);
	}

	private String enumerateThisStringArray(String[] anAra, Locale locale) {
		String result = "";

		if (anAra != null && anAra.length > 0) {
			if (anAra.length == 1 && anAra[0].toString().equals("0")) {
				// no individual item was selected: just return the selectAll label
				result = mdService.getSelectAllLabel(locale.getLanguage());
			} else {
				for (int i = 0; i < anAra.length; i++) {
					if (!"0".equals(anAra[i])) { // exclude value 0: it means "Select All" and was included in the selection
						String anAraLabel = anAra[i].replace("/Hc", "/HC");
						result = result + anAraLabel + ", ";
					}
				}
				result = result.substring(0, result.length() - 2); // remove last comma
			}
		} else {
			result = null;
		}

		return result;
	}
}
