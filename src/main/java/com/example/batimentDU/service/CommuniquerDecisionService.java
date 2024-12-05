package com.example.batimentDU.service;

import com.example.batimentDU.dto.CommuniquerDecisionDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.*;
import com.example.batimentDU.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommuniquerDecisionService {

    @Autowired
    private  CommuniquerDecisionRepository communiquerDecisionRepository;
    @Autowired
    private  RelogementRepository relogementRepository;
    @Autowired
    private  EtatAvancementRepository etatAvancementRepository;
    @Autowired
    private DecisionCollectiveRepository decisionCollectiveRepository;
    @Autowired
    private  BatimentRepository batimentRepository;

    @Autowired
    private AppercuRapportRepository appercuRapportRepository;



    // Afficher toutes les CommuniquerDecision
    public List<CommuniquerDecision> findAll() {
        return communiquerDecisionRepository.findAll();
    }

    // Méthode pour obtenir les CommuniquerDecision par nom d'arrondissement
    public List<CommuniquerDecisionDTO> getCommuniquerDecisionsByNomArrondissement(NomArrondissement nomArrondissement) {
        // Obtenir la liste des bâtiments en fonction du nom de l'arrondissement
        List<Batiment> batiments = batimentRepository.findByArrondissement(nomArrondissement);
        // Transformer les bâtiments en DTO de CommuniquerDecision
        return batiments.stream()
                .map(Batiment::getAppercuRapport)                    // Obtenir les AppercuRapport de chaque bâtiment
                .filter(Objects::nonNull)                            // Filtrer les AppercuRapport nuls
                .map(AppercuRapport::getDecisionCollective)          // Obtenir DecisionCollective depuis AppercuRapport
                .filter(Objects::nonNull)                            // Filtrer les DecisionCollective nulles
                .map(DecisionCollective::getCommuniquerDecision)     // Obtenir CommuniquerDecision depuis DecisionCollective
                .filter(Objects::nonNull)                            // Filtrer les CommuniquerDecision nulles
                .map(communiquer -> new CommuniquerDecisionDTO(      // Mapper les données dans un DTO
                        communiquer.getIdCom(),
                        communiquer.getNbDec(),
                        communiquer.getDateDec()
                        // Ajouter d'autres champs si nécessaire
                ))
                .collect(Collectors.toList());
    }
    // Méthode pour obtenir les CommuniquerDecision par nom d'annexe
    public List<CommuniquerDecisionDTO> getCommuniquerDecisionsByNomAnnexe(NomAnnexe nomAnnexe) {
        // Obtenir la liste des bâtiments en fonction du nom de l'annexe
        List<Batiment> batiments = batimentRepository.findByNomAnnexe(nomAnnexe);
        return batiments.stream()
                .map(Batiment::getAppercuRapport)                    // Obtenir les AppercuRapport de chaque bâtiment
                .filter(Objects::nonNull)                            // Filtrer les AppercuRapport nuls
                .map(AppercuRapport::getDecisionCollective)          // Obtenir directement DecisionCollective depuis AppercuRapport
                .filter(Objects::nonNull)                            // Filtrer les DecisionCollective nulles
                .map(DecisionCollective::getCommuniquerDecision)     // Obtenir CommuniquerDecision depuis DecisionCollective
                .filter(Objects::nonNull)                            // Filtrer les CommuniquerDecision nulles
                .map(communiquer -> new CommuniquerDecisionDTO(
                        communiquer.getIdCom(),                      // Récupérer l'ID de CommuniquerDecision
                        communiquer.getNbDec(),                      // Récupérer les détails de CommuniquerDecision
                        communiquer.getDateDec()                     // Inclure d'autres champs nécessaires
                ))
                .collect(Collectors.toList());
    }


    // Ajouter une CommuniquerDecision
    @Transactional
    public CommuniquerDecision addCommuniquerDecision(
            Long idBat,
            Integer nbDec, // Integer permet d'accepter null
            LocalDate dateDec) {

        // Récupérer l'AppercuRapport associé au Bâtiment
        AppercuRapport appercuRapport = appercuRapportRepository.findByBatimentId(idBat)
                .orElseThrow(() -> new IllegalArgumentException("Aperçu introuvable pour le bâtiment avec l'ID " + idBat));
        // Vérifier si une DecisionCollective existe pour cet AppercuRapport
        DecisionCollective decisionCollective = appercuRapport.getDecisionCollective();
        if (decisionCollective == null) {
            throw new IllegalArgumentException("Aucune décision collective associée à cet aperçu.");
        }
        // Vérifier si une CommuniquerDecision existe déjà pour cette DecisionCollective
        if (decisionCollective.getCommuniquerDecision() != null) {
            throw new IllegalArgumentException("Une communication existe déjà pour cette décision collective.");
        }
        // Créer une nouvelle instance de CommuniquerDecision
        CommuniquerDecision communiquerDecision = new CommuniquerDecision();
        if (nbDec != null) {
            communiquerDecision.setNbDec(nbDec);
        }
        if (dateDec != null) {
            communiquerDecision.setDateDec(dateDec);
        }
        communiquerDecision.setDecisionCollective(decisionCollective); // Associer à la décision collective
        // Sauvegarder la CommuniquerDecision dans la base de données
        CommuniquerDecision savedCommunication = communiquerDecisionRepository.save(communiquerDecision);
        // Mettre à jour la DecisionCollective pour inclure la communication
        decisionCollective.setCommuniquerDecision(savedCommunication);
        return savedCommunication;
    }




    // Rechercher une CommuniquerDecision par ID
    public CommuniquerDecision findCommuniquerDecisionById(Long idCom) {
        return communiquerDecisionRepository.findById(idCom)
                .orElseThrow(() -> new EntityNotFoundException("CommuniquerDecision avec l'id " + idCom + " n'existe pas."));
    }

    // Supprimer une CommuniquerDecision
    @Transactional
    public void supprimerCommuniquerDecision(Long idCom) {
        CommuniquerDecision communiquerDecision = communiquerDecisionRepository.findById(idCom)
                .orElseThrow(() -> new EntityNotFoundException("CommuniquerDecision avec l'id " + idCom + " n'existe pas."));

        // Détacher ou supprimer la relation avec DecisionCollective
        if (communiquerDecision.getDecisionCollective() != null) {
            DecisionCollective decision = communiquerDecision.getDecisionCollective();
            decision.setCommuniquerDecision(null);  // Détacher la relation
            decisionCollectiveRepository.save(decision); // Enregistrer le changement
        }

        // Supprimer CommuniquerDecision
        communiquerDecisionRepository.delete(communiquerDecision);
    }

    // Modifier une CommuniquerDecision existante
    @Transactional
    public CommuniquerDecision modifierCommuniquerDecision(Long idCom, CommuniquerDecision nouvelleDecision) {
        CommuniquerDecision decisionExistante = communiquerDecisionRepository.findById(idCom)
                .orElseThrow(() -> new EntityNotFoundException("CommuniquerDecision avec l'id " + idCom + " n'existe pas."));

        // Mettre à jour les champs
        decisionExistante.setNbDec(nouvelleDecision.getNbDec());
        decisionExistante.setDateDec(nouvelleDecision.getDateDec());
        // Sauvegarder et retourner l'entité mise à jour
        return communiquerDecisionRepository.save(decisionExistante);
    }
}
