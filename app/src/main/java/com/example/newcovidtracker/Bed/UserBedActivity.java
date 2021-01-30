package com.example.newcovidtracker.Bed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.example.newcovidtracker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserBedActivity extends AppCompatActivity
{
    RecyclerView recView;

    DatabaseReference reference;
    ArrayList<bedModel> list;
    bedAdapter bedAdapter;
    Toolbar toolbar;
    ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bed);


        recView=findViewById(R.id.bedList);
        recView.setLayoutManager(new LinearLayoutManager(this));
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pd=new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.show();
        reference= FirebaseDatabase.getInstance().getReference().child("HospitalUsers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                list =new ArrayList<bedModel>();
                for(DataSnapshot sp :snapshot.getChildren())
                {
                    String name=sp.child("Hospital Name").getValue().toString();
                    String avail=sp.child("Number of beds available").getValue().toString();
                    String total=sp.child("Total Number of beds").getValue().toString();

                    bedModel details =new bedModel(name,total,avail);
                    list.add(details);

                }
                bedAdapter =new bedAdapter(UserBedActivity.this,list);
                recView.setAdapter(bedAdapter);
                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(UserBedActivity.this, "Something went wrong,Please try again !", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item =menu.findItem(R.id.search);

        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                bedAdapter.getFilter().filter(s);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
}