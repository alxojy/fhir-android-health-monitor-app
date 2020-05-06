package edu.monash.kmhc.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import edu.monash.kmhc.R;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.viewModel.PatientInfoViewModel;

/**
 * This fragment is used to display extra information about the patient.
 * Extra information about patient: birth date, gender and address (city, state, country)
 * TODO: Display required information & get info from the model
 */
public class PatientInfoFragment extends Fragment {

    // is ViewModel necessary ?
    // since we are already passing the patient model instance here..?
    // and there is no option for user to update address
    // + each instance refer to a new patient..
    private PatientInfoViewModel mViewModel;
    private PatientModel patient;
    private TextView name;
    private TextView birthday;
    private TextView gender;
    private TextView address;

    Toolbar toolbar;
    //private TextView city;


    public PatientInfoFragment(PatientModel patient) {
        this.patient = patient;
    }

//    public static PatientInfoFragment newInstance() {
//        return new PatientInfoFragment();
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.patient_info_fragment, container, false);

        //set up toolbar
        toolbar = root.findViewById(R.id.toolbar);
        setUpToolBar();


        //find views
        name = root.findViewById(R.id.info_patientName);
        birthday = root.findViewById(R.id.info_patientBirthD);
        gender = root.findViewById(R.id.info_patientGender);
        address = root.findViewById(R.id.info_patientAddress);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PatientInfoViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.setPatient(patient);

        name.setText(patient.getName());
        birthday.setText(patient.getBirthDate());
        gender.setText(StringUtils.capitalize(patient.getGender().toLowerCase()));
        address.setText(patient.getAddress().getFullAddress());
    }

    public void setUpToolBar(){
        toolbar.setTitle("Patient Info");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

    }

//    public void setCallBackInterface(FragmentActionListener callBackInterface) {
//        this.callBackInterface = callBackInterface;
//    }

}
