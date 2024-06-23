package com.example.ufscarona2;

import android.content.Context;
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
import java.util.List;

public class Caronas extends AppCompatActivity {

    private ListView caronaList;
    private ApiServiceCarona apiService;
    private SharedPreferences SaveOrigem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caronas);

        // Inicialização das SharedPreferences
        SaveOrigem = getSharedPreferences("SaveOrigem", Context.MODE_PRIVATE);

        // Inicialização do ApiServiceCarona
        apiService = new ApiServiceCarona(this);

        // Referência para o ListView no layout
        caronaList = findViewById(R.id.carona_list);

        // Chamada à API para obter os dados das caronas
        apiService.executeApi(new ApiServiceCarona.ApiCallback() {
            @Override
            public void onApiSuccess(List<String> origens, List<String> destinos, List<String> placas, List<Integer> anos) {
                // Criar lista de objetos Carona com os dados obtidos da API
                ArrayList<Carona> caronasList = new ArrayList<>();
                for (int i = 0; i < origens.size(); i++) {
                    String origem = origens.get(i);
                    String destino = destinos.get(i);
                    String placa = placas.get(i);
                    String ano = String.valueOf(anos.get(i));
                    Carona carona = new Carona(origem, destino, placa, ano);
                    caronasList.add(carona);
                }

                // Criar um ArrayAdapter para exibir as caronas na ListView
                ArrayAdapter<Carona> adapter = new ArrayAdapter<>(Caronas.this, android.R.layout.simple_list_item_1, caronasList);

                // Atualizar a UI no thread principal
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        caronaList.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onApiError(String error) {
                Toast.makeText(Caronas.this, "Erro ao obter dados da API: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar o clique nos itens da lista
        caronaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Carona selectedCarona = (Carona) parent.getItemAtPosition(position);
                String origemCarona = selectedCarona.getOrigem();

                // Verificar se a origem selecionada não é nula
                if (origemCarona != null) {
                    // Salvar a origem selecionada nas SharedPreferences
                    SaveOrigem.edit().putString("origemCarona", origemCarona).apply();

                    // Iniciar a atividade MainMapaCaroneiro passando a origem como extra
                    Intent intent = new Intent(Caronas.this, MainMapaCaroneiro.class);
                    intent.putExtra("origemSelecionada", origemCarona);
                    startActivity(intent);

                    // Exemplo de log para verificar se a origem foi salva corretamente
                    Log.d("Caronas", "Origem selecionada: " + origemCarona);
                } else {
                    Toast.makeText(Caronas.this, "Origem selecionada é nula.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configuração dos botões
        Button confirmar = findViewById(R.id.btn_confirmar);
        Button sair = findViewById(R.id.btn_sair);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Exemplo de abertura da atividade sem uma origem selecionada
                Intent intent = new Intent(Caronas.this, MainMapaCaroneiro.class);
                startActivity(intent);
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Exemplo de abertura da atividade sem uma origem selecionada
                Intent intent = new Intent(Caronas.this, MainCaroneiro.class);
                startActivity(intent);
            }
        });
    }
}



class Carona {
    private String origem;
    private String destino;
    private String placa;
    private String ano;



    public Carona(String origem, String destino, String placa, String ano) {
        this.origem = origem;
        this.destino = destino;
        this.placa = placa;
        this.ano = ano;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public String getPlaca() {
        return placa;
    }

    public String getAno() {
        return ano;
    }

    @Override
    public String toString() {
        return "Origem: " + origem + "\nDestino: " + destino + "\nPlaca: " + placa + "\nAno: " + ano;
    }
}
