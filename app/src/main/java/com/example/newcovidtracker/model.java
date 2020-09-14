package com.example.newcovidtracker;

public class model
{
    private String flag, country, activeCases, recovered, death, todayDeath, todayCases, critical, cases;

    public model()
    {

    }

    public model(String flag, String country, String activeCases, String recovered, String death, String todayDeath, String todayCases, String critical, String cases) {
        this.flag = flag;
        this.country = country;
        this.activeCases = activeCases;
        this.recovered = recovered;
        this.death = death;
        this.todayDeath = todayDeath;
        this.todayCases = todayCases;
        this.critical = critical;
        this.cases = cases;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getActiveCases() {
        return activeCases;
    }

    public void setActiveCases(String activeCases) {
        this.activeCases = activeCases;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public String getTodayDeath() {
        return todayDeath;
    }

    public void setTodayDeath(String todayDeath) {
        this.todayDeath = todayDeath;
    }

    public String getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    public String getCritical() {
        return critical;
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }
}