package ma.ensa.springbatch.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Compte {
    @Id
    private Long accountID;
    private double solde;

    @OneToMany(mappedBy = "compte")
    private List<Transaction> transactions;
}
