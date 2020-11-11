package com.example.newcovidtracker.Tracker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newcovidtracker.R;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;
import java.util.List;

public class DistrictActivity extends AppCompatActivity {

    EditText Search;
    ListView listView;
    SimpleArcLoader loader;

    public static List<DistrictModel> DistrictModelList = new ArrayList<>();
    DistrictModel districtModel;
    DistrictAdapter districtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);

        listView = findViewById(R.id.listView);
        loader = findViewById(R.id.loader);
        Search = findViewById(R.id.search);
        getSupportActionBar().setTitle("Track District");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                districtAdapter.getFilter().filter(s);
                districtAdapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


}


