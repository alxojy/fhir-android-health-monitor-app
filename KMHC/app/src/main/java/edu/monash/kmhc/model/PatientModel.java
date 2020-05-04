package edu.monash.kmhc.model;

import org.hl7.fhir.r4.model.Enumerations;

import java.util.Date;
import java.util.HashMap;

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
        this.observationReadings = new HashMap<>();
    }

    public String getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
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

    // debug purpose to be removed later
    @Override
    public String toString() {
        return "PatientModel{" +
                "patientID='" + patientID + '\'' +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", gender=" + gender +
                ", address=" + address +
                ", observationReadings=" + observationReadings +
                '}';
    }
}
