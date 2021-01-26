package android.muhayyo.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();

    private Utils() {
    }

    /**
     * Query Guardian's dataset, return a list of News objects.
     *
     * @param requestUrl String
     * @return List of News objects
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL
        URL newsUrl = createUrl(requestUrl);

        // Perform http call to the URL to receive JSON response.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(newsUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "fetchNewsData(): Problem making HTTP request", e);
        }

        // Extract relevant data
        List<News> mynews = extractNews(jsonResponse);
        // Return created list of News objects
        return mynews;
    }

    /**
     * Creates new URL from input string URL
     */
    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "createUrl(): Problem building URL", e);
        }
        return url;
    }

    /**
     * Makes HTTP request to Guardian API. Returns JSON response as a string.
     *
     * @param url URL input as a string
     * @return String       JSON response
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // Check if url is null value
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        // Create connection to host server
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If response code is equal to success code, read input stream
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "makeHttpRequest(): Error code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "makHttpRequest(): Problem retrieving news JSON data.", e);
        } finally {
            // Stop the network call connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Function converts server response data into a String object.
     *
     * @param  inputStream
     * @return String
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                result.append(line);
                line = bufferedReader.readLine();
            }
        }
        return result.toString();
    }


    /**
     * Extracts required data from parsed JSON response and returns a list of News objects
     *
     * @param newsJSON
     * @return List array of News objects
     */
    private static List<News> extractNews(String newsJSON) {
        String section;
        String title;
        String author;
        String date;
        String articleUrl;

        // Check if JSON is empty
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Array for holding News objects
        List<News> myNews = new ArrayList<>();

        // Try to parse JSON response
        try {
            // JSON object from JSON response
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject baseJsonResponseResult = baseJsonResponse.getJSONObject("response");

            // Get JSONArray of news from "results" key string
            JSONArray jsonArrayResults = baseJsonResponseResult.getJSONArray("results");
            author = null;
            // Make news object from each news in currentNewsArticles array
            for (int i = 0; i < jsonArrayResults.length(); i++) {
                JSONObject currentArticle = jsonArrayResults.getJSONObject(i);
                Log.v(LOG_TAG, "Got the currentArticles: ");
                // Extract values from JSONObject
                section = currentArticle.getString("sectionName");
                title = currentArticle.getString("webTitle");
                date = currentArticle.getString("webPublicationDate");
                articleUrl = currentArticle.getString("webUrl");

                // Inner loop to get author name from a key called "tags"
                JSONArray jsonArrayTags = currentArticle.getJSONArray("tags");
                for (int j = 0; j < jsonArrayTags.length(); j++) {
                    if (jsonArrayTags.length() >= 1) {
                        JSONObject tag = (JSONObject) jsonArrayTags.get(0);
                        author = tag.getString("webTitle");
                    } else {
                        author = "";
                    }
                }
                News news = new News(section, title, author, date, articleUrl);
                myNews.add(news);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "extractNews(): Problem parsing results", e);
            e.printStackTrace();
        }
        // Return a list of News objects
        return myNews;
    }
}
