package com.example.ufscarona2;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiPOSTService extends IntentService {

    private static final String API_URL = "http://ufscarona.j.p.carvalho.vms.ufsc.br:8081/api/usuario";

    public ApiPOSTService() {
        super("ApiPOSTService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String nome = intent.getStringExtra("nome");
            String email = intent.getStringExtra("email");
            String matricula = intent.getStringExtra("matricula");
            int motorista = intent.getIntExtra("motorista", 0);
            int caroneiro = intent.getIntExtra("caroneiro", 1);

            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject postData = new JSONObject();
                postData.put("nome", nome);
                postData.put("email", email);
                postData.put("matricula", matricula);
                postData.put("motorista", motorista);
                postData.put("caroneiro", caroneiro);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(postData.toString());
                outputStream.flush();
                outputStream.close();

                int statusCode = connection.getResponseCode();
                if (statusCode == 201) {
                    Log.d("API", "Dados enviados com sucesso!");
                } else {
                    String responseMessage = connection.getResponseMessage();
                    Log.e("API", "Erro ao enviar dados: " + statusCode + " " + responseMessage);
                }
            } catch (IOException | JSONException e) {
                Log.e("ERRO API", "Erro ao enviar dados: " + e.getMessage());
            }
        }
    }
}
