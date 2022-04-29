package ca.gc.hc.siteDemo.constants.testData;

import java.util.ArrayList;
import java.util.List;

import ca.gc.hc.siteDemo.constants.Language;
import ca.gc.hc.siteDemo.models.AvailableApplicantBean;
import ca.gc.hc.siteDemo.models.SelectAdvisorBean;

public class AdvisorTestData {

	public static List<SelectAdvisorBean> getBasicAdvisorListTestData(boolean isNoResult) {
		
		if (isNoResult) {
			return null;
		}
		
		List<SelectAdvisorBean> advisorList = new ArrayList<SelectAdvisorBean>();
		
		SelectAdvisorBean advisor1 = new SelectAdvisorBean();
		advisor1.setAdvisorId(4321L);
		advisor1.setFirstName("Paul");
		advisor1.setLastName("Larocque");
		advisor1.setCommunicationLang(Language.ENGLISH);
		advisor1.setSalutation("Mr");
		
		SelectAdvisorBean advisor2 = new SelectAdvisorBean();
		advisor2.setAdvisorId(4323L);
		advisor2.setFirstName("Katarina");
		advisor2.setLastName("Farsky");
		advisor2.setCommunicationLang(Language.FRENCH);
		advisor2.setSalutation("Mrs");
		
		SelectAdvisorBean advisor3 = new SelectAdvisorBean();
		advisor3.setAdvisorId(4327L);
		advisor3.setFirstName("William");
		advisor3.setLastName("O'Neil");
		advisor3.setCommunicationLang(Language.ENGLISH);
		advisor3.setSalutation("Mr");
		
		SelectAdvisorBean advisor4 = new SelectAdvisorBean();
		advisor4.setAdvisorId(4331L);
		advisor4.setFirstName("Pollen");
		advisor4.setLastName("Yeung");
		advisor4.setCommunicationLang(Language.ENGLISH);
		advisor4.setSalutation("Ms");

		SelectAdvisorBean advisor5 = new SelectAdvisorBean();
		advisor5.setAdvisorId(4335L);
		advisor5.setFirstName("Rene");
		advisor5.setLastName("Langlois");
		advisor5.setCommunicationLang(Language.FRENCH);
		advisor5.setSalutation("Mr");

		advisorList.add(advisor1);
		advisorList.add(advisor2);
		advisorList.add(advisor3);
		advisorList.add(advisor4);
		advisorList.add(advisor5);
		
		return advisorList;
		
	}
	
	public static List<AvailableApplicantBean> getImportApplicantsListTestData() {
		List<AvailableApplicantBean> applicantsList = new ArrayList<AvailableApplicantBean>();
		
		AvailableApplicantBean applicant1 = new AvailableApplicantBean();
		applicant1.setReferenceNumber("8596-13433");
		applicant1.setFirstName("Paul");
		applicant1.setLastName("Larocque");
		applicant1.setContactNumber("762");
		applicant1.setImported(false);

		AvailableApplicantBean applicant2 = new AvailableApplicantBean();
		applicant2.setReferenceNumber("8597-10003");
		applicant2.setFirstName("Katarina");
		applicant2.setLastName("Farsky");
		applicant2.setContactNumber("763");
		applicant2.setImported(false);

		AvailableApplicantBean applicant3 = new AvailableApplicantBean();
		applicant3.setReferenceNumber("8598-13241");
		applicant3.setFirstName("William");
		applicant3.setLastName("O'Neil");
		applicant3.setContactNumber("764");
		applicant3.setImported(false);

		AvailableApplicantBean applicant4 = new AvailableApplicantBean();
		applicant4.setReferenceNumber("8599-34341");
		applicant4.setFirstName("Pollen");
		applicant4.setLastName("Yeung");
		applicant4.setContactNumber("765");
		applicant4.setImported(false);

		AvailableApplicantBean applicant5 = new AvailableApplicantBean();
		applicant5.setReferenceNumber("8600-32043");
		applicant5.setFirstName("Rene");
		applicant5.setLastName("Langlois");
		applicant5.setContactNumber("766");
		applicant5.setImported(false);

		applicantsList.add(applicant1);
		applicantsList.add(applicant2);
		applicantsList.add(applicant3);
		applicantsList.add(applicant4);
		applicantsList.add(applicant5);
		
		return applicantsList;

	}
	
}
