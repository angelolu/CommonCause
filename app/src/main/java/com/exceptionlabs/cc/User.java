package com.exceptionlabs.cc;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by angel on 1/23/2017.
 */

class User {

    String UID, fname, lname, email, datereg, sOrganizations;
    String[] organizations;
    FirebaseDatabase database;
    DatabaseReference myRef;
    UserLoadedListener listener;
    Boolean loaded = false;

    public User(String UID) {
        this.UID = UID;
        this.listener = null;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("/users/UID/" + UID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("fname")) {
                    fname = dataSnapshot.child("fname").getValue(String.class);
                }
                if (dataSnapshot.hasChild("lname")) {
                    lname = dataSnapshot.child("lname").getValue(String.class);
                }
                if (dataSnapshot.hasChild("email")) {
                    email = dataSnapshot.child("email").getValue(String.class);
                }
                if (dataSnapshot.hasChild("datereg")) {
                    datereg = dataSnapshot.child("datereg").getValue(String.class);
                }
                if (dataSnapshot.hasChild("organizations")) {
                    sOrganizations = dataSnapshot.child("organizations").getValue(String.class);
                    organizations = sOrganizations.split(",");
                }
                loaded = true;
                listener.onDataLoaded();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Boolean getBasicProfileState() {
        // Checks if basic profile information has been recorded. Returns true if data is recorded, false if data is missing
        if (!loaded) return null;
        if (fname != null && lname != null && email != null && datereg != null) {
            return true;
        } else {
            return false;
        }
    }

    public void setBasicProfile(String fname, String lname, String email) {
        // Fills in basic profile information on first sign in
        myRef.child("fname").setValue(fname);
        myRef.child("lname").setValue(lname);
        myRef.child("email").setValue(email);
        myRef.child("datereg").setValue(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
    }

    public String[] getOrganizations() {
        return organizations;
    }

    public interface UserLoadedListener {
        // This interface is used to define the UserLoaded listener, which is used to delay activities until Firebase responds
        public void onDataLoaded();
    }

    public void setUserLoadedListener(UserLoadedListener listener) {
        this.listener = listener;
    }
}
