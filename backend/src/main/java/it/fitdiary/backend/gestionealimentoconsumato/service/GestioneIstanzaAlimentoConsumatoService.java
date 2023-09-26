package it.fitdiary.backend.gestionealimentoconsumato.service;

import it.fitdiary.backend.entity.IstanzaAlimentoConsumato;
import it.fitdiary.backend.gestionealimentoconsumato.controller.dto.CreazioneIstanzaAlimentoConsumatoDto;

import java.time.LocalDate;
import java.util.List;

public interface GestioneIstanzaAlimentoConsumatoService {
  IstanzaAlimentoConsumato creazioneIstanzaAlimentoConsumato(Long protocolloId, Long istanzaAlimentoId, int grammi, LocalDate data);

  List<IstanzaAlimentoConsumato> creazioneIstanzeEsercizio(Long protocolloId,List<CreazioneIstanzaAlimentoConsumatoDto> list);

  List<IstanzaAlimentoConsumato> visualizzaIstanzaAlimentiConsumatiByProtocolloAndDate(
      Long idProtocollo,LocalDate dataConsumazione);
}
