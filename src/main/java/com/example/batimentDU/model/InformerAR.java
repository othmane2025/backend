package com.example.batimentDU.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class InformerAR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAR;
    private int nbAR;
    private LocalDate dateAr;

    @OneToOne(mappedBy = "informerAR")
    @JsonIgnore
    private EtatAvancement etatAvancement;

    public InformerAR() {
    }

    public InformerAR(int nbAR, LocalDate dateAr) {
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

    public EtatAvancement getEtatAvancement() {
        return etatAvancement;
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

    public void setEtatAvancement(EtatAvancement etatAvancement) {
        this.etatAvancement = etatAvancement;
    }

    // toString
    @Override
    public String toString() {
        return "InformerAR{" +
                "idAR=" + idAR +
                ", nbAR=" + nbAR +
                ", dateAr=" + dateAr +
                '}';
    }
}
