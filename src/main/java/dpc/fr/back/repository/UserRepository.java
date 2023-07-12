package dpc.fr.back.repository;

import dpc.fr.back.entity.Car;
import dpc.fr.back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    UserEntity findByEmail(String email);
    Optional<UserEntity> findById(int userId);

}
