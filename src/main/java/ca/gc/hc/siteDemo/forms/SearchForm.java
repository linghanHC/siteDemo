package ca.gc.hc.siteDemo.forms;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.Serializable;

/**
 * Action Form class holding the search criteria information.
 */
public class SearchForm implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -192278904074337015L;
    private String drugCode = null;
    private String din = null;
    private String atc = null;
    private String companyName = null;
    private String brandName = null;
    private String activeIngredient = null;
    private String aigNumber = null;
    private String biosimDrugSearch = null;
    private String biosimDrug = null;
    private String[] status = null;
    private String pm = null;
    private String[] dosage = null;
    private String[] schedule = null;
    private String[] route = null;
    private String[] drugClass = null;
    private String[] vetSpecies = null;

    public String getAigNumber()
    {
        if( this.aigNumber != null )
            return this.aigNumber.trim();
        else
            return null;
    }

    public String getAtc()
    {
        if( this.atc != null)
            return this.atc.trim();
        else
            return null;
    }

    public String getActiveIngredient()
    {
        if( this.activeIngredient != null)
            return activeIngredient.trim();
        else
            return null;
    }

    public String getBrandName()
    {
        if( this.brandName != null)
            return brandName.trim();
        else
            return null;
    }

    public String getCompanyName()
    {
        if( this.companyName != null)
            return companyName.trim();
        else
            return null;
    }

    public String getDin()
    {
        if( this.din != null)
            return din.trim();
        else
            return null;
    }

    public String getDrugCode()
    {
        if( this.drugCode != null)
            return drugCode.trim();
        else
            return null;
    }

    public String[] getStatus()
    {
        return status;
    }

    /**
     * @param string
     */
    public void setAigNumber(String string)
    {
        if( string != null)
            aigNumber = string.trim();
    }

    /**
     * @param string
     */
    public void setAtc(String string)
    {
        if( string != null)
            atc = string.trim();
    }

    /**
     * @param string
     */
    public void setActiveIngredient(String string)
    {
        if( string != null)
            activeIngredient = string.trim();
    }

    /**
     * @param string
     */
    public void setBrandName(String string)
    {
        if( string != null)
            brandName = string.trim();
    }

    /**
     * @param string
     */
    public void setCompanyName(String string)
    {
        if( string != null)
            companyName = string.trim();
    }

    /**
     * @param string
     */
    public void setDin(String string)
    {
        if( string != null)
            din = string.trim();
    }

    /**
     * @param string
     */
    public void setDrugCode(String string)
    {
        if( string != null)
            drugCode = string.trim();
    }

    /**
     * @param string
     */
    public void setStatus(String[] string)
    {
        if( string != null)
            status = string;
    }


    public String getBiosimDrugSearch()
    {

        return this.biosimDrugSearch;
    }

    public void setBiosimDrugSearch(String string)
    {
        if( string != null)
            biosimDrugSearch = string.trim();
    }

    public String getBiosimDrug()
    {

        return this.biosimDrug;
    }

    /**
     *
     */
    public void setBiosimDrugh(String string)
    {
        if( string != null)
            biosimDrug = string.trim();
    }

/*
    public void reset()
    {
        this.drugCode = null;
        this.din = null;
        this.atc = null;
        this.status = new String[]{"-1"}; //Requested by ADR0250-1617, default "Select a status"
        this.companyName = null;
        this.brandName = null;
        this.activeIngredient = null;
        this.aigNumber = null;
        this.biosimDrugSearch = null;
        this.biosimDrug = null;
        this.pm = null;
        this.route = new String[]{"0"};
        this.dosage = new String[]{"0"};
        this.schedule = new String[]{"0"};
        this.drugClass = new String[]{"0"};
        this.vetSpecies = new String[]{"0"};
    }

    public ActionErrors validate(
            ActionMapping mapping,
            HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession(false);
        if (session.getAttribute("sessionActive") != null)
        {
            int count = 0;

            if (din != null && din.length() > 0)
            {
                count++;

                if ((din.length() != 8) || !StringUtils.isNumeric(din))
                {
                    errors.add("DIN", new ActionMessage("error.invalide.din"));
                }
            }

            if (atc != null && atc.length() > 0) {
                count++;

            }

            if ((companyName != null && companyName.length() > 0)
                    || (brandName != null && brandName.length() > 0)
                    || (activeIngredient != null && activeIngredient.length() > 0)
                    || (aigNumber != null && aigNumber.length() > 0)
                    || (dosage != null && dosage.length > 0)
                    || (vetSpecies != null && vetSpecies.length > 0)
                    || (schedule != null && schedule.length > 0)
                    || (route != null && route.length > 0)
                    || (biosimDrugSearch != null)) {

                *//* if this is a din or atc search, disregard the default values
                 * for dosage, schedule, and route ("0", for 'Select All')
                 *//*
                String zero = "0";

                if (count > 0
                        && ( dosage[0].equals(zero)
                        && schedule[0].equals(zero)
                        && route[0].equals(zero)) ){
                    // skip
                }else{
                    count++;
                }
            }

            if (session.getAttribute(ApplicationGlobals.AJAX_BEAN) == null){
                // if not from an AJAX call, validate status - resolves JIRA DPD-50
                if (status != null){
                    if (status[0].equals("-1")){
                        errors.add("status", new ActionMessage("error.invalide.status"));
                    }
                }
            }

            if (aigNumber != null
                    && aigNumber.length() > 0
                    && (!StringUtils.isNumeric(aigNumber) || aigNumber.length() != 10))
            {
                errors.add("aigNumber", new ActionMessage("error.invalide.aig"));
            }

            if (count > 1)
            {
                errors.clear();
                errors.add(
                        "criteria",
                        new ActionMessage("error.invalid.toomany.criterias"));
            }
            else if (count < 1)
            {
                errors.clear();
                errors.add("criteria", new ActionMessage("error.invalid.no.criteria"));
            }
        }
        else
        {
            errors.clear();
        }
        return errors;

    }*/
    public String toString()
    {
        return new ToStringBuilder(this)
                .append("drugCode", getDrugCode())
                .append("din", getDin())
                .append("atc", getAtc())
                .append("biosimDrugSearch", getBiosimDrugSearch())
                .append("status", getStatus().toString())
                .append("companyName", getCompanyName())
                .append("brandName", getBrandName())
                .append("brandName", getActiveIngredient())
                .append("productMonograph", getPm())
                .append("route", getRoute().toString())
                .append("dosage", getDosage().toString())
                .append("vetSpecies", getVetSpecies().toString())
                .append("schedule", getSchedule().toString())
                .toString();
    }
    /**
     * @return
     */
    public String getPm() {
        return pm;
    }

    /**
     * @param string
     */
    public void setPm(String string) {
        pm = string;
    }

    public String[] getDosage() {
        return dosage;
    }

    public void setDosage(String[] dosage) {
        this.dosage = dosage;
    }

    public String[] getSchedule() {
        return schedule;
    }

    public void setSchedule(String[] schedule) {
        this.schedule = schedule;
    }

    public String[] getRoute() {
        return route;
    }

    public void setRoute(String[] route) {
        this.route = route;
    }

    public void setDrugClass(String[] drugClass) {
        this.drugClass = drugClass;
    }

    public String[] getDrugClass() {
        return drugClass;
    }

    public String[] getVetSpecies() {
        return vetSpecies;
    }

    public void setVetSpecies(String[] vetSpecies) {
        this.vetSpecies = vetSpecies;
    }

}

