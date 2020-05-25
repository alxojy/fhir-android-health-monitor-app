package edu.monash.kmhc.service.repository;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import edu.monash.kmhc.model.observation.BloodPressureObservationModel;
import edu.monash.kmhc.model.observation.CholesterolObservationModel;
import edu.monash.kmhc.model.observation.ObservationModel;
import edu.monash.kmhc.model.observation.ObservationType;
import edu.monash.kmhc.service.FhirService;

/**
 * This class is responsible for instantiating other Observation models using the Factory design pattern.
 *
 * The ObservationRepositoryFactory creates different types of ObservationModel objects.
 * Currently there is only cholesterol but it can be made extensible whereby in the future when more
 * observation models have to be added, only the create[ObservationTypeModel] method have to be
 * create and getObservationModel method have to be modified to instantiate the different observation
 * types.
 */
public class ObservationRepositoryFactory extends FhirService {

    private IGenericClient client = super.client;

    /**
     * Gets the bundle from the FHIR server for Observation.
     * @return observation bundle
     */
    private Observation getObservation(String patientId, String code) {
        Bundle bundle = client.search()
                .forResource(Observation.class)
                .where(Observation.PATIENT.hasId(patientId))
                .and(Observation.CODE.exactly().code(code))
                .sort().descending(Observation.DATE)
                .returnBundle(Bundle.class)
                .execute();

        return (Observation) (bundle.getEntry().get(0)).getResource();
    }

    public ObservationModel getObservationModel(String patientId, ObservationType type) {
        switch (type) {
            case CHOLESTEROL:
                return createCholesterolModel(patientId);
            case BLOOD_PRESSURE:
                return createBloodPressureModel(patientId);
            default:
                throw new IllegalArgumentException("Observation type invalid");
        }
    }

    private CholesterolObservationModel createCholesterolModel(String patientId) {
        return new CholesterolObservationModel(getObservation(patientId, ObservationType.CHOLESTEROL.getObservationCode()));
    }

    private BloodPressureObservationModel createBloodPressureModel(String patientId) {
        return new BloodPressureObservationModel(getObservation(patientId, ObservationType.BLOOD_PRESSURE.getObservationCode()));
    }
}
