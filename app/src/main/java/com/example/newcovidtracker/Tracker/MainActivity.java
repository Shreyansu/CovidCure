package com.example.newcovidtracker.Tracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newcovidtracker.R;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tvCases,tvRecovered,tvActive,tvdeath,tvTodayCases,tvAffectedCountry,tvCritical,tvTodayDeath;
    SimpleArcLoader Arcloader;
    PieChart pieChart;
    ScrollView scrollStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCases = (TextView)findViewById(R.id.tvCases);
        tvRecovered = (TextView)findViewById(R.id.tvrecovered);
        tvActive = (TextView)findViewById(R.id.tvActive);
        tvdeath = (TextView)findViewById(R.id.tvDeath);
        tvTodayCases = (TextView)findViewById(R.id.tvTodayCase);
        tvAffectedCountry = (TextView)findViewById(R.id.tvaffectedCountry);
        tvCritical = (TextView)findViewById(R.id.tvCritical);
        tvTodayDeath = (TextView)findViewById(R.id.tvtodayDeath);

        Arcloader = (SimpleArcLoader)findViewById(R.id.loader);
        pieChart = (PieChart)findViewById(R.id.pieChart);
        scrollStat = (ScrollView)findViewById(R.id.scrollStats);
        
        fetchData();
    }

    private void fetchData()
    {
        String url = "https://corona.lmao.ninja/v2/all/";

        Arcloader.start();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    tvCases.setText(jsonObject.getString("cases"));
                    tvRecovered.setText(jsonObject.getString("recovered"));
                    tvActive.setText(jsonObject.getString("active"));
                    tvdeath.setText(jsonObject.getString("deaths"));
                    tvTodayCases.setText(jsonObject.getString("todayCases"));
                    tvAffectedCountry.setText(jsonObject.getString("affectedCountries"));
                    tvCritical.setText(jsonObject.getString("critical"));
                    tvTodayDeath.setText(jsonObject.getString("todayDeaths"));


                    pieChart.addPieSlice(new PieModel("Cases", Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#E8D211")));
                    pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#0DC616")));
                    pieChart.addPieSlice(new PieModel("Death", Integer.parseInt(tvdeath.getText().toString()), Color.parseColor("#DC1713")));
                    pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#D22AEF")));
                    pieChart.startAnimation();

                    Arcloader.stop();
                    Arcloader.setVisibility(View.GONE);
                    scrollStat.setVisibility(View.VISIBLE);




                } catch (JSONException e)
                {
                    e.printStackTrace();
                    Arcloader.stop();
                    Arcloader.setVisibility(View.GONE);
                    scrollStat.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Arcloader.stop();
                Arcloader.setVisibility(View.GONE);
                scrollStat.setVisibility(View.VISIBLE);

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public void goTrackCountries(View view)
    {
        Intent intent = new Intent(getApplicationContext(), CountryActivity.class);
        startActivity(intent);

    }


    public void goTrackIndia(View view)
    {
        Intent indiaIntent = new Intent(getApplicationContext(), IndiaActivity.class);
        startActivity(indiaIntent);
    }
}
