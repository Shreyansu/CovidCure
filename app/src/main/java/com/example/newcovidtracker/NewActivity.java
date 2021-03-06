package com.example.newcovidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newcovidtracker.Bed.UserBedActivity;
import com.example.newcovidtracker.Bed.Welcome;
import com.example.newcovidtracker.Tracker.MainActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class NewActivity extends AppCompatActivity {
    CardView card1, card2, card3;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ImageView bell;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DatabaseReference reference, userRef;
    Boolean started;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Integer total;
    NavigationView navigationView;
    HashMap<String,Object> map;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer);
//        setSupportActionBar(toolbar);
        navigationView=findViewById(R.id.nav_view);
        Menu menu =navigationView.getMenu();

        MenuItem add_item = menu.findItem(R.id.ad_admin);
        if(Objects.equals("type","Admin"))
        {
            add_item.setVisible(true);
        }
        else
        {
            add_item.setVisible(false);
        }
        MenuItem dashboard=menu.findItem(R.id.ad_dash);
        if(Objects.equals("type","Admin"))
        {
            dashboard.setVisible(true);
        }
        else
        {
            dashboard.setVisible(false);
        }

        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                map=(HashMap<String,Object>) dataSnapshot.getValue();
                if(map!=null)
                {
                    TextView namet=findViewById(R.id.user);
                    namet.setText(String.valueOf(map.get("name")));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Register",databaseError.getMessage());

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.faq:
                        startActivity(new Intent(NewActivity.this,faqActivity.class));
                        break;

                    case R.id.developer:
                        startActivity(new Intent(NewActivity.this,DevloperActivity.class));
                        break;

                    case R.id.edit_profile:
                        startActivity(new Intent(NewActivity.this,RegisterActivity.class));
                        break;

                    case R.id.ad_admin:
                        startActivity(new Intent(NewActivity.this,AddAdminActivity.class));
                        break;

                    case R.id.ad_dash:
                        startActivity(new Intent(NewActivity.this,AdminDashBoard.class));
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(NewActivity.this, Welcome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        card1=findViewById(R.id.cardview1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(NewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        card2=findViewById(R.id.cardview2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewActivity.this,GeoFenceActivity.class);
                startActivity(intent);
            }
        });
        card3=findViewById(R.id.cardview3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent =new Intent(NewActivity.this, UserBedActivity.class);
                startActivity(intent);

            }
        });

    }
}