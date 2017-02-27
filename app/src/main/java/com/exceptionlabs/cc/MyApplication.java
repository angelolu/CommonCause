package com.exceptionlabs.cc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by angel on 1/24/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase databaseRef = FirebaseDatabase.getInstance();
        databaseRef.setPersistenceEnabled(true);
        // Retrieve current and past UID to ensure offline user access
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String currentUID = sharedPref.getString(getString(R.string.preference_key_current_userID), "0");
        String lastSyncedUID = sharedPref.getString(getString(R.string.preference_key_previous_userID), "0");
        // Check if the same user is still signed in as last launch. If not, delete old data.
        if (!lastSyncedUID.equals("0") && !lastSyncedUID.equals(currentUID)) databaseRef.getReference().child("/users/CCUID/" + lastSyncedUID).keepSynced(false);
        // Keep current account in sync
        if (!currentUID.equals("0")) {
            Log.e("APP", "Keeping /users/CCUID/"+ currentUID+" in sync");
            databaseRef.getReference().child("/users/CCUID/" + currentUID).keepSynced(true);
            // Take a note of the UID currently synced for next start
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.preference_key_previous_userID), currentUID);
            editor.commit();
        }
    }
}
