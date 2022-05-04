package ca.gc.hc.siteDemo.models;

import java.io.Serializable;

import ca.gc.hc.siteDemo.util.LocaleDependantObject;
import ca.gc.hc.siteDemo.util.StringsUtil;


/** @author Hibernate CodeGenerator */
public class Company extends LocaleDependantObject implements Serializable
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 8509859656027483437L;

/** identifier field */
  private Long companyCode;

  /** nullable persistent field */
  private String mfrCode;

  /** nullable persistent field */
  private String companyName;

  /** nullable persistent field */
  private String companyType;

  /** nullable persistent field */
  private String suiteNumner;

  /** nullable persistent field */
  private String streetNameE;
  private String streetNameF;

  /** nullable persistent field */
  private String cityName;

  /** nullable persistent field */
  private String provinceE;
  private String provinceF;

  /** nullable persistent field */
  private String countryE;
  private String countryF;

  /** nullable persistent field */
  private String postalCode;

  /** nullable persistent field */
  private String postOfficeBox;

  /** full constructor */
  public Company(
    Long companyCode,
    String mfrCode,
    String companyName,
    String companyType,
    String suiteNumner,
    String streetNameE,
    String streetNameF,
    String cityName,
    String provinceE,
    String provinceF,
    String countryE,
    String countryF,
    String postalCode,
    String postOfficeBox)
  {
    this.companyCode = companyCode;
    this.mfrCode = mfrCode;
    this.companyName = companyName;
    this.companyType = companyType;
    this.suiteNumner = suiteNumner;
    this.streetNameE = streetNameE;
    this.streetNameF = streetNameF;
    this.cityName = cityName;
    this.provinceE = provinceE;
    this.provinceF = provinceF;
    this.countryE = countryE;
    this.countryF = countryF;
    this.postalCode = postalCode;
    this.postOfficeBox = postOfficeBox;
  }

  /** default constructor */
  public Company()
  {
  }

  public Long getCompanyCode()
  {
    return this.companyCode;
  }

  public void setCompanyCode(Long companyCode)
  {
    this.companyCode = companyCode;
  }

  public String getMfrCode()
  {
    return this.mfrCode;
  }

  public void setMfrCode(String mfrCode)
  {
    this.mfrCode = mfrCode;
  }

  public String getCompanyName()
  {
    return this.companyName;
  }

  public void setCompanyName(String companyName)
  {
    this.companyName = companyName;
  }

  public String getCompanyType()
  {
    return this.companyType;
  }

  public void setCompanyType(String companyType)
  {
    this.companyType = companyType;
  }

  public String getSuiteNumner()
  {
    return this.suiteNumner;
  }

  public void setSuiteNumner(String suiteNumner)
  {
    this.suiteNumner = suiteNumner;
  }

  public String getStreetNameE()
  {
    return this.streetNameE;
  }

  public void setStreetNameE(String streetName)
  {
    this.streetNameE = streetName;
  }

  public String getCityName()
  {
    return this.cityName;
  }

  public void setCityName(String cityName)
  {
    this.cityName = cityName;
  }

  public String getProvinceE()
  {
    return this.provinceE;
  }

  public void setProvinceE(String province)
  {
    this.provinceE = province;
  }

  public String getCountryE()
  {
    return this.countryE;
  }

  public void setCountryE(String country)
  {
    this.countryE = country;
  }

  public String getPostalCode()
  {
    return this.postalCode;
  }

  public void setPostalCode(String postalCode)
  {
    this.postalCode = postalCode;
  }

  public String getPostOfficeBox()
  {
    return this.postOfficeBox;
  }

  public void setPostOfficeBox(String postOfficeBox)
  {
    this.postOfficeBox = postOfficeBox;
  }

public String getStreetNameF() {
	return streetNameF;
}
public String getStreetName() {
	return isLanguageFrench() ? StringsUtil.substituteIfNull(streetNameF, streetNameE) : streetNameE;
}

public void setStreetNameF(String streetNameF) {
	this.streetNameF = streetNameF;
}

public String getProvinceF() {
	return provinceF;
}

public String getProvince() {
	return isLanguageFrench() ? StringsUtil.substituteIfNull(provinceF, provinceE) : provinceE;
}

public void setProvinceF(String provinceF) {
	this.provinceF = provinceF;
}

public String getCountryF() {
	return countryF;
}

public String getCountry() {
	return isLanguageFrench() ? StringsUtil.substituteIfNull(countryF, countryE) : countryE;
}

public void setCountryF(String countryF) {
	this.countryF = countryF;
}


}
