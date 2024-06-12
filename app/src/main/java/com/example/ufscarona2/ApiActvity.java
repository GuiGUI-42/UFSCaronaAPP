package com.example.ufscarona2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiActvity {
    private static final String URL_API = "http://ufscarona.j.p.carvalho.vms.ufsc.br:3000/api/caronas";

    public void logPrimeiraCarona() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL_API)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Erro ao realizar requisição: " + response);
        }

        String responseBody = response.body().string();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(responseBody);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        if (jsonArray.length() > 0) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(0);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            Log.d("API", "Primeira carona: " + jsonObject.toString());
        } else {
            Log.d("API", "Nenhuma carona encontrada");
        }
    }
}