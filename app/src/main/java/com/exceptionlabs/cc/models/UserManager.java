package com.exceptionlabs.cc.models;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by angel on 2/26/2017.
 */

public class UserManager {
    FirebaseDatabase database;
    DatabaseReference myRef;
    User myUser;

    public UserManager() {
        database = FirebaseDatabase.getInstance();
        myUser = null;
    }

    public interface UserLoadedListener {
        // This interface is used to define the UserLoaded listener, which is used to delay activities until Firebase responds
        public void onUserLoaded();
    }

    public void syncUser(User toAdd){
        myRef = database.getReference("/users/CCUID/" + toAdd.getCCUID());
        myRef.setValue(toAdd);
    }

    public void loadUserFromCCUID(String CCUID, UserLoadedListener listener) {
        final UserLoadedListener myListener = listener;
        final String myCCUID = CCUID;
        myRef = database.getReference("/users/CCUID/" + myCCUID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If this is the first time a user in logging in (unregistered) dataSnapshot will be null
                if(dataSnapshot.getValue(User.class) == null){
                    myUser = new User();
                }else{
                    myUser = dataSnapshot.getValue(User.class);
                }
                myUser.setCCUID(myCCUID);
                myListener.onUserLoaded();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                myUser = null;
                myListener.onUserLoaded();
            }
        });
    }

    public User getLoadedUser(){
        return myUser;
    }
}
