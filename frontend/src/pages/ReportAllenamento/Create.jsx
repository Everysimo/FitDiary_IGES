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
  Stack,
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
import {Timer} from "../../components/Timer";

const urlProtocolli = "protocolli";
const urlUtenti = "utenti";

const Create = () => {
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



    const [exerciseData, setExerciseData] = useState([]);
    const [newData, setNewData] = useState({ weight: "", sets: "", reps: "" });

    const handleInputChange = (e) => {
      const { name, value } = e.target;
      setNewData({ ...newData, [name]: value });
    };

    const handleAddData = () => {
      if (newData.weight && newData.sets && newData.reps) {
        setExerciseData([...exerciseData, newData]);
        setNewData({ weight: "", sets: "", reps: "" });
      }
    };

  return (
      <>
        <Flex wrap={"wrap"} p={5}>
          <Flex alignItems={"center"} mb={5}>
            <Heading w={"full"}>NOME_ESERCIZIO</Heading>
          </Flex>
          <Box bg={"white"} roundedTop={20} minW={{ base: "100%", xl: "100%" }} h={"full"} >
            <GradientBar />

            <VStack spacing={3} alignItems="center" pb={5} mt={5}>
              <form style={{ width: "100%" }}>
                <SimpleGrid columns={2} columnGap={5} rowGap={5} pl={[0, 5, 10]} pr={[0, 5, 10]} w="full">
                  <GridItem colSpan={1}>
                    <Image src="../../EserciziPalestra/990x548-410x200.gif" alt="Exercise" maxW="400px" mb={4} />
                    <Stack direction="row" mt={3} spacing={4}>
                      <Input
                          type="number"
                          name="weight"
                          placeholder="Peso (kg)"
                          value={newData.weight}
                          onChange={handleInputChange}
                      />
                      <Input
                          type="number"
                          name="sets"
                          placeholder="Serie"
                          value={newData.sets}
                          onChange={handleInputChange}
                      />
                      <Input
                          type="number"
                          name="reps"
                          placeholder="Ripetizione"
                          value={newData.reps}
                          onChange={handleInputChange}
                      />
                      <Button colorScheme="blue" onClick={handleAddData}>
                        Add
                      </Button>
                    </Stack>
                  </GridItem>
                  <GridItem colSpan={1}>
                    <Box p={4}>
                      <Text mb={2} fontWeight={"semibold"}>Cronologia Serie:</Text>
                      <VStack align="start">
                        {exerciseData.map((data, index) => (
                            <Box key={index}>
                              <Text>
                                Weight: {data.weight} kg, Sets: {data.sets}, Reps: {data.reps}
                              </Text>
                            </Box>
                        ))}
                      </VStack>
                      <Timer>

                      </Timer>
                    </Box>

                  </GridItem>
                  <GridItem colSpan={2} >
                    <Button colorScheme="fitdiary" type={"submit"} w="full">Completa Esercizio</Button>
                  </GridItem>
                </SimpleGrid>
              </form>
            </VStack>
          </Box>
        </Flex>
      </>
  )
}

export default Create;