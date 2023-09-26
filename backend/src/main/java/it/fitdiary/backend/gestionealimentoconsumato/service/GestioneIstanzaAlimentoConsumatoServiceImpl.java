package it.fitdiary.backend.gestionealimentoconsumato.service;

import it.fitdiary.backend.entity.IstanzaAlimento;
import it.fitdiary.backend.entity.IstanzaAlimentoConsumato;
import it.fitdiary.backend.entity.IstanzaEsercizioEseguito;
import it.fitdiary.backend.entity.Protocollo;
import it.fitdiary.backend.gestionealimentoconsumato.controller.dto.CreazioneIstanzaAlimentoConsumatoDto;
import it.fitdiary.backend.gestionealimentoconsumato.repository.IstanzaAlimentoConsumatoRepository;
import it.fitdiary.backend.gestioneprotocollo.repository.ProtocolloRepository;
import it.fitdiary.backend.gestioneschedaalimentare.repository.IstanzaAlimentoRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GestioneIstanzaAlimentoConsumatoServiceImpl
    implements GestioneIstanzaAlimentoConsumatoService {

  public final ProtocolloRepository protocolloRepository;
  public final IstanzaAlimentoRepository istanzaAlimentoRepository;
  public final IstanzaAlimentoConsumatoRepository istanzaAlimentoConsumatoRepository;


  @Override
  public IstanzaAlimentoConsumato creazioneIstanzaAlimentoConsumato(Long protocolloId,
                                                                    Long istanzaAlimentoId,
                                                                    int grammi, LocalDate data) {
    Optional<Protocollo> protocollo = protocolloRepository.findById(protocolloId);
    Optional<IstanzaAlimento> istanzaAlimento = istanzaAlimentoRepository.findById(istanzaAlimentoId);


    IstanzaAlimentoConsumato istanzaAlimentoConsumato = new IstanzaAlimentoConsumato();
    istanzaAlimentoConsumato.setDataConsumazione(data);

    if(protocollo.isEmpty()){
      throw new IllegalStateException("l'id fa riferimento ad un protocollo non esistente");
    }
    if(istanzaAlimento.isEmpty()){
      throw new IllegalStateException("l'id fa riferimento ad un istanza alimento non esistente");
    }

    istanzaAlimentoConsumato.setProtocollo(protocollo.get());
    istanzaAlimentoConsumato.setIstanzaAlimento(istanzaAlimento.get());
    istanzaAlimentoConsumato.setGrammiConsumati(grammi);
    return istanzaAlimentoConsumatoRepository.save(istanzaAlimentoConsumato);
  }

  @Override
  public List<IstanzaAlimentoConsumato> creazioneIstanzeEsercizio(Long protocolloId,List<CreazioneIstanzaAlimentoConsumatoDto> list)
  {
    List<IstanzaAlimentoConsumato> istanzaAlimentoConsumatoList=new ArrayList<>();

    for(CreazioneIstanzaAlimentoConsumatoDto elem:list)
    {
      Optional<Protocollo> protocollo = protocolloRepository.findById(protocolloId);
      Optional<IstanzaAlimento> istanzaAlimento = istanzaAlimentoRepository.findById(elem.getIstanzaAlimentoId());


      IstanzaAlimentoConsumato istanzaAlimentoConsumato = new IstanzaAlimentoConsumato();
      istanzaAlimentoConsumato.setDataConsumazione(elem.getData());

      if(protocollo.isEmpty()){
        throw new IllegalStateException("l'id fa riferimento ad un protocollo non esistente");
      }
      if(istanzaAlimento.isEmpty()){
        throw new IllegalStateException("l'id fa riferimento ad un istanza alimento non esistente");
      }

      istanzaAlimentoConsumato.setProtocollo(protocollo.get());
      istanzaAlimentoConsumato.setIstanzaAlimento(istanzaAlimento.get());
      istanzaAlimentoConsumato.setGrammiConsumati(elem.getGrammi());
      istanzaAlimentoConsumatoList.add(istanzaAlimentoConsumato);
      istanzaAlimentoConsumatoRepository.save(istanzaAlimentoConsumato);
    }

    return istanzaAlimentoConsumatoList;
  }

  @Override
  public List<IstanzaAlimentoConsumato> visualizzaIstanzaAlimentiConsumatiByProtocolloAndDate(
      Long idProtocollo,LocalDate dataConsumazione) {
    Protocollo protocollo = new Protocollo();
    protocollo.setId(idProtocollo);
    return istanzaAlimentoConsumatoRepository.findAllByProtocolloAndDataConsumazione(protocollo,dataConsumazione);
  }
}
