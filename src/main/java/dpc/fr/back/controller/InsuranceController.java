package dpc.fr.back.controller;

import dpc.fr.back.dto.InsuranceDTO;
import dpc.fr.back.dto.InsuranceRequestDTO;
import dpc.fr.back.entity.Car;
import dpc.fr.back.entity.Insurance;
import dpc.fr.back.repository.CarRepository;
import dpc.fr.back.repository.InsuranceRepository;
import dpc.fr.back.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("insurance")
public class InsuranceController {
        @Autowired
        private InsuranceRepository insuranceRepository;

        @Autowired
        private CarRepository carRepository;
        @Autowired
        private InsuranceService insuranceService;

        @PostMapping("/addNewInsurance")
        public ResponseEntity<InsuranceDTO> addNewInsurance(@RequestBody InsuranceRequestDTO requestDTO) {
            try {
                String name = requestDTO.getName();
                String numContrat = requestDTO.getNumContrat();
                String agency = requestDTO.getAgency();
                Date validityFrom = requestDTO.getValidityFrom();
                Date validityTo = requestDTO.getValidityTo();
                int carId = requestDTO.getCarId();

                Optional<Insurance> existingInsurance = Optional.ofNullable(insuranceRepository.findByNumContrat(numContrat));
                if (existingInsurance.isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new InsuranceDTO());
                }

                Optional<Car> optionalCar = carRepository.findById(carId);
                if (optionalCar.isEmpty()) {
                    return ResponseEntity.badRequest().body(new InsuranceDTO());
                }

                Car car = optionalCar.get();

                Insurance insurance = new Insurance();
                insurance.setName(name);
                insurance.setNumContrat(numContrat);
                insurance.setAgency(agency);
                insurance.setValidityFrom(validityFrom);
                insurance.setValidityTo(validityTo);
                insurance.setCar(car);
                car.setInsurance(insurance);
                carRepository.save(car);
                insuranceRepository.save(insurance);

                return ResponseEntity.ok(new InsuranceDTO());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InsuranceDTO());
            }
        }
    @PostMapping
    public ResponseEntity<Insurance> createInsurance(@RequestBody Insurance insurance) {
        Insurance createdInsurance = insuranceService.createInsurance(insurance);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInsurance);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getInsuranceById(@PathVariable int id) {
        Insurance insurance = insuranceService.getInsuranceById(id);
        return ResponseEntity.ok(insurance);
    }

    @GetMapping
    public ResponseEntity<List<Insurance>> getAllInsurances() {
        List<Insurance> insurances = insuranceService.getAllInsurances();
        return ResponseEntity.ok(insurances);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insurance> updateInsurance(@PathVariable int id, @RequestBody Insurance updatedInsurance) {
        Insurance insurance = insuranceService.updateInsurance(id, updatedInsurance);
        return ResponseEntity.ok(insurance);
    }

    @DeleteMapping("/{carId}")
    public void deleteInsuranceFromCar(@PathVariable int carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("Car not found"));

        if (car.getInsurance() != null) {
            Insurance insurance = car.getInsurance();
            car.setInsurance(null);
            carRepository.save(car);
            insuranceRepository.delete(insurance);
        }
    }

}

