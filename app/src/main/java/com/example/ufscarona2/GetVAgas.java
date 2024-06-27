package com.example.ufscarona2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetVAgas {
    private static final String TAG = "ApiService";
    private static final String API_URL = "http://ufscarona.j.p.carvalho.vms.ufsc.br:8081/api/vagas";
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;

    public GetVAgas(Context context) {
        this.prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        this.prefsEditor = prefs.edit();
    }

    public interface ApiCallback {
        void onApiSuccess(List<String> vagas);

        void onApiError(String error);
    }

    public void executeApi(ApiCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(API_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    int statusCode = connection.getResponseCode();
                    if (statusCode == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        Log.d(TAG, "Resposta da API: " + response.toString());

                        try {
                            // Parseando a resposta da API
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.has("Vagas") && jsonObject.getJSONArray("Vagas") != null) {
                                JSONArray dataArray = jsonObject.getJSONArray("Vagas");
                                List<String> vagasList = new ArrayList<>();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject obj = dataArray.getJSONObject(i);

                                    // Extraindo dados
                                    String descricao = obj.optString("descricao", "Descrição não encontrada");
                                    String disponibilidade = obj.optString("disponibilidade", "Disponibilidade não encontrada");
                                    int idVaga = obj.optInt("idvaga", -1);
                                    int idEstacionamento = obj.optInt("fk_estacionamento_idestacionamento", -1);

                                    // Adicionando à lista
                                    vagasList.add(obj.toString());

                                    // Salvando em SharedPreferences
                                    prefsEditor.putString("vaga_" + idVaga, descricao + " " + disponibilidade + " ID Estacionamento: " + idEstacionamento);
                                }

                                // Confirmar a gravação
                                prefsEditor.apply();

                                callback.onApiSuccess(vagasList);
                            } else {
                                callback.onApiError("Chave 'Vagas' não encontrada ou é nula");
                            }
                        } catch (JSONException e) {
                            callback.onApiError("Erro ao processar JSON: " + e.getMessage());
                        }
                    } else {
                        callback.onApiError("Código de resposta de erro: " + statusCode);
                    }
                } catch (IOException e) {
                    callback.onApiError("Erro ao conectar à API: " + e.getMessage());
                }
            }
        }).start();
    }
}
