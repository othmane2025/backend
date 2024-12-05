package com.example.batimentDU.service;

import com.example.batimentDU.dto.AppercuRapportDTO;
import com.example.batimentDU.dto.BatimentDTO;
import com.example.batimentDU.dto.DecisionCollectiveDTO;
import com.example.batimentDU.dto.DecisionDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.*;
import com.example.batimentDU.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DecisionCollectiveService {
    @Autowired
    private  DecisionCollectiveRepository decisionCollectiveRepository;
    @Autowired
    private  CommuniquerDecisionRepository communiquerDecisionRepository;
    @Autowired
    private AppercuRapportRepository appercuRapportRepository;
    @Autowired
    private  BatimentRepository batimentRepository;
    // Chemin du dossier de sauvegarde des fichiers
    private static final String UPLOAD_DIR = "C:/uploads/decision/";



    // Récupérer toutes les décisions collectives
    public List<DecisionCollective> findAll() {
        return decisionCollectiveRepository.findAll();
    }


    //Méthode pour obtenir les decisions par nom d'arrondissement
    public List<DecisionCollectiveDTO> getDecisionCollectivesByNomArrondissement(NomArrondissement nomArrondissement) {
        // Étape 1 : Récupérer les bâtiments liés à l'arrondissement
        List<Batiment> batiments = batimentRepository.findByArrondissement(nomArrondissement);
        // Étape 2 : Mapper les bâtiments pour obtenir les décisions collectives associées
        return batiments.stream()
                .map(Batiment::getAppercuRapport)                    // Obtenir le rapport d'aperçu pour chaque bâtiment
                .filter(Objects::nonNull)                            // Filtrer les rapports d'aperçu null
                .map(AppercuRapport::getDecisionCollective)          // Obtenir la décision collective associée
                .filter(Objects::nonNull)                            // Filtrer les décisions collectives nulles
                .map(decision -> new DecisionCollectiveDTO(          // Mapper les données dans un DTO
                        decision.getIdDec(),
                        decision.getNbDec(),
                        decision.getDateDec(),
                        decision.getRecommandationDecision(),
                        decision.getFileRapport()
                        // Ajouter d'autres attributs ou relations si nécessaire
                ))
                .collect(Collectors.toList());
    }


    //Méthode pour obtenir les decisions par nom d'annexe
    public List<DecisionCollectiveDTO> getDecisionCollectivesByNomAnnexe(NomAnnexe nomAnnexe) {
        List<Batiment> batiments = batimentRepository.findByNomAnnexe(nomAnnexe);
        return batiments.stream()
                .map(Batiment::getAppercuRapport)                    // Get AppercuRapport for each Batiment
                .filter(Objects::nonNull)                            // Filter out null AppercuRapports
                .map(AppercuRapport::getDecisionCollective)          // Get DecisionCollective directly from AppercuRapport
                .filter(Objects::nonNull)                            // Filter out null DecisionCollectives
                .map(decision -> new DecisionCollectiveDTO(
                        decision.getIdDec(),
                        decision.getNbDec(),
                        decision.getDateDec(),
                        decision.getRecommandationDecision(),
                        decision.getFileRapport()
                        // Add any other attributes or relationships of DecisionCollective as needed
                ))
                .collect(Collectors.toList());
    }


    public String findFileDecisionPathByBatimentId(Long idBat) throws FileNotFoundException {
        return decisionCollectiveRepository.findByBatimentId(idBat)
                .orElseThrow(() -> new FileNotFoundException("Aucun fichier trouvé pour l'ID du bâtiment : " + idBat))
                .getFileRapport();
    }



    // Ajouter une nouvelle décision collective avec un fichier
    @Transactional
    public DecisionCollective saveDecisionCollective(
            Long idBat,
            Integer nbDec,
            LocalDate dateDec,
            String recommandationDecision,
            MultipartFile file) throws IOException {
        AppercuRapport appercuRapport = appercuRapportRepository.findByBatimentId(idBat)
                .orElseThrow(() -> new IllegalArgumentException("Aperçu introuvable pour le bâtiment avec l'ID " + idBat));
        if (appercuRapport.getDecisionCollective() != null) {
            throw new IllegalArgumentException("Une décision existe déjà pour ce bâtiment.");
        }
        DecisionCollective decisionCollective = new DecisionCollective();
        decisionCollective.setAppercuRapport(appercuRapport);
        if (nbDec != null) {
            decisionCollective.setNbDec(nbDec);
        }
        if (dateDec != null) {
            decisionCollective.setDateDec(dateDec);
        }
        if (recommandationDecision != null) {
            decisionCollective.setRecommandationDecision(recommandationDecision);
        }
        if (file != null && !file.isEmpty()) {
            String uploadDir = "C:/uploads/decision/";
            Files.createDirectories(Paths.get(uploadDir));
            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(Paths.get(filePath).toFile());
            decisionCollective.setFileRapport(filePath);
        }

        DecisionCollective savedDecision = decisionCollectiveRepository.save(decisionCollective);
        appercuRapport.setDecisionCollective(savedDecision);
        appercuRapportRepository.save(appercuRapport);

        return savedDecision;
    }

    // chercher
    @Transactional
    public List<DecisionDTO> chercherParRecommandationDecision(String recommandationDecision) {String formattedRecommandation = recommandationDecision.trim().toLowerCase();

        // Récupérer toutes les décisions collectives avec la recommandation spécifiée
        List<DecisionCollective> decisions = decisionCollectiveRepository.findByRecommandationDecision(formattedRecommandation);
        // Mapper les résultats dans le DTO
        return decisions.stream()
                .map(decision -> {
                    DecisionDTO dto = new DecisionDTO();
                    // Décision collective
                    dto.setDecisionCollective(decision);
                    // Récupérer l'aperçu lié
                    AppercuRapport appercuRapport = decision.getAppercuRapport();
                    dto.setAppercuRapport(appercuRapport);
                    // Récupérer le bâtiment lié via l'aperçu
                    if (appercuRapport != null) {
                        dto.setBatiment(appercuRapport.getBatiment());
                        // Récupérer l'expertise liée à l'aperçu
                        ExpertiseTechnique expertise = appercuRapport.getExpertise();
                        dto.setExpertise(expertise);
                    }
                    // Récupérer CommuniquerDecision liée à la décision
                    CommuniquerDecision communiquerDecision = decision.getCommuniquerDecision();
                    dto.setCommuniquerDecision(communiquerDecision);
                    if (communiquerDecision != null) {
                        // Relogement lié à CommuniquerDecision
                        dto.setRelogement(communiquerDecision.getRelogement());
                        // EtatAvancement lié à CommuniquerDecision
                        EtatAvancement etatAvancement = communiquerDecision.getEtatAvancement();
                        dto.setEtatAvancement(etatAvancement);
                        if (etatAvancement != null) {
                            // RaisonInobservation liée à EtatAvancement
                            dto.setRaisonInobservation(etatAvancement.getRaisonInobservation());
                            // InformerAR lié à EtatAvancement
                            dto.setInformerAR(etatAvancement.getInformerAR());
                        }
                    }
                    // Récupérer le StatutActuel du bâtiment
                    if (dto.getBatiment() != null) {
                        dto.setStatutActuel(dto.getBatiment().getStatutActuel());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }



    // Rechercher une décision collective par ID
    public DecisionCollective findById(Long idDec) {
        return decisionCollectiveRepository.findById(idDec)
                .orElseThrow(() -> new EntityNotFoundException("DecisionCollective avec l'ID " + idDec + " non trouvée."));
    }

    // Supprimer une décision collective
    @Transactional
    public void deleteDecisionCollective(long idDec) {
        DecisionCollective decisionCollective = decisionCollectiveRepository.findById(idDec)
                .orElseThrow(() -> new EntityNotFoundException("DecisionCollective avec l'ID " + idDec + " non trouvée."));

        // Détachez l'ExpertiseTechnique associée si elle existe


        // Supprimer la DecisionCollective
        decisionCollectiveRepository.delete(decisionCollective);
    }

    // Mettre à jour une décision collective existante
    @Transactional
    public DecisionCollective updateDecisionCollectiveWithFile(
            Long idDec, DecisionCollective updatedDecisionCollective, MultipartFile file) throws IOException {
        // Rechercher la décision collective existante
        DecisionCollective existingDecision = decisionCollectiveRepository.findById(idDec)
                .orElseThrow(() -> new EntityNotFoundException("DecisionCollective avec l'ID " + idDec + " non trouvée."));
        // Mettre à jour les champs de base
        existingDecision.setNbDec(updatedDecisionCollective.getNbDec());
        if (updatedDecisionCollective.getDateDec() != null) {
            existingDecision.setDateDec(updatedDecisionCollective.getDateDec());
        }
        if (updatedDecisionCollective.getRecommandationDecision() != null) {
            existingDecision.setRecommandationDecision(updatedDecisionCollective.getRecommandationDecision());
        }
        // Gestion du fichier (si fourni)
        if (file != null && !file.isEmpty()) {
            // Définir le répertoire de téléchargement
            String uploadDir = "C:/uploads/decision/";
            Files.createDirectories(Paths.get(uploadDir));
            // Définir le chemin complet du fichier
            String filePath = uploadDir + file.getOriginalFilename();
            // Sauvegarder le fichier sur le serveur
            file.transferTo(Paths.get(filePath).toFile());
            // Mettre à jour le chemin du fichier dans l'entité
            existingDecision.setFileRapport(filePath);
        }
        // Sauvegarder les modifications
        return decisionCollectiveRepository.save(existingDecision);
    }



}
