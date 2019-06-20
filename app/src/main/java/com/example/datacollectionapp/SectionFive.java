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

public class SectionFive extends AppCompatActivity {
    Spinner job1,job2;

    List<String> jobgroups;
    private ArrayAdapter<String> jobadaptder;
    int tempid;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_five);
      job1=findViewById(R.id.job1);
      job2=findViewById(R.id.job2);
         jobgroups=new ArrayList<>();
        jobgroups.add("B");
        jobgroups.add("C");
        jobgroups.add("D");
        jobgroups.add("E");
        jobgroups.add("F");
        jobgroups.add("G");
        jobgroups.add("H");
        jobgroups.add("I");
        jobgroups.add("J");
        jobgroups.add("K");
        jobgroups.add("L");
        jobadaptder=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,jobgroups);
        job1.setAdapter(jobadaptder);
        job2.setAdapter(jobadaptder);


    }
    public void next(View view){
        startActivity(new Intent(this,SectionSix.class));

    }


    public void eDetails(View view){
        tempid=1;
        captureImage();

    }

    public void permotion(View view){
        tempid=2;
        captureImage();


    }


    public void payslip1(View view){
        tempid=3;
        captureImage();


    }

    public void payslip2(View view){
        tempid=4;
        captureImage();


    }

    public void payslip3(View view){
        tempid=5;
captureImage();

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

                FormDataModel.employeedetail=tempBitmap;
                Toast.makeText(this, "Picature added", Toast.LENGTH_SHORT).show();

            }else if(tempid==2){

                FormDataModel.promotion=tempBitmap;

                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();

            }else if(tempid==3){
                FormDataModel.slip1=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }else if(tempid==4){
                FormDataModel.slip2=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }else if(tempid==5){
                FormDataModel.slip3=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }
        }
    }


}
