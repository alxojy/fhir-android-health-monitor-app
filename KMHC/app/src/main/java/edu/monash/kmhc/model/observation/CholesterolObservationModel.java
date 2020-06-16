package edu.monash.kmhc.model.observation;

import org.hl7.fhir.r4.model.Observation;

/**
 * Provides observation for cholesterol values
 */
public class CholesterolObservationModel extends ObservationModel {

    public CholesterolObservationModel(Observation observation) {
        super(observation);
    }

    @Override
    public String getValue() {
        return observation.getValueQuantity().getValue().toString();
    }

    @Override
    public String getUnit() {
        return observation.getValueQuantity().getUnit();
    }

    @Override
    public String getDateTime() { return observation.getEffectiveDateTimeType().asStringValue(); }
}
