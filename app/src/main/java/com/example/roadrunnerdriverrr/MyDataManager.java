package com.example.roadrunnerdriverrr;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyDataManager {
    static Context appContext;

    static boolean blackTheme = false;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    static String curLat = "";
    static String curLong = "";
    static String lastLat = "";
    static String lastLong = "";
    static String curPostcode = "";
    static String curAddress;

    static String pound = "\u00a3";

    static float lastAdd = 0.0f;
    static boolean deleteLastMarker = false;


    static float total;
    static float totalNo;
    static ArrayList<SingleDelivery> deliveryArr = new ArrayList<SingleDelivery>();
    static String listItem = "";
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

    public String getCurAddress(){ return curAddress;}

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

    public void setListItem(String i){ listItem = i;}

    public void addDelivery(SingleDelivery newDel){ deliveryArr.add(newDel);}

    public void addDelivery(String address, String postcode){
        Log.v("aa","a:" + address + " p:" + postcode);

            SingleDelivery newDel = new SingleDelivery(deliveryArr.size() + 1, address, postcode);
            deliveryArr.add(newDel);

        //addToTotal(newDel.getReward()); //better to have control somewhere else
    }

    public void addDelivery(String address, String postcode, boolean isDelivered){
        Log.v("aa","a:" + address + " p:" + postcode);

        SingleDelivery newDel = new SingleDelivery(deliveryArr.size()+1, address, postcode);
        newDel.setCurDateAsDelDate();
        newDel.flipStatusDelivered();
        deliveryArr.add(newDel);
        addToTotal(newDel.getReward());
        Log.v("aa","tot:" + getTotal());
        //addToTotal(newDel.getReward()); //better to have control somewhere else
    }

    public void delDelivery(int no){
        takeFromTotal(deliveryArr.get(no).getReward());
        deliveryArr.remove(no);
    }

    public void clearDeliveries(){
        deliveryArr.clear();
        totalNo = 0;
        total = 0;

    }

    public ArrayList<SingleDelivery> getDelArr(){
        return deliveryArr;
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


    public static float getRewardFromPostcode(String newPostcode){
        float result = 0f;

        if(!newPostcode.contains("GL")){

            newPostcode = "GL" + newPostcode;

        }else {

        }
        newPostcode = newPostcode.replaceAll("\\s",""); // to delete spaces
        int pc1 = Integer.parseInt(newPostcode.substring(2,3));
        int pc2 = Integer.parseInt(newPostcode.substring(3,4));
        if(pc1 == 1){
            //all just for pound
            //total++;
            result = result + 1.0f;
        }else if(pc1 == 2){
            if(pc2 == 0){
                result = result + 1.0f; // 1f
            }else if(pc2 == 2){
                result = result + 2.0f;
            }else if(pc2 == 4){
                result = result + 2.0f;
            }else if(pc2 == 5){
                result = result + 1.0f;
            }else if(pc2 == 8){
                result = result + 2.0f;
            }
        }else if(pc1 == 3){
            if(pc2 == 1){
                result = result + 1.5f;
            }else if(pc2 == 3){
                result = result + 1.5f;
            }else if(pc2 == 4){
                result = result + 2.0f;
            }
        }else if(pc1 == 4){
            if(pc2 == 0){
                result = result + 1.5f;
            }else if(pc2 == 3){
                result = result + 1.5f;
            }else if(pc2 == 4){
                result = result + 1.5f;
            }else if(pc2 == 5){
                result = result + 1.5f;
            }else if(pc2 == 6){
                result = result + 1.5f;
            }else if(pc2 == 8){
                result = result + 2.0f;
            }

        }else {
            result = -1f;
        }
        return result;
    }

    public void updateAddressFromCurLoc(){
        curAddress = getAddressFromLoc(curLat, curLong);
    }

    public String getAddressFromLoc(String newLat, String newLong){
        String result = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this.appContext, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.valueOf(newLat), Double.valueOf(newLong), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            curPostcode = postalCode;
            String knownName = addresses.get(0).getFeatureName();
            result = address + " ";// + city + " " + state + " " + country + " " + postalCode + " " + knownName;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    public Context getAppContext(){
        return appContext;
    }

    private void writeFileContent(Uri uri, String textContent)
    {
        try{
            ParcelFileDescriptor pfd =
                    this.getAppContext().getContentResolver().
                            openFileDescriptor(uri, "w");

            FileOutputStream fileOutputStream =
                    new FileOutputStream(
                            pfd.getFileDescriptor());


            fileOutputStream.write(textContent.getBytes());

            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public String getCurDate(){
        String result = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy.MM.dd");
        LocalDateTime now = LocalDateTime.now();
        result = dtf.format(now);
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
                updateAddressFromCurLoc();
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

    public MyDataManager(Context appContext){
        this.appContext = appContext;
    }
    public MyDataManager(){
    }
}
