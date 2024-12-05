package com.example.batimentDU.model;

import com.example.batimentDU.enumeraion.NomArrondissement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idArr")
@JsonIgnoreProperties("annexes")
public class Arrondissement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArr;

    @Enumerated(EnumType.STRING)
    private NomArrondissement nomArrondissement;

    @OneToMany(mappedBy = "arrondissement")
    private List<Annexe> annexes;

    // Constructeurs
    public Arrondissement() {}

    public Arrondissement(NomArrondissement nomArrondissement) {
        this.nomArrondissement = nomArrondissement;
    }

    // Getters
    public Long getIdArr() {
        return idArr;
    }

    public NomArrondissement getNomArrondissement() {
        return nomArrondissement;
    }

    public List<Annexe> getAnnexes() {
        return annexes;
    }

    // Setters
    public void setIdArr(Long idArr) {
        this.idArr = idArr;
    }

    public void setNomArrondissement(NomArrondissement nomArrondissement) {
        this.nomArrondissement = nomArrondissement;
    }

    public void setAnnexes(List<Annexe> annexes) {
        this.annexes = annexes;
    }

    // toString
    @Override
    public String toString() {
        return "Arrondissement{" +
                "idArr=" + idArr +
                ", nomArrondissement=" + nomArrondissement +
                ", annexes=" + annexes +
                '}';
    }
}
