package com.example.batimentDU.controller;

import com.example.batimentDU.dto.CommuniquerDecisionDTO;
import com.example.batimentDU.dto.EtatAvancementDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.EtatAvancement;
import com.example.batimentDU.service.EtatAvancementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/etat")
public class EtatAvancementController {

    @Autowired
    private EtatAvancementService etatAvancementService;

    // Afficher tous les Etats d'Avancement
    @GetMapping("/getEta")
    @ResponseBody
    public List<EtatAvancement> getEtatAvancement() {
        return etatAvancementService.findAll();
    }

    @GetMapping("/arrondissement/{nomArrondissement}")
    public ResponseEntity<List<EtatAvancementDTO>> getEtatAvancementByArrondissement(@PathVariable String nomArrondissement) {
        try {
            // Validation du paramètre
            if (nomArrondissement == null || nomArrondissement.trim().isEmpty()) {
                System.err.println("Le nom d'arrondissement est vide ou null.");
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }
            System.out.println("Nom d'arrondissement reçu : " + nomArrondissement);
            // Conversion du nom d’arrondissement en énumération
            NomArrondissement enumNomArrondissement = NomArrondissement.fromNomArabe(nomArrondissement);
            System.out.println("Conversion réussie en : " + enumNomArrondissement);
            // Appel au service pour récupérer les états d'avancement
            List<EtatAvancementDTO> etats = etatAvancementService.getEtatAvancementByNomArrondissement(enumNomArrondissement);

            // Gestion des cas où aucun état n'est trouvé
            if (etats.isEmpty()) {
                System.out.println("Aucun état d'avancement trouvé pour l'arrondissement : " + nomArrondissement);
                return ResponseEntity.ok(Collections.emptyList());
            }
            // Retour des données avec un statut HTTP 200
            return ResponseEntity.ok(etats);
        } catch (IllegalArgumentException e) {
            System.err.println("Nom d'arrondissement invalide : " + nomArrondissement);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement de la requête : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // Endpoint pour obtenir les EtatAvancement par nom d'annexe
    @GetMapping("/annexe/{nomAnnexe}")
    public  ResponseEntity<List<EtatAvancementDTO>> getEtatAvancementByNomAnnexe(@PathVariable NomAnnexe nomAnnexe) {
        List<EtatAvancementDTO> etats = etatAvancementService.getEtatAvancementByNomAnnexe(nomAnnexe);
        return ResponseEntity.ok(etats);
    }

    // Ajouter un nouvel Etat d'Avancement
    @PostMapping("/createEta")
    @ResponseBody
    public ResponseEntity<?> createEtatAvancement(
            @RequestParam("idBat") Long idBat,
            @RequestParam(value = "nbCorrespondanceAL", required = false) Integer nbCorrespondanceAL,
            @RequestParam(value = "dateCorrespondanceAL", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateCorrespondanceAL,
            @RequestParam(value = "nbReponseAL", required = false) Integer nbReponseAL,
            @RequestParam(value = "dateReponseAL", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReponseAL) {
        // Create an EtatAvancement object and populate only the fields provided
        EtatAvancement etatAvancement = new EtatAvancement();
        if (nbCorrespondanceAL != null) {
            etatAvancement.setNbCorrespondanceAL(nbCorrespondanceAL);
        }
        if (dateCorrespondanceAL != null) {
            etatAvancement.setDateCorrespondanceAL(dateCorrespondanceAL);
        }
        if (nbReponseAL != null) {
            etatAvancement.setNbReponseAL(nbReponseAL);
        }
        if (dateReponseAL != null) {
            etatAvancement.setDateReponseAL(dateReponseAL);
        }
        try {
            EtatAvancement savedEtatAvancement = etatAvancementService.addEtatAvancement(idBat, etatAvancement);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEtatAvancement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la création de l'État d'Avancement.");
        }
    }



    // Rechercher un Etat d'Avancement par ID
    @GetMapping("/found/{idEtat}")
    public ResponseEntity<EtatAvancement> findById(@PathVariable Long idEtat) {
        EtatAvancement etatAvancement = etatAvancementService.findById(idEtat);
        return ResponseEntity.ok(etatAvancement);
    }

    // Supprimer un Etat d'Avancement par ID
    @DeleteMapping("/delete/{idEtat}")
    public ResponseEntity<Void> deleteEtatAvancement(@PathVariable Long idEtat) {
        etatAvancementService.deleteEtatAvancement(idEtat);
        return ResponseEntity.noContent().build();
    }

    // Mettre à jour un Etat d'Avancement existant
    @PutMapping("/update/{idEtat}")
    public ResponseEntity<EtatAvancement> updateEtatAvancement(
            @PathVariable Long idEtat,
            @RequestBody EtatAvancement updatedEtatAvancement) {
        try {
            EtatAvancement updatedEntity = etatAvancementService.updateEtatAvancement(idEtat, updatedEtatAvancement);
            return ResponseEntity.ok(updatedEntity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
