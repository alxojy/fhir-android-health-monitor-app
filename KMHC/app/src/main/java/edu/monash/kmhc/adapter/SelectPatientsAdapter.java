package edu.monash.kmhc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashMap;

import edu.monash.kmhc.R;
import edu.monash.kmhc.model.PatientModel;

/**
 * SelectPatientsAdapter class extends from BaseAdapter
 * SelectPatientsAdapter is the class that is responsible to create recycler view
 * that displays Health Practitioner's patients
 */
public class SelectPatientsAdapter extends BaseAdapter<SelectPatientsAdapter.SelectPatientViewHolder> {

    // select patients adapter on pcl
    private OnPatientClickListener onPatientClickListener;
    private ArrayList<PatientModel> selected_patients;
    // A list to remember a patient's state
    // ( true when patient is selected , otherwise false)
    private ArrayList<Boolean> patientState = new ArrayList<>();

    /**
     * The SelectPatient Adapter constructor, this initialises the adapter that
     * will be used to update the SelectPatient fragment UI.
     *
     * @param patientsHashMap list of patients that is under HealthPractitioner
     * @param onPatientClickListener the listener that is listening to each patient's card clicks
     * @param selected_patients a list of selected patients
     */
    public SelectPatientsAdapter(HashMap<String, PatientModel> patientsHashMap, OnPatientClickListener onPatientClickListener, ArrayList<PatientModel> selected_patients) {
        super(patientsHashMap);
        this.selected_patients = selected_patients;
        this.onPatientClickListener = onPatientClickListener;

        ArrayList<PatientModel> uniquePatients = new ArrayList<>();

        // loop through the patient hash map to get a list of unique patients
        patientsHashMap.forEach((patientID, patientModel) -> {
            if (patientModel != null) {
                uniquePatients.add(patientModel);
                if (isSelectedPatient(patientModel)){
                    patientState.add(true);
                }else{
                    patientState.add(false);
                }
            }
        });
        setUniquePatients(uniquePatients);
    }

    /**
     * This method checks if patient is already in the selected patient list.
     * @param patient - the patient model object
     * @return true - if patient is already in the selected patient list
     *         false - if patient is not in the selected patient list
     */
    private boolean isSelectedPatient(PatientModel patient){
        boolean found = false;
        for (PatientModel p : selected_patients ){
            if(p.getPatientID().equals(patient.getPatientID())){
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * This method overrides its superclass's onCreateViewHolder method
     * It is responsible for creating new card view holders that will be used to hold
     * and display patient'name

     * @return a ViewHolder Object
     */
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_patients_cardview, parent, false);
        return new SelectPatientsAdapter.SelectPatientViewHolder(v, onPatientClickListener);
    }

    /**
     * This method overrides its superclass's onBindViewHolder method
     * This method is used to update the contents of the SelectPatientViewHolder to reflect the patient
     * at the given position.
     *
     * This method also checks if patient is a selected patient and do the following:
     * 1. if true -> highlight the patient's card view in green
     * 2. else -> don't highlight the patient's card view
     *
     * @param baseHolder the reference to the holder that will be used to display the patient's name
     * @param position the position of the adapter
     */
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseHolder, int position) {
        SelectPatientViewHolder holder = (SelectPatientViewHolder) baseHolder;
        holder.checkBox.setChecked(patientState.get(position));
        holder.background.setBackgroundResource(R.drawable.cardv_nonselected_bg);
        holder.patientName.setText(getUniquePatients().get(position).getName());
        if (isSelectedPatient(getUniquePatients().get(position))){
            holder.checkBox.setChecked(true);
            holder.background.setBackgroundResource(R.drawable.cardv_selected_bg);
            patientState.set(position,true);
            onPatientClickListener.onPatientClick(holder.checkBox.isChecked(), getUniquePatients().get(position));
        }

    }

    /**
     * This method overrides its superclass's method,
     * It gets the total number of patients that is under HeathPractitioner
     * @return total number of patients
     */
    @Override
    public int getItemCount() {
        return getUniquePatients().size();
    }

    /**
     * The SelectPatientViewHolder class are objects that holds the reference to the individual
     * card views that is reused to display different patient's data in the recycler view.
     *
     * This class implements View.OnClickListener interface.
     */
    public class SelectPatientViewHolder extends BaseViewHolder{
        TextView patientName;
        CheckBox checkBox; //hidden checkbox
        SelectPatientsAdapter.OnPatientClickListener onPatientClickListener;
        ConstraintLayout background;

        /**
         * The SelectPatientViewHolder constructor
         * initialise SelectPatientViewHolder object that will be used to hold patient's data
         * @param onPatientClickListener the listener that is listening to patient's card clicks
         */
        SelectPatientViewHolder(@NonNull View itemView, OnPatientClickListener onPatientClickListener) {
            super(itemView);
            patientName = itemView.findViewById(R.id.all_patient_txt_name);
            checkBox = itemView.findViewById(R.id.checkBox);
            background = itemView.findViewById(R.id.card_backgroud);
            this.onPatientClickListener = onPatientClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * A method to update the invisible check box's status and update the UI
         * @param position the position of the adapter
         */
        private void changeCheckBoxStatus(int position){
            if (checkBox.isChecked()) {
                patientState.set(position,false);
                background.setBackgroundResource(R.drawable.cardv_nonselected_bg);
                checkBox.setChecked(false);

            } else {
                background.setBackgroundResource(R.drawable.cardv_selected_bg);
                checkBox.setChecked(true);
                patientState.set(position,true);

            }
        }

        /**
         * This method is called when user clicks on a patient's card-view.
         * It notifies its onClickListener (SelectPatientFragment) to add the patient into the
         * selected patient list/ remove the patient form the selected patient list
         */
        @Override
        public void onClick(View v) {
            changeCheckBoxStatus(getAdapterPosition());
            onPatientClickListener.onPatientClick(checkBox.isChecked(), getUniquePatients().get(getAdapterPosition()));
        }
    }

    /**
     * Class the uses this interface must implement their own onPatientClick method.
     * onPatientClick is handle the events that should happen when a patient's card-view is clicked.
     */
    public interface OnPatientClickListener {
        void onPatientClick(boolean checked, PatientModel patient);

    }
}
