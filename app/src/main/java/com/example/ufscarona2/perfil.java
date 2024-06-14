package com.example.ufscarona2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class perfil extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        EditText User = findViewById(R.id.textUser);
        EditText matricula = findViewById(R.id.textMatricula);
        EditText nomeEditText = findViewById(R.id.textUser);
        EditText emailEditText = findViewById(R.id.textEmail);
        Button sair = findViewById(R.id.LogoOut);
        sharedPreferences = getSharedPreferences("perfil", MODE_PRIVATE);

        // Recuperar a entrada salva anteriormente
        String saveduser = sharedPreferences.getString("user", "");
        String savedEmail = sharedPreferences.getString("userEmail", "");
        String savedmatricula = sharedPreferences.getString("matricula", "");
        User.setText(saveduser);
        matricula.setText(savedmatricula);
        nomeEditText.setText(saveduser);
        emailEditText.setText(savedEmail);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome= nomeEditText.getText().toString();
                String mat= matricula.getText().toString();
                String Email = emailEditText.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user", nome);
                editor.putString("matricula", mat);
                editor.putString("userEmail", Email);
                editor.apply();
                Intent intent = new Intent(perfil.this,FormLogin.class);
                startActivity(intent);
            }
        });
    }
}