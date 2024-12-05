package com.example.batimentDU.service;

import com.example.batimentDU.enumeraion.NomArrondissement;
import com.example.batimentDU.model.AppercuRapport;
import com.example.batimentDU.model.Arrondissement;
import com.example.batimentDU.repository.AppercuRapportRepository;
import com.example.batimentDU.repository.ArrondissementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArrondissementService {

    @Autowired
    ArrondissementRepository arrondissementRepository;


    public Arrondissement getArrondissement(Long idArr) {
        return arrondissementRepository.findById(idArr).orElse(null);
    }



    public Arrondissement findByArabicName(String nomArabe) {
        // Correspondance entre le nom en arabe et l'énumération NomArrondissement
        NomArrondissement nomArrondissement;
        switch (nomArabe) {
            case "ابن مسيك":
                nomArrondissement = NomArrondissement.BenMsick;
                break;
            case "سباتة":
                nomArrondissement = NomArrondissement.Sbata;
                break;
            default:
                throw new IllegalArgumentException("Nom d'arrondissement non reconnu : " + nomArabe);
        }
        // Utilisez l'énumération directement dans la recherche
        return arrondissementRepository.findByNomArrondissement(nomArrondissement);
    }

    public Arrondissement findById(Long id) {
        return arrondissementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Arrondissement avec ID " + id + " non trouvé"));
    }

    public List<Arrondissement> findAll(){
        return arrondissementRepository.findAll();
    }

    public Arrondissement save(Arrondissement arrondissement) {
        return arrondissementRepository.save(arrondissement);
    }


}
