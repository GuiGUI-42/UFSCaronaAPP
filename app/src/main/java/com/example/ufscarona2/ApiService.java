package com.example.ufscarona2;

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

public class ApiService {
    private static final String API_URL = "http://ufscarona.j.p.carvalho.vms.ufsc.br:3000/api/caronas";
    private SharedPreferences prefs;

    public interface ApiCallback {
        void onApiSuccess(String caronasString);
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

                        Log.d("API", "Resposta da API: " + response.toString());

                        try {
                            JSONArray dataArray = new JSONArray(response.toString());
                            String caronasString = "";
                            for (int i = 0; i < 10; i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
                                String data = obj.getString("fk_Origem");
                                Log.d("API", "Caronas: " + data);
                                caronasString += data + ","; // adicionar os dados à string
                            }
                            caronasString = caronasString.substring(0, caronasString.length() - 1); // remover a vírgula extra no final
                            Log.d("API", "Caronas string: " + caronasString);
                            callback.onApiSuccess(caronasString);
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
}