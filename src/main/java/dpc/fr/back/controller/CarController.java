package dpc.fr.back.controller;

import dpc.fr.back.entity.Car;
import dpc.fr.back.entity.Insurance;
import dpc.fr.back.entity.UserEntity;
import dpc.fr.back.repository.CarRepository;

import dpc.fr.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("car")
public class CarController{

    @Autowired
    CarRepository carRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("get")
    public List<Car> findAllcars() {
        List<Car> cars = carRepository.findAll();
        return cars;
    }
    @GetMapping("get/{numSerie}")
    public Insurance findCarBynumSerie(@PathVariable String numSerie) {
       Car car = carRepository.findByNumSerie(numSerie);
        if (car == null) {
            return null;
        }
        return car.getInsurance();
    }

    @PostMapping("{userId}")
    public ResponseEntity<?> addNewCar(@PathVariable int userId, @RequestBody Car car) {
        try {
            if (car.getCarBrand() == null || car.getType() == null || car.getNumSerie() == null ||
                    car.getFiscalPower() == 0 || car.getNumImmatriculation() == null) {
                return ResponseEntity.badRequest().body("All fields are required");
            }
            Optional<UserEntity> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            UserEntity user = optionalUser.get();
            Car existingCar = carRepository.findByNumSerie(car.getNumSerie());
            if (existingCar != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Car already exists");
            }
            car.setOwner(user);
            carRepository.save(car);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Car is added");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
    @PostMapping("/removeCar/{idCar}")
    public ResponseEntity<String> removeCar(@PathVariable int idCar) {
        try {
            Optional<Car> carOptional = carRepository.findById(idCar);
            if (carOptional.isPresent()
            ) {
                Car car = carOptional.get();
                carRepository.delete(car);
                return ResponseEntity.ok("Car deleted from user's list");
                } else {
                    return ResponseEntity.badRequest().body("Car not found in user's list");
                }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }


}
