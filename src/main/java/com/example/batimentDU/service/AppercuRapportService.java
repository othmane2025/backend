package com.example.batimentDU.service;



import com.example.batimentDU.dto.*;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.*;

import com.example.batimentDU.repository.AppercuRapportRepository;
import jakarta.persistence.EntityNotFoundException;

import com.example.batimentDU.repository.AppercuRapportRepository;
import com.example.batimentDU.repository.BatimentRepository;
import com.example.batimentDU.repository.ExpertiseTechniqueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppercuRapportService {

    @Autowired
    AppercuRapportRepository appercuRapportRepository;


    @Autowired
    ExpertiseTechniqueRepository expertiseTechniqueRepository;

    @Autowired
    BatimentRepository batimentRepository;


    // afficher
    public List<AppercuRapport> findAll(){
        return appercuRapportRepository.findAll();
    }

    //Méthode pour obtenir les rapports par nom d'arrondissement
    @Transactional
    public List<AppercuRapportDTO> getAppercuRapportsByArrondissement(NomArrondissement nomArrondissement) {
        // Récupérer les bâtiments liés à l'arrondissement
        List<Batiment> batiments = batimentRepository.findByArrondissement(nomArrondissement);

        // Mapper les rapports d'aperçu
        return batiments.stream()
                .map(Batiment::getAppercuRapport) // Obtenir les rapports d'aperçu liés aux bâtiments
                .filter(Objects::nonNull) // Ignorer les rapports nuls
                .map(appercuRapport -> {
                    AppercuRapportDTO appercuRapportDto = new AppercuRapportDTO(
                            appercuRapport.getIdApp(),
                            appercuRapport.getNbRecommandation(),
                            appercuRapport.getDateInspection(),
                            appercuRapport.getRecommandation(),
                            appercuRapport.getNbNotificationRapportInspection(),
                            appercuRapport.getDateNotification(),
                            appercuRapport.getFilerapportInspection()
                    );

                    // Ajouter l'expertise associée (si présente)
                    if (appercuRapport.getExpertise() != null) {
                        ExpertiseTechnique expertise = appercuRapport.getExpertise();
                        ExpertiseTechniqueDTO expertiseDto = new ExpertiseTechniqueDTO();
                        expertiseDto.setIdExp(expertise.getIdExp());
                        expertiseDto.setBureauEtude(expertise.getBureauEtude());
                        expertiseDto.setDate(expertise.getDate());
                        expertiseDto.setRecommandationExpertise(expertise.getRecommandationExpertise());
                        expertiseDto.setFileExpertise(expertise.getFileExpertise());
                        appercuRapportDto.setExpertise(expertiseDto);
                    }

                    // Ajouter la décision collective associée (si présente)
                    if (appercuRapport.getDecisionCollective() != null) {
                        DecisionCollective decision = appercuRapport.getDecisionCollective();
                        DecisionCollectiveDTO decisionDto = new DecisionCollectiveDTO();
                        decisionDto.setIdDec(decision.getIdDec());
                        decisionDto.setNbDec(decision.getNbDec());
                        decisionDto.setDateDec(decision.getDateDec());
                        decisionDto.setRecommandationDecision(decision.getRecommandationDecision());
                        decisionDto.setFileRapport(decision.getFileRapport());
                        appercuRapportDto.setDecision(decisionDto);
                    }

                    return appercuRapportDto;
                })
                .collect(Collectors.toList());
    }




    //Méthode pour obtenir les rapports par nom d'annexe
    @Transactional
    public List<AppercuRapportDTO> getAppercuRapportsByNomAnnexe(NomAnnexe nomAnnexe) {
        List<Batiment> batiments = batimentRepository.findByNomAnnexe(nomAnnexe);

        return batiments.stream()
                .map(Batiment::getAppercuRapport) // Obtenir les AppercuRapport
                .filter(Objects::nonNull) // Filtrer les rapports nuls
                .map(appercuRapport -> {
                    // Initialiser le DTO pour AppercuRapport
                    AppercuRapportDTO appercuRapportDto = new AppercuRapportDTO(
                            appercuRapport.getIdApp(),
                            appercuRapport.getNbRecommandation(),
                            appercuRapport.getDateInspection(),
                            appercuRapport.getRecommandation(),
                            appercuRapport.getNbNotificationRapportInspection(),
                            appercuRapport.getDateNotification(),
                            appercuRapport.getFilerapportInspection()
                    );

                    // Inclure l'expertise associée, si elle existe
                    if (appercuRapport.getExpertise() != null) {
                        ExpertiseTechnique expertise = appercuRapport.getExpertise();
                        ExpertiseTechniqueDTO expertiseDto = new ExpertiseTechniqueDTO();
                        expertiseDto.setIdExp(expertise.getIdExp());
                        expertiseDto.setBureauEtude(expertise.getBureauEtude());
                        expertiseDto.setDate(expertise.getDate());
                        expertiseDto.setRecommandationExpertise(expertise.getRecommandationExpertise());
                        expertiseDto.setFileExpertise(expertise.getFileExpertise());
                        // Associer l'expertise au DTO d'AppercuRapport
                        appercuRapportDto.setExpertise(expertiseDto);
                    }

                    // Inclure la décision collective associée, si elle existe
                    if (appercuRapport.getDecisionCollective() != null) {
                        DecisionCollective decision = appercuRapport.getDecisionCollective();
                        DecisionCollectiveDTO decisionDto = new DecisionCollectiveDTO();
                        decisionDto.setIdDec(decision.getIdDec());
                        decisionDto.setNbDec(decision.getNbDec());
                        decisionDto.setDateDec(decision.getDateDec());
                        decisionDto.setRecommandationDecision(decision.getRecommandationDecision());
                        decisionDto.setFileRapport(decision.getFileRapport());
                        // Inclure CommuniquerDecision si présent
                        if (decision.getCommuniquerDecision() != null) {
                            CommuniquerDecision communiquer = decision.getCommuniquerDecision();
                            CommuniquerDecisionDTO communiquerDto = new CommuniquerDecisionDTO();
                            communiquerDto.setIdCom(communiquer.getIdCom());
                            communiquerDto.setNbDec(communiquer.getNbDec());
                            communiquerDto.setDateDec(communiquer.getDateDec());
                            // Inclure Relogement si présent
                            if (communiquer.getRelogement() != null) {
                                Relogement relogement = communiquer.getRelogement();
                                RelogementDTO relogementDto = new RelogementDTO();
                                relogementDto.setIdRel(relogement.getIdRel());
                                relogementDto.setNbFamilleExplusees(relogement.getNbFamilleExplusees());
                                relogementDto.setNbFamillePrestations(relogement.getNbFamillePrestations());
                                relogementDto.setNomPrenomPrestations(relogement.getNomPrenomPrestations());
                                relogementDto.setNbFamilleBeneficiaire(relogement.getNbFamilleBeneficiaire());
                                relogementDto.setNomPrenomBeneficiaire(relogement.getNomPrenomBeneficiaire());
                                relogementDto.setDateTirageSort(relogement.getDateTirageSort());
                                relogementDto.setLieuPrestation(relogement.getLieuPrestation());
                                relogementDto.setRelocalisation(relogement.getRelocalisation());
                                relogementDto.setSocieteSurveillance(relogement.getSocieteSurveillance());
                                relogementDto.setFileRapportTirage(relogement.getFileRapportTirage());
                                // Associer le Relogement au DTO de CommuniquerDecision
                                communiquerDto.setRelogement(relogementDto);
                            }
                            // Inclure EtatAvancement
                            if (communiquer.getEtatAvancement() != null) {
                                EtatAvancement etat = communiquer.getEtatAvancement();
                                EtatAvancementDTO etatDto = new EtatAvancementDTO(
                                        etat.getIdEtat(),
                                        etat.getNbCorrespondanceAL(),
                                        etat.getDateCorrespondanceAL(),
                                        etat.getNbReponseAL(),
                                        etat.getDateReponseAL()
                                );
                                // Inclure InformerAR si présent
                                if (etat.getInformerAR() != null) {
                                    InformerAR informerAR = etat.getInformerAR();
                                    InformerARDTO informerArDto = new InformerARDTO(
                                            informerAR.getIdAR(),
                                            informerAR.getNbAR(),
                                            informerAR.getDateAr()
                                    );
                                    etatDto.setInformerAR(informerArDto);
                                }
                                if (etat.getRaisonInobservation() != null) {
                                    RaisonInobservation raisonInobservation = etat.getRaisonInobservation();
                                    RaisonInobservationDTO raisonInobservationDTO = new RaisonInobservationDTO(
                                            raisonInobservation.getIdRaison(),
                                            raisonInobservation.getNomRai()
                                    );
                                    etatDto.setRaisonInobservation(raisonInobservationDTO);
                                }
                                communiquerDto.setEtatAvancement(etatDto);
                            }
                            // Associer CommuniquerDecision au DTO de DecisionCollective
                            decisionDto.setCommuniquer(communiquerDto);
                        }
                        // Associer DecisionCollective au DTO d'AppercuRapport
                        appercuRapportDto.setDecision(decisionDto);
                    }
                    return appercuRapportDto;
                })
                .collect(Collectors.toList());
    }





    // Méthode pour récupérer l'adresse d'un bâtiment par ID
    public Optional<Map<String, String>> getAdresseById(Long idBat) {
        return batimentRepository.findById(idBat)
                .map(batiment -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("adresse", batiment.getAdresse()); // Encapsulez l'adresse dans une structure JSON
                    return response;
                });
    }
    // ajouter
    @Transactional
    public AppercuRapport addAppercuRapportToBatimentById(Long idBat, AppercuRapport appercuRapport, MultipartFile file) throws IOException {
        Optional<Batiment> batimentOpt = batimentRepository.findById(idBat);
        if (batimentOpt.isEmpty()) {
            throw new RuntimeException("Bâtiment avec l'ID " + idBat + " non trouvé");
        }
        Batiment batiment = batimentOpt.get();
        if (appercuRapportRepository.existsByBatiment_IdBat(idBat)) {
            throw new RuntimeException("Un AppercuRapport existe déjà pour ce bâtiment.");
        }
        if (file != null && !file.isEmpty()) {
            Files.createDirectories(Paths.get("C:/uploads/appercurapport/"));
            String filePath = "C:/uploads/appercurapport/" + file.getOriginalFilename();
            file.transferTo(Paths.get(filePath).toFile());
            appercuRapport.setFilerapportInspection(filePath);
        }
        appercuRapport.setBatiment(batiment);
        batiment.setAppercuRapport(appercuRapport);
        appercuRapportRepository.save(appercuRapport);
        batimentRepository.save(batiment);
        return appercuRapport;
    }


    @Transactional
    public AppercuRapport ajouteAppercuRapport(AppercuRapport appercuRapport) {
        return appercuRapportRepository.save(appercuRapport);
    }

    // chercher AppercuRapport
    public AppercuRapport findById(Long idApp) {
        return appercuRapportRepository.findById(idApp).orElseThrow(() -> new EntityNotFoundException("AppercuRapport with id" + idApp + " Not Found"));
    }

    // modifier AppercuRapport
    @Transactional
    public void deleteAppercuRapport(long idApp) {
        AppercuRapport appercuRapport = appercuRapportRepository.findById(idApp)
                .orElseThrow(() -> new EntityNotFoundException("AppercuRapport with id " + idApp + " not found"));

        // Handle the associated ExpertiseTechnique (OneToOne relationship)
        ExpertiseTechnique expertise = appercuRapport.getExpertise();
        if (expertise != null) {
            expertise.setAppercuRapport(null); // Detach the AppercuRapport reference
            expertiseTechniqueRepository.save(expertise); // Save the detached expertise
        }

        // Handle the associated Batiment (if any)
        if (appercuRapport.getBatiment() != null) {
            Batiment batiment = appercuRapport.getBatiment();
            batiment.setAppercuRapport(null); // Detach the AppercuRapport reference
            batimentRepository.save(batiment); // Save the detached batiment
        }

        // Now delete the AppercuRapport
        appercuRapportRepository.deleteById(idApp);
    }



    // modifier AppercuRapport
    @Transactional
    public AppercuRapport updateAppercuRapportWithFile(Long idApp, AppercuRapport updatedAppercuRapport, MultipartFile file) throws IOException {
        // Rechercher l'AppercuRapport existant
        AppercuRapport existingAppercuRapport = appercuRapportRepository.findById(idApp)
                .orElseThrow(() -> new EntityNotFoundException("AppercuRapport avec l'ID " + idApp + " non trouvé"));

        // Mise à jour des champs
        existingAppercuRapport.setNbRecommandation(updatedAppercuRapport.getNbRecommandation());
        existingAppercuRapport.setDateInspection(updatedAppercuRapport.getDateInspection());
        existingAppercuRapport.setRecommandation(updatedAppercuRapport.getRecommandation());
        existingAppercuRapport.setNbNotificationRapportInspection(updatedAppercuRapport.getNbNotificationRapportInspection());
        existingAppercuRapport.setDateNotification(updatedAppercuRapport.getDateNotification());
        // Gestion du fichier (si fourni)
        if (file != null && !file.isEmpty()) {
            // Définir le répertoire de téléchargement
            String uploadDir = "C:/uploads/appercurapport/";
            Files.createDirectories(Paths.get(uploadDir));
            // Définir le chemin complet du fichier
            String filePath = uploadDir + file.getOriginalFilename();
            // Sauvegarder le fichier sur le serveur
            file.transferTo(Paths.get(filePath).toFile());
            // Mettre à jour le chemin du fichier dans l'AppercuRapport
            existingAppercuRapport.setFilerapportInspection(filePath);
        }
        // Sauvegarder les modifications
        return appercuRapportRepository.save(existingAppercuRapport);
    }




}
