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

public class ApiService {
    private static final String TAG = "ApiService";
    private static final String API_URL = "http://ufscarona.j.p.carvalho.vms.ufsc.br:8081/api/caronas";
    private SharedPreferences prefs;
    private SharedPreferences prefsDestinos;
    private List<String> caronas = new ArrayList<>();
    private List<String> destinos = new ArrayList<>();

    public ApiService(Context context) {
        this.prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        this.prefsDestinos = context.getSharedPreferences("prefsDestinos", Context.MODE_PRIVATE);
    }

    public interface ApiCallback {
        void onApiSuccess(List<String> caronas, List<String> destinos);

        void onApiError(String error);
    }

    private String convertListToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append(item).append(",");
        }
        return sb.toString();
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
                            JSONArray dataArray = jsonObject.getJSONArray("Caronas");
                            caronas.clear();
                            destinos.clear();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);

                                // Extraindo "Origem" e "Destino"
                                String dataOrigem = obj.optString("Origem", "Origem não encontrada");
                                String dataDestino = obj.optString("Destino", "Destino não encontrado");

                                // Log para depuração
                                //Log.d(TAG, "Item " + i + " - Origem: " + dataOrigem);
                                //Log.d(TAG, "Item " + i + " - Destino: " + dataDestino);

                                // Adicionando às listas
                                caronas.add(dataOrigem);
                                destinos.add(dataDestino);
                            }

                            // Logando as listas finais para depuração
                            Log.d(TAG, "Lista final de Caronas: " + caronas);
                            Log.d(TAG, "Lista final de Destinos: " + destinos);

                            callback.onApiSuccess(caronas, destinos);

                            // Salvando as listas no SharedPreferences
                            prefs.edit().putString("caronas_array", convertListToString(caronas)).apply();
                            prefsDestinos.edit().putString("destinos_array", convertListToString(destinos)).apply();
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
