package com.sunkoiwish.waphotorecon.Images;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;
import com.microsoft.projectoxford.vision.contract.Face;
import com.sunkoiwish.waphotorecon.R;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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

    VisionServiceRestClient visionServiceRestClient = new VisionServiceRestClient("c8fc6c87dde64e5888e49d65cf3d2569", "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");

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

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sad_test);
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

                Log.d(TAG, "buttonClickMicrosoft: Button pressed.");
                // Here we have an async tasks that will return our results from Microsoft
                final AsyncTask<InputStream, String, String> visionTask = new AsyncTask<InputStream, String, String>() {

                    ProgressDialog prgDialog = new ProgressDialog(getContext());
                    @Override
                    protected String doInBackground(InputStream... params) {
                        try {
                            publishProgress("Recognizing...");
                            // we add what features we want.
                            // TODO: Implement the other information from the json file
                            String[] features = {"Description","Tags","ImageType", "Color", "Faces", "Adult", "Categories"};
                            String[] details = {};
                            AnalysisResult result = visionServiceRestClient.analyzeImage(params[0], features, details);

                            // This stores the gotten data into a string that is of Gson
                            String strResult = new Gson().toJson(result);
                            return strResult;

                        } catch (Exception e){
                            return null;
                        }
                    }

                    // genereated, alt + inst Override

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
                        for(Caption caption:result.description.captions)
                        {
                            desc_SB.append(caption.text);
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

                // Now we just execute it
                Log.d(TAG, "buttonClickMicrosoft: Analysis complete!");
                visionTask.execute(inputStream);

            }
        });

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
