package com.eburtis.ElephantBet.service.servicehelper;

import com.eburtis.ElephantBet.domain.BilanJeu;
import com.eburtis.ElephantBet.domain.BilanPremios;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] ENTETES = {"Date", "Jeu", "Nombre de jeux", "Volume de jeux", "Nombre de gains", "Volume des gains", "Balance", "Nb Gains > 250000", "Nb Gains < 250000", "Volume  Gains > 250000", "Volume  Gains < 250000"};

    /**
     * verifier que le fichier est en format excel
     *
     * @param file
     * @return
     */
    public boolean estFormatExcel(MultipartFile file) throws IOException {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    /**
     * Convertir le fichier excel en liste de bilan de jeux
     *
     * @param inputStream
     * @return
     */
    public List<BilanJeu> convertirExcelEnBilanJeu(InputStream inputStream) {
        List<BilanJeu> bilanJeuList = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            // parcourir les ligne du fichier excel
            for (int rowIndex = 1; rowIndex < getNombreCellulesNonNull((XSSFSheet) sheet, 0); rowIndex++) {
                // ligne courrente
                Row row = sheet.getRow(rowIndex);
                // sauter l'entete du fichier
                if (rowIndex == 0) {
                    continue;
                }
                LocalDate dateCreation = row.getCell(0).getLocalDateTimeCellValue().toLocalDate();
                String nomJeu = row.getCell(1).getStringCellValue();
                long nombreJeu = (long) row.getCell(2).getNumericCellValue();
                long volumeJeu = (long) row.getCell(3).getNumericCellValue();
                long nombreGains = (long) row.getCell(4).getNumericCellValue();
                long volumeGains = (long) row.getCell(5).getNumericCellValue();
                long balance = (long) row.getCell(6).getNumericCellValue();
                long nombreGainsSuperieurLimite = (long) row.getCell(7).getNumericCellValue();
                long nombreGainsInferieurLimite = (long) row.getCell(8).getNumericCellValue();
                long volumeGainsSuperieurLimite = (long) row.getCell(9).getNumericCellValue();
                long volumeGainsInferieurLimite = (long) row.getCell(10).getNumericCellValue();
                // creer le bilan de la ligne
                bilanJeuList.add(
                        new BilanJeu(dateCreation, nomJeu, nombreJeu, volumeJeu, nombreGains, volumeGains, balance,
                                nombreGainsSuperieurLimite, nombreGainsInferieurLimite, volumeGainsInferieurLimite,
                                volumeGainsSuperieurLimite));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bilanJeuList;
    }


    /**
     * Convertir le fichier excel en liste de bilan de premios
     *
     * @param inputStream
     * @return
     */
    public List<BilanPremios> convertirExcelEnBilanPremios(InputStream inputStream) {
        List<BilanPremios> bilanPremiosList = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            // parcourir les ligne du fichier excel
            for (int rowIndex = 0; rowIndex < getNombreCellulesNonNull((XSSFSheet) sheet, 0); rowIndex++) {
                // ligne courrente
                Row row = sheet.getRow(rowIndex);
                // sauter l'entete du fichier
                if (rowIndex == 0) {
                    continue;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
                String dateString = row.getCell(0).getStringCellValue().replace(".", "-");
                LocalDate dateEvenement = LocalDate.parse(dateString, formatter);
                long valeur = (long) row.getCell(1).getNumericCellValue();
                String agence = row.getCell(2).getStringCellValue();
                // creer le bilan de la ligne
                bilanPremiosList.add(
                        new BilanPremios(dateEvenement, agence, valeur));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bilanPremiosList;
    }


    /**
     * Trouver le nombre de cellules du fichier excel
     *
     * @param sheet
     * @param columnIndex
     * @return
     */
    public static int getNombreCellulesNonNull(XSSFSheet sheet, int columnIndex) {
        int numOfNonEmptyCells = 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                XSSFCell cell = row.getCell(columnIndex);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    numOfNonEmptyCells++;
                }
            }
        }
        return numOfNonEmptyCells;
    }
}
