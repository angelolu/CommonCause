package com.exceptionlabs.cc.addorgfragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.exceptionlabs.cc.R;
import com.exceptionlabs.cc.models.Organization;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrgNewFragment extends Fragment {

    private OnFragmentAddOrganizationListener mListener;

    public OrgNewFragment() {
        // Required empty public constructor
    }

    public static OrgNewFragment newInstance(String param1, String param2) {
        OrgNewFragment fragment = new OrgNewFragment();
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
        View view = inflater.inflate(R.layout.fragment_org_new, container, false);
        final EditText etOrgName = (EditText) view.findViewById(R.id.etOrgName);
        final EditText etOrgDesc = (EditText) view.findViewById(R.id.etOrgDesc);
        final CheckBox cbPublic = (CheckBox) view.findViewById(R.id.cbPublic);
        final CheckBox cbVolunteers = (CheckBox) view.findViewById(R.id.cbVolunteers);
        final CheckBox cbStaff = (CheckBox) view.findViewById(R.id.cbStaff);
        Button bNext = (Button) view.findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOrgName.getText().toString().equals("") || etOrgDesc.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                } else if (cbVolunteers.isChecked() == false && cbStaff.isChecked() == false) {
                    Toast.makeText(getActivity(), "Check at least one user type", Toast.LENGTH_SHORT).show();
                } else {
                    Organization newOrg = new Organization();
                    newOrg.setDateReg(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
                    newOrg.setName(etOrgName.getText().toString());
                    newOrg.setDesc(etOrgDesc.getText().toString());
                    newOrg.setListed(cbPublic.isChecked());
                    newOrg.setOwner(getActivity().getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString(getString(R.string.preference_key_current_userID), "0"));
                    if (mListener != null) {
                        mListener.onAddOrganizationRequested(newOrg);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentAddOrganizationListener) {
            mListener = (OnFragmentAddOrganizationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentAddOrganizationListener {
        void onAddOrganizationRequested(Organization action);
    }
}
