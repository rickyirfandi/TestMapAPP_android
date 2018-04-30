package com.example.ardasatata.testmap2;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    SupportMapFragment mapFragment;

    Button show;
    Button hide;

    Pedagang test1;


//
// fm.beginTransaction()
//         .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//          .show(somefrag)
//          .commit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        show = findViewById(R.id.show);
        hide = findViewById(R.id.hide);

        test1 = new Pedagang(new LatLng(38.609556, -1.139637),true,"Tahu Campur Pak Sukir","hehe");

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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(38.609556, -1.139637))
//                .anchor(0.5f, 0.5f)
//                .title("Title1")
//                .snippet("Snippet1")
//                );

        mMap.addMarker(new MarkerOptions()
                .position(test1.getLatlng())
                .anchor(0.5f, 0.5f)
                .title(test1.getNamaDagang())
                .snippet("Snippet1")
                );

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Context context = getApplicationContext();
                CharSequence text = test1.getInfo();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return false;
            }
        });

    }
}
