package ca.gc.hc.siteDemo.models;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.gc.hc.siteDemo.util.LocaleDependantObject;
import ca.gc.hc.siteDemo.util.StringsUtil;

/** @author Hibernate CodeGenerator */
public class Schedule extends LocaleDependantObject implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6755631441081293380L;

	/** identifier field */
	private Long drugCode;

	/** nullable persistent field */
	private String scheduleE;
	private String scheduleF;
	private int scheduleCode;

	/** default constructor */
	public Schedule()
	{
	}

	/** full constructor */
	public Schedule(String scheduleE, String scheduleF)
	{
		this.scheduleE = scheduleE;
		this.scheduleF = scheduleF;
	}

	public String getScheduleE() {
		return scheduleE;
	}

	public void setScheduleE(String scheduleE) {
		this.scheduleE = scheduleE;
	}

	public String getScheduleF() {
		return scheduleF;
	}

	public void setScheduleF(String scheduleF) {
		this.scheduleF = scheduleF;
	}

	public int getScheduleCode()
	{
		return this.scheduleCode;
	}

	public void setScheduleCode(int scheduleCode)
	{
		this.scheduleCode = scheduleCode;
	}
	
	public Long getDrugCode()
	{
		return this.drugCode;
	}

	public void setDrugCode(Long drugCode)
	{
		this.drugCode = drugCode;
	}

	public String getSchedule() {
		return isLanguageFrench() ? StringsUtil.substituteIfNull(scheduleF, scheduleE) : scheduleE;
	}
	
	public String toString()
	{
		return new ToStringBuilder(this)
		.append("drugCode", getDrugCode())
		.append("scheduleE", getScheduleE())
		.append("scheduleF", getScheduleF())
		.append("scheduleCode", getScheduleCode())
		.toString();
	}

	public boolean equals(Object other)
	{
		if (!(other instanceof Schedule))
			return false;
		Schedule castOther = (Schedule) other;
		return new EqualsBuilder()
		.append(this.getDrugCode(), castOther.getDrugCode())
		.append(this.getScheduleE(), castOther.getScheduleE())
		.append(this.getScheduleF(), castOther.getScheduleF())
		.append(this.getScheduleCode(), castOther.getScheduleCode())
		.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder()
		.append(getDrugCode())
		.append(getScheduleE())
		.append(getScheduleF())
		.toHashCode();
	}


}
