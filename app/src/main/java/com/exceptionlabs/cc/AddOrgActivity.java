package com.exceptionlabs.cc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exceptionlabs.cc.addorgfragments.OrgListFragment;
import com.exceptionlabs.cc.addorgfragments.OrgNewFragment;
import com.exceptionlabs.cc.models.Organization;
import com.exceptionlabs.cc.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AddOrgActivity extends AppCompatActivity implements OrgListFragment.OnFragmentInteractionListener, OrgNewFragment.OnFragmentAddOrganizationListener{

    FragmentManager fragmentManager;
    RelativeLayout rlHead;
    FrameLayout vFrag;
    TextView tvTitle, tvSubtitle;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<Organization> organizations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_org);

        rlHead = (RelativeLayout) findViewById(R.id.rlHeaderText);
        vFrag = (FrameLayout) findViewById(R.id.vFrag);
        tvTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        tvSubtitle = (TextView) findViewById(R.id.tvHeaderSub);
        //ImageView ivStars = (ImageView) findViewById(R.id.ivStars);

        //ivStars.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_out));
        fragmentManager = getSupportFragmentManager();
        transitionFragment(new OrgListFragment(), false);
        vFrag.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.slide_up));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("/organization/MASTER/");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Organization newOrg = dataSnapshot.getValue(Organization.class);
                newOrg.setOID(dataSnapshot.getKey());
                organizations.add(newOrg);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void transitionFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.vFrag, fragment);
        if (addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        updateHeader(fragment);
    }

    // Two versions of updateHeader to allow for manually passing in fragment
    // This is needed as sometimes fragmentTransaction.commit does not complete in time
    private void updateHeader(Fragment fragment){
        if (fragment instanceof OrgListFragment){
            tvTitle.setText("Get Started");
            tvSubtitle.setText("Choose your organization");
        }else if(fragment instanceof OrgNewFragment){
            tvTitle.setText("Create an Organization");
            tvSubtitle.setText("Tell us about your organization");
        }else{
            tvTitle.setText("Unknown Fragment Displayed");
            tvSubtitle.setText("");
        }
        rlHead.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.slide_up));
    }

    private void updateHeader(){
        Fragment current = fragmentManager.findFragmentById(R.id.vFrag);
        updateHeader(current);
    }

    @Override
    public void onAddOrganizationRequested(Organization action) {
        String sNewOrg = myRef.push().getKey();
        myRef.child(sNewOrg).setValue(action);
        fragmentManager.popBackStack();
    }

    @Override
    public void onFragmentInteraction(String result) {
        if(result.equals("NEW")){
            transitionFragment(new OrgNewFragment(), true);
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
            updateHeader();
        } else {
            super.onBackPressed();
        }
    }
}
