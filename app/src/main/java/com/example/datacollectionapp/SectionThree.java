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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.datacollectionapp.model.FormData;
import com.example.datacollectionapp.model.FormDataModel;

import java.util.ArrayList;
import java.util.List;

public class SectionThree extends AppCompatActivity {
    Spinner aone,atwo,athree,afour;
    List<String> acadamic;
    private ArrayAdapter<String> aadaptder;
     int tempid;
     Button a1btn,a2btn,a3btn,a4btn,p1btn,p2btn,p3btn,p4btn,o1btn,o2btn,o3btn,o4btn;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap tempBitmap;

    EditText p1,p2,p3,p4,o1,o2,o3,o4,town,pcode,postaladdress,mobilenumber,email,postacode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_three);
       aone=findViewById(R.id.aone);
        atwo=findViewById(R.id.atwo);
        athree=findViewById(R.id.athree);
        afour=findViewById(R.id.afour);
        a1btn=findViewById(R.id.aonebtn);
        a2btn=findViewById(R.id.atwobtn);
        a3btn=findViewById(R.id.athreebtn);
        a4btn=findViewById(R.id.afourbtn);
        p1btn=findViewById(R.id.p1btn);

        p2btn=findViewById(R.id.p2btn);
        p3btn=findViewById(R.id.p3btn);
        p4btn=findViewById(R.id.p4btn);
        o1btn=findViewById(R.id.o1btn);

        o2btn=findViewById(R.id.o2btn);

        o3btn=findViewById(R.id.o3btn);

        o4btn=findViewById(R.id.o4btn);

        p1=findViewById(R.id.p1);

        p2=findViewById(R.id.p2);

        p3=findViewById(R.id.p3);

        p4=findViewById(R.id.p4);



        o1=findViewById(R.id.o1);
        o2=findViewById(R.id.o2);

        o3=findViewById(R.id.o3);
        o4=findViewById(R.id.o4);


        mobilenumber=findViewById(R.id.mobilenumber);
        email=findViewById(R.id.email);
        postaladdress=findViewById(R.id.postaladdress);
        town=findViewById(R.id.town);
        postacode=findViewById(R.id.postaladdress);

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
        /*
        aone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String acadamic1=acadamic.get(position);
                FormDataModel.formData.setAcadamic1(acadamic1);

            }
        });
        */











    }
    public void next(View view){

        FormDataModel.formData.setAcadamic1(acadamic.get(aone.getSelectedItemPosition()));

        FormDataModel.formData.setAcadamic2(acadamic.get(atwo.getSelectedItemPosition()));

        FormDataModel.formData.setAcadamic3(acadamic.get(athree.getSelectedItemPosition()));

        FormDataModel.formData.setAcadamic4(acadamic.get(afour.getSelectedItemPosition()));

        FormDataModel.formData.setProfessoin1(p1.getText().toString());

        FormDataModel.formData.setProfessoin2(p2.getText().toString());
        FormDataModel.formData.setProfessoin3(p3.getText().toString());
        FormDataModel.formData.setProfessoin4(p4.getText().toString());

        FormDataModel.formData.setOther1(o1.getText().toString());

        FormDataModel.formData.setOther2(o2.getText().toString());
        FormDataModel.formData.setOther3(o3.getText().toString());
        FormDataModel.formData.setOther4(o4.getText().toString());

        FormDataModel.formData.setPostalAddress(postaladdress.getText().toString());
        FormDataModel.formData.setEmail(email.getText().toString());
        FormDataModel.formData.setMobileNumber(mobilenumber.getText().toString());
        FormDataModel.formData.setTown(town.getText().toString());
        FormDataModel.formData.setPostalCode(postacode.getText().toString());

        if(FormDataModel.acadamic1!=null &&FormDataModel.acadamic2!=null &&FormDataModel.acadamic3!=null&&FormDataModel.acadamic4!=null&&FormDataModel.otheres1!=null&&FormDataModel.otheres2!=null&&FormDataModel.otheres3!=null&&FormDataModel.otheres4!=null &&FormDataModel.professional1!=null&&FormDataModel.professional3!=null&&FormDataModel.professional3!=null&&FormDataModel.professional4!=null) {
            startActivity(new Intent(this, SectionFive.class));
        }else{
            Toast.makeText(this, "Some fields are empty", Toast.LENGTH_SHORT).show();

        }
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
                  a1btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
            }else if(tempid==2){

                FormDataModel.acadamic2=tempBitmap;
                a2btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));

                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();

            }else if(tempid==3){
                FormDataModel.acadamic3=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();
                a3btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));



            }else if(tempid==4){
                FormDataModel.acadamic4=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();
                a4btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));



            }else if(tempid==5){
                FormDataModel.otheres1=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();
                o1btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));



            }else if(tempid==6){
                FormDataModel.otheres2=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();
                o2btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));



            }else if(tempid==7){
                FormDataModel.otheres3=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();
                o3btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));



            }else if(tempid==8){
                FormDataModel.otheres4=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();
                o4btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));



            }else if(tempid==9){
                FormDataModel.professional1=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();
                p1btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));



            }else if(tempid==10){
                FormDataModel.professional2=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();
                p2btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));



            }else if(tempid==11){
                FormDataModel.professional3=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();
                p3btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));



            }else if(tempid==12){
                FormDataModel.professional4=tempBitmap;
                Toast.makeText(this, "Picture added", Toast.LENGTH_SHORT).show();

                p4btn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));


            }

        }
    }


}
