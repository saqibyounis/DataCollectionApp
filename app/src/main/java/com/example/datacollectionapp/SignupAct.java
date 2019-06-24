package com.example.datacollectionapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupAct extends AppCompatActivity {
         FirebaseAuth auth;
         EditText email,passwrod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth=FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        passwrod=findViewById(R.id.password);
    }

    public void signup(View view){
        final ProgressDialog progressDialog =new ProgressDialog(this);

        progressDialog.setMessage("Gettig info.");
        progressDialog.setTitle("Signing Up");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email.getText().toString(),passwrod.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    startActivity(new Intent(SignupAct.this,MainActivity.class));
                    finish();


                }else{
                    Toast.makeText(SignupAct.this, "Signup Failed!", Toast.LENGTH_SHORT).show();

                }
            }
        });



    }



    public void login(View view){
        startActivity(new Intent(this,MainActivity.class));
        finish();

    }


}
