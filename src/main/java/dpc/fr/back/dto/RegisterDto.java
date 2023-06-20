package dpc.fr.back.dto;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class RegisterDto {
    private String username;
    private String password;
    private String fullName;
    private String address;
    private String driverLicense;
    @Temporal(TemporalType.DATE)
    private Date deliveredOn;
    private int number;
    private String email;
}
