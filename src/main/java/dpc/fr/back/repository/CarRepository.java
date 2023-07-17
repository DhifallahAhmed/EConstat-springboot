package dpc.fr.back.repository;

import dpc.fr.back.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car,Integer> {
    public Car findByNumSerie(String numSerie);
    Optional<Car> findById(int carId);
}
