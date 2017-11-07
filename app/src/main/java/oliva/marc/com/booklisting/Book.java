package oliva.marc.com.booklisting;

import android.graphics.Bitmap;

/**
 * Created by marc on 18/08/2017.
 */

public class Book {
    private String mTitle;
    private String mAutors;
    private String mDescription;
    private Bitmap mUrlImage;
    private String mWebReader;

    public Book(String title, String autors, String description, Bitmap urlimage, String webreader) {
        mTitle = title;
        mAutors = autors;
        mDescription = description;
        mUrlImage = urlimage;
        mWebReader = webreader;
    }


    public String getmAutors() {
        return mAutors;
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
