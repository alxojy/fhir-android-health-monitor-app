package edu.monash.kmhc.viewModel;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.model.observation.ObservationModel;
import edu.monash.kmhc.model.observation.ObservationType;
import edu.monash.kmhc.service.repository.ObservationRepositoryFactory;
import edu.monash.kmhc.service.repository.PatientRepository;
//TODO: comment this classss

/**
 * This class is responsible for providing data that is displayed in the HomeFragment.
 * The class also acts as a Subject in the Observer pattern whereby it updates the patientObservations
 * LiveData by polling the server every N seconds with RxJava. It implements the Poll class and
 * provides its own implementation for polling the server.
 */
public class SharedViewModel extends ViewModel implements Poll {

    // for polling
    private MutableLiveData<HashMap<String, PatientModel>> patientObservations = new MutableLiveData<>();
    // to get all the patients under practitioner
    private MutableLiveData<HashMap<String, PatientModel>> patients = new MutableLiveData<>();
    private String practitionerID = "";
    private PatientRepository patientRepository;
    private ObservationRepositoryFactory observationRepositoryFactory;
    private MutableLiveData<String> selectedFrequency = new MutableLiveData<>() ;
    private MutableLiveData<ArrayList<PatientModel>> selectedPatients = new MutableLiveData<>();
    private int frequency;

    private ArrayList<Integer> counters = new ArrayList<>();
    private int pointer = 0;


    private void initShareViewModel(){
        Log.d("Shared View Model" ," initialising Shared View Model");
        patientRepository = new PatientRepository(practitionerID);
        observationRepositoryFactory = new ObservationRepositoryFactory();
        getAllPatients();
        setSelectedPatients(new ArrayList<>());
        frequency = 10000; // default

        // TODO: Remove this
        counters.add(0);
        counters.add(0);
        counters.add(0);
        counters.add(0);
        counters.add(0);
    }

    public void setPractitionerID(String practitionerID) {
        this.practitionerID = practitionerID;
        initShareViewModel();
    }

    public LiveData<String> getSelectedFrequency() {
        return selectedFrequency;
    }

    public void updateCurrentSelected(String currentSelected) {
        this.selectedFrequency.setValue(currentSelected);
        frequency = Integer.parseInt(currentSelected.replace(" seconds","")) * 1000;
    }

    /**
     * Store patients and their observations in LiveData so that UI will be notified when there are changes.
     * @return LiveData HashMap of patient and their observations
     */
    public LiveData<HashMap<String, PatientModel>> getAllPatientObservations() {
        return patientObservations;
    }

    public void setSelectedPatients(ArrayList<PatientModel> selectedPatientsArray) {
        Log.d("Shared View Model","Attempting to update to" + selectedPatientsArray);
        // TODO: delete try catch and remove pointer
        try {
            selectedPatients.setValue(selectedPatientsArray);
            pointer ++;
        }
        catch(Exception ie) {
            System.out.println(ie.getMessage());
        }
    }

    private LiveData<ArrayList<PatientModel>> getSelectedPatients() {
        return selectedPatients;
    }

    public LiveData<HashMap<String, PatientModel>> getPatients() {
        return patients;
    }


    private void getAllPatients() {
        // run asynchronous tasks on background thread to prevent network on main exception
        HandlerThread backgroundThread = new HandlerThread("Background Thread");
        backgroundThread.start();
        Handler timer = new Handler(backgroundThread.getLooper());

        timer.post(() -> {
            HashMap < String, PatientModel > patientHashMap = new HashMap<>();
            // loop through all patients
            for (PatientModel patient : patientRepository.getAllPatients()) {
                patientHashMap.put(patient.getPatientID(), patient);
            }

            // update LiveData and notify observers
            // used by select patient
            patients.postValue(patientHashMap);
        });

        polling();
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
     * This method is responsible for polling the server and updating the observers when the data
     * is updated.
     */
    public void polling() {

        // run asynchronous tasks on background thread to prevent network on main exception
        HandlerThread backgroundThread = new HandlerThread("Background Thread");
        backgroundThread.start();
        Handler timer = new Handler(backgroundThread.getLooper());


        // TODO: remove try catch and counter
        timer.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("SHared View Model", "Polling new round" +  counters);
                    counters.set(pointer,counters.get(pointer)+1);
                    HashMap<String, PatientModel> poHashMap = new HashMap<>();

                    // loop through all patients
                    for (PatientModel patient : getSelectedPatients().getValue()) {
                        try {
                            // set new cholesterol observation reading
                            patient.setObservation(ObservationType.CHOLESTEROL,
                                    getObservation(patient.getPatientID(), ObservationType.CHOLESTEROL));
                            poHashMap.put(patient.getPatientID(), patient);
                        }
                        // patient does not have the observation type
                        catch (Exception e) {
                            Log.e("Patient " + patient.getPatientID(), "No observation type");
                        }
                    }


                    Log.d("SHared View Model", "Ended Polling");

                    // update LiveData and notify observers
                    patientObservations.postValue(poHashMap);
                    timer.postDelayed(this, frequency);
                }
                catch(Exception ei){
                    System.out.println(ei.getMessage());
                }
            }});
    }

}
