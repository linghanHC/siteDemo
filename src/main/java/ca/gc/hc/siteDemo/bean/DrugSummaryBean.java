/*
 * Created on Apr 18, 2004
 *
 */
package ca.gc.hc.siteDemo.bean;

import java.io.Serializable;

import ca.gc.hc.siteDemo.models.ActiveIngredients;
import ca.gc.hc.siteDemo.models.DrugProduct;
import ca.gc.hc.siteDemo.models.DrugStatus;
import ca.gc.hc.siteDemo.models.DrugVeterinarySpecies;
import ca.gc.hc.siteDemo.models.Form;
import ca.gc.hc.siteDemo.models.Route;
import ca.gc.hc.siteDemo.models.Schedule;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Java Bean class to hold the Drug Product summary information.
 * Updated SL/2009-10-01 to include first AI, route, dosage, and schedule
 */
public class DrugSummaryBean extends BaseBean implements Serializable
{
 
	private static final long serialVersionUID = 2293033709403798575L;
	/*
	 * The String Cryptor instance
	 */
	private String id;

	private DrugProduct drug;
	private String companyName;
	private DrugStatus status;
	private String pmE;
	private String pmF;
	private ActiveIngredients firstAI;	//SL/2009-10-01: the first active ingredient used (ordered by id)
	private String AiStrengthAndDosageText;
	private Route route;
	private Form  dosage;
	private Schedule schedule;
	private DrugVeterinarySpecies vetSpecies;
	private boolean isScheduleCDinIssued;

  public DrugSummaryBean(DrugProduct vo, String company, DrugStatus status, 
		  String pmE, String pmF)
  {
	this.id = vo.getDrugCode().toString();
    this.drug = vo;
    this.companyName = company;
    this.status = status;
    this.pmE = pmE;
    this.pmF = pmF;    
  }
 
  /** default constructor */
  public DrugSummaryBean()
  {
  }
  
  /*
   * Partial constructor excluding route and dosage form. Drug products are queried without these two fields
   * in order to avoid duplicates in the results summary. 
   */
  public DrugSummaryBean(DrugProduct vo, String company, DrugStatus status, String pmE, String pmF, 
			Schedule schedule, ActiveIngredients firstAI, DrugVeterinarySpecies vetSpecies,
			boolean isScheduleCDinIssued )
	{
		this.id = vo.getDrugCode().toString();
		this.drug= vo;
		this.companyName = company;
		this.status = status;
		this.pmE = pmE;
		this.pmF = pmF;
		this.schedule = schedule; 
		this.vetSpecies = vetSpecies;
		this.firstAI= firstAI;
		this.isScheduleCDinIssued = isScheduleCDinIssued;
	}

	/**
	 * @param string
	 */
	public void setCompanyName(String string)
	{
		companyName = string;
	}

	/**
	 * @param string
	 */
	public void setDin(String string)
	{
		this.drug.setDrugIdentificationNumber(string);
	}

	/**
	 * @param long1
	 */
	public void setNumberOfAis(Long long1)
	{
		this.drug.setNumberOfAis(long1);
	}

	/**
	 * @return
	 */
	public String getId()
	{
		return id;
	}
	
	public Long getStatusID() {
		return status.getStatusID();
	}

	public DrugStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(DrugStatus status)
	{
		this.status = status;
	}

	/**
	 * @return Well formated object content
	 */
	public String toString()
	{ // todo
		return new ToStringBuilder(this)
//		.append("drugCode", getDrugCode())
//		.append("din", getDin())
		.append("companyName", getCompanyName())
//		.append("brandName", getBrandName())
//		.append("numberOfAis", getNumberOfAis())
//		.append("drugClass", getDrugClass())
		.append("status", getStatus())
		.append("route", getRoute())
		.append("form",getDosage())
//		.append("schedule", getSchedule())
		.append("vetSpecies", getVetSpecies())
		.toString();

	}

	public boolean isScheduleCDinIssued() {
		return isScheduleCDinIssued;
	}

	/**
	 * @return
	 */
	public String getPmE() {
		return pmE;
	}

	/**
	 * @return
	 */
	public String getPmF() {
		return pmF;
	}

	/**
	 * @param string
	 */
	public void setPmE(String string) {
		pmE = string;
	}

	/**
	 * @param string
	 */
	public void setPmF(String string) {
		pmF = string;
	}
	public String getDosage() {
		return dosage.getPharmaceuticalForm();
	}
	public void setDosage(Form dosage) {
		this.dosage = dosage;
	}
	
	public ActiveIngredients getfirstAI() {
		return this.firstAI;
	}
	
	public void setfirstAI(ActiveIngredients firstAI) {
		this.firstAI = firstAI;
	}
	public String getRoute() {
		return route.getRouteOfAdministration();
	}
	public void setRoute(Route route) {
		this.route = route;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public DrugProduct getDrug() {
		return drug;
	}

	public void setDrug(DrugProduct drug) {
		this.drug = drug;
	}

	public String getCompanyName() {
		return companyName;
	}

	public DrugVeterinarySpecies getVetSpecies() {
		return vetSpecies;
	}

	public void setVetSpecies(DrugVeterinarySpecies vetSpecies) {
		this.vetSpecies = vetSpecies;
	}

	public String getStrength() {
		return firstAI.getStrength();
	}
	
}
