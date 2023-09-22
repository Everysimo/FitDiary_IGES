import {
  Box,
  Button,
  Flex,
  FormControl,
  FormErrorMessage,
  FormLabel,
  GridItem,
  Heading,
  HStack,
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
  SimpleGrid,
  Table,
  TableCaption,
  Tbody,
  Td,
  Text,
  Th,
  Thead,
  Tr, useDisclosure,
  useToast,
  VStack
} from "@chakra-ui/react";
import {useForm} from "react-hook-form"
import React, {useCallback, useContext, useEffect, useState} from "react";
import {FetchContext} from "../../context/FetchContext";
import {GradientBar} from "../../components/GradientBar";
import {useNavigate, useParams} from "react-router";

const urlScheda = "schedaAllenamento/getSchedaAllenamentoById";

const View = () => {
  const fetchContext = useContext(FetchContext);
  const navigate = useNavigate();
  const { id } = useParams();
  const [options, setOptions] = useState(null);
  const [isLoading, setisLoading] = useState(false);
  const [schedaAllenamento, setSchedaAllenamento] = useState({
    nome:"",
    listaEsercizi:[]
  });
  const { register, handleSubmit, setValue, formState: { errors} } = useForm();
  const [toastMessage, setToastMessage] = useState(undefined);
  const [search, setSearch] = useState("");


  function toastParam(title, description, status) {
    return {
      title: title, description: description, status: status
    };
  }
  const toast = useToast({
    duration: 3000,
    isClosable: true,
    variant: "solid",
    containerStyle: {
      width: '100%',
      maxWidth: '100%',
    },
  })

  useEffect(() => {
    if (toastMessage) {
      const { title, body, stat } = toastMessage;

      toast({
        title,
        description: body,
        status: stat,
      });
    }
    return () => {
      setTimeout(() => {
        setToastMessage(undefined);
      },1000);
    }
  }, [toastMessage, toast]);

  useEffect(() => {
    setisLoading(true);
    const getScheda = async () => {
      try {
        const {data} = await fetchContext.authAxios(urlScheda+"?idScheda="+id);
        let schedaAll=data.data.scheda_allenamento;
        console.log(schedaAll);
        setSchedaAllenamento(schedaAll);
        setisLoading(false);
      } catch (error) {
        setToastMessage({title:"Error",body:error.message,stat:"error"})
      }
    }
    getScheda();

  }, [fetchContext])


  const data = [
  ];


  //Verifica se una data inserita è precedenta alla odierna
  function isValidDate(value) {
    var date = new Date();
    date.setHours(23, 59, 59, 59);
    return (!isNaN(Date.parse(value)) && (new Date(value) > date) ? true : "Inserisci una data valida");
  }

  return (
    <>
      <Flex wrap={"wrap"} p={5}>
        <Flex alignItems={"center"} mb={5}>
          <Heading w={"full"}>{schedaAllenamento.nome}</Heading>
        </Flex>
        <Box bg={"white"} roundedTop={20} minW={{ base: "100%", xl: "100%" }} h={"full"} >
          <GradientBar />

          <VStack spacing={3} alignItems="center" pb={5} mt={5}>
            <form style={{ width: "100%" }}>
              <SimpleGrid columns={2} columnGap={5} rowGap={5} pl={[0, 5, 10]} pr={[0, 5, 10]} w="full">
                <Text fontWeight={"bold"}>Allenamenti</Text>
                  <GridItem colSpan={2}>
                    <Table variant="simple" style={{borderCollapse:"separate", borderSpacing:"0 1em"}}>
                      <Thead>
                        <Tr>
                          <Th width={"90%"}>Nome</Th>
                          <Th>Azioni</Th>
                        </Tr>
                      </Thead>
                      <Tbody>
                        {schedaAllenamento.listaEsercizi.map((item) => (
                            <Tr>
                              <Td bg="gray.200" fontWeight={"bold"}>{item.esercizio.nome}</Td>
                              <Td bg="gray.200">
                                <Flex justifyContent="flex-end">
                                  <Button
                                      onClick={() => {
                                        navigate("../allenamenti/esegui?idIstanzaEsercizio="+item.id+"&idProtocollo="+id);
                                      }}
                                      bg="blue.500"
                                      rounded="full"
                                      color="white"
                                      _hover={{ bg: 'blue.600' }}
                                      >
                                    Esegui
                                  </Button>
                                </Flex>
                              </Td>
                            </Tr>
                        ))}
                      </Tbody>
                    </Table>
                  </GridItem>
                <GridItem colSpan={2} >
                  <Button colorScheme="fitdiary" type={"submit"} w="full">Termina Scheda</Button>
                </GridItem>
              </SimpleGrid>
            </form>
          </VStack>
        </Box>
      </Flex>
    </>
  )
}

export default View;
