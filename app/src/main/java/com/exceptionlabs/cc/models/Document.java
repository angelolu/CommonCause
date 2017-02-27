package com.exceptionlabs.cc.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by angel on 2/26/2017.
 */

public class Document {
    private String DID, name, data, dateAdd, dateSig, signature;

    public Document() {
    }

    public String getDID() {
        return DID;
    }

    public void setDID(String DID) {
        this.DID = DID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(String dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getDateSig() {
        return dateSig;
    }

    public void setDateSig(String dateSig) {
        this.dateSig = dateSig;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
