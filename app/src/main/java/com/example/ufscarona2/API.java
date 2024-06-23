package com.example.ufscarona2;

import android.content.Context;
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

public class API {
    private static final String TAG = "API";
    private static final String API_URL = "http://ufscarona.j.p.carvalho.vms.ufsc.br:8081/api/caronas";
    private Context context;

    public API(Context context) {
        this.context = context;
    }

    public interface ApiCallback {
        void onApiSuccess(List<String> origens, List<String> destinos, List<Double> oriLatitudes, List<Double> oriLongitudes);

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
                            if (jsonObject.has("Caronas") && jsonObject.getJSONArray("Caronas") != null) {
                                JSONArray dataArray = jsonObject.getJSONArray("Caronas");
                                List<String> origens = new ArrayList<>();
                                List<String> destinos = new ArrayList<>();
                                List<Double> oriLatitudes = new ArrayList<>();
                                List<Double> oriLongitudes = new ArrayList<>();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject obj = dataArray.getJSONObject(i);

                                    // Extraindo dados
                                    String origem = obj.optString("origem", "Origem não encontrada");
                                    double ori_lat = obj.optDouble("ori_lat", 0.0);
                                    double ori_lon = obj.optDouble("ori_lon", 0.0);
                                    String destino = obj.optString("destino", "Destino não encontrado");
                                    double dest_lat = obj.optDouble("dest_lat", 0.0);
                                    double dest_lon = obj.optDouble("dest_lon", 0.0);

                                    // Adicionando à lista
                                    origens.add(origem);
                                    destinos.add(destino);
                                    oriLatitudes.add(ori_lat);
                                    oriLongitudes.add(ori_lon);
                                }

                                callback.onApiSuccess(origens, destinos, oriLatitudes, oriLongitudes);
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
