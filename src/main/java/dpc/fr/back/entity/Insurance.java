package dpc.fr.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int insuranceId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String numContrat;

    @Column(nullable = false)
    private String agency;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date validityFrom;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date validityTo;

    @Column(nullable = false)
    private String image;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car carr;
}
