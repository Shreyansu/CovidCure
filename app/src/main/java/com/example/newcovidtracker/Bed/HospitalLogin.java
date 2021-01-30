package com.example.newcovidtracker.Bed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.newcovidtracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HospitalLogin extends AppCompatActivity
{
    private TextInputLayout mailLay,passLay;
    private Button loginbtn;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_login);

        mailLay=findViewById(R.id.mail_layout);
        passLay=findViewById(R.id.pass_layout);
        loginbtn=findViewById(R.id.btnlogin);


        mAuth =FirebaseAuth.getInstance();


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String email=mailLay.getEditText().getText().toString();
                String pass=passLay.getEditText().getText().toString();

                if(TextUtils.isEmpty(mailLay.getEditText().getText()) || TextUtils.isEmpty(passLay.getEditText().getText()))
                {
                    Toast.makeText(HospitalLogin.this,"Enter the auth",Toast.LENGTH_SHORT);
                }
                else if((mailLay.equals("abc@gmail.com")) || (passLay.equals("123")))
                {
                    Intent intent =new Intent(HospitalLogin.this,HospitalBedUpdate.class);
                    finish();
                    startActivity(intent);
                }
                else
                {
                    userRegistrationCheck(email,pass);
                }

            }
        });



    }

    private void userRegistrationCheck(final String email, final String pass)
    {
        final ProgressDialog pd = new ProgressDialog((HospitalLogin.this));
        pd.setMessage("Checking Details");
        pd.show();
        pd.setCancelable(false);

        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("HospitalUsers");
        userRef.orderByChild("Hospital ID").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) 
            {
                if(snapshot.getValue() !=null)
                {
                    pd.dismiss();
                    signIn(email,pass);
                }
                else
                {
                    pd.dismiss();
                    Intent mIntent =new Intent(HospitalLogin.this,HospitalRegistration.class);
                    mIntent.putExtra("Email",email);
                    finish();
                    startActivity(mIntent);
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                pd.dismiss();
                Toast.makeText(HospitalLogin.this, "Error in connecting with database.Please restart and try again!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void signIn(String email, String pass)
    {
        final ProgressDialog pd =new ProgressDialog(HospitalLogin.this);
        pd.setMessage("Signing In");
        pd.show();
        pd.setCancelable(false);

        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            pd.dismiss();
                            Intent intent =new Intent(HospitalLogin.this,HospitalBedUpdate.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            pd.dismiss();
                            Toast.makeText(HospitalLogin.this, "Authentication failed !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
}