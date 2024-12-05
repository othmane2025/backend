package com.example.batimentDU.repository;


import com.example.batimentDU.model.EtatAvancement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtatAvancementRepository extends JpaRepository<EtatAvancement, Long> {
    boolean existsByCommuniquerDecision_IdCom(Long idCom);
}
