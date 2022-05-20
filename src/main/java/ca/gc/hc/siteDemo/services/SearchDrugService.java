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
import ca.gc.hc.siteDemo.forms.SearchForm;
import ca.gc.hc.siteDemo.models.ActiveIngredients;
import ca.gc.hc.siteDemo.models.DrugProduct;
import ca.gc.hc.siteDemo.models.DrugStatus;
import ca.gc.hc.siteDemo.models.Schedule;
import ca.gc.hc.siteDemo.util.AppUtils;
import ca.gc.hc.siteDemo.util.ApplicationGlobals;
import ca.gc.hc.siteDemo.util.StringsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class SearchDrugService {
	private static final Logger log = LoggerFactory.getLogger(SearchDrugService.class);

	@Autowired
	private SearchDrugDao dao;
	@Autowired
	private JsonBusinessService jsonBusinessService;
	@Autowired
	private ScreenDetailBusinessService screenDetailBusinessService;
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
			}
		}
//		List<Product> productList = Arrays.asList(new Product(23, "potatoes"),
//				new Product(14, "orange"), new Product(13, "lemon"),
//				new Product(23, "bread"), new Product(13, "sugar"));

//		List<User> collectorCollection =
//				productList.stream().map(p-> converterUser(p)).collect(Collectors.toList());

		boolean isFrench = appUtils.isLanguageFrench(locale);
		List<DrugSummary> newlist =
				(List<DrugSummary>) resultsList.stream().map(p -> converter(p, isFrench, locale)).collect(Collectors.toList());

		//  DrugSummaryBean => DrugSummary

//		log.debug(newlist.toString());

		return newlist;
	}

	private DrugSummary converter(Object old, boolean isFrench, Locale locale) {
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
			drugSummary.setPm(getPm(bean, isFrench, locale));
			drugSummary.setSchedule(getSchedule(bean.getSchedule(), isFrench));
			drugSummary.setScheduleLangOfPart(getScheduleLangOfPart(bean.getSchedule(), isFrench));
			drugSummary.setNumberOfAis(getNumberOfAis(bean.getDrug()));
			drugSummary.setFirstAIName(getFirstAIName(bean.getfirstAI(), isFrench));
			drugSummary.setFirstAILangOfPart(getFirstAILangOfPart(bean.getfirstAI(), isFrench));
			drugSummary.setAiStrengthAndDosageLangOfPart(getAiStrengthAndDosageLangOfPart(bean.getfirstAI(), isFrench));
			drugSummary.setAiStrengthAndDosageText(getAiStrengthAndDosageText(bean.getfirstAI(), isFrench));
			drugSummary.setDosageValue(bean.getfirstAI().getDosageValue());
			drugSummary.setAiDosageLangOfPart(getAiDosageLangOfPart(bean.getfirstAI(), isFrench));
			drugSummary.setDosageUnit(getDosageUnit(bean.getfirstAI(), isFrench));
		}
		return drugSummary;
	}


	/***************************************************************************
	 * Gets the name of the product monograph in the language appropriate for the Locale.
	 * @return the locale-specific name of the product monograph.
	 */
	public String getPm(DrugSummaryBean bean, boolean isFrench, Locale locale) {
		String pm = isFrench ? bean.getPmF() : bean.getPmE();
		return StringsUtil.hasData(pm) ?
				screenDetailBusinessService.getResourceMessage("label.results.yes", locale) :
				screenDetailBusinessService.getResourceMessage("label.results.no", locale);
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

	/**
	 //	 * @param index An int corresponding to an index in the current List of ActiveIngredients
	 * @return Either an empty string if the related bilingual properties actually exist in both official
	 * languages, or the ISO language code to use in a lang attribute where the property in the requested
	 * language is missing, and its equivalent in the other official language is being returned instead. <br/>
	 * <strong>Note:</strong> Normally, both languages are expected to be present. If either is missing,
	 * the caller is responsible for getting the individual lang attribute(s) if required using
	 * getAiStrengthLangOfPart and getAiDosageLangOfPart.
	//	 * @see getAiStrengthLangOfPart
	//	 * @see getAiDosageLangOfPart
	 * @author Sylvain LariviÃ¨re  2012-09-19
	 */
	private String getAiStrengthAndDosageLangOfPart(ActiveIngredients firstAI, boolean isFrench) {
		String strengthUnitLangOfPart = appUtils.getLanguageOfPart(firstAI.getStrengthUnitE(), firstAI.getStrengthUnitF(), isFrench);
		String dosageUnitLangOfPart = appUtils.getLanguageOfPart(firstAI.getDosageUnitE(), firstAI.getDosageUnitF(), isFrench);

		return strengthUnitLangOfPart + dosageUnitLangOfPart;
	}

	/**
	 * Sylvain Larivière  2009-12-07
	 * @return active ingredient strength in the form &lt;strength&gt; &lt;unit&gt;
	 *  for instance " 100 MG", or ".2 %".
	 *  Dosage (eg "per tablet" or "per ml") is delegated to getDosageText()
	//	 *  @see getDosageText().
	 */
	private String getAiStrengthAndDosageText(ActiveIngredients firstAI, boolean isFrench){
		String result = "";

		result = getAiStrengthText(firstAI, isFrench);
		if (!isDosageUnitAPercentage(firstAI, isFrench)) {
			result += getAiDosageText(firstAI, isFrench);
		}
		return  result;
	}

	private String getAiStrengthText(ActiveIngredients firstAI, boolean isFrench){
		return firstAI.getStrength() + " " + getStrengthUnit(firstAI, isFrench);
	}

	private String getStrengthUnit(ActiveIngredients firstAI, boolean isFrench){
		return isFrench ? StringsUtil.substituteIfNull(firstAI.getStrengthUnitF(), firstAI.getStrengthUnitE()) : firstAI.getStrengthUnitE();
	}
	private boolean isDosageUnitAPercentage(ActiveIngredients firstAI, boolean isFrench){
		return StringsUtil.hasData(getDosageUnit(firstAI, isFrench)) && firstAI.equals("%");
	}

	private String getDosageUnit(ActiveIngredients firstAI, boolean isFrench) {
		return isFrench ? StringsUtil.substituteIfNull(firstAI.getDosageUnitF(), firstAI.getDosageUnitE()) : firstAI.getDosageUnitE();
	}

	private String getAiStrengthLangOfPart(ActiveIngredients firstAI, boolean isFrench) {
		return appUtils.getLanguageOfPart(firstAI.getStrengthUnitE(), firstAI.getStrengthUnitF(), isFrench);
	}

	private String getAiDosageLangOfPart(ActiveIngredients firstAI, boolean isFrench) {
		return appUtils.getLanguageOfPart(firstAI.getDosageUnitE(), firstAI.getDosageUnitF(), isFrench);
	}

	/**
	 * Sylvain Larivière 2009-12-07
	 * @return Dosage in the form " / &lt;value&gt; &lt;unit&gt; "
	 *  for instance " / 5 ML" (per 5 ml) or " / ML" (per ml)
	//	 * @see getAIStrengthAndDosageText()
	 */
	private String getAiDosageText(ActiveIngredients firstAI, boolean isFrench) {
		String result = "";

		if (StringsUtil.hasData(firstAI.getDosageValue())) {
			if (StringsUtil.hasData(getDosageUnit(firstAI, isFrench))) {
				result = " / " + firstAI.getDosageValue() + " " + getDosageUnit(firstAI, isFrench);
			}
		}else {
			if (!isDosageUnitAPercentage(firstAI, isFrench))
				if (StringsUtil.hasData(getDosageUnit(firstAI, isFrench))){
					result = " / " + getDosageUnit(firstAI, isFrench);
				}
		}
		return result;
	}

		private List<LabelValueBean> getUniqueDrugClasses() throws Exception {
//		if (this.drugClassMap.isEmpty()) {
//			this.loadUniqueDrugClasses();
//		}
//		return (List)drugClassMap.get(getUserLanguage());
			return null;
	}

	public void copyFormValuesToSearchCriteria(SearchForm searchForm, SearchCriteriaBean criteria, Locale locale) {
		BeanUtils.copyProperties(searchForm, criteria);
		String tmpBiosimDrugSearch = criteria.getBiosimDrugSearch();
		log.debug(tmpBiosimDrugSearch);
		if (tmpBiosimDrugSearch != null && tmpBiosimDrugSearch.length() > 0) {
			// Only when Biosimilar biologic drug is selected, String value will be
			// displayed at the search result page
			criteria.setBiosimDrugSearch(screenDetailBusinessService.getResourceMessage("value.yes", locale));
		}
	}
}
