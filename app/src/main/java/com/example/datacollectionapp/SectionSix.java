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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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


       /// if(FormDataModel.appointmentletter!=null){
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Uploain information to server please wait..");
        progressDialog.setTitle("Uploading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        FormDataModel.formData.setCurrentDuities(currdentdDuties.getText().toString());
    FormDataModel.formData.setCurrentJob(jobgroups.get(job1.getSelectedItemPosition()));
    FormDataModel.formData.setCurrentEffectiveDate(currentedate.getText().toString());
    FormDataModel.formData.setCurrentDesignation(currentDesignation.getText().toString());

    storageReference = FirebaseStorage.getInstance().getReference();

    final StorageReference profileimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"profileimage.jpg");
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


       /* }else{

            Toast.makeText(this, "Some fields are empty", Toast.LENGTH_SHORT).show();

        }

*/



}
public void uploaddob(){

    FormDataModel.dob.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    data=baos.toByteArray();
    final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"dobimage.jpg");
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
                FormDataModel.formData.setDobcertificate(task.getResult().toString());
                uploada1();

            }
        }
    });

}

public void uploada1(){

    FormDataModel.acadamic1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    data=baos.toByteArray();
    final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"acadamic1.jpg");
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


}

    public void uploada2(){
        FormDataModel.acadamic2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"acadamic2.jpg");
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



    }

    public void uploada3(){
        FormDataModel.acadamic3.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"acadamic3.jpg");
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


    }

    public void uploada4(){

        FormDataModel.acadamic4.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"acadamic4.jpg");
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


    }

    public void uploado1(){
        FormDataModel.otheres1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"other1.jpg");
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


    }

    public void uploado2(){

        FormDataModel.otheres2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"other2.jpg");
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



    }

    public void uploado3(){
        FormDataModel.otheres3.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"other3.jpg");
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




    }

    public void uploado4(){

        FormDataModel.otheres4.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"other4.jpg");
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



    }

    public void uploadp1(){

        FormDataModel.professional1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"professional1.jpg");
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


    }

    public void uploadp2(){

        FormDataModel.professional2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"professional2.jpg");
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


    }

    public void uploadp3(){
        FormDataModel.professional3.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"professional3.jpg");
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


    }

    public void uploadp4(){

        FormDataModel.professional4.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"professional4.jpg");
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



    }
    public void uploadid(){

        FormDataModel.id.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"idcard.jpg");
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


    }

    public void uploadedtail(){


        FormDataModel.employeedetail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"employeedetail.jpg");
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


    }
    public void  uploadpromotional(){
        FormDataModel.promotion.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"promotiondetails.jpg");
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
        });


    }
    public void uploadpayslip1(){

        FormDataModel.slip1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"payslip1.jpg");
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



    }
    public void uploadpayslip2(){

        FormDataModel.slip2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"payslip2.jpg");
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

    }
    public void uploadpayslip3(){
        FormDataModel.slip3.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"payslip3.jpg");
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

    }
    public void uploadappointmentletter(){

        FormDataModel.appointmentletter.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data=baos.toByteArray();
        final StorageReference dobimagestorageref=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/"+"appointmentletter.jpg");
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




    }
public void uploadFormdata(){
database.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).set(FormDataModel.formData);
progressDialog.dismiss();
startActivity(new Intent(SectionSix.this,SectionOne.class));
finish();

}
}
