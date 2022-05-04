package ca.gc.hc.siteDemo.models;

import java.io.Serializable;
import java.util.Date;

import ca.gc.hc.siteDemo.util.LocaleDependantObject;

public class RiskMinMeasure extends LocaleDependantObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1824632981722250101L;

	/** identifier field */
	private Long id;

	/** persistent field */
	private Long drugCode;

	/** persistent field */
	private Long rmmCode;

	/** persistent field */
	private String descE;
	private String descF;

	private Date endDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(Long drugCode) {
		this.drugCode = drugCode;
	}

	public Long getRmmCode() {
		return rmmCode;
	}

	public void setRmmCode(Long rmmCode) {
		this.rmmCode = rmmCode;
	}

	public String getDescE() {
		return descE;
	}

	public void setDescE(String descE) {
		this.descE = descE;
	}

	public String getDescF() {
		return descF;
	}

	public void setDescF(String descF) {
		this.descF = descF;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getrmmDescription() {
		if (isLanguageFrench()) {
			return this.descF;
		} 
		return this.descE;
	}
}
