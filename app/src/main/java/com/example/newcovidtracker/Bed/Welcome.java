package com.example.newcovidtracker.Bed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.newcovidtracker.NewActivity;
import com.example.newcovidtracker.PhoneActivity;
import com.example.newcovidtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.List;
import java.util.Objects;

public class Welcome extends AppCompatActivity
{
    private Button hospital_login,user_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        hospital_login=findViewById(R.id.hospital);
        user_login=findViewById(R.id.user);

        hospital_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Welcome.this,HospitalLogin.class));
                finish();

            }
        });
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this, PhoneActivity.class));
                finish();
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            List<? extends UserInfo> pd = user.getProviderData();
            UserInfo providerData = pd.get(1);
            String pid = providerData.getProviderId();
            if (Objects.equals(pid, "password")) {
                Intent intent = new Intent(this, NewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("type", "Admin");
                startActivity(intent);
            } else if (Objects.equals(pid, "phone")) {
                Intent intent = new Intent(this, NewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("type", "User");
                startActivity(intent);
            }
        }
    }


}