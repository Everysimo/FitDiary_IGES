package it.fitdiary.backend.gestioneesecuzioneesercizio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fitdiary.backend.entity.*;
import it.fitdiary.backend.gestioneesecuzioneesercizio.controller.dto.IstanzaEsercizioEseguitoDTO;
import it.fitdiary.backend.gestioneesecuzioneesercizio.controller.dto.VisualizzaEserciziDTO;
import it.fitdiary.backend.gestioneesecuzioneesercizio.service.GestioneIstanzaEsercizioEseguitoServiceImpl;
import it.fitdiary.backend.gestioneprotocollo.service.GestioneProtocolloServiceImpl;
import it.fitdiary.backend.gestioneschedaalimentare.controller.TestObjectMapperConfig;
import it.fitdiary.backend.gestioneutenza.service.GestioneUtenzaServiceImpl;
import it.fitdiary.backend.utility.FileUtility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ContextConfiguration(classes = {GestioneIstanzaEsercizioEseguitoController.class, TestObjectMapperConfig.class })
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class GestioneIstanzaEsercizioEseguitoControllerTest {
    @Autowired
    private GestioneIstanzaEsercizioEseguitoController gestioneIstanzaEsercizioEseguitoController;
    @MockBean
    private GestioneIstanzaEsercizioEseguitoServiceImpl gestioneIstanzaEsercizioEseguitoService;

    @MockBean
    private GestioneProtocolloServiceImpl gestioneProtocolloService;
    @Autowired
    private ObjectMapper objectMapper;
    Utente preparatore;
    Protocollo protocollo;
    IstanzaEsercizio istanzaEsercizio;
    private Ruolo ruoloCliente;
    private Ruolo ruoloPreparatore;
    private Utente cliente, cliente2;
    private Esercizio esercizio;
    private CategoriaEsercizio categoriaEsercizio;

    private IstanzaEsercizioEseguito istanzaEsercizioEseguito;

    @BeforeEach
    public void setUp() throws IOException {
        categoriaEsercizio = new CategoriaEsercizio(1L,"TipoEsercizio");


        ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);
        ruoloPreparatore = new Ruolo(2L, "PREPARATORE", null, null);
        cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        preparatore =
                new Utente(1L, "Daniele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true, null, null, null, null,
                        null, null, null, ruoloPreparatore, null, null, null,
                        null, null);
        protocollo = new Protocollo(1L, LocalDate.parse("2022-01-05"),
                new SchedaAlimentare(), new SchedaAllenamento(), cliente,
                preparatore, null, null);
        esercizio = new Esercizio(1L, "Chest press","EserciziPalestra/Air-Twisting-Crunch_waist.gif",categoriaEsercizio);


        istanzaEsercizio = new IstanzaEsercizio();
        istanzaEsercizio.setId(1L);
        istanzaEsercizio.setGiornoDellaSettimana(0);
        istanzaEsercizio.setSerie(5);
        istanzaEsercizio.setRecupero(1);
        istanzaEsercizio.setRipetizioni(1);
        istanzaEsercizio.setEsercizio(esercizio);
        istanzaEsercizio.setDescrizione("Descr");

        istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);

    }

    @Test
    public void creazioneIstanzaEsercizioSuccess() throws Exception {

        categoriaEsercizio = new CategoriaEsercizio(1L,"TipoEsercizio");


        ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);
        ruoloPreparatore = new Ruolo(2L, "PREPARATORE", null, null);
        cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        preparatore =
                new Utente(1L, "Daniele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true, null, null, null, null,
                        null, null, null, ruoloPreparatore, null, null, null,
                        null, null);
        protocollo = new Protocollo(1L, LocalDate.parse("2022-01-05"),
                new SchedaAlimentare(), new SchedaAllenamento(), cliente,
                preparatore, null, null);
        esercizio = new Esercizio(1L, "Chest press","EserciziPalestra/Air-Twisting-Crunch_waist.gif",categoriaEsercizio);


        istanzaEsercizio = new IstanzaEsercizio();
        istanzaEsercizio.setId(1L);
        istanzaEsercizio.setGiornoDellaSettimana(0);
        istanzaEsercizio.setSerie(5);
        istanzaEsercizio.setRecupero(1);
        istanzaEsercizio.setRipetizioni(1);
        istanzaEsercizio.setEsercizio(esercizio);
        istanzaEsercizio.setDescrizione("Descr");

        istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);

        IstanzaEsercizioEseguitoDTO istanzaEsercizioEseguitoDTO = new IstanzaEsercizioEseguitoDTO();
        istanzaEsercizioEseguitoDTO.setIdIstanzaEsercizio(1L);
        istanzaEsercizioEseguitoDTO.setData(LocalDate.of(2023,12,12));
        istanzaEsercizioEseguitoDTO.setRipetizioni(3);
        istanzaEsercizioEseguitoDTO.setSerie(3);
        istanzaEsercizioEseguitoDTO.setIdProtocollo(3L);
        istanzaEsercizioEseguitoDTO.setPesoEsecuzione(3F);


        Principal principal = () -> "1";
        when(gestioneProtocolloService.getByIdProtocollo(
                anyLong())).thenReturn(protocollo);
        when(gestioneIstanzaEsercizioEseguitoService.creazioneIstanzaEsercizio(1L,1L,3f, LocalDate.of(2022,12,12),2,3)).thenReturn(istanzaEsercizioEseguito);

        String requestBody = objectMapper.writeValueAsString(istanzaEsercizioEseguitoDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/istanzaEserciziEseguiti/creaIstanzaEsercizio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneIstanzaEsercizioEseguitoController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().is2xxSuccessful());


    }


    @Test
    public void creazioneIstanzaEsercizioUnauthorized() throws Exception {

        categoriaEsercizio = new CategoriaEsercizio(1L,"TipoEsercizio");


        ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);
        ruoloPreparatore = new Ruolo(2L, "PREPARATORE", null, null);
        cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        cliente2 = new Utente(2L, "Metteo", "De Rossi",
                "MattDeRoss@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        preparatore =
                new Utente(1L, "Daniele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true, null, null, null, null,
                        null, null, null, ruoloPreparatore, null, null, null,
                        null, null);
        protocollo = new Protocollo(1L, LocalDate.parse("2022-01-05"),
                new SchedaAlimentare(), new SchedaAllenamento(), cliente,
                preparatore, null, null);
        esercizio = new Esercizio(1L, "Chest press","EserciziPalestra/Air-Twisting-Crunch_waist.gif",categoriaEsercizio);


        istanzaEsercizio = new IstanzaEsercizio();
        istanzaEsercizio.setId(1L);
        istanzaEsercizio.setGiornoDellaSettimana(0);
        istanzaEsercizio.setSerie(5);
        istanzaEsercizio.setRecupero(1);
        istanzaEsercizio.setRipetizioni(1);
        istanzaEsercizio.setEsercizio(esercizio);
        istanzaEsercizio.setDescrizione("Descr");

        istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);


        IstanzaEsercizioEseguitoDTO istanzaEsercizioEseguitoDTO = new IstanzaEsercizioEseguitoDTO();
        istanzaEsercizioEseguitoDTO.setIdIstanzaEsercizio(1L);
        istanzaEsercizioEseguitoDTO.setData(LocalDate.of(2023,12,12));
        istanzaEsercizioEseguitoDTO.setRipetizioni(3);
        istanzaEsercizioEseguitoDTO.setSerie(3);
        istanzaEsercizioEseguitoDTO.setIdProtocollo(3L);
        istanzaEsercizioEseguitoDTO.setPesoEsecuzione(3F);

        Principal principal = () -> "2";
        when(gestioneProtocolloService.getByIdProtocollo(
                anyLong())).thenReturn(protocollo);
        when(gestioneIstanzaEsercizioEseguitoService.creazioneIstanzaEsercizio(1L,1L,3f, LocalDate.of(2022,12,12),2,3)).thenReturn(istanzaEsercizioEseguito);

        String requestBody = objectMapper.writeValueAsString(istanzaEsercizioEseguitoDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/istanzaEserciziEseguiti/creaIstanzaEsercizio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneIstanzaEsercizioEseguitoController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isUnauthorized());



    }
/*
    @Test
    public void creazioneIstanzaEsercizioTestBadRequestNoIdProtocollo() throws Exception {

        categoriaEsercizio = new CategoriaEsercizio(1L,"TipoEsercizio");


        ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);
        ruoloPreparatore = new Ruolo(2L, "PREPARATORE", null, null);
        cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        cliente2 = new Utente(2L, "Metteo", "De Rossi",
                "MattDeRoss@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        preparatore =
                new Utente(1L, "Daniele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true, null, null, null, null,
                        null, null, null, ruoloPreparatore, null, null, null,
                        null, null);
        protocollo = new Protocollo(1L, LocalDate.parse("2022-01-05"),
                new SchedaAlimentare(), new SchedaAllenamento(), cliente,
                preparatore, null, null);
        esercizio = new Esercizio(1L, "Chest press","EserciziPalestra/Air-Twisting-Crunch_waist.gif",categoriaEsercizio);


        istanzaEsercizio = new IstanzaEsercizio();
        istanzaEsercizio.setId(1L);
        istanzaEsercizio.setGiornoDellaSettimana(0);
        istanzaEsercizio.setSerie(5);
        istanzaEsercizio.setRecupero(1);
        istanzaEsercizio.setRipetizioni(1);
        istanzaEsercizio.setEsercizio(esercizio);
        istanzaEsercizio.setDescrizione("Descr");

        istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2f,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);


        IstanzaEsercizioEseguitoDTO istanzaEsercizioEseguitoDTO = new IstanzaEsercizioEseguitoDTO();
        istanzaEsercizioEseguitoDTO.setIdIstanzaEsercizio(1L);
        istanzaEsercizioEseguitoDTO.setData(LocalDate.of(2023,12,12));
        istanzaEsercizioEseguitoDTO.setRipetizioni(3);
        istanzaEsercizioEseguitoDTO.setSerie(3);
        istanzaEsercizioEseguitoDTO.setPesoEsecuzione(3F);

        Principal principal = () -> "1";
        when(gestioneProtocolloService.getByIdProtocollo(
                anyLong())).thenReturn(protocollo);
        when(gestioneIstanzaEsercizioEseguitoService.creazioneIstanzaEsercizio(1L,1L,3f, LocalDate.of(2022,12,12),2,3)).thenReturn(istanzaEsercizioEseguito);

        String requestBody = objectMapper.writeValueAsString(istanzaEsercizioEseguitoDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/istanzaEserciziEseguiti/creaIstanzaEsercizio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneIstanzaEsercizioEseguitoController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isBadRequest());


    }
*/
    @Test
    public void creazioneIstanzaEsercizioTestBadRequestNoIdIstanEse() throws Exception {

        categoriaEsercizio = new CategoriaEsercizio(1L,"TipoEsercizio");


        ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);
        ruoloPreparatore = new Ruolo(2L, "PREPARATORE", null, null);
        cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        cliente2 = new Utente(2L, "Metteo", "De Rossi",
                "MattDeRoss@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        preparatore =
                new Utente(1L, "Daniele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true, null, null, null, null,
                        null, null, null, ruoloPreparatore, null, null, null,
                        null, null);
        protocollo = new Protocollo(1L, LocalDate.parse("2022-01-05"),
                new SchedaAlimentare(), new SchedaAllenamento(), cliente,
                preparatore, null, null);
        esercizio = new Esercizio(1L, "Chest press","EserciziPalestra/Air-Twisting-Crunch_waist.gif",categoriaEsercizio);


        istanzaEsercizio = new IstanzaEsercizio();
        istanzaEsercizio.setId(1L);
        istanzaEsercizio.setGiornoDellaSettimana(0);
        istanzaEsercizio.setSerie(5);
        istanzaEsercizio.setRecupero(1);
        istanzaEsercizio.setRipetizioni(1);
        istanzaEsercizio.setEsercizio(esercizio);
        istanzaEsercizio.setDescrizione("Descr");

        istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);


        IstanzaEsercizioEseguitoDTO istanzaEsercizioEseguitoDTO = new IstanzaEsercizioEseguitoDTO();
        istanzaEsercizioEseguitoDTO.setData(LocalDate.of(2023,12,12));
        istanzaEsercizioEseguitoDTO.setRipetizioni(3);
        istanzaEsercizioEseguitoDTO.setSerie(3);
        istanzaEsercizioEseguitoDTO.setIdProtocollo(3L);
        istanzaEsercizioEseguitoDTO.setPesoEsecuzione(3F);

        Principal principal = () -> "1";
        when(gestioneProtocolloService.getByIdProtocollo(
                anyLong())).thenReturn(protocollo);
        when(gestioneIstanzaEsercizioEseguitoService.creazioneIstanzaEsercizio(1L,1L,3f, LocalDate.of(2022,12,12),2,3)).thenReturn(istanzaEsercizioEseguito);

        String requestBody = objectMapper.writeValueAsString(istanzaEsercizioEseguitoDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/istanzaEserciziEseguiti/creaIstanzaEsercizio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneIstanzaEsercizioEseguitoController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizioSuccess() throws Exception {

        categoriaEsercizio = new CategoriaEsercizio(1L,"TipoEsercizio");


        ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);
        ruoloPreparatore = new Ruolo(2L, "PREPARATORE", null, null);
        cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        cliente2 = new Utente(2L, "Metteo", "De Rossi",
                "MattDeRoss@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        preparatore =
                new Utente(1L, "Daniele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true, null, null, null, null,
                        null, null, null, ruoloPreparatore, null, null, null,
                        null, null);
        protocollo = new Protocollo(1L, LocalDate.parse("2022-01-05"),
                new SchedaAlimentare(), new SchedaAllenamento(), cliente,
                preparatore, null, null);
        esercizio = new Esercizio(1L, "Chest press","EserciziPalestra/Air-Twisting-Crunch_waist.gif",categoriaEsercizio);


        istanzaEsercizio = new IstanzaEsercizio();
        istanzaEsercizio.setId(1L);
        istanzaEsercizio.setGiornoDellaSettimana(0);
        istanzaEsercizio.setSerie(5);
        istanzaEsercizio.setRecupero(1);
        istanzaEsercizio.setRipetizioni(1);
        istanzaEsercizio.setEsercizio(esercizio);
        istanzaEsercizio.setDescrizione("Descr");

        istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);
        IstanzaEsercizioEseguito istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);
        ArrayList<IstanzaEsercizioEseguito> istanzaEsercizioEseguiti = new ArrayList<>();
        istanzaEsercizioEseguiti.add(istanzaEsercizioEseguito);

        VisualizzaEserciziDTO visualizzaEserciziDTO = new VisualizzaEserciziDTO();
        visualizzaEserciziDTO.setIstanzaEsercizio(istanzaEsercizio);
        visualizzaEserciziDTO.setListaEserciziEseguiti(istanzaEsercizioEseguiti);

        Principal principal = () -> "1";
        when(gestioneProtocolloService.getByIdProtocollo(
                protocollo.getId())).thenReturn(protocollo);
        when(gestioneIstanzaEsercizioEseguitoService.visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(anyLong(),anyLong())).thenReturn(visualizzaEserciziDTO);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(
                                "/api/v1/istanzaEserciziEseguiti/visualizzaIstanzaEsercizio?idProtocollo="+
                                        istanzaEsercizioEseguito.getProtocollo().getId().toString()+
                                        "&idIstanzaEsercizio="+istanzaEsercizioEseguito.getIstanzaEsercizio().getId())
                        .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneIstanzaEsercizioEseguitoController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizioBadRequestNoIdProtocollo() throws Exception {

        categoriaEsercizio = new CategoriaEsercizio(1L,"TipoEsercizio");


        ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);
        ruoloPreparatore = new Ruolo(2L, "PREPARATORE", null, null);
        cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        cliente2 = new Utente(2L, "Metteo", "De Rossi",
                "MattDeRoss@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        preparatore =
                new Utente(1L, "Daniele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true, null, null, null, null,
                        null, null, null, ruoloPreparatore, null, null, null,
                        null, null);
        protocollo = new Protocollo(1L, LocalDate.parse("2022-01-05"),
                new SchedaAlimentare(), new SchedaAllenamento(), cliente,
                preparatore, null, null);
        esercizio = new Esercizio(1L, "Chest press","EserciziPalestra/Air-Twisting-Crunch_waist.gif",categoriaEsercizio);


        istanzaEsercizio = new IstanzaEsercizio();
        istanzaEsercizio.setId(1L);
        istanzaEsercizio.setGiornoDellaSettimana(0);
        istanzaEsercizio.setSerie(5);
        istanzaEsercizio.setRecupero(1);
        istanzaEsercizio.setRipetizioni(1);
        istanzaEsercizio.setEsercizio(esercizio);
        istanzaEsercizio.setDescrizione("Descr");

        istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);
        IstanzaEsercizioEseguito istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);
        ArrayList<IstanzaEsercizioEseguito> istanzaEsercizioEseguiti = new ArrayList<>();
        istanzaEsercizioEseguiti.add(istanzaEsercizioEseguito);

        VisualizzaEserciziDTO visualizzaEserciziDTO = new VisualizzaEserciziDTO();
        visualizzaEserciziDTO.setIstanzaEsercizio(istanzaEsercizio);
        visualizzaEserciziDTO.setListaEserciziEseguiti(istanzaEsercizioEseguiti);


        Principal principal = () -> "1";
        when(gestioneProtocolloService.getByIdProtocollo(
                protocollo.getId())).thenReturn(protocollo);
        when(gestioneIstanzaEsercizioEseguitoService.visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(anyLong(),anyLong())).thenReturn(visualizzaEserciziDTO);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(
                                "/api/v1/istanzaEserciziEseguiti/visualizzaIstanzaEsercizio?idProtocollo=&idIstanzaEsercizio="+istanzaEsercizioEseguito.getProtocollo().getId().toString())
                        .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneIstanzaEsercizioEseguitoController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizioBadRequestNoIdIstanzaEse() throws Exception {

        categoriaEsercizio = new CategoriaEsercizio(1L,"TipoEsercizio");


        ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);
        ruoloPreparatore = new Ruolo(2L, "PREPARATORE", null, null);
        cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        cliente2 = new Utente(2L, "Metteo", "De Rossi",
                "MattDeRoss@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        preparatore =
                new Utente(1L, "Daniele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true, null, null, null, null,
                        null, null, null, ruoloPreparatore, null, null, null,
                        null, null);
        protocollo = new Protocollo(1L, LocalDate.parse("2022-01-05"),
                new SchedaAlimentare(), new SchedaAllenamento(), cliente,
                preparatore, null, null);
        esercizio = new Esercizio(1L, "Chest press","EserciziPalestra/Air-Twisting-Crunch_waist.gif",categoriaEsercizio);


        istanzaEsercizio = new IstanzaEsercizio();
        istanzaEsercizio.setId(1L);
        istanzaEsercizio.setGiornoDellaSettimana(0);
        istanzaEsercizio.setSerie(5);
        istanzaEsercizio.setRecupero(1);
        istanzaEsercizio.setRipetizioni(1);
        istanzaEsercizio.setEsercizio(esercizio);
        istanzaEsercizio.setDescrizione("Descr");

        istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);
        IstanzaEsercizioEseguito istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);
        ArrayList<IstanzaEsercizioEseguito> istanzaEsercizioEseguiti = new ArrayList<>();
        istanzaEsercizioEseguiti.add(istanzaEsercizioEseguito);

        VisualizzaEserciziDTO visualizzaEserciziDTO = new VisualizzaEserciziDTO();
        visualizzaEserciziDTO.setIstanzaEsercizio(istanzaEsercizio);
        visualizzaEserciziDTO.setListaEserciziEseguiti(istanzaEsercizioEseguiti);


        Principal principal = () -> "1";
        when(gestioneProtocolloService.getByIdProtocollo(
                protocollo.getId())).thenReturn(protocollo);
        when(gestioneIstanzaEsercizioEseguitoService.visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(anyLong(),anyLong())).thenReturn(visualizzaEserciziDTO);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(
                                "/api/v1/istanzaEserciziEseguiti/visualizzaIstanzaEsercizio?idProtocollo="+
                                        istanzaEsercizioEseguito.getProtocollo().getId().toString()+
                                        "&idIstanzaEsercizio=")
                        .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneIstanzaEsercizioEseguitoController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isBadRequest());

    }
}
