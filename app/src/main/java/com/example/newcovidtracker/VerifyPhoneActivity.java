package com.example.newcovidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

import com.google.firebase.auth.PhoneAuthCredential;


import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {


    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        mAuth=FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);

        et1 = findViewById(R.id.et_1);
        et2 = findViewById(R.id.et_2);
        et3 = findViewById(R.id.et_3);
        et4 = findViewById(R.id.et_4);
        et5 = findViewById(R.id.et_5);
        et6 = findViewById(R.id.et_6);


        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(!editable.toString().equals(""))
                {
                    et2.requestFocus();

                }

            }
        });
        changeTextViewFocus(et1, et2, et3);
        changeTextViewFocus(et2, et3, et4);
        changeTextViewFocus(et3, et4, et5);
        changeTextViewFocus(et4, et5, et6);

        et6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().equals(""))
                    et5.requestFocus();


            }
        });

        String phonenumber = getIntent().getStringExtra("phonenumber");

        sendVerificationCode(phonenumber);

        findViewById(R.id.verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String code = et1.getText().toString().trim()
                        + et2.getText().toString().trim()
                        + et3.getText().toString().trim()
                        + et4.getText().toString().trim()
                        + et5.getText().toString().trim()
                        + et6.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    et1.setError("Enter code...");
                    et1.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });
    }






    private void sendVerificationCode(String number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );


    }


    private void changeTextViewFocus(final EditText et1, EditText et2, final EditText et3)
    {
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals(""))
                {
                    et3.requestFocus();

                }
                else
                    et1.requestFocus();

            }
        });
    }





    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(VerifyPhoneActivity.this, RegisterActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }





    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }





    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                int codeint = Integer.parseInt(code);

                int six = codeint % 10;
                int five = (codeint % 100) / 10;
                int four = (codeint % 1000) / 100;
                int three = (codeint % 10000) / 1000;
                int two = (codeint % 100000) / 10000;
                int one = (codeint) / 100000;

                et1.setText(String.valueOf(one));
                et2.setText(String.valueOf(two));
                et3.setText(String.valueOf(three));
                et4.setText(String.valueOf(four));
                et5.setText(String.valueOf(five));
                et6.setText(String.valueOf(six));

                verifyCode(code);
            } else {

                et1.setText("1");
                et2.setText("2");
                et3.setText("3");
                et4.setText("4");
                et5.setText("5");
                et6.setText("6");

                signInWithCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

}