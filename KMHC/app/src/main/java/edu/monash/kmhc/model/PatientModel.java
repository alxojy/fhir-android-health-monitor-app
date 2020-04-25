package edu.monash.kmhc.model;

import org.hl7.fhir.r4.model.Enumerations;

import java.util.Date;

/**
 * Patient model class. Represents a patient in the app.
 */
public class PatientModel {

    private String patientID;
    private String name;
    private Date birthDate;
    private Enumerations.AdministrativeGender gender;
    private PatientAddressModel address;

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
}
