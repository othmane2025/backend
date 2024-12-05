package com.example.batimentDU.repository;

import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.Arrondissement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArrondissementRepository  extends JpaRepository<Arrondissement, Long> {
    // Rechercher un arrondissement par son nom en français
    Arrondissement findByNomArrondissement(NomArrondissement nomArrondissement);

}
