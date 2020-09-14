package com.example.newcovidtracker;

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
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryActivity extends AppCompatActivity {
    EditText Search;
    ListView listView;
    SimpleArcLoader loader;

    public static List<model> countryModelList = new ArrayList<>();
    model countryModel;
    myAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        listView =findViewById(R.id.listView);
        loader  = findViewById(R.id.loader);
        Search = findViewById(R.id.search);
        getSupportActionBar().setTitle("Track Countries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        fetchData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                startActivity(new Intent(getApplicationContext(),DetailCountryActivity.class).putExtra("position",position));

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
                adapter.getFilter().filter(s);
                adapter.notifyDataSetChanged();


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
        String url = "https://corona.lmao.ninja/v2/countries/";

        loader.start();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {

                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String countryName = jsonObject.getString("country");
                        String activeCases = jsonObject.getString("active");
                        String todayCases = jsonObject.getString("todayCases");
                        String death = jsonObject.getString("deaths");
                        String todayDeath = jsonObject.getString("todayDeaths");
                        String recovered = jsonObject.getString("recovered");
                        String critical = jsonObject.getString("critical");
                        String cases = jsonObject.getString("cases");

                        JSONObject object = jsonObject.getJSONObject("countryInfo");
                        String Flag = object.getString("flag");


                        countryModel = new model(Flag,countryName,activeCases,recovered,death,todayDeath,todayCases,critical,cases);
                        countryModelList.add(countryModel);

                    }

                    adapter = new myAdapter(CountryActivity.this,countryModelList);
                    listView.setAdapter(adapter);
                    loader.stop();
                    loader.setVisibility(View.GONE);

                 }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(CountryActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CountryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();



            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

}
