package com.example.batimentDU.repository;



import com.example.batimentDU.model.CommuniquerDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;





import java.util.List;

@Repository
public interface CommuniquerDecisionRepository extends JpaRepository<CommuniquerDecision, Long> {
    List<CommuniquerDecision> findByEtatAvancement_IdEtat(long idEtat);


}
