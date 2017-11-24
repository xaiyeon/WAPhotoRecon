package com.sunkoiwish.waphotorecon.Home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import java.util.List;
import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Xaiyeon on 10/24/2017.
 */

@RuntimePermissions
public class CameraFragment extends Fragment {

    // Used for logging
    private static final String TAG = "CameraFragment";

    final Context mcameraFragment = getContext();

    // used for getting location
    private LocationManager locationManager;
    private LocationListener locationListener;
    public String cur_location = "N/A";

    public Double s_lat = 1.0;
    public Double s_long = 1.0;
    public List<Address> addresses;
    public String a_location_name = "N/A";

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

    // Here we will do the location and geocoder stuff
    Geocoder geocoder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        geocoder = new Geocoder(getContext(), Locale.getDefault());

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

        // location
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                cur_location = "Lat: " + location.getLatitude() + " | " + "Long: " + location.getLongitude();
                s_lat = location.getLatitude();
                s_long = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent c_intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(c_intent);

            }
        };


        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this gets permission
                Log.d(TAG, "setOnClickListener: Clicked camera_photo_btn...");
                CameraFragmentPermissionsDispatcher.cameraStoreLocNeedsWithPermissionCheck(cameraFragment);
            }
        });

        return view;

    } // END OF ON CREATE

    // Fire base storage and database automatically create a background task.

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: pass super.");
        // Maybe shrink image
        // Make an else to print log for not working

        geocoder = new Geocoder(getContext(), Locale.getDefault());

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

            // Adding a try catch for upload service
            try {

                StorageReference uploadTask = mstorageReference.child("photos/users/" + user_id.replace("/", "")).child(newUri.getLastPathSegment());
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
                        photo_name = photo_name + "WA " + date_time_s.substring(0, date_time_s.length());
                        String description = editText.getText().toString();

                        // This generates a random ID for our database ID
                        String UID = databasePhoto.push().getKey();

                        // our actual download of image url
                        String main_data_URL = downloadUrl.toString();
                        // Preparing image for upload to Cloud Storage, this is the URL we store in real-time database

                        // Get the device's name
                        String user_device_name = Build.MANUFACTURER + " " + Build.MODEL + " " + Build.VERSION.RELEASE;

                        try {
                            addresses = geocoder.getFromLocation(s_lat, s_long, 1);

                            String address = addresses.get(0).getAddressLine(0);
                            String area = addresses.get(0).getLocality();
                            String city = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalcode = addresses.get(0).getPostalCode();
                            String areax = "";

                            String fulllocationname = address + ", " + area + ", " + city + ", " + country + ", " + postalcode;
                            String xfulllocationname = address + ", " + city + ", " + country;
                            a_location_name = xfulllocationname;

                        } catch (IOException e) {
                            Log.d(TAG, "onActivityResult, uploadTask: Failed to get address");
                        }


                        // Now we have everything we need to store on real-time database according to our model class.
                        // Photo does not include is analyzed or
                        Photo nphoto = new Photo(UID, auth.getCurrentUser().getUid().toString(), auth.getCurrentUser().getDisplayName().toString(), photo_name, main_data_URL, cur_location, a_location_name, description, date_time_s, user_device_name, "false" );

                        // Now to store into our database!! Now it's in our real-time database
                        // This is for ALL THE PHOTOS
                        databasePhoto.child(UID).setValue(nphoto);

                        // Next we store the photo into the real-time database for just that user so we can just pull that user's photos...
                        databasePhoto = FirebaseDatabase.getInstance().getReference("userphotos").child(auth.getCurrentUser().getUid().toString());
                        UserPhoto userPhoto = new UserPhoto(UID, auth.getCurrentUser().getUid().toString(), auth.getCurrentUser().getDisplayName().toString(), photo_name, main_data_URL, cur_location, a_location_name, description, date_time_s, user_device_name, "false" );
                        databasePhoto.child(UID).setValue(nphoto);

                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Upload Success to Real-Time Database!", Toast.LENGTH_LONG).show();

                    }
                }); // add failure method
            } catch (Exception e)
            {
                Log.d(TAG, "onUploadTask: Failure to connect to FireBase upload or service failed.");
                Toast.makeText(getActivity(), "Connection to service has failed.", Toast.LENGTH_LONG);
            }

        }
        else
        {
            // Log an error
            Log.d(TAG, "onActivityResult: Failure to camera upload.");

        }

    }

    @NeedsPermission({Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void cameraStoreLocNeeds() {


        if ( Build.VERSION.SDK_INT >= 23 &&
                getContext().checkSelfPermission( android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                getContext().checkSelfPermission( android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        locationManager.requestLocationUpdates("gps", 3000, (float) 0.0, locationListener);

        // Our photo activity is now launch for camera
        Log.d(TAG, "onActivityResult: Starting camera...");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RC_TAKE_PHOTO);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CameraFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    @OnShowRationale({Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void cameraStoreLocRationale(final PermissionRequest request) {
        // This displays an alert dialogue to user asking for permissions
        new AlertDialog.Builder(mcameraFragment)
                .setMessage("To take photo and store., enable Camera and Storage and location.")
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

    @OnNeverAskAgain({Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void cameraStoreLocNever() {

        Toast.makeText(mcameraFragment, "You have denied permission!", Toast.LENGTH_LONG).show();

    }


    //        locationManager.requestLocationUpdates("gps", 3000, (float) 0.2, locationListener);

// Lets just make the GPS call and such only update after camera during upload service.





}
