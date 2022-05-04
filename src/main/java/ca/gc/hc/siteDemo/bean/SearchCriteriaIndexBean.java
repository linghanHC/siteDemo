/*
 * Created on Apr 9, 2004
 *
 */
package ca.gc.hc.siteDemo.bean;
import java.io.Serializable;
 

/**
 * 
 * DPD Online Query Seach Croteria Bean.
 *
 */
public class SearchCriteriaIndexBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7647848583485796103L;
	private Boolean selected;
	private int index;
	private Object criteria;
	private Object criteriaObjectClass;
	
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Object getCriteria() {
		return criteria;
	}
	public void setCriteria(Object criteria) {
		this.criteria = criteria;
	}
	public Object getCriteriaObjectClass() {
		return criteriaObjectClass;
	}
	public void setCriteriaObjectClass(Object criteriaObjectClass) {
		this.criteriaObjectClass = criteriaObjectClass;
	}
	
	
}
