package ca.gc.hc.siteDemo.models;

import java.io.Serializable;

import ca.gc.hc.siteDemo.constants.Language;
import lombok.Data;

@Data
public class SelectAdvisorBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4710305403868095780L;

	private Long advisorId;
	
	private String firstName;
	
	private String lastName;
	
	private String salutation;
	
	private Language communicationLang;

}
