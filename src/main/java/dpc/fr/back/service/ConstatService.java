package dpc.fr.back.service;

import dpc.fr.back.dto.CarDamageDto;
import dpc.fr.back.dto.ConstatDto;
import dpc.fr.back.entity.*;
import dpc.fr.back.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConstatService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    InsuranceRepository insuranceRepository;
    @Autowired
    DamageRepository carDamageRepository;
    @Autowired
    ConstatRepository constatRepository;
    public ResponseEntity<Constat> addNewConstat(ConstatDto constatDto) {
            try {
                // Retrieve the necessary entities
                UserEntity userA = userRepository.findById(constatDto.getUserAId())
                        .orElseThrow(() -> new IllegalArgumentException("User A not found"));
                Car carA = carRepository.findById(constatDto.getCarAId())
                        .orElseThrow(() -> new IllegalArgumentException("Car A not found"));
                Insurance insuranceA = insuranceRepository.findById(constatDto.getInsuranceAId())
                        .orElseThrow(() -> new IllegalArgumentException("Insurance A not found"));
                UserEntity userB = userRepository.findById(constatDto.getUserBId())
                        .orElseThrow(() -> new IllegalArgumentException("User B not found"));
                Car carB = carRepository.findById(constatDto.getCarBId())
                        .orElseThrow(() -> new IllegalArgumentException("Car B not found"));
                Insurance insuranceB = insuranceRepository.findById(constatDto.getInsuranceBId())
                        .orElseThrow(() -> new IllegalArgumentException("Insurance B not found"));

                // Create the Constat entity
                Constat constat = new Constat();
                constat.setUserA(userA);
                constat.setCarA(carA);
                constat.setInsuranceA(insuranceA);
                constat.setUserB(userB);
                constat.setCarB(carB);
                constat.setInsuranceB(insuranceB);

                // Create and associate the CarDamage entities
                CarDamage carDamageA = new CarDamage();
                carDamageA.setTopLeft(constatDto.getCarDamageA().getTopLeft());
                carDamageA.setMidLeft(constatDto.getCarDamageA().getMidLeft());
                carDamageA.setBottomLeft(constatDto.getCarDamageA().getBottomLeft());
                carDamageA.setTopRight(constatDto.getCarDamageA().getTopRight());
                carDamageA.setMidRight(constatDto.getCarDamageA().getMidRight());
                carDamageA.setBottomRight(constatDto.getCarDamageA().getBottomRight());
                carDamageA.setCar(carA);
                carDamageRepository.save(carDamageA);
                constat.setCarDamageA(carDamageA);

                CarDamage carDamageB = new CarDamage();
                carDamageB.setTopLeft(constatDto.getCarDamageB().getTopLeft());
                carDamageB.setMidLeft(constatDto.getCarDamageB().getMidLeft());
                carDamageB.setBottomLeft(constatDto.getCarDamageB().getBottomLeft());
                carDamageB.setTopRight(constatDto.getCarDamageB().getTopRight());
                carDamageB.setMidRight(constatDto.getCarDamageB().getMidRight());
                carDamageB.setBottomRight(constatDto.getCarDamageB().getBottomRight());
                carDamageB.setCar(carB);
                carDamageRepository.save(carDamageB);
                constat.setCarDamageB(carDamageB);

                // Save the Constat entity
                Constat savedConstat = constatRepository.save(constat);

                return ResponseEntity.ok(savedConstat);
            } catch (Exception e) {
                System.out.println(e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }



    public ResponseEntity<String> getConstat(int idU) {
            Constat constat = constatRepository.findByUserA_UserId(idU);
            if (constat != null) {
                int carBId = constat.getCarB().getCarId();
                Car carB = carRepository.findById(carBId)
                        .orElseThrow(() -> new IllegalArgumentException("Car B not found"));

                String brand = String.valueOf(carB.getCarBrand());
                return ResponseEntity.ok(brand);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur");
            }
        }


}