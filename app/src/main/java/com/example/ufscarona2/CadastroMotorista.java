package com.example.ufscarona2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroMotorista extends AppCompatActivity {
    private Button btnCadastroMotorista;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_motorista);

        btnCadastroMotorista = findViewById(R.id.CadastroMotorista);
        EditText modelo = findViewById(R.id.ModeloCarro);
        EditText ano = findViewById(R.id.AnoCarro);
        EditText placa = findViewById(R.id.PlacaCarro);
        EditText passageiros = findViewById(R.id.Capacidade);

        sharedPreferences = getSharedPreferences("cadastrocarro", MODE_PRIVATE);
        String savedModelo = sharedPreferences.getString("Modelo do Carro", "");
        String savedAno = sharedPreferences.getString("Ano do Carro", "");
        String savedPlaca = sharedPreferences.getString("Placa do Carro", "");
        String savedCapacidade = sharedPreferences.getString("Capacidade de Passageiros", "");
        modelo.setText(savedModelo);
        ano.setText(savedAno);
        placa.setText(savedPlaca);
        passageiros.setText(savedCapacidade);

        btnCadastroMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modeloCarro = modelo.getText().toString();
                String anoCarro = ano.getText().toString();
                String placaCarro = placa.getText().toString();
                String capacidade = passageiros.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Modelo do Carro", modeloCarro);
                editor.putString("Ano do Carro", anoCarro);
                editor.putString("Placa do Carro", placaCarro);
                editor.putString("Capacidade de Passageiros", capacidade);
                editor.apply();

                // Iniciar o serviço ApiPOSTService
                Intent serviceIntent = new Intent(CadastroMotorista.this, ApiPOSTService.class);
                serviceIntent.putExtra("nome", "dasd");
                serviceIntent.putExtra("email", "obagsjs@lksadm.feio");
                serviceIntent.putExtra("matricula", "18202454");
                serviceIntent.putExtra("motorista", 0); // Ajustar conforme necessário
                serviceIntent.putExtra("caroneiro", 1); // Ajustar conforme necessário
                startService(serviceIntent);

                // Iniciar a próxima atividade
                Intent intent = new Intent(CadastroMotorista.this, MainMotorista.class);
                startActivity(intent);
            }
        });
    }
}
