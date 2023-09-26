package it.fitdiary.backend.gestioneesecuzioneesercizio.service;

import it.fitdiary.BackendApplicationTest;
import it.fitdiary.backend.entity.*;
import it.fitdiary.backend.gestioneesecuzioneesercizio.repository.IstanzaEsercizioEseguitoRepository;
import it.fitdiary.backend.gestioneprotocollo.repository.ProtocolloRepository;
import it.fitdiary.backend.gestioneprotocollo.service.GestioneProtocolloServiceImpl;
import it.fitdiary.backend.gestioneschedaallenamento.repository.IstanzaEsercizioRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BackendApplicationTest.class)
@ActiveProfiles("test")
public class GestioneIstanzaEsercizioEseguitoServiceImplTest {

    @Mock
    private IstanzaEsercizioEseguitoRepository istanzaEsercizioEseguitoRepository;
    @Mock
    private ProtocolloRepository protocolloRepository;
    @Mock
    private IstanzaEsercizioRepository istanzaEsercizioRepository;

    @InjectMocks
    private GestioneIstanzaEsercizioEseguitoServiceImpl gestioneIstanzaEsercizioEseguitoService;

    Utente preparatore;
    Protocollo protocollo;
    IstanzaEsercizio istanzaEsercizio;
    private Ruolo ruoloCliente;
    private Ruolo ruoloPreparatore;
    private Utente cliente;
    private Esercizio esercizio;
    private CategoriaEsercizio categoriaEsercizio;

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
    }

    @Test
    public void creaIstanzaEsercizioSuccess(){

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


        when(protocolloRepository.findById(protocollo.getId())).thenReturn(Optional.ofNullable(protocollo));


        when(istanzaEsercizioRepository.findById(istanzaEsercizio.getId())).thenReturn(Optional.ofNullable(istanzaEsercizio));
        IstanzaEsercizioEseguito istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);

        when(istanzaEsercizioEseguitoRepository.save(any())).thenReturn(istanzaEsercizioEseguito);

        assertEquals(istanzaEsercizioEseguito,
                gestioneIstanzaEsercizioEseguitoService.creazioneIstanzaEsercizio(
                        protocollo.getId(),
                        istanzaEsercizio.getId(),
                        istanzaEsercizioEseguito.getPesoEsecuzione(),
                        istanzaEsercizioEseguito.getDataEsecuzione(),
                        istanzaEsercizioEseguito.getNumeroSerie(),
                        istanzaEsercizioEseguito.getRipetizioni()
                ));
    }


    @Test
    public void visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(){
        IstanzaEsercizioEseguito istanzaEsercizioEseguito = new IstanzaEsercizioEseguito(1L,2,3,4, LocalDate.of(2022,12,12),protocollo,istanzaEsercizio);
        ArrayList<IstanzaEsercizioEseguito> istanzaEsercizioEseguiti = new ArrayList<>();
        istanzaEsercizioEseguiti.add(istanzaEsercizioEseguito);
        when(istanzaEsercizioEseguitoRepository.findAllByProtocolloAndIstanzaEsercizio(any(),any())).thenReturn(istanzaEsercizioEseguiti);

        assertEquals(istanzaEsercizioEseguiti,
                gestioneIstanzaEsercizioEseguitoService.visualizzaIstanzaEserciziEseguitiByProtocolloAndIstanzaEsercizio(
                        1l,
                        1l
                ));
    }

}
