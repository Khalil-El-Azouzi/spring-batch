package ma.ensa.springbatch.batch;

import ma.ensa.springbatch.dao.CompteRepository;
import ma.ensa.springbatch.dao.TransactionRepository;
import ma.ensa.springbatch.entities.Compte;
import ma.ensa.springbatch.entities.Transaction;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionItemWriter implements ItemWriter<Transaction> {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CompteRepository compteRepository;

    @Override
    public void write(List<? extends Transaction> list) {
        for (Transaction t: list){
            Compte cpt = compteRepository.getOne(t.getCompte().getAccountID());
            cpt.setSolde(cpt.getSolde() - t.getMontant());// on débit le compte
            compteRepository.save(cpt);//on fait la persistance
            transactionRepository.save(t);//on fait la persistance
            }
    }
}
