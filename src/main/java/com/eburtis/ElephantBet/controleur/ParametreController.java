package com.eburtis.ElephantBet.controleur;

import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.facade.ParametrageFacade;
import com.eburtis.ElephantBet.presentation.vo.ParametrageVo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/csv/parametrage")
public class ParametreController {
    private final ParametrageFacade parametrageFacade;

    public ParametreController( ParametrageFacade parametrageFacade) {
        this.parametrageFacade = parametrageFacade;
    }

    @GetMapping("/code/{code}")
    public List<Parametrage> afficherParametrageParCode(@PathVariable String code) throws Exception {
        return parametrageFacade.afficherParametreParCode(code);
    }

    @PostMapping("/enregistrer")
    public void  enregistrerParametre(@RequestBody ParametrageVo parametrageVo) throws Exception{
        parametrageFacade.enregistrerParametre(parametrageVo);
    }

    @PutMapping("/modifier")
    public Parametrage modifierParametrage(@RequestBody @Valid ParametrageVo parametrageVo) throws Exception {
        return parametrageFacade.modifierParametrage(parametrageVo);
    }

    @DeleteMapping("/supprimer/{id}")
    public void supprimerParametrage(@PathVariable Long id) throws Exception {
        parametrageFacade.supprimerParametrage(id);
    }

}
