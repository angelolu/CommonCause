package com.exceptionlabs.cc.models;

import java.util.List;

/**
 * Created by angel on 2/26/2017.
 */

public class Organization {
    private String OID, dateReg, owner, name, desc, def_role;
    private boolean listed;
    private List<Role> roles;
    private List<Document> documents;

    public Organization() {
    }

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public String getDateReg() {
        return dateReg;
    }

    public void setDateReg(String dateRed) {
        this.dateReg = dateRed;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDef_role() {
        return def_role;
    }

    public void setDef_role(String def_role) {
        this.def_role = def_role;
    }

    public boolean isListed() {
        return listed;
    }

    public void setListed(boolean listed) {
        this.listed = listed;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
