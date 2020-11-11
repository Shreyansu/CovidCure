package com.example.newcovidtracker.Tracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newcovidtracker.R;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class DetailIndiaActivity extends AppCompatActivity
{
    TextView aconfirmed,aRecovered,aActive,adeath,adeltaConfirmed,aState,alastupdatedTime,adeltadeath,adeltarecovered;
    SimpleArcLoader Arcloader;
    PieChart pieChart;
    ScrollView scrollStat;
    private int positionIndia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_india);

        Intent intent  = getIntent();
        positionIndia = intent.getIntExtra("position",0);

        aconfirmed = (TextView)findViewById(R.id.aConfirmed);
        aRecovered = (TextView)findViewById(R.id.arecovered);
        aActive = (TextView)findViewById(R.id.aActive);
        adeath = (TextView)findViewById(R.id.aDeath);
        adeltaConfirmed = (TextView)findViewById(R.id.aDeltaConfirmed);
        aState = (TextView)findViewById(R.id.aState);
        alastupdatedTime = (TextView)findViewById(R.id.aLastUpdatedTime);
        adeltadeath = (TextView)findViewById(R.id.aDeltaDeath);
        adeltarecovered = (TextView)findViewById(R.id.aDeltaRecovered);



        Arcloader = (SimpleArcLoader)findViewById(R.id.loader);
        pieChart = (PieChart)findViewById(R.id.pieChart);
        scrollStat = (ScrollView)findViewById(R.id.scrollStats);


        aState.setText(IndiaActivity.IndiaModelList.get(positionIndia).getState());
        aconfirmed.setText(IndiaActivity.IndiaModelList.get(positionIndia).getConfirmed());
        aRecovered.setText(IndiaActivity.IndiaModelList.get(positionIndia).getRecovered());
        aActive.setText(IndiaActivity.IndiaModelList.get(positionIndia).getActive());
        adeath.setText(IndiaActivity.IndiaModelList.get(positionIndia).getDeaths());
        adeltaConfirmed.setText(IndiaActivity.IndiaModelList.get(positionIndia).getDeltaConfirmed());
        alastupdatedTime.setText(IndiaActivity.IndiaModelList.get(positionIndia).getLastUpdatedTime());
        adeltadeath.setText(IndiaActivity.IndiaModelList.get(positionIndia).getDeltaDeaths());
        adeltarecovered.setText(IndiaActivity.IndiaModelList.get(positionIndia).getDeltaRecovered());



        pieChart.addPieSlice(new PieModel("Confirmed", Integer.parseInt(aconfirmed.getText().toString()), Color.parseColor("#E8D211")));
        pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(aRecovered.getText().toString()), Color.parseColor("#0DC616")));
        pieChart.addPieSlice(new PieModel("Death", Integer.parseInt(adeath.getText().toString()), Color.parseColor("#DC1713")));
        pieChart.addPieSlice(new PieModel("Active Cases", Integer.parseInt(aActive.getText().toString()), Color.parseColor("#D22AEF")));
        pieChart.startAnimation();

        Arcloader.stop();
        Arcloader.setVisibility(View.GONE);
        scrollStat.setVisibility(View.VISIBLE);









    }


    public void goTrackDistrict(View view)
    {
        Intent district = new Intent(getApplicationContext(),DistrictActivity.class);
        startActivity(district);
    }
}
