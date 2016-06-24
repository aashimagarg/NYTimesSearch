package com.example.aashimagarg.nytimessearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by aashimagarg on 6/24/16.
 */
public class TopArticleArrayAdapter extends RecyclerView.Adapter<TopArticleArrayAdapter.ViewHolder>  {

    private List<TopArticle> marticles;

    public TopArticleArrayAdapter(List<TopArticle> articles){
        this.marticles = articles;
    }

    /***** Creating OnItemClickListener *****/

    // Define listener member variable
    private static OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

   /* @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for current position
        Article article = this.getItem(position);

        //check to see if existing view is being reused or recycled
        //not using recycle -> inflate layout
        if (convertView == null){
           LayoutInflater inflater = LayoutInflater.from(getContext());
           convertView = inflater.inflate(R.layout.item_article_result, parent, false);
       }

        //find the image view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);

        //clear out recycled image from convertview from last time
        imageView.setImageResource(0);

        //populate headline
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadLine());

        //populate thumbnail image
        //remote download the image in the background
        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }

        return convertView;
    }*/

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvTitle;
        public ImageView ivImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            //initialize
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
        // Handles the row being being clicked

    }

    //inflating layout from XML and returning to holder
    @Override
    public TopArticleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TopArticleArrayAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        TopArticle article = marticles.get(position);

        // Set item views based on the data model
        TextView title = viewHolder.tvTitle;
        title.setText(article.getHeadLine());

        ImageView thumbnail = viewHolder.ivImage;
        thumbnail.setImageResource(0);

        String thumbnailUrl = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnailUrl)){
            Glide.with(thumbnail.getContext()).load(thumbnailUrl).into(thumbnail);
        } else {
            thumbnail.setImageResource(R.drawable.no_image);
        }

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return marticles.size();
    }

}

