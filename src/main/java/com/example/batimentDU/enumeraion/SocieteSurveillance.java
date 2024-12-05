package com.example.batimentDU.enumeraion;

public enum SocieteSurveillance {
    CASABLANCA,
    EL_OMRANE;

    @Override
    public String toString() {
        switch (this) {
            case CASABLANCA:
                return "Casablanca";
            case EL_OMRANE:
                return "El Omrane";
            default:
                return super.toString();
        }
    }
}
