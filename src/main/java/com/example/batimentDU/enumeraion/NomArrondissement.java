package com.example.batimentDU.enumeraion;

public enum NomArrondissement {
    BenMsick("ابن مسيك"),
    Sbata("سباتة");

    private final String nomArabe;

    NomArrondissement(String nomArabe) {
        this.nomArabe = nomArabe;
    }

    public String getNomArabe() {
        return nomArabe;
    }

    public static NomArrondissement fromNomArabe(String nomArabe) {
        for (NomArrondissement arrondissement : values()) {
            if (arrondissement.getNomArabe().equals(nomArabe)) {
                return arrondissement;
            }
        }
        throw new IllegalArgumentException("Nom d'arrondissement invalide : " + nomArabe);
    }
}
