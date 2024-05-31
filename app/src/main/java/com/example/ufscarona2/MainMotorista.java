package com.example.ufscarona2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
public class MainMotorista extends AppCompatActivity {
    Button btnCadastroMotorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_motorista);
        View Btn1 = findViewById(R.id.btnOfCarona);
        View Btn2 = findViewById(R.id.btnBcCarona);
        View Btn3 = findViewById(R.id.SwitchMotorista);
        View Btn4 = findViewById(R.id.btnPerfil);
        View Btn5 = findViewById(R.id.btnLogOut);
        View Btn6 = findViewById(R.id.btnSobre);

        Btn1.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMotorista.this,MainActivity.class);
                startActivity(intent);

            }
        });
        Btn2.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMotorista.this,ViewVagas.class);
                startActivity(intent);

            }
        });
        Btn3.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMotorista.this,MainCaroneiro.class);
                startActivity(intent);

            }
        });
        Btn4.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMotorista.this, perfil.class);
                startActivity(intent);

            }
        });
        Btn5.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMotorista.this,FormLogin.class);
                startActivity(intent);

            }
        });
        Btn6.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMotorista.this, perfil.class);
                startActivity(intent);

            }
        });
    }
}