package com.example.batimentDU.repository;

import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.AppercuRapport;
import com.example.batimentDU.model.Batiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppercuRapportRepository extends JpaRepository<AppercuRapport, Long> {
    // Cette méthode vérifie si un AppercuRapport existe déjà pour un bâtiment spécifique (par son ID)
    // Utilisez le nom correct de la propriété "idBat"
    boolean existsByBatiment_IdBat(Long idBat);

    @Query("SELECT ar FROM AppercuRapport ar " +
            "JOIN ar.batiment b " +
            "WHERE b.idBat = :batimentId")
    Optional<AppercuRapport> findByBatimentId(@Param("batimentId") Long batimentId);

    @Query("SELECT ar FROM AppercuRapport ar WHERE ar.batiment.annexe.arrondissement.nomArrondissement = :nomArrondissement")
    List<AppercuRapport> findByArrondissement(@Param("nomArrondissement") NomArrondissement nomArrondissement);


}
