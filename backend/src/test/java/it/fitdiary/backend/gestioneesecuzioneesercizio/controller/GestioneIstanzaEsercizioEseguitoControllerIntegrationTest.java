package it.fitdiary.backend.gestioneesecuzioneesercizio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fitdiary.backend.entity.*;
import it.fitdiary.backend.gestioneesecuzioneesercizio.controller.dto.IstanzaEsercizioEseguitoDTO;
import it.fitdiary.backend.gestioneprotocollo.repository.ProtocolloRepository;
import it.fitdiary.backend.gestioneschedaalimentare.controller.dto.CreaSchedaAlimentareDTO;
import it.fitdiary.backend.gestioneutenza.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GestioneIstanzaEsercizioEseguitoControllerIntegrationTest {

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
    private Utente cliente, cliente2;
    private Esercizio esercizio;
    private IstanzaEsercizioEseguitoDTO istanzaEsercizioEseguitoDTO;
    private CategoriaEsercizio categoriaEsercizio;

    private IstanzaEsercizioEseguito istanzaEsercizioEseguito;

    private String tokenCliente;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() throws IOException {
        categoriaEsercizio = new CategoriaEsercizio(1L,"TipoEsercizio");


        ruoloCliente = new Ruolo(3L, "CLIENTE", null, null);
        ruoloPreparatore = new Ruolo(2L, "PREPARATORE", null, null);
        cliente = utenteRepository.findByEmail("cliente@fitdiary.it");

        preparatore =
                new Utente(1L, "Daniele", "De Marco", "diodani5@gmail.com",
                        "Trappo#98", true, null, null, null, null,
                        null, null, null, ruoloPreparatore, null, null, null,
                        null, null);

        esercizio = new Esercizio(1L, "Chest press","EserciziPalestra/Air-Twisting-Crunch_waist.gif",categoriaEsercizio);

        protocollo = protocolloRepository.getById(3L);

        tokenCliente = setUpToken(cliente.getEmail(), "Password123!");
        istanzaEsercizio = new IstanzaEsercizio();
        istanzaEsercizio.setId(1L);
        istanzaEsercizio.setGiornoDellaSettimana(0);
        istanzaEsercizio.setSerie(5);
        istanzaEsercizio.setRecupero(1);
        istanzaEsercizio.setRipetizioni(1);
        istanzaEsercizio.setEsercizio(esercizio);
        istanzaEsercizio.setDescrizione("Descr");

        istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);

        istanzaEsercizioEseguitoDTO = new IstanzaEsercizioEseguitoDTO();
        istanzaEsercizioEseguitoDTO.setIdIstanzaEsercizio(1L);
        istanzaEsercizioEseguitoDTO.setData(LocalDate.of(2023,12,12));
        istanzaEsercizioEseguitoDTO.setRipetizioni(3);
        istanzaEsercizioEseguitoDTO.setSerie(3);
        istanzaEsercizioEseguitoDTO.setIdProtocollo(3L);
        istanzaEsercizioEseguitoDTO.setPesoEsecuzione(3F);

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
    public void creazioneIstanzaEsercizioTest_Success() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", tokenCliente);
        headers.setContentType(MediaType.APPLICATION_JSON);


        istanzaEsercizioEseguitoDTO = new IstanzaEsercizioEseguitoDTO();
        istanzaEsercizioEseguitoDTO.setIdIstanzaEsercizio(1L);
        istanzaEsercizioEseguitoDTO.setData(LocalDate.of(2023,12,12));
        istanzaEsercizioEseguitoDTO.setRipetizioni(3);
        istanzaEsercizioEseguitoDTO.setSerie(3);
        istanzaEsercizioEseguitoDTO.setIdProtocollo(4L);
        istanzaEsercizioEseguitoDTO.setPesoEsecuzione(3F);
        HttpEntity<IstanzaEsercizioEseguitoDTO> entity = new HttpEntity<>(istanzaEsercizioEseguitoDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/istanzaEserciziEseguiti/creaIstanzaEsercizio",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(org.springframework.http.HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    public void creazioneIstanzaEsercizioTestErrorNoIdProtocollo() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", tokenCliente);
        headers.setContentType(MediaType.APPLICATION_JSON);


        istanzaEsercizioEseguitoDTO = new IstanzaEsercizioEseguitoDTO();
        istanzaEsercizioEseguitoDTO.setIdIstanzaEsercizio(1L);
        istanzaEsercizioEseguitoDTO.setData(LocalDate.of(2023,12,12));
        istanzaEsercizioEseguitoDTO.setRipetizioni(3);
        istanzaEsercizioEseguitoDTO.setSerie(3);
        istanzaEsercizioEseguitoDTO.setIdProtocollo(null);
        istanzaEsercizioEseguitoDTO.setPesoEsecuzione(3F);
        HttpEntity<IstanzaEsercizioEseguitoDTO> entity = new HttpEntity<>(istanzaEsercizioEseguitoDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/istanzaEserciziEseguiti/creaIstanzaEsercizio",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }


    @Test
    public void creazioneIstanzaEsercizioTestErrorNoIdEsercizio() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", tokenCliente);
        headers.setContentType(MediaType.APPLICATION_JSON);


        istanzaEsercizioEseguitoDTO = new IstanzaEsercizioEseguitoDTO();
        istanzaEsercizioEseguitoDTO.setIdIstanzaEsercizio(null);
        istanzaEsercizioEseguitoDTO.setData(LocalDate.of(2023,12,12));
        istanzaEsercizioEseguitoDTO.setRipetizioni(3);
        istanzaEsercizioEseguitoDTO.setSerie(3);
        istanzaEsercizioEseguitoDTO.setIdProtocollo(3L);
        istanzaEsercizioEseguitoDTO.setPesoEsecuzione(3F);
        HttpEntity<IstanzaEsercizioEseguitoDTO> entity = new HttpEntity<>(istanzaEsercizioEseguitoDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/istanzaEserciziEseguiti/creaIstanzaEsercizio",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void visualizzaIstanzaEsercizioEseguitoSuccess() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", tokenCliente);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<IstanzaEsercizioEseguitoDTO> entity = new HttpEntity<>(istanzaEsercizioEseguitoDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/istanzaEserciziEseguiti/visualizzaIstanzaEsercizio?idProtocollo=3&idIstanzaEsercizio=1",
                HttpMethod.GET,
                entity,
                String.class
        );
        System.out.println(response);

        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void visualizzaIstanzaEsercizioEseguitoBadRequestNoIdProtocollo() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", tokenCliente);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<IstanzaEsercizioEseguitoDTO> entity = new HttpEntity<>(istanzaEsercizioEseguitoDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/istanzaEserciziEseguiti/visualizzaIstanzaEsercizio?idProtocollo=&idIstanzaEsercizio=1",
                HttpMethod.GET,
                entity,
                String.class
        );
        System.out.println(response);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void visualizzaIstanzaEsercizioEseguitoBadRequestNoIdIstanzaEse() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", tokenCliente);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<IstanzaEsercizioEseguitoDTO> entity = new HttpEntity<>(istanzaEsercizioEseguitoDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/istanzaEserciziEseguiti/visualizzaIstanzaEsercizio?idProtocollo=3&idIstanzaEsercizio=",
                HttpMethod.GET,
                entity,
                String.class
        );
        System.out.println(response);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
}
