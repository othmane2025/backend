package com.example.batimentDU.model;

import com.example.batimentDU.enumeraion.EntierementDemoli;
import com.example.batimentDU.enumeraion.PartiellementDemoli;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatutActuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStatut;
    private String vide;
    private String vacant;

    @Column(name = "entierementDemoli")
    private String entierementDemoli;

    @Column(name = "partiellementDemoli")
    private String partiellementDemoli;

    private String renforce;
    private String restaurer;

    @OneToOne
    @JoinColumn(name = "batiment_id", referencedColumnName = "idBat")
    private Batiment batiment;

    public StatutActuel() {
    }

    public StatutActuel(String vide, String vacant, String entierementDemoli, String partiellementDemoli, String renforce, String restaurer) {
        this.vide = vide;
        this.vacant = vacant;
        this.entierementDemoli = entierementDemoli;
        this.partiellementDemoli = partiellementDemoli;
        this.renforce = renforce;
        this.restaurer = restaurer;
    }

    // Getters
    public Long getIdStatut() { return idStatut; }
    public String getVide() { return vide; }
    public String getVacant() { return vacant; }

    public String getEntierementDemoli() {
        return entierementDemoli;
    }

    public String getPartiellementDemoli() {
        return partiellementDemoli;
    }

    public String getRenforce() { return renforce; }
    public String getRestaurer() { return restaurer; }
    public Batiment getBatiment() { return batiment; }

    // Setters
    public void setIdStatut(Long idStatut) { this.idStatut = idStatut; }
    public void setVide(String vide) { this.vide = vide; }
    public void setVacant(String vacant) { this.vacant = vacant; }

    public void setEntierementDemoli(String entierementDemoli) {
        this.entierementDemoli = entierementDemoli;
    }

    public void setPartiellementDemoli(String partiellementDemoli) {
        this.partiellementDemoli = partiellementDemoli;
    }

    public void setRenforce(String renforce) { this.renforce = renforce; }
    public void setRestaurer(String restaurer) { this.restaurer = restaurer; }
    public void setBatiment(Batiment batiment) { this.batiment = batiment; }

    // toString
    @Override
    public String toString() {
        return "StatutActuel{" +
                "idStatut=" + idStatut +
                ", vide='" + vide + '\'' +
                ", vacant='" + vacant + '\'' +
                ", entierementDemoli=" + entierementDemoli +
                ", partiellementDemoli=" + partiellementDemoli +
                ", renforce=" + renforce +
                ", restaurer=" + restaurer +
                '}';
    }
}
