package edu.monash.kmhc.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.monash.kmhc.model.PatientModel;

public class PatientInfoViewModel extends ViewModel {
    private MutableLiveData<PatientModel> patient;

    public PatientInfoViewModel() {
        patient = new MutableLiveData<>();
    }

    public LiveData<PatientModel> getPatient() {
        return patient;
    }

    public void setPatient(PatientModel patient) {
        this.patient.setValue(patient);
    }
}
