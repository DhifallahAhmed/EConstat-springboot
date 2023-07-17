package dpc.fr.back.repository;

import dpc.fr.back.entity.Constat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstatRepository extends JpaRepository<Constat,Integer> {
    Constat findByUserA_UserId(int id);

}
