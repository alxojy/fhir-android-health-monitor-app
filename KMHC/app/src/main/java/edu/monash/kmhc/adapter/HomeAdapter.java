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

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private HashMap<PatientModel, ObservationModel> patientObservationHashMap ;
    private ArrayList<PatientModel> patients = new ArrayList<>();
    private ArrayList<ObservationModel> observations = new ArrayList<>();

    public HomeAdapter(HashMap<PatientModel, ObservationModel> patientObservationHashMap) {
        this.patientObservationHashMap = patientObservationHashMap;
        patientObservationHashMap.forEach((p,o) -> {
            patients.add(p);
            observations.add(o);
        });
        System.out.println(patients.toString());
        System.out.println(observations.toString());
        Log.i("HomeAdapter","HomeAdapter - Constructor Called");

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("HomeAdapter","HomeAdapter - OnCreateViewHolder Called");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_cardview,parent,false);
        HomeViewHolder viewHolder = new HomeViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Log.i("HomeAdapter", "HomeAdapter - onBindViewHolder Called");
        String cholStat = observations.get(position).getValue() + " " + observations.get(position).getUnit();
        holder.patientName.setText(patients.get(position).getName());
        holder.cholesterolValue.setText(cholStat);
        }


    @Override
    public int getItemCount() {
        return patientObservationHashMap.size();
    }

    public class HomeViewHolder extends  RecyclerView.ViewHolder {
        public TextView patientName;
        public TextView cholesterolValue;
        public TextView time;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            patientName = itemView.findViewById(R.id.cv_patientName);
            cholesterolValue = itemView.findViewById(R.id.cv_cholVal);
            time = itemView.findViewById(R.id.cv_time);

        }
    }
}