package com.ebizon.fluid.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by manish on 02/02/16.
 */
public class AddressData {
    private final String firstName;
    private final String lastName;
    private final String contactNumber;
    private final String emailAddress;
    private final String street;
    private final String city;
    private final String zipCode;
    private final String state;
    private final String countryId;

    public AddressData(String firstName, String lastName, String contactNumber, String emailAddress, String street, String city, String zipCode, String state, String countryId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.state = state;
        this.countryId = countryId;
    }

    public AddressData(AddressData that){
        this.firstName = that.firstName;
        this.lastName = that.lastName;
        this.contactNumber = that.contactNumber;
        this.emailAddress = that.emailAddress;
        this.street = that.street;
        this.city = that.city;
        this.zipCode = that.zipCode;
        this.state = that.state;
        this.countryId = that.countryId;
    }

    public String getFirstName(){return this.firstName;}

    public String getLastName(){return this.lastName;}

    public String getContactNumber(){return this.contactNumber;}

    public String getEmailAddress(){return this.emailAddress;}

    public String getStreet(){return this.street;}

    public String getCity(){return this.city;}

    public String getZipCode(){return this.zipCode;}

    public String getState(){return this.state;}

    public String getCountryId(){return this.countryId;}

    public String getFullName(){return this.firstName + " " + this.lastName;}

    public JSONObject getJSON(){
        HashMap<String, String> addressMap = new HashMap<String, String>();
        addressMap.put("firstname", this.firstName);
        addressMap.put("lastname", this.lastName);
        addressMap.put("street", this.street);
        addressMap.put("city", this.city);
        addressMap.put("email", this.emailAddress);
        addressMap.put("phone", this.contactNumber);
        addressMap.put("postcode", this.zipCode);
        addressMap.put("region", this.state);
        addressMap.put("country", this.countryId);

        JSONObject jsonObject = new JSONObject(addressMap);

        return jsonObject;
    }

    public JSONObject getAsBillingJSON() {
        HashMap<String,String>  addressMap = new HashMap<>();

        addressMap.put("billfname", this.getFirstName());
        addressMap.put("billlname", this.getLastName());
        addressMap.put("billstreet1", this.getStreet());
        addressMap.put("billcity", this.getCity());
        addressMap.put("billpostcode", this.getZipCode());
        addressMap.put("billphone", this.getContactNumber());
        addressMap.put("billstate", this.getState());
        addressMap.put("billcountry", this.getCountryId());

        JSONObject jsonObj = new JSONObject(addressMap);

        return jsonObj;
    }

    public JSONObject getAsShippingJSON() {
        HashMap<String,String>  addressMap = new HashMap<>();

        addressMap.put("shippfname", this.getFirstName());
        addressMap.put("shipplname", this.getLastName());
        addressMap.put("shippstreet1", this.getStreet());
        addressMap.put("shippcity", this.getCity());
        addressMap.put("shippphone", this.getContactNumber());
        addressMap.put("shipppostcode", this.getZipCode());
        addressMap.put("shippstate", this.getState());
        addressMap.put("shippcountry", this.getCountryId());

        JSONObject jsonObj=new JSONObject(addressMap);

        return jsonObj;
    }


    public static AddressData create(JSONObject jsonObject){
        AddressData addressData = null;
        try {
            String firstName = jsonObject.getString("firstname");
            String lastName = jsonObject.getString("lastname");
            String contactNo = jsonObject.getString("contactno");
            String city = jsonObject.getString("city");
            String street = jsonObject.getString("street");
            String state = jsonObject.getString("region");
            String countryId = jsonObject.getString("countryid");
            String pinCode = jsonObject.getString("pincode");
            addressData = new AddressData(firstName, lastName, contactNo, "", street, city, pinCode, state, countryId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return addressData;
    }
}


