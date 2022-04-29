package ca.gc.hc.siteDemo.forms;

import java.io.Serializable;

import ca.gc.hc.siteDemo.constants.Language;

public class BaseForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5097687752816461188L;

	private long advisorId;
	
	private String lastName;
	
	private String firstName;
	
	private String salutation;
	
	private Language communicationLang;

	public long getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(long advisorId) {
		this.advisorId = advisorId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public Language getCommunicationLang() {
		return communicationLang;
	}

	public void setCommunicationLang(Language communicationLang) {
		this.communicationLang = communicationLang;
	}

}
