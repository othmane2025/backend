package com.example.batimentDU.dto;

import com.example.batimentDU.enumeraion.RecommandationDecision;

import java.time.LocalDate;

public class DecisionCollectiveDTO {
    private Long idDec;
    private int nbDec;
    private LocalDate dateDec;
    private String recommandationDecision;
    private String fileRapport;
    private CommuniquerDecisionDTO communiquer;

    // Constructeur complet

    public DecisionCollectiveDTO() {

    }

    public DecisionCollectiveDTO(Long idDec, int nbDec, LocalDate dateDec, String recommandationDecision, String fileRapport) {
        this.idDec = idDec;
        this.nbDec = nbDec;
        this.dateDec = dateDec;
        this.recommandationDecision = recommandationDecision;
        this.fileRapport = fileRapport;

    }

    // Getters et Setters


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


    public CommuniquerDecisionDTO getCommuniquer() {
        return communiquer;
    }

    public void setCommuniquer(CommuniquerDecisionDTO communiquer) {
        this.communiquer = communiquer;
    }
}