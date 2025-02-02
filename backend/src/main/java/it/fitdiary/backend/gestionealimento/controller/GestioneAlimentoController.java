package it.fitdiary.backend.gestionealimento.controller;

import it.fitdiary.backend.entity.Alimento;
import it.fitdiary.backend.gestionealimento.service.GestioneAlimentoService;
import it.fitdiary.backend.utility.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "api/v1/alimenti")
public class GestioneAlimentoController {


  private GestioneAlimentoService service;

  @Autowired
  public GestioneAlimentoController(
      GestioneAlimentoService service) {
    this.service = service;
  }

  @GetMapping("getAlimento")
  private ResponseEntity<Object> VisualizzaAlimento(
      @RequestParam("idAlimento") final Long idAlimento) {

    try {
      Alimento alimento = service.getById(idAlimento);
      return ResponseHandler.generateResponse(HttpStatus.OK, "alimento",
          alimento
      );
    } catch (IllegalArgumentException e) {
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
          (Object) e.getMessage());
    }

  }

  @GetMapping("getAllAlimenti")
  private ResponseEntity<Object> VisualizzaListaAlimenti() {

    try {
      List<Alimento> listaAlimenti = service.getAllAlimenti();
      return ResponseHandler.generateResponse(HttpStatus.OK, "lista_alimenti",
              listaAlimenti
      );
    } catch (IllegalArgumentException e) {
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,
              (Object) e.getMessage());
    }

  }

}
