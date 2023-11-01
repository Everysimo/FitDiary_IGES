package it.fitdiary.backend.gestionealimentoconsumato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fitdiary.backend.entity.*;
import it.fitdiary.backend.entity.enums.GIORNO_SETTIMANA;
import it.fitdiary.backend.entity.enums.PASTO;
import it.fitdiary.backend.gestionealimentoconsumato.controller.dto.CreazioneIstanzaAlimentoConsumatoDto;
import it.fitdiary.backend.gestionealimentoconsumato.controller.dto.ListCreazioneIstanzaAlimentoConsumatoDto;
import it.fitdiary.backend.gestioneesecuzioneesercizio.controller.dto.IstanzaEsercizioEseguitoDTO;
import it.fitdiary.backend.gestioneprotocollo.repository.ProtocolloRepository;
import it.fitdiary.backend.gestioneutenza.repository.UtenteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GestioneAlimentoConsumatoControllerIntegrationTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private ProtocolloRepository protocolloRepository;


    private Utente preparatore;
    private Protocollo protocollo;
    private IstanzaEsercizio istanzaEsercizio;
    private Ruolo ruoloCliente;
    private Ruolo ruoloPreparatore;
    private Utente cliente, cliente2, cliente3, cliente4;
    private Esercizio esercizio;
    private IstanzaEsercizioEseguitoDTO istanzaEsercizioEseguitoDTO;
    private CategoriaEsercizio categoriaEsercizio;

    private IstanzaEsercizioEseguito istanzaEsercizioEseguito;

    private String tokenCliente, tokenCliente2, tokenCliente3, tokenCliente4;

    private CreazioneIstanzaAlimentoConsumatoDto creazioneIstanzaAlimentoConsumatoDto;

    private ListCreazioneIstanzaAlimentoConsumatoDto listCreazioneIstanzaAlimentoConsumatoDto;
    private List<CreazioneIstanzaAlimentoConsumatoDto> listCreazioneIstanzaAlimentoConsumato;

    private IstanzaAlimentoConsumato istanzaAlimentoConsumato;

    private List<IstanzaAlimentoConsumato> listIstanzaAlimentoConsumato;
    private Alimento alimento;
    private SchedaAlimentare schedaAlimentare;
    private IstanzaAlimento istanzaAlimento;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() throws IOException {
        categoriaEsercizio = new CategoriaEsercizio(1L,"TipoEsercizio");


        ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);
        ruoloPreparatore = new Ruolo(2L, "PREPARATORE", null, null);
        cliente = utenteRepository.findByEmail("inapina@libero.it");
        cliente2 = utenteRepository.findByEmail("lmonaco@gmail.com");
        cliente3 = utenteRepository.findByEmail("cliente@fitdiary.it");
        cliente4 = utenteRepository.findByEmail("paloso@info.it");
        preparatore =
                new Utente(1L, "Daniele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true, null, null, null, null,
                        null, null, null, ruoloPreparatore, null, null, null,
                        null, null);

        esercizio = new Esercizio(1L, "Chest press","EserciziPalestra/Air-Twisting-Crunch_waist.gif",categoriaEsercizio);

        protocollo = protocolloRepository.getById(3L);

        tokenCliente = setUpToken(cliente.getEmail(), "Password123!");
        tokenCliente2 = setUpToken(cliente2.getEmail(), "Password123!");
        tokenCliente3 = setUpToken(cliente3.getEmail(), "Password123!");
        tokenCliente4 = setUpToken(cliente4.getEmail(), "Password123!");

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

    private String setUpToken(String email, String password) {
        MultiValueMap<String, Object>
                parts = new LinkedMultiValueMap<String, Object>();
        parts.add("email",email);
        parts.add("password",password);
        var c = restTemplate.postForEntity("http" +
                "://localhost:" + port + "/api" +
                "/v1/utenti/login", parts,Object.class);
        for(String cookie : Objects.requireNonNull(
                c.getHeaders().get(HttpHeaders.SET_COOKIE))){
            if(cookie.contains("accessToken")){
                return cookie + ";refreshToken=test";
            }
        }
        return null;
    }

    @Test
    @Order(1)
    public void creazioneIstanzaAlimentoConsumatoTestSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Cookie", tokenCliente3);
        headers.setContentType(MediaType.APPLICATION_JSON);




        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ListCreazioneIstanzaAlimentoConsumatoDto> entity = new HttpEntity<>(listCreazioneIstanzaAlimentoConsumatoDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/istanzaAlimentiConsumati/creaIstanzaAlimentoConsumato",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(org.springframework.http.HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    @Order(2)
    public void creazioneIstanzaAlimentoConsumatoTestUnauthorized() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", tokenCliente2);
        headers.setContentType(MediaType.APPLICATION_JSON);




        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ListCreazioneIstanzaAlimentoConsumatoDto> entity = new HttpEntity<>(listCreazioneIstanzaAlimentoConsumatoDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/istanzaAlimentiConsumati/creaIstanzaAlimentoConsumato",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    @Order(3)
    public void creazioneIstanzaAlimentoConsumatoTestBadRequest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", tokenCliente);
        headers.setContentType(MediaType.APPLICATION_JSON);


        listCreazioneIstanzaAlimentoConsumatoDto.setIdProtocollo(null);

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ListCreazioneIstanzaAlimentoConsumatoDto> entity = new HttpEntity<>(listCreazioneIstanzaAlimentoConsumatoDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/istanzaAlimentiConsumati/creaIstanzaAlimentoConsumato",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }

/*
    @Test
    @Order(4)
    public void visualizzaIstanzaAlimentiConsumatiByProtocolloAndIstanzaAlimentoAndDateTest_Success() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", tokenCliente4);
        headers.setContentType(MediaType.APPLICATION_JSON);




        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ListCreazioneIstanzaAlimentoConsumatoDto> entity = new HttpEntity<>(listCreazioneIstanzaAlimentoConsumatoDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/istanzaAlimentiConsumati/visualizzaIstanzeAlimentoConsumato?idProtocollo=4&dataConsumazione=2023-12-13",
                HttpMethod.GET,
                entity,
                String.class
        );

        System.out.println("errore:"+response.getStatusCode());
        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());

    }
*/
}
