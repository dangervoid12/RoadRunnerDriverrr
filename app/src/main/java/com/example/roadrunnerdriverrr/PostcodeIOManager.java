package com.example.roadrunnerdriverrr;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class PostcodeIOManager {
    static JSONParser parser = new JSONParser();
    public PostcodeIOManager(){

    }

    public static ArrayList<String> getPostcodeDetails(String postcode) {
        ArrayList<String> resList = new ArrayList<String>();
        String res = sendRequest("postcodes/" + postcode);
        parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(res);
            String result = (String) jsonObject.get("result").toString();
            JSONObject jsonObject2 = (JSONObject) parser.parse(result);
            resList = new ArrayList<String>();
            resList.add("Country: " + (String) jsonObject2.get("country"));
            resList.add("Admin Ward: " + (String) jsonObject2.get("admin_ward"));
            resList.add("CCG: " + (String) jsonObject2.get("ccg"));
            resList.add("Lat: " + jsonObject2.get("latitude").toString());
            resList.add("Lon: " + jsonObject2.get("longitude").toString());
            resList.add("Postcode: " + (String) jsonObject2.get("postcode"));
            resList.add("Electoral Reg: " + (String) jsonObject2.get("european_electoral_region"));
            resList.add("Parl. Constit.: " + (String) jsonObject2.get("parliamentary_constituency"));
            resList.add("Admin County: " + (String) jsonObject2.get("admin_county"));
            resList.add("Admin District: " + (String) jsonObject2.get("admin_district"));
            resList.add("Parish: " + (String) jsonObject2.get("parish"));
            resList.add("Outcode: " + (String) jsonObject2.get("outcode"));
            resList.add("Incode: " + (String) jsonObject2.get("incode"));
            resList.add("Nhs: " + (String) jsonObject2.get("nhs_ha"));
            resList.add("Region: " + (String) jsonObject2.get("region"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resList;
    }

    public static String getNearestPostcode(float locLong, float locLat) {
        return sendRequest("postcodes?lon=" + locLong + "&lat=" + locLat);
    }

    public static String getRandomPostcodeDetails(){
        return sendRequest("random/postcodes");
    }

    public static boolean validatePostcode(String postcode){
        boolean result = false;
        String req = sendRequest("postcodes/" + postcode + "/validate");

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(req);
            String tmp = jsonObject.get("result").toString();
            result = Boolean.parseBoolean(tmp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String autocompletePostcode(String partOfPostcode) throws ParseException {
        String req = sendRequest("postcodes/" + partOfPostcode + "/autocomplete");
        ArrayList<String> resArr = new ArrayList<String>();
        parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(req);
        String result = jsonObject.get("result").toString();
        return result;
    }

    private static String sendRequest(String req){
        String result = "";
        HttpGetRequest getRequest = new HttpGetRequest();
        String myUrl = "https://api.postcodes.io/" + req;
        try {
            result = getRequest.execute(myUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
