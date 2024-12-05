package com.example.batimentDU.service;

import com.example.batimentDU.dto.StatutActuelDTO;
import com.example.batimentDU.enumeraion.EntierementDemoli;
import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.PartiellementDemoli;
import com.example.batimentDU.model.Batiment;
import com.example.batimentDU.model.StatutActuel;
import com.example.batimentDU.repository.BatimentRepository;
import com.example.batimentDU.repository.StatutActuelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatutActuelService {

    @Autowired
    private StatutActuelRepository statutActuelRepository;

    @Autowired
    private BatimentRepository batimentRepository;

    // Afficher tous les statuts actuels
    public List<StatutActuel> findAll() {
        return statutActuelRepository.findAll();
    }

    //
    public List<StatutActuelDTO> getStatutActuelsByNomAnnexe(NomAnnexe nomAnnexe) {
        List<Batiment> batiments = batimentRepository.findByNomAnnexe(nomAnnexe);
        return batiments.stream()
                .map(Batiment::getStatutActuel) // Supposons que chaque Batiment a un StatutActuel
                .filter(Objects::nonNull) // Filtrer les statuts actuels nuls
                .map(statut -> new StatutActuelDTO(
                        statut.getIdStatut(),
                        statut.getVide(),
                        statut.getVacant(),
                        statut.getEntierementDemoli(),
                        statut.getPartiellementDemoli(),
                        statut.getRenforce(),
                        statut.getRestaurer()
                ))
                .collect(Collectors.toList());
    }

    // Ajouter un statut actuel
    @Transactional
    public Map<String, Object> createStatutActuel(
            Long idBat,
            String vide,
            String vacant,
            String entierementDemoli,
            String partiellementDemoli,
            String renforce,
            String restaurer) {

        // Vérifier si le bâtiment existe
        Batiment batiment = batimentRepository.findById(idBat)
                .orElseThrow(() -> new EntityNotFoundException("Bâtiment introuvable avec l'ID " + idBat));
        // Vérifier si un statut existe déjà
        if (statutActuelRepository.existsByBatiment_IdBat(idBat)) {
            throw new IllegalArgumentException("Un StatutActuel existe déjà pour ce bâtiment.");
        }
        // Créer une nouvelle entité StatutActuel
        StatutActuel statutActuel = new StatutActuel();
        statutActuel.setVide(vide);
        statutActuel.setVacant(vacant);
        try {
            statutActuel.setEntierementDemoli(entierementDemoli);
            statutActuel.setPartiellementDemoli(partiellementDemoli);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valeur invalide pour EntierementDemoli ou PartiellementDemoli.");
        }
        statutActuel.setRenforce(renforce);
        statutActuel.setRestaurer(restaurer);
        // Associer le StatutActuel au bâtiment
        statutActuel.setBatiment(batiment);
        batiment.setStatutActuel(statutActuel);
        // Sauvegarder le statut dans la base de données
        statutActuelRepository.save(statutActuel);
        // Préparer la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("message", "StatutActuel ajouté avec succès !");
        response.put("statutActuel", statutActuel);
        return response;
    }




    // Rechercher un statut actuel par ID
    public StatutActuel findById(Long idStatut) {
        return statutActuelRepository.findById(idStatut)
                .orElseThrow(() -> new EntityNotFoundException("StatutActuel with id " + idStatut + " not found"));
    }

    // Supprimer un statut actuel
    @Transactional
    public void deleteStatutActuel(long idStatut) {
        StatutActuel statutActuel = statutActuelRepository.findById(idStatut)
                .orElseThrow(() -> new EntityNotFoundException("StatutActuel with id " + idStatut + " not found"));

        // Vérifiez s'il y a un Batiment associé et détachez-le
        Batiment batiment = statutActuel.getBatiment();
        if (batiment != null) {
            batiment.setStatutActuel(null); // Détache le StatutActuel
            batimentRepository.save(batiment); // Enregistre la modification
        }

        statutActuelRepository.deleteById(idStatut);
    }

    // Mettre à jour un statut actuel
    @Transactional
    public StatutActuel updateStatutActuel(Long idStatut, StatutActuel updatedStatut) {
        return statutActuelRepository.findById(idStatut)
                .map(statutActuel -> {
                    if (updatedStatut.getVide() != null) {
                        statutActuel.setVide(updatedStatut.getVide());
                    }
                    if (updatedStatut.getVacant() != null) {
                        statutActuel.setVacant(updatedStatut.getVacant());
                    }
                    if (updatedStatut.getEntierementDemoli() != null) {
                        statutActuel.setEntierementDemoli(updatedStatut.getEntierementDemoli());
                    }
                    if (updatedStatut.getPartiellementDemoli() != null) {
                        statutActuel.setPartiellementDemoli(updatedStatut.getPartiellementDemoli());
                    }
                    if (updatedStatut.getRenforce() != null) {
                        statutActuel.setRenforce(updatedStatut.getRenforce());
                    }
                    if (updatedStatut.getRestaurer() != null) {
                        statutActuel.setRestaurer(updatedStatut.getRestaurer());
                    }
                    return statutActuelRepository.save(statutActuel);
                })
                .orElseThrow(() -> new EntityNotFoundException("StatutActuel not found with ID: " + idStatut));
    }

}
