package dpc.fr.back.api.controller;

import dpc.fr.back.controller.ConstatController;
import dpc.fr.back.dto.ConstatDto;
import dpc.fr.back.entity.Constat;
import dpc.fr.back.service.ConstatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ConstatControllerTest {

    @Mock
    private ConstatService constatService;

    @InjectMocks
    private ConstatController constatController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewConstat_Success() {
        // Given
        ConstatDto constatDto = new ConstatDto();

        Constat constat = new Constat();
        when(constatService.addNewConstat(constatDto)).thenReturn(ResponseEntity.ok(constat));

        // When
        ResponseEntity<Constat> responseEntity = constatController.addNewConstat(constatDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(constat);
    }

    @Test
    public void testGetConstat_Success() {
        // Given
        int idU = 123;

        String carBrand = "Toyota";

        ResponseEntity<String> expectedResponse = ResponseEntity.ok(carBrand);
        when(constatService.getConstat(idU)).thenReturn(expectedResponse);

        // When
        ResponseEntity<String> responseEntity = constatController.getConstat(idU);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(carBrand);
    }

    @Test
    public void testGetConstat_Unauthorized() {
        // Given
        int idU = 456;

        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur");
        when(constatService.getConstat(idU)).thenReturn(expectedResponse);

        // When
        ResponseEntity<String> responseEntity = constatController.getConstat(idU);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("Erreur");
    }
}
