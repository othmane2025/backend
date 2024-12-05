package com.example.batimentDU.service;

import com.example.batimentDU.enumeraion.NomAnnexe;
import com.example.batimentDU.model.Annexe;
import com.example.batimentDU.model.Batiment;
import com.example.batimentDU.repository.AnnexeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnexeService {

    @Autowired
    AnnexeRepository annexeRepository;

    public List<Annexe> findAll(){
        return annexeRepository.findAll();
    }

    public Annexe addAnnexe(Annexe annexe) {
        if (annexe.getArrondissement() == null || annexe.getArrondissement().getIdArr() == null) {
            throw new IllegalArgumentException("Annexe must have a non-null arrondissement");
        }
        annexeRepository.save(annexe);
        return annexe;
    }

    public Annexe findByNomAnnexe(NomAnnexe nomAnnexe) {
        return annexeRepository.findByNomAnnexe(nomAnnexe);
    }

    // chercher Annexe
    public Annexe findById(Long idAnn) {
        return annexeRepository.findById(idAnn).orElseThrow(() -> new EntityNotFoundException("Annexe with id" + idAnn + " Not Found"));
    }

    public List<Annexe> findAnnexesByArrondissementId(Long arrondissementId) {
        return annexeRepository.findByArrondissementId(arrondissementId);
    }

}
