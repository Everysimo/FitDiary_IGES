package it.fitdiary.backend.gestioneesecuzioneesercizio.controller.dto;

import it.fitdiary.backend.entity.IstanzaEsercizio;
import it.fitdiary.backend.entity.IstanzaEsercizioEseguito;

import java.util.List;

public class VisualizzaEserciziDTO {
    private List<IstanzaEsercizioEseguito> listaEserciziEseguiti;
    private IstanzaEsercizio istanzaEsercizio;
    public VisualizzaEserciziDTO() {
    }

    public List<IstanzaEsercizioEseguito> getListaEserciziEseguiti() {
        return listaEserciziEseguiti;
    }

    public IstanzaEsercizio getIstanzaEsercizio() {
        return istanzaEsercizio;
    }

    public void setListaEserciziEseguiti(List<IstanzaEsercizioEseguito> listaEserciziEseguiti) {
        this.listaEserciziEseguiti = listaEserciziEseguiti;
    }

    public void setIstanzaEsercizio(IstanzaEsercizio istanzaEsercizio) {
        this.istanzaEsercizio = istanzaEsercizio;
    }
}
