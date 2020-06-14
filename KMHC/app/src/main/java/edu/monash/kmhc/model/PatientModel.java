package edu.monash.kmhc.model;

import android.text.format.DateFormat;

import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import edu.monash.kmhc.model.observation.BloodPressureObservationModel;
import edu.monash.kmhc.model.observation.ObservationModel;
import edu.monash.kmhc.model.observation.ObservationType;

/**
 * Patient model class. Represents a patient in the app.
 */
public class PatientModel {

    private String patientID;
    private String name;
    private Date birthDate;
    private Enumerations.AdministrativeGender gender;
    private PatientAddressModel address;
    // a patient can have more than one type of observation type. use HashMap to store the observation
    // based on their type
    private HashMap<ObservationType, ObservationModel> observationReadings;
    private HashMap<ObservationType, Boolean> isMonitored;
    private ArrayList<BloodPressureObservationModel> latestBPReadings;

    /**
     * Constructor
     * @param patientID Unique patient ID from the FHIR server
     * @param name name of the patient
     */
    public PatientModel(String patientID, String name, Date birthDate, Enumerations.AdministrativeGender gender, PatientAddressModel address) {
        this.patientID = patientID;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        observationReadings = new HashMap<>();
        isMonitored = new HashMap<>();
        latestBPReadings = new ArrayList<>();
        initialiseMonitoredObservations();
    }

    public String getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return DateFormat.format("yyyy.MM.dd", birthDate).toString();
    }

    public String getGender() {
        return gender.toString();
    }

    public PatientAddressModel getAddress() {
        return address;
    }

    public void setObservation(ObservationType type, ObservationModel observation) {
        observationReadings.put(type, observation);
    }

    public ObservationModel getObservationReading(ObservationType type) {
        return observationReadings.get(type);
    }

    public void monitorObservation(ObservationType type, Boolean check) {
        isMonitored.put(type, check);
    }

    public Boolean isObservationMonitored(ObservationType type) {
        return isMonitored.get(type);
    }

    private void initialiseMonitoredObservations() {
        for (ObservationType type: ObservationType.values()) {
            monitorObservation(type, false);
        }
    }

    public void addLatestBPReadings(ArrayList<BloodPressureObservationModel> readings) {
        latestBPReadings = readings;
    }

    public ArrayList<BloodPressureObservationModel> getLatestReadings(ObservationType type) {
        return latestBPReadings;
    }
}
