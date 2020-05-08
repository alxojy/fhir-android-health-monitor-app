package edu.monash.kmhc.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

/**
 * Initialise service to connect to the FHIR API.
 */
public class FhirService {

    protected IGenericClient client;

    /**
     * Constructor to initialise the FHIR connection.
     */
    public FhirService() {
        FhirContext ctx = FhirContext.forR4();
        // Server url
        String BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/";
        client = ctx.newRestfulGenericClient(BASE_URL);

        // increase timeout
        ctx.getRestfulClientFactory().setConnectTimeout(60*1000);
        ctx.getRestfulClientFactory().setSocketTimeout(60*1000);
    }
}
