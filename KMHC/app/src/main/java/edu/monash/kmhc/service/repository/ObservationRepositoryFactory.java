package edu.monash.kmhc.service.repository;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;

import java.util.ArrayList;

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
 * It can be made extensible whereby in the future when more observation models have to be added, 
 * only the create[ObservationTypeModel] method have to be created and getObservationModel method 
 * have to be modified to instantiate the different observation types.
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

    /**
     * Returns an ArrayList of n latest readings
     * @param patientId patient id
     * @param code observation code
     * @param n latest n readings
     * @return ArrayList of readings
     */
    private ArrayList<Observation> getLatestObservationReadings(String patientId, String code, int n) {
        ArrayList<Observation> latestReadings = new ArrayList<>();
        Bundle bundle = client.search()
                .forResource(Observation.class)
                .where(Observation.PATIENT.hasId(patientId))
                .and(Observation.CODE.exactly().code(code))
                .sort().descending(Observation.DATE)
                .returnBundle(Bundle.class)
                .execute();

        for (int i = 0; i < Math.min(n, bundle.getTotal()); i++) {
            latestReadings.add((Observation) (bundle.getEntry().get(i)).getResource());
        }
        return latestReadings;
    }

    public ArrayList<BloodPressureObservationModel> getLatestBloodPressureReadings(String patientId, int n) {
        ArrayList<BloodPressureObservationModel> bpReadings = new ArrayList<>();
        for (Observation observation: getLatestObservationReadings(patientId, ObservationType.BLOOD_PRESSURE.getObservationCode(), n)) {
            bpReadings.add(new BloodPressureObservationModel(observation));
        }
        return bpReadings;
    }

}
