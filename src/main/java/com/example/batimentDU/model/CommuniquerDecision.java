package com.example.batimentDU.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommuniquerDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCom;

    private int nbDec;
    private LocalDate dateDec;

    @OneToOne(mappedBy = "communiquerDecision")
    @JsonIgnore
    private DecisionCollective decisionCollective;

    @OneToOne(cascade = CascadeType.MERGE)
    private Relogement relogement;

    @OneToOne(cascade = CascadeType.MERGE)
    private EtatAvancement etatAvancement;

    public CommuniquerDecision() {}

    public CommuniquerDecision(int nbDec, LocalDate dateDec, Relogement relogement, EtatAvancement etatAvancement) {
        this.nbDec = nbDec;
        this.dateDec = dateDec;
        this.relogement = relogement;
        this.etatAvancement = etatAvancement;
    }

    // Getters
    public Long getIdCom() {
        return idCom;
    }

    public int getNbDec() {
        return nbDec;
    }

    public LocalDate getDateDec() {
        return dateDec;
    }

    public Relogement getRelogement() {
        return relogement;
    }

    public EtatAvancement getEtatAvancement() {
        return etatAvancement;
    }

    public DecisionCollective getDecisionCollective() {
        return decisionCollective;
    }

    // Setters
    public void setIdCom(Long idCom) {
        this.idCom = idCom;
    }

    public void setNbDec(int nbDec) {
        this.nbDec = nbDec;
    }

    public void setDateDec(LocalDate dateDec) {
        this.dateDec = dateDec;
    }

    public void setRelogement(Relogement relogement) {
        this.relogement = relogement;
    }

    public void setEtatAvancement(EtatAvancement etatAvancement) {
        this.etatAvancement = etatAvancement;
    }

    public void setDecisionCollective(DecisionCollective decisionCollective) {
        this.decisionCollective = decisionCollective;
    }

    // toString
    @Override
    public String toString() {
        return "CommuniquerDecision{" +
                "idCom=" + idCom +
                ", nbDec=" + nbDec +
                ", dateDec=" + dateDec +
                ", decisionCollective=" + decisionCollective +
                ", relogement=" + relogement +
                ", etatAvancement=" + etatAvancement +
                '}';
    }
}
