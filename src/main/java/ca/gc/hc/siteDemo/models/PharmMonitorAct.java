package ca.gc.hc.siteDemo.models;

import java.io.Serializable;
import java.util.Date;

import ca.gc.hc.siteDemo.util.LocaleDependantObject;

public class PharmMonitorAct extends LocaleDependantObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7382914165143801259L;

	/** identifier field */
	private Long id;

	/** persistent field */
	private Long drugCode;

	/** persistent field */
	private Long phaMonCode;

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

	public Long getPhaMonCode() {
		return phaMonCode;
	}

	public void setPhaMonCode(Long phaMonCode) {
		this.phaMonCode = phaMonCode;
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

	public String getMonitorActDescription() {
		if (isLanguageFrench()) {
			return this.descF;
		} 
		return this.descE;
	}
	  
}