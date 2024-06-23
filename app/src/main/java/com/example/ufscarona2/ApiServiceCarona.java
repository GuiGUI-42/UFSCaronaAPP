package com.example.ufscarona2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
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

public class ApiServiceCarona {
    private static final String TAG = "ApiService";
    private static final String API_URL = "http://ufscarona.j.p.carvalho.vms.ufsc.br:8081/api/caronas";
    private SharedPreferences prefs;
    private SharedPreferences prefsDestinos;

    public ApiServiceCarona(Context context) {
        this.prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        this.prefsDestinos = context.getSharedPreferences("prefsDestinos", Context.MODE_PRIVATE);
    }

    public interface ApiCallback {
        void onApiSuccess(List<String> origens, List<String> destinos, List<String> placas, List<Integer> anos);

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
                            if (jsonObject.has("Caronas") && !jsonObject.isNull("Caronas")) {
                                JSONArray dataArray = jsonObject.getJSONArray("Caronas");
                                List<String> origens = new ArrayList<>();
                                List<String> destinos = new ArrayList<>();
                                List<String> placas = new ArrayList<>();
                                List<Integer> anos = new ArrayList<>();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject obj = dataArray.getJSONObject(i);

                                    // Extraindo dados
                                    String origem = obj.optString("origem", "Origem não encontrada");
                                    String destino = obj.optString("destino", "Destino não encontrado");
                                    String placa = obj.optString("placa", "Placa não encontrada");
                                    int ano = obj.optInt("ano", 0);

                                    // Adicionando à lista
                                    origens.add(origem);
                                    destinos.add(destino);
                                    placas.add(placa);
                                    anos.add(ano);

                                    // Log individualmente
                                    Log.d(TAG, "Origem: " + origem);
                                    Log.d(TAG, "Destino: " + destino);
                                    Log.d(TAG, "Placa: " + placa);
                                    Log.d(TAG, "Ano: " + ano);
                                }

                                // Passando os dados de volta através do callback
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onApiSuccess(origens, destinos, placas, anos);
                                    }
                                });
                            } else {
                                callback.onApiError("Chave 'Caronas' não encontrada ou é nula");
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
