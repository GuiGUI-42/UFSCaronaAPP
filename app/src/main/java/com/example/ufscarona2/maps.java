package com.example.mapsufscarona;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.ufscarona2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Adicione aqui o código para customizar o mapa.
        LatLng point1 = new LatLng(-26.9209275, -49.1029942);
        LatLng point2 = new LatLng(-26.913423,-49.0880496);

        mMap.addMarker(new MarkerOptions()
                .position(point1)
                .title("Ponto 1")
                .snippet("UFSC"));

        mMap.addMarker(new MarkerOptions()
                .position(point2)
                .title("Ponto 2")
                .snippet("X"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point1, 14));

    }


}