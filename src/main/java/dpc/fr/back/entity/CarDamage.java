package dpc.fr.back.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
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
