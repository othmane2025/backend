package com.example.batimentDU.enumeraion;

public enum RecommandationDecision {
    DEMOLITION_TOTALE,
    DEMOLITION_PARTIELLE,
    RENFORCEMENT_ET_REPARATION,
    RESTAURATION,
    VIDANGE_INSTANTANEE;

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
            case VIDANGE_INSTANTANEE:
                return "Vidange instantanée";
            default:
                return super.toString();
        }
    }
}
