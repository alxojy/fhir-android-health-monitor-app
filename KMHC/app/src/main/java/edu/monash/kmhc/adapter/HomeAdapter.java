package edu.monash.kmhc.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.HashMap;

import edu.monash.kmhc.R;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.model.observation.ObservationModel;
import edu.monash.kmhc.model.observation.ObservationType;

/**
 * HomeAdapter is the class that is responsible to create recycler view
 * that displays Patient's cholesterol value.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private HashMap<String, PatientModel> patientObservationHashMap;
    private ArrayList<PatientModel> patients = new ArrayList<>();
    private ArrayList<String> patientIDs = new ArrayList<>();
    private OnPatientClickListener onPatientClickListener;
    private float average_value;


    /**
     * The Home Adapter constructor, this initialises the adapter that will be used to update the home fragment UI.
     * @param patientObservationHashMap A hash map that contains all the Patient Models that the practitioner is monitoring
     * @param onPatientClickListener the class that is listening to individual patient card clicks
     */
    public HomeAdapter(HashMap<String, PatientModel> patientObservationHashMap,OnPatientClickListener onPatientClickListener) {
        this.patientObservationHashMap = patientObservationHashMap;
        this.onPatientClickListener = onPatientClickListener;

        // loop to the hash map to get a list of patients and patient IDs
        // this is because adapter uses index to access the data, hash-map does not use indexes
        patientObservationHashMap.forEach((patientID, patientModel) -> {
            patientIDs.add(patientID);
            patients.add(patientModel);
        });
        calculateAverage();

    }

    /**
     * This method overrides its superclass's onCreateViewHolder method
     * It is responsible for creating new card view holders that will be used to hold
     * and display the data.
     * @return A HomeViewHolder instance that will be used to display the data
     */
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.d("HomeAdapter", "HomeAdapter - OnCreateViewHolder Called");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_cardview, parent, false);
        return new HomeViewHolder(v,onPatientClickListener);
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
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
       // Log.d("HomeAdapter", "HomeAdapter - onBindViewHolder Called");
        ObservationModel observationModel = patients.get(position).getObservationReading(ObservationType.CHOLESTEROL);
        String cholStat = observationModel.getValue() + " " + observationModel.getUnit();
        String date = observationModel.getDateTime();
        holder.patientName.setText(patients.get(position).getName());

        //if current patients cholesterol value is greater than average
        if (Float.parseFloat(observationModel.getValue()) > average_value ){
            //Log.d("home adpater", "current average :"+ average_value);
            holder.cholesterolValue.setBackgroundResource(R.drawable.cardv_red_bg);
            holder.patientName.setTextColor(R.color.colorRed);
        }
        holder.cholesterolValue.setText(cholStat);
        holder.time.setText(date);
    }


    /**
     * This method overrides its superclass's method,
     * It gets the total number of patients that Health Practitioner is monitoring.
     * @return total number of patients
     */
    @Override
    public int getItemCount() {
        return patients.size();
    }

    /**
     * The HomeViewHolder class are objects that holds the reference to the individual
     * card views that is reused to display different sets of data in the recycler view.
     *
     * This class implements View.OnClickListener interface.
     */
    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            onPatientClickListener.onPatientClick(getAdapterPosition(), patients.get(getAdapterPosition()));
        }
    }

    /**
     * Class the uses this interface must implement their own onPatientClick method.
     * onPatientClick is handle the events that should happen when a patient's card-view is clicked.
     */
    public interface OnPatientClickListener {
        void onPatientClick(int position, PatientModel patient);
    }

    /**
     * This method calculate the average cholesterol value
     */
    private void calculateAverage(){
        float total = 0;
        
        for( PatientModel p : patients){
            total += Float.parseFloat(p.getObservationReading(ObservationType.CHOLESTEROL).getValue());
        }
        average_value = total/patients.size();
    }
}