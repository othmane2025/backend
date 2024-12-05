package com.example.batimentDU.dto;

import java.time.LocalDate;
import java.util.List;

public class AppercuRapportDTO {

    private Long idApp;
    private int nbRecommandation;
    private LocalDate dateInspection;
    private String recommandation;
    private int nbNotificationRapportInspection;
    private LocalDate dateNotification;
    private String filerapportInspection;

    private ExpertiseTechniqueDTO expertise; // Liste des expertises
    private DecisionCollectiveDTO decision; // lise des decisions

    public AppercuRapportDTO(Long idApp, int nbRecommandation, LocalDate dateInspection, String recommandation, int nbNotificationRapportInspection, LocalDate dateNotification, String filerapportInspection) {
        this.idApp = idApp;
        this.nbRecommandation = nbRecommandation;
        this.dateInspection = dateInspection;
        this.recommandation = recommandation;
        this.nbNotificationRapportInspection = nbNotificationRapportInspection;
        this.dateNotification = dateNotification;
        this.filerapportInspection = filerapportInspection;
    }

    public AppercuRapportDTO() {

    }

    public Long getIdApp() {
        return idApp;
    }

    public int getNbRecommandation() {
        return nbRecommandation;
    }

    public LocalDate getDateInspection() {
        return dateInspection;
    }

    public String getRecommandation() {
        return recommandation;
    }

    public int getNbNotificationRapportInspection() {
        return nbNotificationRapportInspection;
    }

    public LocalDate getDateNotification() {
        return dateNotification;
    }

    public String getFilerapportInspection() {
        return filerapportInspection;
    }

    public void setIdApp(Long idApp) {
        this.idApp = idApp;
    }

    public void setNbRecommandation(int nbRecommandation) {
        this.nbRecommandation = nbRecommandation;
    }

    public void setDateInspection(LocalDate dateInspection) {
        this.dateInspection = dateInspection;
    }

    public void setRecommandation(String recommandation) {
        this.recommandation = recommandation;
    }

    public void setNbNotificationRapportInspection(int nbNotificationRapportInspection) {
        this.nbNotificationRapportInspection = nbNotificationRapportInspection;
    }

    public void setDateNotification(LocalDate dateNotification) {
        this.dateNotification = dateNotification;
    }

    public void setFilerapportInspection(String filerapportInspection) {
        this.filerapportInspection = filerapportInspection;
    }

    public ExpertiseTechniqueDTO getExpertise() {
        return expertise;
    }

    public void setExpertise(ExpertiseTechniqueDTO expertise) {
        this.expertise = expertise;
    }

    public DecisionCollectiveDTO getDecision() {
        return decision;
    }

    public void setDecision(DecisionCollectiveDTO decision) {
        this.decision = decision;
    }
}
