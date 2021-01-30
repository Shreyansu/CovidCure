package com.example.newcovidtracker.Bed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.newcovidtracker.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HospitalBedUpdate extends AppCompatActivity
{
    TextInputEditText ktotalBeds,kavailableBeds;
    Button kButton;
    String txtTotalBeds,txtAvailableBeds;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser kuser;
    FirebaseAuth auth;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_bed_update);

        ktotalBeds=findViewById(R.id.et_bed_text);
        kavailableBeds=findViewById(R.id.et_avail_bed_text);
        kButton=findViewById(R.id.submit_bed);

//        txtTotalBeds=ktotalBeds.getText().toString();
//        txtAvailableBeds=kavailableBeds.getText().toString();

        database=FirebaseDatabase.getInstance();
        reference=database.getReference();

        kuser=FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();
        uid=auth.getCurrentUser().getUid();

        kButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Calendar c=Calendar.getInstance();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate =sdf.format(c.getTime());
                txtTotalBeds=ktotalBeds.getText().toString();
                txtAvailableBeds=kavailableBeds.getText().toString();

                UpdateData(txtTotalBeds,txtAvailableBeds,reference,formattedDate);


            }
        });

    }

    private void UpdateData(String txttotalBeds, String txtavailableBeds, DatabaseReference kreference, String formattedDate)
    {
        final ProgressDialog pd =new ProgressDialog(HospitalBedUpdate.this);
        pd.setMessage("Checking Details");
        pd.show();
        pd.setCancelable(false);

        reference.child("HospitalUsers").child(uid).child("Total Number of beds").setValue("Total Number of beds :" + txttotalBeds);
        reference.child("HospitalUsers").child(uid).child("Number of beds available").setValue("Number of beds available:" + txtavailableBeds);
        reference.child("HospitalUsers").child(uid).child("Date-Time").setValue("Updated on:"+formattedDate);

        kreference.child("HospitalUsers").child(uid).child("Date-Time").equalTo(formattedDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        pd.dismiss();
                        Toast.makeText(HospitalBedUpdate.this, "Data Updated", Toast.LENGTH_SHORT).show();
                        ktotalBeds.setText("");
                        kavailableBeds.setText("");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        pd.dismiss();
                        Toast.makeText(HospitalBedUpdate.this, "Error Updating the data,Please try again !", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}