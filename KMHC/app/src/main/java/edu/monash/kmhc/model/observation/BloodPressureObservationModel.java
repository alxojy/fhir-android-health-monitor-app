package edu.monash.kmhc.model.observation;

import org.hl7.fhir.r4.model.Observation;

public class BloodPressureObservationModel implements ObservationModel {

    private Observation observation;

    public BloodPressureObservationModel(Observation observation) {
        this.observation = observation;
    }

    @Override
    public String getValue() {
        return observation.getValueQuantity().getValue().toString();
    }

    public String getSystolic() { return observation.getComponent().get(0).getValueQuantity().getValue().toString(); }

    public String getDiastolic() { return observation.getComponent().get(1).getValueQuantity().getValue().toString(); }

    @Override
    public String getUnit() {
        return observation.getComponent().get(0).getValueQuantity().getUnit();
    }

    @Override
    public String getDateTime() { return observation.getEffectiveDateTimeType().asStringValue(); }
}
