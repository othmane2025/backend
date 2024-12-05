package com.example.batimentDU.service;

import com.example.batimentDU.dto.InformerARDTO;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.model.*;
import com.example.batimentDU.repository.AppercuRapportRepository;
import com.example.batimentDU.repository.BatimentRepository;
import com.example.batimentDU.repository.EtatAvancementRepository;
import com.example.batimentDU.repository.InformerARRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des entités InformerAR
 */
@Service
public class InformerARService {

    @Autowired
    private InformerARRepository informerARRepository;

    @Autowired
    private BatimentRepository batimentRepository;

    @Autowired
    private AppercuRapportRepository appercuRapportRepository;

    @Autowired
    private EtatAvancementRepository etatAvancementRepository;


    // Récupérer tous les InformerAR
    public List<InformerAR> findAll() {
        return informerARRepository.findAll();
    }

    // Obtenir la liste des bâtiments en fonction du nom de l'annexe pour InformerAR
    public List<InformerARDTO> getInformerAgentByNomAnnexe(NomAnnexe nomAnnexe) {
        List<Batiment> batiments = batimentRepository.findByNomAnnexe(nomAnnexe);
        return batiments.stream()
                .map(Batiment::getAppercuRapport)                    // Obtenir les AppercuRapport de chaque bâtiment
                .filter(Objects::nonNull)                            // Filtrer les AppercuRapport nuls
                .map(AppercuRapport::getDecisionCollective)          // Obtenir directement DecisionCollective depuis AppercuRapport
                .filter(Objects::nonNull)                            // Filtrer les DecisionCollective nulles
                .map(DecisionCollective::getCommuniquerDecision)     // Obtenir CommuniquerDecision depuis DecisionCollective
                .filter(Objects::nonNull)                            // Filtrer les CommuniquerDecision nulles
                .map(CommuniquerDecision::getEtatAvancement)         // Obtenir EtatAvancement depuis CommuniquerDecision
                .filter(Objects::nonNull)                            // Filtrer les EtatAvancement nulls
                .map(EtatAvancement::getInformerAR)                  // Obtenir InformerAR pour chaque EtatAvancement
                .filter(Objects::nonNull)                            // Filtrer les InformerAR nulls
                .map(informerAR -> new InformerARDTO(
                        informerAR.getIdAR(),                        // Récupérer l'ID de InformerAR
                        informerAR.getNbAR(),                        // Récupérer le nombre d'AR
                        informerAR.getDateAr()                       // Récupérer la date d'AR
                ))
                .collect(Collectors.toList());
    }


    // Ajouter un nouveau InformerAR
    @Transactional
    public InformerAR addInformerAR(Long idBat, InformerAR informerAR) {
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

        // Vérifier si un InformerAR existe déjà
        if (etatAvancement.getInformerAR() != null) {
            throw new IllegalArgumentException("Un InformerAR existe déjà pour cet État d'Avancement.");
        }
        // Associer InformerAR à EtatAvancement
        etatAvancement.setInformerAR(informerAR);
        informerAR.setEtatAvancement(etatAvancement);
        // Sauvegarder InformerAR et mettre à jour l'EtatAvancement
        informerARRepository.save(informerAR);
        etatAvancementRepository.save(etatAvancement);
        return informerAR;
    }



    // Rechercher un InformerAR par ID
    public InformerAR findInformerARById(Long idAR) {
        return informerARRepository.findById(idAR)
                .orElseThrow(() -> new EntityNotFoundException("InformerAR avec l'id " + idAR + " n'existe pas."));
    }

    // Supprimer un InformerAR par ID
    @Transactional
    public void deleteInformerAR(Long idAR) {
        if (informerARRepository.existsById(idAR)) {
            informerARRepository.deleteById(idAR);
        } else {
            throw new EntityNotFoundException("InformerAR avec l'id " + idAR + " n'existe pas.");
        }
    }

    // Mettre à jour un InformerAR existant
    @Transactional
    public InformerAR updateInformerAR(Long idAR, InformerAR informerARDetails) {
        InformerAR informerAR = findInformerARById(idAR);
        informerAR.setNbAR(informerARDetails.getNbAR());
        informerAR.setDateAr(informerARDetails.getDateAr());
        return informerARRepository.save(informerAR);
    }
}
