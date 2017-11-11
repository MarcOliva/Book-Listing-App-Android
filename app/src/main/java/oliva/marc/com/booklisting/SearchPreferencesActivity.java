package oliva.marc.com.booklisting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SearchPreferencesActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private RadioButton mEsLanguageRb, mEnLanguageRb, mAllLanguageRb, mRelevanceRb,mNewestRb;
    private EditText mMaxResultsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_preferences);
        setTitle(R.string.title_activity_search_preferences);
        init();
        loadSharedPreference();
    }

    private void init() {

        mAllLanguageRb = findViewById(R.id.all_language_rb);
        mEnLanguageRb = findViewById(R.id.en_language_rb);
        mEsLanguageRb = findViewById(R.id.es_language_rb);
        mRelevanceRb = findViewById(R.id.order_by_relevance);
        mNewestRb = findViewById(R.id.order_by_newest);
        mMaxResultsEditText = findViewById(R.id.max_results_edit_text);
    }

    private void createSharedPreference() {
        sharedPreferences = getPreferences(this.MODE_PRIVATE);
        SearchPreferences searchPreferences = new SearchPreferences(sharedPreferences);
        //language radiogroup
        searchPreferences.setAll(mAllLanguageRb.isChecked());
        searchPreferences.setEnglish(mEnLanguageRb.isChecked());
        searchPreferences.setSpanish( mEsLanguageRb.isChecked());
        //max results edittext
        if(Integer.valueOf(mMaxResultsEditText.getText().toString())>40){
            searchPreferences.setMaxResults(40);
            Toast.makeText(this, getString(R.string.error_max_result_msg), Toast.LENGTH_LONG).show();
        }else{
            searchPreferences.setMaxResults(Integer.valueOf(mMaxResultsEditText.getText().toString()));
        }
        //order radiogroup
        searchPreferences.setRelevance(mRelevanceRb.isChecked());
        searchPreferences.setNewest(mNewestRb.isChecked());
        validatePreferences();
    }

    private void loadSharedPreference() {
        sharedPreferences = getPreferences(this.MODE_PRIVATE);
        SearchPreferences searchPreferences = new SearchPreferences(sharedPreferences);

        mAllLanguageRb.setChecked(searchPreferences.getAll());
        mEnLanguageRb.setChecked(searchPreferences.getEnglish());
        mEsLanguageRb.setChecked(searchPreferences.getSpanish());

        mMaxResultsEditText.setText(String.valueOf(searchPreferences.getMaxResults()));

        mRelevanceRb.setChecked(searchPreferences.getRelevance());
        mNewestRb.setChecked(searchPreferences.getNewest());

        validatePreferences();
    }

    private void validatePreferences() {
        if (mAllLanguageRb.isChecked()) {
            QueryUtils.language_preference = null;
        }
        if (mEnLanguageRb.isChecked()) {
            QueryUtils.language_preference = "en";
        }
        if (mEsLanguageRb.isChecked()) {
            QueryUtils.language_preference = "es";
        }
        if(!TextUtils.isEmpty(mMaxResultsEditText.getText().toString())){
            if(Integer.valueOf(mMaxResultsEditText.getText().toString())>40){
                BookListActivity.max_result_preference = "40";
            }else{
                BookListActivity.max_result_preference = mMaxResultsEditText.getText().toString();
            }

        }
        if(mRelevanceRb.isChecked()){
            BookListActivity.order_by_preference = "relevance";
        }
        if(mNewestRb.isChecked()){
            BookListActivity.order_by_preference = "newest";
        }
    }

    @Override
    public void onBackPressed() {
        createSharedPreference();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                createSharedPreference();
                NavUtils.navigateUpFromSameTask(SearchPreferencesActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
