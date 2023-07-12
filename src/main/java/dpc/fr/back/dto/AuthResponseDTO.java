package dpc.fr.back.dto;

import dpc.fr.back.entity.UserEntity;
import lombok.Data;

@Data
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";
    private String fullname;
    public AuthResponseDTO(String fullname, String accessToken) {
        this.fullname = fullname;
        this.accessToken = accessToken;
    }
    public AuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getFullname(String fullname) {
        return fullname;
    }
}