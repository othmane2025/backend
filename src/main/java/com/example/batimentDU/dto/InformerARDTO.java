package com.example.batimentDU.dto;

import java.time.LocalDate;

public class InformerARDTO {

    private Long idAR;
    private int nbAR;
    private LocalDate dateAr;

    // Constructeur par défaut
    public InformerARDTO() {
    }

    // Constructeur pour initialiser le DTO avec les attributs nécessaires
    public InformerARDTO(Long idAR, int nbAR, LocalDate dateAr) {
        this.idAR = idAR;
        this.nbAR = nbAR;
        this.dateAr = dateAr;
    }

    // Getters
    public Long getIdAR() {
        return idAR;
    }

    public int getNbAR() {
        return nbAR;
    }

    public LocalDate getDateAr() {
        return dateAr;
    }

    // Setters
    public void setIdAR(Long idAR) {
        this.idAR = idAR;
    }

    public void setNbAR(int nbAR) {
        this.nbAR = nbAR;
    }

    public void setDateAr(LocalDate dateAr) {
        this.dateAr = dateAr;
    }

    // toString pour afficher les informations du DTO
    @Override
    public String toString() {
        return "InformerARDTO{" +
                "idAR=" + idAR +
                ", nbAR=" + nbAR +
                ", dateAr=" + dateAr +
                '}';
    }
}
