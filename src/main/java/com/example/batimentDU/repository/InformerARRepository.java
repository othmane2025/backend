package com.example.batimentDU.repository;

import com.example.batimentDU.model.InformerAR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformerARRepository extends JpaRepository<InformerAR, Long> {
}
