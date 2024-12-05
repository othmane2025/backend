package com.example.batimentDU.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class RaisonInobservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRaison;
    private String nomRai;

    @OneToOne
    @JoinColumn(name = "etat_avancement_id")
    private EtatAvancement etatAvancement;

    public RaisonInobservation() {
    }

    public RaisonInobservation(String nomRai, EtatAvancement etatAvancement) {
        this.nomRai = nomRai;
        this.etatAvancement = etatAvancement;
    }

    // Getters
    public Long getIdRaison() {
        return idRaison;
    }

    public String getNomRai() {
        return nomRai;
    }

    public EtatAvancement getEtatAvancement() {
        return etatAvancement;
    }

    // Setters
    public void setIdRaison(Long idRaison) {
        this.idRaison = idRaison;
    }

    public void setNomRai(String nomRai) {
        this.nomRai = nomRai;
    }

    public void setEtatAvancement(EtatAvancement etatAvancement) {
        this.etatAvancement = etatAvancement;
    }

    // toString
    @Override
    public String toString() {
        return "RaisonInobservation{" +
                "idRaison=" + idRaison +
                ", nomRai='" + nomRai + '\'' +
                ", etatAvancement=" + etatAvancement +
                '}';
    }
}
