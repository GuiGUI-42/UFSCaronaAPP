package com.example.ufscarona2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ViewVagas extends AppCompatActivity {
    private SharedPreferences prefs;
    private Handler handler;
    private Runnable refreshRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_vagas);

        // Inicializando Views das vagas de estacionamento
        View vaga1 = findViewById(R.id.vaga1);
        View vaga2 = findViewById(R.id.vaga2);
        View vaga3 = findViewById(R.id.vaga3);
        View vaga4 = findViewById(R.id.vaga4);
        View vaga5 = findViewById(R.id.vaga5);
        View vaga6 = findViewById(R.id.vaga6);
        View vaga7 = findViewById(R.id.vaga7);
        View vaga8 = findViewById(R.id.vaga8);

        Button att = findViewById(R.id.att);
        Button next = findViewById(R.id.btn_nextB);
        Button sair = findViewById(R.id.sair);

        // Inicializa SharedPreferences
        prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);

        // Chama a API e processa os resultados
        fetchVagasData();

        // Configura os listeners dos botões
        att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chama a API novamente para atualizar os dados
                fetchVagasData();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewVagas.this, ViewVagasB.class);
                startActivity(intent);
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewVagas.this, MainMotorista.class);
                startActivity(intent);
            }
        });

        // Inicializa Handler e Runnable para refresh automático
        handler = new Handler(Looper.getMainLooper());
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                // Chama a API para atualizar os dados
                fetchVagasData();

                // Agendar o próximo refresh em 30 segundos
                handler.postDelayed(this, 30000); // 30000 milissegundos = 30 segundos
            }
        };

        // Iniciar o refresh pela primeira vez
        handler.post(refreshRunnable);
    }

    // Função para buscar dados das vagas e atualizar a UI
    private void fetchVagasData() {
        GetVAgas getVAgas = new GetVAgas(this);
        getVAgas.executeApi(new GetVAgas.ApiCallback() {
            @Override
            public void onApiSuccess(List<String> vagas) {
                // Log para depuração da resposta da API
                Log.d("ViewVagas", "Vagas recebidas: " + vagas.toString());

                // Processa as vagas retornadas
                for (String vagaInfo : vagas) {
                    Log.d("ViewVagas", "Processando vaga: " + vagaInfo);

                    try {
                        // Verifica se a string vagaInfo começa com "{" para evitar conversões inválidas
                        if (vagaInfo.startsWith("{")) {
                            JSONObject vagaJson = new JSONObject(vagaInfo);

                            // Extrai os dados relevantes do JSON
                            String descricao = vagaJson.getString("descricao");
                            String disponibilidade = vagaJson.getString("disponibilidade");
                            int idVaga = vagaJson.getInt("idvaga");

                            // Log para depuração dos dados extraídos
                            Log.d("ViewVagas", "Descrição: " + descricao);
                            Log.d("ViewVagas", "Disponibilidade: " + disponibilidade);
                            Log.d("ViewVagas", "ID Vaga: " + idVaga);

                            // Atualiza a view da vaga correspondente
                            updateParkingSpaceView(idVaga, disponibilidade);
                        } else {
                            Log.e("ViewVagas", "Dados inválidos recebidos da API para vaga: " + vagaInfo);
                        }
                    } catch (JSONException e) {
                        Log.e("ViewVagas", "Erro ao processar JSON da vaga: " + vagaInfo, e);
                    }
                }
            }

            @Override
            public void onApiError(String error) {
                Log.e("ViewVagas", "Erro ao obter dados da API: " + error);
            }
        });
    }

    // Atualiza a view da vaga de estacionamento com base no idVaga e disponibilidade
    private void updateParkingSpaceView(int idVaga, String disponibilidade) {
        // Mapeia o ID da vaga para o ID da view correspondente no layout
        int viewId = getResources().getIdentifier("vaga" + idVaga, "id", getPackageName());
        if (viewId == 0) {
            Log.e("ViewVagas", "Nenhuma view encontrada para o ID da vaga: " + idVaga);
            return;
        }

        View parkingSpaceView = findViewById(viewId);

        Log.d("ViewVagas", "Atualizando view para vaga ID " + idVaga + " com disponibilidade: " + disponibilidade);

        if (parkingSpaceView != null) {
            if ("ocupada".equalsIgnoreCase(disponibilidade)) {
                Log.d("ViewVagas", "Setando cor ocupada para vaga " + idVaga);
                parkingSpaceView.setBackgroundResource(R.color.parking_space_occupied);
            } else if ("livre".equalsIgnoreCase(disponibilidade)) {
                Log.d("ViewVagas", "Setando cor disponível para vaga " + idVaga);
                parkingSpaceView.setBackgroundResource(R.color.parking_space_available);
            } else {
                Log.d("ViewVagas", "Setando cor desconhecida para vaga " + idVaga);
                parkingSpaceView.setBackgroundResource(R.color.parking_space_occupied);
            }
        } else {
            Log.e("ViewVagas", "View para vaga ID " + idVaga + " não encontrada.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remover callbacks quando a activity for destruída para evitar vazamento de memória
        handler.removeCallbacks(refreshRunnable);
    }
}
