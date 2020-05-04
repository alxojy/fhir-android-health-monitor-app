package edu.monash.kmhc.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.HashMap;

import edu.monash.kmhc.R;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.model.observation.ObservationModel;
import edu.monash.kmhc.model.observation.ObservationType;
import edu.monash.kmhc.viewModel.HomeViewModel;

/**
 * This fragment is used to display the main home screen upon login.
 * All patients monitored by the health practitioner will be displayed in this screen.
 * TODO: Select, add, remove patient functionality.
 */
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textView = root.findViewById(R.id.text_home);

        homeViewModel.getAllPatientObservations().observe(getViewLifecycleOwner(), new Observer<HashMap<String, PatientModel>>() {
            @Override
            public void onChanged(HashMap<String, PatientModel> patientObservationHashMap) {
                System.out.println("another");
                patientObservationHashMap.forEach((p,o) -> {
                    textView.setText(o.getName() + " " + o.getObservationReading(ObservationType.CHOLESTEROL).getValue());
                });
            }
        });
            return root;
    }

}
