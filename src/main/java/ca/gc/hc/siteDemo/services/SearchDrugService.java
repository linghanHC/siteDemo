/**
 * Name:  SearchDrugDao
 * Purpose:  Data Access Object to create, read, update object for table USER
 * Date: June 2006
 * Author: Diane Beauvais
 * 
 */
package ca.gc.hc.siteDemo.services;

import ca.gc.hc.siteDemo.bean.DrugBean;
import ca.gc.hc.siteDemo.bean.DrugSummaryBean;
import ca.gc.hc.siteDemo.bean.LabelValueBean;
import ca.gc.hc.siteDemo.bean.SearchCriteriaBean;
import ca.gc.hc.siteDemo.dao.SearchDrugDao;
import ca.gc.hc.siteDemo.util.ActionUtil;
import ca.gc.hc.siteDemo.util.ApplicationGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchDrugService {
	private static Logger log = LoggerFactory.getLogger(SearchDrugService.class);

	@Autowired
	private SearchDrugDao dao;
	@Autowired
	private JsonBusinessService jsonBusinessService;
	@Autowired
	private LocalTestDataBusinessService localTestDataBusinessService;

	@Value("${useLocalTestData:false}")
	boolean useLocalTestData;

	@Value("${DT.server.processing.results.toggle}")		//todo set default value to 0 as struts app
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
			log.debug("1==>"+jsonBusinessService.serializeObjectToJsonString(results));
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

	public List processSearchByNames(HttpServletRequest request, SearchCriteriaBean criteria) throws Exception {
		List resultsList = new ArrayList();

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
//		int serverSideThreshold = ApplicationGlobals.instance()
//				.getDataTableServerProcessingThreshold();
		log.debug("serverSideThreshold = " + serverSideThreshold);

		if (resultsSize > 0
				&& resultsSize >= serverSideThreshold) {
//			ActionUtil.setupForServerProcessing(request, dao);
//			// Only get the first page of results
//			resultsList = dao.getNextResults(criteria, request);
//
		} else if(resultsSize > 0
				&& resultsSize < serverSideThreshold) {
			// re-query for actual results
			if (useLocalTestData) {
				String jsonString = localTestDataBusinessService.getMultiDrugsTestData();
				resultsList= jsonBusinessService.deserializeJsonStringToDataCollection(jsonString, DrugSummaryBean.class).getData();
			} else {
				resultsList = dao.SearchByCriteria(criteria, request);
			}
//			log.debug("2==>"+jsonBusinessService.serializeObjectToJsonString(resultsList));
			request.getSession().setAttribute(
					ApplicationGlobals.RESULT_COUNT_KEY, resultsList.size());
		}
		return resultsList;
	}


		private List<LabelValueBean> getUniqueDrugClasses() throws Exception {
//		if (this.drugClassMap.isEmpty()) {
//			this.loadUniqueDrugClasses();
//		}
//		return (List)drugClassMap.get(getUserLanguage());
			return null;
	}
}
