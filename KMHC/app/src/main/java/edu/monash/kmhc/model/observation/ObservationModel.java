package edu.monash.kmhc.model.observation;

/**
 * Abstract class used by all observations
 */
public abstract class ObservationModel {

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
     * @return
     */
    public abstract String getDateTime();
}
