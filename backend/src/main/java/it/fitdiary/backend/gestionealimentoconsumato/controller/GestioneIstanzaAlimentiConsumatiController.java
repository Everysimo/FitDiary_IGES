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
import it.fitdiary.backend.utility.ResponseHandler;
import java.time.DayOfWeek;
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
public class GestioneIstanzaAlimentiConsumatiController {

  public final GestioneIstanzaAlimentoConsumatoService gestioneIstanzaAlimentoConsumatoService;

  public final GestioneSchedaAlimentareService gestioneSchedaAlimentareService;

  private final GestioneProtocolloService gestioneProtocolloService;

  @Autowired
  public GestioneIstanzaAlimentiConsumatiController(
      GestioneIstanzaAlimentoConsumatoService gestioneIstanzaAlimentoConsumatoService,
      GestioneSchedaAlimentareService gestioneSchedaAlimentareService,
      GestioneProtocolloService gestioneProtocolloService) {
    this.gestioneIstanzaAlimentoConsumatoService = gestioneIstanzaAlimentoConsumatoService;
    this.gestioneSchedaAlimentareService = gestioneSchedaAlimentareService;
    this.gestioneProtocolloService = gestioneProtocolloService;
  }


  @PostMapping("creaIstanzaAlimentoConsumato")
  private ResponseEntity<Object> creazioneIstanzaAlimentoConsumato(@RequestBody
                                                                       ListCreazioneIstanzaAlimentoConsumatoDto creazioneIstanzaAlimentoConsumatoDto) {

    HttpServletRequest request = ((ServletRequestAttributes)
        RequestContextHolder.getRequestAttributes()).getRequest();

    Long idCliente = Long.parseLong(
        request.getUserPrincipal().getName());
    Protocollo protocollo = gestioneProtocolloService.getByIdProtocollo(
        creazioneIstanzaAlimentoConsumatoDto.getIdProtocollo());
    if (!Objects.equals(protocollo.getCliente().getId(), idCliente)) {
      return ResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED,
          "Il cliente non può creare un'istanza di alimento consumato per questo "
              + "protocollo poichè non gli appartiene");
    }

    try {
      List<IstanzaAlimentoConsumato> newIstanzaAlimentoConsumato =
          gestioneIstanzaAlimentoConsumatoService.
              creazioneIstanzeEsercizio(creazioneIstanzaAlimentoConsumatoDto.getIdProtocollo(),
                  creazioneIstanzaAlimentoConsumatoDto.getListaAlimenti());
      return ResponseHandler.generateResponse(HttpStatus.CREATED,
          "istanzaAlimentoConsumato", newIstanzaAlimentoConsumato);
    } catch (IllegalArgumentException e) {
      return ResponseHandler.generateResponse(BAD_REQUEST,
          (Object) e.getMessage());
    } catch (Exception e) {
      System.out.println(e);
      return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
          (Object) e.getMessage());
    }
  }

  @GetMapping("visualizzaIstanzeAlimentoConsumato")
  private ResponseEntity<Object> visualizzaIstanzaAlimentiConsumatiByProtocolloAndIstanzaAlimentoAndDate(
      @RequestParam("idProtocollo") final Long idProtocollo,
      @RequestParam("dataConsumazione") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate dataConsumazione) {

    try {
      List<IstanzaAlimentoConsumato> istanzaEsercizioEseguitoList =
          gestioneIstanzaAlimentoConsumatoService.
              visualizzaIstanzaAlimentiConsumatiByProtocolloAndDate(idProtocollo, dataConsumazione);
      int calorieConsumate = 0;
      for (IstanzaAlimentoConsumato al : istanzaEsercizioEseguitoList) {
        calorieConsumate += al.getGrammiConsumati() * al.getIstanzaAlimento().getAlimento().getKcal()/100;
      }
      Protocollo protocollo = gestioneProtocolloService.getByIdProtocollo(idProtocollo);
      List<IstanzaAlimento> istanzeAlimentoDellaGiornata =
          gestioneSchedaAlimentareService.getAlimentiBySchedaAlimentareAndGiornoDellaSettimana(
              protocollo.getSchedaAlimentare(), GiornoSettimanaToDayOfWeek(dataConsumazione.getDayOfWeek()));
      int calorieAspettate = 0;
      for (IstanzaAlimento istanzaAlimento : istanzeAlimentoDellaGiornata)
      {
        calorieAspettate += istanzaAlimento.getGrammi() * istanzaAlimento.getAlimento().getKcal()/100;
      }

      Map<String, Object> map = new HashMap<>();

      map.put("calorieConsumate", calorieConsumate);
      map.put("calorieAspettate", calorieAspettate);
      map.put("istanzeAlimentiConsumati", istanzaEsercizioEseguitoList);
      map.put("istanzeAlimenti",istanzeAlimentoDellaGiornata);

      return ResponseHandler.generateResponse(HttpStatus.OK,
          "result", map);
    } catch (IllegalArgumentException e) {
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
          (Object) e.getMessage());
    }
  }

  private GIORNO_SETTIMANA GiornoSettimanaToDayOfWeek(DayOfWeek dayOfWeek) {
    switch (dayOfWeek) {

      case MONDAY:
        return GIORNO_SETTIMANA.LUNEDI;
      case TUESDAY:
        return GIORNO_SETTIMANA.MARTEDI;
      case WEDNESDAY:
        return GIORNO_SETTIMANA.MERCOLEDI;
      case THURSDAY:

        return GIORNO_SETTIMANA.GIOVEDI;

      case FRIDAY:
        return GIORNO_SETTIMANA.VENERDI;

      case SATURDAY:
        return GIORNO_SETTIMANA.SABATO;
      case SUNDAY:
        return GIORNO_SETTIMANA.DOMENICA;
      default:
        throw new IllegalStateException("Unexpected value: " + dayOfWeek);
    }
  }


}
