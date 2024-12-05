package com.example.batimentDU.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idApp")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AppercuRapport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idApp;

    private int nbRecommandation;
    private LocalDate dateInspection;
    private String recommandation;
    private int nbNotificationRapportInspection;
    private LocalDate dateNotification;
    private String filerapportInspection;


    @OneToOne
    @JoinColumn(name = "batiment_id")
    @JsonBackReference
    private Batiment batiment;


    @OneToOne(mappedBy = "appercuRapport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private ExpertiseTechnique expertise;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "decision_collective_id", referencedColumnName = "idDec")
    @JsonManagedReference
    private DecisionCollective decisionCollective;





    // Constructeurs
    public AppercuRapport() {}

    public AppercuRapport(int nbRecommandation, LocalDate dateInspection, String recommandation,
                          int nbNotificationRapportInspection, LocalDate dateNotification, String filerapportInspection) {
        this.nbRecommandation = nbRecommandation;
        this.dateInspection = dateInspection;
        this.recommandation = recommandation;
        this.nbNotificationRapportInspection = nbNotificationRapportInspection;
        this.dateNotification = dateNotification;
        this.filerapportInspection = filerapportInspection;
    }

    // Getters
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

    public Batiment getBatiment() {
        return batiment;
    }

    public DecisionCollective getDecisionCollective() {
        return decisionCollective;
    }


    public ExpertiseTechnique getExpertise() {
        return expertise;
    }

    // Setters
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

    public void setDecisionCollective(DecisionCollective decisionCollective) {
        this.decisionCollective = decisionCollective;
    }
    public void setDateNotification(LocalDate dateNotification) {
        this.dateNotification = dateNotification;
    }

    public void setFilerapportInspection(String filerapportInspection) {
        this.filerapportInspection = filerapportInspection;
    }

    public void setBatiment(Batiment batiment) {
        this.batiment = batiment;
    }

    public void setExpertise(ExpertiseTechnique expertise) {
        this.expertise = expertise;
    }

    // toString
    @Override
    public String toString() {
        return "AppercuRapport{" +
                "idApp=" + idApp +
                ", nbRecommandation=" + nbRecommandation +
                ", dateInspection=" + dateInspection +
                ", recommandation='" + recommandation + '\'' +
                ", nbNotificationRapportInspection=" + nbNotificationRapportInspection +
                ", dateNotification=" + dateNotification +
                ", filerapportInspection='" + filerapportInspection + '\'' +
                '}';
    }
}
