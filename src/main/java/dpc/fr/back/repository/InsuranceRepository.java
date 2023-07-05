package dpc.fr.back.repository;

import dpc.fr.back.entity.Car;
import dpc.fr.back.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance,Integer> {
    public Insurance findByNumContrat(String numContrat);
}
