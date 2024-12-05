package com.example.batimentDU.controller;

import com.example.batimentDU.dto.BatimentDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.enumeraion.SituationChefFamille;
import com.example.batimentDU.enumeraion.SituationExploitant;
import com.example.batimentDU.model.*;
import com.example.batimentDU.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/batiment")
public class BatimentController {

    @Autowired
    private BatimentService batimentService;

    @Autowired
    private AnnexeService annexeService;

    @Autowired
    private ArrondissementService arrondissementService;

    @Autowired
    private StatutActuelService statutActuelService;

    @Autowired
    private AppercuRapportService appercuRapportService;

    @Autowired
    private FileStorageService fileStorageService;

    // Endpoint pour obtenir les bâtiments par nom d'arrondissement en tant que String
    @GetMapping("/arrondissement/{nomArrondissement}")
    @ResponseBody
    public ResponseEntity<List<BatimentDTO>> getBatimentsByArrondissement(@PathVariable String nomArrondissement) {
        try {
            // Convertir le nom arabe en énumération
            NomArrondissement enumNomArrondissement = NomArrondissement.fromNomArabe(nomArrondissement);
            List<BatimentDTO> batimentDTOs = batimentService.getBatimentsByArrondissement(enumNomArrondissement);
            return ResponseEntity.ok(batimentDTOs);
        } catch (IllegalArgumentException e) {
            System.err.println("Nom d'arrondissement invalide : " + nomArrondissement);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }




    // Endpoint pour obtenir les bâtiments par nom d'annexe en tant que String
    @GetMapping("/annexe/{nomAnnexe}")
    @ResponseBody
    public ResponseEntity<List<BatimentDTO>> getBatimentsByNomAnnexe(@PathVariable String nomAnnexe) {
        try {
            NomAnnexe enumNomAnnexe = NomAnnexe.valueOf(nomAnnexe);
            System.out.println("Fetching buildings for annex: " + enumNomAnnexe);
            List<BatimentDTO> batimentDTOs = batimentService.getBatimentsByNomAnnexe(enumNomAnnexe);
            return ResponseEntity.ok(batimentDTOs);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid annex name: " + nomAnnexe);
            return ResponseEntity.badRequest().build();
        }
    }



    // ajouter batiment à l'aide de l'annexe et arrondissement
    @PostMapping("/createBat")
    @ResponseBody
    public ResponseEntity<?> createBatiment(
            @RequestParam(value = "adresse", required = false) String adresse,
            @RequestParam(value = "surface", required = false) Double surface,
            @RequestParam(value = "nbetage", required = false) String NBetage,
            @RequestParam(value = "nomPrenomProprietaire", required = false) String nomPrenomProprietaire,
            @RequestParam(value = "nbFamilleResident", required = false) Integer nbFamilleResident,
            @RequestParam(value = "nomPrenomResident", required = false) String nomPrenomResident,
            @RequestParam(value = "situationChefFamille", required = false) String situationChefFamille,
            @RequestParam(value = "nbMagasin", required = false) Integer nbMagasin,
            @RequestParam(value = "nomPrenomExploitantMagasin", required = false) String nomPrenomExploitantMagasin,
            @RequestParam(value = "situationExploitant", required = false) String situationExploitant,
            @RequestParam(value = "idAnn", required = false) Long idAnn,
            @RequestParam(value = "idArr", required = false) Long idArr,
            @RequestParam(value = "filetelechargerImage", required = false) MultipartFile file) {
        try {
            // Validation des paramètres critiques
            if (idAnn == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'ID de l'annexe est requis.");
            }
            if (idArr == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'ID de l'arrondissement est requis.");
            }
            // Création de l'objet Batiment
            Batiment batiment = new Batiment();
            // Mise à jour des champs uniquement s'ils sont fournis
            if (adresse != null) batiment.setAdresse(adresse);
            if (surface != null) batiment.setSurface(surface);
            if (NBetage != null) batiment.setNBetage(NBetage);
            if (nomPrenomProprietaire != null) batiment.setNomPrenomProprietaire(nomPrenomProprietaire);
            if (nbFamilleResident != null) batiment.setNbFamilleResident(nbFamilleResident);
            if (nomPrenomResident != null) batiment.setNomPrenomResident(nomPrenomResident);
            if (situationChefFamille != null) batiment.setSituationChefFamille(situationChefFamille);
            if (nbMagasin != null) batiment.setNbMagasin(nbMagasin);
            if (nomPrenomExploitantMagasin != null) batiment.setNomPrenomExploitantMagasin(nomPrenomExploitantMagasin);
            if (situationExploitant != null) batiment.setSituationExploitant(situationExploitant);
            // Gestion de l'annexe et de l'arrondissement
            Annexe annexe = annexeService.findById(idAnn);
            if (annexe == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Annexe non trouvée.");
            }
            Arrondissement arrondissement = arrondissementService.findById(idArr);
            if (arrondissement == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Arrondissement non trouvé.");
            }
            // Associer l'annexe et l'arrondissement
            if (annexe.getArrondissement() == null || !annexe.getArrondissement().equals(arrondissement)) {
                annexe.setArrondissement(arrondissement);
            }
            batiment.setAnnexe(annexe);
            // Sauvegarde du bâtiment
            Batiment savedBatiment = batimentService.saveBatiment(batiment, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBatiment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur interne est survenue.");
        }
    }
    // Méthode pour créer une annexe par défaut si aucune information n'est fournie
    private Annexe createDefaultAnnexe() {
        Annexe annexe = new Annexe();
        NomAnnexe nomAnnexeParDefaut = NomAnnexe.values()[new java.util.Random().nextInt(NomAnnexe.values().length)];
        annexe.setNomAnnexe(nomAnnexeParDefaut);

        Arrondissement arrondissement = Math.random() < 0.5
                ? arrondissementService.findByArabicName("ابن مسيك")
                : arrondissementService.findByArabicName("سباتة");
        annexe.setArrondissement(arrondissement);

        System.out.println("Création et sauvegarde de l'annexe par défaut avec nom : " + annexe.getNomAnnexe());
        return annexeService.addAnnexe(annexe);
    }


    // telecherger fichier
    @GetMapping("/download/{idBat}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("idBat") Long idBat) {
        try {
            // Récupérer le bâtiment à partir de l'ID
            Batiment batiment = batimentService.findById(idBat);
            if (batiment == null || batiment.getFiletelechargerImage() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Construire le chemin du fichier
            String filePath = batiment.getFiletelechargerImage();
            File file = new File(filePath);
            // Vérifier si le fichier existe
            if (!file.exists()) {
                System.err.println("Fichier introuvable : " + filePath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Charger le fichier comme ressource
            Resource resource = new FileSystemResource(file);
            // Déterminer le type de contenu
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Par défaut
            }
            // Retourner le fichier comme ressource téléchargeable
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/found/{idBat}")
    public ResponseEntity<Batiment> findById(@PathVariable Long idBat) {
        try {
            Batiment batiment = batimentService.findById(idBat);
            return ResponseEntity.ok(batiment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{idBat}")
    public ResponseEntity<Void> deleteBatiment(@PathVariable Long idBat) {
        batimentService.deleteBatiment(idBat);
        return ResponseEntity.noContent().build();
    }

    // Méthode pour mettre à jour un bâtiment
    @PutMapping("/update/{idBat}")
    public ResponseEntity<Batiment> updateBatimentWithFile(
            @PathVariable Long idBat,
            @RequestPart(value = "filetelechargerImage", required = false) MultipartFile file,
            @RequestPart("batiment") String batimentJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Batiment updatedBatiment = objectMapper.readValue(batimentJson, Batiment.class);

            // Retrieve the existing Batiment record
            Batiment existingBatiment = batimentService.findById(idBat);
            if (existingBatiment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Update the file if provided
            if (file != null && !file.isEmpty()) {
                String fileName = fileStorageService.saveFile(file);
                updatedBatiment.setFiletelechargerImage(fileName);
            } else {
                updatedBatiment.setFiletelechargerImage(existingBatiment.getFiletelechargerImage());
            }

            // Perform the update
            Batiment updatedEntity = batimentService.updateBatiment(idBat, updatedBatiment,file);
            return ResponseEntity.ok(updatedEntity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


    }

}
