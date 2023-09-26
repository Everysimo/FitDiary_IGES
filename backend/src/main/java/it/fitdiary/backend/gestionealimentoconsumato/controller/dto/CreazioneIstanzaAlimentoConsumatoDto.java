package it.fitdiary.backend.gestionealimentoconsumato.controller.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class CreazioneIstanzaAlimentoConsumatoDto {

  public CreazioneIstanzaAlimentoConsumatoDto(Long istanzaAlimentoId, int grammi,
                                              LocalDate data) {
    this.istanzaAlimentoId = istanzaAlimentoId;
    this.grammi = grammi;
    this.data = data;
  }

  public CreazioneIstanzaAlimentoConsumatoDto() {
  }


  private Long istanzaAlimentoId;

  private int grammi;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate data;


  public Long getIstanzaAlimentoId() {
    return istanzaAlimentoId;
  }

  public void setIstanzaAlimentoId(Long istanzaAlimentoId) {
    this.istanzaAlimentoId = istanzaAlimentoId;
  }

  public int getGrammi() {
    return grammi;
  }

  public void setGrammi(int grammi) {
    this.grammi = grammi;
  }

  public LocalDate getData() {
    return data;
  }

  public void setData(LocalDate data) {
    this.data = data;
  }
}
