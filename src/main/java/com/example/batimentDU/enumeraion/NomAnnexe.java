package com.example.batimentDU.enumeraion;

public enum NomAnnexe {

    ANNEXE_56("56"),
    ANNEXE_57("57"),
    ANNEXE_57Bis("57Bis"),
    ANNEXE_58("58"),
    ANNEXE_58Bis("58Bis"),
    ANNEXE_59("59"),
    ANNEXE_60("60"),
    ANNEXE_60Bis("60Bis"),
    ANNEXE_61("61"),
    ANNEXE_62("62"),
    ANNEXE_SAB("SAB");

    private final String numero;

    NomAnnexe(String numero) {
        this.numero = numero;
    }

    // Méthode pour récupérer une valeur NomAnnexe par numéro
    public static NomAnnexe fromNumero(String numero) {
        for (NomAnnexe nomAnnexe : NomAnnexe.values()) {
            if (nomAnnexe.numero.equals(numero)) {
                return nomAnnexe;
            }
        }
        throw new IllegalArgumentException("Aucun nom d'annexe trouvé pour le numéro : " + numero);
    }

    @Override
    public String toString() {
        return numero; // Retourne seulement le numéro.
    }
}
