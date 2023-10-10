package it.fitdiary.backend.gestionealimentoconsumato.controller.dto;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class ListCreazioneIstanzaAlimentoConsumatoDto {

  private Long idProtocollo;
  private List<CreazioneIstanzaAlimentoConsumatoDto> listaAlimenti;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate data;

  public ListCreazioneIstanzaAlimentoConsumatoDto(Long idProtocollo,
                                                  List<CreazioneIstanzaAlimentoConsumatoDto> listaAlimenti,
                                                  LocalDate data) {
    this.idProtocollo = idProtocollo;
    this.listaAlimenti = listaAlimenti;
    this.data = data;
  }

  public ListCreazioneIstanzaAlimentoConsumatoDto() {
  }

  public Long getIdProtocollo() {
    return idProtocollo;
  }

  public List<CreazioneIstanzaAlimentoConsumatoDto> getListaAlimenti() {
    return listaAlimenti;
  }

  public LocalDate getData() {
    return data;
  }
}
