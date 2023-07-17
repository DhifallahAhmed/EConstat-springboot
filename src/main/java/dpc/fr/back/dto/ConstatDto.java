package dpc.fr.back.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class ConstatDto {
    private int userAId;
    private int carAId;
    private int insuranceAId;
    private CarDamageDto carDamageA;
    private int userBId;
    private int carBId;
    private int insuranceBId;
    private CarDamageDto carDamageB;
}
