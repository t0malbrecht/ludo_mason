package services;

import game.Game2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

    public class CSVWriter {
        private static final String SAMPLE_CSV_FILE = "./results.csv";

        public static void print() throws IOException {
            try (
                    BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));

                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                            .withHeader("ID", "KombinationsID", "Position", "Strategie", "Gewinne", "Zuge", "Hat_Gekickt", "Wurde_Gekickt", "Token_in_Winspot_gesetzt"));
            ) {
                for (String[] rowItem : Game2.rowItems) {
                    csvPrinter.printRecord(rowItem[0], rowItem[1], rowItem[2], rowItem[3], rowItem[4], rowItem[5], rowItem[6], rowItem[7], rowItem[8]);
                }

                csvPrinter.flush();
            }
        }
    }
