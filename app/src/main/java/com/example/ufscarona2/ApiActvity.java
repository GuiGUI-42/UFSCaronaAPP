package com.example.ufscarona2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiActvity extends AppCompatActivity {
    private static final String API_URL = "http://ufscarona.j.p.carvalho.vms.ufsc.br:3000/api/caronas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
                                String data = obj.getString("idCarona");
                                Log.d("API", "Carona: " + data);
                            }
                        } catch (JSONException e) {
                            Log.e("API", "Erro ao carregar dados: " + e.getMessage());
                        }
                    } else {
                        Log.e("API", "Erro ao carregar dados: " + statusCode);
                    }
                } catch (IOException e) {
                    Log.e("ERRO API", "Erro ao carregar dados: " + e.getMessage());
                }
            }
        }).start();
    }
}