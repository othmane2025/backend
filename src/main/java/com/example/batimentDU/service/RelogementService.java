package com.example.batimentDU.service;

import com.example.batimentDU.dto.RelogementDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.*;
import com.example.batimentDU.repository.AppercuRapportRepository;
import com.example.batimentDU.repository.BatimentRepository;
import com.example.batimentDU.repository.CommuniquerDecisionRepository;
import com.example.batimentDU.repository.RelogementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RelogementService {

    @Autowired
    private RelogementRepository relogementRepository;

    @Autowired
    private CommuniquerDecisionRepository communiquerDecisionRepository;

    @Autowired
    private BatimentRepository batimentRepository;

    @Autowired
    private AppercuRapportRepository appercuRapportRepository;

    // Afficher tous les relogements
    public List<Relogement> findAll() {
        return relogementRepository.findAll();
    }

    //
    public List<RelogementDTO> getRelogementsByNomArrondissement(NomArrondissement nomArrondissement) {
        // Récupérer les bâtiments liés à l'arrondissement
        List<Batiment> batiments = batimentRepository.findByArrondissement(nomArrondissement);

        // Transformer les bâtiments en DTO de Relogement
        return batiments.stream()
                .map(Batiment::getAppercuRapport)                  // Obtenir AppercuRapport directement depuis Batiment
                .filter(Objects::nonNull)                          // Filtrer les AppercuRapports null
                .map(AppercuRapport::getDecisionCollective)        // Accéder à DecisionCollective depuis AppercuRapport
                .filter(Objects::nonNull)                          // Filtrer les DecisionCollectives nulles
                .map(DecisionCollective::getCommuniquerDecision)   // Obtenir CommuniquerDecision depuis DecisionCollective
                .filter(Objects::nonNull)                          // Filtrer les CommuniquerDecisions nulles
                .map(CommuniquerDecision::getRelogement)           // Accéder à Relogement depuis CommuniquerDecision
                .filter(Objects::nonNull)                          // Filtrer les Relogements nulls
                .map(relogement -> new RelogementDTO(              // Mapper Relogement vers RelogementDTO
                        relogement.getIdRel(),
                        relogement.getNbFamilleExplusees(),
                        relogement.getNbFamillePrestations(),
                        relogement.getNomPrenomPrestations(),
                        relogement.getNbFamilleBeneficiaire(),
                        relogement.getNomPrenomBeneficiaire(),
                        relogement.getDateTirageSort(),
                        relogement.getLieuPrestation(),
                        relogement.getRelocalisation(),
                        relogement.getSocieteSurveillance(),
                        relogement.getFileRapportTirage()
                ))
                .collect(Collectors.toList());
    }


    //
    public List<RelogementDTO> getRelogementsByNomAnnexe(NomAnnexe nomAnnexe) {
        List<Batiment> batiments = batimentRepository.findByNomAnnexe(nomAnnexe);
        return batiments.stream()
                .map(Batiment::getAppercuRapport)                  // Get AppercuRapport directly from Batiment
                .filter(Objects::nonNull)                          // Filter out null AppercuRapports
                .map(AppercuRapport::getDecisionCollective)        // Access DecisionCollective directly from AppercuRapport
                .filter(Objects::nonNull)                          // Filter out null DecisionCollectives
                .map(DecisionCollective::getCommuniquerDecision)   // Get CommuniquerDecision from DecisionCollective
                .filter(Objects::nonNull)                          // Filter out null CommuniquerDecisions
                .map(CommuniquerDecision::getRelogement)           // Access Relogement from CommuniquerDecision
                .filter(Objects::nonNull)                          // Filter out null Relogements
                .map(relogement -> new RelogementDTO(              // Map Relogement to RelogementDTO
                        relogement.getIdRel(),
                        relogement.getNbFamilleExplusees(),
                        relogement.getNbFamillePrestations(),
                        relogement.getNomPrenomPrestations(),
                        relogement.getNbFamilleBeneficiaire(),
                        relogement.getNomPrenomBeneficiaire(),
                        relogement.getDateTirageSort(),
                        relogement.getLieuPrestation(),
                        relogement.getRelocalisation(),
                        relogement.getSocieteSurveillance(),
                        relogement.getFileRapportTirage()
                ))
                .collect(Collectors.toList());
    }


    //
    public String findFileRelogementPathByBatimentId(Long idBat) throws FileNotFoundException {
        return relogementRepository.findByBatimentId(idBat)
                .orElseThrow(() -> new FileNotFoundException("Aucun fichier de relogement trouvé pour l'ID du bâtiment : " + idBat))
                .getFileRapportTirage();
    }


    // Ajouter un nouveau relogement avec un fichier
    @Transactional
    public Relogement addRelogement(Long idBat, Relogement relogement, MultipartFile file) throws IOException {
        // Retrieve AppercuRapport associated with the building
        AppercuRapport appercuRapport = appercuRapportRepository.findByBatimentId(idBat)
                .orElseThrow(() -> new IllegalArgumentException("Aperçu introuvable pour le bâtiment avec l'ID " + idBat));

        DecisionCollective decisionCollective = appercuRapport.getDecisionCollective();
        if (decisionCollective == null) {
            throw new IllegalArgumentException("Aucune décision collective trouvée pour cet aperçu.");
        }
        CommuniquerDecision communiquerDecision = decisionCollective.getCommuniquerDecision();
        if (communiquerDecision == null) {
            throw new IllegalArgumentException("Aucune communication trouvée pour cette décision collective.");
        }
        if (relogementRepository.existsByCommuniquerDecision_IdCom(communiquerDecision.getIdCom())) {
            throw new IllegalArgumentException("Un relogement existe déjà pour ce bâtiment.");
        }
        // Handle file if present
        if (file != null && !file.isEmpty()) {
            Files.createDirectories(Paths.get("C:/uploads/relogement/"));
            String filePath = "C:/uploads/relogement/" + file.getOriginalFilename();
            file.transferTo(Paths.get(filePath).toFile());
            relogement.setFileRapportTirage(filePath);
        } else {
            relogement.setFileRapportTirage(null); // Handle case when no file is provided
        }
        relogement.setCommuniquerDecision(communiquerDecision);
        Relogement savedRelogement = relogementRepository.save(relogement);
        communiquerDecision.setRelogement(savedRelogement);
        communiquerDecisionRepository.save(communiquerDecision);
        return savedRelogement;
    }




    // Sauvegarder un relogement
    @Transactional
    public Relogement saveRelogement(Relogement relogement) {
        return relogementRepository.save(relogement);
    }

    // Rechercher un relogement par ID
    public Relogement findById(Long idRel) {
        return relogementRepository.findById(idRel)
                .orElseThrow(() -> new EntityNotFoundException("Relogement with id " + idRel + " not found"));
    }

    // Supprimer un relogement par ID
    @Transactional
    public void deleteRelogement(long idRel) {
        Relogement relogement = relogementRepository.findById(idRel)
                .orElseThrow(() -> new EntityNotFoundException("Relogement with id " + idRel + " not found"));

        CommuniquerDecision decision = relogement.getCommuniquerDecision();
        if (decision != null) {
            decision.setRelogement(null);
            communiquerDecisionRepository.save(decision);
        }

        relogementRepository.deleteById(idRel);
    }

    // Mettre à jour un relogement existant
    @Transactional
    public Relogement updateRelogementWithFile(
            Long idRel, Relogement updatedRelogement, MultipartFile file) throws IOException {

        // Rechercher le relogement existant
        Relogement existingRelogement = relogementRepository.findById(idRel)
                .orElseThrow(() -> new EntityNotFoundException("Relogement avec l'ID " + idRel + " non trouvé"));

        // Mettre à jour les champs si non null
            existingRelogement.setNbFamilleExplusees(updatedRelogement.getNbFamilleExplusees());
            existingRelogement.setNbFamillePrestations(updatedRelogement.getNbFamillePrestations());
        if (updatedRelogement.getNomPrenomPrestations() != null) {
            existingRelogement.setNomPrenomPrestations(updatedRelogement.getNomPrenomPrestations());
        }
            existingRelogement.setNbFamilleBeneficiaire(updatedRelogement.getNbFamilleBeneficiaire());

        if (updatedRelogement.getNomPrenomBeneficiaire() != null) {
            existingRelogement.setNomPrenomBeneficiaire(updatedRelogement.getNomPrenomBeneficiaire());
        }
        if (updatedRelogement.getDateTirageSort() != null) {
            existingRelogement.setDateTirageSort(updatedRelogement.getDateTirageSort());
        }
        if (updatedRelogement.getLieuPrestation() != null) {
            existingRelogement.setLieuPrestation(updatedRelogement.getLieuPrestation());
        }
        if (updatedRelogement.getRelocalisation() != null) {
            existingRelogement.setRelocalisation(updatedRelogement.getRelocalisation());
        }
        if (updatedRelogement.getSocieteSurveillance() != null) {
            existingRelogement.setSocieteSurveillance(updatedRelogement.getSocieteSurveillance());
        }
        // Gestion du fichier (si fourni)
        if (file != null && !file.isEmpty()) {
            String uploadDir = "C:/uploads/relogement/";
            Files.createDirectories(Paths.get(uploadDir));
            // Chemin complet du fichier
            String filePath = uploadDir + file.getOriginalFilename();
            // Sauvegarder le fichier sur le serveur
            file.transferTo(Paths.get(filePath).toFile());
            // Mettre à jour le chemin du fichier
            existingRelogement.setFileRapportTirage(filePath);
        }
        // Sauvegarder les modifications
        return relogementRepository.save(existingRelogement);
    }

}
