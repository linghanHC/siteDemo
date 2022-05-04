package ca.gc.hc.siteDemo.models;

import java.io.Serializable;
import java.util.Date;

import ca.gc.hc.siteDemo.util.LocaleDependantObject;

public class SpecialIdentifier extends LocaleDependantObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 317002075499442663L;

	/** identifier field */
	private Long id;

	/** persistent field */
	private Long drugCode;

	/** persistent field */
	private Long siCode;

	/** persistent field */
	private String descE;
	private String descF;

	private Date dateAssigned;

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

	public Long getSiCode() {
		return siCode;
	}

	public void setSiCode(Long siCode) {
		this.siCode = siCode;
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

	public Date getDateAssigned() {
		return dateAssigned;
	}

	public void setDateAssigned(Date dateAssigned) {
		this.dateAssigned = dateAssigned;
	}

	
}