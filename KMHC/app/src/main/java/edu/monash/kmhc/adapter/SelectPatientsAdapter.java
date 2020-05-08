package edu.monash.kmhc.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import edu.monash.kmhc.R;
import edu.monash.kmhc.model.PatientModel;

public class SelectPatientsAdapter extends RecyclerView.Adapter<SelectPatientsAdapter.ViewHolder> {
    private HashMap<String, PatientModel> patientsHashMap;
    private ArrayList<PatientModel> uniquePatients = new ArrayList<>();
    private OnPatientClickListener onPatientClickListener;
    private ArrayList<PatientModel> selected_patients;
    private ArrayList<Boolean> patientState = new ArrayList<>();

    public SelectPatientsAdapter(HashMap<String, PatientModel> patientsHashMap, OnPatientClickListener onPatientClickListener,ArrayList<PatientModel> selected_patients) {
        this.selected_patients = selected_patients;
        this.onPatientClickListener = onPatientClickListener;
        this.patientsHashMap = patientsHashMap;
        patientsHashMap.forEach((patientID, patientModel) -> {
            if (patientModel != null) {
                uniquePatients.add(patientModel);
                if (selected_patients.contains(patientModel)){
                    patientState.add(true);
                }else{
                    patientState.add(false);
                }
            }
        });
        System.out.println(this.uniquePatients.toString());
        Log.i("SelectPatientsAdapter", "SelectPatientsAdapter - Constructor Called");
        Log.i("SelectPatientsAdapter", "patients:"+ uniquePatients.toString());

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
        holder.checkBox.setChecked(patientState.get(position));
        holder.background.setBackgroundResource(R.drawable.cardv_nonselected_bg);
        holder.patientName.setText(uniquePatients.get(position).getName());
        if (selected_patients.contains(uniquePatients.get(position))){
            holder.checkBox.setChecked(true);
            holder.setCheckboxStatus(true);
            holder.background.setBackgroundResource(R.drawable.cardv_selected_bg);
            patientState.set(position,true);
            onPatientClickListener.onPatientClick(holder.checkBox.isChecked(), uniquePatients.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return uniquePatients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView patientName;
        CheckBox checkBox;
        OnPatientClickListener onPatientClickListener;
        ConstraintLayout background;
        private boolean checkboxStatus = false;


        ViewHolder(@NonNull View itemView, OnPatientClickListener onPatientClickListener) {
            super(itemView);
            patientName = itemView.findViewById(R.id.all_patient_txt_name);
            checkBox = itemView.findViewById(R.id.checkBox);
            background = itemView.findViewById(R.id.card_backgroud);
            this.onPatientClickListener = onPatientClickListener;
            itemView.setOnClickListener(this);
        }

        boolean getCheckboxStatus() {
            return checkboxStatus;
        }

        void setCheckboxStatus(boolean checkboxStatus) {
            this.checkboxStatus = checkboxStatus;
        }

        private void changeCheckBoxStatus(int position){
            if (getCheckboxStatus()) {
                patientState.set(position,false);

                try {
                    System.out.println(background);
                    background.setBackgroundResource(R.drawable.cardv_nonselected_bg);
                }
                catch (Exception ie){
                    System.out.println(ie.getMessage());
                }
                checkBox.setChecked(false);
                setCheckboxStatus(false);

            } else {

                try {
                    System.out.println(background);
                    background.setBackgroundResource(R.drawable.cardv_selected_bg);
                }
                catch (Exception ie){
                    System.out.println(ie.getMessage());
                }

                checkBox.setChecked(true);
                setCheckboxStatus(true);
            }
        }
        //card view listener
        @Override
        public void onClick(View v) {
            changeCheckBoxStatus(getAdapterPosition());
            onPatientClickListener.onPatientClick(checkBox.isChecked(), uniquePatients.get(getAdapterPosition()));
        }
    }

    // click listener for each view holder
    public interface OnPatientClickListener {
        void onPatientClick(boolean checked, PatientModel patient);

    }
}