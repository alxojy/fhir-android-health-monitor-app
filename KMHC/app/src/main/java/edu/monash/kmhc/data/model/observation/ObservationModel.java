package edu.monash.kmhc.data.model.observation;

import org.hl7.fhir.r4.model.Observation;

/**
 * Abstract class used by all observations
 */
public abstract class ObservationModel {

    Observation observation;

    public ObservationModel (Observation resource) {
        observation = resource;
    }

    /**
     * Return the Observation value
     * @return double
     */
    public abstract double getValue();

    /**
     * Get the unit type for the Observation
     */
    public abstract String getUnit();
}
