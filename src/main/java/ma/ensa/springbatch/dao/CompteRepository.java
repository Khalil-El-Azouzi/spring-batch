package ma.ensa.springbatch.dao;

import ma.ensa.springbatch.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<Compte, Long> {

}
