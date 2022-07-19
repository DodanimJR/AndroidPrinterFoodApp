package com.example.proyectododoantho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText txtNAME,email,password,confirmpassword;
    FirebaseAuth mAuth;
    ImageView google,facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        txtNAME=findViewById(R.id.editTxtNOMBREREG);
        email=findViewById(R.id.editTxtEMAILREG);
        password=findViewById(R.id.edtTxtPASSREG);
        confirmpassword=findViewById(R.id.editTxtCONFIRMPASSREG);
        google=findViewById(R.id.imageViewGoogleReg);
        facebook=findViewById(R.id.imageViewFacebookReg);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Redirecting to google",Toast.LENGTH_SHORT).show();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Redirecting to facebook",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void Register(View view) {
        //Intent i = new Intent(Register.this,AlimentsMenu.class);
        //startActivity(i);
        createUser();
    }

    public void GoLogin(View view) {
        Intent i = new Intent(Register.this,Login.class);
        startActivity(i);
    }
    private void createUser(){
        String correo= email.getText().toString();
        String pass= password.getText().toString();
        String confirmPass= confirmpassword.getText().toString();
        String name = txtNAME.getText().toString();




        if(TextUtils.isEmpty(correo)){
            email.setError("EMAIL CANNOT BE EMPTY");
            email.requestFocus();
        }else if(TextUtils.isEmpty(pass)){
            password.setError("PASSWORD CANNOT BE EMPTY");
            password.requestFocus();
        }else if(TextUtils.isEmpty(confirmPass)){
            confirmpassword.setError("CONFIRM PASSWORD CANNOT BE EMPTY");
            confirmpassword.requestFocus();

        }else if(TextUtils.isEmpty(name)){
            txtNAME.setError("NAME CANNOT BE EMPTY");
            txtNAME.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(correo,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"USER CORRECTLY REGISTERED",Toast.LENGTH_SHORT).show();
                        startActivity( new Intent(getApplicationContext(),Login.class));
                    }else{
                        Toast.makeText(getApplicationContext(),"REGISTRATION ERROR"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }
}