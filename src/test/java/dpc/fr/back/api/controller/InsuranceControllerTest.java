package dpc.fr.back.api.controller;

import dpc.fr.back.controller.InsuranceController;
import dpc.fr.back.dto.InsuranceDTO;
import dpc.fr.back.dto.InsuranceRequestDTO;
import dpc.fr.back.entity.Car;
import dpc.fr.back.entity.Insurance;
import dpc.fr.back.repository.CarRepository;
import dpc.fr.back.repository.InsuranceRepository;
import dpc.fr.back.service.InsuranceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class InsuranceControllerTest {

    @Mock
    private InsuranceRepository insuranceRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private InsuranceService insuranceService;

    @InjectMocks
    private InsuranceController insuranceController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewInsurance_Success() {
        // Given
        InsuranceRequestDTO requestDTO = new InsuranceRequestDTO();
        Car car = new Car();
        car.setCarId(1);
        Optional<Car> optionalCar = Optional.of(car);
        when(carRepository.findById(anyInt())).thenReturn(optionalCar);

        Insurance insurance = new Insurance();
        when(insuranceRepository.findByNumContrat(anyString())).thenReturn(null);
        when(insuranceRepository.save(any(Insurance.class))).thenReturn(insurance);

        // When
        ResponseEntity<InsuranceDTO> responseEntity = insuranceController.addNewInsurance(requestDTO);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

//    @Test
//    public void testAddNewInsurance_Conflict() {
//        // Given
//        InsuranceRequestDTO requestDTO = new InsuranceRequestDTO();
//
//        Insurance existingInsurance = Insurance
//                .builder()
//                .insuranceId(1)
//                .agency("test")
//                .name("test")
//                .numContrat("55")
//                .validityFrom(new Date())
//                .validityTo(new Date())
//                .build();
//        when(insuranceRepository.findByNumContrat(anyString())).thenReturn(existingInsurance);
//
//        // When
//        ResponseEntity<InsuranceDTO> responseEntity = insuranceController.addNewInsurance(requestDTO);
//
//        // Then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
//        assertThat(responseEntity.getBody()).isNotNull();
//    }

    @Test
    public void testAddNewInsurance_CarNotFound() {
        // Given
        InsuranceRequestDTO requestDTO = new InsuranceRequestDTO();
        // Set properties for requestDTO, e.g., name, numContrat, agency, ...

        when(carRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When
        ResponseEntity<InsuranceDTO> responseEntity = insuranceController.addNewInsurance(requestDTO);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testAddNewInsurance_InternalServerError() {
        // Given
        InsuranceRequestDTO requestDTO = new InsuranceRequestDTO();
        // Set properties for requestDTO, e.g., name, numContrat, agency, ...

        when(carRepository.findById(anyInt())).thenReturn(Optional.of(new Car()));
        when(insuranceRepository.findByNumContrat(anyString())).thenReturn(null);
        when(insuranceRepository.save(any(Insurance.class))).thenThrow(new RuntimeException("Some error occurred"));

        // When
        ResponseEntity<InsuranceDTO> responseEntity = insuranceController.addNewInsurance(requestDTO);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testCreateInsurance_Success() {
        // Given
        Insurance insurance = new Insurance();
        // Set properties for insurance

        when(insuranceService.createInsurance(any(Insurance.class))).thenReturn(insurance);

        // When
        ResponseEntity<Insurance> responseEntity = insuranceController.createInsurance(insurance);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(insurance);
    }

    @Test
    public void testGetInsuranceById_Success() {
        // Given
        int insuranceId = 1;
        Insurance insurance = new Insurance();
        // Set properties for insurance

        when(insuranceService.getInsuranceById(insuranceId)).thenReturn(insurance);

        // When
        ResponseEntity<Insurance> responseEntity = insuranceController.getInsuranceById(insuranceId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(insurance);
    }

    @Test
    public void testGetAllInsurances_Success() {
        // Given
        List<Insurance> insurances = new ArrayList<>(); // Replace with some test Insurance objects

        when(insuranceService.getAllInsurances()).thenReturn(insurances);

        // When
        ResponseEntity<List<Insurance>> responseEntity = insuranceController.getAllInsurances();

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(insurances);
    }

    @Test
    public void testUpdateInsurance_Success() {
        // Given
        int insuranceId = 1;
        Insurance updatedInsurance = new Insurance();
        // Set properties for updatedInsurance

        when(insuranceService.updateInsurance(eq(insuranceId), any(Insurance.class))).thenReturn(updatedInsurance);

        // When
        ResponseEntity<Insurance> responseEntity = insuranceController.updateInsurance(insuranceId, updatedInsurance);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(updatedInsurance);
    }

    @Test
    public void testDeleteInsuranceFromCar_Success() {
        // Given
        int carId = 1;
        Car car = new Car();
        car.setInsurance(new Insurance());
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // When
        insuranceController.deleteInsuranceFromCar(carId);

        // Then
        verify(insuranceRepository, times(1)).delete(any(Insurance.class));
        verify(carRepository, times(1)).save(any(Car.class));
    }


    @Test
    public void testDeleteInsuranceFromCar_CarNotFound() {
        // Given
        int carId = 999; // Invalid car ID
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // When
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> insuranceController.deleteInsuranceFromCar(carId));

        // Then: Verify that delete and save methods were not called
        verify(insuranceRepository, never()).delete(any(Insurance.class));
        verify(carRepository, never()).save(any(Car.class));
    }

}
