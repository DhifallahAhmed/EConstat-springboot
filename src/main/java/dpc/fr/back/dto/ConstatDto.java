package dpc.fr.back.dto;

import lombok.*;

import java.util.List;
@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
