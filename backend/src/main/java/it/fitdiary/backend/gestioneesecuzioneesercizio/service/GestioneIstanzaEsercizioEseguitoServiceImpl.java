package it.fitdiary.backend.gestioneesecuzioneesercizio.service;

import it.fitdiary.backend.entity.IstanzaEsercizio;
import it.fitdiary.backend.entity.IstanzaEsercizioEseguito;
import it.fitdiary.backend.entity.Protocollo;
import it.fitdiary.backend.gestioneesecuzioneesercizio.controller.dto.VisualizzaEserciziDTO;
import it.fitdiary.backend.gestioneesecuzioneesercizio.repository.IstanzaEsercizioEseguitoRepository;
import it.fitdiary.backend.gestioneprotocollo.repository.ProtocolloRepository;
import it.fitdiary.backend.gestioneschedaallenamento.repository.IstanzaEsercizioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GestioneIstanzaEsercizioEseguitoServiceImpl implements GestioneIstanzaEsercizioEseguitoService{


    public final IstanzaEsercizioEseguitoRepository instanzaEsercizioEseguitoRepository;
    public final ProtocolloRepository protocolloRepository;
    public final IstanzaEsercizioRepository istanzaEsercizioRepository;
    @Override
    public IstanzaEsercizioEseguito creazioneIstanzaEsercizio(Long idProtocollo, Long idIstanzaEsercizio, Float pesoEsecuzione, LocalDate data, int serie, int ripetizioni) {
        Optional<Protocollo> protocollo = protocolloRepository.findById(idProtocollo);
        Optional<IstanzaEsercizio> istanzaEsercizio = istanzaEsercizioRepository.findById(idIstanzaEsercizio);


        IstanzaEsercizioEseguito istanzaEsercizioEseguito = new IstanzaEsercizioEseguito();
        istanzaEsercizioEseguito.setDataEsecuzione(data);

        if(protocollo.isEmpty()){
            throw new IllegalStateException("uno delle istanze protocollo non può essere null");
        }
        if(istanzaEsercizio.isEmpty()){
            throw new IllegalStateException("uno delle istanze esercizio non può essere null");
        }

        istanzaEsercizioEseguito.setProtocollo(protocollo.get());
        istanzaEsercizioEseguito.setIstanzaEsercizio(istanzaEsercizio.get());
        istanzaEsercizioEseguito.setPesoEsecuzione(pesoEsecuzione);
        istanzaEsercizioEseguito.setDataEsecuzione(data);
        istanzaEsercizioEseguito.setNumeroSerie(serie);
        istanzaEsercizioEseguito.setRipetizioni(ripetizioni);
        return instanzaEsercizioEseguitoRepository.save(istanzaEsercizioEseguito);
    }

    @Override
    public VisualizzaEserciziDTO visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(Long idProtocollo,
                                                                                                  Long idIstanzaEsercizio) {
        Protocollo protocollo=new Protocollo(idProtocollo,null,null,null,null,null,null,null);
        IstanzaEsercizio istanza=new IstanzaEsercizio();
        istanza.setId(idIstanzaEsercizio);

        VisualizzaEserciziDTO visualizzaEserciziDTO = new VisualizzaEserciziDTO();

        List<IstanzaEsercizioEseguito> listaEserciziEseguiti = instanzaEsercizioEseguitoRepository.findAllByProtocolloAndIstanzaEsercizio(
              protocollo,
              istanza);
        visualizzaEserciziDTO.setListaEserciziEseguiti(listaEserciziEseguiti);
        Optional<IstanzaEsercizio> istanzaEsercizio = istanzaEsercizioRepository.findById(idIstanzaEsercizio);
        if(istanzaEsercizio.isPresent())
        {
            IstanzaEsercizio istanzaEsercizio1 = new IstanzaEsercizio();
            IstanzaEsercizio istanzaEsercizio2 = istanzaEsercizio.get();
            istanzaEsercizio1.setRipetizioni(istanzaEsercizio2.getRipetizioni());
            istanzaEsercizio1.setSerie(istanzaEsercizio2.getSerie());
            istanzaEsercizio1.setRecupero(istanzaEsercizio2.getRecupero());
            visualizzaEserciziDTO.setIstanzaEsercizio(istanzaEsercizio1);
        }

        return visualizzaEserciziDTO;
    }


}
