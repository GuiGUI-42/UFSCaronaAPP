package com.example.ufscarona2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
public class SomenteCaroneiro extends AppCompatActivity {
    Button btnCadastroMotorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_somente_caroneiro);
        Button Btn1 = findViewById(R.id.btnBcCarona);
        Button Btn3 = findViewById(R.id.btnPerfil);
        Button Btn4 = findViewById(R.id.btnSobre);
        Button Btn5 = findViewById(R.id.btnLogOut);

        Btn1.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SomenteCaroneiro.this,MainActivity.class);
                startActivity(intent);

            }
        });

        Btn3.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SomenteCaroneiro.this,perfil.class);
                startActivity(intent);

            }
        });
        Btn4.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SomenteCaroneiro.this, Sobre.class);
                startActivity(intent);

            }
        });
        Btn5.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SomenteCaroneiro.this,FormLogin.class);
                startActivity(intent);

            }
        });
    }
}