package com.exceptionlabs.cc.models;

import java.util.List;

/**
 * Created by angel on 2/26/2017.
 */

public class OrgUser extends User {
    private String OUID, role;
    private List<Document> waivers;

    public OrgUser() {
    }

    public String getOUID() {
        return OUID;
    }

    public void setOUID(String OUID) {
        this.OUID = OUID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Document> getWaivers() {
        return waivers;
    }

    public void setWaivers(List<Document> waivers) {
        this.waivers = waivers;
    }
}
