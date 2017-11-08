package oliva.marc.com.booklisting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioButton;

public class SearchPreferencesActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private RadioButton mEsLanguageRb, mEnLanguageRb, mAllLanguageRb;

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
    }

    private void createSharedPreference() {
        sharedPreferences = getPreferences(this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("all", mAllLanguageRb.isChecked());
        editor.putBoolean("en", mEnLanguageRb.isChecked());
        editor.putBoolean("es", mEsLanguageRb.isChecked());
        editor.commit();

        if (mAllLanguageRb.isChecked()) {
            QueryUtils.language_preference = null;
        }
        if (mEnLanguageRb.isChecked()) {
            QueryUtils.language_preference = "en";
        }
        if (mEsLanguageRb.isChecked()) {
            QueryUtils.language_preference = "es";
        }
    }

    private void loadSharedPreference() {
        sharedPreferences = getPreferences(this.MODE_PRIVATE);
        Boolean valueAll = sharedPreferences.getBoolean("all", true);
        Boolean valueEn = sharedPreferences.getBoolean("en", false);
        Boolean valueEs = sharedPreferences.getBoolean("es", false);

        mAllLanguageRb.setChecked(valueAll);
        mEnLanguageRb.setChecked(valueEn);
        mEsLanguageRb.setChecked(valueEs);
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
