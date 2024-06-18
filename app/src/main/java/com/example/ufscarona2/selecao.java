package com.example.ufscarona2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class selecao extends AppCompatActivity {
    private SharedPreferences pref;
    Button btnMotorista;
    Button btnCaroneiro;
    EditText nome;
    EditText matricula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selecao);
        btnMotorista = findViewById(R.id.btnMotorista);
        btnCaroneiro = findViewById(R.id.btnCaroneiro);
        EditText nome = findViewById(R.id.textUser);
        EditText matricula = findViewById(R.id.textMatricula);
        pref = getSharedPreferences("perfil", MODE_PRIVATE);

        String usuario = pref.getString("user", "");
        String mat = pref.getString("matricula", "");
        nome.setText(usuario);
        matricula.setText(mat);

        btnMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = nome.getText().toString();
                String mat = matricula.getText().toString();
                int valor = 10; // valor a ser guardado
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user", usuario);
                editor.putString("matricula", mat);
                editor.putInt("myInt", valor);
                editor.apply();
                Intent intent = new Intent(selecao.this, CadastroMotorista.class);
                startActivity(intent);
            }
        });

        btnCaroneiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = nome.getText().toString();
                String mat = matricula.getText().toString();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user", usuario);
                editor.putString("matricula", mat);
                editor.apply();
                Intent intent = new Intent(selecao.this, SomenteCaroneiro.class);
                startActivity(intent);
            }
        });
    }
}