package it.fitdiary.backend.gestioneesecuzioneesercizio.controller;

import it.fitdiary.backend.entity.IstanzaEsercizioEseguito;
import it.fitdiary.backend.entity.Protocollo;
import it.fitdiary.backend.entity.Utente;
import it.fitdiary.backend.gestioneesecuzioneesercizio.service.GestioneIstanzaEsercizioEseguitoService;
import it.fitdiary.backend.gestioneesecuzioneesercizio.service.GestioneIstanzaEsercizioEseguitoServiceImpl;
import it.fitdiary.backend.gestioneprotocollo.service.GestioneProtocolloService;
import it.fitdiary.backend.gestioneutenza.service.GestioneUtenzaService;
import it.fitdiary.backend.utility.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Slf4j
@RequestMapping(path = "api/v1/istanzaEserciziEseguiti")
public class GestioneIstanzaEsercizioEseguitoController {

    public final GestioneIstanzaEsercizioEseguitoService gestioneIstanzaEsercizioEseguitoService;

    private final GestioneProtocolloService gestioneProtocolloService;
    @Autowired
    public GestioneIstanzaEsercizioEseguitoController(
            final GestioneIstanzaEsercizioEseguitoService gestioneIstanzaEsercizioEseguitoService,
            final GestioneProtocolloService gestioneProtocolloService){
        this.gestioneIstanzaEsercizioEseguitoService = gestioneIstanzaEsercizioEseguitoService;
        this.gestioneProtocolloService = gestioneProtocolloService;
    }


    @PostMapping("creaIstanzaEsercizio")
    private ResponseEntity<Object> creazioneIstanzaEsercizio(@RequestParam("data")
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                 final LocalDate data,
                                                             @RequestParam("idProtocollo") final Long idProtocollo,
                                                             @RequestParam("idIstanzaEsercizio") final Long idIstanzaEsercizio,
                                                             @RequestParam("pesoEsecuzione") final Float pesoEsecuzione,
                                                             @RequestParam("serie") final int serie,
                                                             @RequestParam("ripetizioni") final int ripetizioni) {


        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();

        Long idCliente = Long.parseLong(
                request.getUserPrincipal().getName());
        Protocollo protocollo = gestioneProtocolloService.getByIdProtocollo(idProtocollo);
        if(protocollo.getCliente().getId() != idCliente){
            return ResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED,
                    "Il cliente non può creare un'istanza di esercizio per questo "
                            + "protocollo poichè non gli appartiene");
        }
        if ((idProtocollo == null || idIstanzaEsercizio == null))
        {
            return ResponseHandler.generateResponse(BAD_REQUEST, (Object)
                    "idProtocollo e idIstanzaEsercizio non possono essere null ");
        }

        try{
            IstanzaEsercizioEseguito newIstanzaEsercizioEseguito = gestioneIstanzaEsercizioEseguitoService.
                    creazioneIstanzaEsercizio(
                            idProtocollo, idIstanzaEsercizio, pesoEsecuzione, data, serie, ripetizioni);
            return ResponseHandler.generateResponse(HttpStatus.CREATED,
                    "istanzaEsercizio", newIstanzaEsercizioEseguito);
        } catch (IllegalArgumentException e) {
            return ResponseHandler.generateResponse(BAD_REQUEST,
                    (Object) e.getMessage());
        }catch (Exception e)
        {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    (Object) e.getMessage());
        }
    }

    @PostMapping("creaIstanzaEsercizio")
    private ResponseEntity<Object> visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(
            @RequestParam("idProtocollo") final Long idProtocollo,
            @RequestParam("idIstanzaEsercizio") final Long idIstanzaEsercizio) {

        if ((idProtocollo == null || idIstanzaEsercizio == null))
        {
            return ResponseHandler.generateResponse(BAD_REQUEST, (Object)
                    "idProtocollo e idIstanzaEsercizio non possono essere null ");
        }
        try{
            List<IstanzaEsercizioEseguito> istanzaEsercizioEseguitoList = gestioneIstanzaEsercizioEseguitoService.
            visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(idProtocollo, idIstanzaEsercizio);
            return ResponseHandler.generateResponse(HttpStatus.OK,
                    "istanzeEserciziEseguiti", istanzaEsercizioEseguitoList);
        }catch (IllegalArgumentException e) {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
                    (Object) e.getMessage());
        }
    }




}
