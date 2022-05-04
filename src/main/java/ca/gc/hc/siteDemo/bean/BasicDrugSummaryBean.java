/*
 * Created on Apr 9, 2004
 *
 */
package ca.gc.hc.siteDemo.bean;
import java.io.Serializable;
 

/**
 * 
 * DPD Online Query Basic Drug Summary Bean.
 *
 */
public class BasicDrugSummaryBean extends BaseBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4597184032508983826L;
	
	private Long drugCode;
	private String brandName;
	private String brandNameF;
	private String durgIdentificationNumber;	
	private Long companyCode;
	private String drugClass;
	private String drugClassF;
	private Long numberOfAis;
	private String aiGroupNo;
	private String companyName;
	private Long externalStatusCode;
	private String externalStatusE;
	private String externalStatusF;
	private String scheduleE;
	private String scheduleF;
	private String vetSpeciesE;
	private String vetSpeciesF;
	private Long classCode;
	private String ingredientE;
	private String ingredientF;
	private String strength;
	private String strengthUnitE;
	private String strengthUnitF;
	private String dosageValue;
	private String dosageUnitE;
	private String dosageUnitF;
	private String pmEnglishFName;
	private String pmFrenchFName;
	
	public Long getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(Long drugCode) {
		this.drugCode = drugCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandNameF() {
		return brandNameF;
	}
	public void setBrandNameF(String brandNameF) {
		this.brandNameF = brandNameF;
	}
	public String getDurgIdentificationNumber() {
		return durgIdentificationNumber;
	}
	public void setDurgIdentificationNumber(String durgIdentificationNumber) {
		this.durgIdentificationNumber = durgIdentificationNumber;
	}
	public Long getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(Long companyCode) {
		this.companyCode = companyCode;
	}
	public String getDrugClass() {
		return drugClass;
	}
	public void setDrugClass(String drugClass) {
		this.drugClass = drugClass;
	}
	public String getDrugClassF() {
		return drugClassF;
	}
	public void setDrugClassF(String drugClassF) {
		this.drugClassF = drugClassF;
	}
	public Long getNumberOfAis() {
		return numberOfAis;
	}
	public void setNumberOfAis(Long numberOfAis) {
		this.numberOfAis = numberOfAis;
	}
	public String getAiGroupNo() {
		return aiGroupNo;
	}
	public void setAiGroupNo(String aiGroupNo) {
		this.aiGroupNo = aiGroupNo;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Long getExternalStatusCode() {
		return externalStatusCode;
	}
	public void setExternalStatusCode(Long externalStatusCode) {
		this.externalStatusCode = externalStatusCode;
	}
	public String getExternalStatusE() {
		return externalStatusE;
	}
	public void setExternalStatusE(String externalStatusE) {
		this.externalStatusE = externalStatusE;
	}
	public String getExternalStatusF() {
		return externalStatusF;
	}
	public void setExternalStatusF(String externalStatusF) {
		this.externalStatusF = externalStatusF;
	}
	public String getScheduleE() {
		return scheduleE;
	}
	public void setScheduleE(String scheduleE) {
		this.scheduleE = scheduleE;
	}
	public String getScheduleF() {
		return scheduleF;
	}
	public void setScheduleF(String scheduleF) {
		this.scheduleF = scheduleF;
	}
	public Long getClassCode() {
		return classCode;
	}
	public void setClassCode(Long classCode) {
		this.classCode = classCode;
	}
	public String getIngredientE() {
		return ingredientE;
	}
	public void setIngredientE(String ingredientE) {
		this.ingredientE = ingredientE;
	}
	public String getIngredientF() {
		return ingredientF;
	}
	public void setIngredientF(String ingredientF) {
		this.ingredientF = ingredientF;
	}
	public String getStrength() {
		return strength;
	}
	public void setStrength(String strength) {
		this.strength = strength;
	}
	public String getStrengthUnitE() {
		return strengthUnitE;
	}
	public void setStrengthUnitE(String strengthUnitE) {
		this.strengthUnitE = strengthUnitE;
	}
	public String getStrengthUnitF() {
		return strengthUnitF;
	}
	public void setStrengthUnitF(String strengthUnitF) {
		this.strengthUnitF = strengthUnitF;
	}
	public String getDosageValue() {
		return dosageValue;
	}
	public void setDosageValue(String dosageValue) {
		this.dosageValue = dosageValue;
	}
	public String getDosageUnitE() {
		return dosageUnitE;
	}
	public void setDosageUnitE(String dosageUnitE) {
		this.dosageUnitE = dosageUnitE;
	}
	public String getDosageUnitF() {
		return dosageUnitF;
	}
	public void setDosageUnitF(String dosageUnitF) {
		this.dosageUnitF = dosageUnitF;
	}
	public String getPmEnglishFName() {
		return pmEnglishFName;
	}
	public void setPmEnglishFName(String pmEnglishFName) {
		this.pmEnglishFName = pmEnglishFName;
	}
	public String getPmFrenchFName() {
		return pmFrenchFName;
	}
	public void setPmFrenchFName(String pmFrenchFName) {
		this.pmFrenchFName = pmFrenchFName;
	}
	public String getVetSpeciesE() {
		return vetSpeciesE;
	}
	public void setVetSpeciesE(String vetSpeciesE) {
		this.vetSpeciesE = vetSpeciesE;
	}
	public String getVetSpeciesF() {
		return vetSpeciesF;
	}
	public void setVetSpeciesF(String vetSpeciesF) {
		this.vetSpeciesF = vetSpeciesF;
	}

	
}
