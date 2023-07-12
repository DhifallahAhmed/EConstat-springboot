package dpc.fr.back.controller;

import dpc.fr.back.dto.CarDamageDto;
import dpc.fr.back.entity.Car;
import dpc.fr.back.entity.CarDamage;
import dpc.fr.back.repository.CarRepository;
import dpc.fr.back.repository.DamageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("damage")
public class DamageController {
    @Autowired
    CarRepository carRepository;
    @Autowired
    DamageRepository damageRepository;
    @PostMapping("/car-damages")
    public ResponseEntity<CarDamage> addNewCarDamage(@RequestBody CarDamageDto carDamageDto) {
        try {
            Car car = carRepository.findById(carDamageDto.getCar().getCarId())
                    .orElseThrow(() -> new IllegalArgumentException("Car not found"));

            CarDamage carDamage = new CarDamage();
            carDamage.setTopLeft(carDamageDto.getTopLeft());
            carDamage.setMidLeft(carDamageDto.getMidLeft());
            carDamage.setBottomLeft(carDamageDto.getBottomLeft());
            carDamage.setTopRight(carDamageDto.getTopRight());
            carDamage.setMidRight(carDamageDto.getMidRight());
            carDamage.setBottomRight(carDamageDto.getBottomRight());
            carDamage.setCar(car);
            CarDamage savedCarDamage = damageRepository.save(carDamage);

            return ResponseEntity.ok(savedCarDamage);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
