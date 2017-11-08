package oliva.marc.com.booklisting;

import android.graphics.Bitmap;

/**
 * Created by marc on 18/08/2017.
 */

public class Book {
    private String mTitle;
    private String mAuthors;
    private String mDescription;
    private Bitmap mUrlImage;
    private String mWebReader;

    public Book(String title, String autors, String description, Bitmap urlimage, String webreader) {
        mTitle = title;
        mAuthors = autors;
        mDescription = description;
        mUrlImage = urlimage;
        mWebReader = webreader;
    }


    public String getmAutors() {
        return mAuthors;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmTitle() {
        return mTitle;
    }

    public Bitmap getmUrlImage() {
        return mUrlImage;
    }

    public String getmWebReader() {
        return mWebReader;
    }
}
