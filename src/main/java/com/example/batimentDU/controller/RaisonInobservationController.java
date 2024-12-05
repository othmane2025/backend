package com.example.batimentDU.controller;

import com.example.batimentDU.dto.RaisonInobservationDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.model.RaisonInobservation;
import com.example.batimentDU.service.RaisonInobservationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/raison")
public class RaisonInobservationController {

    @Autowired
    private RaisonInobservationService raisonInobservationService;

    // Afficher toutes les raisons d'inobservation
    @GetMapping("/getRai")
    @ResponseBody
    public List<RaisonInobservation> getRaisonInobservation() {
        return raisonInobservationService.findAll();
    }

    @GetMapping("/annexe/{nomAnnexe}")
    public ResponseEntity<List<RaisonInobservationDTO>> getRaisonsByNomAnnexe(@PathVariable("nomAnnexe") NomAnnexe nomAnnexe) {
        System.out.println("NomAnnexe reçu: " + nomAnnexe);
        List<RaisonInobservationDTO> raisons = raisonInobservationService.getRaisonsByNomAnnexe(nomAnnexe);
        if (raisons.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(raisons);
    }


    // Ajouter une nouvelle raison d'inobservation
    @PostMapping("/createRai")
    @ResponseBody
    public ResponseEntity<?> addRaisonInobservation(
            @RequestParam("idBat") Long idBat,
            @RequestParam(value = "nomRai", required = false) String nomRai) {
        try {
            // Créer un objet RaisonInobservation
            RaisonInobservation raisonInobservation = new RaisonInobservation();
            if (nomRai != null) {
                raisonInobservation.setNomRai(nomRai);
            }
            RaisonInobservation savedRaison = raisonInobservationService.addRaisonInobservation(idBat, raisonInobservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRaison);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout de la raison.");
        }
    }


    // Rechercher une raison d'inobservation par ID
    @GetMapping("/found/{idRai}")
    public ResponseEntity<RaisonInobservation> findById(@PathVariable Long idRai) {
        try {
            RaisonInobservation raisonInobservation = raisonInobservationService.findById(idRai);
            return ResponseEntity.ok(raisonInobservation);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Supprimer une raison d'inobservation par ID
    @DeleteMapping("/delete/{idRai}")
    public ResponseEntity<Void> supprimerRaisonObservation(@PathVariable Long idRai) {
        try {
            raisonInobservationService.supprimerRaisonObservation(idRai);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Mettre à jour une raison d'inobservation existante
    @PutMapping("/update/{idRaison}")
    public ResponseEntity<RaisonInobservation> updateRaisonInobservation(
            @PathVariable Long idRaison,
            @RequestBody RaisonInobservation nouvelleRaison) {
        try {
            // Appeler le service pour modifier la raison
            RaisonInobservation raisonModifiee = raisonInobservationService.modifierRaisonInobservation(idRaison, nouvelleRaison);
            return ResponseEntity.ok(raisonModifiee); // Retourne une réponse 200 avec la raison mise à jour
        } catch (EntityNotFoundException e) {
            // Gérer l'erreur si l'entité n'est pas trouvée
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Gérer toute autre erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
