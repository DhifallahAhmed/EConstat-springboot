package dpc.fr.back.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CarDamage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int CarDamageId;

    @Column(nullable = false)
    private Boolean topLeft;

    @Column(nullable = false)
    private Boolean midLeft;

    @Column(nullable = false)
    private Boolean bottomLeft;

    @Column(nullable = false)
    private Boolean topRight;

    @Column(nullable = false)
    private Boolean midRight;

    @Column(nullable = false)
    private Boolean bottomRight;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;
}
