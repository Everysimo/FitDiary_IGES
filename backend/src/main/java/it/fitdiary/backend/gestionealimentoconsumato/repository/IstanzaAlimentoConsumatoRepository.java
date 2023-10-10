package it.fitdiary.backend.gestionealimentoconsumato.repository;

import it.fitdiary.backend.entity.IstanzaAlimentoConsumato;
import it.fitdiary.backend.entity.Protocollo;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IstanzaAlimentoConsumatoRepository extends JpaRepository<IstanzaAlimentoConsumato, Long> {

    List<IstanzaAlimentoConsumato> findAllByProtocolloAndDataConsumazione(
        @NotNull(message = "Il protocollo non può essere nullo") Protocollo protocollo,
        @NotNull(message = "La data dell'esercizio non può essere nullo")  LocalDate dataConsumazione);

    void deleteAllByProtocolloAndDataConsumazione(
        @NotNull(message = "Il protocollo non può essere nullo") Protocollo protocollo,
        @NotNull(message = "La data dell'esercizio non può essere nullo") LocalDate dataConsumazione);
}
