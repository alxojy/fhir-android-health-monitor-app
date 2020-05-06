package edu.monash.kmhc.view;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import edu.monash.kmhc.MainActivity;
import edu.monash.kmhc.R;
import edu.monash.kmhc.adapter.SelectPatientsAdapter;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.viewModel.SharedViewModel;

public class SelectPatientsFragment extends Fragment implements SelectPatientsAdapter.OnPatientClickListener{

    private String practitionerID;
    private SelectPatientsFragment thisFrag;
    private Toolbar toolbar;
    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    private SelectPatientsAdapter selectPatientsAdapter;
    private ArrayList<PatientModel> selected_patients = new ArrayList<>();
    private TextView title;

    public SelectPatientsFragment(String practitionerID) {
        this.practitionerID = practitionerID;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        View root = inflater.inflate(R.layout.select_patients_fragment, container, false);

        sharedViewModel.setPractitionerID(practitionerID);

        toolbar = root.findViewById(R.id.home_toolbar);
        title = toolbar.findViewById(R.id.toolbar_title);
        setUpToolBar();

        recyclerView = root.findViewById(R.id.select_patients_recycler_view);
        sharedViewModel.getPatients().observe(getViewLifecycleOwner(),patientUpdatedObserver);
        thisFrag = this;
        return root;
    }

    Observer<HashMap< String, PatientModel >> patientUpdatedObserver = new Observer< HashMap < String, PatientModel >>() {
        @Override
        public void onChanged( HashMap < String, PatientModel > patientHashMap) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            selectPatientsAdapter = new SelectPatientsAdapter(patientHashMap,thisFrag,selected_patients);
            recyclerView.setAdapter(selectPatientsAdapter);
        }
    };

    public void setUpToolBar(){
        title.setText(R.string.title_patient_selection);
        toolbar.inflateMenu(R.menu.select_patients_menu);
        Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (selected_patients.size()>0) {
                    // save list into view model
                    sharedViewModel.setSelectedPatients(selected_patients);
                    Log.d("Select Patient Fragement", "begin monitoring :"+ selected_patients);
                    Log.d("Select Patient Fragement", "Share View Model Status :"+ sharedViewModel.getSelectedPatients().getValue());

                    // TODO: implement call to home fragment
                    // TODO : move call to activity
                    MainActivity main = (MainActivity) getActivity();
                    main.findFragment(MainActivity.home_fragment);
                }
                else{
                    String message = "Please select AT LEAST 1 patient";
                    Context context = getContext();
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                    toast.show();
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(menuItemClickListener);

    }

    private void updateToolbar(){
        String text = selected_patients.size() + " Patients Selected";
        title.setText(text);
    }

    @Override
    public void onPatientClick(boolean checked, PatientModel patient) {
        if (checked) {
            if (!selected_patients.contains(patient)){
            selected_patients.add(patient);
            }
        }
        else if (!checked){
            selected_patients.remove(patient);
        }
        Log.d("Select Patient Fragment", "checked : " + checked + "|  patient " + patient);
        Log.d("Select Patient Fragment", "add/ remove patient"+ selected_patients);
        updateToolbar();
    }
}
