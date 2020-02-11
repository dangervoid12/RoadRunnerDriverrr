package com.example.roadrunnerdriverrr;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SingleDelivery {
    private int delId = -1;
    private String dateOfAdd, dateOfDelivery;
    private String address, postcode;
    private float reward;
    private boolean statusDelivered;
    private String alt = ";";

    public SingleDelivery(){

    }

    public SingleDelivery(int delId, String ads, String pcd){
        this.delId = delId;
        this.dateOfAdd = getCurDate();
        this.address = ads;
        this.postcode = pcd;
        this.reward = MyDataManager.getRewardFromPostcode(pcd);
    }

    public SingleDelivery(int delId, String dod, String ads, String pcd, float rwd){
        this.delId = delId;
        this.dateOfAdd = getCurDate();
        this.dateOfDelivery = dod;
        this.address = ads;
        this.postcode = pcd;
        this.reward = rwd;
    }

    public SingleDelivery(int delId, String doa, String dod, String ads, String pcd, float rwd){
        this.delId = delId;
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

    public String getSingleDeliveryString(){
        String result = "";
        result = delId + alt + dateOfAdd + alt + dateOfDelivery + alt + address + alt +
                postcode + alt + reward + alt + statusDelivered;
        return result;
    }

    public void setSingleDeliveryFromString(String decodeMe){
        String[] decStringArr = decodeMe.split(alt);
        this.dateOfAdd = decStringArr[0];
        this.dateOfDelivery = decStringArr[1];
        this.address = decStringArr[2];
        this.postcode = decStringArr[3];
        this.reward = Float.valueOf(decStringArr[4]);
        this.statusDelivered = Boolean.valueOf(decStringArr[5]);
    }

    public void setCurDateAsDelDate(){
        setDateOfDelivery(getCurDate());
    }

    public String getCurDate(){
        String result = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        result = dtf.format(now);
        return result;
    }
}
