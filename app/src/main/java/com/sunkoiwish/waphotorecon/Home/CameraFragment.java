package com.sunkoiwish.waphotorecon.Home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sunkoiwish.waphotorecon.Models.Photo;
import com.sunkoiwish.waphotorecon.Models.UserPhoto;
import com.sunkoiwish.waphotorecon.R;
import com.sunkoiwish.waphotorecon.Utils.ImageFixer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Xaiyeon on 10/24/2017.
 */

@RuntimePermissions
public class CameraFragment extends Fragment{

    // Used for logging
    private static final String TAG = "CameraFragment";

    final Context mcameraFragment = getContext();

    // Define resources
    Button btnTakePic;
    ImageView imageView;
    EditText editText;
    Button btnUpFireBase;

    // for progress
    ProgressDialog progressDialog;

    // define camera constant
    final int RC_TAKE_PHOTO = 10;

    // Define FireBase
    private FirebaseApp app;
    private FirebaseAuth auth;
    // Creating StorageReference and DatabaseReference object.
    private StorageReference mstorageReference;
    // database base on Photo class
    private DatabaseReference databasePhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        // Firebase instances
        app = FirebaseApp.getInstance();
        auth = FirebaseAuth.getInstance(app);

        // Connect to firebase cloud storage
        mstorageReference = FirebaseStorage.getInstance().getReference();
        // Database reference for our Photo
        databasePhoto = FirebaseDatabase.getInstance().getReference("allphotos");

        // need to use for permissions
        final CameraFragment cameraFragment = this;

        TextView textView = (TextView) view.findViewById(R.id.camera_txtview);
        editText = (EditText) view.findViewById(R.id.imagedesc_editTxt);

        // Now we will define an imageView for to show to captured image
        imageView = (ImageView) view.findViewById(R.id.camfrag_imageView);

        // init buttons
        btnTakePic = (Button) view.findViewById(R.id.camera_photo_btn);

        // FireBase Upload Button and onClickListener... NOT USED
        btnUpFireBase = (Button) view.findViewById(R.id.add_firebasephoto_btn);
        btnUpFireBase.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view1){

            }
        });

        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this gets permission
                Log.d(TAG, "setOnClickListener: Clicked camera_photo_btn...");
                CameraFragmentPermissionsDispatcher.takePhotoAndUploadWithPermissionCheck(cameraFragment);
            }
        });

        return view;
    }


    /**
     *  Below is a new edit that we are running and testing
     */

    @NeedsPermission({Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.CAMERA, Manifest.permission.SEND_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void takePhotoAndUpload() {
        // Our photo activity is now launch for camera
        Log.d(TAG, "onActivityResult: Starting camera...");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RC_TAKE_PHOTO);

    }

    @OnShowRationale({Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.CAMERA, Manifest.permission.SEND_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void takePhotoAndUploadRationale(final PermissionRequest request) {
        // This displays an alert dialogue to user asking for permissions
        new AlertDialog.Builder(mcameraFragment)
                .setMessage("To take photo and store., enable Camera and Storage")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        request.proceed();
                    }
                } )
                .setNegativeButton("Deny", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        request.cancel();
                    }
                })
                .show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.CAMERA, Manifest.permission.SEND_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void takePhotoAndUploadNever() {

        Toast.makeText(mcameraFragment, "You have denied permission!", Toast.LENGTH_LONG).show();

    }


    // Fire base storage and database automatically create a background task.

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: pass super.");
        // Maybe shrink image
        // Make an else to print log for not working
        if (requestCode == RC_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: Now onActivityResult...");

            Uri newUri = data.getData();
            Bitmap bitmap = null;
            File filep = null;
            Uri the_storage_uri = null;

            // We will shrink this photo man... If it works...
            // So what we are trying to do now is save this bitmap to the phone's storage and
            // retrieve the filepath of the new shrunken image and upload that version.

            try {
                // We fix the image, then we store it.
                bitmap = ImageFixer.handleSamplingAndRotationBitmap(getContext(),newUri);
                Log.d(TAG, "onActivityResult: Passed resizing of the image...");
                // now we store it and use that file for uploading.
                filep = ImageFixer.storeImage(bitmap, getContext());
                Log.d(TAG, "onActivityResult: Passed storing to SDCARD...");
                the_storage_uri = ImageFixer.getImageContentUri(getContext(),filep);
                Log.d(TAG, "onActivityResult: Passed generating the URI...");
                //bitmap = MediaStore.Images.Media.getBitmap(mcameraFragment.getContentResolver(), newUri);

            } catch (IOException e) {
                Toast.makeText(getActivity(), "FAILED TO CAPTURE AND RESIZE IMAGE.", Toast.LENGTH_LONG).show();
            }


            //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

            // Progress
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Performing operations...");
            progressDialog.show();

            Log.d(TAG, "onActivityResult: processing...");
            // Now we use newUri for storing photo into FireBase storage
            // newUri = data.getData();

            // this is where we put the image in cloud fire base storage, in regards to user

            String user_id = auth.getCurrentUser().getUid().toString();

            StorageReference uploadTask  = mstorageReference.child("photos/users/" + user_id.replace("/", "")).child(newUri.getLastPathSegment());
            // Now we trying to upload image...
            Log.d(TAG, "onActivityResult: Trying to Upload to Fire Base Storage");

            uploadTask.putFile(the_storage_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //TO DO
                    // TO:DO Get URI from the database and store into real time database
                    Toast.makeText(getActivity(), "Image Storage Upload Done!", Toast.LENGTH_LONG).show();

                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    // Now we store that main URL for real-time database
                    //storage_downloadURL = downloadUrl;

                    Toast.makeText(getActivity(), "Starting real-time database upload!", Toast.LENGTH_LONG).show();
                    String photo_name = "";
                    Date currentTime = Calendar.getInstance().getTime();

                    // Photo name is auto generated, main parts to class
                    String date_time_s = currentTime.toString();
                    photo_name = photo_name + "WA " + date_time_s.substring(0,date_time_s.length());
                    String description = editText.getText().toString();

                    // This generates a random ID for our database ID
                    String UID = databasePhoto.push().getKey();

                    // our actual download of image url
                    String main_data_URL = downloadUrl.toString();
                    // Preparing image for upload to Cloud Storage, this is the URL we store in real-time database

                    // Now we have everything we need to store on real-time database according to our model class.
                    Photo nphoto = new Photo(UID, auth.getCurrentUser().getUid().toString() ,photo_name, main_data_URL, description, date_time_s);

                    // Now to store into our database!! Now it's in our real-time database
                    // This is for ALL THE PHOTOS
                    databasePhoto.child(UID).setValue(nphoto);

                    // Next we store the photo into the real-time database for just that user so we can just pull that user's photos...
                    databasePhoto = FirebaseDatabase.getInstance().getReference("userphotos").child(auth.getCurrentUser().getUid().toString());
                    UserPhoto userPhoto = new UserPhoto(UID, auth.getCurrentUser().getUid().toString(), photo_name, main_data_URL, description, date_time_s);
                    databasePhoto.child(UID).setValue(nphoto);

                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Upload Success to Real-Time Database!", Toast.LENGTH_LONG).show();

                }
            }); // add failure method

        }
        else
        {
            // Log an error
            Log.d(TAG, "onActivityResult: Failure to camera upload.");

        }

    }

}
