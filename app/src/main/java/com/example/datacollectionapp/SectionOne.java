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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.datacollectionapp.model.FormDataModel;
/*
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;*/

import java.util.ArrayList;
import java.util.List;

public class SectionOne extends AppCompatActivity {
    List<String> departments;
   ArrayAdapter<String> dadapter;
   Spinner dspinner,dispinner,section,station,jobgroup;
   ArrayAdapter<String> diadapter,secadapter,staadapter,jobadaptder;
   List<String> directorate;
    List<String> stations;
    List<String> sections;
    List<String> jobgroups;
    Bitmap tempBitmap;
    int tempid;
    ImageView profilephoto,tempImageview;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_one);
        profilephoto=findViewById(R.id.profilephoto);
        dspinner=findViewById(R.id.dspinner);
         dispinner=findViewById(R.id.dispinner);
        section=findViewById(R.id.section);
        station=findViewById(R.id.station);
        jobgroup=findViewById(R.id.jobgroup);

        departments=new ArrayList<>();
        directorate=new ArrayList<>();

        stations=new ArrayList<>();

        sections=new ArrayList<>();

        jobgroups=new ArrayList<>();
        directorate.add("Internal");
        directorate.add("External");
         departments.add("Agriculture");
         departments.add("Finance");
         departments.add("Sport");
         departments.add("ICT");
         sections.add("Zone A");
        sections.add("Zone B");
        sections.add("Zone A");

        sections.add("Zone C");
stations.add("Nyaaaa");
stations.add("Nany");
stations.add("Ngob");

jobgroups.add("A");

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

        dadapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,departments);
        diadapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,directorate);
        jobadaptder=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,jobgroups);
        staadapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,stations);
        secadapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,sections);

        dspinner.setAdapter(dadapter);
dispinner.setAdapter(diadapter);
section.setAdapter(secadapter);
station.setAdapter(staadapter);
jobgroup.setAdapter(jobadaptder);

        /*new AskPermission.Builder(this)
                .setPermissions(Manifest.permission.CAMERA)
                .setCallback(new PermissionCallback() {
                    @Override
                    public void onPermissionsGranted(int requestCode) {

                    }

                    @Override
                    public void onPermissionsDenied(int requestCode) {

                    }
                })
                .setErrorCallback(new ErrorCallback() {
                    @Override
                    public void onShowRationalDialog(PermissionInterface permissionInterface, int requestCode) {

                    }

                    @Override
                    public void onShowSettings(PermissionInterface permissionInterface, int requestCode) {

                    }
                })
                .request(MY_CAMERA_PERMISSION_CODE);
    }*/


    }
    public void next(View view){
        startActivity(new Intent(this,SectionThree.class));

    }
@RequiresApi(api = Build.VERSION_CODES.M)
public void idCapture(View view){
       tempid=1;
        captureImage();



}
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void dobCapture(View view){
        tempid=2;

        captureImage();

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void profileCapture(View view){
        tempid=3;
           tempImageview=profilephoto;
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

                FormDataModel.id=tempBitmap;
                Toast.makeText(this, "ID Card added", Toast.LENGTH_SHORT).show();

            }else if(tempid==2){

                FormDataModel.dob=tempBitmap;

                Toast.makeText(this, "DOB certificated added", Toast.LENGTH_SHORT).show();

            }else if(tempid==3){
                FormDataModel.profile=tempBitmap;
                profilephoto.setImageBitmap(tempBitmap);
                profilephoto.invalidate();
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();



            }

        }
    }

}
