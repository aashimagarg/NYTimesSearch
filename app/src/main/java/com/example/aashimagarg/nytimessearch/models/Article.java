package com.example.aashimagarg.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by aashimagarg on 6/20/16.
 */
public class Article implements Serializable {

    String headLine;
    String webUrl;
    String thumbNail;

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");

            JSONObject headline = jsonObject.getJSONObject("headline");
            this.headLine = headline.getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                this.thumbNail = "";
            }
        } catch (JSONException e){

        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> results = new ArrayList<>();
        for (int x = 0; x < array.length(); x++){
            try {
                results.add(new Article(array.getJSONObject(x)));

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
