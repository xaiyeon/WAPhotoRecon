package com.sunkoiwish.waphotorecon.FireBase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.sunkoiwish.waphotorecon.Models.Photo;
import com.sunkoiwish.waphotorecon.Models.User;
import com.sunkoiwish.waphotorecon.Models.UserPhoto;
import com.sunkoiwish.waphotorecon.R;

import org.w3c.dom.Text;

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

    // Define FireBase
    private FirebaseApp app;
    private FirebaseAuth auth;

    // Define the photos Firebase DatabaseReference
    private DatabaseReference database_photos;

    // Define the user_photos Firebase DatabaseReference
    private DatabaseReference database_user_photos;

    // Define the users Firebase DatabaseReference
    private DatabaseReference database_users;

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

        // Firebase instances
        app = FirebaseApp.getInstance();
        auth = FirebaseAuth.getInstance(app);

        // get from out database of the userphotos photos
        database_photos = FirebaseDatabase.getInstance().getReference("userphotos").child(auth.getCurrentUser().getUid().toString());


        View view = inflater.inflate(R.layout.fragment_fire_base, container, false);

        rec_view = (RecyclerView) view.findViewById(R.id.FireBaseDB_RecView);
        rec_view.setLayoutManager(new LinearLayoutManager(getContext()));

        // Don't need to check if we are signed in because you have to sign in...

        // start firebase recylce... You must define the parameters you specified below to let it know what you doing.
        FirebaseRecyclerAdapter<UserPhoto, UserImageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserPhoto, UserImageViewHolder>(
                UserPhoto.class,
                R.layout.layout_userphotos,
                UserImageViewHolder.class,
                database_photos
        ) {
            @Override
            protected void populateViewHolder(UserImageViewHolder viewHolder, UserPhoto model, int position) {

                viewHolder.txtname.setText(auth.getCurrentUser().getDisplayName());
                viewHolder.txtdate.setText(model.getPhoto_create_date());
                viewHolder.txtloc.setText(model.getLocation_name()); // where is it
                viewHolder.txtdesc.setText("Description: " + model.getPhoto_description());
                viewHolder.txtimgname.setText(model.getPhotoName());
                Picasso.with(getContext()).load(model.getImgdata_url()).into(viewHolder.imgUsersimg);
                viewHolder.txtdevicename.setText("Device: " + model.getDevice_name());
                viewHolder.txtanalyzed.setText("Is Analyzed?: " + model.getIsAnalyzed()); // is true or false


            }
        };

        // now we set it, and it populates!
        rec_view.setAdapter(firebaseRecyclerAdapter);


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


    // UserImageViewHolder is used as a layout to populate for the recycler view.

    public static class UserImageViewHolder extends RecyclerView.ViewHolder{

        TextView txtname;
        TextView txtdate;
        TextView txtloc;
        TextView txtimgname;
        TextView txtdesc;
        ImageView imgUsersimg;
        TextView txtdevicename;
        TextView txtanalyzed;

        public UserImageViewHolder(View itemView){
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.layout_usernameoremail);
            txtdate = (TextView) itemView.findViewById(R.id.layout_createdate);
            txtloc = (TextView) itemView.findViewById(R.id.layout_location);
            txtimgname = (TextView) itemView.findViewById(R.id.layout_imgname);
            txtdesc = (TextView) itemView.findViewById(R.id.layout_description);
            imgUsersimg = (ImageView) itemView.findViewById(R.id.layout_userimage);
            txtdevicename = (TextView) itemView.findViewById(R.id.layout_devicename);
            txtanalyzed = (TextView) itemView.findViewById(R.id.layout_isanalyzed);
        }


    } // End of UserImageViewHolder



}
