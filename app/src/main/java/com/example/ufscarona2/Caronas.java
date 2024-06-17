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

import java.util.ArrayList;

public class Caronas extends AppCompatActivity {

    private ListView caronaList;
    private SharedPreferences prefs;

    private String caronasString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caronas);

        caronaList = findViewById(R.id.caronalist);
        caronasString = "Carona 1 - Origem: A - Destino: B,Carona 2 - Origem: C - Destino: D,Carona 3 - Origem: E - Destino: F";
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        Button confirmar= findViewById(R.id.btn_confirmar);
        Button sair= findViewById(R.id.btn_sair);
        Log.d("SharedPreferences", "Caronas string: " + caronasString);

        if (caronasString.isEmpty()) {
            Toast.makeText(this, "Erro: caronasString é vazia", Toast.LENGTH_SHORT).show();
        } else {
            String[] caronasArray = caronasString.split(",");
            ArrayList<Carona> caronasList = new ArrayList<>();
            for (String caronaString : caronasArray) {
                String[] parts = caronaString.split(" - ");
                Carona carona = new Carona(parts[0], parts[1], parts[2]);
                caronasList.add(carona);
            }

            ArrayAdapter<Carona> adapter = new ArrayAdapter<Carona>(this, android.R.layout.simple_list_item_1, caronasList);
            caronaList.setAdapter(adapter);

            caronaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Carona selectedCarona = (Carona) parent.getItemAtPosition(position);
                    prefs.edit().putString("selected_carona", selectedCarona.toString()).apply();
                    Toast.makeText(Caronas.this, "Você selecionou o item " + selectedCarona.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Caronas.this, MainMapaCaroneiro.class);
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

class Carona {
    private String nome;
    private String origem;
    private String destino;

    public Carona(String nome, String origem, String destino) {
        this.nome = nome;
        this.origem = origem;
        this.destino = destino;
    }

    public String getNome() {
        return nome;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    @Override
    public String toString() {
        return nome + " - Oi: " + origem + " - Destino: " + destino;
    }
}