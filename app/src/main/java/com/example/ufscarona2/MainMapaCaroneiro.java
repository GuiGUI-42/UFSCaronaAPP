package com.example.ufscarona2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMapaCaroneiro extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private SharedPreferences prefs;
    private SharedPreferences save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mapa);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        save = getSharedPreferences("orig", MODE_PRIVATE);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);

        String caronasString = prefs.getString("caronas_array", "");
        Log.d("SharedPreferences", "Caronas string: " + caronasString); // Adicionei essa linha para imprimir a string no logcat

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);

        // Lendo destinos do prefsDestinos em vez de prefs
        SharedPreferences prefsDestinos = getSharedPreferences("prefsDestinos", MODE_PRIVATE);
        String destinosString = prefsDestinos.getString("destinos_array", ""); // Modificação aqui
        Log.d("SharedPreferences", "Destinos string: " + destinosString);

        String Origem =save.getString("origemCarona", "");



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void updateMapPoint1(String Origem) {
        // Atualiza o ponto no mapa com base na origem selecionada
        float lat, lgn;
        if (Origem.equals("UFSC Blumenau Bloco A")) {
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

    private void updateMapPoint2(String selectedDestination) {
        // Atualiza o ponto no mapa com base no destino selecionado
        // Aqui você deve implementar a lógica de atualização com base no destino selecionado
        // Adicione a lógica específica para os destinos, similar à lógica de updateMapPoint1 se necessário
    }
}
