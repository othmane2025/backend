package com.example.batimentDU.controller;

import com.example.batimentDU.dto.StatutActuelDTO;
import com.example.batimentDU.enumeraion.EntierementDemoli;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.PartiellementDemoli;
import com.example.batimentDU.model.Batiment;
import com.example.batimentDU.model.StatutActuel;
import com.example.batimentDU.repository.BatimentRepository;
import com.example.batimentDU.repository.StatutActuelRepository;
import com.example.batimentDU.service.StatutActuelService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/statut")
public class StatutActuelController {

    @Autowired
    private StatutActuelService statutActuelService;

    @Autowired
    BatimentRepository batimentRepository;
    @Autowired
    StatutActuelRepository statutActuelRepository;


    // Afficher tous les statuts actuels
    @GetMapping("/getSta")
    @ResponseBody
    public List<StatutActuel> getStatutActuel() {
        return statutActuelService.findAll();
    }

    //
    @GetMapping("/annexe/{nomAnnexe}")
    public ResponseEntity<List<StatutActuelDTO>> getStatutActuelsByNomAnnexe(@PathVariable NomAnnexe nomAnnexe) {
        List<StatutActuelDTO> statutActuels = statutActuelService.getStatutActuelsByNomAnnexe(nomAnnexe);
        if (statutActuels.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(statutActuels);
    }

    /**
     * Endpoint pour créer un nouveau StatutActuel associé à un bâtiment
     */
    @PostMapping("/createSta")
    public ResponseEntity<?> createStatutActuel(
            @RequestParam("idBat") Long idBat,
            @RequestParam(value = "vide", required = false) String vide,
            @RequestParam(value = "vacant", required = false) String vacant,
            @RequestParam(value = "entierementDemoli", required = false) String entierementDemoli,
            @RequestParam(value = "partiellementDemoli", required = false) String partiellementDemoli,
            @RequestParam(value = "renforce", required = false) String renforce,
            @RequestParam(value = "restaurer", required = false) String restaurer) {
        try {
            StatutActuel statutActuel = new StatutActuel();
            Batiment batiment = batimentRepository.findById(idBat)
                    .orElseThrow(() -> new EntityNotFoundException("Bâtiment introuvable"));

            statutActuel.setBatiment(batiment);
            if (vide != null) statutActuel.setVide(vide);
            if (vacant != null) statutActuel.setVacant(vacant);
            if (entierementDemoli != null) statutActuel.setEntierementDemoli(entierementDemoli);
            if (partiellementDemoli != null) statutActuel.setPartiellementDemoli(partiellementDemoli);
            if (renforce != null) statutActuel.setRenforce(renforce);
            if (restaurer != null) statutActuel.setRestaurer(restaurer);

            StatutActuel savedStatut = statutActuelRepository.save(statutActuel);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStatut);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la création du statut.");
        }
    }





    // Rechercher un statut actuel par ID
    @GetMapping("/found/{idStatut}")
    public ResponseEntity<StatutActuel> findById(@PathVariable Long idStatut) {
        StatutActuel statutActuel = statutActuelService.findById(idStatut);
        return ResponseEntity.ok(statutActuel);
    }

    // Supprimer un statut actuel par ID
    @DeleteMapping("/delete/{idStatut}")
    public ResponseEntity<Void> deleteStatutActuel(@PathVariable Long idStatut) {
        statutActuelService.deleteStatutActuel(idStatut);
        return ResponseEntity.noContent().build();
    }

    // Mettre à jour un statut actuel existant
    @PutMapping("/update/{idStatut}")
    public ResponseEntity<StatutActuel> updateStatutActuel(@PathVariable Long idStatut, @RequestBody StatutActuel updatedStatut) {
        StatutActuel statutActuel = statutActuelService.updateStatutActuel(idStatut, updatedStatut);
        return ResponseEntity.ok(statutActuel);
    }
}
