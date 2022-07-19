package com.example.proyectododoantho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dumplings extends AppCompatActivity {
    Button resta,suma;
    TextView cantidad;
    int quantity=0;
    final int precio=6000;
    final String local="Chinos S.A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dumplings);
        suma=findViewById(R.id.btnMasAC3);
        resta=findViewById(R.id.btnMenos3);
        cantidad=findViewById(R.id.textView7);
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

    public void addtoCart(View view) {
        if(quantity>0){
            int monto= quantity*precio;
            Intent i = new Intent(Dumplings.this, Pagar.class);
            i.putExtra("Name","Dumplings");
            i.putExtra("Cantidad",String.valueOf(quantity));
            i.putExtra("Monto",String.valueOf(monto));
            i.putExtra("Local",local);
            startActivity(i);

        }

    }
}