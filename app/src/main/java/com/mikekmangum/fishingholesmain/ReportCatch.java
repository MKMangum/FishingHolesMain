package com.mikekmangum.fishingholesmain;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.SettingsSlicesContract.KEY_LOCATION;



public class ReportCatch extends AppCompatActivity implements
        OnMapReadyCallback {


    private static final String TAG = ReportCatch.class.getSimpleName();
    private static final String DEBUG_TAG = "fishingholesmain.MYMSG";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private UiSettings mUiSettings;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private static Timestamp mTimestamp;
    private static String mSpecies;
    private static double mLength = 16.0;                 //Default Value
    private static double mWeight = 24.0;                 //Default Value
    private static String mLure;
    private static double mLatitude;
    private static double mLongitude;
    private static double mTemperature;
    private static String mConditions;
    private static String mPicture;
    private static Bitmap mImageBitmap;
    byte[] mByteArray;
    private static ImageView mImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static boolean mPictureDone = false;
    private static final int CAMERA_PIC_REQUEST = 1337;


    private EditText lengthInfo;
    private EditText weightInfo;

    Catch mCatch = new Catch();
    

    // A default location (Boston, MA) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(42.3601, -71.0589);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;



    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private String mCurrentPhotoPath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_report_catch);

        final Button reportCatch = (Button) findViewById(R.id.report_catch_button);

        Button cameraButton = (Button)findViewById(R.id.camera_button);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                mPictureDone = true;
            }
        });





        final Spinner mSpinnerSpecies = (Spinner) findViewById(R.id.species_spinner);
        final Spinner mSpinnerLure = (Spinner) findViewById(R.id.lure_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSpecies = ArrayAdapter.createFromResource(this,
                R.array.species_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterLure = ArrayAdapter.createFromResource(this,
                R.array.lure_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapterSpecies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mSpinnerSpecies.setAdapter(adapterSpecies);
        mSpinnerLure.setAdapter(adapterLure);

        reportCatch.setEnabled(false);
        lengthInfo = (EditText) findViewById(R.id.editTextLength);
        weightInfo = (EditText) findViewById(R.id.editTextWeight);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reportCatch.setEnabled(!TextUtils.isEmpty(lengthInfo.getText())
                        && !TextUtils.isEmpty(weightInfo.getText())
                        && mPictureDone == true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        lengthInfo.addTextChangedListener(tw);
        weightInfo.addTextChangedListener(tw);



        reportCatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();


                    mTimestamp = new Timestamp(System.currentTimeMillis());
                    mSpecies = String.valueOf(mSpinnerSpecies.getSelectedItem());
                    mLure = String.valueOf(mSpinnerLure.getSelectedItem());
                    mLength = Float.valueOf(lengthInfo.getText().toString());
                    mWeight = Float.valueOf(weightInfo.getText().toString());


                    mCatch.setTimestamp(mTimestamp);
                    mCatch.setSpecies(mSpecies);
                    mCatch.setLength(mLength);
                    mCatch.setWeight(mWeight);
                    mCatch.setLure(mLure);
                    mCatch.setLatitude(mLatitude);
                    mCatch.setLongitude(mLongitude);
                    mCatch.setTemperature(mTemperature);
                    mCatch.setConditions(mConditions);
                    mCatch.setPicture(mByteArray);

                    MarkerOptions markerOptions = new MarkerOptions();

                    markerOptions.position(new LatLng(mLatitude, mLongitude))
                            .title("Catch Recorded :")
                            .snippet("" +
                                    "\nSpecies = " + mSpecies +
                                    "\nLength = " + mLength + " inches" +
                                    "\nWeight = " + mWeight + " pounds" +
                                    "\nLure Used = " + mLure +
                                    "\nLatitude = " + mLatitude +
                                    "\nLongitude = " + mLongitude +
                                    "\nTemperature = " + String.format("%.1f", mTemperature) +
                                    "\nConditions = " + mConditions);


                    CustomInfoWindowGoogleMap customInfoWindow =
                            new CustomInfoWindowGoogleMap(context);

                    mMap.setInfoWindowAdapter(customInfoWindow);

                    InfoWindowData infoWindowData = new InfoWindowData();
                    infoWindowData.setImage(mImageBitmap);

                    Marker m = mMap.addMarker(markerOptions);
                    m.setTag(infoWindowData);
                    m.showInfoWindow();

                    //Add new catch to database
                    DatabaseHelper.getInstance(context).addCatch(mCatch);




            }
        });


        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.catch_map);
        mapFragment.getMapAsync(this);



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            mImageBitmap = (Bitmap) data.getExtras().get("data");
            ImageView imageview = (ImageView) findViewById(R.id.ImageViewCamera);
            //sets imageview as the bitmap
            imageview.setImageBitmap(mImageBitmap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream );
            mByteArray  = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(mByteArray, Base64.DEFAULT);
            mPictureDone = true;
        }
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onPause()  {
        super.onPause();



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {

                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            // Get Latitude and Longitude coordinates of current location
                            mLatitude = mLastKnownLocation.getLatitude();
                            mLongitude = mLastKnownLocation.getLongitude();
                            LatLng latLng = new LatLng(mLatitude, mLongitude);


                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            // Get temperature and conditions from OpenWeatherMapAPI
                            // Construct API call using current lat, lon values
                            String apiRequest = "https://api.openweathermap.org/data/2.5/weather" +
                                    "?lat=" + mLatitude +
                                    "&lon=" + mLongitude +
                                    "&APPID=ce6aaf1b1190ea7e31c979ef8c26a470";

                            // Connect to Internet
                            ConnectivityManager connMgr = (ConnectivityManager)
                                    getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


                            // Check if connected to internet, and executes API request
                            if (networkInfo != null && networkInfo.isConnected()) {
                                new downloadJSONObj().execute(apiRequest);

                            } else {
                                Toast.makeText(ReportCatch.this,
                                        "No network connection available", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }



    private class downloadJSONObj extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return null;
            }
        }

        protected void onPostExecute(String jsonString) {
            try {
                //Convert downloaded string to JSON Object
                JSONObject json = new JSONObject(jsonString);

                //Extract values for temperature and conditions from JSON object

                double temp = json.getJSONObject("main").getDouble("temp");

                //Convert temperature value from Kelvin to Fahrenheit
                mTemperature = temp * 9 / 5 - 459.67;

                //Convert temperature to formatted string with one decimal place
                String tempFString = String.format("%.1f", mTemperature);

                //Extract the JSON array weather from JSON object
                JSONArray array = json.getJSONArray("weather");

                //Extract the string value for icon from the weather array
                mConditions = array.getJSONObject(0).getString("description");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();

            int response = conn.getResponseCode();
            Log.i(DEBUG_TAG, "The response is: " + response);

            is = conn.getInputStream();
            String jsonString = convertStreamToString(is);

            return jsonString;

        }catch(Exception e) {
            Log.i(DEBUG_TAG, e.toString());
        }finally {
            if (is != null) {
                is.close();
            }
        }

        return null;
    }
    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }




}

