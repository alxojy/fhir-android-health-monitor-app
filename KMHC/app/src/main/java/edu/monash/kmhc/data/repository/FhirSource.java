package edu.monash.kmhc.data.repository;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import edu.monash.kmhc.data.model.observation.ObservationModel;

/**
 * Initialise service to connect to the FHIR API.
 */
public class FhirSource {

    protected IGenericClient client;

    /**
     * Constructor to initialise the FHIR connection.
     */
    public FhirSource() {
        FhirContext ctx = FhirContext.forR4();
        // Server url
        String BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/";
        client = ctx.newRestfulGenericClient(BASE_URL);
        /* Prevent connection from cutting off */
        ctx.getRestfulClientFactory().setConnectTimeout(60 * 1000);
        ctx.getRestfulClientFactory().setSocketTimeout(60 * 1000);
    }

    /**
     * Gets the bundle from the FHIR server for Observation.
     * @param patientId patient id
     * @param ocode code of the observation. ie. 2093-3 for cholesterol
     * @return observation bundle
     */
    protected Bundle getObservationBundle(String patientId, String ocode) {
        return this.client.search()
                .forResource(Observation.class)
                .where(Observation.PATIENT.hasId(patientId))
                .and(Observation.CODE.exactly().code(ocode))
                .sort().descending(Observation.DATE)
                .returnBundle(Bundle.class)
                .execute();
    }
}
