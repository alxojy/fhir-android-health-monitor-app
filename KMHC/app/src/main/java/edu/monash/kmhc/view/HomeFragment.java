package edu.monash.kmhc.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import edu.monash.kmhc.MainActivity;
import edu.monash.kmhc.R;
import edu.monash.kmhc.adapter.HomeAdapter;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.viewModel.SharedViewModel;

/**
 * This fragment is used to display the main screen after the health practitioner selected their patients.
 * All patients monitored by the health practitioner will be displayed in this screen.
 */
public class HomeFragment extends Fragment implements HomeAdapter.OnPatientClickListener {

    private HomeFragment thisFrag ;
    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    Toolbar toolbar;


    /**
     * This method performs all graphical initialization, assign all view variables and set up the tool bar.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return The Main UI view that is created.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //set tool bar
        toolbar = root.findViewById(R.id.home_toolbar);
        setUpToolBar();

        recyclerView = root.findViewById(R.id.home_recycler_view);

        sharedViewModel.getAllPatientObservations().observe(getViewLifecycleOwner(), patientUpdatedObserver);

        thisFrag = this;

        return root;
    }

    /**
     * This observer observes for changes in patient
     */
    Observer<HashMap<String, PatientModel>> patientUpdatedObserver = new Observer<HashMap<String, PatientModel>>() {
            @Override
            public void onChanged(HashMap<String, PatientModel> patientObservationHashMap) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                homeAdapter = new HomeAdapter(patientObservationHashMap,thisFrag);
                recyclerView.setAdapter(homeAdapter);
            }
        };

    @Override
    public void onPatientClick(int position, PatientModel patient) {
        // navigate to new fragment
        // pass in the patient
        MainActivity main = (MainActivity) getActivity();
        main.newPatientInfoFragment(MainActivity.patient_info__fragment,patient);
    }

    public void setUpToolBar(){
        toolbar.setTitle("Home Page");
        toolbar.inflateMenu(R.menu.home_menu);

        Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MainActivity main = (MainActivity) getActivity();
                main.findFragment(MainActivity.select_patients_fragment);
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
    }
}


