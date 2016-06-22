package com.example.aashimagarg.nytimessearch.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.aashimagarg.nytimessearch.Article;
import com.example.aashimagarg.nytimessearch.R;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //custom font on toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);

        // Get access to our TextView
        TextView txt = (TextView) findViewById(R.id.myTitle);
        // Create the TypeFace from the TTF asset
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/NYFont.ttf");
        // Assign the typeface to the view
        txt.setTypeface(font);

        Article article = (Article) getIntent().getSerializableExtra("article");
        WebView webView = (WebView) findViewById(R.id.wvArticle);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(article.getWebUrl());
    }




}
