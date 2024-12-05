package com.example.batimentDU.enumeraion;

public enum SituationChefFamille {
    PROPRIETAIRE("مالك"),
    LOCATAIRE("مكتري");

    private final String arabicValue;

    SituationChefFamille(String arabicValue) {
        this.arabicValue = arabicValue;
    }

    public String getArabicValue() {
        return arabicValue;
    }

    // Convert Arabic or Enum Name to Enum
    public static SituationChefFamille fromValue(String value) {
        for (SituationChefFamille situation : values()) {
            if (situation.name().equalsIgnoreCase(value) || situation.getArabicValue().equalsIgnoreCase(value)) {
                return situation;
            }
        }
        throw new IllegalArgumentException("Valeur inconnue pour SituationChefFamille : " + value);
    }

    // Convert Enum to Arabic
    @Override
    public String toString() {
        return this.arabicValue;
    }
}
