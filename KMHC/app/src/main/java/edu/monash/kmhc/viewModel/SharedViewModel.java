package edu.monash.kmhc.viewModel;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.model.observation.ObservationModel;
import edu.monash.kmhc.model.observation.ObservationType;
import edu.monash.kmhc.service.repository.ObservationRepositoryFactory;
import edu.monash.kmhc.service.repository.PatientRepository;

/**
 * This class is responsible for providing data that is displayed in the
 * HomeFragment, Settings Fragment and Select Patient Fragment.
 *
 * The class also acts as a Subject in the Observer pattern whereby it updates the patientObservations
 * LiveData by polling the server every N seconds with RxJava.
 *
 * It implements the Poll class and provides its own implementation for polling the server.
 */
public class SharedViewModel extends ViewModel implements Poll {

    // for polling
    private MutableLiveData<HashMap<String, PatientModel>> patientObservations = new MutableLiveData<>();
    // to get all the patients under practitioner
    private MutableLiveData<HashMap<String, PatientModel>> allPatients = new MutableLiveData<>();
    private String practitionerID = "";
    private PatientRepository patientRepository;
    private ObservationRepositoryFactory observationRepositoryFactory;
    private MutableLiveData<String> selectedFrequency = new MutableLiveData<>() ;
    private MutableLiveData<ArrayList<PatientModel>> selectedPatients = new MutableLiveData<>();

    private void initShareViewModel() {
        patientRepository = new PatientRepository(practitionerID);
        observationRepositoryFactory = new ObservationRepositoryFactory();
        fetchAllPatients();
        setSelectedPatients(new ArrayList<>());
        selectedFrequency.setValue("60");
    }

    /**
     * Setter for practitioner ID
     * initialise the share view model after setting the practitioner ID
     * @param practitionerID - Health Practitioner's ID / identifier
     */
    public void setPractitionerID(String practitionerID) {
        this.practitionerID = practitionerID;
        initShareViewModel();
    }

    /**
     * Getter for selected frequency
     * @return the current selected polling frequency
     */
    public LiveData<String> getSelectedFrequency() {
        return selectedFrequency;
    }

    /**
     * Update the selected frequency when the user reselect a new polling frequency
     * @param currentSelected the new chosen frequency
     */
    public void updateCurrentSelected(String currentSelected) {
        this.selectedFrequency.setValue(currentSelected);
    }

    /**
     * Getter for AllPatientObservations (Used for polling)
     * Store patients and their observations in LiveData so that UI will be notified when there are changes.
     * @return LiveData HashMap of patient and their observations
     */
    public LiveData<HashMap<String, PatientModel>> getAllPatientObservations() {
        return patientObservations;
    }

    /**
     * Setter for selected patients
     * This is called whenever user select a new sets of patients to monitor
     * @param selectedPatientsArray the new set of selected patients
     */
    public void setSelectedPatients(ArrayList<PatientModel> selectedPatientsArray) {
        selectedPatients.setValue(selectedPatientsArray);
    }

    /**
     * Getter for AllPatients(Used for patient selection)
     * Store patients in LiveData so that UI will be notified when there are changes.
     * @return  LiveData HashMap of patient
     */
    public LiveData<HashMap<String, PatientModel>> getAllPatients() {
        return allPatients;
    }


    /**
     * Returns the observation reading based on the observation type.
     *
     * This is extensible as different
     * observations in the future only need to specify the type to get the observation reading.
     * @param id patient id
     * @param observationType type of observation. ie. cholesterol
     * @return observation reading for the patient
     */
    private ObservationModel getObservation(String id, ObservationType observationType) {
        return observationRepositoryFactory.getObservationModel(id, observationType);
    }

    /**
     * This method is to fetch all patients that are treated by Health Practitioner
     * from the patient repository.
     */
    private void fetchAllPatients() {
        // run asynchronous tasks on background thread to prevent network on main exception
        HandlerThread backgroundThread = new HandlerThread("Background Thread");
        backgroundThread.start();
        Handler timer = new Handler(backgroundThread.getLooper());

        timer.post(() -> {
            HashMap < String, PatientModel > patientHashMap = new HashMap<>();
            // loop through all patients
            for (PatientModel patient : patientRepository.getAllPatients()) {
                try {
                    // only show patients with cholesterol and bp values
                    for (ObservationType type: ObservationType.values()) {
                        patient.setObservation(type, getObservation(patient.getPatientID(), type));
                        patientHashMap.put(patient.getPatientID(), patient);
                    }
                    patient.addLatestBPReadings(observationRepositoryFactory.getLatestBloodPressureReadings(patient.getPatientID(),5));

                }
                // patient does not have the observation type
                catch (Exception e) {
                    Log.e("Patient ", "No observation type");
                }
            }

            // update LiveData and notify observers - used by select patient
            allPatients.postValue(patientHashMap);
        });

        polling();
    }

    /**
     * This method is responsible for polling the server and updating the observers when the data
     * is updated.
     */
    public void polling() {

        // run asynchronous tasks on background thread to prevent network on main exception
        HandlerThread backgroundThread = new HandlerThread("Background Thread");
        backgroundThread.start();
        Handler timer = new Handler(backgroundThread.getLooper());

        timer.post(new Runnable() {
            @Override
            public void run() {
                HashMap<String, PatientModel> poHashMap = new HashMap<>();

                // loop through all patients. update observations if observation is selected to be monitored
                for (PatientModel patientModel: Objects.requireNonNull(selectedPatients.getValue())) {
                    for (ObservationType type: ObservationType.values()) {
                        if (patientModel.isObservationMonitored(type)) {
                            patientModel.setObservation(type, getObservation(patientModel.getPatientID(), type));
                            poHashMap.put(patientModel.getPatientID(), patientModel);
                            if (type == ObservationType.BLOOD_PRESSURE) {
                                patientModel.addLatestBPReadings(observationRepositoryFactory.getLatestBloodPressureReadings(patientModel.getPatientID(),5));
                            }
                        }
                    }
                }

                // update LiveData and notify observers
                patientObservations.postValue(poHashMap);
                timer.postDelayed(this, Integer.parseInt(Objects.requireNonNull(getSelectedFrequency().getValue()))*1000);
            }});
    }

}
