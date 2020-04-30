package edu.monash.kmhc.model.observation;

public enum ObservationType {
    CHOLESTEROL;

    private String observationCode;

    static {
        CHOLESTEROL.observationCode = "2093-3";
    }

    public String getObservationCode() {
        return observationCode;
    }
}
