package android.muhayyo.newsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//import android.os.AsyncTask;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int NEWS_LOADER_ID = 7;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<News> myNews = new ArrayList<>();

    // URL for making API calls
    public static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?&show-tags=contributor&api-key=test";
    //https://content.guardianapis.com/search?&show-tags=contributor&q='science'&api-key=
    // Key
    private final String api_key = "21216dc1-7801-47a4-840b-9a1ab4f576a4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        LoaderManager loaderManager = getLoaderManager();

        // Find a reference to the RecyclerView in the layout
        recyclerView = findViewById(R.id.rv_news_list);
        layoutManager = new LinearLayoutManager(this);
        // Create a new adapter
        mAdapter = new NewsAdapter(myNews);
        recyclerView.setLayoutManager(layoutManager);
        // Set the adapter on the RecyclerView to populate the UI
        recyclerView.setAdapter(mAdapter);

        loaderManager.initLoader(NEWS_LOADER_ID, null, this);

//        DownloadNewsAsyncTask asyncTask = new DownloadNewsAsyncTask();
//        asyncTask.execute(GUARDIAN_REQUEST_URL);
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsArticleLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        // Clear the view
        recyclerView.removeAllViewsInLayout();
        mAdapter.notifyDataSetChanged();

        if (data != null && !data.isEmpty()) {
            myNews.addAll(data);
        }
        // Set the data to adapter
        mAdapter = new NewsAdapter(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
            // Clear the view
            recyclerView.removeAllViewsInLayout();
            mAdapter.notifyDataSetChanged();
    }

//    public class DownloadNewsAsyncTask extends AsyncTask<String, Void, List<News>> {
//        @Override
//        protected List<News> doInBackground(String... urls) {
//            // Check if the url's list is not empty and ant the first url is not null.
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//            // Request data for the first url in a list
//            List<News> result = Utils.fetchNewsData(urls[0]);
//            return result;
//        }


//        @Override
//        protected void onPostExecute(List<News> data) {
//            // Clear the view
//            recyclerView.removeAllViewsInLayout();
//            mAdapter.notifyDataSetChanged();
//
//            if (data != null && !data.isEmpty()) {
//                myNews.addAll(data);
//            }
//            // Set the data to adapter
//            mAdapter = new NewsAdapter(data);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // Click listener for menu item
    public boolean onOptionsItemSelected(MenuItem item) {
        // handling item selection
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // create intent
            Intent intent = new Intent(this, SettingsActivity.class);
            // start Settings activity
            this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets news articles in the background thread.
     */
    public class NewsArticleLoader extends AsyncTaskLoader<List<News>> {
        private final String LOG_TAG = NewsArticleLoader.class.getName();

        private String url;

        public NewsArticleLoader(@NonNull Context context, String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Nullable
        @Override
        public List<News> loadInBackground() {
            if (this.url == null){
                return null;
            }

            List<News> latestNews = Utils.fetchNewsData(this.url);
            return latestNews;
        }
    }
}