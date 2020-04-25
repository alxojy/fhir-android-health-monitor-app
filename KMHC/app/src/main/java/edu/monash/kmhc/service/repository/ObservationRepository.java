package edu.monash.kmhc.service.repository;

import android.os.AsyncTask;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import edu.monash.kmhc.service.FhirService;

/**
 * This class is responsible as a template for subclasses to return Observation models based on their
 * Observation types. This class should not be instantiated.
 * MutableLiveData is used whereby it is responsible for the Subject role in the Observer pattern.
 */
public abstract class ObservationRepository extends FhirService {

    private IGenericClient client = super.client;
    private Bundle bundle;

    /**
     * Gets the bundle from the FHIR server for Observation.
     * @return observation bundle
     */
    protected Bundle getObservationBundle(String patientId, String ocode) {
        new ObservationRequestAsyncTask(patientId, ocode).execute();
        return bundle;
    }

    /**
     * Class responsible in querying the server in the background. AsyncTask is used to not overload
     * the UI thread.
     */
    public class ObservationRequestAsyncTask extends AsyncTask<Void, Void, Bundle> {

        private String patientId;
        private String ocode;

        public ObservationRequestAsyncTask(String patientId, String code) {
            this.patientId = patientId;
            ocode = code;
        }

        @Override
        protected Bundle doInBackground(Void...voids) {
            return client.search()
                    .forResource(Observation.class)
                    .where(Observation.PATIENT.hasId(patientId))
                    .and(Observation.CODE.exactly().code(ocode))
                    .sort().descending(Observation.DATE)
                    .returnBundle(Bundle.class)
                    .execute();
        }

        //TODO: Does this actually work?
        @Override
        protected void onPostExecute(Bundle observationBundle) {
            super.onPostExecute(observationBundle);
            bundle = observationBundle;
        }
    }
}
