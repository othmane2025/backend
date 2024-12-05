package com.example.batimentDU.dto;

import java.time.LocalDate;

public class CommuniquerDecisionDTO {
    private Long idCom;
    private int nbDec;
    private LocalDate dateDec;
    private RelogementDTO relogement;

    private EtatAvancementDTO etatAvancement;


    public CommuniquerDecisionDTO() {

    }
    public CommuniquerDecisionDTO(Long idCom, int nbDec, LocalDate dateDec) {
        this.idCom = idCom;
        this.nbDec = nbDec;
        this.dateDec = dateDec;
    }


    public Long getIdCom() {
        return idCom;
    }

    public void setIdCom(Long idCom) {
        this.idCom = idCom;
    }

    public int getNbDec() {
        return nbDec;
    }

    public void setNbDec(int nbDec) {
        this.nbDec = nbDec;
    }

    public LocalDate getDateDec() {
        return dateDec;
    }

    public void setDateDec(LocalDate dateDec) {
        this.dateDec = dateDec;
    }

    public RelogementDTO getRelogement() {
        return relogement;
    }

    public void setRelogement(RelogementDTO relogement) {
        this.relogement = relogement;
    }

    public EtatAvancementDTO getEtatAvancement() {
        return etatAvancement;
    }

    public void setEtatAvancement(EtatAvancementDTO etatAvancement) {
        this.etatAvancement = etatAvancement;
    }
}