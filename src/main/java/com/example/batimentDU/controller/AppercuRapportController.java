package com.example.batimentDU.controller;


import com.example.batimentDU.dto.AppercuRapportDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.enumeraion.SituationChefFamille;
import com.example.batimentDU.enumeraion.SituationExploitant;
import com.example.batimentDU.model.*;
import com.example.batimentDU.service.AnnexeService;
import com.example.batimentDU.service.AppercuRapportService;
import com.example.batimentDU.service.BatimentService;
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
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/appercu")
public class AppercuRapportController {


    @Autowired
    AppercuRapportService appercuRapportService;

    @Autowired
    BatimentService batimentService;


    @GetMapping("/getApp")
    @ResponseBody
    public List<AppercuRapport> getAppercu(){
        return appercuRapportService.findAll();
    }

    // Endpoint pour récupérer les rapports d'aperçu par arrondissement
    @GetMapping("/arrondissement/{nomArrondissement}")
    public ResponseEntity<List<AppercuRapportDTO>> getAppercuRapportsByArrondissement(@PathVariable String nomArrondissement) {
        try {
            NomArrondissement enumNomArrondissement = NomArrondissement.fromNomArabe(nomArrondissement);
            List<AppercuRapportDTO> appercuRapportDTOs = appercuRapportService.getAppercuRapportsByArrondissement(enumNomArrondissement);
            return ResponseEntity.ok(appercuRapportDTOs);
        } catch (IllegalArgumentException e) {
            System.err.println("Nom d'arrondissement invalide : " + nomArrondissement);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }


    // Endpoint pour obtenir les appercu rapports par nom d'annexe en tant que String
    @GetMapping("/annexe/{nomAnnexe}")
    public ResponseEntity<List<AppercuRapportDTO>> getAppercuRapportsByNomAnnexe(@PathVariable String nomAnnexe) {
        try {
            NomAnnexe enumNomAnnexe = NomAnnexe.valueOf(nomAnnexe); // Conversion en énumération
            List<AppercuRapportDTO> appercuRapportDTOs = appercuRapportService.getAppercuRapportsByNomAnnexe(enumNomAnnexe);
            return ResponseEntity.ok(appercuRapportDTOs);
        } catch (IllegalArgumentException e) {
            // En cas de nom d'annexe invalide, retourner une erreur
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/downloadReportFromBatiment/{idBat}")
    public ResponseEntity<Resource> downloadReportFromBatiment(@PathVariable("idBat") Long idBat) {
        try {
            System.out.println("Recherche du bâtiment avec ID : " + idBat);
            Batiment batiment = batimentService.findBatimentWithReport(idBat);
            if (batiment == null) {
                System.out.println("Bâtiment non trouvé pour l'ID : " + idBat);
                return ResponseEntity.notFound().build();
            }
            AppercuRapport rapport = batiment.getAppercuRapport();
            if (rapport == null || rapport.getFilerapportInspection() == null) {
                System.out.println("Aperçu de rapport ou fichier non trouvé pour le bâtiment ID : " + idBat);
                return ResponseEntity.notFound().build();
            }
            File file = new File(rapport.getFilerapportInspection());
            if (!file.exists()) {
                System.out.println("Fichier non trouvé : " + rapport.getFilerapportInspection());
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
            }else if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {
                fileType = "application/vnd.ms-excel"; // MIME type for Excel files
            } else {
                throw new IllegalArgumentException("Format de fichier non pris en charge.");
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


    // Endpoint pour obtenir l'adresse d'un bâtiment par ID
    @GetMapping("/adresse/{idBat}")
    public ResponseEntity<Map<String, String>> getAdresseById(@PathVariable Long idBat) {
        return appercuRapportService.getAdresseById(idBat)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST pour créer un nouvel appercu avec un fichier
    @PostMapping("/createApp")
    public ResponseEntity<?> createAppercuRapportById(
            @RequestParam("idBat") Long idBat,
            @RequestParam(value = "nbRecommandation", required = false) Integer nbRecommandation,
            @RequestParam(value = "dateInspection", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateInspection,
            @RequestParam(value = "recommandation", required = false) String recommandation,
            @RequestParam(value = "nbNotificationRapportInspection", required = false) Integer nbNotificationRapportInspection,
            @RequestParam(value = "dateNotification", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNotification,
            @RequestParam(value = "filerapportInspection", required = false) MultipartFile file) {

        if (nbNotificationRapportInspection == null) {
            nbNotificationRapportInspection = 0; // Valeur par défaut si null
        }

        AppercuRapport appercuRapport = new AppercuRapport();
        appercuRapport.setNbRecommandation(nbRecommandation);
        appercuRapport.setDateInspection(dateInspection);
        appercuRapport.setRecommandation(recommandation);
        appercuRapport.setNbNotificationRapportInspection(nbNotificationRapportInspection);
        appercuRapport.setDateNotification(dateNotification);

        try {
            AppercuRapport result = appercuRapportService.addAppercuRapportToBatimentById(idBat, appercuRapport, file);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la création de l'AppercuRapport.");
        }
    }




    // Mapping pour la requête GET pour trouver un appercurapport par ID
    @GetMapping("/found/{idApp}")
    public ResponseEntity<AppercuRapport> findById(@PathVariable Long idApp) {
        AppercuRapport appercuRapport = appercuRapportService.findById(idApp);  // Appel au service
        return ResponseEntity.ok(appercuRapport);  // Renvoie un 200 OK avec l'objet Appercu
    }

    // Mapping pour la requête DELETE pour supprimer un appercurapport par ID
    @DeleteMapping("/delete/{idApp}")
    public ResponseEntity<Void> deleteAppercurapport(@PathVariable Long idApp) {
        appercuRapportService.deleteAppercuRapport(idApp);  // Appel au service pour supprimer le appercurapport
        return ResponseEntity.noContent().build();  // Renvoie une réponse 204 No Content si la suppression est réussie
    }

    // Mettre à jour un AppercuRapport existant
    @PutMapping("/updateApp/{idApp}")
    public ResponseEntity<AppercuRapport> updateAppercuRapportWithFile(
            @PathVariable Long idApp,
            @RequestPart("appercuRapport") String appercuRapportJson,
            @RequestPart(value = "filerapportInspection", required = false) MultipartFile file) {
        try {
            // Désérialiser l'objet AppercuRapport à partir du JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            AppercuRapport updatedAppercuRapport = objectMapper.readValue(appercuRapportJson, AppercuRapport.class);
            // Appeler le service pour mettre à jour
            AppercuRapport result = appercuRapportService.updateAppercuRapportWithFile(idApp, updatedAppercuRapport, file);
            // Retourner la réponse avec succès
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
