package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.domain.Parametrages;
import com.eburtis.ElephantBet.presentation.vo.ParametrageVo;
import com.eburtis.ElephantBet.repository.parametrage.ParametrageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ParametrageFacade {
    private final ParametrageRepository parametrageRepository;
    @Autowired
    private final CacheManager cacheManager;

    /**
     * Constructeur de la Facade de parametrage
     * @param parametrageRepository
     * @param cacheManager
     */
    public ParametrageFacade(ParametrageRepository parametrageRepository, CacheManager cacheManager) {
        this.parametrageRepository = parametrageRepository;
        this.cacheManager = cacheManager;
    }

    /**
     * Cette fonction permet lister un parametrage par son code
     * @param code le code du parametrage
     * @return un paramatrage
     * @throws Exception
     */
    @Transactional
    public List<Parametrage> afficherParametreParCode(String code) throws Exception {
        Parametrages parametrages = Parametrages.valueOf(code);
        Optional<List<Parametrage>> optionalParametrages = this.parametrageRepository.findByCode(parametrages);
        if(optionalParametrages.isPresent()){
            return optionalParametrages.get();
        }
        else if(optionalParametrages.isEmpty()){
            return Collections.emptyList();
        }
        else{
            throw new Exception("Pas de parametrage pour ce code");
        }
    }

    /**
     * cette fonction permet d'enregistrer un parametrage
     * @param parametrageVo les informations du parametrage
     * @throws Exception
     */
    @Transactional
    public void enregistrerParametre(ParametrageVo parametrageVo) throws Exception {
        if(parametrageVo.getCode() != null){
            Parametrage parametrage =  new Parametrage(Parametrages.valueOf(parametrageVo.getCode()),
                    parametrageVo.getLimiteDebut(),
                    parametrageVo.getLimiteFin(),
                    parametrageVo.getGroupe(),
                    LocalDate.parse(parametrageVo.getDateDebutLimite()),
                    LocalDate.parse(parametrageVo.getDateFinLimite()));
            this.parametrageRepository.save(parametrage);
            cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
        }
        else{
            throw new Exception("Le code est obligatoire");
        }
    }

    /**
     * cette fonction permet de mettre à jour un parametrage
     * @param parametrageVo les informations du parametrage
     * @return Parametrage
     * @throws Exception
     */
    @Transactional
    public Parametrage modifierParametrage(ParametrageVo parametrageVo) throws Exception {
        Optional<Parametrage> parametrageOptional = parametrageRepository.findById(parametrageVo.getId());
        if (parametrageOptional.isPresent()) {
            Parametrage parametrage = parametrageOptional.get();
            parametrage.mettreAjour(
                    parametrageVo.getLimiteDebut(),
                    parametrageVo.getLimiteFin(),
                    parametrageVo.getGroupe(),
                    parametrageVo.getDateDebutLimite(),
                    parametrageVo.getDateFinLimite()
            );
            cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
            return this.parametrageRepository.save(parametrage);
        }
        else{
            throw new Exception("Ce parametrage n'existe pas");
        }
    }

    /**
     * Cette fonction permet de supprimer un parametrage
     * @param id Id du parametrage
     * @throws Exception
     */
    @Transactional
    public void supprimerParametrage(Long id) throws Exception {
        Optional<Parametrage> utilisateurOptional = parametrageRepository.findById(id);
        if (utilisateurOptional.isPresent()) {
            Parametrage utilisateur = utilisateurOptional.get();
            this.parametrageRepository.deleteById(id);
            cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
        }
        else {
            throw new Exception("Ce parametrage n'existe pas");
        }
    }

}
