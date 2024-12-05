package com.example.batimentDU.controller;

import com.example.batimentDU.dto.DecisionCollectiveDTO;
import com.example.batimentDU.dto.DecisionDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.enumeraion.RecommandationDecision;
import com.example.batimentDU.model.CommuniquerDecision;
import com.example.batimentDU.model.DecisionCollective;
import com.example.batimentDU.model.EtatAvancement;
import com.example.batimentDU.model.InformerAR;
import com.example.batimentDU.model.Relogement;
import com.example.batimentDU.service.CommuniquerDecisionService;
import com.example.batimentDU.service.DecisionCollectiveService;
import com.example.batimentDU.service.EtatAvancementService;
import com.example.batimentDU.service.InformerARService;
import com.example.batimentDU.service.RelogementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/decision")
public class DecisionCollectiveController {

    @Autowired
    private DecisionCollectiveService decisionCollectiveService;

    @Autowired
    private CommuniquerDecisionService communiquerDecisionService;

    @Autowired
    private EtatAvancementService etatAvancementService;

    @Autowired
    private InformerARService informerARService;

    @Autowired
    private RelogementService relogementService;

    // Afficher toutes les décisions
    @GetMapping("/getDec")
    @ResponseBody
    public List<DecisionCollective> getDecision() {
        return decisionCollectiveService.findAll();
    }

    @GetMapping("/arrondissement/{nomArrondissement}")
    public ResponseEntity<List<DecisionCollectiveDTO>> getDecisionsByArrondissement(@PathVariable String nomArrondissement) {
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
            // Appel au service pour récupérer les décisions collectives
            List<DecisionCollectiveDTO> decisions = decisionCollectiveService.getDecisionCollectivesByNomArrondissement(enumNomArrondissement);
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

    // Endpoint pour obtenir les decisions par nom d'annexe en tant que String
    @GetMapping("/annexe/{nomAnnexe}")
    public ResponseEntity<List<DecisionCollectiveDTO>> getDecisionCollectivesByNomAnnexe(@PathVariable NomAnnexe nomAnnexe) {
        List<DecisionCollectiveDTO> decisions = decisionCollectiveService.getDecisionCollectivesByNomAnnexe(nomAnnexe);
        return ResponseEntity.ok(decisions);
    }


    @GetMapping("/downloadFileDecision/{idBat}")
    public ResponseEntity<Resource> downloadFileDecisionFromBatiment(@PathVariable("idBat") Long idBat) {
        try {
            // Trouver le chemin du fichier de décision en fonction de l'ID du bâtiment
            String filePath = decisionCollectiveService.findFileDecisionPathByBatimentId(idBat);
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Fichier non trouvé : " + filePath);
                return ResponseEntity.notFound().build();
            }
            System.out.println("Fichier trouvé, préparation pour le téléchargement : " + file.getPath());
            Resource resource = new FileSystemResource(file);
            String fileType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            // Détection du type de fichier
            if (file.getName().endsWith(".pdf")) {
                fileType = MediaType.APPLICATION_PDF_VALUE;
            } else if (file.getName().endsWith(".docx")) {
                fileType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            } else if (file.getName().endsWith(".txt")) {
                fileType = MediaType.TEXT_PLAIN_VALUE;
            } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
                fileType = MediaType.IMAGE_JPEG_VALUE;
            } else if (file.getName().endsWith(".png")) {
                fileType = MediaType.IMAGE_PNG_VALUE;
            } else if (file.getName().endsWith(".gif")) {
                fileType = MediaType.IMAGE_GIF_VALUE;
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du traitement de la demande de téléchargement pour l'ID du bâtiment : " + idBat);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Ajouter une nouvelle décision
    @PostMapping("/createDec")
    public ResponseEntity<?> addDecisionCollective(
            @RequestParam("idBat") Long idBat,
            @RequestParam(value = "nbDec", required = false) Integer nbDec,
            @RequestParam(value = "dateDec", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDec,
            @RequestParam(value = "recommandationDecision", required = false) String recommandationDecision,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            // Appeler le service pour ajouter la décision
            DecisionCollective newDecision = decisionCollectiveService.saveDecisionCollective(idBat, nbDec, dateDec, recommandationDecision, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(newDecision);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du traitement du fichier.");
        }
    }

    @GetMapping("/searchDecision")
    public ResponseEntity<List<DecisionDTO>> searchDecision(@RequestParam String recommandationDecision) {
        System.out.println("Recommandation reçue : " + recommandationDecision);
        List<DecisionDTO> results = decisionCollectiveService.chercherParRecommandationDecision(recommandationDecision);
        return ResponseEntity.ok(results);
    }




    // Rechercher une décision par ID
    @GetMapping("/found/{idDec}")
    public ResponseEntity<DecisionCollective> findById(@PathVariable Long idDec) {
        DecisionCollective decisionCollective = decisionCollectiveService.findById(idDec);
        return ResponseEntity.ok(decisionCollective);
    }

    // Supprimer une décision par ID
    @DeleteMapping("/delete/{idDec}")
    public ResponseEntity<Void> deleteDecisionCollective(@PathVariable Long idDec) {
        decisionCollectiveService.deleteDecisionCollective(idDec);
        return ResponseEntity.noContent().build();
    }

    // Mettre à jour une décision existante
    @PutMapping("/updateDecision/{idDec}")
    public ResponseEntity<?> updateDecisionCollectiveWithFile(
            @PathVariable Long idDec,
            @RequestPart("decisionCollective") String decisionCollectiveJson,
            @RequestPart(value = "fileRapport", required = false) MultipartFile file) {
        try {
            if (idDec == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de DecisionCollective manquant.");
            }
            // Désérialiser l'objet DecisionCollective à partir du JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Pour gérer les types LocalDate
            DecisionCollective updatedDecision = objectMapper.readValue(decisionCollectiveJson, DecisionCollective.class);
            // Appeler le service pour mettre à jour
            DecisionCollective decision = decisionCollectiveService.updateDecisionCollectiveWithFile(idDec, updatedDecision, file);
            // Retourner la réponse avec succès
            return ResponseEntity.ok(decision);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la mise à jour : " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}
