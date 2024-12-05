package com.example.batimentDU.model;

import com.example.batimentDU.enumeraion.SituationChefFamille;
import com.example.batimentDU.enumeraion.SituationExploitant;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idBat")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Batiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBat;

    private String adresse;
    private double surface;
    private String NBetage;
    private String nomPrenomProprietaire;
    private int nbFamilleResident;
    private String nomPrenomResident;

    @Column(name = "situation_chef_famille")
    private String situationChefFamille; // Stocke la valeur arabe directement

    private int nbMagasin;
    private String nomPrenomExploitantMagasin;

    @Column(name = "situation_exploitant")
    private String situationExploitant; // Stocke la valeur arabe directement

    private String filetelechargerImage;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "annexe_id")
    private Annexe annexe;

    @OneToOne(mappedBy = "batiment", cascade = CascadeType.MERGE)
    private StatutActuel statutActuel;

    @OneToOne(mappedBy = "batiment", cascade = CascadeType.ALL)
    @JsonManagedReference
    private AppercuRapport appercuRapport;


    // Constructeurs
    public Batiment() {}

    public Batiment(String adresse, double surface, String NBetage, String nomPrenomProprietaire, int nbFamilleResident, String nomPrenomResident, String situationChefFamille, int nbMagasin, String nomPrenomExploitantMagasin, String situationExploitant, String filetelechargerImage, Annexe annexe, AppercuRapport appercuRapport) {
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
        this.annexe = annexe;
        this.appercuRapport = appercuRapport;
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

    public Annexe getAnnexe() {
        return annexe;
    }

    public void setAnnexe(Annexe annexe) {
        this.annexe = annexe;
    }

    public StatutActuel getStatutActuel() {
        return statutActuel;
    }

    public void setStatutActuel(StatutActuel statutActuel) {
        this.statutActuel = statutActuel;
    }

    public AppercuRapport getAppercuRapport() {
        return appercuRapport;
    }

    public void setAppercuRapport(AppercuRapport appercuRapport) {
        this.appercuRapport = appercuRapport;
    }

    @Override
    public String toString() {
        return "Batiment{" +
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
                ", annexe=" + annexe +
                ", statutActuel=" + statutActuel +
                ", appercuRapport=" + appercuRapport +
                '}';
    }
}
