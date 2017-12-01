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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.*;
import com.sunkoiwish.waphotorecon.R;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

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
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_images, container, false);

        // setting our test image
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.devin_boy);
        // fINDING OUR view objects

        imageView = (ImageView) view.findViewById(R.id.fragimg_imageView);
        mbutton = (Button) view.findViewById(R.id.fragimg_submitbtn);
        desc_txtView = (TextView) view.findViewById(R.id.fragimg_desctxtview);
        age_txtView = (TextView) view.findViewById(R.id.fragimg_faceage_txtview);

        imageView.setImageBitmap(bitmap);

        // Convert the image to stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

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

    //                        StringBuilder stringBuilder = new StringBuilder();
    //
    //                        // Added this for faces stringBuilder
    //                        StringBuilder facestringBuilder = new StringBuilder();
    //                        // String Face;
    //
    //                        // age
    //                        for(Face face:result.faces){
    //                            facestringBuilder.append(face.age);
    //                        }
    //                        age_txtView.setText(facestringBuilder);
    //
    //
    //                        // description
    //                        for(Caption caption:result.description.captions){
    //                            stringBuilder.append(caption.text);
    //                        }
    //                        // Set our description here
    //                        desc_txtView.setText(stringBuilder);

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

        return view;
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
        if(faces != null){
            boolean checker = false;
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
        if(!checker){
            Log.d(TAG, "drawFaceRecOnBitmap: FaceAPI, No face was found!");
        }

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
}
