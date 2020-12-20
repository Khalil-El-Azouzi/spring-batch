package ma.ensa.springbatch.batch;

import ma.ensa.springbatch.dao.CompteRepository;
import ma.ensa.springbatch.entities.Compte;
import ma.ensa.springbatch.entities.Transaction;
import ma.ensa.springbatch.entities.TransactionContainer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TransactionItemProcessor implements ItemProcessor<TransactionContainer, Transaction> {

    @Autowired
    private CompteRepository compteRepository;


    @Override
    public Transaction process(TransactionContainer transactionContainer) {

        Transaction transaction = new Transaction();
        transaction.setTransactionID(transactionContainer.getIdTransaction());
        transaction.setMontant(transactionContainer.getMontant());
        transaction.setDateTransaction(transactionContainer.getDateTransaction());
        transaction.setDateDebit(new Date());
        if (!compteRepository.findById(transactionContainer.getIdCompte()).isPresent()) {
            compteRepository.save(new Compte(transactionContainer.getIdCompte(), Math.random() * 5376, null));
        }
        transaction.setCompte(compteRepository.getOne(transactionContainer.getIdCompte()));

        return transaction;
    }

}
