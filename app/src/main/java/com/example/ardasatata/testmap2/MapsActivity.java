package com.example.ardasatata.testmap2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

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

    TextView bottomSheet1;
    Button bottomSheetCall;
    Button bottomSheetPanggil;

    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};

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

        bottomSheet1 = findViewById(R.id.bottomSheetText1);
        bottomSheetCall = findViewById(R.id.bottomSheetCall);
        bottomSheetPanggil = findViewById(R.id.bottomSheetPanggil);


        firebaseAuth = FirebaseAuth.getInstance();

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
                    pedagangList.add(new Pedagang(pedagang.getLatlng(), pedagang.isStatus(), pedagang.getNamaDagang(), pedagang.getInfo()));
                    //pedagangList.add(pedagang);
                }

                for (Pedagang pedagang : pedagangList) {
                    mMap.addMarker(new MarkerOptions()
                            .position(new com.google.android.gms.maps.model.LatLng(pedagang.getLatlng().getLatitude(), pedagang.getLatlng().getLongitude()))
                            //.anchor(0.5f, 0.5f)
                            .title(pedagang.getNamaDagang())
                            .snippet(pedagang.getNamaDagang())
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

                bottomSheet1.setText(marker.getSnippet());

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
                    @Override
                    public void onClick(View view) {
                        DatabaseReference refPanggil = FirebaseDatabase.getInstance().getReference("pedagang").child(marker.getSnippet()).child();
                    }
                });

                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return true;
            }
        });



    }
}
