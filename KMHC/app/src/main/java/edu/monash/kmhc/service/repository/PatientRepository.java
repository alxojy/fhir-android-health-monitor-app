package edu.monash.kmhc.service.repository;

import androidx.lifecycle.MutableLiveData;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.Date;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import edu.monash.kmhc.model.PatientAddressModel;
import edu.monash.kmhc.model.PatientModel;
import edu.monash.kmhc.service.FhirService;

/**
 * This class is responsible for returning all patients that are associated to the practitioner from
 * the FHIR server.
 * MutableLiveData is used whereby it is responsible for the Subject role in the Observer pattern.
 */
public class PatientRepository extends FhirService {

    private String practitionerId;
    private IGenericClient client = super.client;

    /**
     * Constructor.
     * @param practitionerId Health practitioner's id
     */
    public PatientRepository(String practitionerId) {
        this.practitionerId = practitionerId;
    }

    /**
     * Get all patients treated by the practitioner
     * @return ArrayList of patients
     */
    public ArrayList<PatientModel> getAllPatients() {
        // store patient references
        ArrayList<String> patientReferences = new ArrayList<>();
        // store patients
        ArrayList<PatientModel> patientModels = new ArrayList<>();

        // search for all encounters with the practitioner id
        Bundle bundle = client.search().forResource(Encounter.class)
                .where(new ReferenceClientParam("participant")
                        .hasId("Practitioner/" + practitionerId))
                .returnBundle(Bundle.class)
                .execute();

        // get all patient references
        bundle.getEntry().forEach((entry) -> patientReferences.add(
                (((Encounter) entry.getResource()).getSubject()).getReference()));

        Bundle patientBundle;

        // go through each patient reference and get the patient
        for (String id: patientReferences) {
            patientBundle = client.search()
                    .forResource(Patient.class)
                    .where(Patient.RES_ID.exactly().identifier(id))
                    .returnBundle(Bundle.class)
                    .execute();

            Patient patient = (Patient) (patientBundle.getEntry().get(0)).getResource();

            // human name documentation: https://www.hl7.org/fhir/DSTU2/datatypes-definitions.html#HumanName
            HumanName humanName = patient.getName().get(0);
            // prefix ie. Mr/ Mrs, given name ie. first & middle names, family ie. surname
            String patientName = humanName.getPrefixAsSingleString() + " "
                    + humanName.getGivenAsSingleString() + " " + humanName.getFamily();

            // get birth date
            Date birthDate = patient.getBirthDate();

            // get gender
            Enumerations.AdministrativeGender gender = patient.getGender();

            // get address
            PatientAddressModel patientAddress = new PatientAddressModel(patient.getAddress().get(0).getCity(),
                    patient.getAddress().get(0).getState(), patient.getAddress().get(0).getCountry());

            patientModels.add(new PatientModel(id, patientName, birthDate, gender, patientAddress));
        }

        return patientModels;
    }

}
