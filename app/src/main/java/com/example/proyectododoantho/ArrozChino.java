package com.example.proyectododoantho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ArrozChino extends AppCompatActivity {
    Button resta,suma;
    TextView cantidad;
    int quantity=0;
    final int precio=3500;
    final String local="Chinos S.A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_arroz_chino);
        resta=findViewById(R.id.btnMenos);
        cantidad=findViewById(R.id.textView5);
        suma=findViewById(R.id.btnMasAC);

        resta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity>0){
                    quantity-=1;
                    cantidad.setText(String.valueOf(quantity));
                }
            }
        });
        suma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity+=1;
                cantidad.setText(String.valueOf(quantity));
            }
        });

    }

    public void pagar(View view) {
        if(quantity>0){
            int monto= quantity*precio;
            Intent i = new Intent(ArrozChino.this, Pagar.class);
            i.putExtra("Name","Arroz Chino");
            i.putExtra("Cantidad",String.valueOf(quantity));
            i.putExtra("Monto",String.valueOf(monto));
            i.putExtra("Local",local);
            startActivity(i);
        }

    }
}