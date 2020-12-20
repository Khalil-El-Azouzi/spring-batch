package ma.ensa.springbatch.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class TransactionContainer {

    private Long idTransaction;
    private long idCompte;
    private double montant;
    private Date dateTransaction;

}
