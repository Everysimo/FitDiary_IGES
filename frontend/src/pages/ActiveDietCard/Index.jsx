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

const urlScheda = "schedaAllenamento/getSchedaAllenamentoById";
const Index = () => {
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

  const [selectedDay, setSelectedDay] = useState(-1);
  const [daysAllenamenti,setDaysAllenamenti] = useState([]);

  const localizer = momentLocalizer(moment);

  const [giorniScheda, setGiorniScheda] = useState([
    {
      title: 'Evento 1',
      kcalConsumate:0,
      kcalPreviste:10,
      start: moment().toDate(),
      end: moment().add(1, 'hour').toDate(),
    },
    {
      title: 'Evento 2',
      kcalConsumate:0,
      kcalPreviste:10,
      start: moment().add(1, 'day').toDate(),
      end: moment().add(1, 'day').toDate(),
    },
  ]);


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
        let vettGiorni=[];
        for(let i=0;i<schedaAll.listaEsercizi.length;i++)
        {
          let obj=schedaAll.listaEsercizi[i];
          let giorno=obj.giornoDellaSettimana;
          if(!vettGiorni.includes(giorno))
          {
            vettGiorni.push(giorno);
          }
        }
        setDaysAllenamenti(vettGiorni);
        console.log(schedaAll);
        setSchedaAllenamento(schedaAll);
        setSelectedDay(-1);
        setisLoading(false);
      } catch (error) {
        setToastMessage({title:"Error",body:error.message,stat:"error"})
      }
    }
    getScheda();

  }, [fetchContext])


  // Funzione per gestire il clic su un evento
  const handleEventClick = (event) => {
    alert(`Hai cliccato su: ${event.title}`);
  };

  const CustomEvent = ({ event }) => (
      <div
          onClick={() => handleEventClick(event)}
      >
        <VStack>
          <Text>{event.title}</Text>
          <Text>{event.kcalConsumate}/{event.kcalPreviste} kcal</Text>
        </VStack>

      </div>
  );

  return (
    <>
      <Flex wrap={"wrap"} p={5}>
        <Flex alignItems={"center"} mb={5}>
          <Heading w={"full"}>{schedaAllenamento.nome}</Heading>
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
                  style={{ width: '100%' }}ù
                  components={{
                    event: CustomEvent, // Utilizza il componente personalizzato per gli eventi
                  }}
                  views={['month']}
                  defaultView="month"
              />
            </div>
          </div>
          );
        </Box>
      </Flex>
    </>
  )
}

export default Index;
