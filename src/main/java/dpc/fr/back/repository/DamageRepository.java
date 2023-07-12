package dpc.fr.back.repository;

import dpc.fr.back.entity.CarDamage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DamageRepository extends JpaRepository<CarDamage,Integer> {
}
