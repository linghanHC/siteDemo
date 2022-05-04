package ca.gc.hc.siteDemo.models;
import java.io.Serializable;

import ca.gc.hc.siteDemo.util.ApplicationGlobals;
import ca.gc.hc.siteDemo.util.LocaleDependantObject;
import ca.gc.hc.siteDemo.util.StringsUtil;

/** @author Hibernate CodeGenerator */
public class DrugProduct extends LocaleDependantObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5055174528754278343L;

	/** identifier field */
	private Long drugCode;

	/** persistent field */
	private String brandNameE;
	private String brandNameF;

	/** persistent field */
	private String drugIdentificationNumber;

	/** persistent field */
	private Long companyCode;

	/** nullable persistent field */
	private String drugClassE;
	private String drugClassF;
	private Long classCode;

	/** persistent field */
	private Long numberOfAis;

	/** nullable persistent field */
	private String aiGroupNo;
	
	private String biosimDrug;
	
	private String descriptorE;
	private String descriptorF;

	private String riskManPlan;
	
	private boolean isScheduleCDinIssued;
	
	private String opioidManPlan;
	
	/** full constructor */
	public DrugProduct(
			String brandNameE,
			String brandNameF,
			String drugIdentificationNumber,
			Long companyCode,
			String drugClassE,
			String drugClassF,
			Long numberOfAis,
			String aiGroupNo, 
			String species, 
			String subSpecies, 
			String descriptorE, 
			String descriptorF,
			String biosimDrug) {
		this.brandNameE = brandNameE;
		this.brandNameF = brandNameF;
		this.drugIdentificationNumber = drugIdentificationNumber;
		this.companyCode = companyCode;
		this.drugClassE = drugClassE;
		this.drugClassF = drugClassF;
		this.numberOfAis = numberOfAis;
		this.aiGroupNo = aiGroupNo;
		this.descriptorE = descriptorE;
		this.descriptorF = descriptorF;
		this.biosimDrug = biosimDrug;
	}

	/** default constructor */
	public DrugProduct() {
	}

	/** minimal constructor */
	public DrugProduct(
			String brandNameE, String brandNameF,
			String drugIdentificationNumber,
			Long companyCode,
			Long numberOfAis) {
		this.brandNameE = brandNameE;
		this.brandNameF = brandNameF;
		this.drugIdentificationNumber = drugIdentificationNumber;
		this.companyCode = companyCode;
		this.numberOfAis = numberOfAis;
	}

	public Long getDrugCode() {
		return this.drugCode;
	}

	public void setDrugCode(Long drugCode) {
		this.drugCode = drugCode;
	}

	public String getBrandName() {
		return isLanguageFrench() ? 
				StringsUtil.substituteIfNull(brandNameF, brandNameE)
			   :StringsUtil.substituteIfNull(brandNameE, brandNameF);
	}
	
	public String getBrandNameE() {
		return brandNameE;
	}
	
	public String getBrandNameF() {
		return brandNameF;
	}

	public void setBrandNameE(String brandName) {
		this.brandNameE = brandName;
	}
	
	public void setBrandNameF(String brandName) {
		this.brandNameF = brandName;
	}

	public String getDrugIdentificationNumber() {
		// Implementation for June 13, 2018
		// 2020: Class check is no longer needed, because Biosimilar drug is not Radiopharmaceutical drug
		//if (classCode.equals(ApplicationGlobals.RADIOPHARMACEUTICAL_CLASS_CODE))
		
		if (!isScheduleCDinIssued) {
			return super.isLanguageFrench() ? ApplicationGlobals.NOT_APPLICABLE_F : ApplicationGlobals.NOT_APPLICABLE_E;
		}		
	    return this.drugIdentificationNumber;
	}

	public void setDrugIdentificationNumber(
			String drugIdentificationNumber) {
		this.drugIdentificationNumber = drugIdentificationNumber;
	}

	public Long getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(Long companyCode) {
		this.companyCode = companyCode;
	}

	public String getDrugClass() {
		return isLanguageFrench() ? StringsUtil.substituteIfNull(drugClassF, drugClassE) : drugClassE;
	}

	public Long getNumberOfAis() {
		return this.numberOfAis;
	}

	public void setNumberOfAis(Long numberOfAis) {
		this.numberOfAis = numberOfAis;
	}

	public String getAiGroupNo() {
		return this.aiGroupNo;
	}

	public void setAiGroupNo(String aiGroupNo) {
		this.aiGroupNo = aiGroupNo;
	}

	public String getDescriptor() {
		return isLanguageFrench() ? StringsUtil.substituteIfNull(descriptorF, descriptorE) : descriptorE;
	}

	public void setDescriptorE(String descriptor) {
		this.descriptorE = descriptor;
	}
	public void setDescriptorF(String descriptor) {
		this.descriptorF = descriptor;
	}

	public String getDescriptorE() {
		return descriptorE;
	}

	public String getDescriptorF() {
		return descriptorF;
	}

	public String getDrugClassE() {
		return drugClassE;
	}

	public void setDrugClassE(String drugClassE) {
		this.drugClassE = drugClassE;
	}

	public String getDrugClassF() {
		return drugClassF;
	}

	public void setDrugClassF(String drugClassF) {
		this.drugClassF = drugClassF;
	}

	public void setClassCode(Long classCode) {
		this.classCode = classCode;
	}

	public Long getClassCode() {
		return classCode;
	}

	public String getRiskManPlan() {
		return riskManPlan;
	}

	public void setRiskManPlan(String riskManPlan) {
		if (riskManPlan == null) {
			// This is to make sure the risk management plan is never null
			this.riskManPlan = "N";
		} else {
			this.riskManPlan = riskManPlan;			
		}
	}

	public boolean isScheduleCDinIssued() {
		return isScheduleCDinIssued;
	}

	public void setScheduleCDinIssued(boolean isScheduleCDinIssued) {
		this.isScheduleCDinIssued = isScheduleCDinIssued;
	}

	public String getOpioidManPlan() {
		return opioidManPlan;
	}

	public void setOpioidManPlan(String opioidManPlan) {
		this.opioidManPlan = opioidManPlan;
	}	

	public String getBiosimDrug() {
		return biosimDrug;
	}
	
	public void setBiosimDrug(String biosimDrug) {
		
		this.biosimDrug = biosimDrug;
	}	
	
	
}
