package it.fitdiary.backend.gestioneprotocollo.controller;

import it.fitdiary.backend.entity.*;
import it.fitdiary.backend.gestioneprotocollo.service.GestioneProtocolloServiceImpl;
import it.fitdiary.backend.gestioneutenza.service.GestioneUtenzaServiceImpl;
import it.fitdiary.backend.utility.FileUtility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ContextConfiguration(classes = {GestioneProtocolloController.class})
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
class GestioneProtocolloControllerTest {
    @Autowired
    private GestioneProtocolloController gestioneProtocolloController;
    @MockBean
    private GestioneUtenzaServiceImpl gestioneUtenzaServiceImpl;
    @MockBean
    private GestioneProtocolloServiceImpl gestioneProtocolloServiceImpl;
    private Ruolo ruoloCliente;
    private Ruolo ruoloPreparatore;
    private Utente cliente;
    private Utente clienteAggiornato;
    private Utente preparatore;
    private Utente updatedPreparatore;
    private Utente cliente1;
    private Utente cliente2;
    private Utente cliente3;
    private Utente preparatore1;
    private Protocollo protocollo;
    private Protocollo protocolloConSchedaAlimentare;
    private Protocollo protocolloConSchedaAllenamento;
    private Protocollo protocolloPieno;
    private Alimento alimento;
    private Esercizio esercizio;
    private SchedaAllenamento schedaAllenamento;
    private SchedaAlimentare schedaAlimentare;
    private CategoriaEsercizio categoriaEsercizio;

    private static MockedStatic<FileUtility> fileUtility;

    @BeforeAll
    public static void init() {
        fileUtility = Mockito.mockStatic(FileUtility.class);
    }

    @AfterAll
    public static void close() {
        fileUtility.close();
    }

    @BeforeEach
    public void setUp() throws IOException {
        ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);
        ruoloPreparatore = new Ruolo(2L, "PREPARATORE", null, null);
        cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true, null, null, null,
                null, null, null, null, ruoloCliente, null, null, null, null,
                null);
        clienteAggiornato = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true,
                LocalDate.parse("2000-10-30"), null, "3894685921",
                "Francesco rinaldo", "94061", "Agropoli", null,
                ruoloCliente, null, null, null, null, null);
        preparatore =
                new Utente(1L, "Daniele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true, null, null, null, null,
                        null, null, null, ruoloPreparatore, null, null, null,
                        null, null);
        updatedPreparatore =
                new Utente(1L, "Michele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true,
                        LocalDate.parse("2000-03-03"), null, "3459666587",
                        "Francesco La Francesca", "84126", "Salerno", null,
                        ruoloPreparatore, null, null, null, null, null);
        protocollo =
                new Protocollo(1L, LocalDate.now(), null, null, cliente,
                        preparatore, LocalDateTime.now(), null);

        protocolloConSchedaAlimentare =
                new Protocollo(1L, LocalDate.now(), schedaAlimentare, null, cliente,
                        preparatore, LocalDateTime.now(), null);
        protocolloConSchedaAllenamento =
                new Protocollo(1L, LocalDate.now(), null, schedaAllenamento, cliente,
                        preparatore, LocalDateTime.now(), null);
        protocolloPieno = new Protocollo(1L, LocalDate.now(), schedaAlimentare, schedaAllenamento, cliente,
                preparatore, LocalDateTime.now(), null);
         cliente1 = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true,
                LocalDate.parse("2000-10-30"), null, "3894685921",
                "Francesco rinaldo", "94061", "Agropoli", null,
                ruoloCliente, null, null, null, null, null);


         preparatore1 =
                new Utente(1L, "Davide", "La Gamba", "davide@gmail.com"
                        , "Davide123*", true, LocalDate.parse("2000-03" +
                        "-03"), "M", null, null, null,
                        null, null, ruoloPreparatore, null, null, null, null, null);
        cliente2 = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true,
                LocalDate.parse("2000-10-30"), null, "3894685921",
                "Francesco rinaldo", "94061", "Agropoli", preparatore,
                ruoloCliente, null, null, null, null, null);
        cliente3 = new Utente(2L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true,
                LocalDate.parse("2000-10-30"), null, "3894685921",
                "Francesco rinaldo", "94061", "Agropoli", preparatore,
                ruoloCliente, null, null, null, null, null);
        //alimento = new Alimento(null,"Pasta","pranzo","1",200f,"100",
        //        null);
        categoriaEsercizio = new CategoriaEsercizio(1L,"Pettorali");
        esercizio = new Esercizio(1L, "Chest press", "EserciziPalestra/Air-Twisting-Crunch_waist.gif",
                categoriaEsercizio);
        schedaAlimentare =
                new SchedaAlimentare(1L, "schedaBuona", 2000f, new ArrayList<IstanzaAlimento>(),
                        preparatore,
                        LocalDateTime.now(), LocalDateTime.now());
        schedaAllenamento = new SchedaAllenamento(1L, "Test",4,null,preparatore,
                LocalDateTime.now(), LocalDateTime.now());

    }

    @Test
    void visualizzaStoricoProtocolliCliente() throws Exception {
        Principal principal = () -> "1";
        when(gestioneProtocolloServiceImpl.visualizzaStoricoProtocolliCliente(
                cliente2)).thenReturn(new ArrayList<Protocollo>());
        when(gestioneUtenzaServiceImpl.getById(1L)).thenReturn(preparatore);
        when(gestioneUtenzaServiceImpl.existsByPreparatoreAndId(preparatore,
                cliente2.getId())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/v1/protocolli").param("clienteId",
                        String.valueOf(cliente2.getId())).principal(principal);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(
                        this.gestioneProtocolloController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andDo(print()).andExpect(
                MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void visualizzaStoricoProtocolliSuccess() throws Exception {
        Principal principal = () -> "1";
        when(gestioneUtenzaServiceImpl.getById(1L)).thenReturn(cliente);
        when(gestioneProtocolloServiceImpl.visualizzaStoricoProtocolliCliente(
                cliente)).thenReturn(new ArrayList<Protocollo>());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/v1/protocolli").principal(principal);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(
                        this.gestioneProtocolloController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().is2xxSuccessful());
    }

   @Test
    void visualizzaStoricoProtocolliBadRequest() throws Exception {
        Principal principal = () -> "1";
        when(gestioneUtenzaServiceImpl.getById(1L)).thenThrow(IllegalArgumentException.class);
        when(gestioneProtocolloServiceImpl.visualizzaStoricoProtocolliCliente(
                cliente)).thenReturn(new ArrayList<Protocollo>());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/v1/protocolli").principal(principal);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(
                        this.gestioneProtocolloController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andDo(print()).andExpect(
                MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void visualizzaProtocolloFromClienteTest_Success() throws Exception {

        Ruolo ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);

        Utente cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true,
                LocalDate.parse("2000-10-30"), null, "3894685921",
                "Francesco rinaldo", "94061", "Agropoli", null,
                ruoloCliente, null, null, null, null, null);
        Ruolo ruoloPrep = new Ruolo(1L, "PREPARATORE", null, null);

        Utente preparatore =
                new Utente(1L, "Davide", "La Gamba", "davide@gmail.com"
                        , "Davide123*", true, LocalDate.parse("2000-03" +
                        "-03"), "M", null, null, null,
                        null, null, ruoloPrep, null, null, null, null, null);

        Protocollo protocollo =
                new Protocollo(1L, LocalDate.now(), null, null, cliente,
                        preparatore, LocalDateTime.now(), null);
        Principal principal = () -> "1";
        when(gestioneProtocolloServiceImpl.getByIdProtocollo(
                protocollo.getId())).thenReturn(protocollo);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/v1/protocolli/1")
                        .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneProtocolloController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().is2xxSuccessful());

    }

    @Test
    public void visualizzaProtocolloTest_Success()
            throws Exception {

        Ruolo ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);

        Utente cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true,
                LocalDate.parse("2000-10-30"), null, "3894685921",
                "Francesco rinaldo", "94061", "Agropoli", null,
                ruoloCliente, null, null, null, null, null);
        Ruolo ruoloPrep = new Ruolo(1L, "PREPARATORE", null, null);

        Utente preparatore =
                new Utente(1L, "Davide", "La Gamba", "davide@gmail.com"
                        , "Davide123*", true, LocalDate.parse("2000-03" +
                        "-03"), "M", null, null, null,
                        null, null, ruoloPrep, null, null, null, null, null);

        Protocollo protocollo =
                new Protocollo(1L, LocalDate.now(), null, null, cliente,
                        preparatore, LocalDateTime.now(), null);
        Principal principal = () -> "1";
        when(gestioneProtocolloServiceImpl.getByIdProtocollo(
                protocollo.getId())).thenReturn(protocollo);
        when(gestioneUtenzaServiceImpl.existsByPreparatoreAndId(preparatore,cliente.getId())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(
                                "/api/v1/protocolli/1")
                        .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneProtocolloController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().is2xxSuccessful());

    }

    @Test
    public void visualizzaProtocolloTest_BadRequest() throws Exception {

        Ruolo ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);

        Utente cliente = new Utente(1L, "Rebecca", "Di Matteo",
                "beccadimatteoo@gmail.com", "Becca123*", true,
                LocalDate.parse("2000-10-30"), null, "3894685921",
                "Francesco rinaldo", "94061", "Agropoli", null,
                ruoloCliente, null, null, null, null, null);
        Ruolo ruoloPrep = new Ruolo(1L, "PREPARATORE", null, null);

        Utente preparatore =
                new Utente(1L, "Davide", "La Gamba", "davide@gmail.com"
                        , "Davide123*", true, LocalDate.parse("2000-03" +
                        "-03"), "M", null, null, null,
                        null, null, ruoloPrep, null, null, null, null, null);

        Protocollo protocollo =
                new Protocollo(1L, LocalDate.now(), null, null, cliente,
                        preparatore, LocalDateTime.now(), null);
        Principal principal = () -> "8";
        when(gestioneProtocolloServiceImpl.getByIdProtocollo(
                protocollo.getId())).thenReturn(protocollo);
        when(gestioneUtenzaServiceImpl.existsByPreparatoreAndId(preparatore,cliente.getId())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(
                                "/api/v1/protocolli/1")
                        .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneProtocolloController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void creazioneProtocolloSuccess()
            throws Exception {
        String dataScadenza= "2023-12-12";
        Principal principal = () -> "1";
        Long idCliente = 2L;
        when(gestioneUtenzaServiceImpl.getById(1L)).thenReturn(preparatore);
        when(gestioneUtenzaServiceImpl.existsByPreparatoreAndId(preparatore, idCliente)).thenReturn(true);
        when(gestioneUtenzaServiceImpl.getById(idCliente)).thenReturn(cliente3);


        Protocollo protocolloPre = new Protocollo();
        protocolloPre.setDataScadenza(LocalDate.parse(dataScadenza));
        protocolloPre.setCliente(cliente3);
        protocolloPre.setPreparatore(preparatore);
       when(gestioneProtocolloServiceImpl.creazioneProtocollo(LocalDate.parse(dataScadenza), cliente3, preparatore,
               schedaAlimentare.getId(),schedaAllenamento.getId())).thenReturn(protocollo);
        MockHttpServletRequestBuilder requestBuilder =
                (MockMvcRequestBuilders.multipart(
                                "/api/v1/protocolli")
                        .param("dataScadenza", dataScadenza)
                        .param("idCliente", idCliente.toString())
                        .param("idSchedaAlimentare", schedaAlimentare.getId().toString())
                        .param("idSchedaAllenamento",schedaAllenamento.getId().toString())
                        .principal(principal));
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneProtocolloController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void creazioneProtocolloErrorUnauthorized()
            throws Exception {
        String dataScadenza= "2023-12-12";
        Principal principal = () -> "1";
        Long idCliente = 2L;
        when(gestioneUtenzaServiceImpl.getById(1L)).thenReturn(preparatore);
        when(gestioneUtenzaServiceImpl.existsByPreparatoreAndId(preparatore, idCliente)).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder =
                (MockMvcRequestBuilders.multipart(
                                "/api/v1/protocolli")
                        .param("dataScadenza", dataScadenza)
                        .param("idCliente", idCliente.toString())
                        .param("idSchedaAlimentare", schedaAlimentare.getId().toString())
                        .param("idSchedaAllenamento",schedaAllenamento.getId().toString())
                        .principal(principal));
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneProtocolloController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    public void creazioneProtocolloErrorSchedeVuote()
            throws Exception {
        String dataScadenza= "2023-12-12";
        Principal principal = () -> "1";
        Long idCliente = 2L;
        when(gestioneUtenzaServiceImpl.getById(1L)).thenReturn(preparatore);
        when(gestioneUtenzaServiceImpl.existsByPreparatoreAndId(preparatore, idCliente)).thenReturn(true);
        when(gestioneUtenzaServiceImpl.getById(idCliente)).thenReturn(cliente3);

        MockHttpServletRequestBuilder requestBuilder =
                (MockMvcRequestBuilders.multipart(
                                "/api/v1/protocolli")
                        .param("dataScadenza", dataScadenza)
                        .param("idCliente", idCliente.toString())
                        .param("idSchedaAlimentare", "")
                        .param("idSchedaAllenamento","")
                        .principal(principal));
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneProtocolloController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isBadRequest());
    }

   @Test
    public void modificaProtocolloSuccess()
            throws Exception {
        Principal principal = () -> "1";
        Long idCliente = 1L;
        Long idProtocollo = 1L;
        when(gestioneUtenzaServiceImpl.getById(1L)).thenReturn(preparatore);
        when(gestioneProtocolloServiceImpl.getByIdProtocollo(idProtocollo)).thenReturn(protocollo);
        when(gestioneUtenzaServiceImpl.existsByPreparatoreAndId(preparatore, idCliente)).thenReturn(true);


       MockHttpServletRequestBuilder requestBuilder =
               (MockMvcRequestBuilders.multipart(
                               "/api/v1/protocolli/1")
                       .param("idSchedaAlimentare", schedaAlimentare.getId().toString())
                       .param("idSchedaAllenamento",schedaAllenamento.getId().toString())
                       .principal(principal));
        requestBuilder.with(request -> {request.setMethod("PUT"); return request;});
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneProtocolloController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void modificaProtocolloErrorUnauthorized()
            throws Exception {
        Principal principal = () -> "1";
        Long idCliente = 1L;
        Long idProtocollo = 1L;
        when(gestioneUtenzaServiceImpl.getById(1L)).thenReturn(preparatore);
        when(gestioneProtocolloServiceImpl.getByIdProtocollo(idProtocollo)).thenReturn(protocollo);
        when(gestioneUtenzaServiceImpl.existsByPreparatoreAndId(preparatore, idCliente)).thenReturn(false);



        MockHttpServletRequestBuilder requestBuilder =
                (MockMvcRequestBuilders.multipart(
                                "/api/v1/protocolli/1")
                        .param("idSchedaAlimentare", schedaAlimentare.getId().toString())
                        .param("idSchedaAllenamento",schedaAllenamento.getId().toString())
                        .principal(principal));
        requestBuilder.with(request -> {request.setMethod("PUT"); return request;});
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneProtocolloController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void modificaProtocolloSuccessSchedaAllenamentoEmpty()
            throws Exception {
        Principal principal = () -> "1";
        Long idCliente = 1L;
        Long idProtocollo = 1L;
        when(gestioneUtenzaServiceImpl.getById(1L)).thenReturn(preparatore);
        when(gestioneProtocolloServiceImpl.getByIdProtocollo(idProtocollo)).thenReturn(protocollo);
        when(gestioneUtenzaServiceImpl.existsByPreparatoreAndId(preparatore, idCliente)).thenReturn(true);



        MockHttpServletRequestBuilder requestBuilder =
                (MockMvcRequestBuilders.multipart(
                                "/api/v1/protocolli/1")
                        .param("idSchedaAlimentare", schedaAlimentare.getId().toString())
                        .param("idSchedaAllenamento","")
                        .principal(principal));
        requestBuilder.with(request -> {request.setMethod("PUT"); return request;});
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneProtocolloController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andDo(print()).andExpect(
                MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void modificaProtocolloSuccessSchedaAlimentareEmpty()
            throws Exception {

        Principal principal = () -> "1";
        Long idCliente = 1L;
        Long idProtocollo = 1L;
        when(gestioneUtenzaServiceImpl.getById(1L)).thenReturn(preparatore);
        when(gestioneProtocolloServiceImpl.getByIdProtocollo(idProtocollo)).thenReturn(protocollo);
        when(gestioneUtenzaServiceImpl.existsByPreparatoreAndId(preparatore, idCliente)).thenReturn(true);


        MockHttpServletRequestBuilder requestBuilder =
                (MockMvcRequestBuilders.multipart(
                                "/api/v1/protocolli/1")
                        .param("idSchedaAlimentare", "")
                        .param("idSchedaAllenamento",schedaAllenamento.getId().toString())
                        .principal(principal));
        requestBuilder.with(request -> {request.setMethod("PUT"); return request;});
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneProtocolloController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void modificaProtocolloErrorSchedeEmpty()
            throws Exception {
        Principal principal = () -> "1";
        Long idCliente = 1L;
        Long idProtocollo = 1L;
        when(gestioneUtenzaServiceImpl.getById(1L)).thenReturn(preparatore);
        when(gestioneProtocolloServiceImpl.getByIdProtocollo(idProtocollo)).thenReturn(protocollo);
        when(gestioneUtenzaServiceImpl.existsByPreparatoreAndId(preparatore, idCliente)).thenReturn(true);


        MockHttpServletRequestBuilder requestBuilder =
                (MockMvcRequestBuilders.multipart(
                                "/api/v1/protocolli/1")
                        .param("idSchedaAlimentare","")
                        .param("idSchedaAllenamento","")
                        .principal(principal));
        requestBuilder.with(request -> {request.setMethod("PUT"); return request;});
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneProtocolloController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isBadRequest());
    }
}
