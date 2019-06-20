package com.example.datacollectionapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.datacollectionapp.model.FormDataModel;

import java.util.ArrayList;
import java.util.List;

public class SectionThree extends AppCompatActivity {
    Spinner aone,atwo,athree,afour;
    List<String> acadamic;
    private ArrayAdapter<String> aadaptder;
     int tempid;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_three);
       aone=findViewById(R.id.aone);
        atwo=findViewById(R.id.atwo);
        athree=findViewById(R.id.athree);
        afour=findViewById(R.id.afour);

        acadamic=new ArrayList<>();
        acadamic.add("Acadamics");
        acadamic.add("PHD");
        acadamic.add("Masters");
        acadamic.add("Bachelors");
        acadamic.add("Diploma");
        acadamic.add("Certificate");
        acadamic.add("Professionals");



        aadaptder=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,acadamic);
      aone.setAdapter(aadaptder);
       atwo.setAdapter(aadaptder);
        athree.setAdapter(aadaptder);
        afour.setAdapter(aadaptder);
    }
    public void next(View view){
        startActivity(new Intent(this,SectionFive.class));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void captureImage(){

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void acdamdic1(View view){
        tempid=1;
        captureImage();


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void acdamdic2(View view){
        tempid=2;
        captureImage();


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void acdamdic3(View view){
        tempid=3;
        captureImage();


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void acdamdic4(View view){
        tempid=4;
        captureImage();


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void others1(View view){
        tempid=5;
        captureImage();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void others2(View view){
        tempid=6;
        captureImage();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void others3(View view){
        tempid=7;
        captureImage();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void others4(View view){
        tempid=8;
        captureImage();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void professional1(View view){
        tempid=9;
        captureImage();


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void professional2(View view){
        tempid=10;
        captureImage();


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void professional3(View view){
        tempid=11;
        captureImage();


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void professional4(View view){
        tempid=12;
        captureImage();


    }






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            tempBitmap = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);

            if(tempid==1){

                FormDataModel.acadamic1=tempBitmap;
                Toast.makeText(this, "Picature added", Toast.LENGTH_SHORT).show();

            }else if(tempid==2){

                FormDataModel.acadamic2=tempBitmap;

                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();

            }else if(tempid==3){
                FormDataModel.acadamic3=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }else if(tempid==4){
                FormDataModel.acadamic4=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }else if(tempid==5){
                FormDataModel.otheres1=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }else if(tempid==6){
                FormDataModel.otheres2=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }else if(tempid==7){
                FormDataModel.otheres3=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }else if(tempid==8){
                FormDataModel.otheres4=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }else if(tempid==9){
                FormDataModel.professional1=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }else if(tempid==10){
                FormDataModel.professional2=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }else if(tempid==11){
                FormDataModel.professional3=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }else if(tempid==12){
                FormDataModel.professional4=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }

        }
    }


}
