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
import React, {useContext, useEffect, useState} from "react";
import {FetchContext} from "../../context/FetchContext";
import {GradientBar} from "../../components/GradientBar";
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

  const [infoEs,setInfoEs] = useState({
    reps:0,
    serie:0,
    recupero:0
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
  }, [toastMessage]);

  useEffect(() => {
    setisLoading(true);
    const getInfo = async () => {
      try {
        const url = new URL(window.location.href);

        // Ottenere i parametri dall'URL
        const idIstanzaEsercizio = url.searchParams.get("idIstanzaEsercizio");
        const idProtocollo = url.searchParams.get("idProtocollo");
        let urlAPI=urlInfo+"?idProtocollo="+idProtocollo+"&idIstanzaEsercizio="+idIstanzaEsercizio;
        const {data} = await fetchContext.authAxios(urlAPI);
        let infoEs=data.data.istanzeEserciziEseguiti.listaEserciziEseguiti;

        let info=data.data.istanzeEserciziEseguiti.istanzaEsercizio;
        console.log(info);
        let infoObj={
          reps:info.ripetizioni,
          serie:info.serie,
          recupero:info.recupero
        };

        setInfoEs(infoObj);

        let vettStorico=[];
        if(infoEs !== null)
        {
          for(let i=0;i<infoEs.length;i++)
          {
            let tmp=infoEs[i];
            let data=tmp.dataEsecuzione;
            let peso=tmp.pesoEsecuzione;
            let serie=tmp.numeroSerie;
            let ripetizioni=tmp.ripetizioni;
            let obj={weight: peso, sets: serie, reps: ripetizioni, data: data};
            vettStorico.push(obj);
          }
        }

        let report={};
        report.nome="Storico Esercizio";

        setReportAllenamento(report);
        setExerciseData(vettStorico);
        setisLoading(false);
      } catch (error) {
        setToastMessage({title:"Error",body:error.message,stat:"error"})
      }
    }
    getInfo();
  }, [fetchContext])




    const [exerciseData, setExerciseData] = useState([]);
    const [newData, setNewData] = useState({ weight: "", sets: "", reps: "", data: "Adesso"});

    const handleInputChange = (e) => {
      const { name, value } = e.target;
      setNewData({ ...newData, [name]: value });
    };

    const handleAddData = async () => {
      if (newData.weight && newData.sets && newData.reps && newData.data)
      {
        if(newData.weight.length>0 && newData.sets.length>0 && newData.reps.length>0)
        {
          const url = new URL(window.location.href);

          // Ottenere i parametri dall'URL
          const idIstanzaEsercizio = url.searchParams.get("idIstanzaEsercizio");
          const idProtocollo = url.searchParams.get("idProtocollo");

          let jsonReq={
            "idProtocollo":idProtocollo,
            "idIstanzaEsercizio":idIstanzaEsercizio,
            "pesoEsecuzione":newData.weight,
            "serie":newData.sets,
            "ripetizioni":newData.reps
          };

          try
          {
            const {data} = await fetchContext.authAxios.post(urlCreazione, jsonReq);

            if(data.status === "success")
            {
              setToastMessage({title:"Fatto",body:"Esercizio effettuato",stat:"success"});

              setExerciseData([...exerciseData, newData]);
              setNewData({ weight: "", sets: "", reps: "", data: "Adesso"});
            }
            else
            {
              setToastMessage({title:"Errore",body:"Errore durante la richiesta",stat:"error"});
            }
          }
          catch (e)
          {
            setToastMessage({title:"Errore",body:"Errore durante la richiesta",stat:"error"});
          }
        }
        else
        {
          setToastMessage({title:"Attenzione",body:"Inserisci tutti i dati",stat:"warning"});
        }
      }
      else
      {
        setToastMessage({title:"Attenzione",body:"Inserisci tutti i dati",stat:"warning"});
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
                      <Text fontWeight="bold">Informazioni Allenamento</Text>
                    <Flex>
                      <Text fontWeight="bold">Ripetizioni:</Text>
                      <Text>
                        {infoEs.reps}
                      </Text>
                    </Flex>
                    <Flex>
                      <Text fontWeight="bold">Serie:</Text>
                      <Text>
                        {infoEs.serie}
                      </Text>
                    </Flex>
                    <Flex>
                      <Text fontWeight="bold">Recupero:</Text>
                      <Text>
                        {infoEs.recupero} s
                      </Text>
                    </Flex>
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
                      <Input
                          type="hidden"
                          name="date"
                          placeholder="Data"
                          value={"Oggi"}
                          onChange={handleInputChange}
                      />
                    </Stack>
                    <Timer onClick={(time)=>{
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
                              <Flex>
                                <Text fontWeight="bold">[{data.data}] </Text>
                                <Text>Peso: {data.weight} kg, Serie: {data.sets}, Ripetizione: {data.reps}</Text>
                              </Flex>
                            </Box>
                        ))}
                      </VStack>
                    </Box>

                  </GridItem>
                  <GridItem colSpan={2} >
                    {/* eslint-disable-next-line no-restricted-globals */}
                    <Button colorScheme="fitdiary" type={"button"} w="full" onClick={()=>{history.back();}}>Chiudi Esercizio</Button>
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