package edu.monash.kmhc.data.repository.request;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;

import edu.monash.kmhc.data.model.observation.CholesterolObservationModel;
import edu.monash.kmhc.data.repository.FhirSource;

/**
 * This class is responsible for getting the cholesterol observations from the FHIR server.
 */
public class CholesterolSourceRequest extends FhirSource {

    // code for cholesterol
    final private String CODE = "2093-3";
    private String patientId;

    public CholesterolSourceRequest(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Returns the cholesterol observation model from the server
     * @return Cholesterol measurement
     */
    public CholesterolObservationModel getModel() {
        return new CholesterolObservationModel((Observation) getObservationBundle(patientId, CODE)
                .getEntry().get(0).getResource());
    }
}
