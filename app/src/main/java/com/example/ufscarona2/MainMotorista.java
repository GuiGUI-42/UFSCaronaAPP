package com.example.ufscarona2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainMotorista extends AppCompatActivity {
    Button btnCadastroMotorista;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_motorista);

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        Button btnOfCarona = findViewById(R.id.btnOfCarona);
        Button btnViewVagas = findViewById(R.id.btnViewVagas);
        Button btnSwitchMotorista = findViewById(R.id.SwitchCaroneiro);
        Button btnPerfil = findViewById(R.id.btnPerfil);
        Button btnLogOut = findViewById(R.id.btnLogOut);
        Button btnSobre = findViewById(R.id.btnSobre);

        btnOfCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("API", "Botão clicado");
                ApiService apiService = new ApiService(MainMotorista.this);
                apiService.executeApi(new ApiService.ApiCallback() {
                    @Override
                    public void onApiSuccess(List<String> origens, List<String> destinos) {
                        Log.d("API", "Origens: " + origens);
                        Log.d("API", "Destinos: " + destinos);
                        Intent intent = new Intent(MainMotorista.this, MainMapa.class);
                        intent.putStringArrayListExtra("origens", (ArrayList<String>) origens);
                        intent.putStringArrayListExtra("destinos", (ArrayList<String>) destinos);
                        startActivity(intent);
                    }

                    @Override
                    public void onApiError(String error) {
                        Log.e("API", "Erro ao carregar dados: " + error);
                        // Trate o erro conforme necessário
                    }
                });
            }
        });

        btnViewVagas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMotorista.this, ViewVagas.class);
                startActivity(intent);
            }
        });

        btnSwitchMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMotorista.this, MainCaroneiro.class);
                startActivity(intent);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMotorista.this, perfil.class);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMotorista.this, FormLogin.class);
                startActivity(intent);
            }
        });

        btnSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMotorista.this, Sobre.class);
                startActivity(intent);
            }
        });
    }
}