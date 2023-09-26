package it.fitdiary.backend.gestioneesecuzioneesercizio.repository;

import it.fitdiary.backend.entity.IstanzaEsercizio;
import it.fitdiary.backend.entity.IstanzaEsercizioEseguito;
import it.fitdiary.backend.entity.Protocollo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IstanzaEsercizioEseguitoRepository extends JpaRepository<IstanzaEsercizioEseguito, Long> {

    List<IstanzaEsercizioEseguito> findAllByProtocolloAndIstanzaEsercizio(@NotNull(message = "Il protocollo non può essere nullo") Protocollo protocollo, @NotNull(message = "L'istanza dell'esercizio non può essere nullo") IstanzaEsercizio istanzaEsercizio);
}
