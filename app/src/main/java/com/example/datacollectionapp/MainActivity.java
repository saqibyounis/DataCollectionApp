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

public class MainActivity extends AppCompatActivity {
EditText email,password;

FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       email=findViewById(R.id.email);
       password=findViewById(R.id.password);
       auth=FirebaseAuth.getInstance();

    }

    public void login(View view){
        if(email.getText().toString().isEmpty() && password.getText().toString().isEmpty()){


            Toast.makeText(this, "Email and Password requires", Toast.LENGTH_SHORT).show();
        }else {

            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.setMessage("Gettig info.");
            progressDialog.setTitle("Loging you in..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {

                        startActivity(new Intent(MainActivity.this, SectionOne.class));


                    }
                }
            });


        }
    }

    public void signup(View view){
        startActivity(new Intent(this,SignupAct.class));

finish();


    }

}
