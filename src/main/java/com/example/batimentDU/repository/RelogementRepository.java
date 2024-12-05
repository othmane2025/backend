package com.example.batimentDU.repository;

import com.example.batimentDU.model.Annexe;
import com.example.batimentDU.model.Relogement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RelogementRepository extends JpaRepository<Relogement, Long> {

    @Query("SELECT r FROM Relogement r " +
            "JOIN r.communiquerDecision cd " +   // Direct relation with CommuniquerDecision
            "JOIN cd.decisionCollective dc " +  // Relation with DecisionCollective through CommuniquerDecision
            "JOIN dc.appercuRapport ar " +      // Relation from DecisionCollective to AppercuRapport
            "JOIN ar.batiment b " +             // Relation from AppercuRapport to Batiment
            "WHERE b.idBat = :batimentId")
    Optional<Relogement> findByBatimentId(@Param("batimentId") Long batimentId);
    boolean existsByCommuniquerDecision_IdCom(Long idCom);


}

