package it.fitdiary.backend.gestionealimentoconsumato.controller.dto;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import it.fitdiary.backend.entity.IstanzaAlimentoConsumato;
import it.fitdiary.backend.entity.Protocollo;
import it.fitdiary.backend.gestionealimentoconsumato.controller.CreazioneIstanzaAlimentoConsumatoDto;
import it.fitdiary.backend.gestionealimentoconsumato.service.GestioneIstanzaAlimentoConsumatoService;
import it.fitdiary.backend.gestioneprotocollo.service.GestioneProtocolloService;
import it.fitdiary.backend.utility.ResponseHandler;
import java.time.LocalDate;
import java.util.List;
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
  @Autowired
  public GestionIstanzaAlimentiConsumatiController(
      GestioneIstanzaAlimentoConsumatoService gestioneIstanzaAlimentoConsumatoService,
      GestioneProtocolloService gestioneProtocolloService){
    this.gestioneIstanzaAlimentoConsumatoService = gestioneIstanzaAlimentoConsumatoService;
    this.gestioneProtocolloService = gestioneProtocolloService;
  }




  @PostMapping("creaIstanzaAlimentoConsumato")
  private ResponseEntity<Object> creazioneIstanzaAlimentoConsumato(@RequestBody
                                                                   CreazioneIstanzaAlimentoConsumatoDto creazioneIstanzaAlimentoConsumatoDto) {

    HttpServletRequest request = ((ServletRequestAttributes)
        RequestContextHolder.getRequestAttributes()).getRequest();

    Long idCliente = Long.parseLong(
        request.getUserPrincipal().getName());
    Protocollo protocollo = gestioneProtocolloService.getByIdProtocollo(creazioneIstanzaAlimentoConsumatoDto.getProtocolloId());
    if(!Objects.equals(protocollo.getCliente().getId(), idCliente)){
      return ResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED,
          "Il cliente non può creare un'istanza di alimento consumato per questo "
              + "protocollo poichè non gli appartiene");
    }

    try{
      IstanzaAlimentoConsumato newIstanzaAlimentoConsumato = gestioneIstanzaAlimentoConsumatoService.
          creazioneIstanzaAlimentoConsumato(
              creazioneIstanzaAlimentoConsumatoDto.getProtocolloId(), creazioneIstanzaAlimentoConsumatoDto.getIstanzaAlimentoId(), creazioneIstanzaAlimentoConsumatoDto.getGrammi(), creazioneIstanzaAlimentoConsumatoDto.getData());
      return ResponseHandler.generateResponse(HttpStatus.CREATED,
          "istanzaAlimentoConsumato", newIstanzaAlimentoConsumato);
    } catch (IllegalArgumentException e) {
      return ResponseHandler.generateResponse(BAD_REQUEST,
          (Object) e.getMessage());
    }catch (Exception e)
    {
      return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
          (Object) e.getMessage());
    }
  }

  @GetMapping("visualizzaIstanzaAlimentoConsumato")
  private ResponseEntity<Object> visualizzaIstanzaAlimentiConsumatiByProtocolloAndIstanzaAlimentoAndDate(
      @RequestParam("idProtocollo") final Long idProtocollo,
      @RequestParam("idIstanzaAlimento") final Long idIstanzaAlimento,
      @RequestParam("dataConsumazione") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataConsumazione) {

    try{
      List<IstanzaAlimentoConsumato> istanzaEsercizioEseguitoList = gestioneIstanzaAlimentoConsumatoService.
          visualizzaIstanzaAlimentiConsumatiByProtocolloAndIstanzaAlimentoAndDate(idProtocollo, idIstanzaAlimento,dataConsumazione);
      return ResponseHandler.generateResponse(HttpStatus.OK,
          "istanzeAlimentiConsumati", istanzaEsercizioEseguitoList);
    }catch (IllegalArgumentException e) {
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
          (Object) e.getMessage());
    }
  }

}
