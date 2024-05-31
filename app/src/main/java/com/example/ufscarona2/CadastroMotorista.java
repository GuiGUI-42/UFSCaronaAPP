
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
    Button btnCadastroMotorista;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_motorista);
        Button Btn1 = findViewById(R.id.CadastroMotorista);
        EditText modelo= findViewById((R.id.ModeloCarro));
        EditText Ano= findViewById((R.id.AnoCarro));
        EditText Placa= findViewById((R.id.PlacaCarro));
        EditText passageiros= findViewById((R.id.Capacidade));
        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String savedmodelo= sharedPreferences.getString("Modelo do Carro", "");
        String savedano = sharedPreferences.getString("Ano do Carro", "");
        String savedplaca = sharedPreferences.getString("Placa do Carro", "");
        String savedcapacidade = sharedPreferences.getString("Capacidade de Passageiros", "");
        modelo.setText(savedmodelo);
        Ano.setText(savedano);
        Placa.setText(savedplaca);
        passageiros.setText(savedcapacidade);

        Btn1.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String Modelo= modelo.getText().toString();
                String ano= Ano.getText().toString();
                String placa= Placa.getText().toString();
                String Capacidade = passageiros.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Modelo do Carro", Modelo);
                editor.putString("Ano do Carro", ano);
                editor.putString("Placa do Carro", placa);
                editor.putString("Capacidade de Passageiros", Capacidade);
                editor.apply();
                Intent intent = new Intent(CadastroMotorista.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }
}