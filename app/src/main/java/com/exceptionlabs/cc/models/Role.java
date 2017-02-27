package com.exceptionlabs.cc.models;

import java.util.List;

/**
 * Created by angel on 2/26/2017.
 */

public class Role {
    private String RID, name, invite;
    private List<String> waivers;
    private List<String[]> permissions;

    public Role() {
    }

    public String getRID() {
        return RID;
    }

    public void setRID(String RID) {
        this.RID = RID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvite() {
        return invite;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }

    public List<String> getWaivers() {
        return waivers;
    }

    public void setWaivers(List<String> waivers) {
        this.waivers = waivers;
    }

    public List<String[]> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String[]> permissions) {
        this.permissions = permissions;
    }
}
