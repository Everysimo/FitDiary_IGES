package it.fitdiary.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class IstanzaAlimentoConsumato {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * peso esecuzione.
   */
  @NotNull(message = "I grammi consumati dell'alimento")
  @Column(name = "grammi_consumati")
  private int grammiConsumati;

  /**
   * data esecuzione esercizio.
   */
  @NotNull(message = "La data dell'esercizio non può essere nullo")
  @Column(name = "data_esecuzione")
  private LocalDate dataConsumazione;

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
  @NotNull(message = "L'istanza dell'alimento non può essere nullo")
  @ManyToOne
  @JoinColumn(name = "istanzaAlimento")
  @EqualsAndHashCode.Exclude
  private IstanzaAlimento istanzaAlimento;

}
