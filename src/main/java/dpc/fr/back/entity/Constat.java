package dpc.fr.back.entity;

import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Constat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ConstatId;

    @ManyToOne
    @JoinColumn(name = "user_a_id")
    private UserEntity userA;

    @ManyToOne
    @JoinColumn(name = "car_a_id")
    private Car carA;

    @ManyToOne
    @JoinColumn(name = "insurance_a_id")
    private Insurance insuranceA;

    @ManyToOne
    @JoinColumn(name = "user_b_id")
    private UserEntity userB;

    @ManyToOne
    @JoinColumn(name = "car_b_id")
    private Car carB;

    @ManyToOne
    @JoinColumn(name = "insurance_b_id")
    private Insurance insuranceB;

    @ManyToOne
    @JoinColumn(name = "car_damage_a_id")
    private CarDamage carDamageA;

    @ManyToOne
    @JoinColumn(name = "car_damage_b_id")
    private CarDamage carDamageB;
}
