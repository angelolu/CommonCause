package com.exceptionlabs.cc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exceptionlabs.cc.models.*;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    static int RC_SIGN_IN = 16;
    static String TAG = "Firebase Auth";
    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String fName, lName;
    ProgressBar pbLoading;
    SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Set the dimensions of the sign-in button.
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading) ;
        signInButton = (SignInButton) findViewById(R.id.gmsLogin);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    final com.exceptionlabs.cc.models.UserManager myUserManager = new com.exceptionlabs.cc.models.UserManager();
                    myUserManager.loadUserFromCCUID(user.getUid(), new com.exceptionlabs.cc.models.UserManager.UserLoadedListener() {
                        @Override
                        public void onUserLoaded() {
                            User myUser = myUserManager.getLoadedUser();
                            // Check if this is a new user, if not, record basic data
                            if(!myUser.isBasicProfileComplete()){
                                myUser.setBasicProfile(fName, lName, user.getEmail());
                                myUserManager.syncUser(myUser);
                            }
                            // Decide next step
                            Intent nextActivity;
                            if(myUser.getOrgs() == null || myUser.getOrgs().size() == 0){
                                // The user is not registered with any organizations, prompt them to set one up
                                nextActivity = new Intent(SignInActivity.this, AddOrgActivity.class);
                            }else{
                                // User is all set up and ready to go!
                                nextActivity = new Intent(SignInActivity.this, DashboardActivity.class);
                            }

                            //Save current user to start syncing firebase data
                            Context context = getApplicationContext();
                            SharedPreferences sharedPref = context.getSharedPreferences(
                                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.preference_key_current_userID), user.getUid());
                            editor.commit();

                            // Let's continue, sign in flow is complete
                            startActivity(nextActivity);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            if (mAuthListener != null) {
                                mAuth.removeAuthStateListener(mAuthListener);
                            }
                            finish();
                        }
                    });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(SignInActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void setSignInProgress(Boolean state){
        if(state){
            //Currently signing in, hide button
            pbLoading.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.GONE);
        }else{
            //Not signing in, show button
            pbLoading.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        setSignInProgress(true);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        setSignInProgress(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase

                GoogleSignInAccount account = result.getSignInAccount();
                //validate domain
                firebaseAuthWithGoogle(account);


            } else {
                Toast.makeText(SignInActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                signOut();
            }
        }else{
            setSignInProgress(false);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        lName = acct.getFamilyName();
        fName = acct.getGivenName();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Sign in failed",
                                    Toast.LENGTH_SHORT).show();
                            signOut();
                        }
                        // ...
                    }
                });
    }
}
