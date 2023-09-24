import {
  Box,
  Button,
  Flex,
  GridItem,
  Heading,
  Image,
  Input,
  SimpleGrid,
  Stack,
  Text,
  useToast,
  VStack
} from "@chakra-ui/react";
import {useForm} from "react-hook-form"
import React, {useCallback, useContext, useEffect, useState} from "react";
import {FetchContext} from "../../context/FetchContext";
import { GiMeal } from "react-icons/gi";
import {IoIosFitness} from "react-icons/io";
import {AddIcon, CloseIcon, InfoIcon, SearchIcon} from "@chakra-ui/icons";
import Select from "react-select";
import {GradientBar} from "../../components/GradientBar";
import {useNavigate} from "react-router";
import {Timer} from "../../components/Timer";

const urlInfo = "istanzaEserciziEseguiti/visualizzaIstanzaEsercizio";
const urlCreazione="istanzaEserciziEseguiti/creaIstanzaEsercizio";

const Create = () => {
  const fetchContext = useContext(FetchContext);
  const [isLoading, setisLoading] = useState(false);
  const [toastMessage, setToastMessage] = useState(undefined);
  const [reportAllenamento, setReportAllenamento] = useState({
    nome:"NOME_ES"
  });



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
    const getInfo = async () => {
      try {
        const url = new URL(window.location.href);

        // Ottenere i parametri dall'URL
        const idIstanzaEsercizio = url.searchParams.get("idIstanzaEsercizio");
        const idProtocollo = url.searchParams.get("idProtocollo");
        const {info} = await fetchContext.authAxios(urlInfo+"?idProtocollo="+idProtocollo+"&idIstanzaEsercizio="+idIstanzaEsercizio);
        console.log("INFO:");
        console.log(info);
        let report={};
        report.nome="NOME_ES";

        let vettTest=[];
        let objTest={weight: "Ok", sets: "Lol", reps: "xD", pauseTime: 0};
        vettTest.push(objTest);
        vettTest.push(objTest);

        setReportAllenamento(report);
        setExerciseData(vettTest);
        setisLoading(false);
      } catch (error) {
        setToastMessage({title:"Error",body:error.message,stat:"error"})
      }
    }
    getInfo();
  }, [fetchContext])




    const [exerciseData, setExerciseData] = useState([]);
    const [newData, setNewData] = useState({ weight: "", sets: "", reps: "", pauseTime: 0});

    const handleInputChange = (e) => {
      const { name, value } = e.target;
      setNewData({ ...newData, [name]: value });
    };

    const handleAddData = () => {
      if (newData.weight && newData.sets && newData.reps && newData.pauseTime) {
        setExerciseData([...exerciseData, newData]);
        setNewData({ weight: "", sets: "", reps: "", pauseTime: 0});

        alert("ADD TO DB");
      }
    };

  return (
      <>
        <Flex wrap={"wrap"} p={5}>
          <Flex alignItems={"center"} mb={5}>
            <Heading w={"full"}>{reportAllenamento.nome}</Heading>
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
                    </Stack>
                    <Timer onClick={(time)=>{
                      newData.pauseTime=time;
                      handleAddData();
                    }}>

                    </Timer>
                  </GridItem>
                  <GridItem colSpan={1}>
                    <Box p={4}>
                      <Text mb={2} fontWeight={"semibold"}>Cronologia Serie:</Text>
                      <VStack align="start">
                        {exerciseData.map((data, index) => (
                            <Box key={index}>
                              <Text>
                                Peso: {data.weight} kg, Serie: {data.sets}, Ripetizione: {data.reps}, Tempo: {data.pauseTime}s
                              </Text>
                            </Box>
                        ))}
                      </VStack>
                    </Box>

                  </GridItem>
                  <GridItem colSpan={2} >
                    {/* eslint-disable-next-line no-restricted-globals */}
                    <Button colorScheme="fitdiary" type={"button"} w="full" onClick={()=>{history.back();}}>Termina Esercizio</Button>
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