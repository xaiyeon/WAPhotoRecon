package com.sunkoiwish.waphotorecon.Connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;
import com.microsoft.projectoxford.vision.contract.Tag;
import com.squareup.picasso.Picasso;
import com.sunkoiwish.waphotorecon.Models.UserAnalyzePhoto;
import com.sunkoiwish.waphotorecon.Models.UserPhoto;
import com.sunkoiwish.waphotorecon.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConnectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConnectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String TAG = "ConnectFragment: ";

    // Define FireBase
    private FirebaseApp app;
    private FirebaseAuth auth;

    // Define the photos Firebase DatabaseReference
    private DatabaseReference database_photos;

    // Used to store analyze images
    private DatabaseReference db_analyze_photos;

    private DatabaseReference update_database_photos;

    // Creating StorageReference and DatabaseReference object.
    private StorageReference mstorageReference;

    // An arrayList of our objects...
    final ArrayList<UserPhoto> fromRTDatabase_AL = new ArrayList<UserPhoto>();

    // Layouts
    RecyclerView rec_view;
    private LinearLayoutManager mLayoutManager;

    // My subscription key for microsoft cog service
    // If expire use new keys...
    // old key: c8fc6c87dde64e5888e49d65cf3d2569
    // new key: 5d4298ebea2842148d97033a7fe95a61
    VisionServiceRestClient visionServiceRestClient = new VisionServiceRestClient("5d4298ebea2842148d97033a7fe95a61", "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");

    // Face API
    FaceServiceClient faceServiceClient = new FaceServiceRestClient("https://westcentralus.api.cognitive.microsoft.com/face/v1.0", "b50c03b4bb8a4608826e2dcc2fb1cec8");


    // These Global Variables will be used for storing Analyzed information to our storage and database
    public String a_user_analyze_photo_id = ""; // primary key
    public String a_display_name = "";
    public String a_user_id = ""; // foreign key, from user auth
    public String a_photo_id = ""; // foreign key, from photo database
    public String a_analyzed_image_url = ""; // this is from the storage of the analyzed image
    public String a_is_analyzed = ""; // becomes true
    public String a_status = ""; // will be used for validity later on
    // below is basically Strings from the JSON object.
    public String a_tags = ""; // for like what it think it is
    public String a_description = "";
    public String a_adult_content = "";
    public String a_racy_content = "";
    public String a_age = "";
    public String a_gender = "";
    public String a_smile = "";

    // Used for storing into the storage.
    public Bitmap a_analyzed_bitmap;
    public String a_analyzed_date;
    public String a_image_status;
    public String a_search_date;
    public String a_taken_location;
    public String a_devince_name;

    // For storage upload task
    public byte[] aa_data;

    // for progress
    ProgressDialog progressDialog;

    // for waiting for Detection face
    public Boolean wait_please = false;

    public ConnectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConnectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConnectFragment newInstance(String param1, String param2) {
        ConnectFragment fragment = new ConnectFragment();
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
        // Firebase instances
        app = FirebaseApp.getInstance();
        auth = FirebaseAuth.getInstance(app);

        // get from out database of the userphotos photos
        database_photos = FirebaseDatabase.getInstance().getReference("userphotos").child(auth.getCurrentUser().getUid().toString());

        // to store our analyzed data;
        db_analyze_photos = FirebaseDatabase.getInstance().getReference("allanalyzedphotos");

        //
        update_database_photos = FirebaseDatabase.getInstance().getReference("userphotos");

        // Connect to firebase cloud storage
        mstorageReference = FirebaseStorage.getInstance().getReference();

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_connect, container, false);


        // Now set the properties of the LinearLayoutManager
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);


        rec_view = (RecyclerView) view.findViewById(R.id.FireBaseConnect_Image_RecView);
        rec_view.setLayoutManager(mLayoutManager);
        //rec_view.setAdapter(new Adapter(getContext(), d));

        // Don't need to check if we are signed in because you have to sign in...

        // start firebase recylce... You must define the parameters you specified below to let it know what you doing.
        FirebaseRecyclerAdapter<UserPhoto, ConnectFragment.UserImageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserPhoto, ConnectFragment.UserImageViewHolder>(
                UserPhoto.class,
                R.layout.layout_analyze_a_photo,
                ConnectFragment.UserImageViewHolder.class,
                database_photos
        ) {
            @Override
            protected void populateViewHolder(final ConnectFragment.UserImageViewHolder viewHolder, final UserPhoto model, final int position) {

                viewHolder.txtname.setText(model.getUser_name());
                viewHolder.txttitle.setText(model.getPhoto_name());
                viewHolder.txtdate.setText(model.getPhoto_create_date());
                Picasso.with(getContext()).load(model.getImgdata_url()).into(viewHolder.imgUsersimg);
                viewHolder.txtanalyzed.setText("Analyzed?: " + model.getIs_analyzed()); // is true or false
                viewHolder.txtdevicename.setText("Device: " + model.getDevice_name());
                viewHolder.txtdatainfo.setText("Data Info: " + model.getSearch_date() + " " + model.getImage_status() );
                viewHolder.button_analyze.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(model.getIs_analyzed().equals("true"))
                        {
                            Toast.makeText(getContext(), "This image has already been analyzed!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            // Code here for the operations

                            Log.d(TAG, "buttonClickMicrosoft: Button pressed.");
                            Toast.makeText(getContext(), "Click at position: " + position, Toast.LENGTH_LONG).show();
                            // Getting ready to send that image...
                            BitmapDrawable drawable = (BitmapDrawable) viewHolder.imgUsersimg.getDrawable();
                            final Bitmap bitmap = drawable.getBitmap();

                            // Convert the image to stream
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());


                            try {

                                // Progress
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setTitle("Please wait...");
                                progressDialog.setMessage("Performing Analyze operations...");
                                progressDialog.show();

                                Log.d(TAG, "buttonClickMicrosoft: Button pressed.");

                                // Here we have an async tasks that will return our results from Microsoft
                                AsyncTask<InputStream, String, String> visionTask = new AsyncTask<InputStream, String, String>() {

                                    //ProgressDialog prgDialog = new ProgressDialog(getContext());
                                    @Override
                                    protected String doInBackground(InputStream... params) {
                                        try {
                                            // Progress dialog
                                            //publishProgress("Recognizing...");
                                            // we add what features we want.
                                            // TODO: Implement the other information from the json file
                                            progressDialog.setMessage("Performing: Microsoft Cognitive Vision API...");

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
                                            progressDialog.dismiss();
                                            return null;
                                        }
                                    }

                                    // genereated code below, alt + inst Override, for the task execution
                                    @Override
                                    protected void onPreExecute() {

                                        //prgDialog.show();
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        //prgDialog.dismiss();

                                        // Convert back the string to object.
                                        AnalysisResult result = new Gson().fromJson(s, AnalysisResult.class);
                                        // had to declare as final View
                                        //desc_txtView = (TextView) view.findViewById(R.id.fragimg_desctxtview);
                                        //age_txtView = (TextView) view.findViewById(R.id.fragimg_faceage_txtview);

                                        // We can build strings for the other parts of it as well...
                                        StringBuilder tags_SB = new StringBuilder();
                                        StringBuilder descr_SB = new StringBuilder();
                                        StringBuilder adult_SB = new StringBuilder();
                                        StringBuilder racy_SD = new StringBuilder();


                                        for(Tag tag:result.tags)
                                        {
                                            tags_SB.append(tag.name + ", ");
                                        }
                                        //desc_txtView.setText(desc_SB);
                                        // Just for description caption...
                                        for(Caption caption:result.description.captions){
                                            descr_SB.append(caption.text + ". ");
                                        }

                                        boolean adult_sb = result.adult.isAdultContent;
                                        boolean racy_sb = result.adult.isRacyContent;



                                        // COLLECTING ANALYSIS

                                        a_tags = tags_SB.toString();
                                        a_description = descr_SB.toString();
                                        a_adult_content = Boolean.toString(adult_sb);
                                        a_racy_content = Boolean.toString(racy_sb);

                                    }

                                    @Override
                                    protected void onProgressUpdate(String... values) {
                                        //prgDialog.setMessage(values[0]);
                                    }
                                };

                                try {

                                    // Now we just execute it

                                    visionTask.execute(inputStream);
                                    Log.d(TAG, "buttonClickMicrosoft: Analysis complete!");
                                    progressDialog.setMessage("Performing: Microsoft Face Vision API...");

                                } catch (Exception e){
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "The service is down or key is expired!", Toast.LENGTH_LONG).show();
                                }


                            } catch (Exception e){
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "The service is down or key is expired!", Toast.LENGTH_LONG).show();
                            }

                            // Next is the Face API, call
                            // We need to wait some seconds and such.
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    //Toast.makeText(getActivity(), "Now Running FaceAPI! ...", Toast.LENGTH_LONG).show();
                                    // Actions to do after 3 seconds, or when finishes

                                    // Our new bitmap is here, but now we need to save it and convert
                                    FaceDetectionFun(bitmap, view, viewHolder);

                                    // Another handler, that will wait for the above function
                                    Handler uploadhandler = new Handler();
                                    uploadhandler.postDelayed(new Runnable() {
                                        public void run() {

                                            // PUT CODE HERE, runs after around 10 seconds, or when finishes

                                            // Okay
                                            // Get the data from an ImageView as bytes
                                            viewHolder.imgUsersimg.setDrawingCacheEnabled(true);
                                            viewHolder.imgUsersimg.buildDrawingCache();
                                            a_analyzed_bitmap = viewHolder.imgUsersimg.getDrawingCache();
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            a_analyzed_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                            aa_data = baos.toByteArray();


                                            // After all that we can now upload the image to FireBase storage
                                            // Now we store the data into the real-time database
                                            // Adding a try catch for upload service
                                            // TODO: Resolve
                                            final String user_id = auth.getCurrentUser().getUid().toString();
                                            try {

                                                progressDialog.setMessage("Performing: Storage and Database Upload to FireBase...");

                                                String analyzed_file_name = "Analyzed_IMG_" + model.getSearch_date() + "_" + model.getImage_status() + ".jpg";

                                                // What should we name the file?
                                                StorageReference uploadTask = mstorageReference.child("users/" + user_id + "/").child("analyzedphotos/").child(analyzed_file_name);
                                                // Now we trying to upload image...
                                                Log.d(TAG, "onActivityResult: Trying to Upload to Fire Base Storage");

                                                uploadTask.putBytes(aa_data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        //TO DO
                                                        // TO:DO Get URI from the database and store into real time database
                                                        //Toast.makeText(getActivity(), "Image Storage Upload Done!", Toast.LENGTH_LONG).show();

                                                        @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                                        // Now we store that main URL for real-time database
                                                        //storage_downloadURL = downloadUrl;

                                                        //Toast.makeText(getActivity(), "Starting real-time database upload!", Toast.LENGTH_LONG).show();
                                                        Date currentTime = Calendar.getInstance().getTime();
                                                        // Photo name is auto generated, main parts to class
                                                        String date_time_s = currentTime.toString();

                                                        // This generates a random ID for our database ID
                                                        String UID = db_analyze_photos.push().getKey();

                                                        // The user's uid
                                                        String users_uid = auth.getCurrentUser().getUid().toString();

                                                        // our actual download of image url
                                                        a_analyzed_image_url = downloadUrl.toString();
                                                        // Preparing image for upload to Cloud Storage, this is the URL we store in real-time database
                                                        // Now we have everything we need to store on real-time database according to our model class.
                                                        Log.d(TAG, "UploadTask: Now uploading to real-time database");
                                                        // ANALYZE IMAGE SETTINGS
                                                        a_is_analyzed = "true";
                                                        a_status = "private";
                                                        a_analyzed_date = date_time_s;
                                                        a_devince_name = model.getDevice_name();

                                                        // Next we store the photo into the real-time database for just that user so we can just pull that user's photos...
                                                        // The analyzed photos will now be stored into the RT DB
                                                        db_analyze_photos = FirebaseDatabase.getInstance().getReference("allanalyzedphotos");
                                                        UserAnalyzePhoto userAnalyzePhoto = new UserAnalyzePhoto(UID, auth.getCurrentUser().getDisplayName().toString(),auth.getCurrentUser().getUid().toString(),
                                                                model.getPhoto_id(), a_analyzed_image_url, a_is_analyzed, a_status, a_tags, a_description,
                                                                a_adult_content, a_racy_content, a_age, a_gender, a_smile, a_analyzed_date, model.getImage_status(), model.getSearch_date(),
                                                                model.getTaken_location(), a_devince_name);
                                                        db_analyze_photos.child(UID).setValue(userAnalyzePhoto);

                                                        // Next we store the photo into the real-time database for just that user so we can just pull that user's photos...
                                                        db_analyze_photos = FirebaseDatabase.getInstance().getReference("useranalyzedphotos").child(auth.getCurrentUser().getUid().toString());
                                                        db_analyze_photos.child(UID).setValue(userAnalyzePhoto);


                                                        // UPDATE
                                                        // Now lets update that field for is_analyzed
                                                        DatabaseReference update_db_uphotos = FirebaseDatabase.getInstance().getReference("userphotos");
                                                        update_db_uphotos.child(users_uid).child(model.getPhoto_id()).child("is_analyzed").setValue("true");

                                                        // For like all photos
                                                        DatabaseReference update_db_photos = FirebaseDatabase.getInstance().getReference("allphotos");
                                                        update_db_photos.child(model.getPhoto_id()).child("is_analyzed").setValue("true");

                                                        progressDialog.dismiss();
                                                        Toast.makeText(getActivity(), "Upload Success to Real-Time Database for Analyzed Image!", Toast.LENGTH_LONG).show();

                                                    }
                                                }); // add failure method
                                            } catch (Exception e)
                                            {
                                                progressDialog.dismiss();
                                                Log.d(TAG, "onUploadTask: Failure to connect to FireBase upload or service failed.");

                                                Toast.makeText(getActivity(), "Connection to service has failed.", Toast.LENGTH_LONG);
                                            }

                                        }
                                    }, 10000);

                                    // We needed to wait here

                                }
                            }, 3000);


                        }

                    }
                });

            }

            @Override
            public void onBindViewHolder(final UserImageViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

            }

        };

        // now we set it, and it populates!
        rec_view.setAdapter(firebaseRecyclerAdapter);


        //Intent intent = new Intent();
        //intent.putExtra("photo_list", fromRTDatabase_AL);


        // TODO: Add a firebase query to sync pictures
        // Add two buttons, one for syncing and one for analyizing the images and storing
        // into the firebase database

        // Add a new field to the photos called isAnalyzed, so this way we only fetch photos
        // that have not been analyzed already from the database.

        // Help:
        // https://stackoverflow.com/questions/21250339/how-to-pass-arraylistcustomeobject-from-one-activity-to-another




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

    public static class UserImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtname;
        TextView txttitle;
        TextView txtdate;
        ImageView imgUsersimg;
        TextView txtanalyzed;
        TextView txtdatainfo;
        Button button_analyze;
        TextView txtdevicename;

        private ClickHandler clickHandler;

        public UserImageViewHolder(View itemView){
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.lay_analyze_name_txtview);
            txttitle = (TextView) itemView.findViewById(R.id.lay_analyze_title_txtview);
            txtdate = (TextView) itemView.findViewById(R.id.lay_analyze_date_txtview);
            imgUsersimg = (ImageView) itemView.findViewById(R.id.lay_analyze_imageView);
            txtanalyzed = (TextView) itemView.findViewById(R.id.lay_analyze_isanalyzed_txtview);
            txtdatainfo = (TextView) itemView.findViewById(R.id.lay_analyze_data_info_txtview);
            button_analyze = (Button) itemView.findViewById(R.id.lay_analyze_analyzeButton);
            txtdevicename = (TextView) itemView.findViewById(R.id.lay_analyze_devicename_txtview);
        }


        @Override
        public void onClick(View v) {
            if (clickHandler != null) {
                clickHandler.onMyButtonClicked(getAdapterPosition());
            }
        }
    } // End of UserImageViewHolder

    // Used above
    private interface ClickHandler {
        void onMyButtonClicked(final int position);
    }

    // Face API method, I also pass the current view for setting the text for age and gender.
    private void FaceDetectionFun(final Bitmap a_bitmap, final View v, final ConnectFragment.UserImageViewHolder viewHolder) {

        Log.d(TAG, "FaceAPI: Starting...");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        a_bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        final AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, com.microsoft.projectoxford.face.contract.Face[]>() {
            //private ProgressDialog progress2 = new ProgressDialog(getContext());

            // Post


            @Override
            protected void onPostExecute(com.microsoft.projectoxford.face.contract.Face[] faces) {
                // After
                //progress2.dismiss();
                if (faces == null) return;

                viewHolder.imgUsersimg.setImageBitmap(drawFaceRecOnBitmap(a_bitmap, faces, v));
                a_analyzed_bitmap = drawFaceRecOnBitmap(a_bitmap, faces, v);
                wait_please = true;
                // We can upload this?
            }

            @Override
            protected void onPreExecute() {
                Log.d(TAG, "FaceAPI: PreExecute...");
                super.onPreExecute();
                //progress2.show();

            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                //progress2.setMessage(values[0]);
            }

            @Override
            protected com.microsoft.projectoxford.face.contract.Face[] doInBackground(InputStream... params) {
                //publishProgress("Detecing doinBackground, FaceAPI...");
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
                        //publishProgress("Detection, FaceAPI, Nothing was found.");
                        return null;

                    }
                    //publishProgress(String.format("Detection Finished. %d face(s) detected.", result.length));
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

                // SETTING ANALYSIS, ANALYZE ANALYSIS
                a_age = Double.toString(face.faceAttributes.age);
                a_gender = face.faceAttributes.gender;
                a_smile = Double.toString(face.faceAttributes.smile);

                //age_txtView = (TextView) v.findViewById(R.id.fragimg_faceage_txtview);
                //age_txtView.setText(face_age_SB);
                Log.d(TAG, face_age_SB.toString());
                //Toast.makeText(getContext(), face_age_SB.toString(), Toast.LENGTH_LONG);

            }
        }
        if(!checker){
            a_age = "N/A";
            a_gender = "N/A";
            a_smile = "N/A";

            Log.d(TAG, "drawFaceRecOnBitmap: FaceAPI, No face was found!");
        }
        a_analyzed_bitmap = new_bitmap;
        return new_bitmap;

    }


}
