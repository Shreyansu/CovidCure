package com.example.newcovidtracker.Tracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newcovidtracker.R;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IndiaActivity extends AppCompatActivity {

    EditText Search;
    ListView listView;
    SimpleArcLoader loader;

    public static List<IndiaModel> IndiaModelList = new ArrayList<>();
    IndiaModel indiaModel;
    IndiaAdapter indiaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_india);

        listView =findViewById(R.id.listView);
        loader  = findViewById(R.id.loader);
        Search = findViewById(R.id.search);
        getSupportActionBar().setTitle("Track State");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        fetchData();




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                startActivity(new Intent(getApplicationContext(),DetailIndiaActivity.class).putExtra("position",position));

            }
        });

        Search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                indiaAdapter.getFilter().filter(s);
                indiaAdapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable s)
            {


            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void fetchData()
    {

        String Url = "https://api.covid19india.org/data.json";

        loader.start();

        StringRequest request = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("statewise");

                    for(int i =0;i<jsonArray.length();i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String active = object.getString("active");
                        String confirmed = object.getString("confirmed");
                        String death = object.getString("deaths");
                        String deltaconfirmed = object.getString("deltaconfirmed");
                        String lastUpdateTime = object.getString("lastupdatedtime");
                        String deltarecovered = object.getString("deltarecovered");
                        String recovered = object.getString("recovered");
                        String state = object.getString("state");

                        indiaModel = new IndiaModel(active,confirmed,death,deltaconfirmed,deltarecovered,deltarecovered,lastUpdateTime,recovered,state);
                        IndiaModelList.add(indiaModel);

                    }
                    indiaAdapter = new IndiaAdapter(IndiaActivity.this,IndiaModelList);
                    listView.setAdapter(indiaAdapter);
                    loader.stop();
                    loader.setVisibility(View.GONE);

                }
                catch (JSONException e)
                {

                    e.printStackTrace();
                    Toast.makeText(IndiaActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    loader.stop();
                    loader.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loader.stop();
                loader.setVisibility(View.GONE);
                Toast.makeText(IndiaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();



            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }



}
