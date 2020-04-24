package edu.monash.kmhc.data.model;

/**
 * Class used to store patient's address
 * Address is comprised of city, state, country
 */
public class PatientAddressModel {

    private String city;
    private String state;
    private String country;

    /**
     * Constructor used to initialise new patient's address
     * @param city city the patient is from
     * @param state state the patient is from
     * @param country country the patient is from
     */
    public PatientAddressModel(String city, String state, String country) {
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }
}
