package com.example.roadrunnerdriverrr;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyDataManager {
    static boolean blackTheme = false;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    static String curLat = "";
    static String curLong = "";
    static String lastLat = "";
    static String lastLong = "";
    static String curPostcode = "";

    static String pound = "\u00a3";

    static float lastAdd = 0.0f;
    static boolean deleteLastMarker = false;


    static float total;
    static float totalNo;
    static ArrayList<String> postcodeArr = new ArrayList<String>();
    String listItem = "";
    static boolean widgetPerm = false;
    static int screenWidth = 0;
    static int screenHeight = 0;
    static boolean stickyEdges = true;
    static String deliveryConfirmationNumber = "0";
    static boolean autoGetPostcodeFromLoc = false;


    public void setBlackTheme(boolean i){ blackTheme = i;}
    public boolean getBlackTheme(){return blackTheme;}

    public void setLocationManager(LocationManager newLocMan){
        locationManager = newLocMan;
    }

    public String getCurPostcode(){ return curPostcode; }

    public String getPoundSymbol(){ return pound;}


    public void setTotalNo(float i){ totalNo = i;}
    public float getTotalNo(){ return totalNo;}

    public float getLastAdd(){ return lastAdd;}
    public void setLastAdd(float i){ lastAdd = i;}
    public boolean getDeleteLastMarker(){ return deleteLastMarker;}
    public void setDeleteLastMarker(boolean i){ deleteLastMarker = i;}

    public boolean getWidgetPerm(){ return widgetPerm;    }
    public int getScreenWidth(){ return screenWidth;}
    public void setScreenWidth(int i){ screenWidth = i;}
    public int getScreenHeight(){ return screenHeight;}
    public void setScreenHeight(int i){ screenHeight = i;}
    public void setWidgetPerm(boolean i){ widgetPerm = i;}
    public void setStickyEdges(boolean i){
        Log.v("mm","jjj");
    stickyEdges = i;}
    public boolean getStickyEdges(){ return stickyEdges;}
    public void setDeliveryConfirmationNumber(String i){ deliveryConfirmationNumber = i;}
    public String getDeliveryConfirmationNumber(){ return deliveryConfirmationNumber;}
    public void addToTotal(float i){
        total = total + i;
    }
    public void takeFromTotal(float i){
        total = total - i;
    }
    public float getTotal(){
        return total;
    }
    public void setTotal(float i){
        total = i;
    }
    public void setAutoGetPostcodeFromLoc(boolean i){ autoGetPostcodeFromLoc = i;}
    public boolean getAutoGetPostcodeFromLoc(){ return autoGetPostcodeFromLoc;}
    public String getListItem(){
        String result = "";
        int i = 0;
        while (i<postcodeArr.size()){
            result = result + (i+1) + ". " + postcodeArr.get(i) + " \n";
            i++;
        }
        return result;
    }
    public void setListItem(String i){ listItem = i;}


    public void addPostcode(String i){
        postcodeArr.add(i);
    }
    public void takePostcode(int pcNo){
        postcodeArr.remove(pcNo);
    }
    public String getPostcodeNo(int pcNo){
        return postcodeArr.get(pcNo);
    }
    public ArrayList<String> getPostcodeArr(){
        return postcodeArr;
    }

    public int sendDeliveryConfirmationSMS() {
        int result = -1;
        if(deliveryConfirmationNumber == "0"){
            result = 0;
        }else {
            try {
                String msg = "D";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(deliveryConfirmationNumber, null, msg, null, null);
                result = 1;
            } catch (Exception ex) {
                ex.printStackTrace();
                result = 0;
            }
        }
        return result;
    }

    public void addPostcodeString(String newPostcode){
        totalNo++;

        if(!newPostcode.contains("GL")){

            listItem = "GL" + newPostcode;
            newPostcode = listItem;
        }else {
            listItem = newPostcode;
        }

        float tmpTotal = getTotal();
        newPostcode = newPostcode.replaceAll("\\s",""); // to delete spaces

        int pc1 = Integer.parseInt(newPostcode.substring(2,3));
        int pc2 = Integer.parseInt(newPostcode.substring(3,4));
        if(pc1 == 1){
            //all just for pound
            //total++;
            addToTotal(1.0f);
        }else if(pc1 == 2){
            if(pc2 == 0){
                addToTotal(1.0f); // 1f
            }else if(pc2 == 2){
                addToTotal(2.0f);
            }else if(pc2 == 4){
                addToTotal(2.0f);
            }else if(pc2 == 5){
                addToTotal(1.0f);
            }else if(pc2 == 8){
                addToTotal(2.0f);
            }
        }else if(pc1 == 3){
            if(pc2 == 1){
                addToTotal(1.5f);
            }else if(pc2 == 3){
                addToTotal(1.5f);
            }else if(pc2 == 4){
                addToTotal(2.0f);
            }
        }else if(pc1 == 4){
            if(pc2 == 0){
                addToTotal(1.5f);
            }else if(pc2 == 3){
                addToTotal(1.5f);
            }else if(pc2 == 4){
                addToTotal(1.5f);
            }else if(pc2 == 5){
                addToTotal(1.5f);
            }else if(pc2 == 6){
                addToTotal(1.5f);
            }else if(pc2 == 8){
                addToTotal(2.0f);
            }

        }
        if(getTotal() != tmpTotal){
            //listItem = listItem + "    " + (getTotal() - tmpTotal);
            //tvList.append(listItem);
            lastAdd = getTotal() - tmpTotal;
            addPostcode(newPostcode);
        }
        Log.v("aa","listitem:" + listItem);
    }

    public void deleteLast(){
        postcodeArr.remove(postcodeArr.size()-1);
        takeFromTotal(lastAdd);
    }

    public void clearList(){
        totalNo = 0;
        total = 0;
        postcodeArr = null;
        postcodeArr = new ArrayList<String>();
    }



    public void addPostcodeFromCurLoc(){
        addPostcodeString(curPostcode);
    }

    private String getPostcodeFromCurLoc(){
        String result = curPostcode;
        String shortCurLat = curLat.substring(0,6);
        String shortCurLong = curLong.substring(0,6);
        String shortLastLat = "";
        String shortLastLong = "";
        if(lastLat.length()>3) {
            shortLastLat = lastLat.substring(0, 6);
            shortLastLong = lastLong.substring(0, 6);
        }
        //Log.v("aa" ,"sh:" + shortCurLat + " " + shortCurLong + " " + shortLastLat + " " + shortLastLong);
        if(!shortCurLat.equals(shortLastLat) | !shortCurLong.equals(shortLastLong)){
            //Log.v("aa","sending and recieving");
            HttpGetRequest getRequest = new HttpGetRequest();
            //String myUrl = "http://api.postcodes.io/postcodes?lon=-2.2886635&lat=51.7916635";
            String myUrl = "https://api.postcodes.io/postcodes?lon=" + curLong + "&lat=" + curLat;
            try {
                result = getRequest.execute(myUrl).get();
                //Log.v("aaa","result: " +result);
                int indStart = result.indexOf("post") + 11;
                int indEnd = result.indexOf("quality",indStart) - 3;
                //Log.v("aaa","inStart=" + indStart + " indEnd=" + indEnd);
                result = result.substring(indStart,indEnd);

                curPostcode = result;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        return result;
    }

    public void updateCurLoc(){


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLong = curLong;
                lastLat = curLat;
                curLong = "" + location.getLongitude();
                curLat = "" + location.getLatitude();

              //  Log.v("vv","lat=" + curLat + " long=" + curLong);
                getPostcodeFromCurLoc();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 5, 0, locationListener);



    }

    public MyDataManager(){

    }
}
