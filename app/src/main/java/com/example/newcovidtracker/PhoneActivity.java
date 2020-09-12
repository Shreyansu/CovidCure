package com.example.newcovidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PhoneActivity extends AppCompatActivity {

    private EditText editText;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        editText = (EditText) findViewById(R.id.et_phone_number);
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = editText.getText().toString().trim();
                if (number.isEmpty() || number.length() < 10) {
                    editText.setError("Please Enter Valid Number");
                    editText.requestFocus();
                    return;
                }
                String phoneNumber = "+" + 91 + number;
                preferences = getSharedPreferences("App", MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("Contact", phoneNumber);
                editor.commit();

                Intent intent = new Intent(PhoneActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", phoneNumber);
                startActivity(intent);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            Intent intent = new Intent(this, NewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.putExtra("type", "User");
            startActivity(intent);

        }
    }
}
