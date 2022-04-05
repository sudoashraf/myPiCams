package in.ashrafsiddiqui.picams;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "PI_CAMS.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "my_picams";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ALIAS = "alias";
    private static final String COLUMN_IP = "ip";
    private static final String COLUMN_WEBPORT = "web_port";
    private static final String COLUMN_SSHPORT = "ssh_port";
    private static final String COLUMN_UNAME = "uname";
    private static final String COLUMN_PASS = "pass";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ALIAS + " TEXT, " +
                COLUMN_IP + " TEXT, " +
                COLUMN_WEBPORT + " TEXT, " +
                COLUMN_SSHPORT + " TEXT, " +
                COLUMN_UNAME + " TEXT, " +
                COLUMN_PASS + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    void addPiCam(String alias, String ip, String web_port, String ssh_port, String uname, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ALIAS, alias);
        cv.put(COLUMN_IP, ip);
        cv.put(COLUMN_WEBPORT, web_port);
        cv.put(COLUMN_SSHPORT, ssh_port);
        cv.put(COLUMN_UNAME, uname);
        cv.put(COLUMN_PASS, pass);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Unable to add", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String alias, String ip, String web_port, String ssh_port, String uname, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALIAS, alias);
        cv.put(COLUMN_IP, ip);
        cv.put(COLUMN_WEBPORT, web_port);
        cv.put(COLUMN_SSHPORT, ssh_port);
        cv.put(COLUMN_UNAME, uname);
        cv.put(COLUMN_PASS, pass);

        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to update.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully updated.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Unable to Delete!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
