package com.example.ufscarona2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ViewVagasC extends AppCompatActivity {
    int x = 0;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_vagas_c);
        View parkingSpaceView = findViewById(R.id.vaga1);
        Button att = findViewById(R.id.att);
        Button back = findViewById(R.id.btn_backB);
        Button sair = findViewById(R.id.sair);

        // Initialize SharedPreferences
        prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);

        // Retrieve the value of x from SharedPreferences
        x = prefs.getInt("x", 0);

        // Update the parking space view based on the value of x
        updateParkingSpaceView(parkingSpaceView, x);

        att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the value of x
                if(x==1){
                    x=0;
                }
                else{
                    x=1;
                }


                // Store the value of x in SharedPreferences
                prefs.edit().putInt("x", x).apply();

                // Update the parking space view based on the new value of x
                updateParkingSpaceView(parkingSpaceView, x);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewVagasC.this,ViewVagasB.class);
            }
        });
        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewVagasC.this,MainMotorista.class);
            }
        });
    }

    private void updateParkingSpaceView(View parkingSpaceView, int x) {
        if (x == 1) {
            parkingSpaceView.setBackgroundResource(R.color.parking_space_occupied);
        } else if (x == 0) {

            parkingSpaceView.setBackgroundResource(R.color.parking_space_available);

        } else {
            parkingSpaceView.setBackgroundResource(R.color.parking_space_unknown);
        }
    }
}