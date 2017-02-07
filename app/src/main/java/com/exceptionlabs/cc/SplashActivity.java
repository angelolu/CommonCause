package com.exceptionlabs.cc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //User signed in
            final User myUser = new User(user.getUid());
            myUser.setUserLoadedListener(new User.UserLoadedListener() {
                @Override
                public void onDataLoaded() {
                    if (!myUser.getBasicProfileState()) {
                        // Something has gone wrong with this user's profile. Prompt them to sign in again
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                        finish();
                    }

                    Intent nextActivity;
                    if (myUser.getOrganizations() == null || myUser.getOrganizations().length == 0) {
                        // User does not belong to any organizations, let's add one now
                        nextActivity = new Intent(SplashActivity.this, AddOrgActivity.class);
                    } else {
                        // User is all set up and ready to go!
                        nextActivity = new Intent(SplashActivity.this, DashboardActivity.class);
                    }
                    startActivity(nextActivity);
                    finish();
                }
            });
        } else {
            //User not signed in
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }
}
