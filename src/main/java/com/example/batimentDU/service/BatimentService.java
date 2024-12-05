package com.example.batimentDU.service;
import com.example.batimentDU.dto.*;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.*;
import com.example.batimentDU.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BatimentService {

    @Autowired
    private  BatimentRepository batimentRepository;
    @Autowired
    private  AppercuRapportRepository appercuRapportRepository;
    @Autowired
    private ExpertiseTechniqueRepository expertiseTechniqueRepository;
    @Autowired
    private  AnnexeRepository annexeRepository;
    private  Annexe defaultAnnexe;
    private  StatutActuel defaultStatutActuel;
    private  AppercuRapport defaultAppercuRapport;
    @Autowired
    private StatutActuelRepository statutActuelRepository;
    private final String UPLOAD_DIR = "C:/uploads/batiment/";



    // Obtenir tous les bâtiments
    public List<Batiment> findAll() {
        return batimentRepository.findAll();
    }

    private String saveFileToServer(MultipartFile file, String uploadDir) throws IOException {
        if (file == null || file.isEmpty()) {
            return null; // Aucun fichier fourni
        }
        // Créer le répertoire de téléchargement s'il n'existe pas
        Files.createDirectories(Paths.get(uploadDir));

        // Générer le chemin complet pour le fichier
        String filePath = uploadDir + file.getOriginalFilename();
        // Copier le fichier dans le répertoire
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }
        return filePath;
    }

    // Sauvegarder un bâtiment avec un fichier
    @Transactional
    public Batiment saveBatiment(Batiment batiment, MultipartFile file) throws IOException {
        // Vérifier les doublons si l'adresse et l'annexe sont définies
        if (batiment.getAdresse() != null && batiment.getAnnexe() != null) {
            Optional<Batiment> existingBatiment = batimentRepository.findByAdresseAndAnnexe(
                    batiment.getAdresse(),
                    batiment.getAnnexe().getIdAnn()
            );
            if (existingBatiment.isPresent()) {
                throw new IllegalArgumentException("Un bâtiment avec cette adresse et annexe existe déjà.");
            }
        }
        // Gérer le fichier (facultatif)
        String filePath = saveFileToServer(file, "C:/uploads/batiment/");
        if (filePath != null) {
            batiment.setFiletelechargerImage(filePath); // Associer le fichier au bâtiment
        }

        // Sauvegarder l'entité et retourner le bâtiment sauvegardé
        return batimentRepository.save(batiment);
    }

    // Méthode pour obtenir les bâtiments par nom d'arrondissement
    @Transactional
    public List<BatimentDTO> getBatimentsByArrondissement(NomArrondissement nomArrondissement) {
        List<Batiment> batiments = batimentRepository.findByArrondissement(nomArrondissement);

        return batiments.stream().map(batiment -> {
            // Map Batiment to BatimentDTO (similar to getBatimentsByNomAnnexe)
            BatimentDTO dto = new BatimentDTO();
            dto.setIdBat(batiment.getIdBat());
            dto.setAdresse(batiment.getAdresse());
            dto.setSurface(batiment.getSurface());
            dto.setNBetage(batiment.getNBetage());
            dto.setNomPrenomProprietaire(batiment.getNomPrenomProprietaire());
            dto.setNbFamilleResident(batiment.getNbFamilleResident());
            dto.setNomPrenomResident(batiment.getNomPrenomResident());
            dto.setSituationChefFamille(batiment.getSituationChefFamille());
            dto.setNbMagasin(batiment.getNbMagasin());
            dto.setNomPrenomExploitantMagasin(batiment.getNomPrenomExploitantMagasin());
            dto.setSituationExploitant(batiment.getSituationExploitant());

            // Add Status
            if (batiment.getStatutActuel() != null) {
                StatutActuel statut = batiment.getStatutActuel();
                StatutActuelDTO statutDto = new StatutActuelDTO();
                statutDto.setIdStatut(statut.getIdStatut());
                statutDto.setVide(statut.getVide());
                statutDto.setVacant(statut.getVacant());
                statutDto.setEntierementDemoli(statut.getEntierementDemoli());
                statutDto.setPartiellementDemoli(statut.getPartiellementDemoli());
                statutDto.setRenforce(statut.getRenforce());
                statutDto.setRestaurer(statut.getRestaurer());
                dto.setStatut(statutDto);
            }

            // Add AppercuRapport
            if (batiment.getAppercuRapport() != null) {
                AppercuRapportDTO appercuDto = new AppercuRapportDTO();
                appercuDto.setIdApp(batiment.getAppercuRapport().getIdApp());
                appercuDto.setNbRecommandation(batiment.getAppercuRapport().getNbRecommandation());
                appercuDto.setDateInspection(batiment.getAppercuRapport().getDateInspection());
                appercuDto.setRecommandation(batiment.getAppercuRapport().getRecommandation());
                appercuDto.setNbNotificationRapportInspection(batiment.getAppercuRapport().getNbNotificationRapportInspection());
                appercuDto.setDateNotification(batiment.getAppercuRapport().getDateNotification());

                // Expertise
                if (batiment.getAppercuRapport().getExpertise() != null) {
                    ExpertiseTechnique expertise = batiment.getAppercuRapport().getExpertise();
                    ExpertiseTechniqueDTO expertiseDto = new ExpertiseTechniqueDTO();
                    expertiseDto.setIdExp(expertise.getIdExp());
                    expertiseDto.setBureauEtude(expertise.getBureauEtude());
                    expertiseDto.setDate(expertise.getDate());
                    expertiseDto.setRecommandationExpertise(expertise.getRecommandationExpertise());
                    expertiseDto.setFileExpertise(expertise.getFileExpertise());
                    appercuDto.setExpertise(expertiseDto);
                }

                // Decision Collective
                if (batiment.getAppercuRapport().getDecisionCollective() != null) {
                    DecisionCollective decision = batiment.getAppercuRapport().getDecisionCollective();
                    DecisionCollectiveDTO decisionDto = new DecisionCollectiveDTO();
                    decisionDto.setIdDec(decision.getIdDec());
                    decisionDto.setNbDec(decision.getNbDec());
                    decisionDto.setDateDec(decision.getDateDec());
                    decisionDto.setRecommandationDecision(decision.getRecommandationDecision());
                    decisionDto.setFileRapport(decision.getFileRapport());
                    appercuDto.setDecision(decisionDto);
                }

                dto.setAppercuRapport(appercuDto);
            }

            return dto;
        }).collect(Collectors.toList());
    }


    // Méthode pour obtenir les bâtiments par nom d'annexe
    @Transactional
    public List<BatimentDTO> getBatimentsByNomAnnexe(NomAnnexe nomAnnexe) {
        List<Batiment> batiments = batimentRepository.findByNomAnnexe(nomAnnexe);

        return batiments.stream().map(batiment -> {
            // Initialiser le DTO du bâtiment
            BatimentDTO dto = new BatimentDTO();
            dto.setIdBat(batiment.getIdBat());
            dto.setAdresse(batiment.getAdresse());
            dto.setSurface(batiment.getSurface());
            dto.setNBetage(batiment.getNBetage());
            dto.setNomPrenomProprietaire(batiment.getNomPrenomProprietaire());
            dto.setNbFamilleResident(batiment.getNbFamilleResident());
            dto.setNomPrenomResident(batiment.getNomPrenomResident());
            dto.setSituationChefFamille(batiment.getSituationChefFamille());
            dto.setNbMagasin(batiment.getNbMagasin());
            dto.setNomPrenomExploitantMagasin(batiment.getNomPrenomExploitantMagasin());
            dto.setSituationExploitant(batiment.getSituationExploitant());

            // Ajouter le statut actuel au DTO
            if (batiment.getStatutActuel() != null) {
                StatutActuel statut = batiment.getStatutActuel();
                StatutActuelDTO statutDto = new StatutActuelDTO();
                statutDto.setIdStatut(statut.getIdStatut());
                statutDto.setVide(statut.getVide());
                statutDto.setVacant(statut.getVacant());
                statutDto.setEntierementDemoli(statut.getEntierementDemoli());
                statutDto.setPartiellementDemoli(statut.getPartiellementDemoli());
                statutDto.setRenforce(statut.getRenforce());
                statutDto.setRestaurer(statut.getRestaurer());
                dto.setStatut(statutDto); // Associer le StatutActuelDTO au BatimentDTO
            }

            // Ajouter l'AppercuRapport au DTO du bâtiment
            if (batiment.getAppercuRapport() != null) {
                AppercuRapportDTO appercuDto = new AppercuRapportDTO();
                appercuDto.setIdApp(batiment.getAppercuRapport().getIdApp());
                appercuDto.setNbRecommandation(batiment.getAppercuRapport().getNbRecommandation());
                appercuDto.setDateInspection(batiment.getAppercuRapport().getDateInspection());
                appercuDto.setRecommandation(batiment.getAppercuRapport().getRecommandation());
                appercuDto.setNbNotificationRapportInspection(batiment.getAppercuRapport().getNbNotificationRapportInspection());
                appercuDto.setDateNotification(batiment.getAppercuRapport().getDateNotification());

                // Ajouter l'expertise unique au DTO
                if (batiment.getAppercuRapport().getExpertise() != null) {
                    ExpertiseTechnique expertise = batiment.getAppercuRapport().getExpertise();
                    ExpertiseTechniqueDTO expertiseDto = new ExpertiseTechniqueDTO();
                    expertiseDto.setIdExp(expertise.getIdExp());
                    expertiseDto.setBureauEtude(expertise.getBureauEtude());
                    expertiseDto.setDate(expertise.getDate());
                    expertiseDto.setRecommandationExpertise(expertise.getRecommandationExpertise());
                    expertiseDto.setFileExpertise(expertise.getFileExpertise());
                    appercuDto.setExpertise(expertiseDto); // Lier une seule expertise
                }

                // Inclure la décision collective associée, si elle existe
                if (batiment.getAppercuRapport().getDecisionCollective() != null) {
                    DecisionCollective decision = batiment.getAppercuRapport().getDecisionCollective();
                    DecisionCollectiveDTO decisionDto = new DecisionCollectiveDTO();
                    decisionDto.setIdDec(decision.getIdDec());
                    decisionDto.setNbDec(decision.getNbDec());
                    decisionDto.setDateDec(decision.getDateDec());
                    decisionDto.setRecommandationDecision(decision.getRecommandationDecision());
                    decisionDto.setFileRapport(decision.getFileRapport());
                    // Associer la décision collective au DTO d'AppercuRapport
                    appercuDto.setDecision(decisionDto);
                }

                // Lier AppercuRapport au DTO du bâtiment
                dto.setAppercuRapport(appercuDto);
            }

            return dto;
        }).collect(Collectors.toList());
    }




    public Batiment findBatimentWithReport(Long idBat) {
        // Récupération du bâtiment à partir de l'id
        Batiment batiment = batimentRepository.findById(idBat)
                .orElseThrow(() -> new EntityNotFoundException("Bâtiment non trouvé"));
        // Récupération de l'ID du rapport associé
        Long idApp = batiment.getAppercuRapport().getIdApp();
        if (idApp != null) {
            // Récupération du rapport s'il existe
            AppercuRapport rapport = appercuRapportRepository.findById(idApp).orElse(null);
            batiment.setAppercuRapport(rapport); // Assurez-vous que Batiment a une relation avec AppercuRapport
        }
        return batiment;
    }

    public String findFileExpertisePathByBatimentId(Long idBat) {
        // Récupération du bâtiment
        Batiment batiment = batimentRepository.findById(idBat)
                .orElseThrow(() -> new EntityNotFoundException("Bâtiment non trouvé"));
        // Récupération de l'AppercuRapport associé
        AppercuRapport appercuRapport = batiment.getAppercuRapport();
        if (appercuRapport == null) {
            throw new EntityNotFoundException("Aucun rapport d'aperçu associé à ce bâtiment");
        }
        // Récupération de la première expertise technique associée
        ExpertiseTechnique expertise = expertiseTechniqueRepository
                .findByAppercuRapport_IdApp(appercuRapport.getIdApp())
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Aucune expertise technique associée trouvée pour cet aperçu rapport"));
        // Retourner le chemin du fichier
        return expertise.getFileExpertise();
    }



    // Méthodes pour obtenir les valeurs par défaut
    public Annexe getDefaultAnnexe() {
        return defaultAnnexe;
    }
    public StatutActuel getDefaultStatutActuel() {
        return defaultStatutActuel;
    }
    public AppercuRapport getDefaultAppercuRapport() {
        return defaultAppercuRapport;
    }

    // Chercher un bâtiment par ID
    public Batiment findById(Long idBat) {
        return batimentRepository.findById(idBat)
                .orElseThrow(() -> new EntityNotFoundException("Batiment avec l'ID " + idBat + " non trouvé"));
    }

    // Supprimer un bâtiment
    @Transactional
    public void deleteBatiment(long idBat) {
        Batiment batiment = batimentRepository.findById(idBat)
                .orElseThrow(() -> new EntityNotFoundException("Bâtiment non trouvé avec id : " + idBat));

        if (batiment.getStatutActuel() != null) {
            statutActuelRepository.delete(batiment.getStatutActuel());
        }

        batimentRepository.deleteById(idBat);
    }


    // Mettre à jour un bâtiment
    @Transactional
    public Batiment updateBatiment(Long idBat, Batiment updatedBatiment, MultipartFile file) throws IOException {
        // Rechercher le bâtiment existant
        Batiment existingBatiment = batimentRepository.findById(idBat)
                .orElseThrow(() -> new EntityNotFoundException("Batiment avec l'ID " + idBat + " non trouvé"));

        // Mettre à jour les champs du bâtiment
        existingBatiment.setAdresse(updatedBatiment.getAdresse());
        existingBatiment.setSurface(updatedBatiment.getSurface());
        existingBatiment.setNBetage(updatedBatiment.getNBetage());
        existingBatiment.setNomPrenomProprietaire(updatedBatiment.getNomPrenomProprietaire());
        existingBatiment.setNbFamilleResident(updatedBatiment.getNbFamilleResident());
        existingBatiment.setNomPrenomResident(updatedBatiment.getNomPrenomResident());
        existingBatiment.setSituationChefFamille(updatedBatiment.getSituationChefFamille());
        existingBatiment.setNbMagasin(updatedBatiment.getNbMagasin());
        existingBatiment.setNomPrenomExploitantMagasin(updatedBatiment.getNomPrenomExploitantMagasin());
        existingBatiment.setSituationExploitant(updatedBatiment.getSituationExploitant());

        // Mise à jour ou ajout du fichier
        if (file != null && !file.isEmpty()) {
            String filePath = "C:/uploads/batiment/" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            existingBatiment.setFiletelechargerImage(filePath); // Mise à jour du chemin du fichier
        }

        // Enregistrer les modifications
        return batimentRepository.save(existingBatiment);
    }





    // Méthode auxiliaire pour définir les valeurs par défaut si nécessaire
    private void setDefaultEntities(Batiment batiment) {
        if (batiment.getAnnexe() == null && defaultAnnexe != null) {
            batiment.setAnnexe(defaultAnnexe);
        }
        if (batiment.getStatutActuel() == null && defaultStatutActuel != null) {
            batiment.setStatutActuel(defaultStatutActuel);
        }
        if (batiment.getAppercuRapport() == null && defaultAppercuRapport != null) {
            batiment.setAppercuRapport(defaultAppercuRapport);
        }
    }
}
