package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.configuration.securite.SecurityService;
import com.eburtis.ElephantBet.domain.ERole;
import com.eburtis.ElephantBet.domain.Utilisateur;
import com.eburtis.ElephantBet.presentation.factory.UtilisateurVoFactory;
import com.eburtis.ElephantBet.presentation.vo.UtilisateurVo;
import com.eburtis.ElephantBet.repository.utilisateur.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UtilisateurFacadeTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurFacade utilisateurFacade;
    @Mock
    private SecurityService securityService;
    private UtilisateurVoFactory utilisateurVoFactory;

    @BeforeEach
    void setUp() {
        utilisateurFacade=new UtilisateurFacade(securityService,utilisateurRepository,utilisateurVoFactory, cacheManager);
    }

    @Test
    void should_liste_tous_les_utilisateurs_when_utilisateur_exist() {
        //given
        List<Utilisateur> utilisateurList = new ArrayList();
        utilisateurList.add(new Utilisateur());
        given(utilisateurRepository.findAll()).willReturn(utilisateurList);
        //when
        List<Utilisateur> expected = utilisateurFacade.listeUtilisateurs();
        //then
        assertEquals(expected, utilisateurList);
        verify(utilisateurRepository).findAll();
    }

    @Test
    public void should_liste_utilisateur_par_role_when_utilisateur_exist() throws Exception {
        // given
        List<Utilisateur> utilisateurList = new ArrayList();
        String role = "user";
        // given
        when(utilisateurRepository.findByRole(role)).thenReturn(Optional.of(utilisateurList));
        List<Utilisateur> expected = utilisateurFacade.listeUtilisateursParRole(role);
        // then
        assertEquals(expected, utilisateurList);
        verify(utilisateurRepository).findByRole(role);
    }

    @Test
    public void should_liste_utilisateur_par_role_when_utilisateur_not_exist() {
        //given
        List<Utilisateur> utilisateurList = new ArrayList();
        String role = "user";
        given(utilisateurRepository.findByRole(anyString())).willReturn(Optional.ofNullable(null));
        //then
        assertThatThrownBy(() -> utilisateurFacade.listeUtilisateursParRole(role))
                .hasMessageContaining("Ce utilisateur n'existe pas");
        verify(utilisateurRepository).findByRole(anyString());
    }

    @Test
    public void should_enregistrer_utilisateur_when_utilisateur_exist() throws Exception {
        //given
        Utilisateur utilisateur = new Utilisateur();
        UtilisateurVo utilisateurVo = new UtilisateurVo(1L,"aziz","abdoul","messi","messi","ADMIN");
        given(utilisateurRepository.findByUsername(utilisateurVo.getUsername())).willReturn(Optional.of(utilisateur));
        //then
        assertThatThrownBy(() -> utilisateurFacade.creerUtilisateur(utilisateurVo))
                .hasMessageContaining("Il existe déjà un compte avec cet username, Veuillez ressayer avec un nouveau nom  ou connectez-vous à votre compte");
        verify(utilisateurRepository, never()).save(any());
    }

    @Test
    void should_enregistrer_utilisateur_when_utilisateur_not_exist() throws Exception {
        //given
        Utilisateur utilisateur = new Utilisateur();
        UtilisateurVo utilisateurVo = new UtilisateurVo(1L,"aziz","abdoul","messi","messi","ADMIN");
        given(utilisateurRepository.findByUsername(utilisateurVo.getUsername())).willReturn(Optional.ofNullable(null));
        given   (utilisateurRepository.save(ArgumentMatchers.any(Utilisateur.class))).willReturn(utilisateur);
        //when
        utilisateurFacade.creerUtilisateur(utilisateurVo);
        //then
        verify(utilisateurRepository).save(ArgumentMatchers.any(Utilisateur.class));
    }


    @Test
    public void should_modifier_utilisateur_when_utilisateur_exist() throws Exception {
        //given
        Utilisateur utilisateur = new Utilisateur(1L,"aziz","abdoul","messi",true,"messi",ERole.ADMIN);
        UtilisateurVo utilisateurVo = new UtilisateurVo(1L,"aziz","abdoul","messi","messi","ADMIN");
        given(utilisateurRepository.findById(utilisateurVo.getId())).willReturn(Optional.of(utilisateur));
        given(utilisateurRepository.save(ArgumentMatchers.any(Utilisateur.class))).willReturn(utilisateur);
        //when
        utilisateurFacade.modifierUtilisateur(utilisateurVo);
        //then
        verify(utilisateurRepository).save(ArgumentMatchers.any(Utilisateur.class));


    }

    @Test
    public void should_modifier_utilisateur_when_utilisateur_not_exist() {
        //given
        Utilisateur utilisateur = new Utilisateur();
        UtilisateurVo utilisateurVo = new UtilisateurVo(1L,"aziz","abdoul","messi","messi","ADMIN");
        given(utilisateurRepository.findById(utilisateurVo.getId())).willReturn(Optional.ofNullable(null));
        //when
        //then
        assertThatThrownBy(() -> utilisateurFacade.modifierUtilisateur(utilisateurVo))
                .hasMessageContaining("Ce utilisateur n'existe pas");
        verify(utilisateurRepository).findById(any());


    }

    @Test
    void should_supprimer_utilisateur_when_utilisateur_exist() throws Exception {
        //given
        Utilisateur utilisateur = new Utilisateur(1L,"aziz","abdoul","messi",true,"messi",ERole.ADMIN);
        given(utilisateurRepository.findById(utilisateur.getId())).willReturn(Optional.of(utilisateur));
        // when
        utilisateurFacade.supprimerUtilisateur(utilisateur.getId());
        //then
        verify(utilisateurRepository).deleteById(utilisateur.getId());

    }

    @Test
    void should_supprimer_utilisateur_when_utilisateur_not_exist() throws Exception {
        // given
        Utilisateur utilisateur = new Utilisateur(1L,"aziz","abdoul","messi",true,"messi",ERole.ADMIN);
        given(utilisateurRepository.findById(utilisateur.getId())).willReturn(Optional.ofNullable(null));
        // when
        assertThatThrownBy(() -> utilisateurFacade.supprimerUtilisateur(utilisateur.getId()))
                .hasMessageContaining("Ce utilisateur n'existe pas");
        verify(utilisateurRepository).findById(utilisateur.getId());

    }
}