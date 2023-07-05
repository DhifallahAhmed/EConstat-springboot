package dpc.fr.back.dto;

import dpc.fr.back.entity.Insurance;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class InsuranceDTO {
    private String message;
    private Insurance insurance;
}
