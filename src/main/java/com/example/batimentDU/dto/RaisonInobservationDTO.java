package com.example.batimentDU.dto;

public class RaisonInobservationDTO {
    private Long idRaison;
    private String nomRai;

    public RaisonInobservationDTO() {
    }

    public RaisonInobservationDTO(Long idRaison, String nomRai) {
        this.idRaison = idRaison;
        this.nomRai = nomRai;
    }

    // Getters
    public Long getIdRaison() {
        return idRaison;
    }

    public String getNomRai() {
        return nomRai;
    }

    // Setters
    public void setIdRaison(Long idRaison) {
        this.idRaison = idRaison;
    }

    public void setNomRai(String nomRai) {
        this.nomRai = nomRai;
    }

    // toString
    @Override
    public String toString() {
        return "RaisonInobservationDTO{" +
                "idRaison=" + idRaison +
                ", nomRai='" + nomRai + '\'' +
                '}';
    }
}