package com.example.batimentDU.enumeraion;

public enum RecommandationExpertise {
    DEMOLITION_TOTALE,
    DEMOLITION_PARTIELLE,
    RENFORCEMENT_ET_REPARATION,
    RESTAURATION,
    BATIMENT_INTACT;

    @Override
    public String toString() {
        switch (this) {
            case DEMOLITION_TOTALE:
                return "Démolition totale";
            case DEMOLITION_PARTIELLE:
                return "Démolition partielle";
            case RENFORCEMENT_ET_REPARATION:
                return "Renforcement et réparation";
            case RESTAURATION:
                return "Restauration";
            case BATIMENT_INTACT:
                return "Bâtiment intact";
            default:
                return super.toString();
        }
    }
}
