package it.fitdiary.backend.gestionealimentoconsumato.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import it.fitdiary.backend.entity.IstanzaAlimento;
import it.fitdiary.backend.entity.IstanzaAlimentoConsumato;
import it.fitdiary.backend.entity.Protocollo;
import it.fitdiary.backend.entity.enums.GIORNO_SETTIMANA;
import it.fitdiary.backend.gestionealimentoconsumato.controller.dto.ListCreazioneIstanzaAlimentoConsumatoDto;
import it.fitdiary.backend.gestionealimentoconsumato.service.GestioneIstanzaAlimentoConsumatoService;
import it.fitdiary.backend.gestioneprotocollo.service.GestioneProtocolloService;
import it.fitdiary.backend.gestioneschedaalimentare.service.GestioneSchedaAlimentareService;
import it.fitdiary.backend.gestioneschedaallenamento.service.GestioneSchedaAllenamentoService;
import it.fitdiary.backend.utility.ResponseHandler;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@Slf4j
@RequestMapping(path = "api/v1/istanzaAlimentiConsumati")
public class GestionIstanzaAlimentiConsumatiController {

  public final GestioneIstanzaAlimentoConsumatoService gestioneIstanzaAlimentoConsumatoService;

  private final GestioneProtocolloService gestioneProtocolloService;

  private final GestioneSchedaAlimentareService schedaAlimentareService;
  @Autowired
  public GestionIstanzaAlimentiConsumatiController(
          GestioneIstanzaAlimentoConsumatoService gestioneIstanzaAlimentoConsumatoService,
          GestioneProtocolloService gestioneProtocolloService, GestioneSchedaAlimentareService schedaAlimentareService){
    this.gestioneIstanzaAlimentoConsumatoService = gestioneIstanzaAlimentoConsumatoService;
    this.gestioneProtocolloService = gestioneProtocolloService;
    this.schedaAlimentareService = schedaAlimentareService;
  }




  @PostMapping("creaIstanzaAlimentoConsumato")
  private ResponseEntity<Object> creazioneIstanzaAlimentoConsumato(@RequestBody
                                                                   ListCreazioneIstanzaAlimentoConsumatoDto creazioneIstanzaAlimentoConsumatoDto) {

    HttpServletRequest request = ((ServletRequestAttributes)
        RequestContextHolder.getRequestAttributes()).getRequest();

    Long idCliente = Long.parseLong(
        request.getUserPrincipal().getName());
    Protocollo protocollo = gestioneProtocolloService.getByIdProtocollo(creazioneIstanzaAlimentoConsumatoDto.getIdProtocollo());
    if(!Objects.equals(protocollo.getCliente().getId(), idCliente)){
      return ResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED,
          "Il cliente non può creare un'istanza di alimento consumato per questo "
              + "protocollo poichè non gli appartiene");
    }

    try{
      List<IstanzaAlimentoConsumato> newIstanzaAlimentoConsumato = gestioneIstanzaAlimentoConsumatoService.
              creazioneIstanzeEsercizio(creazioneIstanzaAlimentoConsumatoDto.getIdProtocollo(),creazioneIstanzaAlimentoConsumatoDto.getListaAlimenti());
      return ResponseHandler.generateResponse(HttpStatus.CREATED,
          "istanzaAlimentoConsumato", newIstanzaAlimentoConsumato);
    } catch (IllegalArgumentException e) {
      return ResponseHandler.generateResponse(BAD_REQUEST,
          (Object) e.getMessage());
    }catch (Exception e)
    {
      System.out.println(e);
      return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
          (Object) e.getMessage());
    }
  }

  @GetMapping("visualizzaIstanzeAlimentoConsumato")
  private ResponseEntity<Object> visualizzaIstanzaAlimentiConsumatiByProtocolloAndIstanzaAlimentoAndDate(
      @RequestParam("idProtocollo") final Long idProtocollo,
      @RequestParam("dataConsumazione") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataConsumazione) {

    try{
      List<IstanzaAlimentoConsumato> istanzaEsercizioEseguitoList = gestioneIstanzaAlimentoConsumatoService.
          visualizzaIstanzaAlimentiConsumatiByProtocolloAndDate(idProtocollo,dataConsumazione);

      float somma=0;
      float calPrev=0;

      GIORNO_SETTIMANA giornoSettimana = switch (dataConsumazione.getDayOfWeek())
      {
          case MONDAY -> GIORNO_SETTIMANA.LUNEDI;
          case TUESDAY -> GIORNO_SETTIMANA.MARTEDI;
          case WEDNESDAY -> GIORNO_SETTIMANA.MERCOLEDI;
          case THURSDAY -> GIORNO_SETTIMANA.GIOVEDI;
          case FRIDAY -> GIORNO_SETTIMANA.VENERDI;
          case SATURDAY -> GIORNO_SETTIMANA.SABATO;
          case SUNDAY -> GIORNO_SETTIMANA.DOMENICA;
      };

        //Calcolo calorie del giorno
      Protocollo protocollo=gestioneProtocolloService.getByIdProtocollo(idProtocollo);
      List<IstanzaAlimento> listAlimentiGiorno=protocollo.getSchedaAlimentare().getListaAlimenti().stream().filter(al->al.getGiornoDellaSettimana() == giornoSettimana).toList();
      for(IstanzaAlimento al:listAlimentiGiorno)
      {
        float calAl=(al.getAlimento().getKcal()/100)*al.getGrammi();
        calPrev+=calAl;
      }

      for(IstanzaAlimentoConsumato al:istanzaEsercizioEseguitoList)
      {
        float calIstanza=(al.getIstanzaAlimento().getAlimento().getKcal()/100)*al.getGrammiConsumati();
        somma+=calIstanza;
      }
      Map<String,Object> map= new HashMap<>();
      map.put("caloriePreviste",calPrev);
      map.put("calorieConsumate",somma);
      map.put("istanzeAlimentiConsumati",istanzaEsercizioEseguitoList);
      return ResponseHandler.generateResponse(HttpStatus.OK,
          "result", map);
    }catch (IllegalArgumentException e) {
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
          (Object) e.getMessage());
    }
  }

}
