package com.example.batimentDU.repository;


import com.example.batimentDU.model.RaisonInobservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface RaisonInobservationRepository extends JpaRepository<RaisonInobservation, Long> {
    List<RaisonInobservation> findByEtatAvancement_IdEtat(long idEtat);
    boolean existsByEtatAvancement_IdEtat(Long idEtat);

}
