package edu.monash.kmhc.view.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.service.repository.PatientRepository;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private PatientRepository patientRepository;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        patientRepository = new PatientRepository("606732");
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<ArrayList<PatientModel>> getAllPatients() {
        return patientRepository.getAllPatients();
    }
}