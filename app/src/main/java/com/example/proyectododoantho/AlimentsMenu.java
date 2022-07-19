package com.example.proyectododoantho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AlimentsMenu extends AppCompatActivity {
    ImageView IceCream,FastFood,CakeShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aliments_menu);
        IceCream=findViewById(R.id.imageView25);
        FastFood=findViewById(R.id.imageView22);
        CakeShop=findViewById(R.id.imageView23);

        IceCream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"EN MANTENIMIENTO",Toast.LENGTH_SHORT).show();
            }
        });
        FastFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"EN MANTENIMIENTO",Toast.LENGTH_SHORT).show();
            }
        });
        CakeShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"EN MANTENIMIENTO",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GoComidaChina(View view) {
        Intent i = new Intent(AlimentsMenu.this,ComidaChina.class);
        startActivity(i);
    }
}