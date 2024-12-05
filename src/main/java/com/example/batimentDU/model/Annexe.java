package com.example.batimentDU.model;

import com.example.batimentDU.enumeraion.NomAnnexe;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore les champs JSON non reconnus
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idAnn")
public class Annexe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnn;

    @Enumerated(EnumType.STRING)
    private NomAnnexe nomAnnexe;

    @ManyToOne
    @JoinColumn(name = "arrondissement_id", nullable = false)
    private Arrondissement arrondissement;

    @OneToMany(mappedBy = "annexe")
    @JsonIgnore
    private List<Batiment> batiments;

    // Constructeurs
    public Annexe() {}

    public Annexe(NomAnnexe nomAnnexe, Arrondissement arrondissement) {
        this.nomAnnexe = nomAnnexe;
        this.arrondissement = arrondissement;
    }

    // Getters
    public Long getIdAnn() {
        return idAnn;
    }

    public NomAnnexe getNomAnnexe() {
        return nomAnnexe;
    }

    public Arrondissement getArrondissement() {
        return arrondissement;
    }

    public List<Batiment> getBatiments() {
        return batiments;
    }

    // Setters
    public void setIdAnn(Long idAnn) {
        this.idAnn = idAnn;
    }

    public void setNomAnnexe(NomAnnexe nomAnnexe) {
        this.nomAnnexe = nomAnnexe;
    }

    public void setArrondissement(Arrondissement arrondissement) {
        this.arrondissement = arrondissement;
    }

    public void setBatiments(List<Batiment> batiments) {
        this.batiments = batiments;
    }

    // toString
    @Override
    public String toString() {
        return "Annexe{" +
                "idAnn=" + idAnn +
                ", nomAnnexe=" + nomAnnexe +
                ", arrondissement=" + arrondissement +
                ", batiments=" + batiments +
                '}';
    }
}
