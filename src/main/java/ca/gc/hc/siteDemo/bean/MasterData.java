package ca.gc.hc.siteDemo.bean;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class MasterData {
	private HashMap<String, List<LabelValueBean>> statusMap = new HashMap<String,List<LabelValueBean>>();
	private HashMap<String,List<LabelValueBean>> drugClassMap = new HashMap<String,List<LabelValueBean>>();
	private HashMap<String,List<LabelValueBean>> uniqueFormsMap = new HashMap<String,List<LabelValueBean>>();
	private HashMap<String,List<LabelValueBean>> uniqueRoutesMap = new HashMap<String,List<LabelValueBean>>();
	private HashMap<String,List<LabelValueBean>> uniqueSchedulesMap = new HashMap<String,List<LabelValueBean>>();
	private HashMap<String,List<LabelValueBean>> uniqueSpeciesMap = new HashMap<String,List<LabelValueBean>>();
}
