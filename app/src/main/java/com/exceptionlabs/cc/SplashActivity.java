package com.exceptionlabs.cc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.exceptionlabs.cc.models.User;
import com.exceptionlabs.cc.models.UserManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.e("SplashActivity", "User logged in");
            //User signed in, grab their profile
            final UserManager myUserManager = new UserManager();
            myUserManager.loadUserFromCCUID(user.getUid(), new UserManager.UserLoadedListener() {
                @Override
                public void onUserLoaded() {
                    User curUser = myUserManager.getLoadedUser();
                    Intent nextActivity;
                    if (!curUser.isBasicProfileComplete()) {
                        // Something has gone wrong with this user's profile. Prompt them to sign in again
                        FirebaseAuth.getInstance().signOut();
                        nextActivity = new Intent(SplashActivity.this, SignInActivity.class);
                    }else{

                        if (curUser.getOrgs() == null || curUser.getOrgs().size() == 0) {
                            // User does not belong to any organizations, let's add one now
                            nextActivity = new Intent(SplashActivity.this, AddOrgActivity.class);
                        } else {
                            // User is all set up and ready to go!
                            nextActivity = new Intent(SplashActivity.this, DashboardActivity.class);
                        }
                    }
                    startActivity(nextActivity);
                    finish();
                }
            });
        } else {
            //User not signed in
            Log.e("SplashActivity", "User not logged in");
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }
}
