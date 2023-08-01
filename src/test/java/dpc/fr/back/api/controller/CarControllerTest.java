package dpc.fr.back.api.controller;

import dpc.fr.back.controller.CarController;
import dpc.fr.back.entity.Car;
import dpc.fr.back.entity.CarBrand;
import dpc.fr.back.entity.Insurance;
import dpc.fr.back.entity.UserEntity;
import dpc.fr.back.repository.CarRepository;
import dpc.fr.back.repository.InsuranceRepository;
import dpc.fr.back.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CarControllerTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InsuranceRepository insuranceRepository;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllcars() {
        // Given
        List<Car> cars = new ArrayList<>(); // Replace with some test car objects
        when(carRepository.findAll()).thenReturn(cars);

        // When
        List<Car> result = carController.findAllcars();

        // Then
        assertThat(result).isEqualTo(cars);
    }

    @Test
    public void testFindCarBynumSerie_Success() {
        // Given
        String numSerie = "123456";
        Car car = new Car(); // Replace with a test car object
        car.setNumSerie(numSerie);
        Insurance insurance = new Insurance(); // Replace with a test insurance object
        car.setInsurance(insurance);

        when(carRepository.findByNumSerie(numSerie)).thenReturn(car);

        // When
        Insurance result = carController.findCarBynumSerie(numSerie);

        // Then
        assertThat(result).isEqualTo(insurance);
    }

    @Test
    public void testFindCarBynumSerie_NotFound() {
        // Given
        String numSerie = "invalid_numSerie";
        when(carRepository.findByNumSerie(numSerie)).thenReturn(null);

        // When
        Insurance result = carController.findCarBynumSerie(numSerie);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void testAddNewCar_Success() {
        // Given
        int userId = 1;
        Car car = Car.builder()
                .carId(1)
                .carBrand(CarBrand.Skoda)
                .fiscalPower(55)
                .type("test")
                .numImmatriculation("465465")
                .numSerie("445445")
                .build();

        UserEntity user = new UserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(carRepository.findByNumSerie(car.getNumSerie())).thenReturn(null);
        when(carRepository.save(any(Car.class))).thenReturn(car);

        // When
        ResponseEntity<?> responseEntity = carController.addNewCar(userId, car);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(responseEntity.getBody()).isEqualTo("Car is added");
        assertThat(car.getOwner()).isEqualTo(user);
    }

    @Test
    public void testAddNewCar_UserNotFound() {
        // Given
        int userId = 999;
        Car car = Car.builder()
                .carId(1)
                .carBrand(CarBrand.Skoda)
                .fiscalPower(55)
                .type("test")
                .numImmatriculation("465465")
                .numSerie("445445")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> responseEntity = carController.addNewCar(userId, car);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testAddNewCar_CarAlreadyExists() {
        // Given
        int userId = 1;
        Car car = Car.builder()
                .carId(1)
                .carBrand(CarBrand.Skoda)
                .fiscalPower(55)
                .type("test")
                .numImmatriculation("465465")
                .numSerie("445445")
                .build();

        UserEntity user = new UserEntity(); // Replace with a test user object
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(carRepository.findByNumSerie(car.getNumSerie())).thenReturn(car);

        // When
        ResponseEntity<?> responseEntity = carController.addNewCar(userId, car);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void testRemoveCar_Success() {
        // Given
        int carId = 1;
        Car car = new Car(); // Replace with a test car object

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // When
        ResponseEntity<String> responseEntity = carController.removeCar(carId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Car deleted from user's list");
        verify(carRepository, times(1)).delete(car);
    }

    @Test
    public void testRemoveCar_NotFound() {
        // Given
        int carId = 999; // Invalid car ID

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<String> responseEntity = carController.removeCar(carId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("Car not found in user's list");
    }

    @Test
    public void testRemoveCar_InternalServerError() {
        // Given
        int carId = 1;

        when(carRepository.findById(carId)).thenThrow(new RuntimeException("Some error occurred"));

        // When
        ResponseEntity<String> responseEntity = carController.removeCar(carId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

    }}
