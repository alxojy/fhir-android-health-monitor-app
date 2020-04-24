package edu.monash.kmhc.view.patientInfo;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.monash.kmhc.R;

/**
 * This fragment is used to display extra information about the patient.
 * Extra information about patient: birth date, gender and address (city, state, country)
 * TODO: Display required information & get info from the model
 */
public class PatientInfoFragment extends Fragment {

    private PatientInfoViewModel mViewModel;

    public static PatientInfoFragment newInstance() {
        return new PatientInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.patient_info_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PatientInfoViewModel.class);
        // TODO: Use the ViewModel
    }

}
