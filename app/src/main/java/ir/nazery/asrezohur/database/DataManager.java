package ir.nazery.asrezohur.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/***
 * Created by reza on 95/3/2.
 ***/
public class DataManager {

    private Context context;
    DatabaseHelper databaseHelper;

    public DataManager(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public List<String> getIndexList() {
        if (databaseHelper == null) {
            throw new ExceptionInInitializerError("call initDB method first");
        }

        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String tableName = "dastanha";
//        String[] columns = {"onvan", "matn", "nevisande", "tasvir", "fav", "khande", "ezafi"};
        String[] columns = {"onvan"};
        Cursor cursor = database.query(tableName, columns, null, null, null, null, null);

        List<String> list = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(columns[0])));
            } while (cursor.moveToNext());
            cursor.close();
        }

        database.close();
        return list;
    }

    public int getIdForIndexPosition(int position) {
        if (databaseHelper == null) {
            throw new ExceptionInInitializerError("call initDB method first");
        }

        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String tableName = "dastanha";
        String[] columns = {"id"};
        Cursor cursor = database.query(tableName, columns, null, null, null, null, null);
        int i = 0;
        int id = -1;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (i++ == position) {
                    id = cursor.getInt(cursor.getColumnIndex(columns[0]));
                    break;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        database.close();
        return id;
    }

    public String getTextForID(Integer id) {
        if (databaseHelper == null) {
            throw new ExceptionInInitializerError("call initDB method first");
        }

        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String tableName = "dastanha";
//        String[] columns = {"onvan", "matn", "nevisande", "tasvir", "fav", "khande", "ezafi"};
        String[] columns = {"matn"};
        String where = "id = ?";
        String[] whereArgs = {id.toString()};
        Cursor cursor = database.query(tableName, columns, where, whereArgs, null, null, null);
        String text = "";

        if (cursor != null && cursor.moveToFirst()) {
            text = cursor.getString(cursor.getColumnIndex(columns[0]));
            cursor.close();
        }

        database.close();
        return text;
    }
}
