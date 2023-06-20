package dpc.fr.back.controller;

import dpc.fr.back.entity.Car;
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
    public String findCarBynumSerie(@PathVariable String numSerie) {
       Car car = carRepository.findByNumSerie(numSerie);
        if (car == null) {
            return null;
        }
        return car.getOwner().toString();
    }

    @PostMapping("{userId}")
    public ResponseEntity<?> addNewCar(@PathVariable int userId, @RequestBody Car car) {
        try {
            // Perform validation checks on the car object
            if (car.getCarBrand() == null || car.getType() == null || car.getNumSerie() == null ||
                    car.getFiscalPower() == 0 || car.getNumImmatriculation() == null) {
                return ResponseEntity.badRequest().body("All fields are required");
            }

            // Check if the user exists
            Optional<UserEntity> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            UserEntity user = optionalUser.get();

            // Check if the car with the same serial number already exists
            Car existingCar = carRepository.findByNumSerie(car.getNumSerie());
            if (existingCar != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Car already exists");
            }

            // Set the owner of the car
            car.setOwner(user);

            // Save the car to the database
            Car savedCar = carRepository.save(car);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Car is added");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
    @PostMapping("/removeCar/{userId}")
    public ResponseEntity<String> removeCar(@RequestBody String numSerie, @PathVariable int userId) {
        try {
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                List<Car> cars = user.getCars();
                Car carToRemove = null;
                for (Car car : cars) {
                    if (car.getNumSerie().equals(numSerie)) {
                        carToRemove = car;
                        break;
                    }
                }
                if (carToRemove != null) {
                    cars.remove(carToRemove);
                    userRepository.save(user);
                    return ResponseEntity.ok("Car deleted from user's list");
                } else {
                    return ResponseEntity.badRequest().body("Car not found in user's list");
                }
            } else {
                return ResponseEntity.badRequest().body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }


}
