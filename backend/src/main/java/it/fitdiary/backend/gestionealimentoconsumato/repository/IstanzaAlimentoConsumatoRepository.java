package it.fitdiary.backend.gestionealimentoconsumato.repository;

import it.fitdiary.backend.entity.IstanzaAlimento;
import it.fitdiary.backend.entity.IstanzaAlimentoConsumato;
import it.fitdiary.backend.entity.IstanzaEsercizio;
import it.fitdiary.backend.entity.IstanzaEsercizioEseguito;
import it.fitdiary.backend.entity.Protocollo;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IstanzaAlimentoConsumatoRepository extends JpaRepository<IstanzaAlimentoConsumato, Long> {

    List<IstanzaAlimentoConsumato> findAllByProtocolloAndIstanzaAlimentoAndDataConsumazione(
        @NotNull(message = "Il protocollo non può essere nullo") Protocollo protocollo,
        @NotNull(message = "L'istanza dell'alimento non può essere nullo") IstanzaAlimento istanzaAlimento,
        @NotNull(message = "La data dell'esercizio non può essere nullo")  LocalDate dataConsumazione);
}
