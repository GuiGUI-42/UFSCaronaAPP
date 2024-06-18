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
    private SharedPreferences prefsDestinos;
    private SharedPreferences SaveOrigem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caronas);

        caronaList = findViewById(R.id.caronalist);
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        prefsDestinos = getSharedPreferences("prefsDestinos", MODE_PRIVATE);
        SaveOrigem =getSharedPreferences("orig",MODE_PRIVATE);

        // Recuperar strings armazenadas
        String caronasString = prefs.getString("caronas_array", "");
        String destinosString = prefsDestinos.getString("destinos_array", "");

        Log.d("Caronas", "Caronas String: " + caronasString);
        Log.d("Caronas", "Destinos String: " + destinosString);

        // Verificar se as strings não estão vazias
        if (caronasString.isEmpty() || destinosString.isEmpty()) {
            Toast.makeText(this, "Erro: Dados de caronas ou destinos não encontrados", Toast.LENGTH_SHORT).show();
        } else {
            // Separar os valores
            String[] caronasArray = caronasString.split(",");
            String[] destinosArray = destinosString.split(",");

            // Criar uma lista de Carona
            ArrayList<Carona> caronasList = new ArrayList<>();
            for (int i = 0; i < caronasArray.length; i++) {
                String origem = caronasArray[i];
                String destino = i < destinosArray.length ? destinosArray[i] : "Destino não encontrado";
                Carona carona = new Carona("Carona " + (i + 1), origem, destino);
                caronasList.add(carona);
            }

            // Criar um ArrayAdapter para exibir as caronas
            ArrayAdapter<Carona> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, caronasList);
            caronaList.setAdapter(adapter);

            // Adicionar um listener para os itens da lista
            caronaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Carona selectedCarona = (Carona) parent.getItemAtPosition(position);
                    prefs.edit().putString("selected_carona", selectedCarona.toString()).apply();

                    // Logar a origem do item selecionado
                    Log.d("Caronas", "Origem selecionada: " + selectedCarona.getOrigem());
                    String origemCarona = selectedCarona.getOrigem();
                    SaveOrigem.edit().putString("origemCarona", origemCarona).apply();


                    Toast.makeText(Caronas.this, "Você selecionou: " + selectedCarona.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Configurar botões
        Button confirmar = findViewById(R.id.btn_confirmar);
        Button sair = findViewById(R.id.btn_sair);

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
        return nome + " - Origem: " + origem + " - Destino: " + destino;
    }
}
