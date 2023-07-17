package dpc.fr.back.api.service;

import dpc.fr.back.entity.Insurance;
import dpc.fr.back.repository.InsuranceRepository;
import dpc.fr.back.service.InsuranceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InsuranceServiceTest {
    @Mock
    private InsuranceRepository insuranceRepository;
    @InjectMocks
    private InsuranceService insuranceService;


    @BeforeEach void setUp()
    {
        Insurance insurance = Insurance.builder()
                .insuranceId(1)
                .agency("Star")
                .name("test")
                .validityTo(new Date())
                .validityFrom(new Date())
                .numContrat("555")
                .build();
    }

    @Test
    public void createInsuranceTest(){
        //
        Insurance insurance = Insurance.builder()
                .insuranceId(1)
                .agency("Star")
                .name("test")
                .validityTo(new Date())
                .validityFrom(new Date())
                .numContrat("555")
                .build();
        //
        insuranceService.createInsurance(insurance);
        verify(insuranceRepository).save(insurance);
        //
        Assertions.assertThat(insurance).isNotNull();
    }
    @Test
    public void getInsuranceByIdTest(){
        //
        Insurance insurance = Insurance.builder()
                .insuranceId(1)
                .agency("Star")
                .name("test")
                .validityTo(new Date())
                .validityFrom(new Date())
                .numContrat("555")
                .build();
        //
        when(insuranceRepository.findById(1)).thenReturn(Optional.of(insurance));
        //
        Insurance actualInsurance = insuranceService.getInsuranceById(1);
        //
        Assertions.assertThat(actualInsurance).isNotNull();
        Assertions.assertThat(actualInsurance).isEqualTo(insurance);
    }
    @Test
    public void getAllInsurancesTest(){
        //
        Insurance insurance = Insurance.builder()
                .insuranceId(1)
                .agency("Star")
                .name("test")
                .validityTo(new Date())
                .validityFrom(new Date())
                .numContrat("555")
                .build();
        Insurance insurance1 = Insurance.builder()
                .insuranceId(2)
                .agency("Star")
                .name("test")
                .validityTo(new Date())
                .validityFrom(new Date())
                .numContrat("555")
                .build();
        //
        given(insuranceRepository.findAll()).willReturn(List.of(insurance,insurance1));
        //
        List<Insurance> insurances = insuranceService.getAllInsurances();
        //
        Assertions.assertThat(insurances).isNotNull();
        Assertions.assertThat(insurances.size()).isEqualTo(2);
    }
}
