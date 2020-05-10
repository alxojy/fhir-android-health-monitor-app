package edu.monash.kmhc.model.observation;

/**
 * Abstract class used by all observations
 */
public interface ObservationModel {

    /**
     * Return the Observation value
     * @return double
     */
    String getValue();

    /**
     * Get the unit type for the Observation
     */
    String getUnit();

    /**
     * Get the effective DateTime
     * @return effective date time
     */
    String getDateTime();
}
