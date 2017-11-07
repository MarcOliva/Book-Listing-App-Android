package oliva.marc.com.booklisting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * Created by marc on 18/08/2017.
 */

public final class QueryUtils {


    public static List<Book> fetchBookData(String requestUrl) {

        Log.i("FETCH", "START THE HTTP REQUEST");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Book Activity", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<Book> books = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Book}s
        return books;
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Book> extractFeatureFromJson(String bookJSON) {
        //if bookJson is null or empty , return null;
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject obj = new JSONObject(bookJSON);
            JSONArray items = obj.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject book = items.getJSONObject(i);
                JSONObject info = book.getJSONObject("volumeInfo");
                String title = reformatTitle(info.getString("title"));
                StringBuilder authors = new StringBuilder();
                JSONArray authorsArray = info.getJSONArray("authors");
                for (int j = 0; j < authorsArray.length(); j++) {
                    authors.append(authorsArray.getString(j));
                    if (authorsArray.length() != j + 1) {
                        authors.append(" , ");
                    }
                }

                String description = limitDescription(info.getString("description"));

                JSONObject linkimage = info.getJSONObject("imageLinks");

                Bitmap urlimage = getBitmapFromURL(linkimage.getString("thumbnail"));

                JSONObject accesInfo = book.getJSONObject("accessInfo");

                String webreader = accesInfo.getString("webReaderLink");

                books.add(new Book(title, authors.toString(), description, urlimage, webreader));

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }
        return books;
    }

    //Show the title with each word beginning with Uppercase
    private static String reformatTitle(String title) {

        title = title.toLowerCase();
        char[] caracteres = title.toCharArray();

        caracteres[0] = Character.toUpperCase(caracteres[0]);
        // 2 is to avoid an exception when falling from the fix
        for (int i = 0; i < title.length() - 2; i++)
            // Is 'word'
            if (caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ',')
                // Replace
                caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);

        return new String(caracteres);
    }

    //Method to remove the image of a url and convert it into Bitmap format
    private static Bitmap getBitmapFromURL(String url_image) {
        try {
            URL url = new URL(url_image);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap", "returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }

    // limit the amount of words that will be seen in the description
    private static String limitDescription(String description) {
        String result = description.substring(0, 60);
        return result + "...";
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("BookActivity", "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Book Activity", "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e("BookActivity", "Problem retrieving the book JSON results.", e);
        } finally {
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
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }

        }
        return output.toString();
    }

}
