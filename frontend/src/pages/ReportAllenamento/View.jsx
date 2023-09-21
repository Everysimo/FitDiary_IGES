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
import {Link as ReactLink} from "react-router-dom";
import React, {useCallback, useContext, useEffect, useState} from "react";
import {useDropzone} from 'react-dropzone';
import {FetchContext} from "../../context/FetchContext";
import { GiMeal } from "react-icons/gi";
import {IoIosFitness} from "react-icons/io";
import {AddIcon, CloseIcon, InfoIcon, SearchIcon} from "@chakra-ui/icons";
import Select from "react-select";
import {GradientBar} from "../../components/GradientBar";
import {useNavigate} from "react-router";

const urlProtocolli = "protocolli";
const urlUtenti = "utenti";

const View = () => {
  const fetchContext = useContext(FetchContext);
  const navigate = useNavigate();
  const [options, setOptions] = useState([{}]);
  const [isLoading, setisLoading] = useState(false);
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
    const getUsers = async () => {
      try {
        const { data: { data: { clienti } } } = await fetchContext.authAxios(urlUtenti);
        setOptions(
          clienti.map((e) => {
            return { value: e.id, label: e.nome };
          })
        );
        setisLoading(false);
      } catch (error) {
        setToastMessage({title:"Error",body:error.message,stat:"error"})
      }
    }
    getUsers();
  }, [fetchContext])


  const data = [
    { id: 1, name: 'Allenamento 1' },
    { id: 2, name: 'Allenamento 2' },
    { id: 3, name: 'Allenamento 3' },
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
          <Heading w={"full"}>Scheda allenamento del GIORNO/MESE</Heading>
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
                        {data.map((item) => (
                            <Tr>
                              <Td bg="gray.200" fontWeight={"bold"}>{item.name}</Td>
                              <Td bg="gray.200">
                                <Flex justifyContent="flex-end">
                                  <Button
                                      onClick={() => {
                                        navigate("./esegui/"+item.id)
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
