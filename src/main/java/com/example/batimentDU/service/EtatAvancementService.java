package com.example.batimentDU.service;

import com.example.batimentDU.dto.EtatAvancementDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.*;
import com.example.batimentDU.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtatAvancementService {
    @Autowired
    private  EtatAvancementRepository etatAvancementRepository;
    @Autowired
    private  InformerARRepository informerARRepository;
    @Autowired
    private  CommuniquerDecisionRepository communiquerDecisionRepository;
    @Autowired
    private  RaisonInobservationRepository raisonInobservationRepository;
    @Autowired
    private  BatimentRepository batimentRepository;

    @Autowired
    private  AppercuRapportRepository appercuRapportRepository;


    // Récupérer tous les Etats d'Avancement
    public List<EtatAvancement> findAll() {
        return etatAvancementRepository.findAll();
    }

    //
    public List<EtatAvancementDTO> getEtatAvancementByNomArrondissement(NomArrondissement nomArrondissement) {
        // Récupérer la liste des bâtiments en fonction du nom de l'arrondissement
        List<Batiment> batiments = batimentRepository.findByArrondissement(nomArrondissement);
        // Transformer les bâtiments en DTO d'État d'Avancement
        return batiments.stream()
                .map(Batiment::getAppercuRapport)                 // Obtenir les AppercuRapport de chaque bâtiment
                .filter(Objects::nonNull)                         // Filtrer les AppercuRapport nuls
                .map(AppercuRapport::getDecisionCollective)       // Obtenir les DecisionCollective depuis AppercuRapport
                .filter(Objects::nonNull)                         // Filtrer les DecisionCollective nulles
                .map(DecisionCollective::getCommuniquerDecision)  // Obtenir CommuniquerDecision depuis DecisionCollective
                .filter(Objects::nonNull)                         // Filtrer les CommuniquerDecision nulles
                .map(CommuniquerDecision::getEtatAvancement)      // Obtenir EtatAvancement depuis CommuniquerDecision
                .filter(Objects::nonNull)                         // Filtrer les EtatAvancement nulls
                .map(etatAvancement -> new EtatAvancementDTO(     // Mapper EtatAvancement en EtatAvancementDTO
                        etatAvancement.getIdEtat(),
                        etatAvancement.getNbCorrespondanceAL(),
                        etatAvancement.getDateCorrespondanceAL(),
                        etatAvancement.getNbReponseAL(),
                        etatAvancement.getDateReponseAL()
                ))
                .collect(Collectors.toList());
    }


    //
    public List<EtatAvancementDTO> getEtatAvancementByNomAnnexe(NomAnnexe nomAnnexe) {
        // Obtenir la liste des bâtiments en fonction du nom de l'annexe
        List<Batiment> batiments = batimentRepository.findByNomAnnexe(nomAnnexe);
        return batiments.stream()
                .map(Batiment::getAppercuRapport)                 // Obtenir les AppercuRapport de chaque bâtiment
                .filter(Objects::nonNull)                         // Filtrer les AppercuRapport nuls
                .map(AppercuRapport::getDecisionCollective)       // Obtenir les DecisionCollective depuis AppercuRapport
                .filter(Objects::nonNull)                         // Filtrer les DecisionCollective nulles
                .map(DecisionCollective::getCommuniquerDecision)  // Obtenir CommuniquerDecision depuis DecisionCollective
                .filter(Objects::nonNull)                         // Filtrer les CommuniquerDecision nulles
                .map(CommuniquerDecision::getEtatAvancement)      // Obtenir EtatAvancement depuis CommuniquerDecision
                .filter(Objects::nonNull)                         // Filtrer les EtatAvancement nulls
                .map(etatAvancement -> new EtatAvancementDTO(
                        etatAvancement.getIdEtat(),               // Récupérer l'ID d'EtatAvancement
                        etatAvancement.getNbCorrespondanceAL(),   // Récupérer le nombre de correspondances AL
                        etatAvancement.getDateCorrespondanceAL(), // Récupérer la date de correspondance AL
                        etatAvancement.getNbReponseAL(),          // Récupérer le nombre de réponses AL
                        etatAvancement.getDateReponseAL()         // Récupérer la date de réponse AL
                ))
                .collect(Collectors.toList());
    }


    // Ajouter un nouvel Etat d'Avancement
    @Transactional
    public EtatAvancement addEtatAvancement(Long idBat, EtatAvancement etatAvancement) {
        // Récupérer l'AppercuRapport associé au bâtiment
        AppercuRapport appercuRapport = appercuRapportRepository.findByBatimentId(idBat)
                .orElseThrow(() -> new IllegalArgumentException("Aperçu introuvable pour le bâtiment avec l'ID " + idBat));
        // Récupérer la DecisionCollective associée à l'AppercuRapport
        DecisionCollective decisionCollective = appercuRapport.getDecisionCollective();
        if (decisionCollective == null) {
            throw new IllegalArgumentException("Aucune décision collective trouvée pour cet aperçu.");
        }
        // Récupérer la CommuniquerDecision associée à la DecisionCollective
        CommuniquerDecision communiquerDecision = decisionCollective.getCommuniquerDecision();
        if (communiquerDecision == null) {
            throw new IllegalArgumentException("Aucune communication trouvée pour cette décision collective.");
        }
        // Vérifier si un EtatAvancement existe déjà pour cette CommuniquerDecision
        if (etatAvancementRepository.existsByCommuniquerDecision_IdCom(communiquerDecision.getIdCom())) {
            throw new IllegalArgumentException("Un État d'Avancement existe déjà pour cette communication.");
        }
        // Associer l'EtatAvancement à la CommuniquerDecision
        etatAvancement.setCommuniquerDecision(communiquerDecision);
        // Sauvegarder l'EtatAvancement
        EtatAvancement savedEtatAvancement = etatAvancementRepository.save(etatAvancement);
        // Associer et sauvegarder l'objet CommuniquerDecision avec l'EtatAvancement
        communiquerDecision.setEtatAvancement(savedEtatAvancement);
        communiquerDecisionRepository.save(communiquerDecision);

        return savedEtatAvancement;
    }




    // Rechercher un Etat d'Avancement par ID
    public EtatAvancement findById(Long idEtat) {
        return etatAvancementRepository.findById(idEtat)
                .orElseThrow(() -> new EntityNotFoundException("EtatAvancement avec l'ID " + idEtat + " non trouvé"));
    }

    // Supprimer un Etat d'Avancement
    @Transactional
    public void deleteEtatAvancement(long idEtat) {
        // Détacher les décisions qui pointent vers cet Etat d'Avancement
        List<CommuniquerDecision> decisions = communiquerDecisionRepository.findByEtatAvancement_IdEtat(idEtat);
        for (CommuniquerDecision decision : decisions) {
            decision.setEtatAvancement(null);
            communiquerDecisionRepository.save(decision); // Détacher la relation
        }

        // Supprimer les raisons d'inobservation associées
        List<RaisonInobservation> raisons = raisonInobservationRepository.findByEtatAvancement_IdEtat(idEtat);
        for (RaisonInobservation raison : raisons) {
            raison.setEtatAvancement(null);
            raisonInobservationRepository.save(raison); // Détacher la relation
        }

        // Supprimer l'Etat d'Avancement
        etatAvancementRepository.deleteById(idEtat);
    }

    // Mettre à jour un Etat d'Avancement
    @Transactional
    public EtatAvancement updateEtatAvancement(Long id, EtatAvancement updatedEtatAvancement) {
        EtatAvancement etatAvancement = etatAvancementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EtatAvancement avec l'ID " + id + " non trouvé"));
        // Mettre à jour les champs de EtatAvancement
        etatAvancement.setNbCorrespondanceAL(updatedEtatAvancement.getNbCorrespondanceAL());
        etatAvancement.setDateCorrespondanceAL(updatedEtatAvancement.getDateCorrespondanceAL());
        etatAvancement.setNbReponseAL(updatedEtatAvancement.getNbReponseAL());
        etatAvancement.setDateReponseAL(updatedEtatAvancement.getDateReponseAL());
        etatAvancement.setInformerAR(updatedEtatAvancement.getInformerAR());
        // Sauvegarder et retourner l'Etat d'Avancement mis à jour
        return etatAvancementRepository.save(etatAvancement);
    }
}
