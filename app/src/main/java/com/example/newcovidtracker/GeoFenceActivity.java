package com.example.newcovidtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GeoFenceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, ResultCallback<Status> {

    private static final String TAG = GeoFenceActivity.class.getSimpleName();

    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    private MapFragment mapFragment;
    private TextView lat, longg;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    boolean check;
    DatabaseReference reference;

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence);

        lat = findViewById(R.id.lat);
        longg = findViewById(R.id.longg);

        sharedPrefs = getSharedPreferences("App", MODE_PRIVATE);
        editor = sharedPrefs.edit();

        //intitializing google Map
        initGoogleMap();

        //Create googleAPI client;
        createGoogleAPIClient();

    }

    private void createGoogleAPIClient() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this).
                    addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        googleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.create_geo: {
                getCurrentlocation();
                item.setEnabled(false);
                return true;
            }
            case R.id.clear_geo: {
                clearGeoFence();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private double latitude,longitude;
    LatLng l1;

    private void getCurrentlocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(GeoFenceActivity.this).requestLocationUpdates(locationRequest,new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                LocationServices.getFusedLocationProviderClient(GeoFenceActivity.this).removeLocationUpdates(this);
                if(locationResult!=null && locationResult.getLocations().size() >0)
                {
                    int latestLoc=locationResult.getLocations().size()-1;
                    latitude=locationResult.getLocations().get(latestLoc).getLatitude();
                    longitude=locationResult.getLocations().get(latestLoc).getLongitude();

                    Geocoder geocoder=new Geocoder(GeoFenceActivity.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
                        String address1 =addressList.get(0).getAddressLine(0);
                        String area=addressList.get(0).getLocality();
                        String city=addressList.get(0).getAdminArea();
                        String country=addressList.get(0).getCountryName();
                        String postalCode=addressList.get(0).getPostalCode();

                        String FullAddress=address1+","+area+","+city+","+country+postalCode;
                        editor.putString("Geo Location",FullAddress);
                        editor.commit();

                        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                        String uid=user.getUid();
                        reference=FirebaseDatabase.getInstance().getReference().child("Geofencing").child(uid);

                        HashMap<String,Object> mp=new HashMap<>();
                        mp.put("Address",FullAddress);
                        reference.updateChildren(mp);



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            l1=new LatLng(latitude,longitude);

                            markerForGeofence(l1);


                        }
                    },10000);

                }
            }
        }, Looper.getMainLooper());



    }
    private Marker geofenceMarker;

    private void markerForGeofence(LatLng l1)
    {
        Log.i(TAG,"marker for GeoFence("+l1+")");
        String title=l1.latitude+","+l1.longitude;
        MarkerOptions markerOptions = new MarkerOptions().position(l1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        if(googleMap!=null)
        {
            if(geofenceMarker!=null)
                geofenceMarker.remove();

            geofenceMarker= googleMap.addMarker(markerOptions);


        }
        startGeofence();



    }

    private void startGeofence()
    {
        Log.i(TAG,"startGeofence()");
        if(geofenceMarker!=null)
        {
            Geofence geofence = createGeofence(geofenceMarker.getPosition(),GEOFENCE_RADIUS);
        }

    }
    private static final long GEO_DURATION=60*60*100;
    private static final String GEOFENCE_REQ_ID="My Geofence";
    private static final float GEOFENCE_RADIUS = 100.0f;

    private Geofence createGeofence(LatLng position, float geofenceRadius)
    {
        Log.i(TAG,"startGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(position.latitude,position.longitude,geofenceRadius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

    }

    private void initGoogleMap()
    {
        mapFragment=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}