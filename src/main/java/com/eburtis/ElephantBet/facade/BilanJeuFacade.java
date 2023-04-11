package com.eburtis.ElephantBet.facade;
import com.eburtis.ElephantBet.domain.BilanJeu;
import com.eburtis.ElephantBet.presentation.vo.BilanJeuVo;
import com.eburtis.ElephantBet.repository.bilanJeu.BilanJeuRepository;
import com.eburtis.ElephantBet.service.servicehelper.ExcelHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Service
public class BilanJeuFacade {

    private final BilanJeuRepository bilanJeuRepository;

    private final ExcelHelper excelHelper;

    public BilanJeuFacade(BilanJeuRepository bilanJeuRepository, ExcelHelper excelHelper) {
        this.bilanJeuRepository = bilanJeuRepository;
        this.excelHelper = excelHelper;
    }


    /**
     * Enregistrer les ligines du fichier excel
     * @param file
     */
    public void telechargementBilanJeu(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            List<BilanJeu> bilanJeuList = excelHelper.convertirExcelEnBilanJeu(inputStream);
            //ordonner la liste de bilans jeux
            List<BilanJeu> listBilanOrdonne= bilanJeuList.stream().sorted(Comparator.comparing(BilanJeu::getDateCreation)).collect(toList());
            // lecture de la date la plus ancienne
            LocalDate dateDebut = listBilanOrdonne.stream().findFirst().get().getDateCreation();
            // lecture de la date la plus recente
            long count = bilanJeuList.stream().count();
            LocalDate dateFin = listBilanOrdonne.stream().skip(count - 1).findFirst().get().getDateCreation();
            // enregistrement des bilans de jeux
            enregistrementBilanJeux(dateDebut,dateFin,bilanJeuList);
        } catch (IOException e) {
            throw new RuntimeException("Echec de la convertion du fichier excel: " + e.getMessage());
        }
    }

    @Transactional
    public void enregistrementBilanJeux(LocalDate dateDebut, LocalDate datefin, List<BilanJeu> bilanJeuList) {
        Set<BilanJeu> bilans = new HashSet<>();
        // Regroupe des bilans par (jour,nom du jeu).
        Map<Pair<LocalDate, String>, BilanJeu> bilanExistantParJourneeNomJeu = bilanJeuRepository.listeBilanJeuPourPeriode(dateDebut,datefin).stream()
                .collect(toMap(entry -> Pair.of(entry.getDateCreation(),entry.getNomJeu()), identity()));
        for(BilanJeu bilanJeu : bilanJeuList) {
          BilanJeu  bilan = bilanExistantParJourneeNomJeu.get(Pair.of(bilanJeu.getDateCreation(), bilanJeu.getNomJeu()));
          if(bilan != null) {
              bilan.mettreAjour(bilanJeu);
          }
          else {
              bilans.add(bilanJeu);
          }
        }
        bilanJeuRepository.saveAll(bilans);
    }

    public List<BilanJeu> listeDesBilansJeux() {
        return bilanJeuRepository.findAll();
    }

    /**
     * Lister les bilans de jeux pour une période passée en paramètre
     * @param debut  la date début de la période
     * @param fin    la date de fin de la période
     * @return liste de bilan jeu pour une période.
     */
    @Transactional(readOnly = true)
    public Set<BilanJeuVo> listeBilanJeuPourPeriode(String debut, String fin) {
        Set<BilanJeuVo> bilanJeuVoSet = new HashSet<>();
        LocalDate dateDebut = LocalDate.parse(debut);
        LocalDate dateFin = LocalDate.parse(fin);
        List<Object[]> bilanJeuList1 = this.bilanJeuRepository.listeBilanJeuPourPeriodeCalculer(dateDebut, dateFin);
        bilanJeuList1.forEach(bilan -> {
            bilanJeuVoSet.add(new BilanJeuVo((String) bilan[0], ((BigDecimal) bilan[1]).longValue(),
                    ((BigDecimal) bilan[2]).longValue(), ((BigDecimal) bilan[3]).longValue(), ((BigDecimal) bilan[4]).longValue(),
                    ((BigDecimal) bilan[5]).longValue(), ((BigDecimal) bilan[6]).longValue(), ((BigDecimal) bilan[7]).longValue(),
                    ((BigDecimal) bilan[9]).longValue(), ((BigDecimal) bilan[8]).longValue()));
        });
        return bilanJeuVoSet;
    }

    /**
     * Lister bilan jeux pour un mois passé en paramètre.
     * @param date   le mois concerné
     * @return Une liste de bilan jeux pour un mois.
     */
    @Transactional(readOnly = true)
    @Cacheable("jeux-mensuel")
    public Set<BilanJeuVo> listerBilanJeuPourMois(String date) {
        Set<BilanJeuVo> bilanJeuVoSet = new HashSet<>();
        Integer annee = Integer.valueOf(date.split("-")[0]);
        Integer mois = Integer.valueOf(date.split("-")[1]);
        // liste des bilans de jeux sur cette période
        List<Object[]> bilanJeuList1 = this.bilanJeuRepository.listeBilanJeuPourMoisCalculer(mois, annee);
        bilanJeuList1.forEach(bilan -> {
            bilanJeuVoSet.add(new BilanJeuVo((String) bilan[0], ((BigDecimal) bilan[1]).longValue(),
                    ((BigDecimal) bilan[2]).longValue(), ((BigDecimal) bilan[3]).longValue(),
                    ((BigDecimal) bilan[4]).longValue(), ((BigDecimal) bilan[5]).longValue(),
                    ((BigDecimal) bilan[6]).longValue(), ((BigDecimal) bilan[7]).longValue(),
                    ((BigDecimal) bilan[9]).longValue(), ((BigDecimal) bilan[8]).longValue()));
        });
        return bilanJeuVoSet;
    }

    /**
     * Lister les bilans de jeux pour une année.
     * @param date   l'année concernée
     * @return Une liste de bilan jeux pour une année.
     */
    @Transactional(readOnly = true)
    @Cacheable("jeux-annuel")
    public Set<BilanJeuVo> listerBilanJeuxPourAnnee(String date) {
        Set<BilanJeuVo> bilanJeuVoSet = new HashSet<>();
        Integer annee = Integer.valueOf(date);
        // liste des bilans de jeux sur cette période
        List<Object[]> bilanJeuList1 = this.bilanJeuRepository.listeBilanJeuPourAnneeCalculer(annee);
        bilanJeuList1.forEach(bilan -> {
            bilanJeuVoSet.add(new BilanJeuVo((String) bilan[0], ((BigDecimal) bilan[1]).longValue(),
                    ((BigDecimal) bilan[2]).longValue(), ((BigDecimal) bilan[3]).longValue(),
                    ((BigDecimal) bilan[4]).longValue(), ((BigDecimal) bilan[5]).longValue(),
                    ((BigDecimal) bilan[6]).longValue(), ((BigDecimal) bilan[7]).longValue(),
                    ((BigDecimal) bilan[9]).longValue(), ((BigDecimal) bilan[8]).longValue()));
        });
        return bilanJeuVoSet;
    }

}
