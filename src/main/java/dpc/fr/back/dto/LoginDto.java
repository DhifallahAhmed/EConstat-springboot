package dpc.fr.back.dto;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class LoginDto {
    private String username;
    private String password;
}
