package examples.csci567.lecture11;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by bryandixon on 2/26/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String dbname = "test.db";
    private static final int version = 1;
    private final String EXAMPLE_TABLE = "exampleTable";

    public DBHelper(Context context){
        super(context,dbname,null,version);
    }
    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + EXAMPLE_TABLE + " (text VARCHAR);");
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertText(String text){
        try{
            SQLiteDatabase qdb = this.getWritableDatabase();
            Log.d("DB Insert: ", "INSERT OR REPLACE INTO " +
                    EXAMPLE_TABLE + " (text) Values (" + text + ");");
            qdb.execSQL("INSERT OR REPLACE INTO " +
                    EXAMPLE_TABLE + " (text) Values (\""+ text + "\");");
            qdb.close();
        }
        catch(SQLiteException se){
            Log.d("DB Insert Error: ",se.toString());
            return false;
        }
        return true;
    }



    public String getText(){
        String toReturn = "";
        try{
            SQLiteDatabase qdb = this.getReadableDatabase();
            qdb.execSQL("CREATE TABLE IF NOT EXISTS " + EXAMPLE_TABLE + " (text VARCHAR);");
            Cursor c = qdb.rawQuery("SELECT * FROM " +
                    EXAMPLE_TABLE, null);
            if (c != null ) {
                if (c.moveToFirst()) {
                    do {
                        String text = c.getString(c.getColumnIndex("text"));
                        toReturn += text + "\n";
                    }
                    while (c.moveToNext());
                }
            }
            qdb.close();
        }
        catch(SQLiteException se){
            Log.d("DB Select Error: ",se.toString());
            return "";
        }
        return toReturn;
    }
}
