package edu.monash.kmhc.model.observation;

import org.hl7.fhir.r4.model.Observation;

/**
 * Abstract class used by all observations
 */
public abstract class ObservationModel {

    Observation observation;

    ObservationModel(Observation observation) {
        this.observation = observation;
    }

    /**
     * Return the Observation value
     * @return double
     */
    public abstract String getValue();

    /**
     * Get the unit type for the Observation
     */
    public abstract String getUnit();

    /**
     * Get the effective DateTime
     * @return effective date time
     */
    public abstract String getDateTime();
}
