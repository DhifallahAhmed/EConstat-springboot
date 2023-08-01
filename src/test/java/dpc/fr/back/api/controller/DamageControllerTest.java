package dpc.fr.back.api.controller;

import dpc.fr.back.controller.DamageController;
import dpc.fr.back.dto.CarDamageDto;
import dpc.fr.back.entity.Car;
import dpc.fr.back.entity.CarBrand;
import dpc.fr.back.entity.CarDamage;
import dpc.fr.back.repository.CarRepository;
import dpc.fr.back.repository.DamageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DamageControllerTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private DamageRepository damageRepository;

    @InjectMocks
    private DamageController damageController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewCarDamage_Success() {
        // Given
        int carId = 1;

        Car car = Car.builder()
                .carId(1)
                .carBrand(CarBrand.Skoda)
                .fiscalPower(55)
                .type("test")
                .numImmatriculation("465465")
                .numSerie("445445")
                .build();
        CarDamageDto carDamageDto = CarDamageDto.builder()
                .bottomRight(true)
                .midRight(false)
                .topRight(false)
                .midLeft(false)
                .bottomLeft(true)
                .topLeft(false)
                .car(car)
                .build();
        when(carRepository.findById(carId)).thenReturn(java.util.Optional.of(car));

        CarDamage savedCarDamage = CarDamage.builder()
                .CarDamageId(1)
                .bottomRight(true)
                .midRight(false)
                .topRight(false)
                .midLeft(false)
                .bottomLeft(true)
                .topLeft(false)
                .car(car)
                .build();

        when(damageRepository.save(any(CarDamage.class))).thenReturn(savedCarDamage);

        // When
        ResponseEntity<CarDamage> responseEntity = damageController.addNewCarDamage(carDamageDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(savedCarDamage);
    }

    @Test
    public void testAddNewCarDamage_CarNotFound() {
        // Given
        int carId = 999; // Invalid car ID
        CarDamageDto carDamageDto = CarDamageDto.builder()
                .bottomRight(true)
                .midRight(false)
                .topRight(false)
                .midLeft(false)
                .bottomLeft(true)
                .topLeft(false)
                .build();
        // Set carDamageDto properties accordingly, e.g., car, topLeft, midLeft, ...

        when(carRepository.findById(carId)).thenReturn(java.util.Optional.empty());

        // When
        ResponseEntity<CarDamage> responseEntity = damageController.addNewCarDamage(carDamageDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testAddNewCarDamage_Exception() {
        // Given
        int carId = 1;
        CarDamageDto carDamageDto = CarDamageDto.builder()
                .bottomRight(true)
                .midRight(false)
                .topRight(false)
                .midLeft(false)
                .bottomLeft(true)
                .topLeft(false)
                .build();
        // Set carDamageDto properties accordingly, e.g., car, topLeft, midLeft, ...

        Car car = new Car();
        car.setCarId(carId);

        when(carRepository.findById(carId)).thenReturn(java.util.Optional.of(car));

        // Simulate an exception during saving
        when(damageRepository.save(any(CarDamage.class))).thenThrow(new RuntimeException("Some error occurred"));

        // When
        ResponseEntity<CarDamage> responseEntity = damageController.addNewCarDamage(carDamageDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNull();
    }
}
