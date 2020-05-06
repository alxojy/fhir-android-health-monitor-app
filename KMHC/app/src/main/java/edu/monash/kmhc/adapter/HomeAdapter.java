package edu.monash.kmhc.adapter;

import android.util.Log;
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

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private HashMap<String, PatientModel> patientObservationHashMap;
    private ArrayList<PatientModel> patients = new ArrayList<>();
    private ArrayList<String> patientIDs = new ArrayList<>();
    private OnPatientClickListener onPatientClickListener;

    public HomeAdapter(HashMap<String, PatientModel> patientObservationHashMap,OnPatientClickListener onPatientClickListener) {
        this.patientObservationHashMap = patientObservationHashMap;
        this.onPatientClickListener = onPatientClickListener;

        patientObservationHashMap.forEach((patientID, patientModel) -> {
            patientIDs.add(patientID);
            patients.add(patientModel);
        });
        Log.d("HomeAdapter " , "monitoring" + patients.toString());
        Log.d("HomeAdapter" , patientIDs.toString());
        Log.d("HomeAdapter", "HomeAdapter - Constructor Called");

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("HomeAdapter", "HomeAdapter - OnCreateViewHolder Called");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_cardview, parent, false);
        HomeViewHolder viewHolder = new HomeViewHolder(v,onPatientClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Log.d("HomeAdapter", "HomeAdapter - onBindViewHolder Called");
        ObservationModel observationModel = patients.get(position).getObservationReading(ObservationType.CHOLESTEROL);
        Log.d("HomeAdapter", "observationModel " + observationModel);

        String cholStat = observationModel.getValue() + " " + observationModel.getUnit();
        String date = observationModel.getDateTime();
        holder.patientName.setText(patients.get(position).getName());
        holder.cholesterolValue.setText(cholStat);
        holder.time.setText(date);

        System.out.println(observationModel.toString());


    }


    @Override
    public int getItemCount() {
        return patientObservationHashMap.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView patientName;
        TextView cholesterolValue;
        TextView time;
        OnPatientClickListener onPatientClickListener;

        HomeViewHolder(@NonNull View itemView, OnPatientClickListener onPatientClickListener) {
            super(itemView);
            this.onPatientClickListener = onPatientClickListener;
            patientName = itemView.findViewById(R.id.cv_patientName);
            cholesterolValue = itemView.findViewById(R.id.cv_cholVal);
            time = itemView.findViewById(R.id.cv_time);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onPatientClickListener.onPatientClick(getAdapterPosition(), patients.get(getAdapterPosition()));
        }
    }

    // click listener for each view holder
    public interface OnPatientClickListener {
        void onPatientClick(int position, PatientModel patient);
    }
}