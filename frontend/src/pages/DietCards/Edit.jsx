import React, {useContext, useEffect, useState} from 'react';
import {useForm} from 'react-hook-form';
import {
    Accordion,
    AccordionButton,
    AccordionIcon,
    AccordionItem,
    AccordionPanel,
    Box,
    Button,
    Flex,
    FormControl,
    FormErrorMessage,
    FormLabel,
    Heading,
    HStack,
    IconButton,
    Image,
    Input,
    InputGroup,
    InputLeftElement,
    Modal,
    ModalBody,
    ModalCloseButton,
    ModalContent,
    ModalFooter,
    ModalHeader,
    ModalOverlay,
    Table,
    TableCaption,
    Tbody,
    Td,
    Text,
    Th,
    Thead,
    Tooltip,
    Select,
    Tr,
    useDisclosure,
    useToast, Toast
} from "@chakra-ui/react";
import {FetchContext} from "../../context/FetchContext";
import {GradientBar} from "../../components/GradientBar";
import {AddIcon, SearchIcon, DeleteIcon} from "@chakra-ui/icons";
import moment from "moment/moment";
import {AuthContext} from "../../context/AuthContext";
import {useParams} from "react-router";

export default function Edit() {
    const fetchContext = useContext(FetchContext);
    const urlCreateSchedaALimentare = "schedaalimentare/modificaScheda";
    const {isOpen, onOpen, onClose} = useDisclosure()
    const {handleSubmit, formState: {errors, isSubmitting}} = useForm();
    const toast = useToast({
        duration: 1000, isClosable: true, variant: "solid", position: "top", containerStyle: {
            width: '100%', maxWidth: '100%',
        },
    })
    const {id} = useParams();
    const authContext = useContext(AuthContext)
    const {authState} = authContext;
    const [search, setSearch] = useState("");
    const [fetchCompleted, setFetchCompleted] = useState(false); // Nuovo stato
    let [schedaAlimentare, setSchedaAlimentare] = useState([[], [], [], [], [], [], []]);
    const [indexGiorno, setIndexGiorno] = useState(0);
    const [nomeScheda, setNomeScheda] = useState("");

    const onChange = (e) => {
        setSearch(e.target.value); // e evento target chi lancia l'evento e il value è il valore
    }
    const [toastMessage, setToastMessage] = useState(undefined);
    const [isLoading, setLoading] = useState(true); // ricarica la pagina quando la variabile termina
    const [listAlimenti, setAlimenti] = useState();

    const days = ["Lunedi", "Martedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato", "Domenica"];

    const protocol = window.location.protocol;
    const domain = window.location.host;
    const full = `${protocol}//${domain}`

    function toastParam(title, description, status) {
        return {
            title: title, description: description, status: status
        };
    }

    moment.locale("it-IT");


    useEffect(() => {
        if (toastMessage) {
            const {title, body, stat} = toastMessage;

            toast({
                title, description: body, status: stat, duration: 1000, isClosable: true
            });
        }
    }, [toastMessage, toast]);

    useEffect(() => {
        const loadlistaAlimenti = async () => {
            if (!fetchCompleted) {
                try {
                    const {data} = await fetchContext.authAxios("alimenti/getAllAlimenti");
                    setAlimenti(data.data);
                    setLoading(false); //viene settato a false per far capire di aver caricato tutti i dati
                } catch (error) {
                    setToastMessage({title: "Errore", body: error.message, stat: "error"});
                }
            }
        }

        function getIndexByGiorno(giorno)
        {
            switch (giorno)
            {
                case "LUNEDI":
                    return 0;
                case "MARTEDI":
                    return 1;
                case "MERCOLEDI":
                    return 2;
                case "GIOVEDI":
                    return 3;
                case "VENERDI":
                    return 4;
                case "SABATO":
                    return 5;
                case "DOMENICA":
                    return 6;
            }
            return -1;
        }

        const getSchedaAlimentare = async () => {
            if (!fetchCompleted) {
                try {
                    const {data} = await fetchContext.authAxios("schedaalimentare/getSchedaAlimentareById?idScheda=" + id);
                    let scheda=data.data.scheda_alimentare;
                    let nome=scheda.nome;
                    setNomeScheda(nome);
                    let tmpList=data.data.scheda_alimentare.listaAlimenti;

                    const raggruppatoPerGiorno = [[],[],[],[],[],[],[]];
                    tmpList.forEach(elemento => {
                        const giorno = elemento.giornoDellaSettimana;
                        let tmpIndex = getIndexByGiorno(giorno);
                        if(tmpIndex !== -1)
                        {
                            raggruppatoPerGiorno[tmpIndex].push(elemento);
                        }
                    });
                    let newScheda=[];
                    for(let i=0;i<7;i++)
                    {
                        let elemVett=[];
                        let tmpVett=raggruppatoPerGiorno[i];
                        for(let j=0;j<tmpVett.length;j++)
                        {
                            let objTmp=tmpVett[j];
                            elemVett.push(objTmp)
                        }
                        newScheda.push(elemVett);
                    }
                    setSchedaAlimentare(newScheda);
                    setLoading(false);
                    setFetchCompleted(true); // Imposta fetchCompleted a true dopo il completamento
                } catch (error) {
                    toast({
                        title: "ERROR",
                        description: "NOT AUTHORIZED",
                        status: "error"
                    })
                }
            }
        }
        loadlistaAlimenti();
        getSchedaAlimentare()
    }, [fetchContext, fetchCompleted]);

    let vettPasti = [
        {"ID": 0, "Nome": "Colazione","Key":"COLAZIONE"},
        {"ID": 1, "Nome": "Spuntino Mattina","Key":"SPUNTINO_COLAZIONE"},
        {"ID": 2, "Nome": "Pranzo","Key":"PRANZO"},
        {"ID": 3, "Nome": "Spuntino Pomeriggio","Key":"SPUNTINO_PRANZO"},
        {"ID": 4, "Nome": "Cena","Key":"CENA"},
        {"ID": 5, "Nome": "Spuntino Sera","Key":"SPUNTINO_CENA"},
        {"ID": 6, "Nome": "Extra","Key":"EXTRA"},
    ];

    function addAlimento(alimento, pasto, grammi) {
        let esiste=false;
        let i=0;
        let pastiGiorno=schedaAlimentare[indexGiorno];
        while(i<pastiGiorno.length && !esiste)
        {
            let pastoObj=pastiGiorno[i];
            if(pastoObj.pasto==pasto && pastoObj.alimento.id==alimento.id)
            {
                esiste=true;
            }
            i++;
        }

        if(!esiste)
        {
            let objTest={};
            objTest.alimento=alimento;
            objTest.pasto=vettPasti[pasto].Key;
            objTest.grammi=grammi;

            switch (indexGiorno)
            {
                case 0:
                    objTest.giornoDellaSettimana="LUNEDI";
                    break;
                case 1:
                    objTest.giornoDellaSettimana="MARTEDI";
                    break;
                case 2:
                    objTest.giornoDellaSettimana="MERCOLEDI";
                    break;
                case 3:
                    objTest.giornoDellaSettimana="GIOVEDI";
                    break;
                case 4:
                    objTest.giornoDellaSettimana="VENERDI";
                    break;
                case 5:
                    objTest.giornoDellaSettimana="SABATO";
                    break;
                case 6:
                    objTest.giornoDellaSettimana="DOMENICA";
                    break;
                default:
                    objTest.giornoDellaSettimana="LUNEDI"
                    break;
            }

            console.log(schedaAlimentare);
            let tmp=schedaAlimentare;
            tmp[indexGiorno].push(objTest);
            console.log(tmp);

            setSchedaAlimentare(tmp);

            toast(toastParam("Operazione eseguita!", "Alimento aggiunto con successo", "success"));
        }
        else
        {
            toast(toastParam("Attenzione!", "Hai già inserito questo alimento", "warning"));
        }
    }

    function formatData(inputData) {
        const formattedData = {
            schedaId: id,
            name: nomeScheda,
            istanzeAlimenti: [],
        };

        if (inputData && Array.isArray(inputData)) {
            for(let i=0;i<inputData.length;i++)
            {
                const instances = inputData[i];
                instances.forEach((instance) => {
                    console.log("Instanza:")
                    console.log(instance);
                    if (instance && instance.alimento) {
                        formattedData.istanzeAlimenti.push({
                            grammi: instance.grammi || 0,
                            giornoDellaSettimana: instance.giornoDellaSettimana || 0,
                            pasto: instance.pasto || "0",
                            idAlimento: instance.alimento.id || 0,
                        });
                    }
                });
            }

        }

        return formattedData;
    }

    const onSubmit = async (values) => {
        try {
            if(nomeScheda.length <= 0){
                document.getElementById("textErrNome").style.visibility = "visible";
                throw new Error()
            }
            let numeroAlimentiScheda = 0
            schedaAlimentare.forEach(el =>{
                numeroAlimentiScheda += el.length;
            })
            if(numeroAlimentiScheda <= 0) {
                document.getElementById("textErrCibi").style.visibility = "visible";
                throw new Error()
            }
        }catch (error) {
            return
        }

        try {
            let formattedScheda = formatData(schedaAlimentare)
            const {data} = await fetchContext.authAxios.post(urlCreateSchedaALimentare, formattedScheda);
            setSchedaAlimentare([[],[],[],[],[],[],[]]);
            setNomeScheda("");
            toast(toastParam("Scheda alimentare modificata con successo", "Scheda modificata!", "success"));
        } catch (error) {
            console.log(error.response.data.message)
            toast({
                title: 'Errore', description: error.response.data.message, status: 'error',
            })
        }
    }

    return (<>
        {!isLoading && (<Flex wrap={"wrap"} p={5}>
            <Flex alignItems={"center"} mb={5}>
                <Heading w={"full"}>Modifica Scheda Alimentare</Heading>
            </Flex>
            <Box bg={"white"} roundedTop={20} minW={{base: "100%", xl: "100%"}} h={"full"}>
                <GradientBar/>
                <Box pl={[0, 5, 20]} pr={[0, 5, 20]} pb={10} pt={5}>
                    <form style={{width: "100%"}} onSubmit={handleSubmit(onSubmit)}>
                        <FormControl id={"nome"} pt={5}>
                            <FormLabel htmlFor="nome">Nome delle scheda</FormLabel>
                            <Input type="text" value={nomeScheda} name={"nome"}
                                   onChange={(e) => {
                                       document.getElementById("textErrNome").style.visibility = "hidden";
                                       let newNome = e.target.value;
                                       setNomeScheda(newNome)
                                   }}/>
                            <FormErrorMessage>{errors.nome && errors.nome.message}</FormErrorMessage>
                        </FormControl>
                        <Text color={"red"} id={"textErrNome"} style={{visibility:"hidden"}}>Il nome della scheda è obbligatorio</Text>

                        <Modal isOpen={isOpen} onClose={onClose} isCentered={true} size={"5xl"}>
                            <ModalOverlay/>
                            <ModalContent>
                                <ModalHeader fontSize={'3xl'} textAlign={"center"}>Aggiungi alimenti alla
                                    scheda</ModalHeader>
                                <ModalCloseButton/>
                                <ModalBody align={"center"}>
                                    <Flex justify="center">
                                        <HStack align="center">
                                            <HStack>
                                                {!isLoading && listAlimenti && (<Flex wrap={"wrap"} p={5}>
                                                    <Box bg={"white"} roundedTop={20}
                                                         minW={{base: "100%", xl: "100%"}}
                                                         h={"full"}>
                                                        <GradientBar/>
                                                        <Box pl={10} pr={10} pb={5} pt={5}>
                                                            <HStack>
                                                                <InputGroup>
                                                                    <InputLeftElement
                                                                        pointerEvents="none"
                                                                        children={<SearchIcon
                                                                            color="gray.300"/>}
                                                                    />
                                                                    <Input
                                                                        className="SearchInput"
                                                                        type="text"
                                                                        onChange={onChange}
                                                                        placeholder="Cerca alimento"
                                                                    />
                                                                </InputGroup>
                                                            </HStack>
                                                            {/* Barra di ricerca*/}
                                                            <Text fontWeight={"bold"} mt={"4"} align={"left"}>Seleziona
                                                                un pasto:</Text>
                                                            <Select placeholder='Seleziona pasto' id={"selectPasto"} onChange={event =>
                                                                    document.getElementById("textErrMod").style.visibility = "hidden"}>
                                                                <option value='0'>Colazione</option>
                                                                <option value='1'>Spuntino Mattina</option>
                                                                <option value='2'>Pranzo</option>
                                                                <option value='3'>Spuntino Pomeriggio</option>
                                                                <option value='4'>Cena</option>
                                                                <option value='5'>Spuntino Serale</option>
                                                                <option value='6'>Extra</option>
                                                            </Select>
                                                            <Text color={"red"} id={"textErrMod"} style={{visibility:"hidden"}} align={"left"}>La selezione del pasto è obbligatoria</Text>
                                                            {listAlimenti.lista_alimenti.length > 0 ? (<>
                                                                <Text fontSize="xl" my={5}>
                                                                    Lista degli alimenti
                                                                </Text>

                                                                <Table variant={"striped"}
                                                                       alignContent={"center"}
                                                                       colorScheme={"gray"}
                                                                       size="md">
                                                                    <TableCaption>Lista
                                                                        Alimenti</TableCaption>
                                                                    <Thead bg="fitdiary.100">
                                                                        <Tr>
                                                                            <Th>Immagine</Th>
                                                                            <Th>Nome</Th>
                                                                            <Th>Kcal</Th>
                                                                            <Th>Proteine</Th>
                                                                            <Th>Grassi</Th>
                                                                            <Th>Carboidrati</Th>
                                                                            <Th>Azione</Th>
                                                                        </Tr>
                                                                    </Thead>
                                                                    <Tbody>
                                                                        {listAlimenti.lista_alimenti.map((alimento) => (alimento.nome.toLowerCase().startsWith(search.toLowerCase()) || search === "") && (
                                                                            <Tr key={alimento.id}>
                                                                                <Td p={1}
                                                                                    m={0}>
                                                                                    <Image
                                                                                        objectFit='contain'
                                                                                        boxSize={100}
                                                                                        src={full + "/" + alimento.pathFoto}
                                                                                        alt='Foto non disponibile'/>
                                                                                </Td>
                                                                                <Td>{alimento.nome}</Td>
                                                                                <Td>{alimento.kcal}</Td>
                                                                                <Td>{alimento.proteine}</Td>
                                                                                <Td>{alimento.grassi}</Td>
                                                                                <Td>{alimento.carboidrati}</Td>
                                                                                <Td><Button
                                                                                    colorScheme='fitdiary'
                                                                                    onClick={() => {
                                                                                        let idPasto = document.getElementById("selectPasto").value;
                                                                                        if (idPasto.length > 0) {
                                                                                            addAlimento(alimento, idPasto, 150);
                                                                                        } else {
                                                                                            document.getElementById("textErrMod").style.visibility = "visible";
                                                                                        }
                                                                                    }}
                                                                                    fontSize={"s"}>
                                                                                    <AddIcon/>
                                                                                </Button></Td>
                                                                            </Tr>))}
                                                                    </Tbody>
                                                                </Table>
                                                            </>) : (<Heading py={5}
                                                                             textAlign={"center"}>
                                                                Non c'è niente qui...
                                                            </Heading>)}
                                                        </Box>
                                                    </Box>
                                                </Flex>)}
                                            </HStack>
                                        </HStack>
                                    </Flex>
                                </ModalBody>
                                <ModalFooter alignItems={"center"}>
                                </ModalFooter>
                            </ModalContent>
                        </Modal>

                        <Accordion allowToggle defaultIndex={[0]} w="full" mt={"60px"}>
                            {days.map((d, i) => {
                                return (
                                    <AccordionItem key={i}>
                                        <h2>
                                            <AccordionButton>
                                                <Box flex='1' textAlign='center' fontWeight={"extrabold"} fontSize={"xl"}>
                                                    {d}
                                                </Box>
                                                <AccordionIcon/>
                                            </AccordionButton>
                                        </h2>
                                        <AccordionPanel pb={4}>
                                            {vettPasti.map((pasto, index) => {
                                                return (
                                                    <div key={index}>
                                                        <Box flex='1' textAlign='center' fontWeight={"extrabold"} fontSize={"xl"}>
                                                            <Text fontSize={"21"} color={"blue"}
                                                                  fontWeight={"semibold"}>{pasto.Nome}</Text>
                                                        </Box>
                                                        {schedaAlimentare[i].filter((t) => t.pasto===pasto.Key && d.toUpperCase() === t.giornoDellaSettimana).map((al, key) => {
                                                            let alimento = al.alimento;
                                                            let caloreCalc = (alimento.kcal / 100) * al.grammi;
                                                            return (
                                                                <>
                                                                    <Table borderBottom={"solid 1px "}
                                                                           borderColor={"blue.200"} key={key}
                                                                           variant="unstyled" size="md">
                                                                        <Thead>
                                                                            <Tr>
                                                                                <Th>Immagine</Th>
                                                                                <Th>Nome</Th>
                                                                                <Th>Kcal</Th>
                                                                                <Th>Proteine</Th>
                                                                                <Th>Grassi</Th>
                                                                                <Th>Carboidrati</Th>
                                                                                <Th>Grammi</Th>
                                                                                <Th>Azioni</Th>
                                                                            </Tr>
                                                                        </Thead>
                                                                        <Tbody>
                                                                            <Tr>
                                                                                <Td
                                                                                    p={1}
                                                                                    m={0}>
                                                                                    <Image
                                                                                        objectFit='contain'
                                                                                        boxSize={100}
                                                                                        src={full + "/" + alimento.pathFoto}
                                                                                        alt='Foto non disponibile'/>
                                                                                </Td>
                                                                                <Td maxWidth={100}>{alimento.nome}</Td>
                                                                                <Td maxWidth={100}>{parseInt(caloreCalc)}</Td>
                                                                                <Td maxWidth={100}>{parseInt(alimento.proteine)}</Td>
                                                                                <Td maxWidth={100}>{parseInt(alimento.grassi)}</Td>
                                                                                <Td maxWidth={100}>{parseInt(alimento.carboidrati)}</Td>
                                                                                <Td maxWidth={100}>
                                                                                    <Input
                                                                                        placeholder={al.grammi}
                                                                                        w={20}
                                                                                        min={1}
                                                                                        max={10000}
                                                                                        type={"number"}
                                                                                        defaultValue={al.grammi}
                                                                                        onChange={(e) => {
                                                                                            if (e.target.value > 0) {
                                                                                                if (e.target.value > 10000) {
                                                                                                    e.target.value = 10000
                                                                                                }
                                                                                                let count = key;
                                                                                                for(let j=0; j < schedaAlimentare[i].length; j++) {
                                                                                                    if(schedaAlimentare[i][j].pasto == index) {
                                                                                                        if(count > 0) {
                                                                                                            count--
                                                                                                        }else {
                                                                                                            schedaAlimentare[i][j].grammi = e.target.value
                                                                                                            break
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            } else {
                                                                                                let count = key;
                                                                                                for(let j=0; j < schedaAlimentare[i].length; j++) {
                                                                                                    if(schedaAlimentare[i][j].pasto == index) {
                                                                                                        if(count > 0) {
                                                                                                            count--
                                                                                                        }else {
                                                                                                            schedaAlimentare[i][j].grammi = 100
                                                                                                            break
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            let newV = [...schedaAlimentare];
                                                                                            setSchedaAlimentare(newV)
                                                                                        }}/>


                                                                                </Td>
                                                                                <Td>
                                                                                    <IconButton colorScheme={"red"}
                                                                                                onClick={() => {
                                                                                                    if (window.confirm("Sei sicuro di voler eliminare l'alimento?")) {
                                                                                                        schedaAlimentare[i].splice(key, 1);
                                                                                                        let newV = [...schedaAlimentare];
                                                                                                        setSchedaAlimentare(newV);
                                                                                                    }
                                                                                                }}
                                                                                                aria-label={"Pulsante che elimina elemento"}>
                                                                                        <DeleteIcon/>
                                                                                    </IconButton>
                                                                                </Td>
                                                                            </Tr>
                                                                        </Tbody>
                                                                    </Table>
                                                                </>
                                                            );
                                                        })}
                                                    </div>
                                                );
                                            })}
                                            <Flex pt={5}>
                                                <Button
                                                    w="full"
                                                    colorScheme='fitdiary'
                                                    onClick={() => {
                                                        document.getElementById("textErrCibi").style.visibility = "hidden";
                                                        setIndexGiorno(i);
                                                        onOpen();
                                                    }}>
                                                    Aggiungi alimenti</Button>
                                            </Flex>
                                        </AccordionPanel>
                                    </AccordionItem>

                                )
                            })}
                            <Text color={"red"} id={"textErrCibi"} style={{visibility:"hidden"}}>Inserire almeno un pasto!</Text>

                        </Accordion>
                        <Button w="full" mt={4} colorScheme='fitdiary' isLoading={isSubmitting} type='submit'>
                            Salva Scheda
                        </Button>
                    </form>
                </Box>
            </Box>
        </Flex>)}
    </>);
}
