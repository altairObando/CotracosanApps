package nacatamalitosoft.com.cotracosanapps.localDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static nacatamalitosoft.com.cotracosanapps.localDB.UserContract.UserEntry.*;


public class UserDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cotracosan.db";
    private static final int VERSION = 1;
    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ID + " TEXT NOT NULL,"
                + SOCIOID + " TEXT NOT NULL,"
                + USER + " TEXT NOT NULL,"
                + EMAIL + " TEXT NOT NULL,"
                + AVATAR + " TEXT NOT NULL,"
                + ROL + " TEXT NOT NULL, "
                + ISLOGGED + " TEXT, "
                + " UNIQUE (" + ID + "))");
    }

    public Cursor getLocalLoggedUser(){
        SQLiteDatabase db = getReadableDatabase();
        return db.query(
                TABLE_NAME,
                null,
                ISLOGGED + " LIKE ? ",
                new String [] {"1"},
                null,
                null,
                null
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ID + " TEXT NOT NULL,"
                + SOCIOID + " TEXT NOT NULL,"
                + USER + " TEXT NOT NULL,"
                + EMAIL + " TEXT NOT NULL,"
                + AVATAR + " TEXT NOT NULL,"
                + ROL + " TEXT NOT NULL, "
                + ISLOGGED + " TEXT, "
                + " UNIQUE (" + ID + "))");
    }

    public long AddNewUserLogged(User userResult) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(
                TABLE_NAME,
                null,
                userResult.toContentValues()
        );
    }

    public void CerrarSesion(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}
