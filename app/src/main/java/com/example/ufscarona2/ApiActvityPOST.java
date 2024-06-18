package com.example.ufscarona2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiActvityPOST extends AppCompatActivity {
    private static final String API_URL = "http://ufscarona.j.p.carvalho.vms.ufsc.br:8081/api/usuario";

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
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    JSONObject postData = new JSONObject();
                    postData.put("nome", "dasd");
                    postData.put("email", "obagsjs@lksadm.feio");
                    postData.put("matricula", "18202454");
                    postData.put("motorista", 0);
                    postData.put("caroneiro", 1);

                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(postData.toString());
                    outputStream.flush();
                    outputStream.close();

                    int statusCode = connection.getResponseCode();
                    if (statusCode == 201) {
                        Log.d("API", "Dados enviados com sucesso!");
                    } else {
                        String responseMessage = connection.getResponseMessage();
                        Log.e("API", "Erro ao enviar dados: " + statusCode +responseMessage);
                    }
                } catch (IOException | JSONException e) {
                    Log.e("ERRO API", "Erro ao enviar dados: " + e.getMessage());
                }
            }
        }).start();
    }
}