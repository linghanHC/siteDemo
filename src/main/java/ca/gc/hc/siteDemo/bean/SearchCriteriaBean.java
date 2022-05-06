/*
 * Created on Apr 9, 2004
 *
 */
package ca.gc.hc.siteDemo.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import ca.gc.hc.siteDemo.util.ApplicationGlobals;
import ca.gc.hc.siteDemo.util.LocaleDependantObject;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 *
 * DPD Online Query Seach Croteria Bean.
 *
 */
public class SearchCriteriaBean extends LocaleDependantObject implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = -574811114931704542L;
  private Long drugCode;
  private Long companyCode;
  private String din;
  private String atc;
  private String companyName;
  private String[] statusCode;
  private String[] status;
  private String brandName;
  private String activeIngredient;
  private String aigNumber;
  private String biosimDrugSearch;
  private String[] route;
  private String[] dosage; // dosage form (pharmaceutical form)
  private String[] schedule;
  private String[] vetSpecies = null;
  private String[] vetSubSpecies = null;
  private String[] drugClass = null;

  /**
   * @return Active Ingredient Group Number
   */
  public String getAigNumber() {
    return this.aigNumber;
  }

  /**
   * @return ATC Number
   */
  public String getAtc() {
    return this.atc;
  }

  /**
   * @return Active Ingredient Name
   */
  public String getActiveIngredient() {
    return this.activeIngredient;
  }

  /**
   * @return Product Brand Name
   */
  public String getBrandName() {
    return this.brandName;
  }

  /**
   * @return Company Name
   */
  public String getCompanyName() {

    return this.companyName;
  }

  /**
   * @return Drug Identification Number
   */
  public String getDin() {
    return this.din;
  }

  /**
   * @return
   */
  public Long getDrugCode() {
    return drugCode;
  }

  /**
   * @return Drug Status
   */
  public String[] getStatus() {
    return this.status;
  }

  /**
   * @return Biosimilar Drug
   */
  public String getBiosimDrugSearch() {
    return this.biosimDrugSearch;
  }

  /**
   * Set AIG Number
   *
   * @param string
   */
  public void setAigNumber(String string) {
    if (string != null && string.length() > 0) {
      this.aigNumber = string.toUpperCase();
    } else {
      this.aigNumber = null;
    }
  }

  /**
   * Set ATC Number
   *
   * @param string
   */
  public void setAtc(String string) {
    if (string != null && string.length() > 0) {
      this.atc = string.toUpperCase();
    } else {
      this.atc = null;
    }
  }

  /**
   * Set Active Ingredient Name
   *
   * @param string
   */
  public void setActiveIngredient(String string) {
    if (string != null && string.length() > 0) {
      /*
       * SL/2013-02-14: should display the way it was entered particularly now that
       * search is case- and accent-insensitive
       */
      // this.activeIngredient = string.toUpperCase();
      this.activeIngredient = string;
    } else {
      this.activeIngredient = null;
    }
  }

  /**
   * Set Product Brand Name
   *
   * @param string
   */
  public void setBrandName(String string) {
    if (string != null && string.length() > 0) {
      /*
       * SL/2013-02-14: should display the way it was entered particularly now that
       * search is case- and accent-insensitive
       */
      // this.brandName = string.toUpperCase();
      this.brandName = string;
    } else {
      this.brandName = null;
    }
  }

  /**
   * Set Company Name
   *
   * @param string
   */
  public void setCompanyName(String string) {
    if (string != null && string.length() > 0) {
      /*
       * SL/2013-02-14: should display the way it was entered particularly now that
       * search is case- and accent-insensitive
       */
      // this.companyName = string.toUpperCase();
      this.companyName = string;
    } else {
      this.companyName = null;
    }
  }

  /**
   * Set Drug Identification Number
   *
   * @param string
   */
  public void setDin(String string) {
    if (string != null && string.length() > 0) {
      this.din = string;
    } else {
      this.din = null;
    }
  }

  /**
   * @param val
   */
  public void setDrugCode(Long val) {
    drugCode = val;
  }

  /**
   * Set Drug Marketing status
   *
   * @param val
   */
  public void setStatus(String[] val) {
    this.status = val;
  }

  /**
   * @return
   */
  public Long getCompanyCode() {
    return companyCode;
  }

  /**
   * @param long1
   */
  public void setCompanyCode(Long long1) {
    companyCode = long1;
  }

  /**
   * Set Biosimilar Drug
   *
   * @param string
   */
  public void setBiosimDrugSearch(String string) {
    if (string != null && string.length() > 0) {
      // Only when Biosimilar biologic drug is selected, String value will be
      // displayed at the search result page
      this.biosimDrugSearch = localisedProperty("Yes", "Oui");
    } else
      // This is used to determine what search criteria shall apply
      this.biosimDrugSearch = null;
  }

  /**
   * @return Well formated object content
   */
  public String toString() {
    return new ToStringBuilder(this).append("drugCode", getDrugCode()).append("din", getDin()).append("atc", getAtc())
        .append("companyCode", getCompanyCode()).append("companyName", getCompanyName())
        // .append("status", status == null ? null : getStatus().toString())
        .append("status", status == null ? null : Arrays.toString(getStatus())).append("brandName", getBrandName())
        .append("activeIngredient", getActiveIngredient()).append("aigNumber", getAigNumber())
        .append("biosimDrug", getBiosimDrugSearch()).append("route", route == null ? null : getRouteEnumeration())
        .append("dosage", dosage == null ? null : getDosageEnumeration())
        .append("schedule", schedule == null ? null : getScheduleEnumeration())
        // .append("vetSpecies", getVetSpecies())
        .append("vetSpecies", vetSpecies == null ? null : getVetSpeciesEnumeration())
        // .append("vetSubSpecies", getVetSubSpecies())
        .append("drugClass", drugClass == null ? null : getDrugClassEnumeration()).toString();
  }

  // updated for an array of values SL/2009-09-10
  public String[] getDosage() {
    return dosage;
  }

  // updated for an array of values SL/2009-09-10
  public void setDosage(String[] dosage) {
    this.dosage = dosage;
  }

  // updated for an array of values SL/2009-09-10
  public String[] getSchedule() {
    return schedule;
  }

  // updated for an array of values SL/2009-09-10
  public void setSchedule(String[] schedule) {
    this.schedule = schedule;
  }

  public String[] getVetSpecies() {
    return vetSpecies;
  }

  public void setVetSpecies(String[] vetSpecies) {
    this.vetSpecies = vetSpecies;
  }

  public String[] getVetSubSpecies() {
    return vetSubSpecies;
  }

  public void setVetSubSpecies(String[] vetSubSpecies) {
    this.vetSubSpecies = vetSubSpecies;
  }

  public String[] getRoute() {
    return route;
  }

  public void setRoute(String[] route) {
    this.route = route;
  }

  /**
   * @author Sylvain Lariviere 2009-09-30
   * @return All the routes of administration as a single String
   */
  public String getRouteEnumeration() {
    return enumerateThisStringArray(getRoute());
  }

  /**
   * @author Sylvain Lariviere 2009-09-30
   * @param anAra String array
   * @return A String containing all the passed array elements, separated by a
   *         comma and space, or the selectAll label, if no individual item was
   *         selected
   */
  // todo move this to a util class
  private String enumerateThisStringArray(String[] anAra) {
    String result = "";

    if (anAra != null && anAra.length > 0) {
      if (anAra.length == 1 && anAra[0].toString().equals("0")) {
        // no individual item was selected: just return the selectAll label
        result = "Select all"; // todo ApplicationGlobals.instance().getSelectAllLabel();
      } else {
        for (int i = 0; i < anAra.length; i++) {
          if (!"0".equals(anAra[i])) { // exclude value 0: it means "Select All" and was included in the selection
            String anAraLabel = anAra[i].replace("/Hc", "/HC");
            result = result + anAraLabel + ", ";
          }
        }
        result = result.substring(0, result.length() - 2); // remove last comma
      }
    } else {
      result = null;
    }

    return result;
  }

  /**
   * @author Sylvain Lariviere 2009-09-30
   * @return All the dosage forms as a single String
   */
  public String getDosageEnumeration() {
    return enumerateThisStringArray(getDosage());
  }

  /**
   * @author Sylvain Lariviere 2009-09-30
   * @return All the schedules as a single String
   */
  public String getScheduleEnumeration() {
    return enumerateThisStringArray(getSchedule());
  }

  public String getVetSpeciesEnumeration() {
    return enumerateThisStringArray(getVetSpecies());
  }

  public String[] getStatusCode() {
    boolean selectAll = false;
    for (String status : this.status) {
      if (status.equals("0")) {
        selectAll = true;
      }
    }
    if (selectAll) {
      return new String[] { "0" };
    } else {
      return this.status;
    }
  }

  public String getStatusEnumeration() throws Exception {
    String[] statusEnumeration = new String[this.status.length];
    // TODO
//    List<LabelValueBean> statusList = ApplicationGlobals.instance().getStatus();
//    int i = 0;
//    for (String s : this.status) {
//      for (LabelValueBean statusLabel : statusList) {
//        if (s.equals(statusLabel.getValue())) {
//          statusEnumeration[i] = statusLabel.getLabel();
//          i++;
//        }
//      }
//    }
    return enumerateThisStringArray(statusEnumeration);
  }

  public void setStatusCode(String[] statusCode) {
    this.statusCode = statusCode;
  }

  public void setDrugClass(String[] drugClass) {
    this.drugClass = drugClass;
  }

  public String[] getDrugClass() {
    return drugClass;
  }

  /**
   * @author Sylvain Lariviere 2014-10-20
   * @return All the drug classes as a single String
   */
  public String getDrugClassEnumeration() {
    return enumerateThisStringArray(getDrugClass());
  }
}
