package com.example.newcovidtracker.Bed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.newcovidtracker.R;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospitalRegistration extends AppCompatActivity
{
    CircleImageView circleImageView;
    TextInputEditText kHospitalName,kHospitalId,kHospitalPass;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_registration);



        circleImageView=findViewById(R.id.uploadImage);
        kHospitalName=findViewById(R.id.et_name_hos);
        kHospitalId=findViewById(R.id.et_id_hos);
        kHospitalPass=findViewById(R.id.et_pass_hos);
        submit=findViewById(R.id.hospital_submit);

        String txt_hospital_id_from_login=getIntent().getExtras().getString("Email","defaultKey");
        kHospitalId.setText(txt_hospital_id_from_login);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String txt_hospital_name=kHospitalName.getText().toString();
                String txt_hospital_id=kHospitalId.getText().toString();
                String txt_hospital_pass=kHospitalPass.getText().toString();

                if(TextUtils.isEmpty(txt_hospital_name) || TextUtils.isEmpty(txt_hospital_id) || TextUtils.isEmpty(txt_hospital_pass))
                {
                    Toast.makeText(HospitalRegistration.this, "Please Fill all the necessary details", Toast.LENGTH_SHORT).show();
                }
                else if(txt_hospital_pass.length() < 6)
                    Toast.makeText(HospitalRegistration.this, "Password Should be atleast of 6 characters", Toast.LENGTH_SHORT).show();
                else
                    send(txt_hospital_name,txt_hospital_id,txt_hospital_pass);



            }
        });



    }

    private void send(String txt_hospital_name, String txt_hospital_id, String txt_hospital_pass)
    {
        Intent mIntent=new Intent(HospitalRegistration.this,HospitalRegistration2.class);
        mIntent.putExtra("Hospital Name",txt_hospital_name);
        mIntent.putExtra("Email",txt_hospital_id);
        mIntent.putExtra("Password",txt_hospital_pass);
        startActivity(mIntent);

    }
}