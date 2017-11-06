package com.sunkoiwish.waphotorecon.Account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.sunkoiwish.waphotorecon.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "AccountFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Firebase and signout stuff
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    // Widget
    private Button mSignout_btn;
    private Button mUprofile_btn;
    public EditText displayname_Edittxt;
    public TextView hello_txtView;

    // Define FireBase
    private FirebaseApp app;
    private FirebaseAuth auth;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // THIS IS WHERE WE CAN CHANGE AND ADD BUTTONS AND TEXT ETC.
        // By using this view.

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        TextView textView = (TextView) view.findViewById(R.id.accountText);
        displayname_Edittxt = (EditText) view.findViewById(R.id.account_displaynameEdittxt);
        hello_txtView = (TextView) view.findViewById(R.id.accFrag_textView);


        // Firebase instances
        app = FirebaseApp.getInstance();
        auth = FirebaseAuth.getInstance(app);

        hello_txtView.setText("Hello " + auth.getCurrentUser().getDisplayName());

        setupFirebaseListener();

        mSignout_btn = (Button) view.findViewById(R.id.signout_Btn);
        mSignout_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "onClick: attempting to sign out user.");
                FirebaseAuth.getInstance().signOut();
            }

        });

        mUprofile_btn = (Button) view.findViewById(R.id.account_updateprofile_btn);
        mUprofile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Attempting update of display name...");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayname_Edittxt.getText().toString())
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                    Toast.makeText(getActivity(), "Display Name has been successfully changed!", Toast.LENGTH_LONG).show();
                                    Toast.makeText(getActivity(), "Please log out and sign in to see changes!", Toast.LENGTH_LONG).show();
                                }
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Someone with that Display Name already exists!",
                                            Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });


        return view;
    }


    private void setupFirebaseListener(){
        Log.d(TAG,"setup Firebase Listener: setting up Firebase listener.");
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(TAG, "onAuthStateChange: signed in: " + user.getUid());

                }
                else{
                    Log.d(TAG, "onAuthStateChange: signed out." );
                    Toast.makeText(getActivity(), "Signed Out!", Toast.LENGTH_LONG).show();

                    // Once they log out we create an new intent and clear the old one and
                    // start the app in a new activity and user

                    Intent intent = new Intent(getActivity(), LoginActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);


                }
            }

        };

    }

    // For Firebase
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    // For Firebase
    @Override
    public void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

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
        void onFragmentInteraction(Uri uri);
    }
}
