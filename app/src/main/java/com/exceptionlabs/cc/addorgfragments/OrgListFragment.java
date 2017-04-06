package com.exceptionlabs.cc.addorgfragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.exceptionlabs.cc.R;
import com.exceptionlabs.cc.models.DashboardIconView;
import com.exceptionlabs.cc.models.Organization;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrgListFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    List<Organization> organizations = new ArrayList<>();
    DashboardIconView divCode, divNew;
    FlexboxLayout fbOrgButtons;
    FlexboxLayout.LayoutParams params;

    public OrgListFragment() {
        // Required empty public constructor
    }

    public static OrgListFragment newInstance() {
        OrgListFragment fragment = new OrgListFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_org_list, container, false);
        fbOrgButtons = (FlexboxLayout) view.findViewById(R.id.fbOrgButtons);
        organizations = new ArrayList<>();

        int dpValue = 4; // margin in dips
        float d = this.getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        params = new FlexboxLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(margin, 0, margin, margin);

        // Default Buttons
        divCode = new DashboardIconView(getContext());
        divCode.setLabel("Enter Invite Code");
        divCode.setColour(ContextCompat.getColor(getContext(), R.color.colorAccent));
        divCode.setOnClickListener(this);
        divCode.setPayload(-2);


        divNew = new DashboardIconView(getContext());
        divNew.setLabel("Create an Organization");
        divNew.setColour(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        divNew.setPayload(-1);
        divNew.setOnClickListener(this);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/organization/MASTER/");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Organization newOrg = dataSnapshot.getValue(Organization.class);
                newOrg.setOID(dataSnapshot.getKey());
                organizations.add(newOrg);
                inValidateButtons();
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
        inValidateButtons();
        return view;
    }

    private void inValidateButtons(){
        // Figure out better implementation
        fbOrgButtons.removeAllViewsInLayout();
        for(int i = 0; i<organizations.size(); i++){
            Organization org = organizations.get(i);
            DashboardIconView divOrg  = new DashboardIconView(getContext());
            divOrg.setLabel(org.getName());
            divOrg.setPayload(i);
            divOrg.setOnClickListener(this);
            fbOrgButtons.addView(divOrg, params);
        }
        fbOrgButtons.addView(divCode, params);
        fbOrgButtons.addView(divNew, params);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onButtonPressed(String action) {
        if (mListener != null) {
            mListener.onFragmentInteraction(action);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String result);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof DashboardIconView) {
            if(((DashboardIconView) v).getPayload() == -1){
                onButtonPressed("NEW");
            }else if(((DashboardIconView) v).getPayload() == -2){
                Toast.makeText(getActivity(), "Find an organization", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Selected " + organizations.get(((DashboardIconView) v).getPayload()).getName(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
