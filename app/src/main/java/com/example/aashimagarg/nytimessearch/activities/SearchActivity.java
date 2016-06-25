package com.example.aashimagarg.nytimessearch.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.aashimagarg.nytimessearch.models.Article;
import com.example.aashimagarg.nytimessearch.models.ArticleArrayAdapter;
import com.example.aashimagarg.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.aashimagarg.nytimessearch.R;
import com.example.aashimagarg.nytimessearch.models.TopArticle;
import com.example.aashimagarg.nytimessearch.models.TopArticleArrayAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

//import com.example.aashimagarg.nytimessearch.FilterDialogFragment;

public class SearchActivity extends AppCompatActivity {

    int FILTER_REQUEST_CODE = 55;
    String query2;
    boolean scroll = false;
    RecyclerView rvArticles;
    String setDateString;
    String setOrderString;
    String setTopicString;
    ArrayList<Article> articles;
   // ArrayList<TopArticle> toparticles;
    ArticleArrayAdapter adapter;
   // TopArticleArrayAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (scroll == false) {
            loadOnStart();
        }
        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (scroll){
            rvArticles.setAdapter(adapter);
        }
        else {
            rvArticles.setAdapter(adapter2);
        }*/

    }

    public void setupViews(){
      //  gvResults = (GridView) findViewById(R.id.gvResults);
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        articles = new ArrayList<>();
        //toparticles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(articles);
        //adapter2 = new TopArticleArrayAdapter(toparticles);
        rvArticles.setAdapter(adapter);
        // Attach the layout manager to the recycler view
        StaggeredGridLayoutManager stagger = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setLayoutManager(stagger);


        //update views based on scroll
        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(stagger) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    loadMoreDataFromApi(page);

                }

        });


        //click on article
        adapter.setOnItemClickListener(new ArticleArrayAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                //create an intent to display the article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                //get the article to display
                //pass in that article into intent
                Article article = articles.get(position);
                i.putExtra("article", article);

                //launch the activity
                startActivity(i); // brings up the second activity
            }
        });


        //custom font on toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);

        // Get access to our TextView
        TextView txt = (TextView) findViewById(R.id.myTitle);
        // Create the TypeFace from the TTF asset
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/NYFont.ttf");
        // Assign the typeface to the view
        txt.setTypeface(font);

    }


    public void loadOnStart() {
      //  scroll = false;
        //Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/topstories/v2/home.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "ce2566fc302146cf8a2d5b84c6baf33a");

        Log.d("searchactivity", url + "?" + params);
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONArray("results");
                    articles.clear();
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }


    public void loadMoreDataFromApi(int page) {

        //Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        //makes endless scroll functional
        if (page == 0){
            articles.clear();
            adapter.notifyDataSetChanged();
        }

        RequestParams params = new RequestParams();
        params.put("api-key", "ce2566fc302146cf8a2d5b84c6baf33a");
        params.put("page", page);
        if (query2 != null) {
            params.put("q", query2);
        }
        if (setDateString != null){
            params.put("begin_date", setDateString);
        }
        if (setOrderString != null ){
            params.put("sort", setOrderString);
        }
        if (setTopicString != null){
            params.put("fq", setTopicString);
        }

        Log.d("searchactivity", url + "?" + params);
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_search, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);

            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

            //change color of search features

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                   // scroll = true;

                    //make query global
                    query2 = query;

                    loadMoreDataFromApi(0);

                    //Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
                    /*AsyncHttpClient client = new AsyncHttpClient();
                    String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

                    RequestParams params = new RequestParams();
                    params.put("api-key", "ce2566fc302146cf8a2d5b84c6baf33a");
                    params.put("page", 0);
                    params.put("q", query);
                    if (!setDateString.isEmpty()){
                        params.put("begin_date", setDateString);
                    }
                    if (!setOrderString.isEmpty()){
                        params.put("sort", setOrderString);
                    }
                    if (!setTopicString.isEmpty()){
                        params.put("fq", setTopicString);
                    }


                    client.get(url, params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("DEBUG", response.toString());
                            JSONArray articleJsonResults = null;

                            //auto-scroll up
                            rvArticles.smoothScrollToPosition(0);

                            try {
                                articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                                toparticles.clear();
                                adapter2.notifyDataSetChanged();
                                articles.clear();
                                articles.addAll(Article.fromJSONArray(articleJsonResults));
                                adapter.notifyDataSetChanged();
                                Log.d("DEBUG", articles.toString());
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });*/
                    // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                    // see https://code.google.com/p/android/issues/detail?id=24599
                    searchView.clearFocus();
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText){
                    return false;
                }
            });

            return super.onCreateOptionsMenu(menu);
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_filter:
                Intent i = new Intent(getApplicationContext(), FiltersActivity.class);
                startActivityForResult(i, FILTER_REQUEST_CODE); // brings up the second activity
                //showFragmentDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == FILTER_REQUEST_CODE) {
            setDateString = data.getExtras().getString("date");
            setOrderString = data.getExtras().getString("order");
            setTopicString = data.getExtras().getString("topics");
            loadMoreDataFromApi(0);
        }
    }


    /*public void onArticleSearch(View view){
        String query = etQuery.getText().toString();
        //Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "ce2566fc302146cf8a2d5b84c6baf33a");
        params.put("page", 0);
        params.put("q", query);

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.clear();
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }*/

    /*private void showFragmentDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance("Some Title");
        filterDialogFragment.show(fm, "fragment_filter_search");
    }*/

}

