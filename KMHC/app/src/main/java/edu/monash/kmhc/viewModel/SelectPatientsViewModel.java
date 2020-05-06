package edu.monash.kmhc.viewModel;

import android.os.Handler;
import android.os.HandlerThread;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.service.repository.PatientRepository;

public class SelectPatientsViewModel extends ViewModel {
    private PatientRepository patientRepository;
    //patientObservations
    private MutableLiveData< HashMap < String, PatientModel >> patients = new MutableLiveData<>();
    private MutableLiveData<ArrayList<PatientModel>> selectedPatients = new MutableLiveData<>();

    public SelectPatientsViewModel() {
        patientRepository = new PatientRepository("3656083");
        getAllPatients();
    }

    public LiveData< HashMap < String, PatientModel >> getPatients() {
        return patients;
    }

    public void setSelectedPatients(ArrayList<PatientModel> selected_patients_array) {
        selectedPatients.setValue(selected_patients_array);
    }

    public LiveData<ArrayList<PatientModel>> getSelectedPatients() {
        return selectedPatients;
    }

    public ArrayList<PatientModel> sh(){
        return getSelectedPatients().getValue();
    }

    public void getAllPatients() {
        // run asynchronous tasks on background thread to prevent network on main exception
        HandlerThread backgroundThread = new HandlerThread("Background Thread");
        backgroundThread.start();
        Handler timer = new Handler(backgroundThread.getLooper());


        timer.post(new Runnable() {
            @Override
            public void run() {
                HashMap < String, PatientModel > patientHashMap = new HashMap<>();
                // loop through all patients
              for (PatientModel patient : patientRepository.getAllPatients()) {
                    patientHashMap.put(patient.getPatientID(), patient);
                }

                // update LiveData and notify observers
                patients.postValue(patientHashMap);
            }
        });
    }
}