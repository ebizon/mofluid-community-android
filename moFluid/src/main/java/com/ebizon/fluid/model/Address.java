package com.ebizon.fluid.model;

import com.mofluid.magento2.EncodeString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by saddam on 19/3/18.
 */

public class Address {
    int customer_id;
    int address_id;
    private String fname;
    private String lname;
    private String contact_no;
    private String street;
    private String city;
    private String country_code;
    private String country;
    private String region_id;
    private String region;
    private String pincode;
    public Address(int address_id, String fname, String lname, String contact_no, String street, String city, String country_code,String country, String region_id, String region, String pincode) {
        this.address_id = address_id;
        this.fname = fname;
        this.lname = lname;
        this.contact_no = contact_no;
        this.street = street;
        this.city = city;
        this.country_code = country_code;
        this.region_id = region_id;
        this.region = region;
        this.pincode = pincode;
        this.country=country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public int getCustomer_id() {
        return customer_id;
    }

    public int getAddress_id() {
        return address_id;
    }


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
public String getCompleteAddress(){
    String address="";
    address+=this.getStreet();
    address+=this.getCity();
    address+=","+this.getCountry();
    address+="-"+this.getPincode();
    address+="\n";
    address+="Mobile-"+this.getContact_no();
    return address;
}
public String getAddressEncodeString(){
    Map<String,String> map=new HashMap<>();
        map.put("id",this.getAddress_id()+"");
        map.put("firstname",this.getFname());
        map.put("lastname",this.getLname());
        map.put("contactno",this.getContact_no());
        map.put("street",this.getStreet());
        map.put("city",this.getCity());
        map.put("country_code",this.getCountry_code());
        map.put("region_id",this.getRegion_id());
        map.put("pincode",this.getPincode());
        JSONArray jsonArray=new JSONArray();
        jsonArray.put(new JSONObject(map));
        String res= EncodeString.encodeStrBase64Bit(jsonArray.toString());
        return  res;
}
    public JSONObject getJSON(){
        HashMap<String, String> addressMap = new HashMap<String, String>();
        addressMap.put("firstname", this.fname);
        addressMap.put("lastname", this.lname);
        addressMap.put("street", this.street);
        addressMap.put("city", this.city);
        //addressMap.put("email", this.email);
        addressMap.put("phone", this.contact_no);
        addressMap.put("postcode", this.pincode);
        addressMap.put("region", this.region);
        addressMap.put("country", this.country_code);
        JSONObject jsonObject = new JSONObject(addressMap);

        return jsonObject;
    }
    public JSONObject getAsBillingJSON() {
        HashMap<String,String>  addressMap = new HashMap<>();
        addressMap.put("billfname", this.fname);
        addressMap.put("billlname", this.lname);
        addressMap.put("billstreet1", this.street);
        addressMap.put("billcity", this.city);
        addressMap.put("billpostcode", this.pincode);
        addressMap.put("billphone", this.contact_no);
        addressMap.put("billstate", this.region);
        addressMap.put("billcountry", this.country_code);
        JSONObject jsonObj = new JSONObject(addressMap);

        return jsonObj;
    }

    public JSONObject getAsShippingJSON() {
        HashMap<String,String>  addressMap = new HashMap<>();
        addressMap.put("shippfname", this.fname);
        addressMap.put("shipplname", this.lname);
        addressMap.put("shippstreet1", this.street);
        addressMap.put("shippcity", this.city);
        addressMap.put("shippphone", this.contact_no);
        addressMap.put("shipppostcode", this.pincode);
        addressMap.put("shippstate", this.region);
        addressMap.put("shippcountry", this.country_code);
        JSONObject jsonObj=new JSONObject(addressMap);

        return jsonObj;
    }
}
