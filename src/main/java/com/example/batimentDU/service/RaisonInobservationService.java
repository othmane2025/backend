package com.example.batimentDU.service;

import com.example.batimentDU.dto.RaisonInobservationDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.model.*;
import com.example.batimentDU.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RaisonInobservationService {

    private static final String NOT_FOUND_MESSAGE = "RaisonInobservation avec l'id %d n'existe pas.";
    private static final String SAVE_ERROR_MESSAGE = "Erreur lors de la sauvegarde de RaisonInobservation";

    @Autowired
    private RaisonInobservationRepository raisonInobservationRepository;

    @Autowired
    private EtatAvancementRepository etatAvancementRepository;

    @Autowired
    private InformerARRepository informerARRepository;

    @Autowired
    private AppercuRapportRepository appercuRapportRepository;
    @Autowired
    private BatimentRepository batimentRepository;

    // Afficher toutes les raisons d'inobservation
    public List<RaisonInobservation> findAll() {
        return raisonInobservationRepository.findAll();
    }

    //
    public List<RaisonInobservationDTO> getRaisonsByNomAnnexe(NomAnnexe nomAnnexe) {
        // Obtenir la liste des bâtiments en fonction du nom de l'annexe
        List<Batiment> batiments = batimentRepository.findByNomAnnexe(nomAnnexe);
        return batiments.stream()
                .map(Batiment::getAppercuRapport)                           // Obtenir les AppercuRapport de chaque bâtiment
                .filter(Objects::nonNull)                                   // Filtrer les AppercuRapport nuls
                .map(AppercuRapport::getDecisionCollective)                 // Obtenir DecisionCollective directement depuis AppercuRapport
                .filter(Objects::nonNull)                                   // Filtrer les DecisionCollective nulles
                .map(DecisionCollective::getCommuniquerDecision)            // Obtenir CommuniquerDecision depuis DecisionCollective
                .filter(Objects::nonNull)                                   // Filtrer les CommuniquerDecision nulles
                .map(CommuniquerDecision::getEtatAvancement)                // Obtenir EtatAvancement depuis CommuniquerDecision
                .filter(Objects::nonNull)                                   // Filtrer les EtatAvancement nulls
                .map(EtatAvancement::getRaisonInobservation)                // Obtenir RaisonInobservation (OneToOne)
                .filter(Objects::nonNull)                                   // Filtrer les RaisonInobservation nulles
                .map(raison -> new RaisonInobservationDTO(
                        raison.getIdRaison(),                               // Inclure l'ID de la raison
                        raison.getNomRai()                                  // Inclure la description ou tout autre champ pertinent
                ))
                .collect(Collectors.toList());
    }




    // Ajouter une nouvelle raison d'inobservation
    @Transactional
    public RaisonInobservation addRaisonInobservation(Long idBat, RaisonInobservation raisonInobservation) {
        // Récupérer l'AppercuRapport associé au bâtiment
        AppercuRapport appercuRapport = appercuRapportRepository.findByBatimentId(idBat)
                .orElseThrow(() -> new IllegalArgumentException("Aperçu introuvable pour le bâtiment avec l'ID " + idBat));

        // Récupérer la DecisionCollective associée
        DecisionCollective decisionCollective = appercuRapport.getDecisionCollective();
        if (decisionCollective == null) {
            throw new IllegalArgumentException("Aucune décision collective trouvée pour cet aperçu.");
        }

        // Récupérer la CommuniquerDecision associée
        CommuniquerDecision communiquerDecision = decisionCollective.getCommuniquerDecision();
        if (communiquerDecision == null) {
            throw new IllegalArgumentException("Aucune communication trouvée pour cette décision collective.");
        }

        // Récupérer l'EtatAvancement associé
        EtatAvancement etatAvancement = communiquerDecision.getEtatAvancement();
        if (etatAvancement == null) {
            throw new IllegalArgumentException("Aucun État d'Avancement trouvé pour cette communication.");
        }
        // Vérifier si une RaisonInobservation existe déjà
        boolean raisonExists = raisonInobservationRepository.existsByEtatAvancement_IdEtat(etatAvancement.getIdEtat());
        if (raisonExists) {
            throw new IllegalArgumentException("Une raison d'inobservation existe déjà pour cet État d'Avancement.");
        }
        // Associer la raison à l'EtatAvancement
        raisonInobservation.setEtatAvancement(etatAvancement);
        // Sauvegarder la raison
        return raisonInobservationRepository.save(raisonInobservation);
    }



    // Rechercher une raison d'inobservation par ID
    public RaisonInobservation findById(Long idRai) {
        return raisonInobservationRepository.findById(idRai)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, idRai)));
    }

    // Supprimer une raison d'inobservation par ID
    @Transactional
    public void supprimerRaisonObservation(Long idRai) {
        if (raisonInobservationRepository.existsById(idRai)) {
            raisonInobservationRepository.deleteById(idRai);
        } else {
            throw new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, idRai));
        }
    }

    // Mettre à jour une raison d'inobservation existante
    @Transactional
    public RaisonInobservation modifierRaisonInobservation(Long idRaison, RaisonInobservation nouvelleRaison) {
        RaisonInobservation raisonsInobservation = findById(idRaison);
        raisonsInobservation.setNomRai(nouvelleRaison.getNomRai());
        return raisonInobservationRepository.save(raisonsInobservation);
    }

}
