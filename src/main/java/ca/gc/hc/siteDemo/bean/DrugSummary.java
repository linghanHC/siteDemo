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
public class DrugSummary implements Serializable {

	private static final long serialVersionUID = 4547574437535824683L;

	private String companyName;
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
	private String dosageValue;
	private String route;
	private String schedule;
	private String scheduleLangOfPart;
	private String pm;	// product monograph
	private String aiStrengthAndDosageText;
	private String aiStrengthAndDosageLangOfPart;
	private String aiDosageLangOfPart;
	private String dosageUnit;
	private boolean isScheduleCDinIssued;

}
