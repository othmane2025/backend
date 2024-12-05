package com.example.batimentDU.model;

import com.example.batimentDU.enumeraion.RecommandationExpertise;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpertiseTechnique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExp;

    private String bureauEtude;

    private LocalDate date;

    @Column(name = "recommandationExpertise")
    private String recommandationExpertise;

    private String fileExpertise;

    @OneToOne
    @JoinColumn(name = "appercu_rapport_id", referencedColumnName = "idApp")
    @JsonIgnore
    private AppercuRapport appercuRapport;





    // Constructeurs
    public ExpertiseTechnique() {}

    public ExpertiseTechnique(String bureauEtude, LocalDate date, String recommandationExpertise, String fileExpertise, AppercuRapport appercuRapport, DecisionCollective decisionCollective) {
        this.bureauEtude = bureauEtude;
        this.date = date;
        this.recommandationExpertise = recommandationExpertise;
        this.fileExpertise = fileExpertise;
        this.appercuRapport = appercuRapport;

    }

    // Getters
    public Long getIdExp() {
        return idExp;
    }

    public String getBureauEtude() {
        return bureauEtude;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getRecommandationExpertise() {
        return recommandationExpertise;
    }

    public String getFileExpertise() {
        return fileExpertise;
    }

    public AppercuRapport getAppercuRapport() {
        return appercuRapport;
    }



    // Setters
    public void setIdExp(Long idExp) {
        this.idExp = idExp;
    }

    public void setBureauEtude(String bureauEtude) {
        this.bureauEtude = bureauEtude;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setRecommandationExpertise(String recommandationExpertise) {
        this.recommandationExpertise = recommandationExpertise;
    }

    public void setFileExpertise(String fileExpertise) {
        this.fileExpertise = fileExpertise;
    }

    public void setAppercuRapport(AppercuRapport appercuRapport) {
        this.appercuRapport = appercuRapport;
    }


    // Méthode toString
    @Override
    public String toString() {
        return "ExpertiseTechnique{" +
                "idExp=" + idExp +
                ", bureauEtude='" + bureauEtude + '\'' +
                ", date=" + date +
                ", recommandationExpertise=" + recommandationExpertise +
                ", fileExpertise='" + fileExpertise + '\'' +
                ", appercuRapport=" + appercuRapport +
                '}';
    }
}
