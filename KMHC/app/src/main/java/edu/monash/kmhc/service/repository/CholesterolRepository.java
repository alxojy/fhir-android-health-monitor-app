package edu.monash.kmhc.service.repository;

import androidx.lifecycle.MutableLiveData;

import org.hl7.fhir.r4.model.Observation;

import edu.monash.kmhc.model.observation.CholesterolObservationModel;

/**
 * This class is responsible for getting the cholesterol observations from the FHIR server.
 * MutableLiveData is used whereby it is responsible for the Subject role in the Observer pattern.
 */
public class CholesterolRepository extends ObservationRepository {

    // code for cholesterol
    final private String CODE = "2093-3";
    private String patientId;

    public CholesterolRepository(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Returns the cholesterol observation model from the server
     * @return Cholesterol measurement
     */
    public MutableLiveData<CholesterolObservationModel> getLatest() {
        MutableLiveData<CholesterolObservationModel> cholesterolModel = new MutableLiveData<>();
        cholesterolModel.setValue(new CholesterolObservationModel((Observation) getObservationBundle(patientId, CODE)
                .getEntry().get(0).getResource()));

        return cholesterolModel;
    }
}
