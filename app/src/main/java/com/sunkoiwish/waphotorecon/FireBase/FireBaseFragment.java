package com.sunkoiwish.waphotorecon.FireBase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sunkoiwish.waphotorecon.Models.Photo;
import com.sunkoiwish.waphotorecon.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FireBaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FireBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FireBaseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // Define the Teacher Firebase DatabaseReference
    private DatabaseReference database_photos;

    Query query = FirebaseDatabase.getInstance()
            .getReference()
            .child("photos")
            .limitToLast(10);


    RecyclerView rec_view;

    public FireBaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FireBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FireBaseFragment newInstance(String param1, String param2) {
        FireBaseFragment fragment = new FireBaseFragment();
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

        // Associate the Teacher Firebase Database Reference with the database's teacher object
        database_photos = FirebaseDatabase.getInstance().getReference();
        database_photos = database_photos.child("photos");

        View view = inflater.inflate(R.layout.fragment_fire_base, container, false);

        rec_view = (RecyclerView) view.findViewById(R.id.FireBaseDB_RecView);

        // TO:DO
        // TODO: Fix this recyle view trash for fire base real time database

       // FirebaseRecyclerOptions<Photo> options =
       //         new FirebaseRecyclerOptions.Builder<Photo>()
       //                 .setQuery(query, Photo.class)
       //                 .build();



        return view;
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
