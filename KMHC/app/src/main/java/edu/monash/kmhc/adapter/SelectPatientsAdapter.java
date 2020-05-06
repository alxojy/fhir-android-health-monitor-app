package edu.monash.kmhc.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import edu.monash.kmhc.R;
import edu.monash.kmhc.model.PatientModel;

public class SelectPatientsAdapter extends RecyclerView.Adapter<SelectPatientsAdapter.ViewHolder> {
    private HashMap<String, PatientModel> patientsHashMap;
    private ArrayList<PatientModel> uniquePatients = new ArrayList<>();
    private OnPatientClickListener onPatientClickListener;
    private ArrayList<PatientModel> selected_patients = new ArrayList<>();


    public SelectPatientsAdapter(HashMap<String, PatientModel> patientsHashMap, OnPatientClickListener onPatientClickListener,ArrayList<PatientModel> selected_patients) {
        this.selected_patients = selected_patients;
        this.onPatientClickListener = onPatientClickListener;
        this.patientsHashMap = patientsHashMap;
        patientsHashMap.forEach((patientID, patientModel) -> {
            if (patientModel != null) {
                uniquePatients.add(patientModel);
            }
        });
        System.out.println(this.uniquePatients.toString());
        Log.i("SelectPatientsAdapter", "SelectPatientsAdapter - Constructor Called");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("SelectPatientsAdapter", "SelectPatientsAdapter - OnCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_patients_cardview, parent, false);
        SelectPatientsAdapter.ViewHolder viewHolder = new SelectPatientsAdapter.ViewHolder(v, onPatientClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("SelectPatientsAdapter", "SelectPatientsAdapter - OnBind");
        holder.patientName.setText(uniquePatients.get(position).getName());
        if (selected_patients.contains(uniquePatients.get(position))){
            holder.checkBox.setChecked(true);
            holder.setCheckboxStatus(true);
            onPatientClickListener.onPatientClick(holder.checkBox.isChecked(), uniquePatients.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return uniquePatients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout linearLayout;
        TextView patientName;
        CheckBox checkBox;
        OnPatientClickListener onPatientClickListener;
        private boolean checkboxStatus = false;


        public ViewHolder(@NonNull View itemView, OnPatientClickListener onPatientClickListener) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            patientName = itemView.findViewById(R.id.all_patient_txt_name);
            checkBox = itemView.findViewById(R.id.checkBox);
            this.onPatientClickListener = onPatientClickListener;
            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(checkListener);
        }
        //check box listener
        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeCheckBoxStatus();
                onPatientClickListener.onPatientClick(checkBox.isChecked(), uniquePatients.get(getAdapterPosition()));
            }
        };

        public boolean getCheckboxStatus() {
            return checkboxStatus;
        }
        public void setCheckboxStatus(boolean checkboxStatus) {
            this.checkboxStatus = checkboxStatus;
        }

        private void changeCheckBoxStatus(){
            if (getCheckboxStatus()) {
                checkBox.setChecked(false);
                setCheckboxStatus(false);

                ;
            } else {
                checkBox.setChecked(true);
                setCheckboxStatus(true);
            }
        }

        //card view listener
        @Override
        public void onClick(View v) {
            changeCheckBoxStatus();
            onPatientClickListener.onPatientClick(checkBox.isChecked(), uniquePatients.get(getAdapterPosition()));
        }
    }

    // click listener for each view holder
    public interface OnPatientClickListener {
        void onPatientClick(boolean checked, PatientModel patient);

    }
}