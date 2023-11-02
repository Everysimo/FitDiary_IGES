package it.fitdiary.backend.gestionealimentoconsumato.controller;

import com.cloudinary.api.exceptions.BadRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.fitdiary.backend.entity.*;
import it.fitdiary.backend.entity.enums.GIORNO_SETTIMANA;
import it.fitdiary.backend.entity.enums.PASTO;
import it.fitdiary.backend.gestionealimentoconsumato.controller.dto.CreazioneIstanzaAlimentoConsumatoDto;
import it.fitdiary.backend.gestionealimentoconsumato.controller.dto.ListCreazioneIstanzaAlimentoConsumatoDto;
import it.fitdiary.backend.gestionealimentoconsumato.service.GestioneIstanzaAlimentoConsumatoService;
import it.fitdiary.backend.gestionealimentoconsumato.service.GestioneIstanzaAlimentoConsumatoServiceImpl;
import it.fitdiary.backend.gestioneesecuzioneesercizio.controller.GestioneIstanzaEsercizioEseguitoController;
import it.fitdiary.backend.gestioneprotocollo.service.GestioneProtocolloServiceImpl;
import it.fitdiary.backend.gestioneschedaalimentare.controller.TestObjectMapperConfig;
import it.fitdiary.backend.gestioneschedaalimentare.service.GestioneSchedaAlimentareService;
import it.fitdiary.backend.gestioneschedaalimentare.service.GestioneSchedaAlimentareServiceImpl;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {GestioneIstanzaAlimentiConsumatiController.class, TestObjectMapperConfig.class })
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class GestioneAlimentoConsumatoControllerTest {

    @Autowired
    private GestioneIstanzaAlimentiConsumatiController gestioneIstanzaAlimentiConsumatiController;
    @MockBean
    private GestioneIstanzaAlimentoConsumatoServiceImpl gestioneIstanzaAlimentoConsumatoService;
    @MockBean
    private GestioneSchedaAlimentareServiceImpl gestioneSchedaAlimentareService;
    @MockBean
    private GestioneProtocolloServiceImpl gestioneProtocolloService;
    @Autowired
    private ObjectMapper objectMapper;


    Utente preparatore;
    Protocollo protocollo;
    IstanzaEsercizio istanzaEsercizio;
    private Ruolo ruoloCliente;
    private Ruolo ruoloPreparatore;
    private Utente cliente;
    private IstanzaAlimentoConsumato istanzaAlimentoConsumato;
    private Alimento alimento;
    private IstanzaAlimento istanzaAlimento;

    private SchedaAlimentare schedaAlimentare;
    private CreazioneIstanzaAlimentoConsumatoDto creazioneIstanzaAlimentoConsumatoDto;

    private ListCreazioneIstanzaAlimentoConsumatoDto listCreazioneIstanzaAlimentoConsumatoDto;
    private List<CreazioneIstanzaAlimentoConsumatoDto> listCreazioneIstanzaAlimentoConsumato;

    private List<IstanzaAlimentoConsumato> listIstanzaAlimentoConsumato;
    @BeforeEach
    public void setUp() throws IOException {


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

        alimento = new Alimento(1L,"Pollo",100f,21f,46f,
                3f,"Alimenti/1.jpg");
        schedaAlimentare =
                new SchedaAlimentare();
        istanzaAlimento = new IstanzaAlimento(1L, GIORNO_SETTIMANA.LUNEDI, PASTO.COLAZIONE,16
                ,alimento,schedaAlimentare);

        istanzaAlimentoConsumato = new IstanzaAlimentoConsumato(1L,2, LocalDate.parse("2023-12-12"), protocollo, istanzaAlimento);

        creazioneIstanzaAlimentoConsumatoDto = new CreazioneIstanzaAlimentoConsumatoDto();
        creazioneIstanzaAlimentoConsumatoDto.setData(LocalDate.parse("2023-12-12"));
        creazioneIstanzaAlimentoConsumatoDto.setIstanzaAlimentoId(istanzaAlimento.getId());
        creazioneIstanzaAlimentoConsumatoDto.setGrammi(10);

        listCreazioneIstanzaAlimentoConsumato = new ArrayList<>();
        listCreazioneIstanzaAlimentoConsumato.add(creazioneIstanzaAlimentoConsumatoDto);

        listCreazioneIstanzaAlimentoConsumatoDto = new ListCreazioneIstanzaAlimentoConsumatoDto(protocollo.getId(),
                listCreazioneIstanzaAlimentoConsumato, LocalDate.parse("2023-12-12"));

        listIstanzaAlimentoConsumato = new ArrayList<>();
        listIstanzaAlimentoConsumato.add(istanzaAlimentoConsumato);
    }
    @Test
    public void creazioneIstanzaAlimentoConsumatoSuccess() throws Exception {
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

        alimento = new Alimento(1L,"Pollo",100f,21f,46f,
                3f,"Alimenti/1.jpg");
        schedaAlimentare =
                new SchedaAlimentare();
        istanzaAlimento = new IstanzaAlimento(1L, GIORNO_SETTIMANA.LUNEDI, PASTO.COLAZIONE,16
                ,alimento,schedaAlimentare);

        istanzaAlimentoConsumato = new IstanzaAlimentoConsumato(1L,2, LocalDate.parse("2023-12-12"), protocollo, istanzaAlimento);

        creazioneIstanzaAlimentoConsumatoDto = new CreazioneIstanzaAlimentoConsumatoDto();
        creazioneIstanzaAlimentoConsumatoDto.setData(LocalDate.parse("2023-12-12"));
        creazioneIstanzaAlimentoConsumatoDto.setIstanzaAlimentoId(istanzaAlimento.getId());
        creazioneIstanzaAlimentoConsumatoDto.setGrammi(10);

        listCreazioneIstanzaAlimentoConsumato = new ArrayList<>();
        listCreazioneIstanzaAlimentoConsumato.add(creazioneIstanzaAlimentoConsumatoDto);

        listCreazioneIstanzaAlimentoConsumatoDto = new ListCreazioneIstanzaAlimentoConsumatoDto(protocollo.getId(),
                listCreazioneIstanzaAlimentoConsumato, LocalDate.parse("2023-12-12"));


        Principal principal = () -> "1";
        when(gestioneProtocolloService.getByIdProtocollo(anyLong())).thenReturn(protocollo);
        when(gestioneIstanzaAlimentoConsumatoService.creazioneIstanzeEsercizio(
                listCreazioneIstanzaAlimentoConsumatoDto.getIdProtocollo(),
                listCreazioneIstanzaAlimentoConsumatoDto.getListaAlimenti(),
                listCreazioneIstanzaAlimentoConsumatoDto.getData())).thenReturn(listIstanzaAlimentoConsumato);

        String requestBody = objectMapper.writeValueAsString(listCreazioneIstanzaAlimentoConsumatoDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/istanzaAlimentiConsumati/creaIstanzaAlimentoConsumato")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneIstanzaAlimentiConsumatiController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().is2xxSuccessful());


    }

    @Test
    public void creazioneIstanzaAlimentoConsumatoUnauthorized() throws Exception {
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

        alimento = new Alimento(1L,"Pollo",100f,21f,46f,
                3f,"Alimenti/1.jpg");
        schedaAlimentare =
                new SchedaAlimentare();
        istanzaAlimento = new IstanzaAlimento(1L, GIORNO_SETTIMANA.LUNEDI, PASTO.COLAZIONE,16
                ,alimento,schedaAlimentare);

        istanzaAlimentoConsumato = new IstanzaAlimentoConsumato(1L,2, LocalDate.parse("2023-12-12"), protocollo, istanzaAlimento);

        creazioneIstanzaAlimentoConsumatoDto = new CreazioneIstanzaAlimentoConsumatoDto();
        creazioneIstanzaAlimentoConsumatoDto.setData(LocalDate.parse("2023-12-12"));
        creazioneIstanzaAlimentoConsumatoDto.setIstanzaAlimentoId(istanzaAlimento.getId());
        creazioneIstanzaAlimentoConsumatoDto.setGrammi(10);

        listCreazioneIstanzaAlimentoConsumato = new ArrayList<>();
        listCreazioneIstanzaAlimentoConsumato.add(creazioneIstanzaAlimentoConsumatoDto);

        listCreazioneIstanzaAlimentoConsumatoDto = new ListCreazioneIstanzaAlimentoConsumatoDto(protocollo.getId(),
                listCreazioneIstanzaAlimentoConsumato, LocalDate.parse("2023-12-12"));


        Principal principal = () -> "2";
        when(gestioneProtocolloService.getByIdProtocollo(anyLong())).thenReturn(protocollo);
        when(gestioneIstanzaAlimentoConsumatoService.creazioneIstanzeEsercizio(
                listCreazioneIstanzaAlimentoConsumatoDto.getIdProtocollo(),
                listCreazioneIstanzaAlimentoConsumatoDto.getListaAlimenti(),
                listCreazioneIstanzaAlimentoConsumatoDto.getData())).thenReturn(listIstanzaAlimentoConsumato);

        String requestBody = objectMapper.writeValueAsString(listCreazioneIstanzaAlimentoConsumatoDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/istanzaAlimentiConsumati/creaIstanzaAlimentoConsumato")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneIstanzaAlimentiConsumatiController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isUnauthorized());

    }

/*
    @Test
    public void creazioneIstanzaAlimentoConsumatoTestBadRequest() throws Exception {
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

        alimento = new Alimento(1L,"Pollo",100f,21f,46f,
                3f,"Alimenti/1.jpg");
        schedaAlimentare =
                new SchedaAlimentare();
        istanzaAlimento = new IstanzaAlimento(1L, GIORNO_SETTIMANA.LUNEDI, PASTO.COLAZIONE,16
                ,alimento,schedaAlimentare);

        istanzaAlimentoConsumato = new IstanzaAlimentoConsumato(1L,2, LocalDate.parse("2023-12-12"), protocollo, istanzaAlimento);

        creazioneIstanzaAlimentoConsumatoDto = new CreazioneIstanzaAlimentoConsumatoDto();
        creazioneIstanzaAlimentoConsumatoDto.setData(LocalDate.parse("2023-12-12"));
        creazioneIstanzaAlimentoConsumatoDto.setIstanzaAlimentoId(istanzaAlimento.getId());
        creazioneIstanzaAlimentoConsumatoDto.setGrammi(10);

        listCreazioneIstanzaAlimentoConsumato = new ArrayList<>();
        listCreazioneIstanzaAlimentoConsumato.add(creazioneIstanzaAlimentoConsumatoDto);

        listCreazioneIstanzaAlimentoConsumatoDto = new ListCreazioneIstanzaAlimentoConsumatoDto(protocollo.getId(),
                listCreazioneIstanzaAlimentoConsumato, LocalDate.parse("2023-12-12"));


        Principal principal = () -> "1";
        when(gestioneProtocolloService.getByIdProtocollo(anyLong())).thenReturn(protocollo);
        when(gestioneIstanzaAlimentoConsumatoService.creazioneIstanzeEsercizio(
                listCreazioneIstanzaAlimentoConsumatoDto.getIdProtocollo(),
                listCreazioneIstanzaAlimentoConsumatoDto.getListaAlimenti(),
                listCreazioneIstanzaAlimentoConsumatoDto.getData())).thenReturn(listIstanzaAlimentoConsumato);



        String requestBody = objectMapper.writeValueAsString(listCreazioneIstanzaAlimentoConsumatoDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/istanzaAlimentiConsumati/creaIstanzaAlimentoConsumato")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneIstanzaAlimentiConsumatiController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().isBadRequest());

    }

 */

    @Test
    public void visualizzaIstanzaAlimentiConsumatiByProtocolloAndIstanzaAlimentoAndDateSuccess() throws Exception {
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

        alimento = new Alimento(1L,"Pollo",100f,21f,46f,
                3f,"Alimenti/1.jpg");
        schedaAlimentare =
                new SchedaAlimentare();
        istanzaAlimento = new IstanzaAlimento(1L, GIORNO_SETTIMANA.LUNEDI, PASTO.COLAZIONE,16
                ,alimento,schedaAlimentare);

        istanzaAlimentoConsumato = new IstanzaAlimentoConsumato(1L,2, LocalDate.parse("2023-12-12"), protocollo, istanzaAlimento);

        creazioneIstanzaAlimentoConsumatoDto = new CreazioneIstanzaAlimentoConsumatoDto();
        creazioneIstanzaAlimentoConsumatoDto.setData(LocalDate.parse("2023-12-12"));
        creazioneIstanzaAlimentoConsumatoDto.setIstanzaAlimentoId(istanzaAlimento.getId());
        creazioneIstanzaAlimentoConsumatoDto.setGrammi(10);

        listCreazioneIstanzaAlimentoConsumato = new ArrayList<>();
        listCreazioneIstanzaAlimentoConsumato.add(creazioneIstanzaAlimentoConsumatoDto);

        listCreazioneIstanzaAlimentoConsumatoDto = new ListCreazioneIstanzaAlimentoConsumatoDto(protocollo.getId(),
                listCreazioneIstanzaAlimentoConsumato, LocalDate.parse("2023-12-12"));

        listIstanzaAlimentoConsumato = new ArrayList<>();
        listIstanzaAlimentoConsumato.add(istanzaAlimentoConsumato);
        Principal principal = () -> "1";
        when(gestioneProtocolloService.getByIdProtocollo(anyLong())).thenReturn(protocollo);
        when(gestioneIstanzaAlimentoConsumatoService.visualizzaIstanzaAlimentiConsumatiByProtocolloAndDate(
                listCreazioneIstanzaAlimentoConsumatoDto.getIdProtocollo(),
                listCreazioneIstanzaAlimentoConsumatoDto.getData())).thenReturn(listIstanzaAlimentoConsumato);

        String requestBody = objectMapper.writeValueAsString(listCreazioneIstanzaAlimentoConsumatoDto);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(
                                "/api/v1/istanzaAlimentiConsumati/visualizzaIstanzeAlimentoConsumato?idProtocollo="+
                                        listCreazioneIstanzaAlimentoConsumatoDto.getIdProtocollo().toString()+
                                        "&dataConsumazione=2023-12-12")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .principal(principal);
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(gestioneIstanzaAlimentiConsumatiController)
                        .build()
                        .perform(requestBuilder);
        actualPerformResult.andExpect(
                MockMvcResultMatchers.status().is2xxSuccessful());


    }

}
