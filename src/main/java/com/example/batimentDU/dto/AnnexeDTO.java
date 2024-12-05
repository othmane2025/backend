package com.example.batimentDU.dto;

import com.example.batimentDU.enumeraion.NomAnnexe;

public class AnnexeDTO {
    private Long idAnn;
    private NomAnnexe nomAnnexe;

    public AnnexeDTO(Long idAnn, NomAnnexe nomAnnexe) {
        this.idAnn = idAnn;
        this.nomAnnexe = nomAnnexe;
    }

    public Long getIdAnn() {
        return idAnn;
    }

    public void setIdAnn(Long idAnn) {
        this.idAnn = idAnn;
    }

    public NomAnnexe getNomAnnexe() {
        return nomAnnexe;
    }

    public void setNomAnnexe(NomAnnexe nomAnnexe) {
        this.nomAnnexe = nomAnnexe;
    }
}