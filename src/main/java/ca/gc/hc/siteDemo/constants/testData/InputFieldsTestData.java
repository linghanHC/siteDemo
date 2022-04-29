package ca.gc.hc.siteDemo.constants.testData;

import ca.gc.hc.siteDemo.forms.InputFieldsForm;

public class InputFieldsTestData {

	public static InputFieldsForm getBasicInputFiledsTestData() {

		InputFieldsForm inputFieldsForm = new InputFieldsForm();		
		inputFieldsForm.setIsPharmaceutical(true);
		inputFieldsForm.setHasSecurityClearance(true);
		inputFieldsForm.setSecurityClearanceRecievedDate("2021-11-11");
		inputFieldsForm.setSecurityClearanceComments("Test data");
		
		return inputFieldsForm;
	}
	
}
