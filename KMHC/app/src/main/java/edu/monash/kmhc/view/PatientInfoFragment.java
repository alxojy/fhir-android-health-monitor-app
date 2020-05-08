package edu.monash.kmhc.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import edu.monash.kmhc.MainActivity;
import edu.monash.kmhc.R;
import edu.monash.kmhc.model.PatientModel;

/**
 * This fragment is used to display extra information about the patient.
 * Extra information about patient: birth date, gender and address (city, state, country)
 */
public class PatientInfoFragment extends Fragment {

    private PatientModel patient;
    private ImageButton backButton;
    private TextView name;
    private TextView birthday;
    private TextView gender;
    private TextView address;
    private Toolbar toolbar;


    /**
     * Patient Info Fragment Constructor
     * @param patient the patient object that should be displayed
     */
    public PatientInfoFragment(PatientModel patient) {
        this.patient = patient;
    }


    /**
     * This method performs all graphical initialization,
     * assign all view variables and set up the tool bar.
     * @return The PatientInfo UI view that is created.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.patient_info_fragment, container, false);

        //set up toolbar
        toolbar = root.findViewById(R.id.patient_info_toolbar);
        backButton = root.findViewById(R.id.btn_back);
        setUpToolBar();

        //find views
        name = root.findViewById(R.id.info_patientName);
        birthday = root.findViewById(R.id.info_patientBirthD);
        gender = root.findViewById(R.id.info_patientGender);
        address = root.findViewById(R.id.info_patientAddress);

        return root;
    }

    /**
     * This method updates the UI component to display the patient's data
     * IE : display patient name , gender, birthday and address
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name.setText(patient.getName());
        birthday.setText(patient.getBirthDate());
        gender.setText(StringUtils.capitalize(patient.getGender().toLowerCase()));
        address.setText(patient.getAddress().getFullAddress());
    }

    /**
     * This method is responsible to set up the tool bar in the PatientInfoFragment
     * It performs the following
     *
     * 1. Set the title
     * 2. Inflate the menu , and assign a back button listener
     *   - When the back Button is click , the app should navigate back to HomeFragment
     */
    private void setUpToolBar(){
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText(R.string.title_patient_info);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        backButton.setOnClickListener(v -> {
            MainActivity main = (MainActivity) getActivity();
            main.findFragment(MainActivity.home_fragment);
        });
    }

}
