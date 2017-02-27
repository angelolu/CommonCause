package com.exceptionlabs.cc.models;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by angel on 2/26/2017.
 */

public class User {
    private String CCUID, fname, lname, email, phone, dateReg, city;
    private List<String[]> orgs;

    public Boolean isBasicProfileComplete() {
        // Checks if basic profile information has been recorded. Returns true if data is recorded, false if data is missing
        if (fname != null && lname != null && email != null && dateReg != null && fname != "" && lname != "" && email != "" && dateReg != "") {
            Log.e("User", "Basic Profile is Complete");
            Log.e("User", "fname: "+fname);
            Log.e("User", "lname: "+lname);
            Log.e("User", "dateReg: "+dateReg);
            Log.e("User", "email: "+email);
            return true;
        } else {
            Log.e("User", "Basic Profile is not Complete");
            return false;
        }
    }

    public void setBasicProfile(String fname, String lname, String email) {
        // Fills in basic profile information on first sign in
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.dateReg = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    // Default constructor for Firebase
    public User() {
    }

    // Getters and Setters Required for Firebase
    public String getCCUID() {
        return CCUID;
    }

    public void setCCUID(String CCUID) {
        this.CCUID = CCUID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateReg() {
        return dateReg;
    }

    public void setDateReg(String dateRef) {
        this.dateReg = dateRef;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String[]> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<String[]> orgs) {
        this.orgs = orgs;
    }
}
