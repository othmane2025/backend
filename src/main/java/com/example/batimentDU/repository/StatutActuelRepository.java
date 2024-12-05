package com.example.batimentDU.repository;


import com.example.batimentDU.model.StatutActuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutActuelRepository extends JpaRepository<StatutActuel, Long> {

    boolean existsByBatiment_IdBat(Long idBat);
}
