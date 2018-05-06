package com.example.ardasatata.testmap2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "";
    private static final float DEFAULT_ZOOM =  14.0f;
    public static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;

    private LatLng latLng;

    private GoogleMap mMap;

    SupportMapFragment mapFragment;

    Button show;
    Button hide;
    Button profile;

    Pedagang test1;

    private FirebaseAuth firebaseAuth;

    private ArrayList<Pedagang> pedagangList = new ArrayList<Pedagang>();
    DatabaseReference databasePedagang;

    LinearLayout llBottomSheet;
    BottomSheetBehavior bottomSheetBehavior;

    TextView bottomSheetNama;
    TextView bottomSheetInfo;
    Button bottomSheetCall;
    Button bottomSheetPanggil;

    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};

    Target targetUser;
    DatabaseReference userDatabase;



    LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    boolean mLocationPermissionGranted;

    FusedLocationProviderClient mFusedLocationProviderClient;

    Location mLastKnownLocation;
    Location mDefaultLocation;
    LatLng currentPos;

    MarkerOptions yourMarkerOptions;
    Marker yourMarker;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        bottomSheetNama = findViewById(R.id.bottomSheetNamaDagang);
        bottomSheetInfo = findViewById(R.id.bottomSheetInfo);
        bottomSheetCall = findViewById(R.id.bottomSheetCall);
        bottomSheetPanggil = findViewById(R.id.bottomSheetPanggil);

        firebaseAuth = FirebaseAuth.getInstance();

        getLocationPermission();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getDeviceLocation();

        userDatabase = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());

        if (firebaseAuth.getCurrentUser() == null) {
            // user is already logged in
            // open profile activity
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            //close this activity
            finish();

        }

        databasePedagang = FirebaseDatabase.getInstance().getReference("pedagang");

        databasePedagang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pedagangList.clear();

                mMap.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Pedagang pedagang = postSnapshot.getValue(Pedagang.class);
                    pedagangList.add(new Pedagang(pedagang.getId(),pedagang.getLatlng(), pedagang.isStatus(), pedagang.getNamaDagang(), pedagang.getInfo()));
                    //pedagangList.add(pedagang);
                }

                for (Pedagang pedagang : pedagangList) {
                    mMap.addMarker(new MarkerOptions()
                            .position(new com.google.android.gms.maps.model.LatLng(pedagang.getLatlng().getLatitude(), pedagang.getLatlng().getLongitude()))
                            .title(pedagang.getNamaDagang())
                            .snippet(pedagang.getId())
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        show = findViewById(R.id.show);
        hide = findViewById(R.id.hide);
        profile = findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                //close this activity
            }
        });

        Button buttonLogout = (Button) findViewById(R.id.logoutMaps);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logging out the user
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        test1 = new Pedagang(new LatLng(38.609556, -1.139637), true, "Tahu Campur Pak Sukir", "hehe");

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment.getView().setVisibility(View.VISIBLE);
            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment.getView().setVisibility(View.INVISIBLE);
            }
        });

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

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34.0, 151.0);
        mMap.addMarker(new MarkerOptions().position(new com.google.android.gms.maps.model.LatLng(sydney.getLatitude(),
                sydney.getLongitude())).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(38.609556, -1.139637))
//                .anchor(0.5f, 0.5f)
//                .title("Title1")
//                .snippet("Snippet1")
//                );

//        mMap.addMarker(new MarkerOptions()
//                .position(test1.getLatlng())
//                .anchor(0.5f, 0.5f)
//                .title(test1.getNamaDagang())
//                .snippet("Snippet1")
//                );

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                Context context = getApplicationContext();
                CharSequence text = test1.getInfo();
                int duration = Toast.LENGTH_SHORT;

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                getDeviceLocation();

                DatabaseReference pedagangRef = FirebaseDatabase.getInstance().getReference("pedagang").child(marker.getSnippet());

                pedagangRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        bottomSheetNama.setText(dataSnapshot.child("namaDagang").getValue(String.class));
                        bottomSheetInfo.setText(dataSnapshot.child("info").getValue(String.class));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                bottomSheetCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "08199999999"));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(MapsActivity.this, PERMISSIONS,1);

                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                    }
                });

                bottomSheetPanggil.setOnClickListener(new View.OnClickListener() {
                    User userForTarget;
                    @Override
                    public void onClick(View view) {
                        DatabaseReference refPanggil = FirebaseDatabase.getInstance().getReference("pedagang").child(marker.getSnippet()).child("target");
                        final DatabaseReference userTarget = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());
                        userTarget.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                userForTarget = dataSnapshot.getValue(User.class);
                                System.out.println(currentPos);
                                System.out.println(mLastKnownLocation);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        targetUser = new Target(userForTarget,currentPos);
                        refPanggil.setValue(targetUser);
                    }
                });




                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return true;
            }
        });


    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            currentPos = new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude());
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new com.google.android.gms.maps.model.LatLng(currentPos.getLatitude(),currentPos.getLongitude()) , 14.0f));
                            // Add a marker in Sydney and move the camera

//                            yourMarkerOptions = new MarkerOptions();
//                            yourMarkerOptions.title("Title");
//                            yourMarkerOptions.snippet("");
//                            yourMarkerOptions.position(currentPos);
//                            //Set your marker icon using this method.
//                            //yourMarkerOptions.icon();
//
//                            yourMarker = mMap.addMarker(yourMarkerOptions);

                            //pedagangDatabase.child(pedagangId).child("latlng").setValue(currentPos);

                            //userDatabase.child("latlng").setValue(currentPos);

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new com.google.android.gms.maps.model.LatLng(mDefaultLocation.getLatitude(),mDefaultLocation.getLongitude()), DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });


            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }


    }

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


}
