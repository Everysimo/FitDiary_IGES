package it.fitdiary.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class IstanzaEsercizioEseguito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * peso esecuzione.
     */
    @NotNull(message = "Il peso dell'esecuzione non può essere nullo")
    @Column(name = "peso_esecuzione")
    private int pesoEsecuzione;
    /**
     * peso esecuzione.
     */
    @NotNull(message = "Il numero della serie non può essere nullo")
    @Column(name = "numero_serie")
    private int numeroSerie;
    /**
     * ripetizioni esercizio.
     */
    @NotNull(message = "Il numero di ripetizioni non può essere nullo")
    @Column(name = "ripetizioni")
    private int ripetizioni;


    /**
     * data esecuzione esercizio.
     */
    @NotNull(message = "La data dell'esercizio non può essere nullo")
    @Column(name = "data_esecuzione")
    private LocalDate dataEsecuzione;

    /**
     * protocollo.
     */
    @NotNull(message = "Il protocollo non può essere nullo")
    @ManyToOne
    @JoinColumn(name = "protocollo_id")
    @EqualsAndHashCode.Exclude
    private Protocollo protocollo;


    /**
     * Istanza esercizio.
     */
    @NotNull(message = "L'istanza dell'esercizio non può essere nullo")
    @ManyToOne
    @JoinColumn(name = "istanzaEsercizio_id")
    @EqualsAndHashCode.Exclude
    private IstanzaEsercizio istanzaEsercizio;

}
