package it.fitdiary.backend.gestioneesecuzioneesercizio.service;

import it.fitdiary.backend.entity.IstanzaEsercizioEseguito;
import it.fitdiary.backend.gestionealimentoconsumato.controller.dto.CreazioneIstanzaAlimentoConsumatoDto;
import it.fitdiary.backend.gestioneesecuzioneesercizio.repository.IstanzaEsercizioEseguitoRepository;
import it.fitdiary.backend.gestioneprotocollo.repository.ProtocolloRepository;
import it.fitdiary.backend.gestioneschedaallenamento.repository.IstanzaEsercizioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface GestioneIstanzaEsercizioEseguitoService {


    IstanzaEsercizioEseguito creazioneIstanzaEsercizio(Long idProtocollo, Long idIstanzaEsercizio, Float pesoEsecuzione, LocalDate data, int serie, int ripetizioni);

    List<IstanzaEsercizioEseguito> visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(Long idProtocollo,
                                                                                                    Long idIstanzaEsercizio);
}
