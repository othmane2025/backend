package com.example.batimentDU.service;

import com.example.batimentDU.dto.ExpertiseDTO;
import com.example.batimentDU.dto.ExpertiseTechniqueDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.AppercuRapport;
import com.example.batimentDU.model.Batiment;
import com.example.batimentDU.model.DecisionCollective;
import com.example.batimentDU.model.ExpertiseTechnique;
import com.example.batimentDU.repository.AppercuRapportRepository;
import com.example.batimentDU.repository.BatimentRepository;
import com.example.batimentDU.repository.DecisionCollectiveRepository;
import com.example.batimentDU.repository.ExpertiseTechniqueRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpertiseTechniqueService {

    @Autowired
    private  ExpertiseTechniqueRepository expertiseTechniqueRepository;
    @Autowired
    private AppercuRapportRepository appercuRapportRepository;
    @Autowired
    private  DecisionCollectiveRepository decisionCollectiveRepository;
    @Autowired
    private  BatimentRepository batimentRepository;
    private static final String UPLOAD_DIR = "C:/uploads/expertise/";  // Constante pour le chemin de fichier



    // Récupérer toutes les Expertises Techniques
    public List<ExpertiseTechnique> findAll() {
        return expertiseTechniqueRepository.findAll();
    }

    //Méthode pour obtenir les expertises par nom d'arrondissement
    @Transactional
    public List<ExpertiseTechniqueDTO> getExpertiseTechniquesByNomArrondissement(NomArrondissement nomArrondissement) {
        if (nomArrondissement == null) {
            throw new IllegalArgumentException("NomArrondissement ne peut pas être null.");
        }
        // Étape 1 : Récupérer les bâtiments liés à l'arrondissement
        List<Batiment> batiments = batimentRepository.findByArrondissement(nomArrondissement);
        if (batiments.isEmpty()) {
            System.out.println("Aucun bâtiment trouvé pour l'arrondissement : " + nomArrondissement);
        }
        // Étape 2 : Mapper les bâtiments pour récupérer leurs rapports d'aperçu et les expertises associées
        return batiments.stream()
                .map(Batiment::getAppercuRapport)                // Obtenir le rapport d’aperçu pour chaque bâtiment
                .filter(Objects::nonNull)                        // Exclure les rapports d’aperçu nulls
                .map(AppercuRapport::getExpertise)               // Obtenir les expertises techniques associées
                .filter(Objects::nonNull)                        // Exclure les expertises nulles
                .map(expertise -> new ExpertiseTechniqueDTO(     // Mapper les données dans un DTO
                        expertise.getIdExp(),
                        expertise.getBureauEtude(),
                        expertise.getDate(),
                        expertise.getRecommandationExpertise(),
                        expertise.getFileExpertise()
                ))
                .collect(Collectors.toList());
    }




    //Méthode pour obtenir les expertises par nom d'annexe
    public List<ExpertiseTechniqueDTO> getExpertiseTechniquesByNomAnnexe(NomAnnexe nomAnnexe) {
        List<Batiment> batiments = batimentRepository.findByNomAnnexe(nomAnnexe);

        return batiments.stream()
                .map(Batiment::getAppercuRapport)                // Get the AppercuRapport for each Batiment
                .filter(Objects::nonNull)                        // Filter out null AppercuRapport
                .map(AppercuRapport::getExpertise)               // Get the ExpertiseTechnique (OneToOne)
                .filter(Objects::nonNull)                        // Filter out null ExpertiseTechnique
                .map(expertise -> new ExpertiseTechniqueDTO(
                        expertise.getIdExp(),
                        expertise.getBureauEtude(),
                        expertise.getDate(),
                        expertise.getRecommandationExpertise(),
                        expertise.getFileExpertise()
                ))
                .collect(Collectors.toList());
    }



    // Ajouter une Expertise Technique
    @Transactional
    public ExpertiseTechnique saveExpertiseTechniqueWithBatiment(
            String bureauEtude,
            LocalDate date,
            String recommandationExpertise,
            MultipartFile file,
            Long idBat) throws IOException {
        AppercuRapport appercuRapport = appercuRapportRepository.findByBatimentId(idBat)
                .orElseThrow(() -> new IllegalArgumentException("Aucun aperçu trouvé pour le bâtiment avec l'ID " + idBat));

        if (expertiseTechniqueRepository.existsByAppercuRapport_IdApp(appercuRapport.getIdApp())) {
            throw new IllegalArgumentException("Une expertise existe déjà pour cet aperçu.");
        }

        ExpertiseTechnique expertiseTechnique = new ExpertiseTechnique();
        if (bureauEtude != null) expertiseTechnique.setBureauEtude(bureauEtude);
        if (date != null) expertiseTechnique.setDate(date);
        if (recommandationExpertise != null) expertiseTechnique.setRecommandationExpertise(recommandationExpertise);

        if (file != null) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String filePath = UPLOAD_DIR + file.getOriginalFilename();
            file.transferTo(Paths.get(filePath).toFile());
            expertiseTechnique.setFileExpertise(filePath);
        }

        expertiseTechnique.setAppercuRapport(appercuRapport);
        return expertiseTechniqueRepository.save(expertiseTechnique);
    }

    // Rechercher une Expertise Technique par RecommandationExpertise
    public List<ExpertiseDTO> chercherParRecommandation(String recommandation) {
        String formattedRecommandation = recommandation.trim().toLowerCase();

        // Récupérer toutes les expertises avec la recommandation spécifiée
        List<ExpertiseTechnique> expertises = expertiseTechniqueRepository.findByRecommandation(formattedRecommandation);

        // Mapper les résultats dans le DTO
        return expertises.stream()
                .map(expertise -> {
                    ExpertiseDTO dto = new ExpertiseDTO();

                    // Récupérer le bâtiment, l'aperçu et l'expertise
                    dto.setExpertise(expertise); // Expertises
                    dto.setAppercuRapport(expertise.getAppercuRapport());  // Récupère l'aperçu lié
                    dto.setBatiment(expertise.getAppercuRapport() != null ? expertise.getAppercuRapport().getBatiment() : null); // Récupère le bâtiment lié

                    // Récupérer les autres entités associées
                    if (expertise.getAppercuRapport() != null) {
                        // Décision collective liée à l'aperçu
                        dto.setDecisionCollective(expertise.getAppercuRapport().getDecisionCollective());
                        // Communiquer décision liée à la décision collective
                        if (dto.getDecisionCollective() != null) {
                            dto.setCommuniquerDecision(dto.getDecisionCollective().getCommuniquerDecision());
                        }
                        // Relogement lié à la communiquer décision
                        if (dto.getCommuniquerDecision() != null) {
                            dto.setRelogement(dto.getCommuniquerDecision().getRelogement());
                        }
                        // EtatAvancement lié à la communiquer décision
                        if (dto.getCommuniquerDecision() != null) {
                            dto.setEtatAvancement(dto.getCommuniquerDecision().getEtatAvancement());
                        }
                        // RaisonInobservation lié à l'état d'avancement
                        if (dto.getEtatAvancement() != null) {
                            dto.setRaisonInobservation(dto.getEtatAvancement().getRaisonInobservation());
                        }
                        // InformerAR lié à l'état d'avancement
                        if (dto.getEtatAvancement() != null) {
                            dto.setInformerAR(dto.getEtatAvancement().getInformerAR());
                        }
                    }
                    // StatutActuel lié au bâtiment
                    if (dto.getBatiment() != null) {
                        dto.setStatutActuel(dto.getBatiment().getStatutActuel());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }


    // Rechercher une Expertise Technique par ID
    public ExpertiseTechnique findById(Long idExp) {
        return expertiseTechniqueRepository.findById(idExp)
                .orElseThrow(() -> new EntityNotFoundException("ExpertiseTechnique avec l'ID " + idExp + " non trouvée"));
    }

    // Rechercher
    public List<ExpertiseTechnique> getByRecommandation(String recommandationExpertise) {
        return expertiseTechniqueRepository.findByRecommandationExpertise(recommandationExpertise);
    }

    // Supprimer une Expertise Technique
    @Transactional
    public void deleteExpertiseTechnique(long idExp) {
        expertiseTechniqueRepository.deleteById(idExp);
    }

    // Mettre à jour une Expertise Technique
    @Transactional
    public ExpertiseTechnique updateExpertiseWithFile(
            Long idExp, ExpertiseTechnique updatedExpertise, MultipartFile file) throws IOException {

        // Rechercher l'ExpertiseTechnique directement par idExp
        ExpertiseTechnique existingExpertise = expertiseTechniqueRepository.findById(idExp)
                .orElseThrow(() -> new EntityNotFoundException("ExpertiseTechnique avec ID " + idExp + " non trouvée"));

        // Mettre à jour les champs
        existingExpertise.setBureauEtude(updatedExpertise.getBureauEtude());
        existingExpertise.setDate(updatedExpertise.getDate());
        existingExpertise.setRecommandationExpertise(updatedExpertise.getRecommandationExpertise());

        // Gestion du fichier (si fourni)
        if (file != null && !file.isEmpty()) {
            // Définir le répertoire de téléchargement
            String uploadDir = "C:/uploads/expertise/";
            Files.createDirectories(Paths.get(uploadDir));

            // Définir le chemin complet du fichier
            String filePath = uploadDir + file.getOriginalFilename();

            // Sauvegarder le fichier sur le serveur
            file.transferTo(Paths.get(filePath).toFile());

            // Mettre à jour le chemin du fichier dans l'entité
            existingExpertise.setFileExpertise(filePath);
        }

        // Sauvegarder et retourner l'ExpertiseTechnique mise à jour
        return expertiseTechniqueRepository.save(existingExpertise);
    }

}
