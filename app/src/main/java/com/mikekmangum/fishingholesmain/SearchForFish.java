package com.mikekmangum.fishingholesmain;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SearchForFish extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationClickListener{



    private GoogleMap mMap;
    private UiSettings mUiSettings;
    DatabaseHelper myDb;
    double mLatitude;
    double mLongitude;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Boston, MA) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(42.3601, -71.0589);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_fish);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);


        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        Cursor res = DatabaseHelper.getInstance(this).getAllData();
        if(res.getCount() == 0) {
            // show message
            showMessage("Error","Nothing found");
            return;
        }

        while (res.moveToNext()) {

            MarkerOptions markerOptions = new MarkerOptions();
            LatLng currentLatLng = new LatLng(res.getFloat(6), res.getFloat(7));
            markerOptions.position(currentLatLng)
                    .title("Catch " + res.getInt(0))
                    .snippet("" +
                            "\nTime = " + res.getString(1) +
                            "\nSpecies = " + res.getString(2) +
                            "\nLength = " + res.getString(3) + " inches" +
                            "\nWeight = " + res.getString(4) + " pounds" +
                            "\nLure Used = " + res.getString(5) +
                            "\nLatitude = " + res.getString(6) +
                            "\nLongitude = " + res.getString(7) +
                            "\nTemperature = " + res.getString(8) +
                            "\nConditions = " + res.getString(9));


            CustomInfoWindowGoogleMap customInfoWindow =
                    new CustomInfoWindowGoogleMap(this);

            mMap.setInfoWindowAdapter(customInfoWindow);

            InfoWindowData infoWindowData = new InfoWindowData();

            if (res.getBlob(10) != null) {
                byte[] byteArray = res.getBlob(10);
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
                infoWindowData.setImage(bitmap);
            }

            Marker m = mMap.addMarker(markerOptions);
            m.setTag(infoWindowData);
            //m.showInfoWindow();
            //mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Catch " +
                              //  res.getString(0)));

        }

        LatLng mCatch1 = new LatLng(42.192422, -71.094436);
        LatLng mCatch2 = new LatLng(42.193, -71.0945);
        LatLng mCatch3 = new LatLng(42.194, -71.096);
 /*
        mMap.addMarker(new MarkerOptions().position(mCatch1).title("Catch 1 " + mCatch1));
        mMap.addMarker(new MarkerOptions().position(mCatch2).title("Catch 2 " + mCatch2));
        mMap.addMarker(new MarkerOptions().position(mCatch3).title("Catch 3 " + mCatch3));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mCatch1));
        mMap.setOnMyLocationButtonClickListener(this);

 */
        mMap.setOnMyLocationClickListener(this);
        mMap.setMyLocationEnabled(true);

    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Toast.makeText(this, "Current location:\n" + marker, Toast.LENGTH_LONG).show();

        return false;
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
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

                        } else {
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







}
