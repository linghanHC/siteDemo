/**
 * 
 */
package ca.gc.hc.siteDemo.models;

import java.io.Serializable;

import ca.gc.hc.siteDemo.util.LocaleDependantObject;
import ca.gc.hc.siteDemo.util.StringsUtil;

/**
 * @author Sylvain Larivière
 *
 */
public class DrugVeterinarySpecies extends LocaleDependantObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1807067900999839661L;
	private Long drugCode;
	private Long vetSpeciesCode;
	private String vetSpeciesE;
	private String vetSpeciesF;
	
	public DrugVeterinarySpecies() {
	}
	
	public DrugVeterinarySpecies(Long code, Long vetCode, String vetSpeciesE, String vetSpeciesF) {
		this.setDrugCode(code);
		this.setVetSpeciesCode(vetCode);
		this.setVetSpeciesE(vetSpeciesE);
		this.setVetSpeciesF(vetSpeciesF);
	}

	public DrugVeterinarySpecies(String vetSpeciesE, String vetSpeciesF)
	{
		this.vetSpeciesE = vetSpeciesE;
		this.vetSpeciesF = vetSpeciesF;
	}

	public Long getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(Long drugCode) {
		this.drugCode = drugCode;
	}

	public Long getVetSpeciesCode() {
		return vetSpeciesCode;
	}

	public void setVetSpeciesCode(Long vetSpeciesCode) {
		this.vetSpeciesCode = vetSpeciesCode;
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

	public String getStatus() {
		return isLanguageFrench() ? StringsUtil.substituteIfNull(vetSpeciesF, vetSpeciesE) : vetSpeciesE;
	}
}
