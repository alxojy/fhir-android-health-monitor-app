package edu.monash.kmhc.view;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import edu.monash.kmhc.R;
import edu.monash.kmhc.adapter.SelectPatientsAdapter;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.viewModel.SharedViewModel2;

public class SelectPatientsFragment extends Fragment implements SelectPatientsAdapter.OnPatientClickListener{

    private SelectPatientsFragment thisFrag;
    private Toolbar toolbar;
    private SharedViewModel2 sharedViewModel;
    private RecyclerView recyclerView;
    private SelectPatientsAdapter selectPatientsAdapter;
    private ArrayList<PatientModel> selected_patients = new ArrayList<>();
    private TextView title;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel2.class);

        View root = inflater.inflate(R.layout.select_patients_fragment, container, false);

        toolbar = root.findViewById(R.id.home_toolbar);
        title = toolbar.findViewById(R.id.toolbar_title);
        setUpToolBar();

        recyclerView = root.findViewById(R.id.select_patients_recycler_view);
        sharedViewModel.getAllPatientObservations().observe(getViewLifecycleOwner(),patientUpdatedObserver);
        thisFrag = this;
        return root;
    }

    Observer<HashMap< String, PatientModel >> patientUpdatedObserver = new Observer< HashMap < String, PatientModel >>() {
        @Override
        public void onChanged( HashMap < String, PatientModel > patientHashMap) {
            //System.out.println("another");
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            selectPatientsAdapter = new SelectPatientsAdapter(patientHashMap,thisFrag);
            recyclerView.setAdapter(selectPatientsAdapter);
        }
    };

    public void setUpToolBar(){
        title.setText(R.string.title_patient_selection);
        toolbar.inflateMenu(R.menu.select_patients_menu);
        Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // save list into view model
                sharedViewModel.setSelectedPatients(selected_patients);
                System.out.println("in SelectPatientFragment monitoring"+ sharedViewModel.getSelectedPatients().getValue());

                // TODO: implement call to home fragment
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment(),"home_frag")
                        .addToBackStack(null)
                        .commit();

                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(menuItemClickListener);

    }

    public void updateToolbar(){
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
        else{
            selected_patients.remove(patient);
        }
        updateToolbar();
        System.out.println(selected_patients);
}
}
