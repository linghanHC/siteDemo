/**
 * Name:  SearchDrugDao
 * Purpose:  Data Access Object to create, read, update object for table USER
 * Date: June 2006
 * Author: Diane Beauvais
 * 
 */
package ca.gc.hc.siteDemo.services;

import ca.gc.hc.siteDemo.bean.*;
import ca.gc.hc.siteDemo.dao.SearchDrugDao;
import ca.gc.hc.siteDemo.models.ActiveIngredients;
import ca.gc.hc.siteDemo.models.DrugProduct;
import ca.gc.hc.siteDemo.models.DrugStatus;
import ca.gc.hc.siteDemo.models.Schedule;
import ca.gc.hc.siteDemo.util.ActionUtil;
import ca.gc.hc.siteDemo.util.AppUtils;
import ca.gc.hc.siteDemo.util.ApplicationGlobals;
import ca.gc.hc.siteDemo.util.StringsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class SearchDrugService {
	private static Logger log = LoggerFactory.getLogger(SearchDrugService.class);

	@Autowired
	private SearchDrugDao dao;
	@Autowired
	private JsonBusinessService jsonBusinessService;
	@Autowired
	private LocalTestDataBusinessService localTestDataBusinessService;
	@Autowired
	private AppUtils appUtils;

	@Value("${useLocalTestData:false}")
	boolean useLocalTestData;

	@Value("${DT.server.processing.results.toggle}")        //todo set default value to 0 as struts app
	protected int serverSideThreshold;

	@PostConstruct
	private void postConstruct() {
		log.info("  useLocalTestData={}", useLocalTestData);
		log.info("  serverSideThreshold={}", serverSideThreshold);
	}

	public List processSearchByAtcOrDin(HttpServletRequest request, SearchCriteriaBean criteria) throws Exception {
		List results = new ArrayList();

		if (useLocalTestData) {
			String jsonString = localTestDataBusinessService.getOneDrugTestData();
			return jsonBusinessService.deserializeJsonStringToObjectList(jsonString, DrugBean[].class);
		} else {
			results = dao.SearchByCriteria(criteria, request);
//			log.debug("1==>"+jsonBusinessService.serializeObjectToJsonString(results));
		}
		// todo should these to be saved in session??
		request.getSession().setAttribute(ApplicationGlobals.RESULT_COUNT_KEY,
				results.size());
		request.getSession().setAttribute(
				ApplicationGlobals.USER_SEARCH_CRITERIA, criteria);
		request.getSession().setAttribute(
				ApplicationGlobals.QUERY_SEARCH_CRITERIA, criteria);
		request.getSession().setAttribute(ApplicationGlobals.LAST_SEARCH_CRITERIA, criteria);

		return results;
	}

	public List processSearchByNames(HttpServletRequest request, SearchCriteriaBean criteria, Locale locale) throws Exception {
		List resultsList = new ArrayList();
		if (useLocalTestData) {
			String jsonString = localTestDataBusinessService.getMultiDrugsTestData();
			resultsList = jsonBusinessService.deserializeJsonStringToObjectList(jsonString, DrugSummaryBean[].class);
//			log.debug("0==>"+resultsList.size());
			// todo cleanup session?
			request.getSession().setAttribute(
					ApplicationGlobals.RESULT_COUNT_KEY, resultsList.size());
		} else {
			/*
			 * Single quotes in SQL will raise an Oracle error. Double
			 * the quotes in criteria items that can include one, for
			 * searching. !! IMPORTANT !! ENSURE THAT ALL SUCH ITEMS ARE
			 * COVERED IN doubleEmbeddedQuotesIn(). Use a clone so that
			 * when we return to the jsp, the criteria displayed will be
			 * as entered and not as modified below.
			 */
//		request.getSession().setAttribute(
//				ApplicationGlobals.USER_SEARCH_CRITERIA, criteria);
//		request.getSession().setAttribute(
//				ApplicationGlobals.QUERY_SEARCH_CRITERIA, criteria);
//		request.getSession().setAttribute(ApplicationGlobals.LAST_SEARCH_CRITERIA, criteria);

			int resultsSize = dao.getQueryResultsCount(criteria);
//		request.getSession().setAttribute(
//				ApplicationGlobals.RESULT_COUNT_KEY, resultsSize);

			// Paginate if the result size is greater than the value set
			// for DataTable server processing
			if (resultsSize > 0
					&& resultsSize >= serverSideThreshold) {
//			ActionUtil.setupForServerProcessing(request, dao);
//			// Only get the first page of results
//			resultsList = dao.getNextResults(criteria, request);
//
			} else if (resultsSize > 0
					&& resultsSize < serverSideThreshold) {
				// re-query for actual results
				resultsList = dao.SearchByCriteria(criteria, request);
				log.debug("2==>" + jsonBusinessService.serializeObjectToJsonString(resultsList));
				request.getSession().setAttribute(
						ApplicationGlobals.RESULT_COUNT_KEY, resultsList.size());
			}
		}
//		List<Product> productList = Arrays.asList(new Product(23, "potatoes"),
//				new Product(14, "orange"), new Product(13, "lemon"),
//				new Product(23, "bread"), new Product(13, "sugar"));

//		List<User> collectorCollection =
//				productList.stream().map(p-> converterUser(p)).collect(Collectors.toList());

		boolean isFrench = appUtils.isLanguageFrench(locale);
		List<DrugSummary> newlist =
				(List<DrugSummary>) resultsList.stream().map(p -> converter(p, isFrench)).collect(Collectors.toList());

		//  DrugSummaryBean => DrugSummary

//		log.debug(newlist.toString());

		return newlist;
	}

//	private User converterUser (DrugSummaryBean old) {
//		User u = new User();
//
//		return u;
//	}

	private DrugSummary converter(Object old, boolean isFrench) {
		DrugSummary drugSummary = new DrugSummary();
		if (old instanceof DrugSummaryBean) {
			DrugSummaryBean bean = (DrugSummaryBean) old;
			drugSummary.setStatus(getStatus(bean.getStatus(), isFrench));
			drugSummary.setDrugCode(bean.getDrug().getDrugCode());
			drugSummary.setDin(getDin(bean.isScheduleCDinIssued(), bean.getDrug(), isFrench));
			drugSummary.setCompanyName(bean.getCompanyName());
			drugSummary.setBrandName(getBrandName(bean.getDrug(), isFrench));
			drugSummary.setBrandLangOfPart(getBrandNameLangOfPart(bean.getDrug(), isFrench));
			drugSummary.setDrugClass(getDrugClass(bean.getDrug(), isFrench));
			drugSummary.setClassLangOfPart(getClassLangOfPart(bean.getDrug(), isFrench));
			drugSummary.setSchedule(getSchedule(bean.getSchedule(), isFrench));
			drugSummary.setScheduleLangOfPart(getScheduleLangOfPart(bean.getSchedule(), isFrench));
			drugSummary.setNumberOfAis(getNumberOfAis(bean.getDrug()));
			drugSummary.setFirstAIName(getFirstAIName(bean.getfirstAI(), isFrench));
			drugSummary.setFirstAILangOfPart(getFirstAILangOfPart(bean.getfirstAI(), isFrench));
		}
		return drugSummary;
	}

	private String getStatus(DrugStatus status, boolean isFrench) {
		return isFrench ? StringsUtil.substituteIfNull(status.getExternalStatus()
				.getExternalStatusF(), status.getExternalStatus().getExternalStatusE())
				: status.getExternalStatus().getExternalStatusE();
	}

	private String getDin(boolean isScheduleCDinIssued, DrugProduct drugProduct, boolean isFrench) {
		// Implementation for June 13, 2018
		// 2020: Class check is no longer needed, because Biosimilar drug is not Radiopharmaceutical drug
		//if (classCode.equals(ApplicationGlobals.RADIOPHARMACEUTICAL_CLASS_CODE))

		if (!isScheduleCDinIssued) {	// todo use ScreenService to do this
			return isFrench ? ApplicationGlobals.NOT_APPLICABLE_F : ApplicationGlobals.NOT_APPLICABLE_E;
		}
		return drugProduct.getDrugIdentificationNumber();
	}

	private String getBrandName(DrugProduct drugProduct, boolean isFrench) {
		return isFrench ?
				StringsUtil.substituteIfNull(drugProduct.getBrandNameF(), drugProduct.getBrandNameE())
				: StringsUtil.substituteIfNull(drugProduct.getBrandNameE(), drugProduct.getBrandNameF());
	}

	private String getBrandNameLangOfPart(DrugProduct drugProduct, boolean isFrench) {
		return appUtils.getLanguageOfPart(drugProduct.getBrandNameE(), drugProduct.getBrandNameF(), isFrench);
	}

	private String getDrugClass(DrugProduct drugProduct, boolean isFrench) {
		return isFrench ?
				StringsUtil.substituteIfNull(drugProduct.getDrugClassF(), drugProduct.getDrugClassE()) :
				drugProduct.getDrugClassE();
	}

	private String getClassLangOfPart(DrugProduct drugProduct, boolean isFrench){
			return appUtils.getLanguageOfPart(drugProduct.getDrugClassE(), drugProduct.getDrugClassF(), isFrench);
		}

	private String getSchedule(Schedule schedule, boolean isFrench) {
		return isFrench ? StringsUtil.substituteIfNull(schedule.getScheduleF(), schedule.getScheduleE()) : schedule.getScheduleE();
	}

	private String getScheduleLangOfPart(Schedule schedule, boolean isFrench) {
		return appUtils.getLanguageOfPart(schedule.getScheduleE(), schedule.getScheduleF(), isFrench);
	}

	private Long getNumberOfAis(DrugProduct drugProduct){
		if (drugProduct.getNumberOfAis() == null) {
			return (long) 0;
		}
		return drugProduct.getNumberOfAis();
	}

	public String getFirstAIName(ActiveIngredients firstAI, boolean isFrench) {
		return isFrench ? StringsUtil.substituteIfNull(firstAI.getIngredientF(), firstAI.getIngredientE()) : firstAI.getIngredientE();
	}

	private String getFirstAILangOfPart(ActiveIngredients firstAI, boolean isFrench) {
		return appUtils.getLanguageOfPart(firstAI.getIngredientE(), firstAI.getIngredientF(), isFrench);
	}

		private List<LabelValueBean> getUniqueDrugClasses() throws Exception {
//		if (this.drugClassMap.isEmpty()) {
//			this.loadUniqueDrugClasses();
//		}
//		return (List)drugClassMap.get(getUserLanguage());
			return null;
	}

}
