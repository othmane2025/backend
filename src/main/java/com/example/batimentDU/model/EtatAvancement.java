package com.example.batimentDU.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class EtatAvancement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtat;
    private int nbCorrespondanceAL;
    private LocalDate dateCorrespondanceAL;
    private int nbReponseAL;
    private LocalDate dateReponseAL;

    @OneToOne(mappedBy = "etatAvancement")
    @JsonIgnore
    private CommuniquerDecision communiquerDecision;

    @OneToOne(mappedBy = "etatAvancement", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private RaisonInobservation raisonInobservation;

    @OneToOne(cascade = CascadeType.MERGE)
    private InformerAR informerAR;

    // Constructeurs
    public EtatAvancement() {}

    public EtatAvancement(int nbCorrespondanceAL, LocalDate dateCorrespondanceAL, int nbReponseAL, LocalDate dateReponseAL, InformerAR informerAR) {
        this.nbCorrespondanceAL = nbCorrespondanceAL;
        this.dateCorrespondanceAL = dateCorrespondanceAL;
        this.nbReponseAL = nbReponseAL;
        this.dateReponseAL = dateReponseAL;
        this.informerAR = informerAR;
    }

    // Getters
    public Long getIdEtat() {
        return idEtat;
    }

    public int getNbCorrespondanceAL() {
        return nbCorrespondanceAL;
    }

    public LocalDate getDateCorrespondanceAL() {
        return dateCorrespondanceAL;
    }

    public int getNbReponseAL() {
        return nbReponseAL;
    }

    public LocalDate getDateReponseAL() {
        return dateReponseAL;
    }

    public InformerAR getInformerAR() {
        return informerAR;
    }


    public RaisonInobservation getRaisonInobservation() {
        return raisonInobservation;
    }

    public CommuniquerDecision getCommuniquerDecision() {
        return communiquerDecision;
    }

    // Setters
    public void setIdEtat(Long idEtat) {
        this.idEtat = idEtat;
    }

    public void setNbCorrespondanceAL(int nbCorrespondanceAL) {
        this.nbCorrespondanceAL = nbCorrespondanceAL;
    }

    public void setDateCorrespondanceAL(LocalDate dateCorrespondanceAL) {
        this.dateCorrespondanceAL = dateCorrespondanceAL;
    }

    public void setNbReponseAL(int nbReponseAL) {
        this.nbReponseAL = nbReponseAL;
    }

    public void setDateReponseAL(LocalDate dateReponseAL) {
        this.dateReponseAL = dateReponseAL;
    }

    public void setInformerAR(InformerAR informerAR) {
        this.informerAR = informerAR;
    }

    public void setRaisonInobservation(RaisonInobservation raisonInobservation) {
        this.raisonInobservation = raisonInobservation;
    }

    public void setCommuniquerDecision(CommuniquerDecision communiquerDecision) {
        this.communiquerDecision = communiquerDecision;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "EtatAvancement{" +
                "idEtat=" + idEtat +
                ", nbCorrespondanceAL=" + nbCorrespondanceAL +
                ", dateCorrespondanceAL=" + dateCorrespondanceAL +
                ", nbReponseAL=" + nbReponseAL +
                ", dateReponseAL=" + dateReponseAL +
                ", informerAR=" + informerAR +
                '}';
    }
}
