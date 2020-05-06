package edu.monash.kmhc.view;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
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
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel2.class);

        View root = inflater.inflate(R.layout.select_patients_fragment, container, false);

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
            //System.out.println("another");
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


//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "home_frag")
//                            .addToBackStack(null)
//                            .commit();

                    String tag = "home_fragment";
                    Fragment home_frag = getActivity().getSupportFragmentManager().findFragmentByTag(tag);
                    if ( home_frag == null){
                        Fragment selectedFragment = new HomeFragment();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,selectedFragment,tag)
                                .addToBackStack(null)
                                .commit();
                    }
                    else{
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,home_frag,tag)
                                .commit();
                    }
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
        else if (!checked){
            selected_patients.remove(patient);
        }
        Log.d("Select Patient Fragment", "checked : " + checked + "|  patient " + patient);
        Log.d("Select Patient Fragment", "add/ remove patient"+ selected_patients);
        updateToolbar();
    }
}
