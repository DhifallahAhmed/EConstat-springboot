package dpc.fr.back.api.service;

import dpc.fr.back.dto.ConstatDto;
import dpc.fr.back.entity.*;
import dpc.fr.back.repository.*;
import dpc.fr.back.service.ConstatService;
import dpc.fr.back.service.InsuranceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConstatServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private InsuranceRepository insuranceRepository;

    @Mock
    private DamageRepository carDamageRepository;

    @Mock
    private ConstatRepository constatRepository;

    @InjectMocks
    private ConstatService constatService;

    @Test
    public void testGetConstat_Success() {
        UserEntity userA = UserEntity.builder()
                .userId(1)
                .build();
        UserEntity userB = UserEntity.builder()
                .userId(1)
                .build();
        Car carB = Car.builder()
                .carId(2)
                .type("test")
                .carBrand(CarBrand.Skoda)
                .numSerie("test")
                .fiscalPower(111)
                .numImmatriculation("test")
                .build();
        Car carA = Car.builder()
                .carId(1)
                .type("test")
                .carBrand(CarBrand.Skoda)
                .numSerie("test")
                .fiscalPower(111)
                .numImmatriculation("test")
                .build();
        Constat constat = Constat.builder()
                .ConstatId(1)
                .carA(carA)
                .carB(carB)
                .carDamageA(new CarDamage())
                .carDamageB(new CarDamage())
                .userA(userA)
                .userB(userB)
                .insuranceA(new Insurance())
                .insuranceB(new Insurance())
                .build();

        // Set up the mock behavior for constatRepository
        int userId = 1;
        when(constatRepository.findByUserA_UserId(userId)).thenReturn(constat);

        // Set up the mock behavior for carRepository
        int carBId = 2;
        when(carRepository.findById(carBId)).thenReturn(Optional.of(carB));

        // Call the method to be tested
        ResponseEntity<String> response = constatService.getConstat(userId);

        // Assertions
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(String.valueOf(carB.getCarBrand()));
    }

    @Test
    public void testGetConstat_ConstatNotFound() {
        // Set up the mock behavior for constatRepository to return null
        int userId = 123;
        when(constatRepository.findByUserA_UserId(userId)).thenReturn(null);

        // Call the method to be tested
        ResponseEntity<String> response = constatService.getConstat(userId);

        // Assertions
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isEqualTo("Erreur");
    }

    @Test
    public void testGetConstat_CarBNotFound() {
        UserEntity userA = UserEntity.builder()
                .userId(1)
                .build();
        UserEntity userB = UserEntity.builder()
                .userId(1)
                .build();
        Car carB = Car.builder()
                .carId(2)
                .type("test")
                .carBrand(CarBrand.Skoda)
                .numSerie("test")
                .fiscalPower(111)
                .numImmatriculation("test")
                .build();
        Car carA = Car.builder()
                .carId(1)
                .type("test")
                .carBrand(CarBrand.Skoda)
                .numSerie("test")
                .fiscalPower(111)
                .numImmatriculation("test")
                .build();
        Constat constat = Constat.builder()
                .ConstatId(1)
                .carA(carA)
                .carB(carB)
                .carDamageA(new CarDamage())
                .carDamageB(new CarDamage())
                .userA(userA)
                .userB(userB)
                .insuranceA(new Insurance())
                .insuranceB(new Insurance())
                .build();
        // Create a mock Constat object for testing


        // Set up the mock behavior for constatRepository
        int userId = 1;
        when(constatRepository.findByUserA_UserId(userId)).thenReturn(constat);

        // Set up the mock behavior for carRepository to return null
        int carBId = 2;
        when(carRepository.findById(carBId)).thenReturn(Optional.empty());

        // Call the method to be tested and expect an IllegalArgumentException
        assertThatThrownBy(() -> constatService.getConstat(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Car B not found");
    }
}
