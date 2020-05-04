package edu.monash.kmhc.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.model.observation.ObservationModel;
import edu.monash.kmhc.model.observation.ObservationType;
import edu.monash.kmhc.service.repository.ObservationRepositoryFactory;
import edu.monash.kmhc.service.repository.PatientRepository;
import io.reactivex.Observable;

/**
 * This class is responsible for providing data that is displayed in the HomeFragment.
 * The class also acts as a Subject in the Observer pattern whereby it updates the patientObservations
 * LiveData by polling the server every N seconds with RxJava. It implements the Poll class and
 * provides its own implementation for polling the server.
 */
public class SharedViewModel extends ViewModel implements Poll {

    private PatientRepository patientRepository;
    private ObservationRepositoryFactory observationRepositoryFactory;
    public MutableLiveData<HashMap<PatientModel, ObservationModel>> patientObservations = new MutableLiveData<>();
    private int frequency;
    private MutableLiveData<String> currentSelected = new MutableLiveData<>() ;

    public LiveData<String> getCurrentSelected() {
        return currentSelected;
    }

    public void updateCurrentSelected(String currentSelected) {
        this.currentSelected.setValue(currentSelected);
        frequency = Integer.parseInt(currentSelected.replace(" seconds","")) * 1000;
        System.out.println(frequency);
        System.out.println(currentSelected);
    }

    public SharedViewModel() {
        //TODO: Change practitioner
        patientRepository = new PatientRepository("606732");
        observationRepositoryFactory = new ObservationRepositoryFactory();

        if (currentSelected.getValue() == null){
            currentSelected.setValue("20 seconds");
            frequency = 20000; //default frequency
        }
        polling(frequency);
    }

    /**
     * Store patients and their observations in LiveData so that UI will be notified when there are changes.
     * @return LiveData HashMap of patient and their observations
     */
    public LiveData<HashMap<PatientModel, ObservationModel>> getPatientMutableLiveData() {
        return patientObservations;
    }

    public void polling(int frequency) {
        Observable.interval(0, frequency, TimeUnit.MILLISECONDS)
                .map(tick -> {
                            HashMap<PatientModel, ObservationModel> poHashMap = new HashMap<>();
                            for (PatientModel patient : getAllPatients()) {
                                for (ObservationType observationType : ObservationType.values()) {
                                    System.out.println(getObservation(patient.getPatientID(), observationType));
                                    poHashMap.put(patient, getObservation(patient.getPatientID(), observationType));
                                }
                            }
                            patientObservations.postValue(poHashMap);
                            return patientObservations;
                        }).subscribe();
    }

    private ArrayList<PatientModel> getAllPatients() {
        return patientRepository.getAllPatients();
    }

    private ObservationModel getObservation(String id, ObservationType observationType) {
        return observationRepositoryFactory.getObservationModel(id, observationType);
    }
}