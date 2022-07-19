package com.example.proyectododoantho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Pagar extends AppCompatActivity {
    TextView platos, cantidades,montoTotal;
    String Plato;
    String cantidad;
    String monto;
    String local;
    Double montoint;
    Double ivaint;
    Double valorProducto;
    String valorProductoSTR;
    String ivastr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar);
        platos = findViewById(R.id.platos);
        cantidades = findViewById(R.id.cantidades);
        montoTotal=findViewById(R.id.total);
        Bundle extras = getIntent().getExtras();
        if(extras.isEmpty()){
            Log.i("INTENT ERROR", "onCreate: EROOORRRRRRR");
        }else{
            Plato= extras.getString("Name");
            cantidad=extras.getString("Cantidad");
            monto=extras.getString("Monto");
            montoint=Double.valueOf(monto);
            ivaint=montoint/1.13;
            ivastr=String.valueOf(ivaint);
            valorProducto = montoint-ivaint;
            valorProductoSTR=Double.toString(valorProducto);
            local=extras.getString("Local");
            platos.setText(Plato);
            cantidades.setText(cantidad);
            montoTotal.setText("Total: "+monto+"₡");
        }


    }

    public void GoFactura(View view) {
        Intent i = new Intent(Pagar.this,Factura.class);
        i.putExtra("Name",Plato);
        i.putExtra("Cantidad",cantidad);
        i.putExtra("Monto", monto);
        i.putExtra("Local",local);
        i.putExtra("IVA",ivastr);
        i.putExtra("Producto",valorProductoSTR);
        startActivity(i);
    }
}