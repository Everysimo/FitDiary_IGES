package it.fitdiary.backend.gestioneesecuzioneesercizio.controller.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class IstanzaEsercizioEseguitoDTO {


  public IstanzaEsercizioEseguitoDTO() {
  }

  public IstanzaEsercizioEseguitoDTO(LocalDate data, Long idProtocollo, Long idIstanzaEsercizio,
                                     Float pesoEsecuzione, int serie, int ripetizioni) {
    this.data = data;
    this.idProtocollo = idProtocollo;
    this.idIstanzaEsercizio = idIstanzaEsercizio;
    this.pesoEsecuzione = pesoEsecuzione;
    this.serie = serie;
    this.ripetizioni = ripetizioni;
  }

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate data;
  private Long idProtocollo;
  private Long idIstanzaEsercizio;
  private Float pesoEsecuzione;

  private int serie;
  private int ripetizioni;

  public LocalDate getData() {
    return data;
  }

  public void setData(LocalDate data) {
    this.data = data;
  }

  public Long getIdProtocollo() {
    return idProtocollo;
  }

  public void setIdProtocollo(Long idProtocollo) {
    this.idProtocollo = idProtocollo;
  }

  public Long getIdIstanzaEsercizio() {
    return idIstanzaEsercizio;
  }

  public void setIdIstanzaEsercizio(Long idIstanzaEsercizio) {
    this.idIstanzaEsercizio = idIstanzaEsercizio;
  }

  public Float getPesoEsecuzione() {
    return pesoEsecuzione;
  }

  public void setPesoEsecuzione(Float pesoEsecuzione) {
    this.pesoEsecuzione = pesoEsecuzione;
  }

  public int getSerie() {
    return serie;
  }

  public void setSerie(int serie) {
    this.serie = serie;
  }

  public int getRipetizioni() {
    return ripetizioni;
  }

  public void setRipetizioni(int ripetizioni) {
    this.ripetizioni = ripetizioni;
  }
}
