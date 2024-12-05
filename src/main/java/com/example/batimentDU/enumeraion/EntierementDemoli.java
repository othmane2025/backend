package com.example.batimentDU.enumeraion;

public enum EntierementDemoli {
    PROPRIETAIRE,
    ENTREPRISE;

    @Override
    public String toString() {
        switch (this) {
            case PROPRIETAIRE:
                return "Entièrement démoli par le propriétaire";
            case ENTREPRISE:
                return "Entièrement démoli par l'entreprise";
            default:
                return super.toString();
        }
    }
}

