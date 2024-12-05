package com.example.batimentDU.dto;

import com.example.batimentDU.enumeraion.SituationChefFamille;
import com.example.batimentDU.enumeraion.SituationExploitant;

public class BatimentDTO {

    private Long idBat;
    private String adresse;
    private double surface;
    private String NBetage;
    private String nomPrenomProprietaire;
    private int nbFamilleResident;
    private String nomPrenomResident;
    private String situationChefFamille;
    private int nbMagasin;
    private String nomPrenomExploitantMagasin;
    private String situationExploitant;
    private String filetelechargerImage;
    private AppercuRapportDTO appercuRapport;

    private StatutActuelDTO statut;

    // Constructeur avec tous les champs
    public BatimentDTO(Long idBat, String adresse, double surface, String NBetage,
                       String nomPrenomProprietaire, int nbFamilleResident,
                       String nomPrenomResident, String situationChefFamille,
                       int nbMagasin, String nomPrenomExploitantMagasin,
                       String situationExploitant, String filetelechargerImage) {
        this.idBat = idBat;
        this.adresse = adresse;
        this.surface = surface;
        this.NBetage = NBetage;
        this.nomPrenomProprietaire = nomPrenomProprietaire;
        this.nbFamilleResident = nbFamilleResident;
        this.nomPrenomResident = nomPrenomResident;
        this.situationChefFamille = situationChefFamille;
        this.nbMagasin = nbMagasin;
        this.nomPrenomExploitantMagasin = nomPrenomExploitantMagasin;
        this.situationExploitant = situationExploitant;
        this.filetelechargerImage = filetelechargerImage;
    }

    public BatimentDTO() {

    }

    public Long getIdBat() {
        return idBat;
    }

    public void setIdBat(Long idBat) {
        this.idBat = idBat;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public String getNBetage() {
        return NBetage;
    }

    public void setNBetage(String NBetage) {
        this.NBetage = NBetage;
    }

    public String getNomPrenomProprietaire() {
        return nomPrenomProprietaire;
    }

    public void setNomPrenomProprietaire(String nomPrenomProprietaire) {
        this.nomPrenomProprietaire = nomPrenomProprietaire;
    }

    public int getNbFamilleResident() {
        return nbFamilleResident;
    }

    public void setNbFamilleResident(int nbFamilleResident) {
        this.nbFamilleResident = nbFamilleResident;
    }

    public String getNomPrenomResident() {
        return nomPrenomResident;
    }

    public void setNomPrenomResident(String nomPrenomResident) {
        this.nomPrenomResident = nomPrenomResident;
    }

    public String getSituationChefFamille() {
        return situationChefFamille;
    }

    public void setSituationChefFamille(String situationChefFamille) {
        this.situationChefFamille = situationChefFamille;
    }

    public int getNbMagasin() {
        return nbMagasin;
    }

    public void setNbMagasin(int nbMagasin) {
        this.nbMagasin = nbMagasin;
    }

    public String getNomPrenomExploitantMagasin() {
        return nomPrenomExploitantMagasin;
    }

    public void setNomPrenomExploitantMagasin(String nomPrenomExploitantMagasin) {
        this.nomPrenomExploitantMagasin = nomPrenomExploitantMagasin;
    }

    public String getSituationExploitant() {
        return situationExploitant;
    }

    public void setSituationExploitant(String situationExploitant) {
        this.situationExploitant = situationExploitant;
    }

    public String getFiletelechargerImage() {
        return filetelechargerImage;
    }

    public void setFiletelechargerImage(String filetelechargerImage) {
        this.filetelechargerImage = filetelechargerImage;
    }

    public AppercuRapportDTO getAppercuRapport() {
        return appercuRapport;
    }

    public void setAppercuRapport(AppercuRapportDTO appercuRapport) {
        this.appercuRapport = appercuRapport;
    }

    public StatutActuelDTO getStatut() {
        return statut;
    }

    public void setStatut(StatutActuelDTO statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "BatimentDTO{" +
                "idBat=" + idBat +
                ", adresse='" + adresse + '\'' +
                ", surface=" + surface +
                ", NBetage='" + NBetage + '\'' +
                ", nomPrenomProprietaire='" + nomPrenomProprietaire + '\'' +
                ", nbFamilleResident=" + nbFamilleResident +
                ", nomPrenomResident='" + nomPrenomResident + '\'' +
                ", situationChefFamille='" + situationChefFamille + '\'' +
                ", nbMagasin=" + nbMagasin +
                ", nomPrenomExploitantMagasin='" + nomPrenomExploitantMagasin + '\'' +
                ", situationExploitant='" + situationExploitant + '\'' +
                ", filetelechargerImage='" + filetelechargerImage + '\'' +
                '}';
    }
}