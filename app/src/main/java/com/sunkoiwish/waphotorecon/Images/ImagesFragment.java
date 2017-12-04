package com.sunkoiwish.waphotorecon.Images;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.MessageFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.*;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;
import com.squareup.picasso.Picasso;
import com.sunkoiwish.waphotorecon.FireBase.FireBaseFragment;
import com.sunkoiwish.waphotorecon.Models.UserAnalyzePhoto;
import com.sunkoiwish.waphotorecon.Models.UserPhoto;
import com.sunkoiwish.waphotorecon.R;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;

// TODO: Implement the Face API and make layout for Face data and request URLs or data from users...
//

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImagesFragment extends Fragment {

    private String TAG = "ImagesFragment";

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
    private DatabaseReference database_user_analyzed_photos;


    // An arrayList of our objects...
    final ArrayList<UserPhoto> fromDatabase_AL = new ArrayList<UserPhoto>();
    /**
     * Here we will test and try Microsoft Cognitive Services
     * for analyze image
     * hopefully it can work!
     *
     *
     */

    // My subscription key for microsoft cog service
    // If expire use new keys...
            // old key: c8fc6c87dde64e5888e49d65cf3d2569
            // new key: 5d4298ebea2842148d97033a7fe95a61
    VisionServiceRestClient visionServiceRestClient = new VisionServiceRestClient("5d4298ebea2842148d97033a7fe95a61", "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");

    // Face API
    FaceServiceClient faceServiceClient = new FaceServiceRestClient("https://westcentralus.api.cognitive.microsoft.com/face/v1.0", "b50c03b4bb8a4608826e2dcc2fb1cec8");

    // view widgets
    ImageView imageView;
    Button mbutton;
    TextView desc_txtView;
    TextView age_txtView;
    Button fburlbutton;

    // Our layouts
    RecyclerView rec_view;
    private LinearLayoutManager mLayoutManager;

    public ImagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImagesFragment newInstance(String param1, String param2) {
        ImagesFragment fragment = new ImagesFragment();
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

    // Right here is where we do loading of images and stuff
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Firebase instances
        app = FirebaseApp.getInstance();
        auth = FirebaseAuth.getInstance(app);

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_images, container, false);

        // setting our test image
        //final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bikini_girl);
        // fINDING OUR view objects

        // get from out database of the userphotos photos
        database_user_analyzed_photos = FirebaseDatabase.getInstance().getReference("useranalyzedphotos").child(auth.getCurrentUser().getUid().toString());

        /**
        // Convert the image to stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // The URL button
        fburlbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "URL Button: onClick Pressed.");
                // Lets use our database firebase reference first...
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference userphotoRef = database.getReference("userphotos").child(auth.getUid());
                userphotoRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){

                            UserPhoto userPhoto = ds.getValue(UserPhoto.class);
                            Log.d(TAG, "Here is your data: " + userPhoto);
                            // for one right now.
                            url_AnalyzeImage(view);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "The FireBase Service is experiencing difficulties!", Toast.LENGTH_LONG).show();
                    }

                });
            }
        });

        // our button click
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                try {
                    Log.d(TAG, "buttonClickMicrosoft: Button pressed.");
                    // Here we have an async tasks that will return our results from Microsoft
                     AsyncTask<InputStream, String, String> visionTask = new AsyncTask<InputStream, String, String>() {

                        ProgressDialog prgDialog = new ProgressDialog(getContext());
                        @Override
                        protected String doInBackground(InputStream... params) {
                            try {
                                // Progress dialog
                                publishProgress("Recognizing...");
                                // we add what features we want.
                                // TODO: Implement the other information from the json file
                                // String array of list of features I want in JSON object
                                String[] features = {"Description","Tags","ImageType", "Color", "Faces", "Adult", "Categories"};
                                String[] details = {};
                                AnalysisResult result = visionServiceRestClient.analyzeImage(params[0], features, details);

                                // This stores the gotten data into a string that is of Gson
                                String strResult = new Gson().toJson(result);

                                Log.d(TAG, strResult);
                                return strResult;

                            } catch (Exception e){
                                Toast.makeText(getActivity(), "The service is down or key is expired!", Toast.LENGTH_LONG).show();
                                return null;
                            }
                        }

                        // genereated code below, alt + inst Override, for the task execution
                        @Override
                        protected void onPreExecute() {
                            prgDialog.show();
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            prgDialog.dismiss();

                            // Convert back the string to object.
                            AnalysisResult result = new Gson().fromJson(s, AnalysisResult.class);
                            // had to declare as final View
                            desc_txtView = (TextView) view.findViewById(R.id.fragimg_desctxtview);
                            age_txtView = (TextView) view.findViewById(R.id.fragimg_faceage_txtview);

                            // We can build strings for the other parts of it as well...
                            StringBuilder desc_SB = new StringBuilder();
                            StringBuilder faceage_SB = new StringBuilder();

                            // Just for description caption...
                            for(Tag tag:result.tags)
                            {
                                desc_SB.append(tag.name + ", ");
                            }
                            desc_txtView.setText(desc_SB);

                            // TODO: Confused on above and here...
                            //for(Face face:result.faces.)

                        }

                        @Override
                        protected void onProgressUpdate(String... values) {
                            prgDialog.setMessage(values[0]);
                        }
                    };

                    try {

                        // Now we just execute it
                        visionTask.execute(inputStream);
                        Log.d(TAG, "buttonClickMicrosoft: Analysis complete!");

                    } catch (Exception e){
                        Toast.makeText(getActivity(), "The service is down or key is expired!", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e){

                    Toast.makeText(getActivity(), "The service is down or key is expired!", Toast.LENGTH_LONG).show();
                }

                // Next is the Face API, call
                // We need to wait some seconds and such.
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Now Running FaceAPI! ...", Toast.LENGTH_LONG).show();
                        // Actions to do after 3 seconds
                        // This is still a test, but this is our sat_test.jpg

                        FaceDetectionFun(bitmap, view);
                    }
                }, 3000);

            }
        });
        */
        // End of commented out code, that was use for testing purposes

        // Now set the properties of the LinearLayoutManager
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rec_view = (RecyclerView) view.findViewById(R.id.FireBaseDB_RecView);
        rec_view.setLayoutManager(mLayoutManager);

        // Don't need to check if we are signed in because you have to sign in...

        // start firebase recylce... You must define the parameters you specified below to let it know what you doing.
        FirebaseRecyclerAdapter<UserAnalyzePhoto, ImagesFragment.UserImageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserAnalyzePhoto, ImagesFragment.UserImageViewHolder>(
                UserAnalyzePhoto.class,
                R.layout.layout_analyzed_userphoto,
                ImagesFragment.UserImageViewHolder.class,
                database_user_analyzed_photos
        ) {
            @Override
            protected void populateViewHolder(ImagesFragment.UserImageViewHolder viewHolder, UserAnalyzePhoto model, int position) {

                viewHolder.txtdatainfo.setText("Data Info: " + model.getSearch_date() + " " + model.getImage_status() );
                viewHolder.txtdate.setText("Analyzed Date: " + model.getAnalyzed_date());
                viewHolder.txtlocoord.setText("Location: " + model.getTaken_location());
                viewHolder.txtstatus.setText("Status: " + model.getStatus());
                viewHolder.txtdevicename.setText("Device: " + model.getDevice_name());
                viewHolder.txttags.setText("Tags: " + model.getTags());
                viewHolder.txtdescription.setText("Description: " + model.getDescription());
                viewHolder.txtadultcontent.setText("Adult?: " + model.getAdult_content() + " | " + "Racy?: " + model.getRacy_content());
                viewHolder.txt_age_gender_smile.setText("Age: " + model.getAge() + " | " + "Gender: " + model.getGender() + " | " + "Smile %: " + model.getSmile());

                Picasso.with(getContext()).load(model.getAnalyzed_image_url()).into(viewHolder.imgUsersimg);


            }
        };

        // now we set it, and it populates!
        rec_view.setAdapter(firebaseRecyclerAdapter);

        return view;

    } // End of onCreate View

    // This is for our URL, need to pass the view in
    private void url_AnalyzeImage(final View view){

         AsyncTask<String, String, String> url_visionTask = new AsyncTask<String, String, String>() {
             ProgressDialog progressDialog3 = new ProgressDialog(getContext());

            @Override
            protected String doInBackground(String... strings) {
                // Progress dialog
                publishProgress("From URL Recognizing...");
                // We can use
                Log.d(TAG, "buttonClickUsing URL: Analyze Image using URL.");
                try {
                    String[] url_features = {"Description","Tags","ImageType", "Color", "Faces", "Adult", "Categories"};
                    String[] url_details = {};
                    AnalysisResult url_result = visionServiceRestClient.analyzeImage(fromDatabase_AL.get(0).getImgdata_url().toString(), url_features , url_details);
                    Log.d(TAG, "buttonClickUsing URL: analyze image URL working...");
                    // This stores the gotten data into a string that is of Gson
                    String url_strResult = new Gson().toJson(url_result);

                    Log.d(TAG, url_strResult);
                    return url_strResult;

                } catch (VisionServiceException e) {
                    Toast.makeText(getActivity(), "The Image API Service is experiencing difficulties!", Toast.LENGTH_LONG).show();
                    Log.d(TAG,"URL button Image API: Failure at RestClient");
                    return null;
                }
            }

            @Override
            protected void onPreExecute() {
                progressDialog3.show();
            }

            @Override
            protected void onPostExecute(String s) {
                // Now this is where we collect the results and stuff!
                progressDialog3.dismiss();
                // Convert back the string to object.
                AnalysisResult result = new Gson().fromJson(s, AnalysisResult.class);
                // had to declare as final View
                desc_txtView = (TextView) view.findViewById(R.id.fragimg_desctxtview);
                age_txtView = (TextView) view.findViewById(R.id.fragimg_faceage_txtview);

                // We can build strings for the other parts of it as well...
                StringBuilder desc_SB = new StringBuilder();
                StringBuilder faceage_SB = new StringBuilder();

                // Just for description caption...
                for(Tag tag:result.tags)
                {
                    desc_SB.append("From URL: " + tag.name + ", ");
                }
                desc_txtView.setText(desc_SB);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                progressDialog3.setMessage(values[0]);
            }
        };

    }


    // Face API method, I also pass the current view for setting the text for age and gender.
    private void FaceDetectionFun(final Bitmap a_bitmap, final View v) {

        Log.d(TAG, "FaceAPI: Starting...");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        a_bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        final AsyncTask<InputStream, String, com.microsoft.projectoxford.face.contract.Face[]> detectTask = new AsyncTask<InputStream, String, com.microsoft.projectoxford.face.contract.Face[]>() {
            private ProgressDialog progress2 = new ProgressDialog(getContext());

            // Post


            @Override
            protected void onPostExecute(com.microsoft.projectoxford.face.contract.Face[] faces) {
                // After
                progress2.dismiss();
                if (faces == null) return;

                imageView.setImageBitmap(drawFaceRecOnBitmap(a_bitmap, faces, v));
            }

            @Override
            protected void onPreExecute() {
                Log.d(TAG, "FaceAPI: PreExecute...");
                super.onPreExecute();
                progress2.show();

            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                progress2.setMessage(values[0]);
            }

            @Override
            protected com.microsoft.projectoxford.face.contract.Face[] doInBackground(InputStream... params) {
                publishProgress("Detecing doinBackground, FaceAPI...");
                Log.d(TAG, "Do in background starting, faceapi");
                try {

                    com.microsoft.projectoxford.face.contract.Face[] result = faceServiceClient.detect(params[0], true, false,
                            new FaceServiceClient.FaceAttributeType[] {
                                    FaceServiceClient.FaceAttributeType.Age,
                                    FaceServiceClient.FaceAttributeType.Gender,
                                    FaceServiceClient.FaceAttributeType.FacialHair,
                                    FaceServiceClient.FaceAttributeType.Smile,
                                    FaceServiceClient.FaceAttributeType.HeadPose,
                                }
                            );
                    if(result == null){
                        Log.d(TAG, "Do in background nothing found, faceapi");
                        publishProgress("Detection, FaceAPI, Nothing was found.");
                        return null;

                    }
                    publishProgress(String.format("Detection Finished. %d face(s) detected.", result.length));
                    return result;


                }
                catch (Exception e){
                    Log.d(TAG, "Fail on FaceAPI asynctask...");
                    return null;
                }

            }
        };

        // execute the face api
        detectTask.execute(inputStream);


    }

    // This is Face contract NOT vision
    public Bitmap drawFaceRecOnBitmap(Bitmap a_bitmap, com.microsoft.projectoxford.face.contract.Face[] faces, View v) {

        Log.d(TAG, "drawFaceRecOnBitmap: FaceAPI");

        Bitmap new_bitmap = a_bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(new_bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        float strokewidth = 12;
        paint.setStrokeWidth(strokewidth);
        boolean checker = false;
        if(faces != null){
            checker = true;
            // This checks the JSON file and we can draw rectangles. We can also do other data
            // And save data to a list, and upload the new image and such as that.
            // TODO: Make a new model for analyzed user images.
            for(com.microsoft.projectoxford.face.contract.Face face:faces){

                com.microsoft.projectoxford.face.contract.FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint
                        );

                Log.d(TAG, "Found info and drawing...: FaceAPI");
                // Show Face Details
                // we also set our data to description...
                StringBuilder face_age_SB = new StringBuilder();
                face_age_SB.append("Age: " + face.faceAttributes.age + ", ");
                face_age_SB.append("Gender: " + face.faceAttributes.gender + ", ");
                face_age_SB.append("Smile: " + face.faceAttributes.smile);
                age_txtView = (TextView) v.findViewById(R.id.fragimg_faceage_txtview);
                age_txtView.setText(face_age_SB);

            }
        }

        if(!checker){
            Log.d(TAG, "drawFaceRecOnBitmap: FaceAPI, No face was found!");
        }

        return new_bitmap;

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


    // VIEWHOLDER FOR ANALYZED IMAGES

    // UserImageViewHolder is used as a layout to populate for the recycler view.

    public static class UserImageViewHolder extends RecyclerView.ViewHolder{

        TextView txtdatainfo;
        TextView txtdate;
        TextView txtlocoord;
        TextView txtstatus;
        TextView txtdevicename;
        TextView txttags;
        TextView txtdescription;
        TextView txtadultcontent;
        TextView txt_age_gender_smile;

        ImageView imgUsersimg;

        public UserImageViewHolder(View itemView){
            super(itemView);
            txtdatainfo = (TextView) itemView.findViewById(R.id.lay_anau_datainfotxtview);
            txtdate = (TextView) itemView.findViewById(R.id.lay_anau_analyzeddate_txtview);
            txtlocoord = (TextView) itemView.findViewById(R.id.lay_anau_location_txtview);
            txtstatus = (TextView) itemView.findViewById(R.id.lay_anau_status_txtview);
            txtdevicename = (TextView) itemView.findViewById(R.id.lay_anau_devicename_txtview);
            txttags = (TextView) itemView.findViewById(R.id.lay_anau_tags_txtview);
            txtdescription = (TextView) itemView.findViewById(R.id.lay_anau_description_txtview);
            txtadultcontent = (TextView) itemView.findViewById(R.id.lay_anau_isadultracy_txtview);
            txt_age_gender_smile = (TextView) itemView.findViewById(R.id.lay_anau_agegendersmile_txtview);
            imgUsersimg = (ImageView) itemView.findViewById(R.id.lay_anau_imageView);

        }


    } // End of UserImageViewHolder











}
