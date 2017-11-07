package oliva.marc.com.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private String NEW_BOOK_REQUEST_URL;

    private static final int BOOK_LOADER_ID = 1;

    private AdapterBookList adapterBookList;

    private LinearLayout searchProgressBar;
    private EditText edittextSearchBook;
    private ImageButton buttonSearchBook;
    private LinearLayout noConnection;
    private TextView resultTextView;
    private ImageView resultImageView;
    private ListView listView;

    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        init();

        //verify the internet connection
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if (isConnected == true) {
            listView.setEmptyView(searchProgressBar);
            noConnection.setVisibility(View.GONE);
            // Get a reference to the LoaderManager, in order to interact with loaders.
            loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        } else {
            listView.setEmptyView(noConnection);
            edittextSearchBook.setFocusable(false);
            searchProgressBar.setVisibility(View.GONE);
            resultTextView.setText(R.string.no_internet_connection);
            resultImageView.setImageResource(R.drawable.ic_cloud_off_black_48dp);
        }

        adapterBookList = new AdapterBookList(BookListActivity.this, new ArrayList<Book>());
        listView.setAdapter(adapterBookList);

        buttonSearchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookEditText = edittextSearchBook.getText().toString();
                if(!TextUtils.isEmpty(bookEditText)){
                    noConnection.setVisibility(View.GONE);
                    searchProgressBar.setVisibility(View.VISIBLE);

                    NEW_BOOK_REQUEST_URL = BOOK_REQUEST_URL + bookEditText;
                    //Loader reset
                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, BookListActivity.this);
                    NEW_BOOK_REQUEST_URL = null;
                }else{
                    Toast.makeText(BookListActivity.this, getString(R.string.search_edit_text_empty), Toast.LENGTH_SHORT).show();
                }
                
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book book = adapterBookList.getItem(position);
                String url = book.getmWebReader();
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                websiteIntent.setData(Uri.parse(url));
                startActivity(websiteIntent);
            }
        });

    }

    private void init() {
        listView = (ListView) findViewById(R.id.list_book);
        resultTextView = (TextView) findViewById(R.id.result_text_view);
        resultImageView = (ImageView) findViewById(R.id.result_image_view);
        noConnection = (LinearLayout) findViewById(R.id.no_connection_internet);
        searchProgressBar = (LinearLayout) findViewById(R.id.searching_progressbar);
        edittextSearchBook = (EditText) findViewById(R.id.search_book);
        buttonSearchBook = (ImageButton) findViewById(R.id.button_search_book);
    }

    //Loader methods
    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle bundle) {
        if (TextUtils.isEmpty(NEW_BOOK_REQUEST_URL)) {
            return new BookLoader(this, BOOK_REQUEST_URL);
        } else {
            return new BookLoader(this, NEW_BOOK_REQUEST_URL);
        }

    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        noConnection.setVisibility(View.GONE);
        searchProgressBar.setVisibility(View.GONE);

        adapterBookList.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.

        if (books != null && !books.isEmpty()) {
            adapterBookList.addAll(books);
        } else {
            listView.setEmptyView(noConnection);
            searchProgressBar.setVisibility(View.GONE);
            resultTextView.setText(R.string.no_books_found);
            resultImageView.setImageResource(R.drawable.ic_info_outline_black_48dp);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapterBookList.clear();
    }


}
