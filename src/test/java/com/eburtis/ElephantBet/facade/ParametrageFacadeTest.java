package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.domain.Parametrages;
import com.eburtis.ElephantBet.presentation.vo.ParametrageVo;
import com.eburtis.ElephantBet.repository.parametrage.ParametrageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParametrageFacadeTest {

    @Mock
    private ParametrageRepository parametrageRepository;

    @InjectMocks
    private ParametrageFacade parametrageFacade;
    @Mock
    private  CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        parametrageFacade=new ParametrageFacade(parametrageRepository,cacheManager);
    }


    @Test
    public void should_afficher_parametre_par_code_exist() throws Exception {
        // given
        List<Parametrage> parametrageList = new ArrayList();
        // when
        when(parametrageRepository.findByCode(Parametrages.LIMITE_TICKETS)).thenReturn(Optional.of(parametrageList));
        List<Parametrage> expected = parametrageFacade.afficherParametreParCode("LIMITE_TICKETS");
        // then
        assertEquals(expected, parametrageList);
        verify(parametrageRepository).findByCode(Parametrages.LIMITE_TICKETS);

    }

    @Test
    public void should_afficher_parametre_par_code_not_exist() {
        // given
        List<Parametrage> parametrageList = new ArrayList();
        // when
        when(parametrageRepository.findByCode(Parametrages.LIMITE_TICKETS)).thenReturn(Optional.ofNullable(null));
        // then
        assertThatThrownBy(() -> parametrageFacade.afficherParametreParCode("LIMITE_TICKETS"))
                .hasMessageContaining("Pas de parametrage pour ce code");
        verify(parametrageRepository).findByCode(Parametrages.LIMITE_TICKETS);
    }

    @Test
    public void should_enregistrer_parametrage_when_code_exist() throws Exception {
        //given
        ParametrageVo parametrageVo = new ParametrageVo("GROUPES_ET_DATES",0,0,"23444","2022-10-18","2022-10-20");
        Parametrage parametrage = new Parametrage();
        given(parametrageRepository.save(ArgumentMatchers.any(Parametrage.class))).willReturn(parametrage);
        //when
        parametrageFacade.enregistrerParametre(parametrageVo);
        //then
        verify(parametrageRepository).save(ArgumentMatchers.any(Parametrage.class));

    }

    @Test
    public void should_enregistrer_parametrage_when_code_not_exist() {
        //given
        ParametrageVo parametrageVo = new ParametrageVo(1L);
        //then
        assertThatThrownBy(() -> parametrageFacade.enregistrerParametre(parametrageVo))
                .hasMessageContaining("Le code est obligatoire");
        verify(parametrageRepository, never()).save(any());
    }

    @Test
    public void should_modifier_parametrage_when_parametrage_exist() throws Exception {
        //given
        ParametrageVo parametrageVo = new ParametrageVo("GROUPES_ET_DATES",0,0,"23444","2022-10-18","2022-10-20");
        Parametrage parametrage = new Parametrage();
        given(parametrageRepository.findById(parametrageVo.getId())).willReturn(Optional.of(parametrage));
        given(parametrageRepository.save(ArgumentMatchers.any(Parametrage.class))).willReturn(parametrage);
        //when
        parametrageFacade.modifierParametrage(parametrageVo);
        //then
        verify(parametrageRepository).save(ArgumentMatchers.any(Parametrage.class));
    }

    @Test
    public void should_modifier_parametrage_when_parametrage_not_exist() {
        //given
        ParametrageVo parametrageVo = new ParametrageVo("GROUPES_ET_DATES",0,0,"23444","2022-10-18","2022-10-20");
        Parametrage parametrage = new Parametrage();
        given(parametrageRepository.findById(parametrageVo.getId())).willReturn(Optional.ofNullable(null));
        //when
        //then
        assertThatThrownBy(() -> parametrageFacade.modifierParametrage(parametrageVo))
                .hasMessageContaining("Ce parametrage n'existe pas");
        verify(parametrageRepository).findById(any());
    }

    @Test
    public void should_supprimer_utilisateur_when_utilisateur_not_exist() {
        // given
        Long id = 1L ;
        given(parametrageRepository.findById(id)).willReturn(Optional.ofNullable(null));
        // when
        assertThatThrownBy(() -> parametrageFacade.supprimerParametrage(id))
                .hasMessageContaining("Ce parametrage n'existe pas");
        verify(parametrageRepository).findById(id);
    }

    @Test
    public void should_supprimer_utilisateur_when_utilisateur_exist() throws Exception {
        //given
        Long id = 1L ;
        Parametrage parametrage = new Parametrage();
        given(parametrageRepository.findById(id)).willReturn(Optional.of(parametrage));
        // when
        parametrageFacade.supprimerParametrage(id);
        //then
        verify(parametrageRepository).deleteById(id);

    }
}