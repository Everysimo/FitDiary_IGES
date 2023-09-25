package it.fitdiary.backend.gestionealimentoconsumato.service;

import it.fitdiary.backend.entity.IstanzaAlimentoConsumato;
import java.time.LocalDate;
import java.util.List;

public interface GestioneIstanzaAlimentoConsumatoService {
  IstanzaAlimentoConsumato creazioneIstanzaAlimentoConsumato(Long protocolloId, Long istanzaAlimentoId, int grammi, LocalDate data);

  List<IstanzaAlimentoConsumato> visualizzaIstanzaAlimentiConsumatiByProtocolloAndIstanzaAlimentoAndDate(
      Long idProtocollo, Long idIstanzaEsercizio,LocalDate dataConsumazione);
}
