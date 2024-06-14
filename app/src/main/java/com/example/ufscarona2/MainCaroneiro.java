package com.example.ufscarona2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
public class MainCaroneiro extends AppCompatActivity {
    Button btnCadastroMotorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_caroneiro);
        Button Btn1 = findViewById(R.id.btnBcCarona);
        Button Btn2 = findViewById(R.id.SwitchMotorista);
        Button Btn3 = findViewById(R.id.btnPerfil);
        Button Btn4 = findViewById(R.id.btnSobre);
        Button Btn5 = findViewById(R.id.btnLogOut);

        Btn1.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCaroneiro.this,Caronas.class);
                startActivity(intent);

            }
        });
        Btn2.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCaroneiro.this,MainMotorista.class);
                startActivity(intent);

            }
        });
        Btn3.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCaroneiro.this,perfil.class);
                startActivity(intent);

            }
        });
        Btn4.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCaroneiro.this, Sobre.class);
                startActivity(intent);

            }
        });
        Btn5.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCaroneiro.this,FormLogin.class);
                startActivity(intent);

            }
        });
    }
}