package com.example.batimentDU.enumeraion;

public enum PartiellementDemoli {
    PROPRIETAIRE,
    ENTREPRISE;

    @Override
    public String toString() {
        switch (this) {
            case PROPRIETAIRE:
                return "Partiellement démoli par le propriétaire";
            case ENTREPRISE:
                return "Partiellement démoli par l'entreprise";
            default:
                return super.toString();
        }
    }
}

