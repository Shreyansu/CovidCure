package com.example.newcovidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText name,age,phone,address;
    RadioButton travelY,TravelN,genderM,genderF;
    int travelStatus=0,gender=0;


    EditText recentPlace;
    RadioButton qYes, qNo, typeHome, typeGovt;
    boolean qStatus;
    String quartype;
    String places;
    TextView proceedBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=findViewById(R.id.regName);
        age=findViewById(R.id.age);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        travelY=findViewById(R.id.radioTravelYes);
        TravelN=findViewById(R.id.radioTravelNo);
        genderM=findViewById(R.id.radioMale);
        genderF=findViewById(R.id.radioFemale);
        travelY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TravelN.setChecked(false);
                travelStatus=1;
                openDialog();
            }
        });
        TravelN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                travelY.setChecked(false);
                travelStatus=2;
            }
        });
        genderM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderF.setChecked(false);
                gender=1;
            }
        });
        genderF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderM.setChecked(false);
                gender=2;

            }
        });
        updateData();

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namet = name.getText().toString();
                String aget = age.getText().toString();
                String phonet = phone.getText().toString();
                String addresst = address.getText().toString();
                if (namet.isEmpty() || aget.isEmpty() || phonet.isEmpty() || addresst.isEmpty() || gender == 0 || travelStatus == 0) {
                    final Dialog dialog = new Dialog(RegisterActivity.this);
                    dialog.setContentView(R.layout.alert_dialog);
                    TextView back = dialog.findViewById(R.id.proceed);
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                } else {
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put("name", namet);
                    objectMap.put("age", aget);
                    objectMap.put("phone", phonet);
                    objectMap.put("address", addresst);
                    objectMap.put("gender", gender == 1 ? "M" : "F");
                    objectMap.put("travelled", travelStatus == 1);
                    objectMap.put("quarantineStatus", qStatus);
                    objectMap.put("quarantineType", quartype);
                    objectMap.put("placesTravelled", places);
                    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(objectMap);

                    Intent intent = new Intent(RegisterActivity.this, NewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("type", "User");
                    startActivity(intent);
                }
            }
        });


    }
    HashMap<String,Object> map;

    private void updateData()
    {

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                progressDialog.dismiss();
                map=(HashMap<String,Object>) dataSnapshot.getValue();
                if(map!=null)
                {
                    name.setText(String.valueOf(map.get("name")));
                    age.setText(String.valueOf(map.get("age")));
                    phone.setText(String.valueOf(map.get("phone")));
                    address.setText(String.valueOf(map.get("address")));
                    String gender = (String) map.get("gender");


                    if (gender != null && gender.equals("M")){
                        RegisterActivity.this.gender = 1;
                        genderM.setChecked(true);
                    } else if (gender != null && gender.equals("F")){
                        RegisterActivity.this.gender = 2;
                        genderF.setChecked(true);
                    }
                    boolean travelStatus = (boolean) map.get("travelled");
                    if(travelStatus) {
                        RegisterActivity.this.travelStatus = 1;
                        travelY.setChecked(true);
                    }else{
                        RegisterActivity.this.travelStatus = 2;
                        TravelN.setChecked(true);
                    }
                    qStatus = (boolean) map.get("quarantineStatus");
                    quartype = (String) map.get("quarantineType");
                    places = (String) map.get("placesTravelled");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
                Log.e("Register",databaseError.getMessage());

            }
        });

    }
    private void openDialog()
    {
        final Dialog dialog=new Dialog(RegisterActivity.this);
        dialog.setContentView(R.layout.dialog_layout);

        recentPlace=dialog.findViewById(R.id.recentPlace);
        qYes=dialog.findViewById(R.id.radioQuarYes);
        qNo=dialog.findViewById(R.id.radioQuarNo);
        typeGovt=dialog.findViewById(R.id.radioGovt);
        typeHome=dialog.findViewById(R.id.radioHome);
        proceedBtn=dialog.findViewById(R.id.proceed);
        if(map!=null){
            recentPlace.setText(places);
            if(qStatus)
                qYes.setChecked(true);
            else
                qNo.setChecked(true);
            if(quartype!=null && quartype.equals("Govt. Center")){
                typeGovt.setChecked(true);
            }else if(quartype!=null && quartype.equals("Home Quarantine")){
                typeHome.setChecked(true);
            }
        }

        qYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qNo.setChecked(false);
                qStatus = true;
            }
        });
        qNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qYes.setChecked(false);
                qStatus = false;

            }
        });
        typeGovt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeHome.setChecked(false);
                quartype = "Govt. Center";
            }
        });
        typeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeGovt.setChecked(false);
                quartype = "Home Quarantine";
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                places = recentPlace.getText().toString();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


//    public void registerClick(View view) {
//        String namet=name.getText().toString();
//        String aget=age.getText().toString();
//        String phonet=phone.getText().toString();
//        String addresst=address.getText().toString();
//        if(namet.isEmpty() || aget.isEmpty() || phonet.isEmpty() || addresst.isEmpty() || gender==0 || travelStatus==0)
//        {
//            final Dialog dialog =new Dialog(RegisterActivity.this);
//            dialog.setContentView(R.layout.alert_dialog);
//            TextView back=dialog.findViewById(R.id.proceed);
//            back.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.show();
//        }
//        else{
//            Map<String,Object> objectMap = new HashMap<>();
//            objectMap.put("name",name);
//            objectMap.put("age",age);
//            objectMap.put("phone",phone);
//            objectMap.put("address",address);
//            objectMap.put("gender", gender==1 ? "M" : "F");
//            objectMap.put("travelled", travelStatus == 1);
//            objectMap.put("quarantineStatus",qStatus);
//            objectMap.put("quarantineType",quartype);
//            objectMap.put("placesTravelled",places);
//            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(objectMap);
//
//            Intent intent = new Intent(RegisterActivity.this, NewActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.putExtra("type", "User");
//            startActivity(intent);
//        }
//    }
}