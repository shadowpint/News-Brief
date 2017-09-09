package com.inshorts.newsbrief;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.gson.Gson;
import com.inshorts.newsbrief.oauth2.response.News;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neeraj on 19/03/17.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    static final String BOOKMARK_URL = "https://news-brief.herokuapp.com/news/api/news/";
    private String TAG = NewsAdapter.class.getSimpleName();
    private Context mContext;
    private List<News> newsList;
   public HashMap<String, String> ratingquery = new HashMap<String, String>();
    private Map<String, String> mp;
    private String jsrating;

    private String token;
    private Response response;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,publisher,category,bookmark,date;
        MaterialFavoriteButton materialFavoriteButtonNice;



        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            publisher= (TextView) view.findViewById(R.id.publisher);
            category = (TextView) view.findViewById(R.id.category);
            bookmark = (TextView) view.findViewById(R.id.bookmark);
            date = (TextView) view.findViewById(R.id.date);
            materialFavoriteButtonNice=(MaterialFavoriteButton)view.findViewById(R.id.favorite_nice);

        }
    }


    public NewsAdapter(Context mContext, List<News> newsList,String token) {
        this.mContext = mContext;
        this.newsList = newsList;
        this.token=token;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        mp = new HashMap<String, String>();
         final News news = newsList.get(position);
        holder.title.setText(news.getTitle());
        switch (news.getCategory()) {
            case "b":
                holder.category.setText("Business");
                break;
            case "t":
                holder.category.setText("Science and technology");
                break;
            case "e":
                holder.category.setText("Entertainment");
                break;
            default:
                holder.category.setText("Health");
                break;
        }
        holder.publisher.setText( "By- "+news.getPublisher());
        Calendar mydate = Calendar.getInstance();
        mydate.setTimeInMillis(Long.parseLong(news.getTimestamp()));

        holder.date.setText(mydate.get(Calendar.DAY_OF_MONTH)+"."+mydate.get(Calendar.MONTH)+"."+mydate.get(Calendar.YEAR) );
//        // loading news cover using Glide library
//        Glide.with(mContext).load(news.getUrl()).into(holder.thumbnail);



        holder.materialFavoriteButtonNice.setFavorite(false, false);
        holder.materialFavoriteButtonNice.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {

                        if (favorite) {
                            Log.e("id", news.getId());
                            mp.put("id",news.getId());
                            mp.put("title",news.getTitle());
                            mp.put("url",news.getUrl());
                            mp.put("publisher",news.getPublisher());
                            mp.put("category",news.getCategory());
                            mp.put("hostname",news.getHostname());
                            mp.put("timestamp",news.getTimestamp());
                            Gson gson = new Gson();
                            jsrating = gson.toJson(mp);
                            new GetRatingTask().execute(jsrating);
                        }
                    }
                });
        holder.materialFavoriteButtonNice.setOnFavoriteAnimationEndListener(
                new MaterialFavoriteButton.OnFavoriteAnimationEndListener() {
                    @Override
                    public void onAnimationEnd(MaterialFavoriteButton buttonView, boolean favorite) {
                        holder.bookmark.setText("Bookmarked");
                    }
                });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                Log.e(TAG, "True"+ news.getUrl());
                intent.putExtra("title", news.getTitle());
                intent.putExtra("url", news.getUrl());
                mContext.startActivity(intent);






            }
        });
  }
    class GetRatingTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */

        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String da = params[0];
            Log.e("token", da);
            if (token != null){
                try {
                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON
                            = MediaType.parse("application/json; charset=utf-8");


                    RequestBody body = RequestBody.create(JSON, da);
                    Request request = new Request.Builder()
                            .header("Content-Type", "application/json")
                            .header("Authorization", "Bearer " + token)
                            .url(BOOKMARK_URL)
                            .post(body)
                            .build();
                    response = client.newCall(request).execute();

                    return response.body().string();
                } catch (@NonNull IOException e) {
                    Log.e(TAG, "" + e.getLocalizedMessage());
                }
        }else{


                Intent intent = new Intent(mContext, LoginActivity.class);

                mContext.startActivity(intent);
            }

            return null;
        }


        @Override
        protected void onPostExecute(String Response) {
            super.onPostExecute(Response);

            Toast.makeText(mContext, "Bookmarked", Toast.LENGTH_LONG).show();








        }
    }
    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_news, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Rate", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
