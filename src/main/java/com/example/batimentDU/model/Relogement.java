package com.example.batimentDU.model;

import com.example.batimentDU.enumeraion.SocieteSurveillance;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Relogement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRel;
    private int nbFamilleExplusees;
    private int nbFamillePrestations;
    private String nomPrenomPrestations;
    private int nbFamilleBeneficiaire;
    private String nomPrenomBeneficiaire;
    private LocalDate dateTirageSort;
    private String lieuPrestation;
    private String relocalisation;
    @Column(name= "societeSurveillance")
    private String societeSurveillance;
    private String fileRapportTirage;

    @OneToOne(mappedBy = "relogement")
    @JsonIgnore
    private CommuniquerDecision communiquerDecision;

    public Relogement() {
    }

    public Relogement(int nbFamilleExplusees, int nbFamillePrestations, String nomPrenomPrestations,
                      int nbFamilleBeneficiaire, String nomPrenomBeneficiaire, LocalDate dateTirageSort,
                      String lieuPrestation, String relocalisation, String societeSurveillance,
                      String fileRapportTirage) {
        this.nbFamilleExplusees = nbFamilleExplusees;
        this.nbFamillePrestations = nbFamillePrestations;
        this.nomPrenomPrestations = nomPrenomPrestations;
        this.nbFamilleBeneficiaire = nbFamilleBeneficiaire;
        this.nomPrenomBeneficiaire = nomPrenomBeneficiaire;
        this.dateTirageSort = dateTirageSort;
        this.lieuPrestation = lieuPrestation;
        this.relocalisation = relocalisation;
        this.societeSurveillance = societeSurveillance;
        this.fileRapportTirage = fileRapportTirage;
    }

    // Getters and Setters

    public Long getIdRel() { return idRel; }
    public int getNbFamilleExplusees() { return nbFamilleExplusees; }
    public String getNomPrenomPrestations() { return nomPrenomPrestations; }
    public String getNomPrenomBeneficiaire() { return nomPrenomBeneficiaire; }
    public LocalDate getDateTirageSort() { return dateTirageSort; }
    public String getLieuPrestation() { return lieuPrestation; }
    public String getRelocalisation() { return relocalisation; }

    public String getSocieteSurveillance() {
        return societeSurveillance;
    }

    public String getFileRapportTirage() { return fileRapportTirage; }
    public int getNbFamillePrestations() { return nbFamillePrestations; }
    public int getNbFamilleBeneficiaire() { return nbFamilleBeneficiaire; }
    public CommuniquerDecision getCommuniquerDecision() { return communiquerDecision; }

    public void setIdRel(Long idRel) { this.idRel = idRel; }
    public void setNbFamilleExplusees(int nbFamilleExplusees) { this.nbFamilleExplusees = nbFamilleExplusees; }
    public void setNomPrenomPrestations(String nomPrenomPrestations) { this.nomPrenomPrestations = nomPrenomPrestations; }
    public void setNomPrenomBeneficiaire(String nomPrenomBeneficiaire) { this.nomPrenomBeneficiaire = nomPrenomBeneficiaire; }
    public void setDateTirageSort(LocalDate dateTirageSort) { this.dateTirageSort = dateTirageSort; }
    public void setLieuPrestation(String lieuPrestation) { this.lieuPrestation = lieuPrestation; }
    public void setRelocalisation(String relocalisation) { this.relocalisation = relocalisation; }

    public void setSocieteSurveillance(String societeSurveillance) {
        this.societeSurveillance = societeSurveillance;
    }

    public void setFileRapportTirage(String fileRapportTirage) { this.fileRapportTirage = fileRapportTirage; }
    public void setNbFamilleBeneficiaire(int nbFamilleBeneficiaire) { this.nbFamilleBeneficiaire = nbFamilleBeneficiaire; }
    public void setNbFamillePrestations(int nbFamillePrestations) { this.nbFamillePrestations = nbFamillePrestations; }
    public void setCommuniquerDecision(CommuniquerDecision communiquerDecision) { this.communiquerDecision = communiquerDecision; }

    @Override
    public String toString() {
        return "Relogement{" +
                "idRel=" + idRel +
                ", nbFamilleExplusees=" + nbFamilleExplusees +
                ", nbFamillePrestations=" + nbFamillePrestations +
                ", nomPrenomPrestations='" + nomPrenomPrestations + '\'' +
                ", nbFamilleBeneficiaire=" + nbFamilleBeneficiaire +
                ", nomPrenomBeneficiaire='" + nomPrenomBeneficiaire + '\'' +
                ", dateTirageSort=" + dateTirageSort +
                ", lieuPrestation='" + lieuPrestation + '\'' +
                ", relocalisation='" + relocalisation + '\'' +
                ", societeSurveillance=" + societeSurveillance +
                ", fileRapportTirage='" + fileRapportTirage + '\'' +
                ", communiquerDecision=" + communiquerDecision +
                '}';
    }
}
