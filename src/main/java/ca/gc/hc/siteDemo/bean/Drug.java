package ca.gc.hc.siteDemo.bean;

import ca.gc.hc.siteDemo.models.Veterinary;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean class used to display detailed drug information
 */
@Data
@NoArgsConstructor
public class Drug implements Serializable {

    private static final long serialVersionUID = -3412785890846243275L;

    private boolean isPmAvailable;

    private String pmName;

    private String pmDate;

    private String din;

    private String statusLangOfPart;

    private String status;

    private boolean isRadioPharmaceutical;

    private String historyDate;

    private boolean isApproved;

    private String originalMarketDate;

    private String lotNumber;

    private String expirationDate;

    private String brandNameLangOfPart;

    private String brandName;

    private String descriptor;

    private String descriptorLangOfPart;

    private boolean isVeterinary;

    private List<Veterinary> vetSpecies = new ArrayList<>();

    private boolean displayFootnoteTwo;

    private boolean displayFootnoteSeven;
}
