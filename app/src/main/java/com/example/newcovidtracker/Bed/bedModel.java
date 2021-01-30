package com.example.newcovidtracker.Bed;

public class bedModel
{
    private String hosName,availbed,totalBed;
    private boolean expnaded;

    public bedModel(String hosName, String availbed, String totalBed) {
        this.hosName = hosName;
        this.availbed = availbed;
        this.totalBed = totalBed;

    }

    public String getHosName() {
        return hosName;
    }

    public void setHosName(String hosName) {
        this.hosName = hosName;
    }

    public String getAvailbed() {
        return availbed;
    }

    public void setAvailbed(String availbed) {
        this.availbed = availbed;
    }

    public String getTotalBed() {
        return totalBed;
    }

    public void setTotalBed(String totalBed) {
        this.totalBed = totalBed;
    }

    public boolean isExpnaded() {
        return expnaded;
    }

    public void setExpnaded(boolean expnaded) {
        this.expnaded = expnaded;
    }

    @Override
    public String toString() {
        return "bedModel{" +
                "hosName='" + hosName + '\'' +
                ", availbed='" + availbed + '\'' +
                ", totalBed='" + totalBed + '\'' +
                ", expnaded=" + expnaded +
                '}';
    }
}
