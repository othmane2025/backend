package com.example.batimentDU.dto;

import com.example.batimentDU.model.*;

public class ExpertiseDTO {
    private ExpertiseTechnique expertise;
    private AppercuRapport appercuRapport;
    private Batiment batiment;
    private DecisionCollective decisionCollective;
    private CommuniquerDecision communiquerDecision;
    private EtatAvancement etatAvancement;
    private Relogement relogement;
    private RaisonInobservation raisonInobservation;
    private InformerAR informerAR;
    private StatutActuel statutActuel;

    // Getters et setters
    public ExpertiseTechnique getExpertise() {
        return expertise;
    }

    public void setExpertise(ExpertiseTechnique expertise) {
        this.expertise = expertise;
    }

    public AppercuRapport getAppercuRapport() {
        return appercuRapport;
    }

    public void setAppercuRapport(AppercuRapport appercuRapport) {
        this.appercuRapport = appercuRapport;
    }

    public Batiment getBatiment() {
        return batiment;
    }

    public void setBatiment(Batiment batiment) {
        this.batiment = batiment;
    }

    public DecisionCollective getDecisionCollective() {
        return decisionCollective;
    }

    public void setDecisionCollective(DecisionCollective decisionCollective) {
        this.decisionCollective = decisionCollective;
    }

    public CommuniquerDecision getCommuniquerDecision() {
        return communiquerDecision;
    }

    public void setCommuniquerDecision(CommuniquerDecision communiquerDecision) {
        this.communiquerDecision = communiquerDecision;
    }

    public EtatAvancement getEtatAvancement() {
        return etatAvancement;
    }

    public void setEtatAvancement(EtatAvancement etatAvancement) {
        this.etatAvancement = etatAvancement;
    }

    public Relogement getRelogement() {
        return relogement;
    }

    public void setRelogement(Relogement relogement) {
        this.relogement = relogement;
    }

    public RaisonInobservation getRaisonInobservation() {
        return raisonInobservation;
    }

    public void setRaisonInobservation(RaisonInobservation raisonInobservation) {
        this.raisonInobservation = raisonInobservation;
    }

    public InformerAR getInformerAR() {
        return informerAR;
    }

    public void setInformerAR(InformerAR informerAR) {
        this.informerAR = informerAR;
    }

    public StatutActuel getStatutActuel() {
        return statutActuel;
    }

    public void setStatutActuel(StatutActuel statutActuel) {
        this.statutActuel = statutActuel;
    }
}
