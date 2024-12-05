package com.example.batimentDU.controller;

import com.example.batimentDU.dto.InformerARDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.model.InformerAR;
import com.example.batimentDU.service.InformerARService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/informer")
public class InformerARController {

    @Autowired
    private InformerARService informerARService;

    // Afficher tous les InformerAR
    @GetMapping("/getInf")
    @ResponseBody
    public List<InformerAR> getInformerARService() {
        return informerARService.findAll();
    }

    // Méthode de point de terminaison pour obtenir les InformerAR en fonction de nomAnnexe
    @GetMapping("/annexe/{nomAnnexe}")
    public ResponseEntity<List<InformerARDTO>> getInformerARByNomAnnexe(@PathVariable NomAnnexe nomAnnexe) {
        List<InformerARDTO> informerARList = informerARService.getInformerAgentByNomAnnexe(nomAnnexe);
        return ResponseEntity.ok(informerARList);
    }

    // Ajouter un nouvel InformerAR
    @PostMapping("/createInf")
    @ResponseBody
    public ResponseEntity<?> addInformerAR(
            @RequestParam("idBat") Long idBat,
            @RequestParam(value = "nbAR", required = false) Integer nbAR,
            @RequestParam(value = "dateAr", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateAr) {
        // Créer un nouvel objet InformerAR
        InformerAR informerAR = new InformerAR();
        if (nbAR != null) {
            informerAR.setNbAR(nbAR);
        }
        if (dateAr != null) {
            informerAR.setDateAr(dateAr);
        }
        try {
            // Appeler le service pour sauvegarder l'objet InformerAR
            InformerAR savedInformerAR = informerARService.addInformerAR(idBat, informerAR);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedInformerAR);
        } catch (IllegalArgumentException e) {
            // Gérer les exceptions spécifiques
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout de l'InformerAR.");
        }
    }


    // Rechercher un InformerAR par ID
    @GetMapping("/found/{idAR}")
    public ResponseEntity<InformerAR> getInformerARById(@PathVariable Long idAR) {
        try {
            InformerAR informerAR = informerARService.findInformerARById(idAR);
            return ResponseEntity.ok(informerAR);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Supprimer un InformerAR par ID
    @DeleteMapping("/delete/{idAR}")
    public ResponseEntity<Void> deleteInformerAR(@PathVariable Long idAR) {
        try {
            informerARService.deleteInformerAR(idAR);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Modifier un InformerAR existant
    @PutMapping("/update/{idAR}")
    public ResponseEntity<InformerAR> updateInformerAR(
            @PathVariable Long idAR, @RequestBody InformerAR informerARDetails) {
        try {
            InformerAR updatedInformerAR = informerARService.updateInformerAR(idAR, informerARDetails);
            return ResponseEntity.ok(updatedInformerAR);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
