package com.example.batimentDU.dto;

import com.example.batimentDU.enumeraion.EntierementDemoli;
import com.example.batimentDU.enumeraion.PartiellementDemoli;

public class StatutActuelDTO {

    private Long idStatut;
    private String vide;
    private String vacant;
    private String entierementDemoli;
    private String partiellementDemoli;
    private String renforce;
    private String restaurer;

    // Constructeur par défaut
    public StatutActuelDTO() {
    }

    // Constructeur avec tous les champs
    public StatutActuelDTO(Long idStatut, String vide, String vacant,
                           String entierementDemoli,
                           String partiellementDemoli,
                           String renforce, String restaurer) {
        this.idStatut = idStatut;
        this.vide = vide;
        this.vacant = vacant;
        this.entierementDemoli = entierementDemoli;
        this.partiellementDemoli = partiellementDemoli;
        this.renforce = renforce;
        this.restaurer = restaurer;
    }

    // Getters et Setters
    public Long getIdStatut() { return idStatut; }
    public void setIdStatut(Long idStatut) { this.idStatut = idStatut; }

    public String getVide() { return vide; }
    public void setVide(String vide) { this.vide = vide; }

    public String getVacant() { return vacant; }
    public void setVacant(String vacant) { this.vacant = vacant; }

    public String getEntierementDemoli() {
        return entierementDemoli;
    }

    public void setEntierementDemoli(String entierementDemoli) {
        this.entierementDemoli = entierementDemoli;
    }

    public String getPartiellementDemoli() {
        return partiellementDemoli;
    }

    public void setPartiellementDemoli(String partiellementDemoli) {
        this.partiellementDemoli = partiellementDemoli;
    }

    public String getRenforce() { return renforce; }
    public void setRenforce(String renforce) { this.renforce = renforce; }

    public String getRestaurer() { return restaurer; }
    public void setRestaurer(String restaurer) { this.restaurer = restaurer; }

    @Override
    public String toString() {
        return "StatutActuelDTO{" +
                "idStatut=" + idStatut +
                ", vide='" + vide + '\'' +
                ", vacant='" + vacant + '\'' +
                ", entierementDemoli=" + entierementDemoli +
                ", partiellementDemoli=" + partiellementDemoli +
                ", renforce='" + renforce + '\'' +
                ", restaurer='" + restaurer + '\'' +
                '}';
    }
}
