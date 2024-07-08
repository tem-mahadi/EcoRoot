package com.temmahadi.ecoroot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_profile extends Fragment {
    TextView profileName, profileEmail, profileAddress, profileConsume;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public frag_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_profile newInstance(String param1, String param2) {
        frag_profile fragment = new frag_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_profile, container, false);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        profileAddress = view.findViewById(R.id.profile_address);
        profileConsume = view.findViewById(R.id.profile_consume);

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String name = sharedPref.getString("name", "");
        String email = sharedPref.getString("email", "");
        String address = sharedPref.getString("address", "");
        float totalConsumed = sharedPref.getFloat("totalConsumed", 0);

        profileName.setText(String.format("Name: %s", name));
        profileEmail.setText(String.format("Email: %s", email));
        profileAddress.setText(String.format("Address: %s", address));
        profileConsume.setText(String.format("Total Consumed: %s gm", totalConsumed));
        return view;
    }
}