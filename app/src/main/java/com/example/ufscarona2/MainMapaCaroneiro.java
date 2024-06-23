package com.example.ufscarona2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainMapaCaroneiro extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String origemSelecionada;
    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mapa_caroneiro);

        // Recuperar origem selecionada da Intent
        origemSelecionada = getIntent().getStringExtra("origemSelecionada");

        // Inicializar o SupportMapFragment e registrar o callback para o mapa estar pronto
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Iniciar o API para obter os dados das caronas
        api = new API(this);
        api.executeApi(new API.ApiCallback() {
            @Override
            public void onApiSuccess(List<String> origens, List<String> destinos, List<Double> oriLatitudes, List<Double> oriLongitudes) {
                // Dados de origens e destinos podem ser usados aqui se necessário
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateMapWithSelectedOrigin(origemSelecionada, origens, oriLatitudes, oriLongitudes);
                    }
                });
            }

            @Override
            public void onApiError(String error) {
                // Exibir Toast de erro na thread principal
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainMapaCaroneiro.this, "Erro ao obter dados da API: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void updateMapWithSelectedOrigin(String origemSelecionada, List<String> origens, List<Double> oriLatitudes, List<Double> oriLongitudes) {
        // A API já garantiu que a origem selecionada está presente nos dados retornados
        // Vamos encontrar as coordenadas correspondentes e atualizar o mapa

        boolean origemEncontrada = false;

        // Verificar se o mapa está pronto antes de atualizar
        if (mMap == null) {
            return;
        }

        for (int i = 0; i < origens.size(); i++) {
            if (origens.get(i).equalsIgnoreCase(origemSelecionada)) {
                // Encontrou a origem selecionada, usar as coordenadas correspondentes
                double ori_lat = oriLatitudes.get(i);
                double ori_lon = oriLongitudes.get(i);

                LatLng latLng = new LatLng(ori_lat, ori_lon);
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Ponto de Origem")
                        .snippet(origemSelecionada));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                origemEncontrada = true;
                break;
            }
        }

        if (!origemEncontrada) {
            // Caso a origem não seja encontrada nas caronas listadas (não deve ocorrer se a API estiver correta)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainMapaCaroneiro.this, "Origem não encontrada nas caronas disponíveis.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
