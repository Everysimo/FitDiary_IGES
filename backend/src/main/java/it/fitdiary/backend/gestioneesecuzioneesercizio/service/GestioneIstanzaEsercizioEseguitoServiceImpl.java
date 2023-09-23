package it.fitdiary.backend.gestioneesecuzioneesercizio.service;

import it.fitdiary.backend.entity.IstanzaEsercizio;
import it.fitdiary.backend.entity.IstanzaEsercizioEseguito;
import it.fitdiary.backend.entity.Protocollo;
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
            throw new IllegalStateException("uno delle istanze alimento fa riferimento ad un Alimento insesistente");
        }
        if(istanzaEsercizio.isEmpty()){
            throw new IllegalStateException("uno delle istanze alimento fa riferimento ad un Alimento insesistente");
        }

        istanzaEsercizioEseguito.setProtocollo(protocollo.get());
        istanzaEsercizioEseguito.setIstanzaEsercizio(istanzaEsercizio.get());
        istanzaEsercizioEseguito.setDataEsecuzione(data);
        istanzaEsercizioEseguito.setNumeroSerie(serie);
        istanzaEsercizioEseguito.setRipetizioni(ripetizioni);
        return instanzaEsercizioEseguitoRepository.save(istanzaEsercizioEseguito);
    }

    @Override
    public List<IstanzaEsercizioEseguito> visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(Long idProtocollo,
                                                                                                           Long idIstanzaEsercizio) {
        Protocollo protocollo=new Protocollo(idProtocollo,null,null,null,null,null,null,null);
        IstanzaEsercizio istanza=new IstanzaEsercizio();
        istanza.setId(idIstanzaEsercizio);
        ArrayList<IstanzaEsercizioEseguito> istanzaEsercizioEseguiti = (ArrayList<IstanzaEsercizioEseguito>)
                instanzaEsercizioEseguitoRepository.findAllByProtocolloAndIstanzaEsercizio(
                        protocollo,
                        istanza);
        return istanzaEsercizioEseguiti;
    }


}
