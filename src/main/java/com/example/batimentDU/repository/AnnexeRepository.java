package com.example.batimentDU.repository;

import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.model.Annexe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnexeRepository extends JpaRepository<Annexe, Long> {
    @Query("SELECT a FROM Annexe a WHERE a.arrondissement.idArr = :arrondissementId")
    List<Annexe> findByArrondissementId(@Param("arrondissementId") Long arrondissementId);

    Annexe findByNomAnnexe(NomAnnexe nomAnnexe);
}
