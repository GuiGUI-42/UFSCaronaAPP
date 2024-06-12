package com.example.ufscarona2;

import android.content.SharedPreferences;
import android.os.Bundle;
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

public class MainMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mapa);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        // Recuperar o array de strings das caronas
        SharedPreferences prefs = getSharedPreferences("caronas_prefs", MODE_PRIVATE);
        String caronasString = prefs.getString("caronas_array", "");
        String[] caronasArray = caronasString.split(",");

        // Criar um ArrayAdapter com o array de strings
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, caronasArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedString = parent.getItemAtPosition(position).toString();
                updateMapPoint1(selectedString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não há nada selecionado
            }
        });

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.destinos,
                android.R.layout.simple_spinner_item
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedString = parent.getItemAtPosition(position).toString();
                updateMapPoint2(selectedString);
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
    }

    private void updateMapPoint1(String selectedPlanet) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        sharedPreferences.edit().putString("caronas_array", selectedPlanet).apply();

        float lat, lgn;
        if (selectedPlanet.equals("UFSC")) {
            lat = (float) -26.9209275;
            lgn = (float) -49.1029942;
        } else {
            lat = (float) -27.9209275;
            lgn = (float) -50.1029942;
        }

        LatLng point1 = new LatLng(lat, lgn);
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(point1)
                .title("Ponto 1")
                .snippet("UFSC"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point1, 14));
    }

    private void updateMapPoint2(String selectedPlanet) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        sharedPreferences.edit().putString("selected_planet2", selectedPlanet).apply();

        float lat, lgn;
        if (selectedPlanet.equals("UFSC")) {
            lat = (float) -26.9209275;
            lgn = (float) -49.1029942;
        } else {
            lat = (float) -27.9209275;
            lgn = (float) -50.1029942;
        }

        LatLng point2 = new LatLng(lat, lgn);
        mMap.addMarker(new MarkerOptions()
                .position(point2)
                .title("Ponto 2")
                .snippet("UFSC"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point2, 14));
    }
}