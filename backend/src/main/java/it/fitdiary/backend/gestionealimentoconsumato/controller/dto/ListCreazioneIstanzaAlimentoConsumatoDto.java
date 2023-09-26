package it.fitdiary.backend.gestionealimentoconsumato.controller.dto;

import java.util.List;

public class ListCreazioneIstanzaAlimentoConsumatoDto {

  private Long idProtocollo;
  private List<CreazioneIstanzaAlimentoConsumatoDto> listaAlimenti;
  public ListCreazioneIstanzaAlimentoConsumatoDto(Long idProtocollo,List<CreazioneIstanzaAlimentoConsumatoDto> list) {
    this.idProtocollo=idProtocollo;
    this.listaAlimenti =list;
  }

  public ListCreazioneIstanzaAlimentoConsumatoDto() {
  }

  public Long getIdProtocollo() {
    return idProtocollo;
  }

  public List<CreazioneIstanzaAlimentoConsumatoDto> getListaAlimenti() {
    return listaAlimenti;
  }
}
