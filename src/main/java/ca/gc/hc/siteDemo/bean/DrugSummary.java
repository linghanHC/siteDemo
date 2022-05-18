	package ca.gc.hc.siteDemo.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * Drug Product summary information view object
 */
// extends BaseBean  todo?
// todo? merge with JsonSummaryBean??
	@Data
	@NoArgsConstructor
public class DrugSummary implements Serializable
{

	private static final long serialVersionUID = 4547574437535824683L;

	private String id;
	private Long drugCode;
	private String din;
	private String status;
	private String brandName;
	private String brandLangOfPart;
	private String drugClass;
	private String classLangOfPart;
	private Long numberOfAis;

	private String firstAIName;
	private String firstAILangOfPart;
	private String dosage;
	private String route;
	private String schedule;
	private String scheduleLangOfPart;


	//	private DrugProduct drug;
	private String companyName;
//	private DrugStatus status;
	private String pm;
//	private String pmE;
//	private String pmF;
//	private ActiveIngredients firstAI;	//SL/2009-10-01: the first active ingredient used (ordered by id)
	private String aiStrengthAndDosageText;
	private String aiStrengthAndDosageLangOfPart;
	//	private Route route;
//	private Form  dosage;
//	private Schedule schedule;
//	private DrugVeterinarySpecies vetSpecies;
	private boolean isScheduleCDinIssued;

}
