package com.example.batimentDU.repository;

import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.Annexe;
import com.example.batimentDU.model.Batiment;
import com.example.batimentDU.model.ExpertiseTechnique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatimentRepository extends JpaRepository<Batiment, Long> {
    @Query("SELECT b FROM Batiment b WHERE b.annexe.nomAnnexe = :nomAnnexe")
    List<Batiment> findByNomAnnexe(@Param("nomAnnexe") NomAnnexe nomAnnexe);

    // Méthode pour récupérer une liste de bâtiments par adresse
    List<Batiment> findByAdresse(String adresse);

    @Query("SELECT b FROM Batiment b WHERE b.adresse = :adresse AND b.annexe.id = :annexeId")
    Optional<Batiment> findByAdresseAndAnnexe(@Param("adresse") String adresse, @Param("annexeId") Long annexeId);

    @Query("SELECT b FROM Batiment b WHERE b.annexe.arrondissement.nomArrondissement = :nomArrondissement")
    List<Batiment> findByArrondissement(@Param("nomArrondissement") NomArrondissement nomArrondissement);






}
