package com.example.batimentDU.model;

import com.example.batimentDU.enumeraion.RecommandationDecision;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DecisionCollective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDec;
    private int nbDec;
    private LocalDate dateDec;

    @Column(name = "recommandationDecision")
    private String recommandationDecision;

    private String fileRapport;

    @OneToOne(mappedBy = "decisionCollective", fetch = FetchType.LAZY)
    @JsonBackReference
    private AppercuRapport appercuRapport;


    @OneToOne(cascade = CascadeType.MERGE)
    private CommuniquerDecision communiquerDecision;

    // Constructeurs
    public DecisionCollective() {}

    public DecisionCollective(int nbDec, LocalDate dateDec, String recommandationDecision, String fileRapport, CommuniquerDecision communiquerDecision) {
        this.nbDec = nbDec;
        this.dateDec = dateDec;
        this.recommandationDecision = recommandationDecision;
        this.fileRapport = fileRapport;
        this.communiquerDecision = communiquerDecision;
    }

    // Getters
    public Long getIdDec() {
        return idDec;
    }

    public int getNbDec() {
        return nbDec;
    }

    public LocalDate getDateDec() {
        return dateDec;
    }

    public String getRecommandationDecision() {
        return recommandationDecision;
    }

    public String getFileRapport() {
        return fileRapport;
    }

    public CommuniquerDecision getCommuniquerDecision() {
        return communiquerDecision;
    }


    public AppercuRapport getAppercuRapport() {
        return appercuRapport;
    }

    // Setters
    public void setIdDec(Long idDec) {
        this.idDec = idDec;
    }

    public void setNbDec(int nbDec) {
        this.nbDec = nbDec;
    }

    public void setDateDec(LocalDate dateDec) {
        this.dateDec = dateDec;
    }

    public void setRecommandationDecision(String recommandationDecision) {
        this.recommandationDecision = recommandationDecision;
    }

    public void setFileRapport(String fileRapport) {
        this.fileRapport = fileRapport;
    }

    public void setCommuniquerDecision(CommuniquerDecision communiquerDecision) {
        this.communiquerDecision = communiquerDecision;
    }

    public void setAppercuRapport(AppercuRapport appercuRapport) {
        this.appercuRapport = appercuRapport;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "DecisionCollective{" +
                "idDec=" + idDec +
                ", nbDec=" + nbDec +
                ", dateDec=" + dateDec +
                ", recommandationDecision=" + recommandationDecision +
                ", fileRapport='" + fileRapport + '\'' +
                ", communiquerDecision=" + communiquerDecision +

                '}';
    }
}
