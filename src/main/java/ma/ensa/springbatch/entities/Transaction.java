package ma.ensa.springbatch.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Transaction {
    @Id
    private Long transactionID;
    private double montant;
    private Date dateTransaction;
    private Date dateDebit;

    @ManyToOne
    private Compte compte;
}
