package ca.gc.hc.siteDemo.models;

import java.io.Serializable;

import ca.gc.hc.siteDemo.util.LocaleDependantObject;
import ca.gc.hc.siteDemo.util.StringsUtil;

/** @author Hibernate CodeGenerator */
public class ActiveIngredients extends LocaleDependantObject implements Serializable
{

  /**
	 * 
	 */
	private static final long serialVersionUID = -4512941875807202740L;

/** identifier field */
  private Long id;

  /** persistent field */
  private Long drugCode;

  /** persistent field */
  private String ingredientE;
  private String ingredientF;

  /** nullable persistent field */
  private String strength;

  /** nullable persistent field */
  private String strengthUnitE;
  public String getStrengthUnitE() {
	return strengthUnitE;
}

public void setStrengthUnitE(String strengthUnitE) {
	this.strengthUnitE = strengthUnitE;
}

private String strengthUnitF;

  public String getStrengthUnitF() {
	return strengthUnitF;
}

public void setStrengthUnitF(String strengthUnitF) {
	this.strengthUnitF = strengthUnitF;
}

/** nullable persistent field */
  private String dosageValue;

  /** nullable persistent field */
  private String dosageUnitE;
  private String dosageUnitF;
  

  public String getDosageUnitF() {
	return dosageUnitF;
}

  public void setDosageUnitF(String dosageUnitF) {
	this.dosageUnitF = dosageUnitF;
}

/** full constructor */
  public ActiveIngredients(Long drugCode, String ingredientE, String ingredientF, 
		  String strength, String strengthUnitE, String strengthUnitF, String dosageValue, String dosageUnitE, String dosageUnitF)
  {
    this.drugCode = drugCode;
    this.ingredientE = ingredientE;
    this.ingredientF = ingredientF;
    this.strength = strength;
    this.strengthUnitE = strengthUnitE;
    this.dosageValue = dosageValue;
    this.dosageUnitE = dosageUnitE;
    this.dosageUnitF = dosageUnitF;
  }

  /** default constructor */
  public ActiveIngredients()
  {
  }

  /** minimal constructor */
  public ActiveIngredients(String ingredientE, String ingredientF)
  {
    this.ingredientE = ingredientE;
    this.ingredientF = ingredientF;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long long1)
  {
    id = long1;
  }

  public Long getDrugCode()
  {
    return this.drugCode;
  }

  public void setDrugCode(Long drugCode)
  {
    this.drugCode = drugCode;
  }

  public String getIngredient() {
	  return isLanguageFrench() ? StringsUtil.substituteIfNull(ingredientF, ingredientE) : ingredientE;
  }

  public void setIngredientE(String ingredientE)
  {
    this.ingredientE = ingredientE;
  }

  public String getStrength()
  {
    return this.strength;
  }

  public void setStrength(String strength)
  {
    this.strength = strength;
  }

  public String getDosageValue()
  {
    return this.dosageValue;
  }

  public void setDosageValue(String dosageValue)
  {
    this.dosageValue = dosageValue;
  }

  public String getDosageUnitE()
  {
    return this.dosageUnitE;
  }
  
  public void setDosageUnitE(String dosageUnitE)
  {
    this.dosageUnitE = dosageUnitE;
  }

	public String getIngredientF() {
		return ingredientF;
	}

	public void setIngredientF(String ingredientF) {
		this.ingredientF = ingredientF;
	}

	public String getIngredientE() {
		return ingredientE;
	}
}
