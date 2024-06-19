package com.example.ufscarona2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

public class MainMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private List<String> origens;
    private List<String> destinos;

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
                updateMapPoint1(selectedString);
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
        // Atualiza o ponto no mapa com base na origem selecionada
        // Você precisa implementar a lógica para extrair as coordenadas da origem selecionada
        // e atualizar o mapa com base nelas
    }

    private void updateMapPoint2(String selectedDestination) {
        // Atualiza o ponto no mapa com base no destino selecionado
        // Você precisa implementar a lógica para extrair as coordenadas do destino selecionado
        // e atualizar o mapa com base nelas
    }
}
