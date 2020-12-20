package ma.ensa.springbatch.dao;

import ma.ensa.springbatch.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
