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
    private static final String API_URL = "http://ufscarona.j.p.carvalho.vms.ufsc.br:3000/api/caronas";
    private SharedPreferences prefs;
    private List<String> caronas = new ArrayList<>();

    public interface ApiCallback {
        void onApiSuccess(String caronasString);
        void onApiError(String error);
    }

    public ApiService(Context context) {
        this.prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
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
                        while ((line = reader.readLine())!= null) {
                            response.append(line);
                        }
                        reader.close();

                        Log.d("API", "Resposta da API: " + response.toString());

                        try {
                            JSONArray dataArray = new JSONArray(response.toString());
                            caronas.clear(); // Limpar a lista de caronas
                            for (int i = 0; i < 10; i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
                                String data = obj.getString("fk_Origem");
                                Log.d("API", "Caronas: " + data);
                                caronas.add(data); // Adicionar os dados à lista de caronas
                            }
                            String caronasString = "";
                            for (String carona : caronas) {
                                caronasString += carona + ","; // Converter a lista de caronas para uma string
                            }
                            caronasString = caronasString.substring(0, caronasString.length() - 1); // Remover a vírgula extra no final
                            Log.d("API", "Caronas string: " + caronasString);
                            callback.onApiSuccess(caronasString);

                            prefs.edit().putString("caronas_array", caronasString).apply();
                        } catch (JSONException e) {
                            callback.onApiError(e.getMessage());
                        }
                    } else {
                        callback.onApiError("Erro ao carregar dados: " + statusCode);
                    }
                } catch (IOException e) {
                    callback.onApiError("Erro ao carregar dados: " + e.getMessage());
                }
            }
        }).start();
    }

    public List<String> getCaronas() {
        return caronas;
    }
}