/**
 * Name:  SearchDrugDao
 * Purpose:  Data Access Object to create, read, update object for table USER
 * Date: June 2006
 * Author: Diane Beauvais
 * 
 */
package ca.gc.hc.siteDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ca.gc.hc.siteDemo.bean.SearchCriteriaBean;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import ca.gc.hc.siteDemo.bean.AjaxBean;
import ca.gc.hc.siteDemo.bean.AjaxBean.AjaxRequestStatus;
import ca.gc.hc.siteDemo.bean.BasicDrugSummaryBean;
import ca.gc.hc.siteDemo.bean.DrugBean;
import ca.gc.hc.siteDemo.bean.DrugSummaryBean;
import ca.gc.hc.siteDemo.bean.SearchCriteriaBean;
import ca.gc.hc.siteDemo.bean.SearchCriteriaIndexBean;
import ca.gc.hc.siteDemo.models.AHFS;
import ca.gc.hc.siteDemo.models.ATC;
import ca.gc.hc.siteDemo.models.ActiveIngredients;
import ca.gc.hc.siteDemo.models.Company;
import ca.gc.hc.siteDemo.models.DrugClass;
import ca.gc.hc.siteDemo.models.DrugProduct;
import ca.gc.hc.siteDemo.models.DrugStatus;
import ca.gc.hc.siteDemo.models.DrugVeterinarySpecies;
import ca.gc.hc.siteDemo.models.ExternalStatus;
import ca.gc.hc.siteDemo.models.Form;
import ca.gc.hc.siteDemo.models.Packaging;
import ca.gc.hc.siteDemo.models.PharmMonitorAct;
import ca.gc.hc.siteDemo.models.ProductMonograph;
import ca.gc.hc.siteDemo.models.RiskMinMeasure;
import ca.gc.hc.siteDemo.models.Route;
import ca.gc.hc.siteDemo.models.Schedule;
import ca.gc.hc.siteDemo.models.SpecialIdentifier;
import ca.gc.hc.siteDemo.models.Veterinary;
import ca.gc.hc.siteDemo.util.ApplicationGlobals;
import ca.gc.hc.siteDemo.util.DataTableColumn;
import ca.gc.hc.siteDemo.util.StringsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Repository;

@Repository
public class SearchDrugDao  {

	private final static String DUPLICATE_ROW_EXCEPTION_MSG = "than one row with the given identifier";
	private final static int ROUTE_CRITERIA = 1;
	private final static int DOSAGE_FORM_CRITERIA = 2;
	private final static int SCHEDULE_CRITERIA = 3;
	private final static int DRUG_CLASS_CRITERIA = 4;
	private final static int FOURTH_LEVEL_ATC_LENGTH = 5;
	private final static int FIFTH_LEVEL_ATC_LENGTH = 7;
	private final static int SPECIES_CRITERIA = 6;
		
	// used for localized searches
	private final static String INGREDIENT_COLUMN = "ingredient";	
	private final static String ROUTE_COLUMN = "route_of_administration";
	private final static String FORM_COLUMN = "pharmaceutical_form";
	private final static String SCHEDULE_COLUMN = "schedule";
	private final static String SPECIES_COLUMN = "vet_species";
		
	private final static String DRUG_CLASS_COLUMN = "class_code";

	
	private boolean isAjaxRequest = false;
	private AjaxBean ajaxBean = null;
	private List<String> listOfIngredents = new ArrayList<>(); 

	/**
	 * The local instance of the LOG4J Logger.
	 */

	private static Logger log = LoggerFactory
			.getLogger(SearchDrugDao.class);

	@PersistenceContext
	protected EntityManager entityManager;

	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	public List<?> SearchByCriteria(SearchCriteriaBean criteria,
			HttpServletRequest request) throws Exception {

		if (criteria.getDrugCode() != null
				&& criteria.getDrugCode().longValue() > 0) {
			return searchDrugByDrugCode(criteria.getDrugCode(), request);
		} else if (isSearchingByDin(criteria)) {
			return searchDrugByDIN(criteria, request);
		} else if (criteria.getCompanyCode() != null
				&& criteria.getCompanyCode().longValue() > 0) {
			return searchDrugByCompanyCode(criteria, request);
		} else if (isSearchingByATC(criteria)) {
			return searchDrugByATC(criteria, request);
		} else {
			return searchDrugByNames(criteria, request);
		}

	}

	private Connection getConnection() {
//		try {
//			return getSession().connection();
//		} catch (Exception ex) {
//			return null;
//		}
		// TODO is it correct??
		try {
			EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) entityManager.getEntityManagerFactory();
			Connection connection = info.getDataSource().getConnection();
			return connection;
		} catch (SQLException sqlException) {
			return null;
		}
	}
		
	private List<DrugBean> searchDrugByDrugCode(Long drugCode, HttpServletRequest request)
			throws Exception {
		List<DrugBean> drugs = new ArrayList<DrugBean>();
		Connection con = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select drug.* from WQRY_DRUG_PRODUCT drug"
					+ " WHERE drug.DRUG_CODE = ?";
			
			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();			
			while ( rs.next() )
		    {
				DrugBean drugInfo = new DrugBean();
				DrugProduct drug = new DrugProduct();
				Long rsDrugCode = rs.getLong("DRUG_CODE");
				Long drugClassCode = rs.getLong("CLASS_CODE");
				drug.setDrugCode(rsDrugCode);
				drug.setBrandNameE(rs.getString("BRAND_NAME"));
				drug.setBrandNameF(rs.getString("BRAND_NAME_F"));
				drug.setDrugIdentificationNumber(rs.getString("DRUG_IDENTIFICATION_NUMBER"));
				drug.setCompanyCode(rs.getLong("COMPANY_CODE"));
				drug.setDrugClassE(rs.getString("CLASS"));
				drug.setDrugClassF(rs.getString("CLASS_F"));
				drug.setClassCode(drugClassCode);
				drug.setDescriptorE(rs.getString("DESCRIPTOR"));
				drug.setDescriptorF(rs.getString("DESCRIPTOR_F"));
				drug.setNumberOfAis(rs.getLong("NUMBER_OF_AIS"));
				drug.setAiGroupNo(rs.getString("AI_GROUP_NO"));
				drug.setRiskManPlan(rs.getString("RISK_MAN_PLAN"));
				drug.setOpioidManPlan(rs.getString("OPIOID_MAN_PLAN"));
				drug.setBiosimDrug(drugInfo.CheckIfIsBiosimDrug(retrieveSIs(drug.getDrugCode())));
				
				drugInfo.setDrugProduct(drug);
				drugInfo.setActiveIngredientList(retrieveAIS(con, rsDrugCode));
				drugInfo.setPharmMonitorActs(retrievePharmMonitorActs(con, rsDrugCode));
				drugInfo.setRiskMinMeasures(retrieveRMMs(con, rsDrugCode));
				drugInfo.setSpecialIdentifiers(retrieveSIs(rsDrugCode));
				//drugInfo.setAhfsList(retrieveAHFS(con, rsDrugCode));
				// Special identifiers are needed to determine if DIN is to be displayed
				drug.setScheduleCDinIssued(drugInfo.isScheduleCDinIssued(drugInfo.getSpecialIdentifiers()));
				
				try {
					ATC current = retrieveAtcVO(con, drug.getDrugCode());

					/*
					 * SL/2012-03-23 ADR3291 and ADR1183: if the ATC is at
					 * the 5th level, display the fourth level ATC number
					 * and description instead.
					 */
					if (current != null) {
						if (current.getAtcNumber() != null && current.getAtcNumber().length() == FIFTH_LEVEL_ATC_LENGTH) {
							try {
								String fourthLevelATCNumber = current
										.getAtcNumber().substring(0,
												FOURTH_LEVEL_ATC_LENGTH);
								ATC fourthLevelATC = retrieveAtcFourthLevelATC(con, fourthLevelATCNumber);
								if (fourthLevelATC != null
										&& StringsUtil.hasData(fourthLevelATC
												.getAtcE())) {
									current.setAtcE(fourthLevelATC.getAtcE());
									current.setAtcF(fourthLevelATC.getAtcF());
									current.setAtcNumber(fourthLevelATC
											.getAtcNumber());
								}
							} catch (Exception e) {
								/*
								 * If a 4th-level ATC cannot be obtained, the
								 * 5th-level ATC (current) will be displayed
								 * instead: no substitution
								 */
							}
						}					
						drugInfo.setAtcVO(current);
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
					log.error("Stack Trace: ", e);
					StringBuffer message = new StringBuffer(
							"Search Drug Product By drugCode [");
					message.append(drugCode);
					message.append("] failed");				
					throw new Exception(message.toString());
				}
				drugInfo.setFormList(retrieveForms(con, rsDrugCode));
				drugInfo.setPackagingList(retrievePackagings(con, rsDrugCode));
				drugInfo.setRouteList(retrieveRoutes(con, rsDrugCode));
				drugInfo.setStatusVO(retrieveStatusVO(con, rsDrugCode));
				drugInfo.setScheduleList(retrieveSchedules(con, rsDrugCode));
				drugInfo.setCompanyVO(retrieveCompanyVO(con, drug.getCompanyCode()));
				drugInfo.setPmVO(retrievePM(con, rsDrugCode));		
				if ( drugClassCode ==  ApplicationGlobals.VETERINARY_CLASS_CODE )
				   drugInfo.setVetSpecies(retrieveVetSpecies(con, rsDrugCode));

				drugs.add(drugInfo);
		    }
			
			log.debug("Search By drugCode Query is: " + query);			
			
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By drugCode [");
				message.append(drugCode);
				message.append("] failed");				
				throw new Exception(message.toString());
			}			
		} finally {
			try { 
				  if (rs != null)  rs.close(); 
			} catch (Exception e) {  
				  log.error("Cannot close searchDrugByDrugCode resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close searchDrugByDrugCode prepared statement.", e); 
			};
			con.close();
		}
		return drugs;
	}

	private BasicDrugSummaryBean populateBasicDrugSummaryBean(ResultSet rs) throws Exception {
		BasicDrugSummaryBean basicDrugSummary = new BasicDrugSummaryBean();
		
		basicDrugSummary.setDrugCode(rs.getLong("drug_code"));
		basicDrugSummary.setBrandName(rs.getString("brand_name"));
		basicDrugSummary.setBrandNameF(rs.getString("brand_name_f"));
		basicDrugSummary.setDurgIdentificationNumber(rs.getString("drug_identification_number"));
		basicDrugSummary.setCompanyCode(rs.getLong("company_code"));
		basicDrugSummary.setCompanyName(rs.getString("COMPANY_NAME"));
		basicDrugSummary.setDrugClass(rs.getString("CLASS"));
		basicDrugSummary.setDrugClassF(rs.getString("CLASS_F"));
		basicDrugSummary.setNumberOfAis(rs.getLong("NUMBER_OF_AIS"));
		basicDrugSummary.setAiGroupNo(rs.getString("AI_GROUP_NO"));
		basicDrugSummary.setExternalStatusE(rs.getString("EXTERNAL_STATUS_ENGLISH"));
		basicDrugSummary.setExternalStatusF(rs.getString("EXTERNAL_STATUS_FRENCH"));
		basicDrugSummary.setExternalStatusCode(rs.getLong("EXTERNAL_STATUS_CODE"));
		basicDrugSummary.setScheduleE(rs.getString("schedule"));
		basicDrugSummary.setScheduleF(rs.getString("schedule_f"));
		basicDrugSummary.setClassCode(rs.getLong("class_code"));
		basicDrugSummary.setIngredientE(rs.getString("ingredient"));
		basicDrugSummary.setIngredientF(rs.getString("ingredient_f"));
		basicDrugSummary.setStrength(rs.getString("strength"));
		basicDrugSummary.setStrengthUnitE(rs.getString("strength_unit"));
		basicDrugSummary.setStrengthUnitF(rs.getString("strength_unit_f"));
		if (StringsUtil.hasData(rs.getString("DOSAGE_VALUE"))) 
			basicDrugSummary.setDosageValue(rs.getString("DOSAGE_VALUE"));
		if (StringsUtil.hasData(rs.getString("DOSAGE_UNIT"))) 
			basicDrugSummary.setDosageUnitE(rs.getString("DOSAGE_UNIT"));
		if (StringsUtil.hasData(rs.getString("DOSAGE_UNIT_F"))) 
			basicDrugSummary.setDosageUnitF(rs.getString("DOSAGE_UNIT_F"));	
		if (StringsUtil.hasData(rs.getString("PM_ENGLISH_FNAME"))) 
		    basicDrugSummary.setPmEnglishFName(rs.getString("PM_ENGLISH_FNAME"));
		if (StringsUtil.hasData(rs.getString("PM_FRENCH_FNAME"))) 
		    basicDrugSummary.setPmFrenchFName(rs.getString("PM_FRENCH_FNAME"));	
	       
		
		return basicDrugSummary;
	}
	
	private List<BasicDrugSummaryBean> populateBasicDrugSummaries(ResultSet rs, AjaxBean ajaxBean) throws Exception {
		List<BasicDrugSummaryBean> basicDrugSummaries = new ArrayList<BasicDrugSummaryBean>();
		try {
			int rowIndex = 0;
			int rowRetrived = 0;
			while (rs.next()) {
				if (ajaxBean != null) {
					if (rowIndex >= ajaxBean.getDisplayStart() && rowRetrived < ajaxBean.getDisplayLength()) {					
						basicDrugSummaries.add(this.populateBasicDrugSummaryBean(rs));		
						rowRetrived++;
					}
					
					if (rowRetrived == ajaxBean.getDisplayLength()) {
						return basicDrugSummaries;
					}
					rowIndex++;					
				} else {
					basicDrugSummaries.add(this.populateBasicDrugSummaryBean(rs));
				}
			}
		} catch (SQLException e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("Populate Basic Drug Summaries");
			throw new Exception(message.toString());
		}

		return basicDrugSummaries;
	}
	
	/***************************************************************************
	 * Gets the a list of Drugs by a DIN number Returns an empty List if none
	 * are found that match.
	 * 
	 * @param criteria
	 *            The current SearchCriteriaBean instance, to adapt the sorting
	 *            clause where DataTable server processing is required
	 * @return the List of Drugs by a DIN number Updated and refactored
	 *         SL/2009-10-02 for additional fields returned
	 */
	private List<DrugBean> searchDrugByDIN(SearchCriteriaBean criteria,
			HttpServletRequest request) throws Exception {
		List<DrugBean> productInfo = new ArrayList<DrugBean>();
		Connection con = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			StringBuffer query = new StringBuffer(
					this.basicDrugSummarySelect());
			query.append(this.basicDrugSummaryFromClause());
			query.append(this.basicDrugSummaryWhereClause());
			query.append(extractDinCriterion());
			query.append(localizedSummaryOrderByClause());

			stmt = con.prepareStatement(query.toString());
			stmt.setString(1, criteria.getDin());

			List<BasicDrugSummaryBean> basicDrugSummaries = this.populateBasicDrugSummaries(rs=stmt.executeQuery(), ajaxBean);
			
			log.debug("Search By DIN Query is: " + query);

			productInfo = this.populateBeans(basicDrugSummaries, request);

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By DIN [");
				message.append(criteria.getDin());
				message.append("] failed");
				throw new Exception(message.toString());
			}
		} finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close searchDrugByDIN resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close searchDrugByDIN prepared statement.", e); 
			};	 
			con.close();
		}
		return productInfo;
	}

	private String extractDinCriterion() {
		return " and drug.DRUG_IDENTIFICATION_NUMBER = ?";
	}

	/**
	 * @param drugInfos
	 *            Search results as a List of objects arrays
	 * @param request
	 *            The current request
	 * @return An ArrayList of either DrugBeans or DrugSummaryBeans, sorted on
	 *         brand names. If there is only one result, it will be searched
	 *         again but this time by drug code. This will return a
	 *         <strong>DrugBean</strong> suitable for the Product Information
	 *         page, whereas for multiple results, we need to return a list of
	 *         <strong>DrugSummaryBeans</strong>, suitable for the search
	 *         results page.
	 * @author SL/2012-07-19
	 * @return 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> T populateBeans(List<BasicDrugSummaryBean> drugInfos, HttpServletRequest request) {		
		if (drugInfos.size() == 1) {
			BasicDrugSummaryBean drug = drugInfos.get(0);
			Long drugCode = drug.getDrugCode();
			try {
				return (T) searchDrugByDrugCode(drugCode, request);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (drugInfos.size() > 1) {
			return (T) this.populateDrugProductSummaries(drugInfos, request);
		}
		
		return (T) new ArrayList();
	}

	/***************************************************************************
	 * Gets the a list of Drugs by a Company Code Returns an empty List if none
	 * are found that match.
	 * 
	 * @param criteria  The current SearchCriteriaBean instance, to adapt the sorting
	 * clause where DataTable server processing is required
	 * @return the List of Drugs by a Company Code Updated SL/2009-10-14 for
	 *         additional criteria
	 */
	private List<DrugSummaryBean> searchDrugByCompanyCode(SearchCriteriaBean criteria,
			HttpServletRequest request) throws Exception {
		List<DrugSummaryBean> val = new ArrayList<DrugSummaryBean>();
		Connection con = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = buildSearchByCompanyCodeSql(criteria);

			// Save the query for possible DataTable server processing
			if (request.getSession()!= null) {
				request.getSession().setAttribute(ApplicationGlobals.SQL_QUERY,query);
            
			   stmt = con.prepareStatement(query.toString());
			   List<SearchCriteriaIndexBean> criteriaIndex = this.getCriteriaIndex(criteria);
			   for (SearchCriteriaIndexBean index : criteriaIndex) {
				   if (index.getSelected()) {
				    	if (index.getCriteriaObjectClass() == String.class) {
				    		stmt.setString(index.getIndex(), (String)index.getCriteria());
					    } else if (index.getCriteriaObjectClass() == Long.class) {
						    stmt.setLong(index.getIndex(), (Long)index.getCriteria());
				        }
			       }	   
			   } // end of for loop

			   List<BasicDrugSummaryBean> basicDrugSummaries = this.populateBasicDrugSummaries(rs=stmt.executeQuery(), ajaxBean);

			   log.debug("Search By Company Code Query is: " + query);
			
			   val = this.populateBeans(basicDrugSummaries, request);
			}  
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By Company [");
				message.append(criteria.getCompanyCode().toString());
				message.append("] Status [");
				message.append(criteria.getStatusCode() != null ? criteria
						.getStatusCode().toString() : null);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		} finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close searchDrugByCompanyCode resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close searchDrugByCompanyCode prepared statement.", e); 
			};
			con.close();
		}
		
		return val; 
	}

	private String buildSearchByCompanyCodeSql(SearchCriteriaBean criteria) {
		log.debug("isAjaxRequest = " + this.isAjaxRequest);
		StringBuilder queryString = new StringBuilder(
				this.basicDrugSummarySelect());
		queryString.append(this.basicDrugSummaryFromClause());
		queryString.append(this.basicDrugSummaryWhereClause());
		queryString.append(" and drug.COMPANY_CODE = ?");

		if (criteria.getStatusCode() != null && !"0".equals(criteria.getStatusCode()[0])) { 
			queryString.append(" and st.status_code = ?");
		}
		queryString.append(localizedSummaryOrderByClause());
		
		return queryString.toString();
	}

	/***************************************************************************
	 * Gets the a list of Drugs by ATC Returns an empty List if none are found
	 * that match.
	 * 
	 * @param criteria
	 *            The current SearchCriteriaBean instance, to adapt the sorting
	 *            clause where DataTable server processing is required
	 * @return the List of Drugs by a ATC
	 */
	private List<BasicDrugSummaryBean> searchDrugByATC(SearchCriteriaBean criteria,
			HttpServletRequest request) throws Exception {
		List<BasicDrugSummaryBean> beans = new ArrayList<BasicDrugSummaryBean>();
		Connection con = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			StringBuffer query = new StringBuffer(
					this.basicDrugSummarySelect());
			query.append(this.basicDrugSummaryFromClause());
			query.append(this.basicDrugSummaryWhereClause());
			query.append(extractAtcCriterion());
			query.append(localizedSummaryOrderByClause());

			// Save the query for possible DataTable server processing
			if (request.getSession() != null) {
			   request.getSession().setAttribute(ApplicationGlobals.SQL_QUERY, query);

			   stmt = con.prepareStatement(query.toString());
			   stmt.setString(1, criteria.getAtc() + "%");
			   List<BasicDrugSummaryBean> basicDrugSummaries = this.populateBasicDrugSummaries(rs=stmt.executeQuery(), ajaxBean);			

			   log.debug("Search by ATC Query is: " + query);

			   beans = this.populateBeans(basicDrugSummaries, request);
			}
   
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By ATC [");
				message.append(criteria);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		} finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close searchDrugByATC resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
		    } catch (Exception e) { 
				  log.error("Cannot close searchDrugByATC prepared statement.", e); 
			};	
			con.close();
		}
		return beans;
	}

	private StringBuffer extractAtcCriterion() {
		StringBuffer result = new StringBuffer(" and drug.DRUG_CODE in");
		result.append(" (select distinct d.DRUG_CODE from WQRY_DRUG_PRODUCT d, WQRY_ATC atc");
		result.append(" where d.DRUG_CODE = atc.DRUG_CODE");
		result.append(" and atc.TC_ATC_NUMBER LIKE ?");
		result.append(")");

		return result;
	}

	/***************************************************************************
	 * Gets the a list of Drugs by a Company name Returns an empty List if none
	 * are found that match.
	 * 
	 * @param criteria
	 * @return the List of Drugs by a company name
	 */
	private List<DrugSummaryBean> searchDrugByNames(SearchCriteriaBean criteria,
			HttpServletRequest request) throws Exception {
		List<DrugSummaryBean> values = new ArrayList<DrugSummaryBean>();
		Connection con = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = buildSearchByNamesSql(criteria);
			log.debug("searchDrugByNames query: \n" + query);
			//System.out.println ("searchDrugByNames query: \n" + query);
			// Save the query for possible DataTable server processing
			if (request.getSession() != null) {
				request.getSession().setAttribute(ApplicationGlobals.SQL_QUERY, query);
			stmt = con.prepareStatement(query);
			List<SearchCriteriaIndexBean> criteriaIndex = this.getCriteriaIndex(criteria);
			for (SearchCriteriaIndexBean index : criteriaIndex) {
				if (index.getSelected()) {
					log.debug("searchDrugByName index = " + index.getIndex() + "Criteria = " + index.getCriteria()); 				
					if (index.getCriteriaObjectClass() == String.class) {				
						stmt.setString(index.getIndex(), (String)index.getCriteria());
					} else if (index.getCriteriaObjectClass() == Long.class) {
						stmt.setLong(index.getIndex(), (Long)index.getCriteria());
					}
				}
			   }	   
			}

			List<BasicDrugSummaryBean> basicDrugSummaries = this.populateBasicDrugSummaries(rs=stmt.executeQuery(), ajaxBean);
			
			values =  this.populateBeans(basicDrugSummaries, request);
		    
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By ATC [");
				message.append(criteria.toString());
				message.append("] failed");
				throw new Exception(message.toString());
			}
		} finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close searchDrugByNames resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close searchDrugByNames prepared statement.", e); 
			};
			con.close();
		}
		return values;
	}

	/***************************************************************************
	 * Gets the a list of AIS Returns an empty List if none are found that
	 * match.
	 * 
	 * @param drugCode
	 * @return the List of AIS
	 */
	private List<ActiveIngredients> retrieveAIS(Connection con, Long drugCode) throws Exception {
		List<ActiveIngredients> activeIngredients = new ArrayList<ActiveIngredients>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			String query = "select ais.* from WQRY_ACTIVE_INGREDIENTS ais ";
			query = query + "where ais.DRUG_CODE = ?";
			query = query + " ORDER BY "
					+ localizedSearchColumnFor(INGREDIENT_COLUMN)
					+ ", STRENGTH, STRENGTH_UNIT";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				ActiveIngredients activeIngredient = new ActiveIngredients();
				activeIngredient.setDrugCode(rs.getLong("DRUG_CODE"));
				activeIngredient.setIngredientE(rs.getString("INGREDIENT"));
				activeIngredient.setIngredientF(rs.getString("INGREDIENT_F"));
				activeIngredient.setStrength(rs.getString("STRENGTH"));
				activeIngredient.setStrengthUnitE(rs.getString("STRENGTH_UNIT"));
				activeIngredient.setStrengthUnitF(rs.getString("STRENGTH_UNIT_F"));
				activeIngredient.setDosageValue(rs.getString("DOSAGE_VALUE"));
				activeIngredient.setDosageUnitE(rs.getString("DOSAGE_UNIT"));
				activeIngredient.setDosageUnitF(rs.getString("DOSAGE_UNIT_F"));
				
				activeIngredients.add(activeIngredient);
			}
		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer("Search By drugCode [");
			message.append(drugCode);
			message.append("] failed");
			throw new Exception(message.toString());
		}
		finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveAIS resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) {
				  log.error("Cannot close retrieveAIS prepared statement.", e); 
			};		
		} 
		return activeIngredients;
	}

	/***************************************************************************
	 * Gets the veterinary species from a drug code Returns null if none are
	 * found that match.
	 * 
	 * @param drugCode
	 * @return a String of the vet species
	 */
	private List<Veterinary> retrieveVetSpecies(Connection con, Long drugCode) throws Exception {
		List<Veterinary> species = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT veterinary.* FROM WQRY_DRUG_VETERINARY_SPECIES veterinary WHERE veterinary.drug_code = ?";

		    stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			while (rs.next()) {
				Veterinary specie = new Veterinary();
				specie.setDrugCode(rs.getLong("DRUG_CODE"));
				specie.setVetSpeciesE(rs.getString("VET_SPECIES"));
				specie.setVetSpeciesF(rs.getString("VET_SPECIES_F"));
				
				species.add(specie);
			}
		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer(
					"Retrieve Vet Species By drugCode [");
			message.append(drugCode);
			message.append("] failed");
			throw new Exception(message.toString());
		}
		finally {
			try { 
				  if (rs != null) rs.close(); 
		    } catch (Exception e) { 
		    	  log.error("Cannot close retrieveVetSpecies resultset.", e); 
		    };
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveVetSpecies prepared statement.", e); 
			};		
		}
		return species;
	}

	/***************************************************************************
	 * Gets the a list of AHFS by drug code Returns an empty List if none are
	 * found that match.
	 * 
	 * @param drugCode
	 * @return the List of AHFS by drug code
	 */
	private List<AHFS> retrieveAHFS(Connection con, Long drugCode) throws Exception {
		List<AHFS> ahfsInfos = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select ahfs.* from WQRY_AHFS ahfs "
					+ "where ahfs.DRUG_CODE = ?";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			while (rs.next()) {				
				AHFS ahfs = new AHFS();
				ahfs.setDrugCode(rs.getLong("DRUG_CODE"));
				ahfs.setAhfsE(rs.getString("TC_AHFS"));
				ahfs.setAhfsF(rs.getString("TC_AHFS_F"));
				ahfs.setAhfsNumber(rs.getString("TC_AHFS_NUMBER"));
				
				ahfsInfos.add(ahfs);
			}			
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search AHFS By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveAHFS resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
		    } catch (Exception e) { 
		    	  log.error("Cannot close retrieveAHFS prepared statement.", e); 
		    };		
		} 
		return ahfsInfos;
	}

	/***************************************************************************
	 * Gets the a list of Risk Minimization Measure Returns an empty List if 
	 * none are found that match.
	 * 
	 * @param drugCode
	 * @return the List of Risk Minimization Measure
	 */
	private List<RiskMinMeasure> retrieveRMMs(Connection con, Long drugCode) throws Exception {
		List<RiskMinMeasure> riskMinMeasures = new ArrayList<RiskMinMeasure>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {

			String query = "select rmm.* from WQRY_RISK_MIN_MEASURE rmm ";
			query = query + "where rmm.DRUG_CODE = ? ";
			query = query + "and (rmm.END_DATE > sysdate or rmm.END_DATE IS NULL)";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			while (rs.next()) {
				RiskMinMeasure rmm = new RiskMinMeasure();
				rmm.setDrugCode(rs.getLong("DRUG_CODE"));
				rmm.setRmmCode(rs.getLong("RMM_CODE"));
				rmm.setDescE(rs.getString("DESC_E"));
				rmm.setDescF(rs.getString("DESC_F"));
				rmm.setEndDate(rs.getDate("END_DATE"));
				
				riskMinMeasures.add(rmm);
			}
		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer("Search By drugCode [");
			message.append(drugCode);
			message.append("] failed");
			throw new Exception(message.toString());
		} finally {
			try { 
				  if (rs != null) rs.close();
			} catch (Exception e) { 
				  log.error("Cannot close retrieveRMMs resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveRMMs prepared statement.", e); 
			};		
		}
		return riskMinMeasures;
	}
	
	/***************************************************************************
	 * Gets the a list of Pharmacovigilance Activity Returns an empty List if 
	 * none are found that match.
	 * 
	 * @param drugCode
	 * @return the List of Pharmacovigilance Activity
	 */
	private List<PharmMonitorAct> retrievePharmMonitorActs(Connection con, Long drugCode) throws Exception {
		List<PharmMonitorAct> pharmMonitorActs = new ArrayList<PharmMonitorAct>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {

			String query = "select pharmMonAct.* from WQRY_PHARMACOVIG_MON_ACT pharmMonAct ";
			query = query + "where pharmMonAct.DRUG_CODE = ? ";
			query = query + "and (pharmMonAct.END_DATE > sysdate or pharmMonAct.END_DATE IS NULL)";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			while (rs.next()) {
				PharmMonitorAct pharmMonAct = new PharmMonitorAct();
				pharmMonAct.setDrugCode(rs.getLong("DRUG_CODE"));
				pharmMonAct.setPhaMonCode(rs.getLong("PHA_MON_CODE"));
				pharmMonAct.setDescE(rs.getString("DESC_E"));
				pharmMonAct.setDescF(rs.getString("DESC_F"));
				pharmMonAct.setEndDate(rs.getDate("END_DATE"));
				
				pharmMonitorActs.add(pharmMonAct);
			}
		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer("Search By drugCode [");
			message.append(drugCode);
			message.append("] failed");
			throw new Exception(message.toString());
		} finally {
			try {
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrievePharmMonitorActs resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrievePharmMonitorActs prepared statement.", e);
			};		
		} 
		return pharmMonitorActs;
	}

	/***************************************************************************
	 * Gets the a list of Special Identifier Returns an empty List if 
	 * none are found that match.
	 * 
	 * @param drugCode
	 * @return the List of Special Identifier
	 */
	private List<SpecialIdentifier> retrieveSIs(Long drugCode) throws Exception {
		List<SpecialIdentifier> specialIdentifiers = new ArrayList<SpecialIdentifier>();
		Connection con = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {

			String query = "select si.* from WQRY_SPECIAL_IDENTIFIER si ";
			query = query + "where si.DRUG_CODE = ? ";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			while (rs.next()) {
				SpecialIdentifier si = new SpecialIdentifier();
				si.setDrugCode(rs.getLong("DRUG_CODE"));
				si.setSiCode(rs.getLong("SI_CODE"));
				si.setDescE(rs.getString("DESC_E"));
				si.setDescF(rs.getString("DESC_F"));
				si.setDateAssigned(rs.getDate("DATE_ASSIGNED"));
				
				specialIdentifiers.add(si);
			}
		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer("Search By drugCode [");
			message.append(drugCode);
			message.append("] failed");
			throw new Exception(message.toString());
		} finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close prepared statement.", e); 
			};		
			try { 
				  if (con != null) con.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close connection.", e); 
			};
		} 
		return specialIdentifiers;
	}
	
	/***************************************************************************
	 * Gets the a list of Schedules by drug code. Returns an empty List if none
	 * is found that matchs.
	 * 
	 * @param drugCode
	 * @return the List of Schedules by drug code
	 */
	private List<Schedule> retrieveDrugSchedules(Long drugCode) throws Exception {
		List<Schedule> schedules = new ArrayList<Schedule>();
		Connection con = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select schedules.* from WQRY_SCHEDULE schedules "
					+ "where schedules.DRUG_CODE = ?";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			
			while (rs.next()) {				
				Schedule schedule = new Schedule();
				schedule.setDrugCode(rs.getLong("DRUG_CODE"));
				schedule.setScheduleE(rs.getString("SCHEDULE"));
				schedule.setScheduleCode(rs.getInt("SCHEDULE_CODE"));
				schedule.setScheduleF(rs.getString("SCHEDULE_F"));
				
				schedules.add(schedule);
			}
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Schedule By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveDrugSchedules resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveDrugSchedules prepared statement.", e); 
			};		
		}
		return schedules;
	}
	
	/***************************************************************************
	 * Gets the a list of ATC by drug code Returns an empty List if none are
	 * found that match.
	 * 
	 * @param drugCode
	 * @return the List of ATC by drug code
	 */
	private ATC retrieveAtcVO(Connection con, Long drugCode) throws Exception {
		ATC atc = new ATC();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {

			String query = "select atc.* from WQRY_ATC atc "
					+ "where atc.DRUG_CODE = ?";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			while (rs.next()) {
				atc.setDrugCode(rs.getLong("DRUG_CODE"));
				atc.setAtcE(rs.getString("TC_ATC"));
				atc.setAtcF(rs.getString("TC_ATC_F"));
				atc.setAtcNumber(rs.getString("TC_ATC_NUMBER"));
			}
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search ATC By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		 finally {
				try { 
					  if (rs != null) rs.close();
				} catch (Exception e) { 
					  log.error(" Cannot close retrieveAtcVO resultset.", e); 
				};	
				try { 
					  if (stmt != null) stmt.close(); 
				} catch (Exception e) { 
					  log.error("Cannot close retrieveAtcVO prepared statement.", e); 
				};
		}
		
		return atc;
	}

	private ATC retrieveAtcFourthLevelATC(Connection con, String atcFourthLevelNumber)
			throws Exception {
		ATC atc = new ATC();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		/*
		 * SL/2012-03-23 ADR1183: A new code table was added to contain all ATC
		 * codes, as opposed to only the codes currently used by products (in
		 * WQRY_ATC). This allows us to display the corresponding 4th-level code
		 * description for any 5th-level code, even when that 4th-level code is
		 * not currently used by any product in WQRY_ATC
		 */
		try {
			String query = "SELECT c.tc_atc_number, c.tc_atc_desc, c.tc_atc_desc_f "
					+ "FROM dpd_online_owner.WQRY_TC_FOR_ATC c "
					+ "where c.tc_atc_number = ?";
			
			stmt = con.prepareStatement(query);
			stmt.setString(1, atcFourthLevelNumber);

			rs = stmt.executeQuery();
			while (rs.next()) {
				atc.setDrugCode(rs.getLong("DRUG_CODE"));
				atc.setAtcE(rs.getString("TC_ATC"));
				atc.setAtcF(rs.getString("TC_ATC_F"));
				atc.setAtcNumber(rs.getString("TC_ATC_NUMBER"));
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		} finally {
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveAtcFourthLevelATC prepared statement.", e);
			};
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveAtcFourthLevelATC resultset.", e);
			};
		}

		return atc;
	}


	/***************************************************************************
	 * Gets the a list of Company by drug code Returns an empty List if none are
	 * found that match.
	 * 
	 * @param companyCode
	 * @return the List of Company by drug code
	 */
	private Company retrieveCompanyVO(Connection con, Long companyCode) throws Exception {
		Company company = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select co.* from WQRY_COMPANIES  co "
					+ "where co.COMPANY_CODE = ?";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, companyCode);

			rs = stmt.executeQuery();
			while (rs.next()) {
				company = new Company();
				company.setCompanyCode(rs.getLong("COMPANY_CODE"));
				company.setMfrCode(rs.getString("MFR_CODE"));
				company.setCompanyName(rs.getString("COMPANY_NAME"));
				company.setCompanyType(rs.getString("COMPANY_TYPE"));
				company.setSuiteNumner(rs.getString("SUITE_NUMNER"));
				company.setStreetNameE(rs.getString("STREET_NAME"));
				company.setStreetNameF(rs.getString("STREET_NAME_F"));
				company.setCityName(rs.getString("CITY_NAME"));
				company.setProvinceE(rs.getString("PROVINCE"));
				company.setProvinceF(rs.getString("PROVINCE_F"));
				company.setCountryE(rs.getString("COUNTRY"));
				company.setCountryF(rs.getString("COUNTRY_F"));
				company.setPostalCode(rs.getString("POSTAL_CODE"));
				company.setPostOfficeBox(rs.getString("POST_OFFICE_BOX"));
			}

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Company By companyCode [");
				message.append(companyCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveCompanyVO resultset.", e); 
			};
			try {
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveCompanyVO prepared statement.", e); 
			};				
		} 
		return company;
	}

	/***************************************************************************
	 * Gets the a list of Forms by drug code Returns an empty List if none are
	 * found that match.
	 * 
	 * @param drugCode
	 * @return the List of Forms by drug code
	 */
	private List<Form> retrieveForms(Connection con, Long drugCode) throws Exception {
		List<Form> formInfos = new ArrayList<Form>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String query = "select forms.* from WQRY_FORM forms "
					+ "where forms.DRUG_CODE = ?";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			
			while (rs.next()) {				
				Form form = new Form();
				form.setDrugCode(rs.getLong("DRUG_CODE"));
				form.setPharmaceuticalFormE(rs.getString("PHARMACEUTICAL_FORM"));
				form.setPharmaceuticalFormF(rs.getString("PHARMACEUTICAL_FORM_F"));
				
				formInfos.add(form);
			}
		} catch (HibernateException he) {
			
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Dosage Form By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveForms resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveForms prepared statement.", e); 
		    };		
		} 
		return formInfos;
	}

	/***************************************************************************
	 * Gets the a list of Packaging by drug code Returns an empty List if none
	 * are found that match.
	 * 
	 * @param drugCode
	 * @return the List of Packaging by drug code
	 */
	private List<Packaging> retrievePackagings(Connection con, Long drugCode) throws Exception {
		List<Packaging> packageInfos = new ArrayList<Packaging>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select distinct pkgs.* from WQRY_PACKAGING pkgs ";
			query = query + "where pkgs.DRUG_CODE = ?";
			query = query + " order by pkgs.PACKAGE_SIZE asc";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			
			while (rs.next()) {				
				Packaging packaging = new Packaging();
				packaging.setDrugCode(rs.getLong("DRUG_CODE"));
				packaging.setPackageSize(rs.getString("PACKAGE_SIZE"));
				packaging.setPackageSizeUnit(rs.getString("PACKAGE_SIZE_UNIT"));
				packaging.setPackageType(rs.getString("PACKAGE_TYPE"));
				packaging.setProductInformation(rs.getString("PRODUCT_INFORMATION"));
				packaging.setUpc(rs.getString("UPC"));
				
				packageInfos.add(packaging);
			}
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Packaging By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrievePackagings resultset.", e); };
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrievePackagings prepared statement.", e); };		
		} 

		return packageInfos;
	}

	/***************************************************************************
	 * Gets the a list of Routes by drug code Returns an empty List if none are
	 * found that match.
	 * 
	 * @param drugCode
	 * @return the List of Routes by drug code
	 */
	private List<Route> retrieveRoutes(Connection con, Long drugCode) throws Exception {
		List<Route> routeInfos = new ArrayList<Route>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select r.* " + "from WQRY_ROUTE r "
					+ "where r.DRUG_CODE = ?";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			
			while (rs.next()) {				
				Route route = new Route();
				route.setDrugCode(rs.getLong("DRUG_CODE"));
				route.setRouteOfAdministrationE(rs.getString("ROUTE_OF_ADMINISTRATION"));
				route.setRouteOfAdministrationF(rs.getString("ROUTE_OF_ADMINISTRATION_F"));
				
				routeInfos.add(route);
			}
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Route By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) {
				  log.error("Cannot close retrieveRoutes resultset.", e); };
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) {
				  log.error("Cannot close retrieveRoutes prepared statement.", e); 
			};		
		} 
		return routeInfos;
	}

	/**
	 * Retrieves a map of unique routes of administration
	 * 
	 * @return HashMap of distinct routes, per language
	 * @throws Exception
	 * @author Sylvain Larivière 2009-09-10 Updated 2009-10-26 to account for
	 *         English and French lists Updated 2012-06-07 to uncomment French
	 *         lists implementation now that content is translated
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, List<String>> retrieveUniqueRoutes()
			throws Exception {
		List<String> englishRoutes = new ArrayList<String>();
		List<String> frenchRoutes = new ArrayList<String>();
		HashMap<String, List<String>> allRoutes = new HashMap<String, List<String>>();

		try {
			String query = "select distinct(r.route_of_administration) "
					+ "from wqry_route r " + "where r.inactive_date is null "
					+ "order by r.route_of_administration";
			englishRoutes = getSession().createSQLQuery(query).list();
			allRoutes.put(ApplicationGlobals.LANG_EN, englishRoutes);

			query = "select distinct(r.route_of_administration_f) "
					+ "from wqry_route r " + "where r.inactive_date is null "
					+ "order by r.route_of_administration_f";
			frenchRoutes = getSession().createSQLQuery(query).list();
			allRoutes.put(ApplicationGlobals.LANG_FR, frenchRoutes);
		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer("retrieveUniqueRoutes [");
			message.append("] failed");
			throw new Exception(message.toString());
		}

		return allRoutes;
	}

	/***************************************************************************
	 * Gets the a list of Schedules by drug code Returns an empty List if none
	 * are found that match.
	 * 
	 * @param drugCode
	 * @return the List of Schedules by drug code
	 */
	private List<Schedule> retrieveSchedules(Connection con, Long drugCode) throws Exception {
		List<Schedule> schedules = new ArrayList<Schedule>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String query = "select schedules.* from WQRY_SCHEDULE schedules "
					+ "where schedules.DRUG_CODE = ?";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			
			while (rs.next()) {				
				Schedule schedule = new Schedule();
				schedule.setDrugCode(rs.getLong("DRUG_CODE"));
				schedule.setScheduleE(rs.getString("SCHEDULE"));
				schedule.setScheduleF(rs.getString("SCHEDULE_F"));
				
				schedules.add(schedule);
			}
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Schedule By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveSchedules resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveSchedules prepared statement.", e); 
			};		
		} 
		return schedules;
	}

	private ExternalStatus retrieveExternalStatus(Connection con, Long code) throws Exception {
		ExternalStatus externalStatus = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select status.* from WQRY_STATUS_EXTERNAL  status "
					+ "where status.EXTERNAL_STATUS_CODE = ?";
			
			stmt = con.prepareStatement(query);
			stmt.setLong(1, code);

			rs = stmt.executeQuery();
			
			while (rs.next()) {
				externalStatus = new ExternalStatus();
				externalStatus.setExternalStatusId(rs.getLong("EXTERNAL_STATUS_CODE"));
				externalStatus.setExternalStatusE(rs.getString("EXTERNAL_STATUS_ENGLISH"));
				externalStatus.setExternalStatusF(rs.getString("EXTERNAL_STATUS_FRENCH"));
			}
		} catch (Exception ex) {
			log.error("Stack Trace: ", ex);
			StringBuffer message = new StringBuffer(
					"Search External Status By External Status Code [");
			message.append(code);
			message.append("] failed");
			throw new Exception(message.toString());
		}
		finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveExternalStatus resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveExternalStatus prepared statement.", e); 
			};		
		} 
		return externalStatus;
	}
	/***************************************************************************
	 * Gets a Drug Status record Returns an empty record.
	 * 
	 * @param drugCode
	 * @return the Drug Status Record
	 */
	private DrugStatus retrieveStatusVO(Connection con, Long drugCode) throws Exception {
		DrugStatus drugStatus = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select status.* from WQRY_STATUS  status "
					+ "where status.DRUG_CODE = ?";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);

			rs = stmt.executeQuery();
			
			while (rs.next()) {	
				drugStatus = new DrugStatus();
				drugStatus.setDrugCode(rs.getLong("DRUG_CODE"));
				drugStatus.setExpirationDate(rs.getDate("EXPIRATION_DATE"));
				drugStatus.setHistoryDate(rs.getDate("HISTORY_DATE"));
				drugStatus.setLotNumber(rs.getString("LOT_NUMBER"));
				drugStatus.setOriginalMarketDate(rs.getDate("ORIGINAL_MARKET_DATE"));
				drugStatus.setStatusID(rs.getLong("STATUS_CODE"));						
				drugStatus.setExternalStatus(retrieveExternalStatus(con, rs.getLong("EXTERNAL_STATUS_CODE")));
			}
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(
					"More than one row with the given identifier was found") > 0) {
				log.warn("Data Problem:", he);
			} else {
				if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
					log.warn("Data Problem: " + he.getMessage());
				} else {
					log.error("Stack Trace: ", he);
					StringBuffer message = new StringBuffer(
							"Search Status By drugCode [");
					message.append(drugCode);
					message.append("] failed");
					throw new Exception(message.toString());
				}
			}
		}
	   finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) {
				  log.error("Cannot close resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close prepared statement.", e); 
			};		
		} 	
		return drugStatus;
	}

	/***************************************************************************
	 * Gets the a productMonograph by drug code Returns an empty List if none
	 * are found that match.
	 * 
	 * @param drugCode
	 * @return the product monograph by drug code
	 * SL/2015-06-22: No longer restricted to "Marketed" status
	 */
	public ProductMonograph retrievePM(Connection con, Long drugCode) throws Exception {
		ProductMonograph productMonograph = null;
		PreparedStatement stmt = null; 
		ResultSet rs = null;
		try {

			String query = "select pm.* from WQRY_PM_DRUG pm, WQRY_STATUS c"
					+ " where pm.DRUG_CODE = ?"
					+ " and pm.PM_DATE = (select max(a.PM_DATE) from WQRY_PM_DRUG a "
					+ " where a.DRUG_CODE = ?"
					+ " and a.PM_VER_NUMBER = (select max(b.PM_VER_NUMBER)"
					+ " from WQRY_PM_DRUG b "
					+ " where b.DRUG_CODE = ?"
					+ " and b.pm_date = (select max(a.PM_DATE) from WQRY_PM_DRUG a "
					+ " where a.DRUG_CODE = ?)))"
					+ " and c.DRUG_CODE = ?";

			stmt = con.prepareStatement(query);
			stmt.setLong(1, drugCode);
			stmt.setLong(2, drugCode);
			stmt.setLong(3, drugCode);
			stmt.setLong(4, drugCode);
			stmt.setLong(5, drugCode);

			rs = stmt.executeQuery();
			
			while (rs.next()) {	
				productMonograph = new ProductMonograph();
				productMonograph.setDrugCode(rs.getLong("DRUG_CODE"));
				productMonograph.setPmControlNumber(rs.getString("PM_CONTROL_NUMBER"));
				productMonograph.setPmDate(rs.getDate("PM_DATE"));
				productMonograph.setPmEnglishFName(rs.getString("PM_ENGLISH_FNAME"));
				productMonograph.setPmFrenchFName(rs.getString("PM_FRENCH_FNAME"));
				productMonograph.setPmNumber(rs.getInt("PM_NUMBER"));
				productMonograph.setPmVersionNumber(rs.getInt("PM_VER_NUMBER"));
			}

			log.debug("Search for Product Monograph By drugCode Query is: "
					+ query);

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Product Monograph By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrievePM resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrievePM prepared statement.", e); 
			};		
		}
		return productMonograph;
	}

	// SL/2013-02-13: Added Translate statements to sql to allow
	// accent-insensitive free-text searches
	private String buildSearchByNamesSql(SearchCriteriaBean criteria)
			throws HibernateException {

		StringBuffer query = new StringBuffer(basicDrugSummarySelect());
		query.append(basicDrugSummaryFromClause());
		if (isSearchingBySpecies(criteria)) {
			query.append(",WQRY_DRUG_VETERINARY_SPECIES species ");
			query.append(this.speciesDrugSummaryWhereClause(criteria));
		} else { 
			query.append(this.basicDrugSummaryWhereClause());
		    query.append(criteriaDrugSummaryWhereClause(criteria));
		}  
		if (criteria.getBiosimDrugSearch() !=null )
			query.append( this.extractBiosimdrugCodeCriterion());
		query.append(localizedSummaryOrderByClause());

		log.debug("Search by Names Query is: " + query);
        //System.out.println("Search by Names Query is: " + query );
		return query.toString();
	}

	private String extractExternalStatusCodeCriterion(String[] status) {
		return " and ste.EXTERNAL_STATUS_CODE in (" + this.fillWildcardsForPreparedStatementIN(this.stringArrayToList(status)) +")";
		
	}
	
	private String extractCompanyNameCriterion() {
		return " and drug.DRUG_CODE in (select drug_code from wqry_drug_product where company_code "
				+ "in (select company_code from WQRY_COMPANIES co where "
				+ StringsUtil.StringWithoutAccent("co.COMPANY_NAME") + " like ?))";
	}
	
	private String extractAigNumberCriterion() {
		return " AND drug.AI_GROUP_NO = ?";
	}
	
	private String extractBrandNameCriterion() {		
        String stringNew = " and ((" + StringsUtil.StringWithoutAccent("BRAND_NAME") + " like ?)" 
			     + " or ( " + StringsUtil.StringWithoutAccent("BRAND_NAME_F") + " like ?))";
			
		return stringNew;	
	}
	
	private String extractActiveIngredientsPart1(String columnName) {
		String stringNew = " AND drug.drug_code IN (select DISTINCT drug_code from wqry_active_ingredients "
				+ " WHERE" + StringsUtil.StringWithoutAccent (columnName) 
				+ " like ?";

	    return stringNew; 	
	}

	private String extractActiveIngredientsPartOr(String columnName) {
    	String stringNew = " OR "
				+ StringsUtil.StringWithoutAccent (columnName) 
				+ " like ?";
    	
		return stringNew;
	}
	  
    private String extractBiosimdrugCodeCriterion() {
		// si_code 6 is biosimiler specialIdentifier code
 		String formsQuery = "AND si.SI_code in (6) "   
                     +  " AND s.drug_code = si.drug_code \n";
 		return formsQuery;
 	}
   
	private ArrayList<String> stringArrayToList(String[] array) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (String value : array) {
			arrayList.add(value);
		}
		return arrayList;
	}
	
	private String fillWildcardsForPreparedStatementIN(List<String> criteria) {
		StringBuilder builder = new StringBuilder();

		for( int i = 0 ; i < criteria.size(); i++ ) {
		    builder.append("?,");
		}
		
		return builder.deleteCharAt(builder.length() -1).toString();
	}

	private String criteriaDrugSummaryWhereClause(SearchCriteriaBean criteria) {
		String wherePortion = "";

		if (isSearchingByDin(criteria)) {
			return wherePortion += this.extractDinCriterion();
		} else if (isSearchingByATC(criteria)) {
			return wherePortion += this.extractAtcCriterion()
					.toString();
		} else if (isSearchingByCompanyCode(criteria)) {
			return wherePortion += this.extractCompanyCodeCriterion();
		} else {
			// searching by names			
			if (criteria.getStatusCode() != null && criteria.getStatusCode().length > 0) {
				if (!criteria.getStatusCode()[0].equals("0")) { // not Select ALL
					wherePortion += this.extractExternalStatusCodeCriterion(criteria.getStatusCode());
				}
			}

			if (criteria.getCompanyCode() != null
					&& criteria.getCompanyCode() != 0) {
				wherePortion += this.extractCompanyCodeCriterion();
			}
			
			if (criteria.getCompanyName() != null
					&& criteria.getCompanyName().length() > 0) {
				wherePortion += this.extractCompanyNameCriterion();
			}

			if (criteria.getAigNumber() != null
					&& criteria.getAigNumber().length() > 0) {
				wherePortion += this.extractAigNumberCriterion();
			}

			if (criteria.getBrandName() != null
					&& criteria.getBrandName().length() > 0) {
				wherePortion += this.extractBrandNameCriterion();
			}

			if (criteria.getActiveIngredient() != null
					&& criteria.getActiveIngredient().length() > 0) {
			   String activeIngredients = criteria.getActiveIngredient().trim().toUpperCase();
               wherePortion += localizedActiveIngredients(activeIngredients); 
			}

			if (criteria.getRoute() != null && criteria.getRoute().length > 0) {
				wherePortion = includeSelectedCriteriaItems(
						criteria.getRoute(), wherePortion, ROUTE_CRITERIA);
			}

			if (criteria.getDosage() != null && criteria.getDosage().length > 0) {
				wherePortion = includeSelectedCriteriaItems(
						criteria.getDosage(), wherePortion,
						DOSAGE_FORM_CRITERIA);
			}

			if (criteria.getSchedule() != null
					&& criteria.getSchedule().length > 0) {
				wherePortion = includeSelectedCriteriaItems(
						criteria.getSchedule(), wherePortion, SCHEDULE_CRITERIA);
			}
			
			if (criteria.getDrugClass() != null
					&& criteria.getDrugClass().length > 0) {
				wherePortion = includeSelectedCriteriaItems(
						criteria.getDrugClass(), wherePortion,
						DRUG_CLASS_CRITERIA);
			}
			
			if (isSearchingBySpecies(criteria)) {
				wherePortion = includeSelectedCriteriaItems(
						            criteria.getVetSpecies(), wherePortion,
						            SPECIES_CRITERIA);
			}
			
		}
		return wherePortion;
	}

	private String extractCompanyCodeCriterion() {
		return " and drug.COMPANY_CODE = ?";
	}

	private boolean isSearchingByCompanyCode(SearchCriteriaBean criteria) {
		return (criteria.getCompanyCode() != null
				&& criteria.getCompanyCode().longValue() > 0) ;
	}

	private boolean isSearchingByATC(SearchCriteriaBean criteria) {
		return (criteria.getAtc() != null && criteria.getAtc().length() > 0);
	}

	private boolean isSearchingByDin(SearchCriteriaBean criteria) {
		return (criteria.getDin() != null && criteria.getDin().length() > 0);
	}
	
	private boolean isSearchingBySpecies(SearchCriteriaBean criteria) {
		boolean speciesSearch = false;
				
		if (criteria.getVetSpecies() != null
				&& criteria.getVetSpecies().length> 0) {
			log.debug("isSearchingBySpecies selection = " +criteria.getVetSpecies()[0] );
			speciesSearch= (!criteria.getVetSpecies()[0].equals("0"));
		}
		log.debug("isSearchingBySpecies speciesSearch = " + speciesSearch);		
		return speciesSearch;
		
	}	
	
	private String includeSelectedCriteriaItems(String[] items,
			String whereClause, int criteriaType) {
		String formsQuery = "";
		String tableName = "";
		String fieldname = "";

		if (items[0].equals("0") && items.length == 1) { //"Select all" could be included with other items)
			log.debug("select all selected");
		} else {
			switch (criteriaType) {
			case ROUTE_CRITERIA:
				tableName = "WQRY_ROUTE";
				fieldname = localizedSearchColumnFor(ROUTE_COLUMN);
				break;
			case DOSAGE_FORM_CRITERIA:
				tableName = "WQRY_FORM";
				fieldname = localizedSearchColumnFor(FORM_COLUMN);
				break;
			case SCHEDULE_CRITERIA:
				tableName = "WQRY_SCHEDULE";
				fieldname = localizedSearchColumnFor(SCHEDULE_COLUMN);
				break;
			case DRUG_CLASS_CRITERIA:
				tableName = "WQRY_DRUG_PRODUCT";
				fieldname = DRUG_CLASS_COLUMN;
				break;
			case SPECIES_CRITERIA:
				tableName = "WQRY_DRUG_VETERINARY_SPECIES";
				fieldname = localizedSearchColumnFor(SPECIES_COLUMN);
				break;								
			}

			// collect all items in the criteria, comma-separated
			formsQuery = " and drug.drug_code in (Select t.drug_code From "
					+ tableName + " t " + "Where t." + fieldname + " in ("
					+ this.fillWildcardsForPreparedStatementIN(this.stringArrayToList(items)) + "))";
		}
		whereClause += formsQuery;

		return whereClause;
	}

	/**
	 * Retrieves a map of unique pharmaceutical forms
	 * 
	 * @return HashMap of distinct pharmaceutical forms, per language
	 * @throws Exception
	 * @author Sylvain Larivière 2009-09-10 <br/>
	 *         Updated 2009-10-26 to account for English and French lists.
	 *         Updated 2012-06-07 to uncomment French lists implementation now
	 *         that content is translated
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, List<String>> retrieveUniqueForms() throws Exception {
		List<String> englishForms = new ArrayList<String>();
		List<String> frenchForms = new ArrayList<String>();
		HashMap<String, List<String>> allForms = new HashMap<String, List<String>>();

		try {
			String query = "select distinct(f.pharmaceutical_form) "
					+ "from wqry_form f " + "where f.inactive_date is null "
					+ "order by f.pharmaceutical_form";
			englishForms = getSession().createSQLQuery(query).list();
			allForms.put(ApplicationGlobals.LANG_EN, englishForms);

			query = "select distinct(f.pharmaceutical_form_f) "
					+ "from wqry_form f " + "where f.inactive_date is null "
					+ "order by f.pharmaceutical_form_f";
			frenchForms = getSession().createSQLQuery(query).list();
			allForms.put(ApplicationGlobals.LANG_FR, frenchForms);

		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer(
					"Search Unique Pharmaceutical Forms [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return allForms;
	}

	/**
	 * Retrieves a map of unique schedules
	 * 
	 * @return HashMap of distinct schedules
	 * @throws Exception
	 * @author Sylvain Larivière 2009-09-10 Updated 2009-10-26 to account for
	 *         English and French lists Updated 2012-06-07 to uncomment French
	 *         lists implementation now that content is translated
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, List<String>> retrieveUniqueSchedules()
			throws Exception {
		List<String> englishSchedules = new ArrayList<String>();
		List<String> frenchSchedules = new ArrayList<String>();
		HashMap<String, List<String>> allSchedules = new HashMap<String, List<String>>();

		try {
			String query = "select distinct(sc.schedule) "
					+ "from wqry_schedule sc "
					+ "where sc.inactive_date is null "
					+ "order by sc.schedule";
			englishSchedules = getSession().createSQLQuery(query).list();
			allSchedules.put(ApplicationGlobals.LANG_EN, englishSchedules);

			query = "select distinct(sc.schedule_f) "
					+ "from wqry_schedule sc "
					+ "where sc.inactive_date is null "
					+ "order by sc.schedule_f";
			frenchSchedules = getSession().createSQLQuery(query).list();
			allSchedules.put(ApplicationGlobals.LANG_FR, frenchSchedules);

		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer(
					"Search Unique Pharmaceutical Forms [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return allSchedules;
	}

	/**
	 * Retrieves a List of unique DrugClass objects
	 * 
	 * @throws Exception
	 * @author Sylvain Larivière 2010-10-15
	 */
	public List<DrugClass> retrieveUniqueDrugClasses() throws Exception {
		List<DrugClass> allClasses = new ArrayList<DrugClass>();
		Connection con = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select distinct class_code, CLASS, CLASS_F "
				+ "from WQRY_DRUG_PRODUCT where class_code is not null " + "order by "
					+ localizedSearchColumnFor("class");

			log.debug("Retrieving unique drug classes :" + query);

			stmt = con.prepareStatement(query.toString());
			rs = stmt.executeQuery();

			while (rs.next()) {
				DrugClass drugClass = new DrugClass();
				drugClass.setDrugClassId(rs.getLong("class_code"));
				drugClass.setDrugClassE(rs.getString("CLASS"));
				drugClass.setDrugClassF(rs.getString("CLASS_F"));
				allClasses.add(drugClass);				
			}
		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer(
					"retrieveUniqueDrugClasses [");
			message.append("] failed");
			throw new Exception(message.toString());
		} finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close retrieveUniqueDrugClasses resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close();
			} catch (Exception e) { 
				  log.error("Cannot close retrieveUniqueDrugClasses prepared statement.", e); 
		    };
			con.close();
		}
		return allClasses;
	}

	/**
	 * Retrieves a List of unique Spices objects
	 * 
	 * @throws Exception
	 * @author Sunny Lo, 2017-10-01
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, List<String>> retrieveUniqueSpecies()
			throws Exception {
		List<String> englishSpecies = new ArrayList<String>();
		List<String> frenchSpecies = new ArrayList<String>();
		HashMap<String, List<String>> allSpecies = new HashMap<String, List<String>>();

		try {
			String query = "select distinct(species.VET_SPECIES) "
					+ "from WQRY_DRUG_VETERINARY_SPECIES species "
					+ "order by species.VET_SPECIES";
			englishSpecies = getSession().createSQLQuery(query).list();
			allSpecies.put(ApplicationGlobals.LANG_EN, englishSpecies);

			query = "select distinct(species.VET_SPECIES_F) "
					+ "from WQRY_DRUG_VETERINARY_SPECIES species "
					+ "order by species.VET_SPECIES_F";
			frenchSpecies = getSession().createSQLQuery(query).list();
			allSpecies.put(ApplicationGlobals.LANG_FR, frenchSpecies);

		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer(
					"Search Unique Drug Veterinary Species [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return allSpecies;
	}

	/**
//	 * @see basicDrugSummarySelect()
	 * @return A list of DrugSummaryBean's used to populate DrugProduct search
	 *         result summaries. Updated SL/2012-06-07 for French lists
	 *         implementation now that content is translated
	 * @author Sylvain Larivière  2009-10-05
	 */
	/*
	 * NOTE that the list constants below match the order of fields of the
	 * SELECT clause in basicDrugSummarySelect()
	 */
	private List<DrugSummaryBean> populateDrugProductSummaries(List<BasicDrugSummaryBean> drugData,
			HttpServletRequest request) throws NumberFormatException {
		List<DrugSummaryBean> drugSummaries = new ArrayList<DrugSummaryBean>();
		String pmE = "";
		String pmF = "";
		Long drugCode = new Long(0);

		try {
			for (BasicDrugSummaryBean data : drugData) {
				DrugProduct drug = new DrugProduct();
				DrugVeterinarySpecies vetSpecies = new DrugVeterinarySpecies();
				
				drug.setDrugCode(data.getDrugCode());
				drug.setBrandNameE(data.getBrandName());
				drug.setBrandNameF(data.getBrandNameF());
				drug.setDrugIdentificationNumber(data.getDurgIdentificationNumber());
				drug.setCompanyCode(data.getCompanyCode());
				drug.setDrugClassE(data.getDrugClass());
				drug.setDrugClassF(data.getDrugClassF());
				drug.setNumberOfAis(data.getNumberOfAis());
				drug.setAiGroupNo(data.getAiGroupNo());
				Long drugClassCode = data.getClassCode();
				drug.setClassCode(drugClassCode);

				pmE=StringsUtil.emptyForNull(data.getPmEnglishFName());
				pmF=StringsUtil.emptyForNull(data.getPmFrenchFName());
                
				//Only get species data if it is veternary class 
				if ( drugClassCode ==  ApplicationGlobals.VETERINARY_CLASS_CODE )
				{
				    String vetSpeciesE = StringsUtil.emptyForNull(data.getVetSpeciesE());
				    String vetSpeciesF = StringsUtil.emptyForNull(data.getVetSpeciesF());
				    vetSpecies.setVetSpeciesE(vetSpeciesE);
				    vetSpecies.setVetSpeciesF(vetSpeciesF);
				}    
				
				String company = data.getCompanyName();

				ExternalStatus extStatus = new ExternalStatus();
				extStatus.setExternalStatusE(data.getExternalStatusE());
				extStatus.setExternalStatusF(data.getExternalStatusF());
				
				DrugStatus status = new DrugStatus();
				status.setStatusID(data.getExternalStatusCode());
				status.setExternalStatus(extStatus);
				
				Schedule schedule = new Schedule(data.getScheduleE(), data.getScheduleF());
			
				ActiveIngredients firstAI = new ActiveIngredients();
				firstAI.setDosageUnitE(data.getDosageUnitE());
				firstAI.setDosageUnitF(data.getDosageUnitF());
				firstAI.setDosageValue(data.getDosageValue());
				firstAI.setDrugCode(drugCode);
				firstAI.setIngredientE(data.getIngredientE());
				firstAI.setIngredientF(data.getIngredientF());
				firstAI.setStrength(data.getStrength());
				firstAI.setStrengthUnitE(data.getStrengthUnitE());
				firstAI.setStrengthUnitF(data.getStrengthUnitF());

				// Implementation for June 13, 2018
				// isScheduleCDinIssued added for the Radiopharmaceuticals requirement
				DrugBean drugInfo = new DrugBean();
				DrugSummaryBean bean = new DrugSummaryBean(drug, company,
						status, pmE, pmF, schedule, firstAI, vetSpecies,
						drugInfo.isScheduleCDinIssued(this.retrieveSIs(data.getDrugCode())));

				drugSummaries.add(bean);
			}
		} catch (NumberFormatException n) {
			log.error("Stack Trace: ", n);
			StringBuffer message = new StringBuffer(
					"Populate Drug Products failed");
			throw new NumberFormatException(message.toString());
		} catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("Populate Drug Products [");
			message.append("] failed");
		}

		return drugSummaries;
	}
	
	/**
	 * @author Sylvain Larivière 2009-10-05, 2016-08-22
//	 * @param criteria
	 *            The current SearchCriteriaBean instance.
	 * @return The basic Select statement common to all returned DrugProduct
	 *         summaries
	 *         <p>
	 *         Updated 2012-06-07 to uncomment French lists implementation now
	 *         that content is translated, and 2016-08-22 to implement DataTable
	 *         Server-side processing.
	 *         </p>
//	 * @see populateDrugProductSummaries
	 */
	/*
	 * NOTE that the order of fields in the SELECT clause below, except for the
	 * localizedSummaryBrandNameSortingClause, MUST match the class-level
	 * positional constants in the class declarations
	 */
	private String basicDrugSummarySelect() {
		StringBuffer query = new StringBuffer();
		query.append("select distinct drug.drug_code, drug.brand_name, drug.brand_name_F, ");
		query.append("drug.drug_identification_number, drug.company_code, ");
		query.append("drug.class, drug.class_f, drug.number_of_ais, drug.ai_group_no, ");
		query.append("co.COMPANY_NAME, ste.EXTERNAL_STATUS_ENGLISH, ste.EXTERNAL_STATUS_FRENCH, ste.EXTERNAL_STATUS_CODE, ");
		query.append("s.schedule, s.schedule_f, drug.class_code, ");
		query.append("i.ingredient, i.ingredient_f, i.strength, i.strength_unit, i.strength_unit_f, ");
		query.append("i.DOSAGE_VALUE, i.DOSAGE_UNIT, i.DOSAGE_UNIT_F, ");
		query.append("pm.PM_ENGLISH_FNAME, pm.PM_FRENCH_FNAME ");
		query.append(localizedSummaryBrandNameSortingClause());

		return query.toString();
	}
	
	private String basicDrugSummaryWhereClause() {
		StringBuffer buf = new StringBuffer();
		buf.append(" where  ");
		buf.append(commonDrugSummaryWhereClause( ));
		return buf.toString();
	}

	private String speciesDrugSummaryWhereClause(SearchCriteriaBean criteria) {
		StringBuffer buf = new StringBuffer();
		buf.append("where drug.drug_code = species.DRUG_CODE");
		buf.append( " and drug.CLASS_CODE = 8 ");
		buf.append(criteriaDrugSummaryWhereClause(criteria));
		buf.append( " and ");
		buf.append(commonDrugSummaryWhereClause( ));		
		return buf.toString();
	}
	
	private String commonDrugSummaryWhereClause( ) {
		
		StringBuffer buf = new StringBuffer();

		buf.append( " drug.DRUG_CODE = st.DRUG_CODE ");
		buf.append("and st.EXTERNAL_STATUS_CODE = ste.EXTERNAL_STATUS_CODE ");
		buf.append("and drug.COMPANY_CODE = co.COMPANY_CODE ");
		buf.append("and drug.drug_code = s.drug_code ");
		buf.append("and drug.drug_code = r.drug_code ");
		buf.append("and drug.drug_code = f.drug_code ");
		buf.append("and drug.drug_code = i.drug_code(+) ");
		buf.append("and drug.drug_code = pm.DRUG_CODE(+) ");
		buf.append("and i.id = ");
		buf.append("(select min(id) from wqry_active_ingredients i where drug.drug_code = i.drug_code) ");
		
		return buf.toString();
	}
	
	private String basicDrugSummaryFromClause() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("from WQRY_DRUG_PRODUCT drug, WQRY_COMPANIES co, WQRY_STATUS st, ");
		buf.append("WQRY_STATUS_EXTERNAL ste, wqry_route r, wqry_form f, wqry_schedule s, WQRY_SPECIAL_IDENTIFIER SI, ");
		buf.append("wqry_active_ingredients i, WQRY_PM_DRUG pm ");
		
		return buf.toString();
	}

	/**
	 * @param columnName
	 *            The English sorting column
	 * @return A String representing the appropriate version of the sorting
	 *         column based on the user language. Simply adds "_f" to the passed
	 *         column name if the user language is French. Based on database
	 *         naming convention.
	 */
	private String localizedSearchColumnFor(String columnName) {
		String result = "";
		Boolean isFrench = false; //todo move checking lang out of dao to ??
//				ApplicationGlobals.LANG_FR.equals(ApplicationGlobals
//				.instance().getUserLocale().getLanguage());

		if (isFrench) {
			result = columnName + "_f";
		} else {
			result = columnName;
		}
		return result;
	}

	/**
	 * @return A String containing an SQL case statement to use in the standard
	 *         drug summary search in order to sort brand names by either the
	 *         value in the user language if it exists, or else by the
	 *         corresponding value in the other official language.
	 *         <p>
	 *         Updated to handle DataTable server-side processing, including
	 *         sorting on other columns. In this case, the user language cannot
	 *         change between requests, and so no language checking is required.
	 *         </p>
	 * @author Sylvain Larivière 2013-02-07, 2016-08-11
//	 * @param criteria
	 *            The current SearchCriteriaBean instance.
	 */
	private String localizedSummaryBrandNameSortingClause() {
		String result = "";

		if (isAjaxRequest
				&& !ajaxBean.getColumnOrderMap().containsKey(new Integer(
						ApplicationGlobals.DATA_TABLE_BRAND_NAME_COLUMN))) {
			/*
			 * User changed the sorting order or the sorting column. Unless this
			 * is an Ajax request that includes the brand name column as a sort
			 * column, do nothing: there should be no SORT_COLUMN pseudo-column.
			 * 
			 * But if the brand name column IS included, either by itself or
			 * with other columns, then do include the SORT_COLUMN. It will be
			 * used in the ORDER BY clause.
			 */
		} else {
			/*
			 * Always sort by brand name, ascending, and DIN: therefore, this
			 * CASE clause needs to be added
			 */
			Boolean isFrench = false; // todo move checking lang out of dao
//					ApplicationGlobals.LANG_FR
//					.equals(ApplicationGlobals.instance().getUserLocale()
//							.getLanguage());
			if (isFrench) {
				result = ", CASE WHEN DRUG.BRAND_NAME_F IS NOT NULL THEN UPPER(DRUG.BRAND_NAME_F)"
						+ " WHEN DRUG.BRAND_NAME IS NOT NULL THEN upper(DRUG.BRAND_NAME)"
						+ " ELSE NULL END AS SORT_COLUMN ";
			} else {
				result = ", CASE WHEN DRUG.BRAND_NAME IS NOT NULL THEN UPPER(DRUG.BRAND_NAME)"
						+ " WHEN DRUG.BRAND_NAME_F IS NOT NULL THEN upper(DRUG.BRAND_NAME_F)"
						+ " ELSE NULL END AS SORT_COLUMN ";
			}
		}
		return result;
	}

	/**
	 * @return A String containing an SQL order by clause that is adapted to the
	 *         user language for the Search Results Summary Page, where all data
	 *         is sorted on the brand name column of the table. English and
	 *         French brand names are sorted together, in a case- and
	 *         accent-insensitive order.
//	 * @see localizedSummaryBrandNameSortingClause
//	 * @param criteria
	 *            The current SearchCriteriaBean instance.
	 */
	private String localizedSummaryOrderByClause() {
		StringBuffer result = new StringBuffer(" ORDER BY ");
		String translateClause = StringsUtil.StringWithoutAccent("SORT_COLUMN");

		if (isAjaxRequest) {
			List<DataTableColumn> sortCols = ajaxBean.getSortColumns();
			Map<Integer, String> colOrdering = ajaxBean.getColumnOrderMap();
			
			for (DataTableColumn col : sortCols) {
				int index = col.getColumnIndex();
				/*
				 * If the entry key matches the brand name column index, apply the case- and
				 * accent- insensitivity; the entry value is the sort direction.
				 */
				if (Integer.parseInt(ApplicationGlobals.DATA_TABLE_BRAND_NAME_COLUMN) == index) {
					result.append(translateClause);
				}else{
					//sort by the field name matching the DataTable column we are sorting on
					result.append(col.getFieldName() + " ");
				}
				result.append(colOrdering.get(Integer.valueOf(index)) + ",");
			}
			// remove the last comma
			result.deleteCharAt(result.length()-1);
		} else {
			// initial jsp page sorting
			result.append(translateClause);
			result.append(", DRUG.DRUG_IDENTIFICATION_NUMBER");
		}
		
		return result.toString();
	}
	
	/**
	 * @return A String containing an SQL Where clause statement for activeIngredients
	 * 
	 * @param activeIngredients
	 *            The input active ingredients strings could be in English or in French 
	 */
	private String localizedActiveIngredients(String activeIngredients) {
		String result = "";
		int andPosition = -1;
		int orPosition = -1;
		int andLen = 0; 
		int orLen = 0;  
		String orString=" OR ";
		String andString= " AND "; 
		String columnName = INGREDIENT_COLUMN;
		int indexCounter = 0;
		String ingredient = "";	 
		
		Boolean isFrench = false; //todo move checking lang out of dao to ??
		// ApplicationGlobals.LANG_FR.equals(ApplicationGlobals
//				.instance().getUserLocale().getLanguage());
		
		if (isFrench) 
			columnName = INGREDIENT_COLUMN + "_F";
		
		
		andPosition = activeIngredients.indexOf(andString);
		orPosition = activeIngredients.indexOf(orString); 
		
		andLen = andString.length();
		orLen = orString.length();		
		
		result += this.extractActiveIngredientsPart1(columnName);
		this.listOfIngredents = new ArrayList<>(); 
		if (andPosition != -1) {
			// there is an "AND" found in the string
			int pos = -1;

			Boolean andFound = false;

			while (andPosition > 0) {
				// Is the next operator an "AND" or an "OR"?
				if (orPosition == -1) {
					pos = andPosition;
					andFound = true;
				} else if (andPosition < orPosition
						&& andPosition != -1) {
					pos = andPosition;
					andFound = true;
				} else {
					pos = orPosition;
					andFound = false;
				}

				// Get the ingredient, and place it to the listOfIngredients
				ingredient = activeIngredients.substring(0, pos);
				this.listOfIngredents.add( indexCounter , ingredient.trim());
     			indexCounter = indexCounter + 1;
				
				if (andFound) {
					result += ")" + this.extractActiveIngredientsPart1(columnName);		
					// Remove the first ingredient from full string
					activeIngredients = activeIngredients.substring(pos + andLen);
				} else {
					result += this.extractActiveIngredientsPartOr(columnName);	
					// Remove the first ingredient from full string
					activeIngredients = activeIngredients.substring(pos + orLen);
				}

				// Check for other ingredients
				orPosition = activeIngredients.indexOf(orString);
				andPosition = activeIngredients.indexOf(andString);
			}
		}

		// no "AND" found - just extract the "OR"
		String orPortion = "";

		// Check if there are other ingredient with OR operator
		orPosition = activeIngredients.indexOf(orString);

		if (orPosition != -1) {
			while (orPosition > 0) {
				
				// Get the ingredient, and place it to the listOfIngredents
				ingredient = activeIngredients.substring(0, orPosition);
				this.listOfIngredents.add( indexCounter , ingredient.trim());               
				indexCounter = indexCounter + 1;

				// Remove the ingredient
				activeIngredients = activeIngredients.substring(orPosition + orLen);
				
				// Check if there is more ingredient
				orPosition = activeIngredients.indexOf(orString);

				orPortion += this.extractActiveIngredientsPartOr(columnName);
			}
		}

		// Finalize the where clause by closing the ")"
		result += orPortion + ")";
		
		this.listOfIngredents.add( indexCounter,activeIngredients.trim() );
		return result;
	}	
	
	/**
	 * @return a list of Search CriteriaIndex Bean, each active Ingredient associates a corresponding index
	 * 
//	 * @param AllActiveIngredients :The input active strings could be in English or in French
	 */
	private List<SearchCriteriaIndexBean> getActiveIngredientIndex(ArrayList<SearchCriteriaIndexBean> indices, int indexCounter) {
	        int i = 0;
	        while (i < listOfIngredents.size()) {
	        	String ingredient  = this.listOfIngredents.get(i);
                // Replacing the like ? of the SQL statement with the like '%ingredient%'        	
   	            indices.add(this.populateSearchCriteriaIndexBean(true
   	            		    , indexCounter + i+1     // important: index from 1 (first ? at the sql statement) 
	            		    ,"%" + StringsUtil.AsUnAccentedUppercase(ingredient) + "%"
	            		    ,ingredient.getClass() ));
	            i++;
	        }
		
	    return indices;
	}	
	
	@SuppressWarnings("unchecked")
	public List<ExternalStatus> retrieveAllExternalStatuses() throws Exception {
		List<ExternalStatus> statusList = new ArrayList<ExternalStatus>();
		Session session = getSession();
		if (session != null) {
		     //PFIX ADR0144-1516: only return external statuses that are actually used by a product
		     statusList =session.createQuery("select distinct ext from DrugStatus s inner join s.externalStatus ext").list();
		}
		
		return statusList;
	}

	/**
	 * @param criteria
	 *            The current SearchCriteriaBean
	 * @param request
	 *            The current request
	 * @return A List of DrugSummaryBean's that is suitable for the current
	 *         DataTable Ajax request
	 * @throws Exception
	 * @author SL/2016-08-22
	 */
	@SuppressWarnings({ "rawtypes" })
	public List getNextResults(SearchCriteriaBean criteria,
			HttpServletRequest request) throws Exception {
		String query = "";
		List beans = new ArrayList();
		HttpSession session=null;

		if ((ajaxBean.getAjaxStatus().equals(AjaxRequestStatus.INACTIVE) || !ajaxBean
				.isUpdatingPageNumber()) || ajaxBean.isUpdatingSorting()) {
			/*
			 * No usable query is available since only a count was obtained so
			 * far. Generate the query sql.
			 * 
			 * Currently, only searching by names or company code could possibly
			 * generate a number of search results that is above the DataTable
			 * server-processing threshold. Hence excluding other types of
			 * searches.
			 */
			if (criteria.getCompanyCode() != null
					&& criteria.getCompanyCode().longValue() > 0) {
				query = buildSearchByCompanyCodeSql(criteria);
			} else {
				query = buildSearchByNamesSql(criteria);
			}

		} else if (ajaxBean.isUpdatingPageNumber()) {
			// stored sql may be used: search unchanged
			query = (String) request.getSession().getAttribute(ApplicationGlobals.SQL_QUERY);
		} else if (ajaxBean.isUpdatingSorting()) {
			// re-query with new sort order
		}
		log.debug("getNextResults query=" + query);
		Connection con = this.getConnection();
		PreparedStatement stmt =null;
		try {
			  session = request.getSession(); 
			  if (session != null) {
				  session.setAttribute(ApplicationGlobals.SQL_QUERY, query);
			      stmt = con.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			      List<SearchCriteriaIndexBean> criteriaIndex = this.getCriteriaIndex(criteria);
			      for (SearchCriteriaIndexBean index : criteriaIndex) {
				     if (index.getSelected()) {
				    	 log.debug("getNextResults index=" + index.getIndex() + "criteria = " + index.getCriteria());
				    	if (index.getCriteriaObjectClass() == String.class) {
							stmt.setString(index.getIndex(), (String) index.getCriteria());
					    } else if (index.getCriteriaObjectClass() == Long.class) {
						stmt.setLong(index.getIndex(), (Long)index.getCriteria());
					}
				}
			}
			
			List<BasicDrugSummaryBean> basicDrugSummaries = this.populateBasicDrugSummaries(stmt.executeQuery(), ajaxBean);

			     if (basicDrugSummaries.size() > 0) {
				     beans = this.populateDrugProductSummaries(basicDrugSummaries, request);
			     }
			  }   
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By ATC [");
				// message.append(criteria.toString());
				message.append("] failed");
				throw new Exception(message.toString());
			}
		} finally {
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close getNextResults prepared statement.", e); 
			};		
			con.close();
		}

		return beans;
	}

	private SearchCriteriaIndexBean populateSearchCriteriaIndexBean(boolean selected, int index, Object criteria, Object criteriaObjectClass) {
		SearchCriteriaIndexBean bean = new SearchCriteriaIndexBean();
		bean.setSelected(selected);
		bean.setIndex(index);
		bean.setCriteria(criteria);
		bean.setCriteriaObjectClass(criteriaObjectClass);
		log.debug("populateSearchCriteriaIndexBean index:= "+ index + "criteria:= "+ criteria + "class" + criteriaObjectClass);
		return bean;
	}
	
	private List<SearchCriteriaIndexBean> getCriteriaIndex(SearchCriteriaBean criteria) {
		ArrayList<SearchCriteriaIndexBean> indices = new ArrayList<SearchCriteriaIndexBean>();
		//This indexCounter shall match number of ? in getQueryResultsCount's sql statement
		int indexCounter = 0;
		
		if (criteria.getStatusCode() != null && criteria.getStatusCode().length > 0) {
			for (String status : criteria.getStatusCode()) {
				if (!status.equals("0")) { // not Select ALL
					indexCounter = indexCounter + 1;
					log.debug("getCriteriaIndex status indexCounter" + indexCounter); 
					indices.add(this.populateSearchCriteriaIndexBean(true, indexCounter, status, status.getClass()));
				} 
			}		
		} else {
			indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
		}

		if (criteria.getCompanyCode() != null) {
			if (criteria.getCompanyCode() != 0) {
				indexCounter = indexCounter + 1;
				indices.add(this.populateSearchCriteriaIndexBean(true, indexCounter, criteria.getCompanyCode(), criteria.getCompanyCode().getClass()));
			} else {
				indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
			}
		} else {
			indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
		}
	
		if (criteria.getCompanyName() != null
				&& criteria.getCompanyName().length() > 0) {
			indexCounter = indexCounter + 1;
			// Company name treatment
			// add the % sign to the front and back of the company name for like condition
			String inputName = criteria.getCompanyName();
			String companyName = "%" + StringsUtil.AsUnAccentedUppercase(criteria.getCompanyName())+ "%";
            log.debug(" Company Input Name: " + inputName + "Output Name" + companyName);
			indices.add(this.populateSearchCriteriaIndexBean(true, indexCounter, companyName, criteria.getCompanyName().getClass()));
		} else {
			indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
		}
		
		if (criteria.getAigNumber() != null
				&& criteria.getAigNumber().length() > 0) {
			indexCounter = indexCounter + 1;
			indices.add(this.populateSearchCriteriaIndexBean(true, indexCounter, criteria.getAigNumber(), criteria.getAigNumber().getClass()));
		} else {
			indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
		}
		
		if (criteria.getBrandName() != null
				&& criteria.getBrandName().length() > 0) {
			log.debug(" Product / Brand Input Name: " + criteria.getBrandName()); 
			// English
			indexCounter = indexCounter + 1;
			indices.add(this.populateSearchCriteriaIndexBean(true, indexCounter, "%" + StringsUtil.AsUnAccentedUppercase(criteria.getBrandName()) + "%", criteria.getBrandName().getClass()));
			
			// French
			indexCounter = indexCounter + 1;
			indices.add(this.populateSearchCriteriaIndexBean(true, indexCounter, "%" + StringsUtil.AsUnAccentedUppercase(criteria.getBrandName()) + "%", criteria.getBrandName().getClass()));
		} else {
			indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
		}

		if (criteria.getActiveIngredient() != null
				&& criteria.getActiveIngredient().length() > 0) {
			
			log.debug("getCriteriaIndex ActiveIngredient indexCounter before" + indexCounter);
			// Each ingredient needs to be added to indices.
			// IndexCounter is the index of the criteria indices list where addition begins.
			getActiveIngredientIndex(indices, indexCounter);
			
			// After the addition is completed, indexCounter is the index of the indices list
			// where the last active ingredient is located.
			indexCounter = indexCounter + listOfIngredents.size();  
			log.debug("getCriteriaIndex ActiveIngredient indexCounter after" + indexCounter);
	    } else {
			indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
		}	

		
		if (criteria.getRoute() != null && criteria.getRoute().length > 0) {
			for (String route : criteria.getRoute()) {
				//disregard 0 as it corresponds to "Select All" and was multi-selected in
				if (!route.equals("0") ) {
					indexCounter = indexCounter + 1;
					indices.add(this.populateSearchCriteriaIndexBean(true, indexCounter, route, route.getClass()));					
				}
			}
		} else {
			indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
		}

		if (criteria.getDosage() != null && criteria.getDosage().length > 0) {
			for (String dosage : criteria.getDosage()) {
				//disregard 0 as it corresponds to "Select All" and was multi-selected in
				if (!dosage.equals("0")) {
					indexCounter = indexCounter + 1;
					indices.add(this.populateSearchCriteriaIndexBean(true, indexCounter, dosage, dosage.getClass()));
				}
			}
		} else {
			indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
		}

		if (criteria.getSchedule() != null && criteria.getSchedule().length > 0) {
			for (String schedule : criteria.getSchedule()) {
				//disregard 0 as it corresponds to "Select All" and was multi-selected in
				if (!schedule.equals("0")) {
					indexCounter = indexCounter + 1;
					indices.add(this.populateSearchCriteriaIndexBean(true, indexCounter, schedule, schedule.getClass()));
				}
			}
		} else {
			indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
		}

		if (criteria.getDrugClass() != null && criteria.getDrugClass().length > 0) {
			for (String drugClass : criteria.getDrugClass()) {
				//disregard 0 as it corresponds to "Select All" and was multi-selected in
				if (!drugClass.equals("0")) {
					indexCounter = indexCounter + 1;
					indices.add(this.populateSearchCriteriaIndexBean(true, indexCounter, drugClass, drugClass.getClass()));
				}
			}
		} else {
			indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
		}

		if (criteria.getVetSpecies() != null && criteria.getVetSpecies().length > 0) {
			for (String species : criteria.getVetSpecies()) {
				//disregard 0 as it corresponds to "Select All" and was multi-selected in
				if (!species.equals("0")) {
					indexCounter = indexCounter + 1;
					indices.add(this.populateSearchCriteriaIndexBean(true, indexCounter, species, species.getClass()));
				}
			}
		} else {
			indices.add(this.populateSearchCriteriaIndexBean(false, 0, null, null));
		}

		return indices;
	}
	
	public int getQueryResultsCount(SearchCriteriaBean criteria) throws SQLException {
		StringBuffer query = new StringBuffer("select count(*) from (select distinct drug.drug_code, co.COMPANY_NAME, ste.EXTERNAL_STATUS_ENGLISH, s.schedule, i.ingredient, pm.PM_ENGLISH_FNAME ");
		
		query.append(basicDrugSummaryFromClause());
		if (isSearchingBySpecies(criteria)) { 
			query.append(",WQRY_DRUG_VETERINARY_SPECIES species ");
			query.append(this.speciesDrugSummaryWhereClause(criteria));
		} else { 
			query.append(this.basicDrugSummaryWhereClause());
		    query.append(criteriaDrugSummaryWhereClause(criteria));
		}  
		if (criteria.getBiosimDrugSearch() !=null )
			query.append(extractBiosimdrugCodeCriterion());			
		query.append(")");
		log.debug( "getQueryResultsCount" + query); 
		//System.out.println ( "getQueryResultsCount" + query); 
		Connection con = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultsCount = 0;

		try {
			stmt = con.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			List<SearchCriteriaIndexBean> criteriaIndex = this.getCriteriaIndex(criteria);
			for (SearchCriteriaIndexBean index : criteriaIndex) {
				if (index.getSelected()) {
					log.debug("getQueryResultsCount at index " + index.getIndex() + " criteria = " + index.getCriteria());
					if (index.getCriteriaObjectClass() == String.class) {
						String inputString = (String) index.getCriteria();
						stmt.setString(index.getIndex(), inputString);
					} else if (index.getCriteriaObjectClass() == Long.class) {
						stmt.setLong(index.getIndex(), (Long)index.getCriteria());
					}
				}
			}
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				resultsCount = rs.getInt(1);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			resultsCount = 0;
		} finally {
			try { 
				  if (rs != null) rs.close(); 
			} catch (Exception e) {
				  log.error("Cannot close getQueryResultsCount resultset.", e); 
			};
			try { 
				  if (stmt != null) stmt.close(); 
			} catch (Exception e) { 
				  log.error("Cannot close getQueryResultsCount prepared statement.", e);
			};
			con.close();
		}

		return resultsCount;
	}

	public boolean isAjaxRequest() {
		return isAjaxRequest;
	}

	public void setAjaxRequest(boolean isAjaxRequest) {
		this.isAjaxRequest = isAjaxRequest;
	}

	public AjaxBean getAjaxBean() {
		return ajaxBean;
	}

	public void setAjaxBean(AjaxBean ajaxBean) {
		this.ajaxBean = ajaxBean;
	}

}
