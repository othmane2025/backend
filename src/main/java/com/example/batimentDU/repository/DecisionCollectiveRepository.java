package com.example.batimentDU.repository;


import com.example.batimentDU.model.DecisionCollective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DecisionCollectiveRepository extends JpaRepository<DecisionCollective, Long> {

    @Query("SELECT dc FROM DecisionCollective dc " +
            "JOIN dc.appercuRapport ar " +    // Direct relationship between DecisionCollective and AppercuRapport
            "JOIN ar.batiment b " +           // Relationship between AppercuRapport and Batiment
            "WHERE b.idBat = :batimentId")
    Optional<DecisionCollective> findByBatimentId(@Param("batimentId") Long batimentId);



    @Query("SELECT d FROM DecisionCollective d " +
            "LEFT JOIN FETCH d.appercuRapport ar " +
            "LEFT JOIN FETCH ar.batiment b " +
            "LEFT JOIN FETCH d.communiquerDecision cd " +
            "LEFT JOIN FETCH cd.etatAvancement ea " +
            "LEFT JOIN FETCH cd.relogement " +
            "WHERE LOWER(d.recommandationDecision) = LOWER(:recommandationDecision)")
    List<DecisionCollective> findByRecommandationDecision(@Param("recommandationDecision") String recommandationDecision);

}

