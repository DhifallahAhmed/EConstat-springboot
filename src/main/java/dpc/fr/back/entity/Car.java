package dpc.fr.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int carId;
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CarBrand carBrand;

    @Column(nullable = false)
    private String numSerie;

    @Column(nullable = false)
    private int fiscalPower;

    @Column(nullable = false)
    private String numImmatriculation;

    @ManyToOne
    private UserEntity owner;
    @JsonIgnore
    @OneToMany(mappedBy = "car",cascade = CascadeType.ALL)
    private List<CarDamage> carDamages;
    @JsonIgnore
    @OneToOne(mappedBy = "carr",cascade = CascadeType.ALL)
    private Insurance insurance;
    private String carImage;
}
