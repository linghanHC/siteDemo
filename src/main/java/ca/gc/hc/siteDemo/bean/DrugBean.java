/*
 * Created on Apr 9, 2004
 */
package ca.gc.hc.siteDemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.gc.hc.siteDemo.models.AHFS;
import ca.gc.hc.siteDemo.models.ATC;
import ca.gc.hc.siteDemo.models.ActiveIngredients;
import ca.gc.hc.siteDemo.models.Company;
import ca.gc.hc.siteDemo.models.DrugProduct;
import ca.gc.hc.siteDemo.models.DrugStatus;
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
import ca.gc.hc.siteDemo.util.StringsUtil;


/**
 * Drug Product Detail Info Bean
 */
public class DrugBean extends BaseBean implements Serializable
{

	private static final long serialVersionUID = -6660975200062933666L;

	private String companyId;

	private DrugProduct drugProduct = new DrugProduct();

	private List<ActiveIngredients> activeIngredientList = new ArrayList<ActiveIngredients>();

	private List<AHFS> ahfsList = new ArrayList<AHFS>();

	private ATC atcVO = new ATC();

	private Company companyVO = new Company();

	private ProductMonograph pmVO = new ProductMonograph();

	private List<Form> formList = new ArrayList<Form>();
	
	private List<Packaging> packagingList = new ArrayList<Packaging>();

	private List<Route> routeList = new ArrayList<Route>();

	private List<Schedule> scheduleList = new ArrayList<Schedule>();

	private DrugStatus statusVO = new DrugStatus();

	private String statusAbbreviation;

	private List<Veterinary> vetSpecies = new ArrayList<Veterinary>();
	
	private List<RiskMinMeasure> riskMinMeasures = new ArrayList<RiskMinMeasure>();
	
	private List<PharmMonitorAct> pharmMonitorActs = new ArrayList<PharmMonitorAct>();
	
	private List<SpecialIdentifier> specialIdentifiers = new ArrayList<SpecialIdentifier>();
	
	private String biosimDrug;

	private String statusLangOfPart;

	private boolean isRadioPharmaceutical;

	private boolean isApproved;
		
	public List<ActiveIngredients> getActiveIngredientList()
	{
		return activeIngredientList;
	}

	public String getRmmDescription(int index) {
		RiskMinMeasure rmm = (RiskMinMeasure) riskMinMeasures.get(index);
		if (rmm != null) {
			return super.getLanguageOfPart(rmm.getDescE(), rmm.getDescF());
		}else{
			return new String("");
		}
	}

	public String getMonitorActDescription(int index) {
		PharmMonitorAct phaMonitorAct = (PharmMonitorAct) pharmMonitorActs.get(index);
		if (phaMonitorAct != null) {
			return super.getLanguageOfPart(phaMonitorAct.getDescE(), phaMonitorAct.getDescF());
		}else{
			return new String("");
		}
	}

	public String getIngredientLangOfPart(int index) {
		ActiveIngredients ingred = (ActiveIngredients) activeIngredientList.get(index); 
		
		if(ingred != null) {
			return super.getLanguageOfPart(ingred.getIngredientE(), ingred.getIngredientF());
		}else{
			return new String("");
		}
	 }

	/**
	 * @param index An int corresponding to an index in the current List of ActiveIngredients 
	 * @return Either an empty string if the related bilingual properties actually exist in both official
	 * languages, or the ISO language code to use in a lang attribute where the property in the requested
	 * language is missing, and its equivalent in the other official language is being returned instead. <br/>
	 * <strong>Note:</strong> Normally, both languages are expected to be present. If either is missing, 
	 * the caller is responsible for getting the individual lang attribute(s) if required using
	 * getAiStrengthLangOfPart and getAiDosageLangOfPart.
//	 * @see getAiStrengthLangOfPart
//	 * @see getAiDosageLangOfPart
	 * @author Sylvain Larivière 2012-09-19
	 */
	public String getAiStrengthAndDosageLangOfPart(int index) {
		ActiveIngredients ingred = (ActiveIngredients) activeIngredientList.get(index);

		if(ingred != null) {
			String strengthUnitLangOfPart = super.getLanguageOfPart(ingred.getStrengthUnitE(), ingred.getStrengthUnitF());
			String dosageUnitLangOfPart = super.getLanguageOfPart(ingred.getDosageUnitE(), ingred.getDosageUnitF());
			return strengthUnitLangOfPart + dosageUnitLangOfPart;
		}else{
			return new String("");
		}
	}
	
	public String getAiStrengthLangOfPart(int index) {
		ActiveIngredients ingred = (ActiveIngredients) activeIngredientList.get(index);
		
		if(ingred != null) {
			return super.getLanguageOfPart(ingred.getStrengthUnitE(), ingred.getStrengthUnitF());
		}else{
			return new String("");
		}		
	}
	
	public String getAiDosageLangOfPart(int index) {
		ActiveIngredients ingred = (ActiveIngredients) activeIngredientList.get(index);
		
		if(ingred != null) {
			return super.getLanguageOfPart(ingred.getDosageUnitE(), ingred.getDosageUnitF());
		}else{
			return new String("");
		}		
	}

	public List<AHFS> getAhfsList()
	{
		return ahfsList;
	}

	public ATC getAtcVO()
	{
		return atcVO;
	}

	public Company getCompanyVO()
	{
		return companyVO;
	}

	public DrugProduct getDrugProduct()
	{
		return drugProduct;
	}

	public List<Form> getFormList()
	{
		return formList;
	}

	public List<Packaging> getPackagingList()
	{
		return packagingList;
	}

	public List<Route> getRouteList()
	{
		return routeList;
	}

	public List<Schedule> getScheduleList()
	{
		return scheduleList;
	}

	public DrugStatus getStatusVO()
	{
		return statusVO;
	}
	
	public String getBiosimDrug()
	{
		return biosimDrug;
	}
	
	/**
	 * @param list
	 */
	public void setActiveIngredientList(List<ActiveIngredients> list)
	{
		activeIngredientList = list;
	}

	/**
	 * @param list
	 */
	public void setAhfsList(List<AHFS> list)
	{
		ahfsList = list;
	}

	/**
	 * @param atc
	 */
	public void setAtcVO(ATC atc)
	{
		atcVO = atc;
	}

	/**
	 * @param company
	 */
	public void setCompanyVO(Company company)
	{
		companyVO = company;
	}

	/**
	 * @param product
	 */
	public void setDrugProduct(DrugProduct product)
	{
		//companyId = sc.encrypt(product.getCompanyCode().toString());
		companyId = product.getCompanyCode().toString();
		drugProduct = product;
	}

	/**
	 * @param list
	 */
	public void setFormList(List<Form> list)
	{
		formList = list;
	}

	/**
	 * @param list
	 */
	public void setPackagingList(List<Packaging> list)
	{
		packagingList = list;
	}

	/**
	 * @param list
	 */
	public void setRouteList(List<Route> list)
	{
		routeList = list;
	}

	/**
	 * @param list
	 */
	public void setScheduleList(List<Schedule> list)
	{
		scheduleList = list;
	}

	/**
	 * @param status
	 */
	public void setStatusVO(DrugStatus status)
	{
		statusVO = status;
	}

	public String getCompanyId()
	{
		return companyId;
	}

	/**
	 * @return Well formated object content
	 */	
	public String toString()
	{
		return new ToStringBuilder(this)
				.append("Drug", getDrugProduct())
				.append("Active Ingredients", getActiveIngredientList().toArray())
				.append("AHFS", getAhfsList().toArray())
				.append("ATC", getAtcVO())
				.append("Company", getCompanyVO())
				.append("Dosage Forms", getFormList().toArray())
				.append("Packaging", getPackagingList().toArray())
				.append("Route of Admin", getRouteList().toArray())
				.append("Schedule", getScheduleList().toArray())
				.append("Status", getStatusVO().toString())
				.append("biosimDrug", getBiosimDrug())
				.toString();
	}

	public String getStatusAbbreviation() {
		return statusAbbreviation;
	}

	/**
	 * @param string
	 */
	public void setStatusAbbreviation(String string) {
		statusAbbreviation = string;
	}

	public ProductMonograph getPmVO() {
		return pmVO;
	}

	/**
	 * @param monograph
	 */
	public void setPmVO(ProductMonograph monograph) {
		pmVO = monograph;
	}

	public List<Veterinary> getVetSpecies() {
		return vetSpecies;
	}

	public void setVetSpecies(List<Veterinary> vetSpecies) {
		this.vetSpecies = vetSpecies;
	}

	/**
	 * @return A distinct series of veterinarian species separated by a space, as a String.
	 * Was used when sub-species existed in the database but were not displayed, to avoid duplication in the series
	 * Cannot be used if each value needs to be evaluated individually (and possibly tagges) for language of part (WGAC2)
	 * @author  Sylvain Larivière 2009-12-01
	 */
	public String getDistinctVetSpecies() {
		String speciesList = "";
		String species = "";
		
		for (Iterator it = vetSpecies.iterator(); it.hasNext();) {
			Veterinary vetSpecies;
			vetSpecies = (Veterinary)it.next();
			species = vetSpecies.getVetSpecies();
			if (speciesList.indexOf(species) == -1) {
				speciesList += ", " + species;
			}
		}
		if (speciesList.length()>0) {
			speciesList= speciesList.substring(2); //remove initial comma
		}
		return speciesList;
	}
	
	public String getDistinctRoutes() {
		String routes = "";
		
		for (Route route : routeList) {
			if (routes.indexOf(route.getRouteOfAdministration()) == -1) { //do not add repeated values
				routes += ", " + route.getRouteOfAdministration();
			}
		}		
		return routes.length() > 0 ? routes.substring(2) : routes;
	}
	
	public String getBrandNameLangOfPart() {
		return super.getLanguageOfPart(drugProduct.getBrandNameE(), drugProduct.getBrandNameF());
	}
	
	public String getDescriptorLangOfPart() {
		return super.getLanguageOfPart(drugProduct.getDescriptorE(), drugProduct.getDescriptorF());
	}
	
	public String getStatusLangOfPart() {
		statusLangOfPart = super.getLanguageOfPart(statusVO.getStatusE(), statusVO.getStatusF());
		return statusLangOfPart;
	}
	
	public String getStreetNameLangOfPart() {
		return super.getLanguageOfPart(companyVO.getStreetNameE(), companyVO.getStreetNameF());
	}

	public String getProvinceLangOfPart() {
		return super.getLanguageOfPart(companyVO.getProvinceE(), companyVO.getProvinceF());
	}

	public String getCountryLangOfPart() {
		return super.getLanguageOfPart(companyVO.getCountryE(), companyVO.getCountryF());
	}
	
	public String getDrugClassLangOfPart() {
		return super.getLanguageOfPart(drugProduct.getDrugClassE(), drugProduct.getDrugClassF());
	}
	
	public String getVetSpeciesLangOfPart(int index) {
		Veterinary species = (Veterinary) vetSpecies.get(index);
		
		if(species != null) {
			return super.getLanguageOfPart(species.getVetSpeciesE(), species.getVetSpeciesF());
		}else{
			return new String("");
		}
	}
	
	public int getVetSpeciesCount() {
		if(vetSpecies != null) 
			  return vetSpecies.size();		
		else
			  return 0;			
	}
	
	public String getFormLangOfPart(int index) {
		Form form = (Form) formList.get(index);
		
		if (form != null) {
			return super.getLanguageOfPart(form.getPharmaceuticalFormE(), form.getPharmaceuticalFormF());
		}else{
			return new String("");
		}
	}
	
	public String getRouteLangOfPart(int index) {
		Route route = (Route) routeList.get(index);
		
		if( route != null) {
			return super.getLanguageOfPart(route.getRouteOfAdministrationE(), route.getRouteOfAdministrationF());
		}else{
			return new String("");
		}
	}
	
	public String getAhfsLangOfPart(int index) {
		AHFS ahfs = (AHFS) ahfsList.get(index);
		
		if(ahfs != null){
			return super.getLanguageOfPart(ahfs.getAhfsE(), ahfs.getAhfsF());
		}else{
			return new String("");
		}		
	}
	
	public String getScheduleLangOfPart(int index) {
		Schedule schedule = (Schedule) scheduleList.get(index);
		
		if(schedule != null){
			return super.getLanguageOfPart(schedule.getScheduleE(), schedule.getScheduleF());
		}else{
			return new String("");
		}
	}
	
	public String getAtcLangOfPart() {
		ATC atc = (ATC) atcVO;
		
		if(atc != null) {
			return super.getLanguageOfPart(atc.getAtcE(), atc.getAtcF());
		}else{
			return new String("");
		}
	}
	
	public int getPharmaceuticalFormCount() {
		return formList.size();
	}
	
	public int getRouteSpeciesCount() {
		return routeList.size();
	}
	
	public int getScheduleSpeciesCount() {
		return scheduleList.size();
	}
	
	public int getAhfsSpeciesCount() {
		return ahfsList.size();
	}
	
	public boolean getIsRadioPharmaceutical() {
		isRadioPharmaceutical = this.drugProduct.getClassCode().equals(ApplicationGlobals.RADIOPHARMACEUTICAL_CLASS_CODE);
		return isRadioPharmaceutical;
	}
	
	public boolean getIsApproved() {
		isApproved = this.statusVO.getExternalStatus().getExternalStatusId().equals(ApplicationGlobals.APPROVED_STATUS_CODE);
		return isApproved;
	}
	
	public boolean getDisplayFootnoteTwo(){
		if ((statusVO.getLotNumber() != null) || (statusVO.getExpirationDate() != null)) {
			return true;
		} else return false;
	}

	public boolean getDisplayFootnoteSeven(){
		if (getRiskManagementPlan() || getOpioidManagementPlan()) {
			return true;
		} else return false;
	}
	
	public boolean getRiskManagementPlan(){
		if (StringsUtil.emptyForNull(this.drugProduct.getRiskManPlan()).equalsIgnoreCase("Y")) {
			return true;
		} else return false;
	}
	
	public boolean getOpioidManagementPlan(){
		if (StringsUtil.emptyForNull(this.drugProduct.getOpioidManPlan()).equalsIgnoreCase("Y")) {
			return true;
		} else return false;
	}
	
	public Object getOriginalMarketDate() {
		String notApplicable = 
		isLanguageFrench() ? ApplicationGlobals.NOT_APPLICABLE_F : ApplicationGlobals.NOT_APPLICABLE_E;
		
		return getIsRadioPharmaceutical() ? notApplicable : statusVO.getOriginalMarketDate();
	}

	public List<RiskMinMeasure> getRiskMinMeasures() {
		return riskMinMeasures;
	}

	public void setRiskMinMeasures(List<RiskMinMeasure> riskMinMeasures) {
		this.riskMinMeasures = riskMinMeasures;
	}

	public List<PharmMonitorAct> getPharmMonitorActs() {
		return pharmMonitorActs;
	}

	public void setPharmMonitorActs(List<PharmMonitorAct> pharmMonitorActs) {
		this.pharmMonitorActs = pharmMonitorActs;
	}

	public List<SpecialIdentifier> getSpecialIdentifiers() {
		return specialIdentifiers;
	}

	public void setSpecialIdentifiers(List<SpecialIdentifier> specialIdentifiers) {
		this.specialIdentifiers = specialIdentifiers;
	}
	
	public boolean isScheduleCDinIssued(List<SpecialIdentifier> specialIdentifiers) {
		for (SpecialIdentifier si : specialIdentifiers) {
			if (si.getSiCode().equals(ApplicationGlobals.SCHDEULE_C_DIN_NOT_ISSUED)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isScheduleD(List<Schedule> schedules) {
		for (Schedule sc : schedules) {
		    if (sc.getScheduleCode()==ApplicationGlobals.BIOSIMILAR_SCHDEULE_D_CODE) 
				return true;
		}
		return false;
	}
	
	public boolean isBiosimilarSpecialIdentifier(List<SpecialIdentifier> specialIdentifiers) {
		for (SpecialIdentifier si : specialIdentifiers) {
			if (si.getSiCode().equals(ApplicationGlobals.BIOSIMILAR_SPECIAL_IDENTIFIER_CODE)) 
				return true;
		}
		return false;
	}	
	
	public String CheckIfIsBiosimDrug(List<SpecialIdentifier> specialIdentifiers ) {
		if (isBiosimilarSpecialIdentifier(specialIdentifiers)) {
			return (this.biosimDrug=localisedProperty("Yes","Oui"));	
		}
		else
			return (this.biosimDrug="");	
	}
	
    public String setBiosimDrug(String biosimDrug) {
		return this.biosimDrug=biosimDrug;
	}
	
}
