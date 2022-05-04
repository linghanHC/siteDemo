package ca.gc.hc.siteDemo.models;


import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.gc.hc.siteDemo.util.LocaleDependantObject;
import ca.gc.hc.siteDemo.util.StringsUtil;

/** @author Hibernate CodeGenerator */
public class Veterinary extends LocaleDependantObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1130173739981690229L;

	private Long id;

	/** identifier field */
	private Long drugCode;

	/** nullable persistent field */
	private String vetSpeciesE;
	private String vetSpeciesF;

	/** full constructor */
	public Veterinary(String tcVetSpeciesE, String tcVetSpeciesF, String tcVetSubSpeciesE, String tcVetSubSpeciesF)
	{
		this.vetSpeciesE = tcVetSpeciesE;
		this.vetSpeciesF = tcVetSpeciesF;
	}

	/** default constructor */
	public Veterinary()
	{
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("drugCode", getDrugCode())
		.append("vetSpeciesE", getVetSpeciesE())
		.append("vetSpeciesF", getVetSpeciesF())
		.toString();
	}

	public Long getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(Long drugCode) {
		this.drugCode = drugCode;
	}

	public String getVetSpecies() {
		return isLanguageFrench() ? StringsUtil.substituteIfNull(vetSpeciesF, vetSpeciesE) : vetSpeciesE;
	}

	public boolean equals(Object other)
	{
		if (!(other instanceof Veterinary))
			return false;
		Veterinary castOther = (Veterinary) other;
		return new EqualsBuilder()
		.append(this.getDrugCode(), castOther.getDrugCode())
		.append(this.getVetSpeciesE(), castOther.getVetSpecies())
		.append(this.getVetSpeciesF(), castOther.getVetSpecies())
		.isEquals();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
