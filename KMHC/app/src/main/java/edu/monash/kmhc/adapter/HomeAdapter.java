package edu.monash.kmhc.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

import edu.monash.kmhc.R;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.model.observation.ObservationModel;
import edu.monash.kmhc.model.observation.ObservationType;

/**
 * HomeAdapter class extends from BaseAdapter
 * HomeAdapter is the class that is responsible to create recycler view
 * that displays Patient's cholesterol value.
 */
public class HomeAdapter extends BaseAdapter<HomeAdapter.HomeViewHolder> {
    private ArrayList<String> patientIDs = new ArrayList<>();
    private OnPatientClickListener onPatientClickListener;
    private float averageValue;

    /**
     * The Home Adapter constructor, this initialises the adapter that will be used to update the home fragment UI.
     * @param patientObservationHashMap A hash map that contains all the Patient Models that the practitioner is monitoring
     * @param onPatientClickListener the class that is listening to individual patient card clicks
     */
    public HomeAdapter(HashMap<String, PatientModel> patientObservationHashMap, OnPatientClickListener onPatientClickListener) {
        super(patientObservationHashMap);
        this.onPatientClickListener = onPatientClickListener;

        ArrayList<PatientModel> patients = new ArrayList<>();

        getPatientsHashMap().forEach((patientID,patientModel) -> {
            patientIDs.add(patientID);
            patients.add(patientModel);
        });
        setUniquePatients(patients);

        calculateAverage();
    }

    /**
     * This method calculate the average cholesterol value
     */
    private void calculateAverage(){
        float total = 0;

        for( PatientModel p : getUniquePatients()){
            total += Float.parseFloat(p.getObservationReading(ObservationType.CHOLESTEROL).getValue());
        }
        averageValue = total/getUniquePatients().size();
    }


    /**
     * This method overrides its superclass's onCreateViewHolder method
     * It is responsible for creating new card view holders that will be used to hold
     * and display the data.
     * @return A HomeViewHolder instance that will be used to display the data
     */
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_cardview, parent, false);
        return new HomeAdapter.HomeViewHolder(v,onPatientClickListener);
    }

    /**
     * This method overrides its superclass's onBindViewHolder method
     * This method is used to update the contents of the HomeViewHolder to reflect the patient
     * at the given position.
     *
     * This method also highlights the patient if his/her cholesterol value is higher
     * than the average cholesterol value.
     * @param holder the homeViewHolder that will hold patient[position]'s data
     * @param position the current UI adapter position
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        HomeViewHolder homeViewHolder = (HomeViewHolder) holder;
        ObservationModel observationModel = getUniquePatients().get(position).getObservationReading(ObservationType.CHOLESTEROL);
        String cholStat = observationModel.getValue() + " " + observationModel.getUnit();
        String date = observationModel.getDateTime();
        homeViewHolder.patientName.setText(getUniquePatients().get(position).getName());

        //if current patients cholesterol value is greater than average
        //highlight cholesterol value in red
        if (Float.parseFloat(observationModel.getValue()) > averageValue ){
            homeViewHolder.cholesterolValue.setBackgroundResource(R.drawable.cardv_red_bg);
            homeViewHolder.patientName.setTextColor(R.color.colorRed);
        }
        homeViewHolder.cholesterolValue.setText(cholStat);
        homeViewHolder.time.setText(date);
    }

    /**
     * This method overrides its superclass's method,
     * It gets the total number of patients that Health Practitioner is monitoring.
     * @return total number of patients
     */
    @Override
    public int getItemCount() {
        return getUniquePatients().size();
    }

    /**
     * The HomeViewHolder class are objects that holds the reference to the individual
     * card views that is reused to display different sets of data in the recycler view.
     *
     * This class implements View.OnClickListener interface.
     */
    public class HomeViewHolder extends BaseViewHolder {
        TextView patientName;
        TextView cholesterolValue;
        TextView time;
        OnPatientClickListener onPatientClickListener;

        /**
         * HomeViewHolder Constructor
         * This initialises the HomeViewHolder object.
         */
        HomeViewHolder(@NonNull View itemView, OnPatientClickListener onPatientClickListener) {
            super(itemView);
            this.onPatientClickListener = onPatientClickListener;
            patientName = itemView.findViewById(R.id.cv_patientName);
            cholesterolValue = itemView.findViewById(R.id.cv_cholVal);
            time = itemView.findViewById(R.id.cv_time);
            itemView.setOnClickListener(this);
        }

        /**
         * This method is called when user clicks on a patient's card-view.
         * It notifies its onClickListener (Home Fragment) to perform a fragment switch to display
         *  individual patient details.
         */
        @Override
        public void onClick(View v) {
            onPatientClickListener.onPatientClick(getAdapterPosition(), getUniquePatients().get(getAdapterPosition()));

        }
    }
    /**
     * Class the uses this interface must implement their own onPatientClick method.
     * onPatientClick is handle the events that should happen when a patient's card-view is clicked.
     */
    public interface OnPatientClickListener {
        void onPatientClick(int position, PatientModel patient);
    }

}
