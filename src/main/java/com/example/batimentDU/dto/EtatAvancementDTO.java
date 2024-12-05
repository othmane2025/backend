package com.example.batimentDU.dto;

import com.example.batimentDU.model.RaisonInobservation;

import java.time.LocalDate;

public class EtatAvancementDTO {

    private Long idEtat;                    // ID de l'état d'avancement
    private int nbCorrespondanceAL;         // Nombre de correspondances AL
    private LocalDate dateCorrespondanceAL; // Date de la correspondance AL
    private int nbReponseAL;                // Nombre de réponses AL
    private LocalDate dateReponseAL;        // Date de la réponse AL
    private InformerARDTO informerAR;
    private RaisonInobservationDTO raisonInobservation;

    // Constructeur
    public EtatAvancementDTO(Long idEtat, int nbCorrespondanceAL, LocalDate dateCorrespondanceAL, int nbReponseAL, LocalDate dateReponseAL) {
        this.idEtat = idEtat;
        this.nbCorrespondanceAL = nbCorrespondanceAL;
        this.dateCorrespondanceAL = dateCorrespondanceAL;
        this.nbReponseAL = nbReponseAL;
        this.dateReponseAL = dateReponseAL;
    }

    // Getters et Setters
    public Long getIdEtat() {
        return idEtat;
    }

    public void setIdEtat(Long idEtat) {
        this.idEtat = idEtat;
    }

    public int getNbCorrespondanceAL() {
        return nbCorrespondanceAL;
    }

    public void setNbCorrespondanceAL(int nbCorrespondanceAL) {
        this.nbCorrespondanceAL = nbCorrespondanceAL;
    }

    public LocalDate getDateCorrespondanceAL() {
        return dateCorrespondanceAL;
    }

    public void setDateCorrespondanceAL(LocalDate dateCorrespondanceAL) {
        this.dateCorrespondanceAL = dateCorrespondanceAL;
    }

    public int getNbReponseAL() {
        return nbReponseAL;
    }

    public void setNbReponseAL(int nbReponseAL) {
        this.nbReponseAL = nbReponseAL;
    }

    public LocalDate getDateReponseAL() {
        return dateReponseAL;
    }

    public void setDateReponseAL(LocalDate dateReponseAL) {
        this.dateReponseAL = dateReponseAL;
    }

    public InformerARDTO getInformerAR() {
        return informerAR;
    }

    public void setInformerAR(InformerARDTO informerAR) {
        this.informerAR = informerAR;
    }

    public RaisonInobservationDTO getRaisonInobservation() {
        return raisonInobservation;
    }

    public void setRaisonInobservation(RaisonInobservationDTO raisonInobservation) {
        this.raisonInobservation = raisonInobservation;
    }

    // toString pour une représentation textuelle utile
    @Override
    public String toString() {
        return "EtatAvancementDTO{" +
                "idEtat=" + idEtat +
                ", nbCorrespondanceAL=" + nbCorrespondanceAL +
                ", dateCorrespondanceAL=" + dateCorrespondanceAL +
                ", nbReponseAL=" + nbReponseAL +
                ", dateReponseAL=" + dateReponseAL +
                '}';
    }
}
