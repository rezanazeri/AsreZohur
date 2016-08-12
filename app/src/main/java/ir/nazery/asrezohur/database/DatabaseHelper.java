package ir.nazery.asrezohur.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/***
 * Created by reza on 95/3/2.
 ***/
public class DatabaseHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "asre_zohur.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }
}
