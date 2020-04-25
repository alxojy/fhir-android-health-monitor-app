package edu.monash.kmhc.model.observation;

import org.hl7.fhir.r4.model.Observation;

/**
 * Provides observation for cholesterol values
 */
public class CholesterolObservationModel extends ObservationModel {

    public CholesterolObservationModel(Observation resource) {
        super(resource);
    }

    @Override
    public double getValue() {
        return observation.getValueQuantity().getValue().doubleValue();
    }

    @Override
    public String getUnit() {
        return observation.getValueQuantity().getUnit();
    }
}
