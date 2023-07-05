package dpc.fr.back.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Data
@Getter
@Setter
public class InsuranceRequestDTO {
        private String name;
        private String numContrat;
        private String agency;
        private Date validityFrom;
        private Date validityTo;
        private int carId;

    }

