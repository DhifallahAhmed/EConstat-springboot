package dpc.fr.back.dto;

import dpc.fr.back.entity.Car;
import dpc.fr.back.entity.Insurance;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Data
@Getter
@Setter
@Builder
public class CarDamageDto {
    private Boolean topLeft;
    private Boolean midLeft;
    private Boolean bottomLeft;
    private Boolean topRight;
    private Boolean midRight;
    private Boolean bottomRight;
    private Car car;
}
