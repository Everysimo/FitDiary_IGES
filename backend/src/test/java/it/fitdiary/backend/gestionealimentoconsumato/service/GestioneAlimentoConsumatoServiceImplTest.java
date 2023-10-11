package it.fitdiary.backend.gestionealimentoconsumato.service;

import it.fitdiary.BackendApplicationTest;
import it.fitdiary.backend.entity.*;
import it.fitdiary.backend.entity.enums.GIORNO_SETTIMANA;
import it.fitdiary.backend.entity.enums.PASTO;
import it.fitdiary.backend.gestionealimentoconsumato.controller.dto.CreazioneIstanzaAlimentoConsumatoDto;
import it.fitdiary.backend.gestionealimentoconsumato.repository.IstanzaAlimentoConsumatoRepository;
import it.fitdiary.backend.gestioneprotocollo.repository.ProtocolloRepository;
import it.fitdiary.backend.gestioneschedaalimentare.repository.IstanzaAlimentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BackendApplicationTest.class)
@ActiveProfiles("test")
public class GestioneAlimentoConsumatoServiceImplTest {

    @Mock
    private ProtocolloRepository protocolloRepository;
    @Mock
    private IstanzaAlimentoRepository istanzaAlimentoRepository;
    @Mock
    private IstanzaAlimentoConsumatoRepository istanzaAlimentoConsumatoRepository;
    @InjectMocks
    private GestioneIstanzaAlimentoConsumatoServiceImpl gestioneIstanzaAlimentoConsumatoService;


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
    }

    @Test
    public void creazioneIstanzaAlimentoConsumatoSuccess(){

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

        List<IstanzaAlimentoConsumato> istanzaAlimentoConsumatoList;
        istanzaAlimentoConsumatoList = new ArrayList<>();
        istanzaAlimentoConsumatoList.add(istanzaAlimentoConsumato);

        List<CreazioneIstanzaAlimentoConsumatoDto> istanzaAlimentoConsumatoDtos = new ArrayList<>();
        istanzaAlimentoConsumatoDtos.add(creazioneIstanzaAlimentoConsumatoDto);

        when(protocolloRepository.findById(protocollo.getId())).thenReturn(Optional.ofNullable(protocollo));

        when(istanzaAlimentoRepository.findById(any())).thenReturn(Optional.ofNullable(istanzaAlimento));

        when(istanzaAlimentoConsumatoRepository.saveAll(any())).thenReturn(istanzaAlimentoConsumatoList);

        assertEquals(istanzaAlimentoConsumatoList,
                gestioneIstanzaAlimentoConsumatoService.creazioneIstanzeEsercizio(
                        protocollo.getId(),
                        istanzaAlimentoConsumatoDtos,
                        LocalDate.parse("2023-10-18")
                ));

    }

    @Test
    public void visualizzaIstanzaAlimentiConsumatiByProtocolloAndDateSuccess(){

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

        List<IstanzaAlimentoConsumato> istanzaAlimentoConsumatoList;
        istanzaAlimentoConsumatoList = new ArrayList<>();
        istanzaAlimentoConsumatoList.add(istanzaAlimentoConsumato);

        List<CreazioneIstanzaAlimentoConsumatoDto> istanzaAlimentoConsumatoDtos = new ArrayList<>();
        istanzaAlimentoConsumatoDtos.add(creazioneIstanzaAlimentoConsumatoDto);


        when(istanzaAlimentoConsumatoRepository.findAllByProtocolloAndDataConsumazione(any(),any())).thenReturn(istanzaAlimentoConsumatoList);

        assertEquals(istanzaAlimentoConsumatoList,
                gestioneIstanzaAlimentoConsumatoService.visualizzaIstanzaAlimentiConsumatiByProtocolloAndDate(
                        protocollo.getId(),
                        LocalDate.parse("2023-10-18")
                ));

    }

}
