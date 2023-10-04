import {
  Box,
  Button,
  Flex,
  Select,
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

import { Calendar, momentLocalizer } from 'react-big-calendar';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import moment from 'moment';

const urlScheda = "istanzaAlimentiConsumati/visualizzaIstanzeAlimentoConsumato";
const Index = () => {
  const fetchContext = useContext(FetchContext);
  const navigate = useNavigate();
  const [isLoading, setisLoading] = useState(false);
  const [idProtocollo, setIdProtocollo] = useState(false);
  const [toastMessage, setToastMessage] = useState(undefined);

  const localizer = momentLocalizer(moment);

  const [giorniScheda, setGiorniScheda] = useState([
    {
      title: "",
      kcalConsumate:0,
      kcalPreviste:0,
      start: moment().toDate(),
      end: moment().toDate(),
    }
  ]);


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
    const url = new URL(window.location.href);
    setIdProtocollo(url.searchParams.get("idProtocollo"));
    const currentDate = moment(); // Ottieni la data corrente
    const daysInMonth = currentDate.daysInMonth(); // Ottieni il numero di giorni nel mese corrente
    const promises = [];

    setisLoading(true);

    // Cicla attraverso tutti i giorni del mese
    for (let i = 1; i <= daysInMonth; i++) {
      const date = currentDate.clone().date(i);
      const formattedDate = date.format("YYYY-MM-DD");
      console.log(formattedDate)
      const promise = fetchContext.authAxios(`${urlScheda}?idProtocollo=${url.searchParams.get("idProtocollo")}&dataConsumazione=${formattedDate}`)
          .then((response) => response.data)
          .then((data) => {
            console.log(data)
            if(data.data.result.calorieConsumate === 0){
              return {
                title: 'Vuoto',
                start: formattedDate,
                end: formattedDate,
              };
            }
            return {
              title: "Ciao",
              kcalConsumate: data.data.result.calorieConsumate,
              kcalPreviste: data.data.result.calorieAspettate,
              start: formattedDate,
              end: formattedDate,
            };
          })
          .catch((error) => {
            console.error(`Errore nella richiesta API per ${formattedDate}: ${error.message}`);
            return null;
          });

      promises.push(promise);
    }

    // Esegui tutte le chiamate API in parallelo
    Promise.all(promises)
        .then((results) => {
          // Filtra i risultati non nulli
          const filteredResults = results.filter((result) => result !== null);
          setGiorniScheda(filteredResults);
          setisLoading(false);
        })
        .catch((error) => {
          console.error(`Errore nella gestione delle promesse: ${error.message}`);
          setisLoading(false);
        });
  }, [fetchContext]);

  // Funzione per gestire il clic su un evento
  const handleEventClick = (event) => {
    navigate("../dieta/esegui?dataConsumazione="+event.start+"&idProtocollo="+idProtocollo);
  };

  const CustomEvent = ({ event }) => (
      <div
          onClick={() => handleEventClick(event)}
      >
        <VStack>
          <Text>{event.title}</Text>
          {event.kcalConsumate > 0 && (
              <Text>
                {event.kcalConsumate}/{event.kcalPreviste} kcal
              </Text>
          )}
        </VStack>

      </div>
  );

  return (
    <>
      <Flex wrap={"wrap"} p={5}>
        <Flex alignItems={"center"} mb={5}>
          <Heading w={"full"}>{"I miei progressi alimentari"}</Heading>
        </Flex>
        <Box bg={"white"} roundedTop={20} minW={{ base: "100%", xl: "100%" }} h={"full"}>
          <GradientBar />

          <div className="App" p={5}>
            <h1>Calendario</h1>
            <div style={{ height: '500px' }} >
              <Calendar
                  localizer={localizer}
                  events={giorniScheda}
                  startAccessor="start"
                  endAccessor="end"

                  style={{ width: '100%' }}
                  components={{
                    event: CustomEvent, // Utilizza il componente personalizzato per gli eventi
                  }}
                  views={['month']}
                  defaultView="month"
              />
            </div>
          </div>
        </Box>
      </Flex>
    </>
  )
}

export default Index;
