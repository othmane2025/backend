package com.example.batimentDU.controller;

import com.example.batimentDU.dto.CommuniquerDecisionDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.CommuniquerDecision;
import com.example.batimentDU.model.EtatAvancement;
import com.example.batimentDU.model.Relogement;
import com.example.batimentDU.service.CommuniquerDecisionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/communiquer")
public class CommuniquerDecisionController {

    @Autowired
    private CommuniquerDecisionService communiquerDecisionService;

    // Afficher toutes les décisions
    @GetMapping("/getCom")
    @ResponseBody
    public List<CommuniquerDecision> getCommuniquer() {
        return communiquerDecisionService.findAll();
    }

    //
    @GetMapping("/arrondissement/{nomArrondissement}")
    public ResponseEntity<List<CommuniquerDecisionDTO>> getCommuniquerDecisionsByArrondissement(@PathVariable String nomArrondissement) {
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
            // Appel au service pour récupérer les CommuniquerDecision
            List<CommuniquerDecisionDTO> decisions = communiquerDecisionService.getCommuniquerDecisionsByNomArrondissement(enumNomArrondissement);
            // Gestion des cas où aucune décision n'est trouvée
            if (decisions.isEmpty()) {
                System.out.println("Aucune décision trouvée pour l'arrondissement : " + nomArrondissement);
                return ResponseEntity.ok(Collections.emptyList());
            }
            // Retour des données avec un statut HTTP 200
            return ResponseEntity.ok(decisions);
        } catch (IllegalArgumentException e) {
            System.err.println("Nom d'arrondissement invalide : " + nomArrondissement);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement de la requête : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // Afficher toutes
    @GetMapping("/annexe/{nomAnnexe}")
    public ResponseEntity<List<CommuniquerDecisionDTO>> getCommuniquerDecisionsByNomAnnexe(@PathVariable NomAnnexe nomAnnexe) {
        List<CommuniquerDecisionDTO> decisions = communiquerDecisionService.getCommuniquerDecisionsByNomAnnexe(nomAnnexe);
        return ResponseEntity.ok(decisions);
    }


    // Ajouter une nouvelle communiquer décision
    @PostMapping("/createCom")
    public ResponseEntity<?> addCommuniquerDecision(
            @RequestParam("idBat") Long idBat,
            @RequestParam(value = "nbDec", required = false) Integer nbDec, // `required = false` permet null
            @RequestParam(value = "dateDec", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDec) {

        try {
            CommuniquerDecision newCommunication = communiquerDecisionService.addCommuniquerDecision(idBat, nbDec, dateDec);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCommunication);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout de la communication.");
        }
    }


    // Rechercher une CommuniquerDecision par ID
    @GetMapping("/found/{idCom}")
    public ResponseEntity<CommuniquerDecision> findCommuniquerDecisionById(@PathVariable Long idCom) {
        try {
            CommuniquerDecision decision = communiquerDecisionService.findCommuniquerDecisionById(idCom);
            return ResponseEntity.ok(decision);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Supprimer une CommuniquerDecision
    @DeleteMapping("/delete/{idCom}")
    public ResponseEntity<Void> deleteCommuniquerDecision(@PathVariable Long idCom) {
        try {
            communiquerDecisionService.supprimerCommuniquerDecision(idCom);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Mettre à jour une CommuniquerDecision
    @PutMapping("/update/{idCom}")
    public ResponseEntity<CommuniquerDecision> updateCommuniquerDecision(
            @PathVariable Long idCom,
            @RequestBody CommuniquerDecision nouvelleDecision) {
        try {
            CommuniquerDecision decisionModifiee = communiquerDecisionService.modifierCommuniquerDecision(idCom, nouvelleDecision);
            return ResponseEntity.ok(decisionModifiee);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
