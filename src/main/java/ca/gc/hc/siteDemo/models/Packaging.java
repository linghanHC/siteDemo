
package ca.gc.hc.siteDemo.models;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
/** @author Hibernate CodeGenerator */
public class Packaging implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = -6440825886004389618L;

/** identifier field */
  private Long packagingId;
 
  /** identifier field */
  private Long drugCode;

  /** nullable persistent field */
  private String upc;

  /** nullable persistent field */
  private String packageSize;

  /** nullable persistent field */
  private String packageSizeUnit;

  /** nullable persistent field */
  private String packageType;

  /** nullable persistent field */
  private String productInformation;

  /** full constructor */
  public Packaging(
	Long packagingId,
	Long drugCode,
    String upc,
    String packageSize,
    String packageSizeUnit,
    String packageType,
    String productInformation)
  {
  	this.drugCode = drugCode;
    this.upc = upc;
    this.packageSize = packageSize;
    this.packageSizeUnit = packageSizeUnit;
    this.packageType = packageType;
    this.productInformation = productInformation;
  }

  /** default constructor */
  public Packaging()
  {
  }

  public Long getDrugCode()
  {
    return this.drugCode;
  }

  public void setDrugCode(Long drugCode)
  {
    this.drugCode = drugCode;
  }

  public String getUpc()
  {
    return this.upc;
  }

  public void setUpc(String upc)
  {
    this.upc = upc;
  }

  public String getPackageSize()
  {
    return this.packageSize;
  }

  public void setPackageSize(String packageSize)
  {
    this.packageSize = packageSize;
  }

  public String getPackageSizeUnit()
  {
    return this.packageSizeUnit;
  }

  public void setPackageSizeUnit(String packageSizeUnit)
  {
    this.packageSizeUnit = packageSizeUnit;
  }

  public String getPackageType()
  {
    return this.packageType;
  }

  public void setPackageType(String packageType)
  {
    this.packageType = packageType;
  }

  public String getProductInformation()
  {
    return this.productInformation;
  }

  public void setProductInformation(String productInformation)
  {
    this.productInformation = productInformation;
  }
  public String toString()
  {
	return new ToStringBuilder(this)
	  .append("drugCode", getDrugCode())
	  .append("upc", getUpc())
	  .append("packageSize", getPackageSize())
	  .append("packageSizeUnit", getPackageSizeUnit())
	  .append("PackageType", getPackageType())
	  .append("packageInformation", getProductInformation())
	  .toString();
  }

 


/**
 * @return
 */
public Long getPackagingId() {
	return packagingId;
}

/**
 * @param long1
 */
public void setPackagingId(Long long1) {
	packagingId = long1;
}

}
