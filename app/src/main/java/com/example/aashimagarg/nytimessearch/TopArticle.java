package com.example.aashimagarg.nytimessearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by aashimagarg on 6/24/16.
 */
public class TopArticle implements Serializable {

    String headLine;
    String webUrl;
    String thumbNail;

    public TopArticle(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("url");

            this.headLine = jsonObject.getString("title");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0){
                JSONObject multimediaJson = multimedia.getJSONObject(2);
                this.thumbNail = multimediaJson.getString("url");
            } else {
                this.thumbNail = "";
            }
        } catch (JSONException e){

        }
    }

    public static ArrayList<TopArticle> fromJSONArray(JSONArray array){
        ArrayList<TopArticle> results = new ArrayList<>();
        for (int x = 0; x < array.length(); x++){
            try {
                results.add(new TopArticle(array.getJSONObject(x)));

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getThumbNail() {
        return thumbNail;
    }

}

