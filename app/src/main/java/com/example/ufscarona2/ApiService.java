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
    private SharedPreferences prefsDestinos;
    private List<String> caronas = new ArrayList<>();
    private List<String> destinos = new ArrayList<>();

    public interface ApiCallback {
        void onApiSuccess(String caronasString, String destinosString);
        void onApiError(String error);
    }

    public ApiService(Context context) {
        this.prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        this.prefsDestinos = context.getSharedPreferences("prefsDestinos", Context.MODE_PRIVATE);
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
                            destinos.clear(); // Limpar a lista de destinos
                            for (int i = 0; i < 10; i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
                                String dataOrigem = obj.getString("fk_Origem");
                                String dataDestino = obj.getString("fk_Destino");
                                Log.d("API", "Origens: " + dataOrigem);
                                Log.d("API", "Destinos: " + dataDestino);
                                caronas.add(dataOrigem); // Adicionar os dados à lista de caronas
                                destinos.add(dataDestino); // Adicionar os dados à lista de destinos
                            }
                            String caronasString = convertListToString(caronas);
                            String destinosString = convertListToString(destinos);
                            Log.d("API", "Caronas string: " + caronasString);
                            Log.d("API", "Destinos string: " + destinosString);
                            callback.onApiSuccess(caronasString, destinosString);

                            prefs.edit().putString("caronas_array", caronasString).apply();
                            prefsDestinos.edit().putString("destinos_array", destinosString).apply();
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

    private String convertListToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append(item).append(",");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    public List<String> getCaronas() {
        return caronas;
    }

    public List<String> getDestinos() {
        return destinos;
    }
}
//String caronasString = "";