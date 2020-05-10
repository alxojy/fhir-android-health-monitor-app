package edu.monash.kmhc.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import edu.monash.kmhc.model.PatientModel;

/**
 * BaseAdapter is an abstract class.
 */

public abstract class BaseAdapter<H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {


    private HashMap<String, PatientModel> patientsHashMap;
    private ArrayList<PatientModel> uniquePatients = new ArrayList<>();

    BaseAdapter(HashMap<String, PatientModel> patientsHashMap) {
        this.patientsHashMap = patientsHashMap;
    }

    HashMap<String, PatientModel> getPatientsHashMap() {
        return patientsHashMap;
    }

    public void setPatientsHashMap(HashMap<String, PatientModel> patientsHashMap) {
        this.patientsHashMap = patientsHashMap;
    }

    ArrayList<PatientModel> getUniquePatients() {
        return uniquePatients;
    }

    void setUniquePatients(ArrayList<PatientModel> uniquePatients) {
        this.uniquePatients = uniquePatients;
    }

    @NonNull
    @Override
    public abstract BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(@NonNull BaseViewHolder holder, int position);

    @Override
    public abstract int getItemCount();

}
