package com.example.newcovidtracker.Bed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.newcovidtracker.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospitalRegistration2 extends AppCompatActivity
{
    CircleImageView empCircleImageView;
    TextInputEditText kEmpName,kEmpId,kEmpNumber;
    Button submit;

    Uri FilePathUri;
    int image_request_code=7;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FusedLocationProviderClient fusedLocationProviderClient;
    String pin="not provided";
    String district="not provided";
    StorageReference storageReference;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;

    private final int UNIQUE_REQUEST_CODE=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_registration2);
        empCircleImageView=findViewById(R.id.uploadImage2);
        kEmpName=findViewById(R.id.et_name_emp);
        kEmpId=findViewById(R.id.et_id_emp);
        kEmpNumber=findViewById(R.id.et_phone_number_emp);
        submit=findViewById(R.id.employee_submit);
        auth= FirebaseAuth.getInstance();

        //get data from intent

        final String txt_hospital_name=getIntent().getExtras().getString("Hospital Name","defaultKey");
        final String txt_hospital_id=getIntent().getExtras().getString("Email","defaultKey");
        final String txt_hospital_password=getIntent().getExtras().getString("Password","defaultKey");

        empCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent =new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"), image_request_code);


            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String txt_employee_name=kEmpName.getText().toString();
                String txt_employee_id=kEmpId.getText().toString();
                String txt_employee_number=kEmpNumber.getText().toString();

                if(TextUtils.isEmpty(txt_employee_name) || TextUtils.isEmpty(txt_employee_id) || TextUtils.isEmpty(txt_employee_number))
                    Toast.makeText(HospitalRegistration2.this, "Please fill all the necessary details", Toast.LENGTH_SHORT).show();
                else if(txt_employee_number.length() !=10)
                    Toast.makeText(HospitalRegistration2.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                else
                {
//                    if(null !=empCircleImageView.getDrawable())
//                    {
                    Register(txt_hospital_name,txt_hospital_id,txt_hospital_password,txt_employee_name,txt_employee_id,txt_employee_number);


//                    }
//                    else
//                        Toast.makeText(HospitalRegistration2.this, "Please add your ID Card by tapping on the circle", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void Register(final String hospital_name, final String hospital_id, String hospital_password, final String employee_name, final String employee_id, final String employee_number)
    {
//        final ProgressDialog pd = new ProgressDialog(HospitalRegistration2.this);
//        pd.setMessage("Registering Hospital");
//        pd.show();
//        pd.setCancelable(false);

        auth.createUserWithEmailAndPassword(hospital_id,hospital_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            firebaseUser=auth.getCurrentUser();
                            assert firebaseUser!=null;
                            String userId=firebaseUser.getUid();

                            reference= FirebaseDatabase.getInstance().getReference("HospitalUsers").child(userId);

                            HashMap<String,String> hashMap =new HashMap<>();
                            hashMap.put("Hospital UID",userId);
                            hashMap.put("Hospital ID",hospital_id);
                            hashMap.put("Hospital Name",hospital_name);
                            hashMap.put("Total Number of beds","NA");
                            hashMap.put("Number of beds available","NA");
                            hashMap.put("ID CARD URL","NA");
                            hashMap.put("Date-Time","NA");
                            hashMap.put("Postal Code","NA");
                            hashMap.put("District","NA");
                            hashMap.put("Employee Name",employee_name);
                            hashMap.put("Employee ID",employee_id);
                            hashMap.put("Employee Phone Number",employee_number);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
//                                        pd.dismiss();
                                        Toast.makeText(HospitalRegistration2.this, "Registration Successful", Toast.LENGTH_SHORT).show();Intent intent =new Intent(HospitalRegistration2.this,HospitalBedUpdate.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                        getLocation();
//                                        uploadImage();

                                    }

                                }
                            });

                        }
                        else
                        {
//                            pd.dismiss();
                            Toast.makeText(HospitalRegistration2.this, "You can't register with this Email Id and Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }



    @SuppressLint("MissingPermission")
    private void getLocation()
    {
        if(ContextCompat.checkSelfPermission(HospitalRegistration2.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {

            try
            {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task)
                    {
                        Location location =task.getResult();
                        if(location!=null)
                        {
                            try {
                                Geocoder geocoder =new Geocoder(HospitalRegistration2.this, Locale.getDefault());
                                List<Address> address=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                pin=address.get(0).getPostalCode();
                                district=address.get(0).getLocality();

                                String uid=firebaseUser.getUid();

                                reference.child("HospitalUsers").child(uid).child("Postal Code").setValue(pin);
                                reference.child("HospitalUsers").child(uid).child("District").setValue(district);
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        else
            ActivityCompat.requestPermissions(HospitalRegistration2.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, UNIQUE_REQUEST_CODE);

    }
    // contract for receiving the results for permission requests.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==UNIQUE_REQUEST_CODE)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                getLocation();
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
        }
        else if(grantResults[0]==PackageManager.PERMISSION_DENIED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(HospitalRegistration2.this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                AlertDialog.Builder dialog= new AlertDialog.Builder(this);
                dialog.setMessage("This permission is important");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(HospitalRegistration2.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},UNIQUE_REQUEST_CODE);
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        }
    }

    //gallery Intent reply


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==image_request_code && resultCode==RESULT_OK && data !=null && data.getData()!=null)
        {
            FilePathUri =data.getData();
            try {
                {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),FilePathUri);
                    empCircleImageView.setImageBitmap(bitmap);
                    Glide.with(HospitalRegistration2.this).load(FilePathUri).into(empCircleImageView);
                }
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {

                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void uploadImage()
    {
        final ProgressDialog pd=new ProgressDialog((HospitalRegistration2.this));
        pd.setMessage("Uploading Image");
        pd.show();
        pd.setCancelable(false);

        if(FilePathUri !=null)
        {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+GetFileExtension(FilePathUri));
            uploadTask=fileReference.putFile(FilePathUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {

                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();

                        String uid=firebaseUser.getUid();

                        reference.child("HospitalUsers").child(uid).child("ID CARD URL").setValue(mUri);
                        pd.dismiss();

                        Intent intent =new Intent(HospitalRegistration2.this,HospitalBedUpdate.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(HospitalRegistration2.this, "Failed !", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(HospitalRegistration2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
        else
            Toast.makeText(HospitalRegistration2.this,"Please add your ID CARD image",Toast.LENGTH_SHORT).show();
    }
}
