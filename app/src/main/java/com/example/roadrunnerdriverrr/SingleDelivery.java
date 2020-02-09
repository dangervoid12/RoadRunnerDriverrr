package com.example.roadrunnerdriverrr;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SingleDelivery {
    private String dateOfAdd, dateOfDelivery;
    private String address, postcode;
    private float reward;
    private boolean statusDelivered;

    public SingleDelivery(){

    }

    public SingleDelivery(String ads, String pcd){
        this.dateOfAdd = getCurDate();
        this.address = ads;
        this.postcode = pcd;
        this.reward = MyDataManager.getRewardFromPostcode(pcd);
    }

    public SingleDelivery(String dod, String ads, String pcd, float rwd){
        this.dateOfAdd = getCurDate();
        this.dateOfDelivery = dod;
        this.address = ads;
        this.postcode = pcd;
        this.reward = rwd;
    }

    public SingleDelivery(String doa, String dod, String ads, String pcd, float rwd){
        this.dateOfAdd = doa;
        this.dateOfDelivery = dod;
        this.address = ads;
        this.postcode = pcd;
        this.reward = rwd;
    }

    public void setDateOfAdd(String i){ this.dateOfAdd = i;}
    public String getDateOfAdd(){ return this.dateOfAdd;}

    public void setDateOfDelivery(String i){ this.dateOfDelivery = i;}
    public String getDateOfDelivery(){ return this.dateOfDelivery;}

    public void setAddress(String i){ this.address = i;}
    public String getAddress(){ return address;}

    public void setPostcode(String i){ this.postcode = i;}
    public String getPostcode(){ return postcode;}

    public void setReward(float newReward){ this.reward = newReward;}
    public float getReward(){ return reward;}

    public void setStatusDelivered(boolean i){ this.statusDelivered = i;}
    public boolean getStatusDelivered(){ return this.statusDelivered;}

    public void flipStatusDelivered(){
        this.statusDelivered = !this.statusDelivered;
    }


    public String getExportString(){
        String result = "";
        result = this.address + " " + this.postcode + " " + this.reward + " ";
        if(statusDelivered){
            result = result + "OK" + " " + this.getDateOfDelivery();
        }else {
            result = result + "NO";
        }
        return result;
    }

    public void setCurDateAsDelDate(){
        setDateOfDelivery(getCurDate());
    }

    private String getCurDate(){
        String result = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        result = dtf.format(now);
        return result;
    }
}
