package com.example.datacollectionapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.ExtractedText;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.datacollectionapp.model.FormData;
import com.example.datacollectionapp.model.FormDataModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.FormatMethod;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SectionSix extends AppCompatActivity {
    Spinner job1,job2;
    int tempid;
      Button apbtn;
    List<String> jobgroups;
    private ArrayAdapter<String> jobadaptder;
    UploadTask uploadTask;
    FirebaseFirestore database;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap tempBitmap;
    ByteArrayOutputStream baos;
    byte[] data;
     ProgressDialog progressDialog;

    StorageReference storageReference;
    EditText currdentdDuties,currentDesignation,currentedate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_six);
          database=FirebaseFirestore.getInstance();

        apbtn=findViewById(R.id.apbtn);
        job1=findViewById(R.id.job1);
        currdentdDuties=findViewById(R.id.currentdduties);
        currentDesignation=findViewById(R.id.currentdesignation);
        currentedate=findViewById(R.id.currentedate);


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

                FormDataModel.appointmentletter=tempBitmap;
                Toast.makeText(this, "Picature added", Toast.LENGTH_SHORT).show();
                apbtn.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
            }
        }
    }

    public void takepicture(View view){
        tempid=1;
        captureImage();


    }
public void finish(View view){


    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Uploain information to server please wait..");
        progressDialog.setTitle("Uploading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    if(FormDataModel.profile!=null){

        FormDataModel.formData.setCurrentDuities(currdentdDuties.getText().toString());
    FormDataModel.formData.setCurrentJob(jobgroups.get(job1.getSelectedItemPosition()));
    FormDataModel.formData.setCurrentEffectiveDate(currentedate.getText().toString());
    FormDataModel.formData.setCurrentDesignation(currentDesignation.getText().toString());

    storageReference = FirebaseStorage.getInstance().getReference();

    final StorageReference profileimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"profileimage.jpg");
    baos = new ByteArrayOutputStream();
    FormDataModel.profile.compress(Bitmap.CompressFormat.JPEG, 100, baos);

    data = baos.toByteArray();


    uploadTask = profileimagestorageref.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return profileimagestorageref.getDownloadUrl();
        }
    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull Task<Uri> task) {
                       if(task.isSuccessful()) {
                           Uri result = task.getResult();
                           FormDataModel.formData.setProfileimageurl(result.toString());
                          uploaddob();
                       }else{
                           progressDialog.dismiss();
                           Toast.makeText(SectionSix.this, "Error accured", Toast.LENGTH_SHORT).show();
                       }



        }
    });


        }else{

uploaddob();
        }





}
public void uploaddob(){
if(FormDataModel.dob!=null){
    FormDataModel.dob.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    data=baos.toByteArray();
    final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"dobimage.jpg");
    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
        @Override
        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
            if (!task.isSuccessful()) {
                throw task.getException();
            }return  dobimagestorageref.getDownloadUrl();
        }
    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull Task<Uri> task) {
            if(task.isSuccessful()){
                Uri result = task.getResult();
                FormDataModel.formData.setDobcertificate(task.getResult().toString());
                uploada1();

            }
        }
    });}else{
    uploada1();

}

}

public void uploada1(){
if(FormDataModel.acadamic1!=null){
    FormDataModel.acadamic1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    data=baos.toByteArray();
    final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"acadamic1.jpg");
    uploadTask=dobimagestorageref.putBytes(data);
    uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
        @Override
        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
            return  dobimagestorageref.getDownloadUrl();
        }
    }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
        @Override
        public void onComplete(@NonNull Task<Task<Uri>> task) {
            if(task.isSuccessful()){
               FormDataModel.formData.setAcadamic1image(task.getResult().toString());
                    uploada2();

            }
        }
    });
}else{                    uploada2();
 }

}

    public void uploada2(){
        if(FormDataModel.acadamic2!=null){

        FormDataModel.acadamic2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"acadamic2.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setAcadamic2image(task.getResult().toString());
                    uploada3();

                }
            }
        });
        }else{
            uploada3();


        }


    }

    public void uploada3(){
        if(FormDataModel.acadamic3!=null){

        FormDataModel.acadamic3.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"acadamic3.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setAcadamic3image(task.getResult().toString());
                    uploada4();

                }
            }
        });

        }else{                    uploada4();
        }
    }

    public void uploada4(){
        if(FormDataModel.acadamic4!=null){

        FormDataModel.acadamic4.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"acadamic4.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setAcadamic4image(task.getResult().toString());
                    uploado1();

                }
            }
        });
        }else{                    uploado1();
        }

    }

    public void uploado1(){
        if(FormDataModel.otheres1!=null){
        FormDataModel.otheres1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"other1.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setOther1image(task.getResult().toString());
                    uploado2();

                }
            }
        });
        }else{

            uploado2();

        }


    }

    public void uploado2(){
        if(FormDataModel.otheres2!=null){
        FormDataModel.otheres2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"other2.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setOther2image(task.getResult().toString());
                    uploado3();

                }
            }
        });

        }else{

            uploado3();

        }


    }

    public void uploado3(){
        if(FormDataModel.otheres3!=null){

        FormDataModel.otheres3.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"other3.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setOther3image(task.getResult().toString());
                    uploado4();

                }
            }
        });

        }else{

            uploado4();

        }


    }

    public void uploado4(){
        if(FormDataModel.otheres4!=null){

        FormDataModel.otheres4.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"other4.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setOther4image(task.getResult().toString());
                    uploadp1();

                }
            }
        });

        }else{
            uploadp1();


        }

    }

    public void uploadp1(){
        if(FormDataModel.professional1!=null){

        FormDataModel.professional1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"professional1.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setProfessional1image(task.getResult().toString());
                    uploadp2();

                }
            }
        });
        }else{
            uploadp2();


        }

    }

    public void uploadp2(){
        if(FormDataModel.professional2!=null){

        FormDataModel.professional2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"professional2.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setProfessional2image(task.getResult().toString());
                    uploadp3();

                }
            }
        });
        }else{                    uploadp3();
        }

    }

    public void uploadp3(){
        if(FormDataModel.professional3!=null){

        FormDataModel.professional3.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"professional3.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setProfessional3image(task.getResult().toString());
                    uploadp4();

                }
            }
        });
        }else{
            uploadp4();


        }

    }

    public void uploadp4(){
        if(FormDataModel.professional4!=null){

        FormDataModel.professional4.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"professional4.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setProfessional4image(task.getResult().toString());
                    uploadid();

                }
            }
        });

        }else{
            uploadid();


        }

    }
    public void uploadid(){
        if(FormDataModel.id!=null){

        FormDataModel.id.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"idcard.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setIdcardimage(task.getResult().toString());
                    uploadedtail();

                }
            }
        });
        }else{

            uploadedtail();

        }

    }

    public void uploadedtail(){

        if(FormDataModel.employeedetail!=null){

        FormDataModel.employeedetail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"employeedetail.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setEmployedetailsImage(task.getResult().toString());
                    uploadpromotional();

                }
            }
        });
        }else{

            uploadpromotional();

        }

    }
    public void  uploadpromotional(){
        if(FormDataModel.promotion!=null){

        FormDataModel.promotion.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"promotiondetails.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setPromotionImage(task.getResult().toString());
                    uploadpayslip1();

                }
            }
        });}else{
            uploadpayslip1();


        }


    }
    public void uploadpayslip1(){
        if(FormDataModel.slip1!=null){

        FormDataModel.slip1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"payslip1.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setPaySlip1image(task.getResult().toString());
                    uploadpayslip2();

                }
            }
        });
        }else{                    uploadpayslip2();
        }


    }
    public void uploadpayslip2(){
        if(FormDataModel.slip2!=null){

        FormDataModel.slip2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"payslip2.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setPaySlip2image(task.getResult().toString());
                    uploadpayslip3();

                }
            }
        });
        }else{
            uploadpayslip3();


        }
    }
    public void uploadpayslip3(){
        if(FormDataModel.slip3!=null){

        FormDataModel.slip3.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"payslip3.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setPaySlip3image(task.getResult().toString());
                    uploadappointmentletter();

                }
            }
        });
        }else{                    uploadappointmentletter();
        }
    }
    public void uploadappointmentletter(){
        if(FormDataModel.appointmentletter!=null){

        FormDataModel.appointmentletter.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FormDataModel.formData.getPersonalNumber()+"/"+"appointmentletter.jpg");
        uploadTask=dobimagestorageref.putBytes(data);
        uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return  dobimagestorageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    FormDataModel.formData.setAppointmentLetterImage(task.getResult().toString());
                   uploadFormdata();
                }
            }
        });
        }else{                   uploadFormdata();
        }



    }
public void uploadFormdata(){
database.collection("User").document(FormDataModel.formData.getPersonalNumber()).set(FormDataModel.formData);
progressDialog.dismiss();
startActivity(new Intent(SectionSix.this,SectionOne.class));
finish();

}
}
