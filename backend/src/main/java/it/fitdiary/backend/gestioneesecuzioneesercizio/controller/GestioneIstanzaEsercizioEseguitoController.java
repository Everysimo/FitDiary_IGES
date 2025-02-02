package it.fitdiary.backend.gestioneesecuzioneesercizio.controller;

import it.fitdiary.backend.entity.IstanzaEsercizioEseguito;
import it.fitdiary.backend.entity.Protocollo;
import it.fitdiary.backend.entity.Utente;
import it.fitdiary.backend.gestioneesecuzioneesercizio.controller.dto.IstanzaEsercizioEseguitoDTO;
import it.fitdiary.backend.gestioneesecuzioneesercizio.controller.dto.VisualizzaEserciziDTO;
import it.fitdiary.backend.gestioneesecuzioneesercizio.service.GestioneIstanzaEsercizioEseguitoService;
import it.fitdiary.backend.gestioneesecuzioneesercizio.service.GestioneIstanzaEsercizioEseguitoServiceImpl;
import it.fitdiary.backend.gestioneprotocollo.service.GestioneProtocolloService;
import it.fitdiary.backend.gestioneutenza.service.GestioneUtenzaService;
import it.fitdiary.backend.utility.ResponseHandler;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;

@RestController
@Slf4j
@RequestMapping(path = "api/v1/istanzaEserciziEseguiti")
public class GestioneIstanzaEsercizioEseguitoController {

  public final GestioneIstanzaEsercizioEseguitoService gestioneIstanzaEsercizioEseguitoService;

  private final GestioneProtocolloService gestioneProtocolloService;

  @Autowired
  public GestioneIstanzaEsercizioEseguitoController(
      GestioneIstanzaEsercizioEseguitoService gestioneIstanzaEsercizioEseguitoService,
      GestioneProtocolloService gestioneProtocolloService) {
    this.gestioneIstanzaEsercizioEseguitoService = gestioneIstanzaEsercizioEseguitoService;
    this.gestioneProtocolloService = gestioneProtocolloService;
  }


  @PostMapping("creaIstanzaEsercizio")
  private ResponseEntity<Object> creazioneIstanzaEsercizio(@RequestBody
                                                           IstanzaEsercizioEseguitoDTO esercizioEseguitoDTO) {


    HttpServletRequest request = ((ServletRequestAttributes)
        RequestContextHolder.getRequestAttributes()).getRequest();



    Long idCliente = Long.parseLong(
        request.getUserPrincipal().getName());


    Protocollo protocollo =
        gestioneProtocolloService.getByIdProtocollo(esercizioEseguitoDTO.getIdProtocollo());


    if (!Objects.equals(protocollo.getCliente().getId(), idCliente)) {
      return ResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED,
          "Il cliente non può creare un'istanza di esercizio per questo "
              + "protocollo poichè non gli appartiene");
    }

    if ((esercizioEseguitoDTO.getIdProtocollo() == null ||
        esercizioEseguitoDTO.getIdIstanzaEsercizio() == null)) {
      return ResponseHandler.generateResponse(BAD_REQUEST, (Object)
          "idProtocollo e idIstanzaEsercizio non possono essere null ");
    }


    try {
      IstanzaEsercizioEseguito newIstanzaEsercizioEseguito =
          gestioneIstanzaEsercizioEseguitoService.
              creazioneIstanzaEsercizio(
                  esercizioEseguitoDTO.getIdProtocollo(),
                  esercizioEseguitoDTO.getIdIstanzaEsercizio(),
                  esercizioEseguitoDTO.getPesoEsecuzione(), LocalDate.now(),
                  esercizioEseguitoDTO.getSerie(), esercizioEseguitoDTO.getRipetizioni());
      return ResponseHandler.generateResponse(HttpStatus.CREATED,
          "istanzaEsercizio", newIstanzaEsercizioEseguito);
    } catch (IllegalArgumentException e) {
      return ResponseHandler.generateResponse(BAD_REQUEST,
          (Object) e.getMessage());
    } catch (Exception e) {
      return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
          (Object) e.getMessage());
    }
  }

  @GetMapping("visualizzaIstanzaEsercizio")
  private ResponseEntity<Object> visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(
      @RequestParam("idProtocollo") final Long idProtocollo,
      @RequestParam("idIstanzaEsercizio") final Long idIstanzaEsercizio) {
    if ((idProtocollo == null || idIstanzaEsercizio == null)) {
      return ResponseHandler.generateResponse(BAD_REQUEST, (Object)
          "idProtocollo e idIstanzaEsercizio non possono essere null ");
    }
    try {
      VisualizzaEserciziDTO visualizzaEserciziDTO =
          gestioneIstanzaEsercizioEseguitoService.
              visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(idProtocollo,
                  idIstanzaEsercizio);
      return ResponseHandler.generateResponse(HttpStatus.OK,
          "istanzeEserciziEseguiti", visualizzaEserciziDTO);
    } catch (IllegalArgumentException e) {
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
          (Object) e.getMessage());
    }
  }


}
