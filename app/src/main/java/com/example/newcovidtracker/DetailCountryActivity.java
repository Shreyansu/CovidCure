package com.example.newcovidtracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class DetailCountryActivity extends AppCompatActivity {

    private int positionCountry;
    private TextView country,cases,todayCases,death,todayDeath,active,critical,recovered;

    SimpleArcLoader Arcloader;
    PieChart pieChart;
    ScrollView scrollStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_country);

        Arcloader = (SimpleArcLoader)findViewById(R.id.loader);
        pieChart = (PieChart)findViewById(R.id.pieChart);
        scrollStat = (ScrollView)findViewById(R.id.scrollStats);

        Intent intent  = getIntent();
        positionCountry = intent.getIntExtra("position",0);

        getSupportActionBar().setTitle("Data of " + CountryActivity.countryModelList.get(positionCountry).getCountry());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        country = (TextView)findViewById(R.id.zCountry);
        cases = (TextView)findViewById(R.id.zCases);
        todayCases = (TextView)findViewById(R.id.zTodayCases);
        todayDeath = (TextView)findViewById(R.id.ztodayDeath);
        death = (TextView)findViewById(R.id.zDeaths);
        active = (TextView)findViewById(R.id.zActive);
        critical = (TextView)findViewById(R.id.zCritical);
        recovered = (TextView)findViewById(R.id.zRecovered);

        country.setText(CountryActivity.countryModelList.get(positionCountry).getCountry());
        cases.setText(CountryActivity.countryModelList.get(positionCountry).getCases());
        todayCases.setText(CountryActivity.countryModelList.get(positionCountry).getTodayCases());
        todayDeath.setText(CountryActivity.countryModelList.get(positionCountry).getTodayDeath());
        death.setText(CountryActivity.countryModelList.get(positionCountry).getDeath());
        active.setText(CountryActivity.countryModelList.get(positionCountry).getActiveCases());
        critical.setText(CountryActivity.countryModelList.get(positionCountry).getCritical());
        recovered.setText(CountryActivity.countryModelList.get(positionCountry).getRecovered());

        pieChart.addPieSlice(new PieModel("Cases", Integer.parseInt(cases.getText().toString()), Color.parseColor("#E8D211")));
        pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recovered.getText().toString()), Color.parseColor("#0DC616")));
        pieChart.addPieSlice(new PieModel("Death", Integer.parseInt(death.getText().toString()), Color.parseColor("#DC1713")));
        pieChart.addPieSlice(new PieModel("Active Cases", Integer.parseInt(active.getText().toString()), Color.parseColor("#D22AEF")));
        pieChart.startAnimation();

        Arcloader.stop();
        Arcloader.setVisibility(View.GONE);
        scrollStat.setVisibility(View.VISIBLE);












    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
