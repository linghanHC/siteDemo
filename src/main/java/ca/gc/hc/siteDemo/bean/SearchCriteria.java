package ca.gc.hc.siteDemo.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


// search criteria concatenated string display for selected dropdown lists
@Data
@NoArgsConstructor
public class SearchCriteria implements Serializable {

  private static final long serialVersionUID = -4194319164958654661L;

  private String selectedStatuses;
  private String selectedDrugClasses;
  private String selectedRoutes;
  private String selectedDosageForms;
  private String selectedSchedules;
  private String selectedSpecies;

}
