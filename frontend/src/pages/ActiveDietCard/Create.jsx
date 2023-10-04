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
    useToast,
    Toast
} from "@chakra-ui/react";
import {FetchContext} from "../../context/FetchContext";
import {GradientBar} from "../../components/GradientBar";
import {AddIcon, SearchIcon, DeleteIcon} from "@chakra-ui/icons";
import moment from "moment/moment";
import {AuthContext} from "../../context/AuthContext";
import {useParams} from "react-router";

export default function Edit() {
    const urlGetIstanzeAlimentiConsumati = "istanzaAlimentiConsumati/visualizzaIstanzeAlimentoConsumato";
    const fetchContext = useContext(FetchContext);
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
    const [listaAlimentiConsumati, setListaAlimentiConsumati] = useState([]);
    const [listaAlimentiScheda, setListaAlimentiScheda] = useState([]);
    const [indexGiorno, setIndexGiorno] = useState(0);
    const [nomeScheda, setNomeScheda] = useState("");
    const [kcalAssunte, setkcalAssunte] = useState(0);

    const [isLoading, setisLoading] = useState(false);
    const [idProtocollo, setIdProtocollo] = useState(false);

    const onChange = (e) => {
        setSearch(e.target.value);
    }
    const [toastMessage, setToastMessage] = useState(undefined);
    const [listAlimenti, setAlimenti] = useState();

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
        setisLoading(true);
        const getAlimentiConsumati = async () => {
            try {
                const url = new URL(window.location.href);
                setIdProtocollo(url.searchParams.get("idProtocollo"));
                const dataConsumazione = url.searchParams.get("dataConsumazione");

                console.log(`${urlGetIstanzeAlimentiConsumati}?idProtocollo=${url.searchParams.get("idProtocollo")}&dataConsumazione=${dataConsumazione}`)

                const {data} = await fetchContext.authAxios(`${urlGetIstanzeAlimentiConsumati}?idProtocollo=${url.searchParams.get("idProtocollo")}&dataConsumazione=${dataConsumazione}`)

                console.log(data.data.result)

                setListaAlimentiConsumati(data.data.result.istanzeAlimentiConsumati);
                setListaAlimentiScheda(data.data.result.istanzeAlimenti)

                setisLoading(false)

            } catch (error) {
                setToastMessage({title: "Error", body: error.message, stat: "error"})
            }
        }


        getAlimentiConsumati();
    }, [fetchContext]);
    let vettPasti = [{"ID": 0, "Nome": "Colazione", "Key": "COLAZIONE"}, {
        "ID": 1,
        "Nome": "Spuntino Mattina",
        "Key": "SPUNTINO_COLAZIONE"
    }, {"ID": 2, "Nome": "Pranzo", "Key": "PRANZO"}, {
        "ID": 3,
        "Nome": "Spuntino Pomeriggio",
        "Key": "SPUNTINO_PRANZO"
    }, {"ID": 4, "Nome": "Cena", "Key": "CENA"}, {"ID": 5, "Nome": "Spuntino Sera", "Key": "SPUNTINO_CENA"}, {
        "ID": 6,
        "Nome": "Extra",
        "Key": "EXTRA"
    },];

    function addAlimento(alimento, pasto, grammi) {
        let esiste = false;

        let i=0;
        while(i<listaAlimentiConsumati.length && !esiste)
        {
            let pastoObj=listaAlimentiConsumati[i];
            if(pastoObj.pasto==pasto && pastoObj.alimento.id==alimento.id)
            {
                esiste=true;
            }
            i++;
        }

         if (!esiste) {
             let objTest = {};
             objTest.alimento = alimento;
             objTest.pasto = pasto;
             objTest.grammi = grammi;

             let tmp = listaAlimentiConsumati;
             tmp.push(objTest);
             setListaAlimentiConsumati(tmp);
             console.log(listaAlimentiConsumati)
            toast(toastParam("Operazione eseguita!", "Alimento aggiunto con successo", "success"));
        } else {
            toast(toastParam("Attenzione!", "Hai già inserito questo alimento", "warning"));
        }
    }

    function formatData(inputData) {
        const formattedData = {
            schedaId: id, name: nomeScheda, istanzeAlimenti: [],
        };

        if (inputData && Array.isArray(inputData)) {
            for (let i = 0; i < inputData.length; i++) {
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

            let numeroAlimentiScheda = 0
            if (numeroAlimentiScheda <= 0) {
                toast(toastParam("Attenzione!", "Inserisci almeno un alimento", "error"));
                throw new Error()
            }
        } catch (error) {
            return
        }

        try {
            toast(toastParam("Sceheda Alimentare modificata con successo", "Scheda modificata!", "success"));
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
                <Heading w={"full"}>Attvità Scheda Alimentare</Heading>
            </Flex>
            <Box bg={"white"} roundedTop={20} minW={{base: "100%", xl: "100%"}} h={"full"}>
                <GradientBar/>
                <Box pl={[0, 5, 20]} pr={[0, 5, 20]} pb={10} pt={5}>
                    <form style={{width: "100%"}} onSubmit={handleSubmit(onSubmit)}>
                        <Modal isOpen={isOpen} onClose={onClose} isCentered={true} size={"5xl"}>
                            <ModalOverlay/>
                            <ModalContent>
                                <ModalHeader fontSize={'3xl'} textAlign={"center"}>Aggiungi alimenti che hai consumato</ModalHeader>
                                <ModalCloseButton/>
                                <ModalBody align={"center"}>
                                    <Flex justify="center">
                                        <HStack align="center">
                                            <HStack>
                                                {!isLoading && listaAlimentiScheda && (<Flex wrap={"wrap"} p={5}>
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
                                                            {listaAlimentiScheda.length > 0 ? (<>
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
                                                                        {listaAlimentiScheda.map((alimento) => (alimento.alimento.nome.toLowerCase().startsWith(search.toLowerCase()) || search === "") && (
                                                                            <Tr key={alimento.id}>
                                                                                <Td p={1}
                                                                                    m={0}>
                                                                                    <Image
                                                                                        objectFit='contain'
                                                                                        boxSize={100}
                                                                                        src={full + "/" + alimento.alimento.pathFoto}
                                                                                        alt='Foto non disponibile'/>
                                                                                </Td>
                                                                                <Td><Text fontWeight={"bold"}>{alimento.pasto}</Text>{alimento.alimento.nome}</Td>
                                                                                <Td>{alimento.alimento.kcal}</Td>
                                                                                <Td>{alimento.alimento.proteine}</Td>
                                                                                <Td>{alimento.alimento.grassi}</Td>
                                                                                <Td>{alimento.alimento.carboidrati}</Td>
                                                                                <Td><Button
                                                                                    colorScheme='fitdiary'
                                                                                    onClick={() => {
                                                                                        addAlimento(alimento.alimento, alimento.pasto, 150);
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

                        <Box>
                            {vettPasti.map((pasto, index) => {
                                return (<div key={index}>
                                        <Box flex='1' textAlign='center' fontWeight={"extrabold"}
                                             fontSize={"xl"}>
                                            <Text fontSize={"21"} color={"blue"}
                                                  fontWeight={"semibold"}>{pasto.Nome}</Text>
                                        </Box>
                                        {listaAlimentiConsumati && listaAlimentiConsumati.length > 0 && (
                                            listaAlimentiConsumati.filter((t) => t.pasto == pasto.Key).map((al, key) => {
                                            let alimento = al.alimento;
                                            let caloreCalc = (alimento.kcal / 100) * al.grammi;
                                            return (<>
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
                                                                                for (let j = 0; j < listaAlimentiConsumati.length; j++) {
                                                                                    if (listaAlimentiConsumati.pasto == index) {
                                                                                        if (count > 0) {
                                                                                            count--
                                                                                        } else {
                                                                                            listaAlimentiConsumati.grammi = e.target.value
                                                                                            break
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                let count = key;
                                                                                for (let j = 0; j < listaAlimentiConsumati.length; j++) {
                                                                                    if (listaAlimentiConsumati.pasto == index) {
                                                                                        if (count > 0) {
                                                                                            count--
                                                                                        } else {
                                                                                            listaAlimentiConsumati.grammi = 100
                                                                                            break
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            let newV = listaAlimentiConsumati;
                                                                            setListaAlimentiConsumati(newV)
                                                                        }}/>


                                                                </Td>
                                                                <Td>
                                                                    <IconButton colorScheme={"red"}
                                                                                onClick={() => {
                                                                                    if (window.confirm("Sei sicuro di voler eliminare l'alimento?")) {
                                                                                        listaAlimentiConsumati.splice(key, 1);
                                                                                        let newV = listaAlimentiConsumati;
                                                                                        setListaAlimentiConsumati(newV);
                                                                                    }
                                                                                }}
                                                                                aria-label={"Pulsante che elimina elemento"}>
                                                                        <DeleteIcon/>
                                                                    </IconButton>
                                                                </Td>
                                                            </Tr>
                                                        </Tbody>
                                                    </Table>
                                                </>);
                                        }))}
                                    </div>);
                            })}
                            <Flex pt={5}>
                                <Button
                                    w="full"
                                    colorScheme='fitdiary'
                                    onClick={() => {
                                        onOpen();
                                    }}>
                                    Aggiungi alimenti</Button>
                            </Flex>
                            <Button w="full" mt={4} colorScheme='fitdiary' isLoading={isSubmitting} type='submit'>
                                Salva Scheda
                            </Button>
                            </Box>
                    </form>
                </Box>
            </Box>
        </Flex>)}
    </>);
}
