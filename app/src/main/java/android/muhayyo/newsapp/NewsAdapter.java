package android.muhayyo.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    private List<News> newsList;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView articleSection;
        public TextView articleTitle;
        public TextView articleAuthor;
        public TextView articlePublishedDate;
        public TextView articleUrl;
        Context context;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            articleSection = itemView.findViewById(R.id.tv_section_name);
            articleTitle = itemView.findViewById(R.id.tv_article_title);
            articleAuthor = itemView.findViewById(R.id.tv_author_name);
            articlePublishedDate = itemView.findViewById(R.id.tv_published_date);
        }
    }

    public NewsAdapter(List<News> allNewsList) {
        newsList = allNewsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder(): called");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new NewsViewHolder(view);
    }

    /**
    * Format date string as "Jan 23, 2021"
    */
    private String formatDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        Date out = null;
        try {
            out = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat publishDate = new SimpleDateFormat("MMM dd, yyyy", Locale.UK);
        return publishDate.format(out);
    }


    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Log.d(LOG_TAG, "onBindViewHolder(): called.");

        News newsItem = newsList.get(position);
        /// Get web URL of the News object at given position
        String currentNews = newsItem.getWebUrl();

        // Set click listener on RecyclerView item to open a web browser to display selected news article.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Recycle Click" + position, Toast.LENGTH_SHORT).show();
                // Convert String URL into URI object
                Uri currentNewsUri = Uri.parse(currentNews);

                // Create an intent to view the news
                Intent newsIntent = new Intent(Intent.ACTION_VIEW, currentNewsUri);

                // Send implicit intent to launch a web browser to display the news
                view.getContext().startActivity(newsIntent);
            }
        });

        String articleDate = formatDate(newsItem.getPublishedDate());



        // Set texts to Recycler View item
        holder.articleSection.setText(newsItem.getSectionName());
        holder.articleTitle.setText(newsItem.getArticleTitle());
        holder.articleAuthor.setText(newsItem.getAuthorName());
        holder.articlePublishedDate.setText(articleDate);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


}
