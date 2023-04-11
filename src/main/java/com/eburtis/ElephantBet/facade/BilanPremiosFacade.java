package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.domain.BilanPremios;
import com.eburtis.ElephantBet.presentation.vo.BilanPremiosVo;
import com.eburtis.ElephantBet.repository.bilanPremios.BilanPremiosRepository;
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
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class BilanPremiosFacade {

    private final BilanPremiosRepository bilanPremiosRepository;

    private final ExcelHelper excelHelper;

    public BilanPremiosFacade(BilanPremiosRepository bilanPremiosRepository, ExcelHelper excelHelper) {
        this.bilanPremiosRepository = bilanPremiosRepository;
        this.excelHelper = excelHelper;
    }


    /**
     * Enregistrer les ligines du fichier excel
     * @param file
     */
    public void telechargementBilanPremios(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            List<BilanPremios> bilanPremiosList = excelHelper.convertirExcelEnBilanPremios(inputStream);
            //ordonner la liste de bilans jeux
            List<BilanPremios> listBilanOrdonne= bilanPremiosList.stream().sorted(Comparator.comparing(BilanPremios::getDateEvenement)).collect(toList());
            // lecture de la date la plus ancienne
            LocalDate dateDebut = listBilanOrdonne.stream().findFirst().get().getDateEvenement();
            // lecture de la date la plus recente
            long count = bilanPremiosList.stream().count();
            LocalDate dateFin = listBilanOrdonne.stream().skip(count - 1).findFirst().get().getDateEvenement();
            // enregistrement des bilans de jeux
            enregistrementBilanPremios(dateDebut,dateFin,bilanPremiosList);
        } catch (IOException e) {
            throw new RuntimeException("Echec de la convertion du fichier excel: " + e.getMessage());
        }
    }

    @Transactional
    public void enregistrementBilanPremios(LocalDate dateDebut, LocalDate datefin, List<BilanPremios> bilanPremiosList) {
        Set<BilanPremios> bilans = new HashSet<>();
        // Regroupe des bilans par (jour,agence).
        Map<Pair<LocalDate, Pair<String, Long>>, BilanPremios> bilanExistantParJourneeNomJeu = bilanPremiosRepository.listeBilanPremiosPourPeriode(dateDebut,datefin).stream()
                .collect(toMap(entry -> Pair.of(entry.getDateEvenement(), Pair.of(entry.getAgence(), entry.getValeur())), identity()));
        for(BilanPremios bilanPremios : bilanPremiosList) {
            BilanPremios  bilan = bilanExistantParJourneeNomJeu.get(Pair.of(bilanPremios.getDateEvenement(),Pair.of(bilanPremios.getAgence(),bilanPremios.getValeur())));
            if(bilan != null) {
                bilan.mettreAjour(bilanPremios);
            }
            else {
                bilans.add(bilanPremios);
            }
        }
        bilanPremiosRepository.saveAll(bilans);
    }

    public List<BilanPremios> listeDesBilansPremios() {
        return bilanPremiosRepository.findAll();
    }

    /**
     * Lister les bilans de premios pour une période passée en paramètre
     * @param debut  la date début de la période
     * @param fin    la date de fin de la période
     * @return liste de bilan premios pour une période.
     */
    @Transactional(readOnly = true)
    public Set<BilanPremiosVo> listeBilanPremiosPourPeriode(String debut, String fin) {
        Set<BilanPremiosVo> bilanPremiosVoSet = new HashSet<>();
        LocalDate dateDebut = LocalDate.parse(debut);
        LocalDate dateFin = LocalDate.parse(fin);
        List<Object[]> bilanJeuList1 = this.bilanPremiosRepository.listeBilanJeuPourPeriodeCalculer(dateDebut, dateFin);
        bilanJeuList1.forEach(bilan -> {
            bilanPremiosVoSet.add(new BilanPremiosVo((String) bilan[0], ((BigDecimal) bilan[1]).longValue(), ((BigInteger) bilan[2]).longValue()));
        });
        return bilanPremiosVoSet;
    }

    /**
     * Lister bilan jeux pour un mois passé en paramètre.
     * @param date   le mois concerné
     * @return Une liste de bilan premios pour un mois.
     */
    @Transactional(readOnly = true)
    @Cacheable("premios-mensuel")
    public Set<BilanPremiosVo> listerBilanPremiosPourMois(String date) {
        Set<BilanPremiosVo> bilanPremiosVoSet = new HashSet<>();
        Integer annee = Integer.valueOf(date.split("-")[0]);
        Integer mois = Integer.valueOf(date.split("-")[1]);
        // liste des bilans de jeux sur cette période
        List<Object[]> bilanJeuList1 = this.bilanPremiosRepository.listeBilanPremiosPourMoisCalculer(mois, annee);
        bilanJeuList1.forEach(bilan -> {
            bilanPremiosVoSet.add(new BilanPremiosVo((String) bilan[0], ((BigDecimal) bilan[1]).longValue(), ((BigInteger) bilan[2]).longValue()));
        });
        return bilanPremiosVoSet;
    }

    /**
     * Lister les bilans de premios pour une année.
     * @param date   l'année concernée
     * @return Une liste de bilan premios pour une année.
     */
    @Transactional(readOnly = true)
    @Cacheable("premios-annuel")
    public Set<BilanPremiosVo> listerBilanPremiosPourAnnee(String date) {
        Set<BilanPremiosVo> bilanPremiosVoSet = new HashSet<>();
        Integer annee = Integer.valueOf(date);
        // liste des bilans de jeux sur cette période
        List<Object[]> bilanJeuList1 = this.bilanPremiosRepository.listeBilanPremiosPourAnneeCalculer(annee);
        bilanJeuList1.forEach(bilan -> {
            bilanPremiosVoSet.add(new BilanPremiosVo((String) bilan[0], ((BigDecimal) bilan[1]).longValue(), ((BigInteger) bilan[2]).longValue()));
        });
        return bilanPremiosVoSet;
    }

}
