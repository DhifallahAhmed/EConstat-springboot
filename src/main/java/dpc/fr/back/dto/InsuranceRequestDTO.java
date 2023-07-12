package dpc.fr.back.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
@Data
@Getter
@Setter
public class InsuranceRequestDTO {
        private String name;
        private String numContrat;
        private String agency;
        @Temporal(TemporalType.DATE)
        private Date validityFrom;
        @Temporal(TemporalType.DATE)
        private Date validityTo;
        private int carId;

    }

