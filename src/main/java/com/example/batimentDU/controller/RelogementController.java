package com.example.batimentDU.controller;

import com.example.batimentDU.dto.RelogementDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.enumeraion.SocieteSurveillance;
import com.example.batimentDU.model.Relogement;
import com.example.batimentDU.service.RelogementService;
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
@RequestMapping("/relogement")
public class RelogementController {

    @Autowired
    private RelogementService relogementService;

    // Afficher tous les relogements
    @GetMapping("/getRel")
    @ResponseBody
    public List<Relogement> getRelogement() {
        return relogementService.findAll();
    }

    //
    @GetMapping("/arrondissement/{nomArrondissement}")
    public ResponseEntity<List<RelogementDTO>> getRelogementsByArrondissement(@PathVariable String nomArrondissement) {
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
            // Appel au service pour récupérer les relogements
            List<RelogementDTO> relogements = relogementService.getRelogementsByNomArrondissement(enumNomArrondissement);
            // Gestion des cas où aucun relogement n'est trouvé
            if (relogements.isEmpty()) {
                System.out.println("Aucun relogement trouvé pour l'arrondissement : " + nomArrondissement);
                return ResponseEntity.ok(Collections.emptyList());
            }
            // Retour des données avec un statut HTTP 200
            return ResponseEntity.ok(relogements);

        } catch (IllegalArgumentException e) {
            System.err.println("Nom d'arrondissement invalide : " + nomArrondissement);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement de la requête : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    //
    @GetMapping("/annexe/{nomAnnexe}")
    public ResponseEntity<List<RelogementDTO>> getRelogementsByNomAnnexe(@PathVariable NomAnnexe nomAnnexe) {
        List<RelogementDTO> relogements = relogementService.getRelogementsByNomAnnexe(nomAnnexe);
        if (relogements.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si aucune donnée trouvée
        }
        return ResponseEntity.ok(relogements); // 200 OK avec les données de relogement
    }

    //
    @GetMapping("/downloadFileRelogement/{idBat}")
    public ResponseEntity<Resource> downloadFileRelogementFromBatiment(@PathVariable("idBat") Long idBat) {
        try {
            // Trouver le chemin du fichier de relogement en fonction de l'ID du bâtiment
            String filePath = relogementService.findFileRelogementPathByBatimentId(idBat);
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Fichier de relogement non trouvé : " + filePath);
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


    // Créer un nouveau relogement avec un fichier
    @PostMapping("/createRel")
    @ResponseBody
    public ResponseEntity<?> createRelogement(
            @RequestParam("idBat") Long idBat,
            @RequestParam(value = "NBFamilleExplusees", required = false) Integer NBFamilleExplusees,
            @RequestParam(value = "NBfamillePrestations", required = false) Integer NBfamillePrestations,
            @RequestParam(value = "nomPrenomPrestations", required = false) String nomPrenomPrestations,
            @RequestParam(value = "NBfamilleBeneficiaire", required = false) Integer NBfamilleBeneficiaire,
            @RequestParam(value = "nomPrenomBeneficiaire", required = false) String nomPrenomBeneficiaire,
            @RequestParam(value = "dateTirageSort", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTirageSort,
            @RequestParam(value = "lieuPrestation", required = false) String lieuPrestation,
            @RequestParam(value = "relocalisation", required = false) String relocalisation,
            @RequestParam(value = "societeSurveillance", required = false) String societeSurveillance,
            @RequestParam(value = "fileRapportTirage", required = false) MultipartFile file) {
        // Créer un objet Relogement
        Relogement relogement = new Relogement();
        if (NBFamilleExplusees != null) {
            relogement.setNbFamilleExplusees(NBFamilleExplusees);
        }
        if (NBfamillePrestations != null) {
            relogement.setNbFamillePrestations(NBfamillePrestations);
        }
        if (nomPrenomPrestations != null) {
            relogement.setNomPrenomPrestations(nomPrenomPrestations);
        }
        if (NBfamilleBeneficiaire != null) {
            relogement.setNbFamilleBeneficiaire(NBfamilleBeneficiaire);
        }
        if (nomPrenomBeneficiaire != null) {
            relogement.setNomPrenomBeneficiaire(nomPrenomBeneficiaire);
        }
        if (dateTirageSort != null) {
            relogement.setDateTirageSort(dateTirageSort);
        }
        if (lieuPrestation != null) {
            relogement.setLieuPrestation(lieuPrestation);
        }
        if (relocalisation != null) {
            relogement.setRelocalisation(relocalisation);
        }
        if (societeSurveillance != null) {
            relogement.setSocieteSurveillance(societeSurveillance);
        }
        try {
            Relogement savedRelogement = relogementService.addRelogement(idBat, relogement, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRelogement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la sauvegarde du fichier.");
        }
    }



    // Rechercher un relogement par ID
    @GetMapping("/found/{idRel}")
    public ResponseEntity<Relogement> findById(@PathVariable Long idRel) {
        Relogement relogement = relogementService.findById(idRel);
        return ResponseEntity.ok(relogement);
    }

    // Supprimer un relogement par ID
    @DeleteMapping("/delete/{idRel}")
    public ResponseEntity<Void> deleteRelogement(@PathVariable Long idRel) {
        relogementService.deleteRelogement(idRel);
        return ResponseEntity.noContent().build();
    }

    // Mettre à jour un relogement existant
    @PutMapping("/updateRelogement/{idRel}")
    public ResponseEntity<Relogement> updateRelogementWithFile(
            @PathVariable Long idRel,
            @RequestPart("relogement") String relogementJson,
            @RequestPart(value = "fileRapportTirage", required = false) MultipartFile file) {
        try {
            // Désérialisation de l'objet Relogement
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Gestion de LocalDate
            Relogement updatedRelogement = objectMapper.readValue(relogementJson, Relogement.class);
            // Appeler la méthode du service pour mettre à jour
            Relogement result = relogementService.updateRelogementWithFile(idRel, updatedRelogement, file);
            // Retourner la réponse en cas de succès
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
