package edu.monash.kmhc.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import edu.monash.kmhc.model.PatientModel;

/**
 * BaseAdapter is an abstract class.
 * This class serves as a skeleton class for all the adapter classes to inherit from.
 * Subclasses must implement all the abstract methods found in this class.
 */

public abstract class BaseAdapter<H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    private HashMap<String, PatientModel> patientsHashMap;
    private ArrayList<PatientModel> uniquePatients = new ArrayList<>();

    /**
     * Constructor.
     */
    BaseAdapter(HashMap<String, PatientModel> patientsHashMap) {
        this.patientsHashMap = patientsHashMap;
    }

    /**
     * Getter for PatientHashMap
     * @return the patientHashMap
     */
    HashMap<String, PatientModel> getPatientsHashMap() {
        return patientsHashMap;
    }

    /**
     * Setter for PatientHashMap
     * @param patientsHashMap new PatientHashMap
     */
    void setPatientsHashMap(HashMap<String, PatientModel> patientsHashMap) {
        this.patientsHashMap = patientsHashMap;
    }


    /**
     * Getter for uniquePatients
     * @return uniquePatients
     */
    ArrayList<PatientModel> getUniquePatients() {
        return uniquePatients;
    }

    /**
     * Setter for uniquePatients
     * @param uniquePatients new uniquePatients list
     */
    void setUniquePatients(ArrayList<PatientModel> uniquePatients) {
        this.uniquePatients = uniquePatients;
    }

    /**
     * onCreateViewHolder overrides it superclass's ( RecyclerView ) method
     * subclass of this class must implement this method.
     */
    @NonNull
    @Override
    public abstract BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    /**
     * onBindViewHolder overrides it superclass's ( RecyclerView ) method
     * subclass of this class must implement this method.
     */
    @Override
    public abstract void onBindViewHolder(@NonNull BaseViewHolder holder, int position);

    /**
     * onGetItemCount overrides it superclass's ( RecyclerView ) method
     * subclass of this class must implement this method.
     */
    @Override
    public abstract int getItemCount();

}
