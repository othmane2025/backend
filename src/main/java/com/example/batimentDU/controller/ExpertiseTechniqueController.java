package com.example.batimentDU.controller;

import com.example.batimentDU.dto.ExpertiseDTO;
import com.example.batimentDU.dto.ExpertiseTechniqueDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.enumeraion.RecommandationExpertise;
import com.example.batimentDU.model.AppercuRapport;
import com.example.batimentDU.model.DecisionCollective;
import com.example.batimentDU.model.ExpertiseTechnique;
import com.example.batimentDU.service.AppercuRapportService;
import com.example.batimentDU.service.BatimentService;
import com.example.batimentDU.service.DecisionCollectiveService;
import com.example.batimentDU.service.ExpertiseTechniqueService;
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
import java.util.Map;

@Controller
@RequestMapping("/expertise")
public class ExpertiseTechniqueController {

    @Autowired
    private ExpertiseTechniqueService expertiseTechniqueService;

    @Autowired
    private AppercuRapportService appercuRapportService;

    @Autowired
    private DecisionCollectiveService decisionCollectiveService;

    @Autowired
    private BatimentService batimentService;

    // Afficher toutes les Expertises Techniques
    @GetMapping("/getExp")
    @ResponseBody
    public List<ExpertiseTechnique> getExpertiseTechnique() {
        return expertiseTechniqueService.findAll();
    }

    // Endpoint pour récupérer les expertises par arrondissement
    @GetMapping("/arrondissement/{nomArrondissement}")
    public ResponseEntity<List<ExpertiseTechniqueDTO>> getExpertisesByArrondissement(@PathVariable String nomArrondissement) {
        try {
            // Validation du paramètre
            if (nomArrondissement == null || nomArrondissement.trim().isEmpty()) {
                System.err.println("Le nom d'arrondissement est vide ou null.");
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }
            System.out.println("Nom d'arrondissement reçu : " + nomArrondissement);
            // Conversion du nom d'arrondissement (par exemple en énumération)
            NomArrondissement enumNomArrondissement = NomArrondissement.fromNomArabe(nomArrondissement);
            System.out.println("Conversion réussie en : " + enumNomArrondissement);
            // Appel au service pour récupérer les expertises
            List<ExpertiseTechniqueDTO> expertises = expertiseTechniqueService.getExpertiseTechniquesByNomArrondissement(enumNomArrondissement);
            // Gestion des cas où aucune expertise n'est trouvée
            if (expertises.isEmpty()) {
                System.out.println("Aucune expertise trouvée pour l'arrondissement : " + nomArrondissement);
                return ResponseEntity.ok(Collections.emptyList());
            }
            // Retour des données avec un statut HTTP 200
            return ResponseEntity.ok(expertises);

        } catch (IllegalArgumentException e) {
            // Gestion des erreurs de conversion ou paramètres invalides
            System.err.println("Nom d'arrondissement invalide : " + nomArrondissement);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            // Gestion des erreurs imprévues
            System.err.println("Erreur lors du traitement de la requête : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }




    // Endpoint pour obtenir les expertises par nom d'annexe en tant que String
    @GetMapping("/annexe/{nomAnnexe}")
    @ResponseBody
    public ResponseEntity<List<ExpertiseTechniqueDTO>> getExpertiseTechniquesByNomAnnexe(@PathVariable String nomAnnexe) {
        try {
            NomAnnexe enumNomAnnexe = NomAnnexe.valueOf(nomAnnexe); // Conversion en énumération
            List<ExpertiseTechniqueDTO> expertiseTechniqueDTOs = expertiseTechniqueService.getExpertiseTechniquesByNomAnnexe(enumNomAnnexe);
            return ResponseEntity.ok(expertiseTechniqueDTOs);
        } catch (IllegalArgumentException e) {
            // En cas de nom d'annexe invalide, retourner une erreur 400 (Bad Request)
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/downloadFileExpertise/{idBat}")
    public ResponseEntity<Resource> downloadFileExpertiseFromBatiment(@PathVariable("idBat") Long idBat) {
        try {
            String filePath = batimentService.findFileExpertisePathByBatimentId(idBat);
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Fichier non trouvé : " + filePath);
                return ResponseEntity.notFound().build();
            }
            System.out.println("Fichier trouvé, préparation pour le téléchargement : " + file.getPath());
            Resource resource = new FileSystemResource(file);
            String fileType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
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
            System.out.println("Erreur lors du traitement de la demande de téléchargement pour l'ID : " + idBat);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ajouter une nouvelle Expertise Technique
    @PostMapping("/createExp")
    public ResponseEntity<?> addExpertiseTechnique(
            @RequestParam(value = "bureauEtude", required = false) String bureauEtude,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "recommandationExpertise", required = false) String recommandationExpertise,
            @RequestParam(value = "fileExpertise", required = false) MultipartFile file,
            @RequestParam("idBat") Long idBat) {
        try {
            ExpertiseTechnique savedExpertise = expertiseTechniqueService.saveExpertiseTechniqueWithBatiment(
                    bureauEtude, date, recommandationExpertise, file, idBat);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedExpertise);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du traitement du fichier.");
        }
    }

    // Rechercher une Expertise Technique par ID
    @GetMapping("/found/{idExp}")
    public ResponseEntity<ExpertiseTechnique> findById(@PathVariable Long idExp) {
        try {
            ExpertiseTechnique expertiseTechnique = expertiseTechniqueService.findById(idExp);
            return ResponseEntity.ok(expertiseTechnique);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


   // Rechercher par recommandation
    @GetMapping("/searchExp")
    public ResponseEntity<List<ExpertiseDTO>> searchExpertises(@RequestParam String recommandation) {
        System.out.println("Recommandation reçue : " + recommandation);
        List<ExpertiseDTO> results = expertiseTechniqueService.chercherParRecommandation(recommandation);
        results.forEach(dto -> System.out.println("Expertise: " + dto));
        return ResponseEntity.ok(results);
    }




    // Supprimer une Expertise Technique par ID
    @DeleteMapping("/delete/{idExp}")
    public ResponseEntity<Void> deleteExpertiseTechnique(@PathVariable Long idExp) {
        expertiseTechniqueService.deleteExpertiseTechnique(idExp);
        return ResponseEntity.noContent().build();
    }

    // Mettre à jour une Expertise Technique existante
    @PutMapping("/updateExpertise/{idExp}")
    public ResponseEntity<?> updateExpertiseWithFile(
            @PathVariable Long idExp,
            @RequestPart("expertiseTechnique") String expertiseTechniqueJson,
            @RequestPart(value = "fileExpertise", required = false) MultipartFile file) {
        try {
            if (idExp == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID d'ExpertiseTechnique manquant.");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Pour gérer les types LocalDate
            ExpertiseTechnique updatedExpertise = objectMapper.readValue(expertiseTechniqueJson, ExpertiseTechnique.class);

            ExpertiseTechnique expertise = expertiseTechniqueService.updateExpertiseWithFile(idExp, updatedExpertise, file);
            return ResponseEntity.ok(expertise);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la mise à jour : " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }





}
