package ca.gc.hc.siteDemo.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class AvailableApplicantBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -949029507380414697L;

	private String referenceNumber;
	
	private String firstName;
	
	private String lastName;
	
	private String contactNumber;
	
	private boolean isImported;
	
}
