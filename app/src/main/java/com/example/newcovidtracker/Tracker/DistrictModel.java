package com.example.newcovidtracker.Tracker;

public class DistrictModel
{
    private String active ,confirmed,deceased,recovered,State;

    public DistrictModel()
    {

    }

    public DistrictModel(String active, String confirmed, String deceased, String recovered, String state) {
        this.active = active;
        this.confirmed = confirmed;
        this.deceased = deceased;
        this.recovered = recovered;
        State = state;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getDeceased() {
        return deceased;
    }

    public void setDeceased(String deceased) {
        this.deceased = deceased;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
