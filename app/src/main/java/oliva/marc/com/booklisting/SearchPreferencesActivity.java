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
        SharedPreferences preferences = getSharedPreferences("language", this.MODE_PRIVATE);
        mAllLanguageRb = findViewById(R.id.all_language_rb);
        mEnLanguageRb = findViewById(R.id.en_language_rb);
        mEsLanguageRb = findViewById(R.id.es_language_rb);
        mRelevanceRb = findViewById(R.id.order_by_relevance);
        mNewestRb = findViewById(R.id.order_by_newest);
        mMaxResultsEditText = findViewById(R.id.max_results_edit_text);
    }

    private void createSharedPreference() {
        sharedPreferences = getPreferences(this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //language radiogroup
        editor.putBoolean("all", mAllLanguageRb.isChecked());
        editor.putBoolean("en", mEnLanguageRb.isChecked());
        editor.putBoolean("es", mEsLanguageRb.isChecked());
        //max results edittext
        if(Integer.valueOf(mMaxResultsEditText.getText().toString())>40){
            editor.putInt("maxres",40);
            Toast.makeText(this, getString(R.string.error_max_result_msg), Toast.LENGTH_LONG).show();
        }else{
            editor.putInt("maxres",Integer.valueOf(mMaxResultsEditText.getText().toString()));
        }

        //order radiogroup
        editor.putBoolean("rel",mRelevanceRb.isChecked());
        editor.putBoolean("new",mNewestRb.isChecked());

        editor.commit();

        validatePreferences();
    }

    private void loadSharedPreference() {
        sharedPreferences = getPreferences(this.MODE_PRIVATE);
        Boolean valueAll = sharedPreferences.getBoolean("all", true);
        Boolean valueEn = sharedPreferences.getBoolean("en", false);
        Boolean valueEs = sharedPreferences.getBoolean("es", false);

        mAllLanguageRb.setChecked(valueAll);
        mEnLanguageRb.setChecked(valueEn);
        mEsLanguageRb.setChecked(valueEs);

        int valueMaxResults = sharedPreferences.getInt("maxres",40);

        mMaxResultsEditText.setText(String.valueOf(valueMaxResults));

        Boolean valueRelevance = sharedPreferences.getBoolean("rel",true);
        Boolean valueNewest = sharedPreferences.getBoolean("new",false);

        mRelevanceRb.setChecked(valueRelevance);
        mNewestRb.setChecked(valueNewest);

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
