/**
 * Name:  	ProductMonograph
 * Purpose: VO to map to the table WQRY_PM_DRUG
 * Date: 	October 2006
 * Author: 	Diane Beauvais
 */
package ca.gc.hc.siteDemo.models;

import java.io.Serializable;
import java.sql.Date;

import ca.gc.hc.siteDemo.util.LocaleDependantObject;

public class ProductMonograph  extends LocaleDependantObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1031588998802586077L;
	/** identifier field */
	private Long drugCode;
	private int pmNumber;
	private int pmVersionNumber;
	private String pmControlNumber = "";
	private Date pmDate;
	private String pmEnglishFName = "";
	private String pmFrenchFName = "";


	
	/**
	 * @return
	 */
	public Long getDrugCode() {
		return drugCode;
	}



	/**
	 * @return
	 */
	public String getPmEnglishFName() {
		return pmEnglishFName;
	}

	/**
	 * @return
	 */
	public String getPmFrenchFName() {
		return pmFrenchFName;
	}

	/**
	 * @return
	 */
	public int getPmNumber() {
		return pmNumber;
	}

	/**
	 * @return
	 */
	public int getPmVersionNumber() {
		return pmVersionNumber;
	}

	

	/**
	 * @param long1
	 */
	public void setDrugCode(Long long1) {
		drugCode = long1;
	}

	/**
	 * @param string
	 */
	public void setPmEnglishFName(String string) {
		pmEnglishFName = string;
	}

	/**
	 * @param string
	 */
	public void setPmFrenchFName(String string) {
		pmFrenchFName = string;
	}
	
	/***************************************************************************
	 * Gets the name of the product monograph in the language appropriate for the Locale.
	 * @return the locale-specific name of the product monograph.
	 */
	public String getPmName() {
		if (isLanguageFrench()) {
			if ((getPmFrenchFName() != null) && (!getPmFrenchFName().isEmpty())){
				return getPmFrenchFName() + ".PDF";
			} else return "";
		}
		if ((getPmEnglishFName() != null) && (!getPmEnglishFName().isEmpty())){	
			return getPmEnglishFName() + ".PDF";
		} else return "";
	}


	/**
	 * @param i
	 */
	public void setPmNumber(int i) {
		pmNumber = i;
	}

	/**
	 * @param i
	 */
	public void setPmVersionNumber(int i) {
		pmVersionNumber = i;
	}

	/**
	 * @return
	 */
	public String getPmControlNumber() {
		return pmControlNumber;
	}

	/**
	 * @return
	 */
	public Date getPmDate() {
		return pmDate;
	}

	/**
	 * @param string
	 */
	public void setPmControlNumber(String string) {
		pmControlNumber = string;
	}

	/**
	 * @param date
	 */
	public void setPmDate(Date date) {
		pmDate = date;
	}

}


