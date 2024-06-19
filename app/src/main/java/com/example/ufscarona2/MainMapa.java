package com.example.ufscarona2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private List<String> origens;
    private List<String> destinos;
    private boolean mapReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mapa);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Recuperar as listas de origens e destinos da Intent
        Intent intent = getIntent();
        origens = intent.getStringArrayListExtra("origens");
        destinos = intent.getStringArrayListExtra("destinos");

        // Popular os Spinners com as listas de origens e destinos
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, origens);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, destinos);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        // Adicionar listeners para os Spinners
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedString = parent.getItemAtPosition(position).toString();
                if (mapReady && mMap!= null) {
                    updateMapPoint1(selectedString);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não há nada selecionado
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedString = parent.getItemAtPosition(position).toString();
                if (mapReady && mMap!= null) {
                    updateMapPoint2(selectedString);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não há nada selecionado
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapReady = true;
    }

    private void updateMapPoint1(String selectedPlanet) {
        // Extrair as coordenadas da origem selecionada
        String[] parts = selectedPlanet.split(" (?=-?[0-9]+\\.?[0-9]* -?[0-9]+\\.?[0-9]*)");
        if (parts.length == 2) {
            String origem = parts[0];
            String[] coordParts = parts[1].split(" ");
            if (coordParts.length == 2) {
                double lat = Double.parseDouble(coordParts[0]);
                double lon = Double.parseDouble(coordParts[1]);

                // Atualizar o mapa com base nas coordenadas da origem selecionada
                LatLng point = new LatLng(lat, lon);
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(origem)
                        .snippet("Origem"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 14));
            } else {
                Log.e("Error", "Invalid coordinates format: " + selectedPlanet);
            }
        } else {
            Log.e("Error", "Invalid format: " + selectedPlanet);
        }
    }

    private void updateMapPoint2(String selectedPlanet) {
        // Extrair as coordenadas do destino selecionado
        String[] parts = selectedPlanet.split(" (?=-?[0-9]+\\.?[0-9]* -?[0-9]+\\.?[0-9]*)");
        if (parts.length == 2) {
            String destino = parts[0];
            String[] coordParts = parts[1].split(" ");
            if (coordParts.length == 2) {
                double lat = Double.parseDouble(coordParts[0]);
                double lon = Double.parseDouble(coordParts[1]);

                // Atualizar o mapa com base nas coordenadas do destino selecionado
                LatLng point = new LatLng(lat, lon);
                mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(destino)
                        .snippet("Destino"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 14));
            } else {
                Log.e("Error", "Invalid coordinates format: " + selectedPlanet);
            }
        } else {
            Log.e("Error", "Invalid format: " + selectedPlanet);
        }
    }
}