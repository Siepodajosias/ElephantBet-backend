package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.configuration.securite.SecurityService;
import com.eburtis.ElephantBet.domain.ERole;
import com.eburtis.ElephantBet.domain.Utilisateur;
import com.eburtis.ElephantBet.presentation.factory.UtilisateurVoFactory;
import com.eburtis.ElephantBet.presentation.vo.UtilisateurVo;
import com.eburtis.ElephantBet.repository.utilisateur.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurFacade {
    private final SecurityService securityService;
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurVoFactory utilisateurVoFactory;

    @Autowired
    private final CacheManager cacheManager;

    /**
     * constructeur de la facade utilisateur
     *
     * @param securityService
     * @param utilisateurRepository
     * @param utilisateurVoFactory
     * @param cacheManager
     */
    public UtilisateurFacade(SecurityService securityService, UtilisateurRepository utilisateurRepository, UtilisateurVoFactory utilisateurVoFactory, CacheManager cacheManager) {
        this.securityService = securityService;
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurVoFactory = utilisateurVoFactory;
        this.cacheManager = cacheManager;
    }

    /**
     * Cette fonction permet de lister tous les utilisateurs
     * @return tous les utilisateurs
     */
    @Transactional
    public List<Utilisateur> listeUtilisateurs(){
        return utilisateurRepository.findAll();
    }

    /**
     * cette fonction permet de lister les utilisateurs par le role
     * @param role le role
     * @return liste des utilisateurs
     * @throws Exception
     */
    @Transactional
    public List<Utilisateur> listeUtilisateursParRole(String role ) throws Exception {
        Optional<List<Utilisateur>> utilisateurOptionals = utilisateurRepository.findByRole(role);
        if (utilisateurOptionals.isPresent()) {
            return  utilisateurOptionals.get();
        }
        throw new Exception("Ce utilisateur n'existe pas");
    }

    /**
     * cette fonction permet d'enregistrer un utilisateur
     * @param utilisateurVo
     * @throws Exception
     */
    @Transactional
    public void creerUtilisateur(UtilisateurVo utilisateurVo) throws Exception {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findByUsername(utilisateurVo.getUsername());
        if(optionalUtilisateur.isPresent()){
            throw new Exception("Il existe déjà un compte avec cet username, Veuillez ressayer avec un nouveau nom  ou connectez-vous à votre compte");
        }
        else {
            ERole role = ERole.valueOf(utilisateurVo.getRole());
            Utilisateur utilisateur = new Utilisateur(this.securityService.crypterPassword(utilisateurVo.getPassword()),role);
            utilisateur.mettreAJour(
                    utilisateurVo.getNom(),
                    utilisateurVo.getPrenoms(),
                    utilisateurVo.getUsername()
            );
            utilisateurRepository.save(utilisateur);
            cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
        }
    }

    /**
     * cette fonction permet de modifier un utilisateur
     * @param utilisateurVo
     * @return
     * @throws Exception
     */
    @Transactional
    public Utilisateur modifierUtilisateur(UtilisateurVo utilisateurVo) throws Exception {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(utilisateurVo.getId());
        if (utilisateurOptional.isPresent()) {
            Utilisateur utilisateur = utilisateurOptional.get();
            if(utilisateurVo.getPassword().isEmpty()){
                utilisateur.miseAjourSansMotDePasse(
                        utilisateurVo.getNom(),
                        utilisateurVo.getPrenoms(),
                        utilisateurVo.getUsername(),
                        utilisateurVo.getRole()
                );
                cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
            }
            else{
                utilisateur.miseAjourGlobal(
                        utilisateurVo.getNom(),
                        utilisateurVo.getPrenoms(),
                        utilisateurVo.getUsername(),
                        this.securityService.crypterPassword(utilisateurVo.getPassword()),
                        utilisateurVo.getRole()
                );
                cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
            }
            return this.utilisateurRepository.save(utilisateur);
        }
        throw new Exception("Ce utilisateur n'existe pas");
    }

    /**
     * Cette fonction permet de supprimer un utilisateur
     * @param id
     * @throws Exception
     */
    @Transactional
    public void supprimerUtilisateur(Long id) throws Exception {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(id);
        if (utilisateurOptional.isPresent()) {
            this.utilisateurRepository.deleteById(id);
            cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
        }
        else {
            throw new Exception("Ce utilisateur n'existe pas");
        }

    }

    /**
     * Cette fonction permet de compter un utilisateur
     * @param id
     * @throws Exception
     */
    @Transactional
    public Long counterUtilisateur(Long id) throws Exception {
        return utilisateurRepository.countUtilisateur(id);
    }
}
