package oliva.marc.com.booklisting;

import android.content.SharedPreferences;

/**
 * Created by ThinkSoft on 11/11/2017.
 */

public class SearchPreferences {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public SearchPreferences(SharedPreferences preferences){
        this.sharedPreferences = preferences;
        this.editor = this.sharedPreferences.edit();
    }

    public int getMaxResults(){
        return sharedPreferences.getInt("maxres",40);
    }
    public void setMaxResults(int value){
        editor.putInt("maxres",value);
        editor.commit();
    }

    public boolean getSpanish(){
        return sharedPreferences.getBoolean("es",false);
    }
    public void setSpanish(boolean value){
        editor.putBoolean("es",value);
        editor.commit();
    }
    public boolean getEnglish(){
        return sharedPreferences.getBoolean("en",false);
    }
    public void setEnglish(boolean value){
        editor.putBoolean("en",value);
        editor.commit();
    }
    public boolean getAll(){
        return sharedPreferences.getBoolean("all",true);
    }
    public void setAll(boolean value){
        editor.putBoolean("all",value);
        editor.commit();
    }

    public boolean getRelevance(){
        return sharedPreferences.getBoolean("rel",true);
    }
    public void setRelevance(boolean value){
        editor.putBoolean("rel",value);
    }

    public boolean getNewest(){
        return sharedPreferences.getBoolean("new",false);
    }
    public void setNewest(boolean value){
        editor.putBoolean("new",value);
    }



}
