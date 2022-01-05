package it.fitdiary.backend.gestioneprotocollo.adapter;

import it.fitdiary.backend.entity.Alimento;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SchedaAlimentareAdapterImpl implements SchedaAlimentareAdapter {

    /**
     * Numero della colonna del file CSV corrispondente a nome.
     */
    public static final int COLUMN_NOME = 0;
    /**
     * Numero della colonna del file CSV corrispondente a pasto.
     */
    public static final int COLUMN_PASTO = 1;
    /**
     * Numero della colonna del file CSV corrispondente a giorno.
     */
    public static final int COLUMN_GIORNO = 2;
    /**
     * Numero della colonna del file CSV corrispondente a kcal.
     */
    public static final int COLUMN_KCAL = 3;
    /**
     * Numero della colonna del file CSV corrispondente a grammi.
     */
    public static final int COLUMN_GRAMMI = 4;

    /**
     * @param file file della scheda alimentare
     * @return
     * @throws IOException
     */
    @Override
    public List<Alimento> parse(final File file)
            throws IOException, NumberFormatException {
        var alimenti = new ArrayList<Alimento>();
        Iterable<CSVRecord> records =
                CSVFormat.EXCEL.withDelimiter(';').parse(new FileReader(file));
        int riga = 1;

        for (CSVRecord record : records) {
            if (riga == 1) {
                riga++;
                continue;
            }
            Alimento alimento = new Alimento();
            alimento.setNome(record.get(COLUMN_NOME));
            alimento.setPasto(record.get(COLUMN_PASTO));
            alimento.setGiorno(record.get(COLUMN_GIORNO));
            alimento.setKcal(Integer.valueOf(record.get(COLUMN_KCAL)));
            alimento.setGrammi(Float.valueOf(record.get(COLUMN_GRAMMI)));
            alimenti.add(alimento);
        }
        return alimenti;
    }
}
