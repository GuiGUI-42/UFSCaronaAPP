package com.example.ufscarona2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashSet;

public class Caronas extends AppCompatActivity {

    private ListView caronaList;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caronas);

        caronaList = findViewById(R.id.caronalist);
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        Button confirmar= findViewById(R.id.btn_confirmar);
        Button sair= findViewById(R.id.btn_sair);
        String caronasString = prefs.getString("caronas_array", "");
        Log.d("SharedPreferences", "Caronas string: " + caronasString);

        if (caronasString.isEmpty()) {
            Toast.makeText(this, "Erro: caronasString é vazia", Toast.LENGTH_SHORT).show();
        } else {
            String[] caronasArray = caronasString.split(",");

            // Remove elementos repetidos usando um HashSet
            HashSet<String> set = new HashSet<>();
            set.addAll(Arrays.asList(caronasArray));
            caronasArray = set.toArray(new String[0]);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, caronasArray);
            caronaList.setAdapter(adapter);

            caronaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedCarona = parent.getItemAtPosition(position).toString();
                    prefs.edit().putString("selected_carona", selectedCarona).apply();
                    Toast.makeText(Caronas.this, "Você selecionou o item " + selectedCarona, Toast.LENGTH_SHORT).show();
                }
            });
        }
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Caronas.this, MainMapa.class);
                startActivity(intent);
            }
        });
        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Caronas.this, MainCaroneiro.class);
                startActivity(intent);
            }
        });
    }
}