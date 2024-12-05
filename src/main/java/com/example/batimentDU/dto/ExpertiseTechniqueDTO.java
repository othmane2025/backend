package com.example.batimentDU.dto;

import com.example.batimentDU.enumeraion.RecommandationExpertise;

import java.time.LocalDate;

public class ExpertiseTechniqueDTO {

    private Long idExp;
    private String bureauEtude;
    private LocalDate date;
    private String recommandationExpertise;
    private String fileExpertise;

    public ExpertiseTechniqueDTO() {

    }
    public ExpertiseTechniqueDTO(Long idExp, String bureauEtude, LocalDate date, String recommandationExpertise, String fileExpertise) {
        this.idExp = idExp;
        this.bureauEtude = bureauEtude;
        this.date = date;
        this.recommandationExpertise = recommandationExpertise;
        this.fileExpertise = fileExpertise;
    }

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
}