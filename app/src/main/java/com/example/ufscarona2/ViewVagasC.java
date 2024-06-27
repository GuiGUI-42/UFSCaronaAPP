package com.example.ufscarona2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ViewVagasC extends AppCompatActivity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_vagas_c);

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
        Button back = findViewById(R.id.btn_backB);
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewVagasC.this, ViewVagasB.class);
                startActivity(intent);
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewVagasC.this, MainMotorista.class);
                startActivity(intent);
            }
        });
    }

    // Função para buscar dados das vagas e atualizar a UI
    private void fetchVagasData() {
        GetVAgas getVAgas = new GetVAgas(this);
        getVAgas.executeApi(new GetVAgas.ApiCallback() {
            @Override
            public void onApiSuccess(List<String> vagas) {
                // Log para depuração da resposta da API
                Log.d("ViewVagasC", "Vagas recebidas: " + vagas.toString());

                // Processa as vagas retornadas
                for (String vagaInfo : vagas) {
                    Log.d("ViewVagasC", "Processando vaga: " + vagaInfo);

                    try {
                        // Verifica se a string vagaInfo começa com "{" para evitar conversões inválidas
                        if (vagaInfo.startsWith("{")) {
                            JSONObject vagaJson = new JSONObject(vagaInfo);

                            // Extrai os dados relevantes do JSON
                            String descricao = vagaJson.getString("descricao");
                            String disponibilidade = vagaJson.getString("disponibilidade");
                            int idVaga = vagaJson.getInt("idvaga");

                            // Log para depuração dos dados extraídos
                            Log.d("ViewVagasC", "Descrição: " + descricao);
                            Log.d("ViewVagasC", "Disponibilidade: " + disponibilidade);
                            Log.d("ViewVagasC", "ID Vaga: " + idVaga);

                            // Filtra as vagas pela descrição "Vasto"
                            if ("Vasto".equalsIgnoreCase(descricao)) {
                                updateParkingSpaceView(idVaga, disponibilidade);
                            }
                        } else {
                            Log.e("ViewVagasC", "Dados inválidos recebidos da API para vaga: " + vagaInfo);
                        }
                    } catch (JSONException e) {
                        Log.e("ViewVagasC", "Erro ao processar JSON da vaga: " + vagaInfo, e);
                    }
                }
            }

            @Override
            public void onApiError(String error) {
                Log.e("ViewVagasC", "Erro ao obter dados da API: " + error);
            }
        });
    }

    // Atualiza a view da vaga de estacionamento com base no idVaga e disponibilidade
    private void updateParkingSpaceView(int idVaga, String disponibilidade) {
        // Mapeia o ID da vaga para o ID da view correspondente no layout
        int viewId = getResources().getIdentifier("vaga" + idVaga, "id", getPackageName());
        if (viewId == 0) {
            Log.e("ViewVagasC", "Nenhuma view encontrada para o ID da vaga: " + idVaga);
            return;
        }

        View parkingSpaceView = findViewById(viewId);

        Log.d("ViewVagasC", "Atualizando view para vaga ID " + idVaga + " com disponibilidade: " + disponibilidade);

        if (parkingSpaceView != null) {
            if ("ocupada".equalsIgnoreCase(disponibilidade)) {
                Log.d("ViewVagasC", "Setando cor ocupada para vaga " + idVaga);
                parkingSpaceView.setBackgroundResource(R.color.parking_space_occupied);
            } else if ("livre".equalsIgnoreCase(disponibilidade)) {
                Log.d("ViewVagasC", "Setando cor disponível para vaga " + idVaga);
                parkingSpaceView.setBackgroundResource(R.color.parking_space_available);
            } else {
                Log.d("ViewVagasC", "Setando cor desconhecida para vaga " + idVaga);
                parkingSpaceView.setBackgroundResource(R.color.parking_space_unknown);
            }
        } else {
            Log.e("ViewVagasC", "View para vaga ID " + idVaga + " não encontrada.");
        }
    }
}
