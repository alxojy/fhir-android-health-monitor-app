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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edu.monash.kmhc.MainActivity;
import edu.monash.kmhc.R;
import edu.monash.kmhc.adapter.SelectPatientsAdapter;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.viewModel.SharedViewModel;

/**
 * This fragment is used to display a list of all patients under Health Practitioner
 * It also allow the Health Practitioner to select / deselect the patients that he/she
 * wants to monitor.
 */

public class SelectPatientsFragment extends Fragment implements SelectPatientsAdapter.OnPatientClickListener{

    private String practitionerID;
    private SelectPatientsFragment thisFrag; // a reference to this fragment
    private Toolbar toolbar;
    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    private ArrayList<PatientModel> selectedPatients = new ArrayList<>();
    private TextView title;
    private TextView loadingTextView;
    private ImageButton backButton;
    private ProgressBar loadingSpinner;

    /**
     * Constructor
     * @param practitionerID the Health Practitioner's ID
     */
    public SelectPatientsFragment(String practitionerID) {
        this.practitionerID = practitionerID;
    }

    /**
     * This method performs all graphical initialization,
     * assign all view variables and set up the tool bar.
     * @return the UI view that is created.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);

        View root = inflater.inflate(R.layout.select_patients_fragment, container, false);

        // find all the graphical components
        recyclerView = root.findViewById(R.id.select_patients_recycler_view);
        backButton = root.findViewById(R.id.btn_back);
        toolbar = root.findViewById(R.id.select_patients_toolbar);
        title = toolbar.findViewById(R.id.toolbar_title);
        loadingTextView = root.findViewById(R.id.select_patients_txt_loading);
        loadingSpinner = root.findViewById(R.id.select_patients_progressBar);
        loadingSpinner.setVisibility(View.VISIBLE);
        loadingTextView.setVisibility(View.VISIBLE);

        sharedViewModel.setPractitionerID(practitionerID);
        setUpToolBar();

        //update UI when there's new patient under Health Practitioner
        sharedViewModel.getAllPatients().observe(getViewLifecycleOwner(),patientUpdatedObserver);

        thisFrag = this;
        return root;
    }

    /**
     * This observer observes for changes in patient list
     * ( The list that contains all patients under Health Practitioner )
     * It displays a loading screen when the app is still fetching data from the server,
     * It immediately updates the UI to display the patients when the system completes
     * the data fetching process.
     */
    private Observer<HashMap< String, PatientModel >> patientUpdatedObserver = new Observer< HashMap < String, PatientModel >>() {
        @Override
        public void onChanged( HashMap < String, PatientModel > patientHashMap) {
            if (patientHashMap.size() == 0 ){
                loadingTextView.setText(R.string.zero_patients_message);
            }
            else{
                loadingSpinner.setVisibility(View.GONE);
                loadingTextView.setVisibility(View.GONE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter( new SelectPatientsAdapter(patientHashMap,thisFrag, selectedPatients));
        }
        }
    };

    /**
     * This method is responsible to set up the tool bar in the SelectPatientFragment
     * It performs the following
     *
     * 1. Set the title
     * 2. Inflate the menu , and assign OnMenuItemClickListener
     *   - Health Practitioner must choose to monitor at least
     *     one patient to proceed to next screen
     * 3. Assign a back button listener, when the back button is clicked,
     *    it goes back to the login fragment.
     */
    private void setUpToolBar(){
        title.setText(R.string.title_patient_selection);
        toolbar.inflateMenu(R.menu.select_patients_menu);
        Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
            if (selectedPatients.size() > 0) {
                // save list into view model
                sharedViewModel.setSelectedPatients(selectedPatients);
                MainActivity main = (MainActivity) getActivity();
                Objects.requireNonNull(main).findFragment(MainActivity.home_fragment);
            } else {
                String message = "Please select AT LEAST 1 patient";
                Context context = getContext();
                Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        };
        toolbar.setOnMenuItemClickListener(menuItemClickListener);

        backButton.setOnClickListener(v -> {
            MainActivity main = (MainActivity) getActivity();
            Objects.requireNonNull(main).findFragment(MainActivity.login_fragment);
        });
    }

    /**
     * This method updates the tool bar to display the total number of patients that is currently
     * being selected
     */
    private void updateToolbar(){
        String text = selectedPatients.size() + " Patients Selected";
       // Log.d("Select_Patient_Fragment","current sellected patient : " + selected_patients.toString());
        title.setText(text);
    }

    /**
     * This method is called when a patient's card-view is specifically clicked
     * It checks if the clicked patient is in the selected patient list and does the following
     *
     * 1. If a patient is selected ,
     *      - If patient is not already in selected patient list
     *              add patient to selected patient list
     *      - else don't do anything
     *
     * 2. If a patient is deselected ,
     *      - Remove patient from selected patient list
     *
     * 3. Update the tool bar
     *
     * @param checked the state of the patient , true for selected, false otherwise
     * @param patient the patient that is clicked
     */
    public void onPatientClick(boolean checked, PatientModel patient) {
        PatientModel patientWithSameID = null;
        boolean found = false;
        for (PatientModel p : selectedPatients){
            if(p.getPatientID().equals(patient.getPatientID())){
                found = true;
                patientWithSameID = p;
                break;
            }
        }
        if (checked) {
            if (!found) {
                selectedPatients.add(patient);
            }
        } else if (!checked) {
            if(found){
                selectedPatients.remove(patientWithSameID);
            }else{
                selectedPatients.remove(patient);
            }
        }
        updateToolbar();
    }
}

