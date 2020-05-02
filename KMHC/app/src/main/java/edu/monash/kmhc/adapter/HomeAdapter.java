package edu.monash.kmhc.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.HashMap;

import edu.monash.kmhc.R;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.model.observation.ObservationModel;
import edu.monash.kmhc.viewModel.HomeViewModel;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private HashMap<PatientModel, ObservationModel> patientObservationHashMap ;

    public HomeAdapter(HashMap<PatientModel, ObservationModel> patientObservationHashMap) {
        this.patientObservationHashMap = patientObservationHashMap;
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
        //temporary solution
        if (patientObservationHashMap != null) {

            // how to get key ???????

            String cholVal = patientObservationHashMap.get(position).getValue();
            String cholUnit = patientObservationHashMap.get(position).getUnit();

            holder.cholesterolValue.setText(cholVal + " " + cholUnit);
        }
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