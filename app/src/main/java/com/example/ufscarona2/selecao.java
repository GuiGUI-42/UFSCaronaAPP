package com.example.ufscarona2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
public class selecao extends AppCompatActivity {
    private SharedPreferences pref;
    Button btnMotorista;
    Button btnCaroneiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selecao);
        View Btn1 = findViewById(R.id.btnMotorista);
        View Btn2 = findViewById(R.id.btnCaroneiro);
        pref = getSharedPreferences("MyPref", MODE_PRIVATE);

        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valor = 10; // valor a ser guardado
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("myInt", valor);
                editor.apply();
                Intent intent = new Intent(selecao.this, CadastroMotorista.class);
                startActivity(intent);

            }
        });
        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selecao.this, MainCaroneiro.class);
                startActivity(intent);
            }
        });
    }
}