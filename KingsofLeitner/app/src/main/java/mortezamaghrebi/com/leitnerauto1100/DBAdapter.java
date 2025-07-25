package mortezamaghrebi.com.leitnerauto1100;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBAdapter extends SQLiteOpenHelper {
    private static final String TAG = "DBAdapter" ;
    private static String DB_NAME = "DB1100.db";
    private static String DB_PATH = "";
    private static DBAdapter instance;

    // TODO: Setup your fields here:
    //Data Table
    public static final String key_id            = "id";
    public static final String key_word          = "word";
    public static final String key_day           = "day";
    public static final String key_persian       = "presian";
    public static final String key_definition    = "definition";
    public static final String key_pronounce     = "pronounce";
    public static final String key_review        = "review";
    public static final String key_lastheart     = "lastheart";
    public static final String key_started       = "started";
    public static final String key_finished      = "finished";
    public static final String key_sound         = "sound";
    public static final String key_example         = "example";
    public static final String key_examplefa         = "examplefa";
    public static final String key_image         = "image";


    public static final int DATABASE_VERSION=2;
    // TODO: Setup your field members here ( 0 = KEY_ROWID, 1= ...)
    public  static  final  int COL_id        = 0;
    public  static  final  int COL_word      = 1;
    public  static  final  int COL_day       = 2;
    public  static  final  int COL_persian   = 3;
    public  static  final  int COL_definition= 4;
    public  static  final  int COL_pronounce = 5;
    public  static  final  int COL_review    = 6;
    public  static  final  int COL_lastheart = 7;
    public  static  final  int COL_started   = 8;
    public  static  final  int COL_finished  = 9;
    public  static  final  int COL_sound     = 10;
    public  static  final  int COL_example     = 11;
    public  static  final  int COL_examplefa     = 12;
    public  static  final  int COL_image     = 2;


    public static  final  String[] ALL_KEYS = new String[] {key_id,key_word,key_day,key_persian,key_definition,key_pronounce,key_review,key_lastheart,key_started,key_finished,key_sound,key_example,key_examplefa};
    public static  final  String[] ALL_KEYS_IMAGE = new String[] {key_id,key_word,key_image};

    public static final String DATABASE_NAME = "DB1100";
    public static final String  DATABASE_TABLE = "words";
    public static final String  DATABASE_TABLE_IMAGE = "images";

    private static final String DATABASE_CREATE_SQL = "create table " + DATABASE_TABLE + " (" + key_id + " integer primary key autoincrement, "
            // TODO: place your fileds here
            + key_word      + " string not null, "
            + key_day       + " integer not null, "
            + key_persian   + " string not null, "
            + key_definition+ " string not null, "
            + key_pronounce + " string not null, "
            + key_review    + " string not null, "
            + key_lastheart + " integer not null, "
            + key_started   + " integer not null, "
            + key_finished  + " integer not null, "
            + key_sound     + " integer not null, "
            + key_example    + " string not null, "
            + key_examplefa    + " string not null"
            + ");";
    private static final String DATABASE_CREATE_SQL_IMAGE = "create table " + DATABASE_TABLE_IMAGE + " (" + key_id + " integer primary key autoincrement, "
            // TODO: place your fileds here
            + key_word      + " string not null, "
            + key_image       + " string not null"
            + ");";

    private final Context context ;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context context) throws IOException {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.context = context;
        myDBHelper = new DatabaseHelper(context);
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        DB_PATH = context.getDatabasePath(DB_NAME).getParent() + "/";
    }

    public static synchronized DBAdapter getInstance(Context context) {
        if (instance == null) {
            try {
                instance = new DBAdapter(context);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            instance.createDatabaseIfNeeded();
            instance.open();
        }
        return instance;
    }
   // public boolean isDatabaseValid() {
   //     SQLiteDatabase db = null;
   //     try {
   //         db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
   //         Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE, null);
   //         if (cursor != null) {
   //             cursor.moveToFirst();
   //             int count = cursor.getInt(0);
   //             cursor.close();
   //             return count > 0;
   //         }
   //         return false;
   //     } catch (Exception e) {
   //         Log.e("DBAdapter", "Database validation error: " + e.getMessage());
   //         return false;
   //     } finally {
   //         if (db != null) {
   //             db.close();
   //         }
   //     }
   // }

   public void createDatabaseIfNeeded() {
       File dbFile = new File(DB_PATH + DB_NAME);
       if (!dbFile.exists()) {
           this.getReadableDatabase(); // ایجاد مسیر databases
           this.close(); // دیتابیس خالی را ببند
           copyDatabase(); // کپی دیتابیس از assets
           Toast.makeText(context,"Database copied from assets.",Toast.LENGTH_SHORT).show();
           Log.d("DBAdapter", "Database copied from assets.");
       } else {
           Toast.makeText(context,"Database already exists.",Toast.LENGTH_SHORT).show();
           Log.d("DBAdapter", "Database already exists.");
       }
   }
    private void copyDatabase() {
        try {
            InputStream myInput = context.getAssets().open(DB_NAME);
            long size= myInput.available();
            File dbDir = new File(DB_PATH);
            if (!dbDir.exists()) dbDir.mkdirs();

            String outFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();

            File f = new File(outFileName);
            Toast.makeText(context,"DB copied, size = " + f.length() + " , "+ size,Toast.LENGTH_SHORT).show();

            Log.d("DBAdapter", "DB copied, size = " + f.length() + " bytes");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,"Error copy database: "+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }
    public DBAdapter open() {
        db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        //db = myDBHelper.getWritableDatabase();
        return  this;
    }
    public void close() { myDBHelper.close();}

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertWord(String word,int day,String persian,String definition,String pronounce,String sound,String example,String examplefa)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(key_word    ,word);
        initialValues.put(key_day ,day);
        initialValues.put(key_persian,persian);
        initialValues.put(key_definition ,definition);
        initialValues.put(key_pronounce,pronounce);
        initialValues.put(key_sound,sound);
        initialValues.put(key_example,example);
        initialValues.put(key_examplefa,examplefa);
        initialValues.put(key_review ,"");
        initialValues.put(key_lastheart,0);
        initialValues.put(key_started ,0);
        initialValues.put(key_finished,0);
        return db.insert(DATABASE_TABLE,null,initialValues);
    }
    public long insertWordImage(String word,String image)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(key_word,word);
        initialValues.put(key_image,image);
        return db.insert(DATABASE_TABLE_IMAGE,null,initialValues);
    }


    public boolean deleteWord(long RowId){
        String where = key_id + "=" + RowId;
        return db.delete(DATABASE_TABLE,where,null) !=0;
    }



    public void deleteAllWords()
    {
        Cursor c= getAllWords();
        long rowId = c.getColumnIndexOrThrow(key_id);
        if(c.moveToFirst()){
            do{
                deleteWord(c.getLong((int) rowId));
            }while (c.moveToNext());
        }
        c.close();
    }

    public Cursor getAllWords(){
        String where = null;
        Cursor c = db.query(true,DATABASE_TABLE,ALL_KEYS,
                where, null,null,null,null,null);
        if(c!=null){c.moveToFirst();}
        return c;
    }


    public Cursor getSearchInWords(String where){
        Cursor c = db.query(true,DATABASE_TABLE,ALL_KEYS,
                where, null,null,null,null,null);
        if(c!=null){c.moveToFirst();}
        return c;
    }

    public Cursor getWord(long rowId)
    {
        String where = key_id + "=" + rowId;
        Cursor c = db.query(true,DATABASE_TABLE,ALL_KEYS,
                where, null,null,null,null,null);
        if(c!=null){c.moveToFirst();}
        return c;
    }
    public Cursor getWord(String word)
    {
        String where = key_word + "= '" + word+"'";
        Cursor c = db.query(true,DATABASE_TABLE,ALL_KEYS,
                where, null,null,null,null,null);
        if(c!=null){c.moveToFirst();}
        return c;
    }
    public Cursor getWordImage(String word)
    {
        String where = key_word + "= '" + word+"'";
        Cursor c = db.query(true,DATABASE_TABLE_IMAGE,ALL_KEYS_IMAGE,
                where, null,null,null,null,null);
        if(c!=null){c.moveToFirst();}
        return c;
    }


    public Boolean updateWordRowFromBackup(String word,int day,String persian,String definition,String pronounce,String sound,String example,String examplefa)
    {
        String where = key_word + "='" + word+"'";
        ContentValues newValues = new ContentValues();
        newValues.put(key_day     ,day);
        newValues.put(key_persian     ,persian);
        newValues.put(key_definition  ,definition);
        newValues.put(key_pronounce  ,pronounce);
        newValues.put(key_sound  ,sound);
        newValues.put(key_example  ,example);
        newValues.put(key_examplefa,examplefa);
        return db.update(DATABASE_TABLE,newValues,where,null)!=0;
    }
    public Boolean updateWordImage(String word,String image)
    {
        String where = key_word + "='" + word+"'";
        ContentValues newValues = new ContentValues();
        newValues.put(key_image     ,image);
        return db.update(DATABASE_TABLE_IMAGE,newValues,where,null)!=0;
    }

    public Boolean updateWordRowFromSaved(String word,String review,int lastheart,int started,int finished)
    {
        String where = key_word + "='" + word+"'";
        ContentValues newValues = new ContentValues();
        newValues.put(key_review     ,review);
        newValues.put(key_lastheart ,lastheart);
        newValues.put(key_started,started);
        newValues.put(key_finished,finished);
        return db.update(DATABASE_TABLE,newValues,where,null)!=0;
    }

    public Boolean UpdateWordReview(long rowId,String review,int lastheart)
    {
        String where = key_id + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(key_review,review);
        newValues.put(key_lastheart,lastheart);
        return db.update(DATABASE_TABLE,newValues,where,null)!=0;
    }
    public Boolean UpdateWordStarted(long rowId,int started)
    {
        String where = key_id + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(key_started  ,started);
        return db.update(DATABASE_TABLE,newValues,where,null)!=0;
    }

    public Boolean UpdateWordFinished(long rowId,int finished)
    {
        String where = key_id + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(key_finished,finished);
        return db.update(DATABASE_TABLE,newValues,where,null)!=0;
    }

    public boolean UpdateOrInsert(wordItem word)
    {
        try {
            Cursor cursor = getWord(word.word);
            if (cursor.getCount() == 0) {
                insertWord(word.word, word.day, word.persian, word.definition, word.pronounce, word.sound,word.example,word.examplefa);
            } else
                updateWordRowFromBackup(word.word, word.day, word.persian, word.definition, word.pronounce, word.sound,word.example,word.examplefa);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }
    public boolean UpdateOrInsertWordImage(String word,String image)
    {
        try {
            Cursor cursor = getWordImage(word);
            if (cursor.getCount() == 0) {
                insertWordImage(word,image);
            } else
                updateWordImage(word,image);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }
    public boolean UpdateSaveItem(wordItem word)
    {
        try {
            Cursor cursor = getWord(word.word);
            if (cursor.getCount() == 0) {
            } else
                updateWordRowFromSaved(word.word, word.review, word.lastheart, word.started, word.finished);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context){super(context,DATABASE_NAME,null,DATABASE_VERSION);}
        public void onCreate(SQLiteDatabase _db){_db.execSQL(DATABASE_CREATE_SQL);_db.execSQL(DATABASE_CREATE_SQL_IMAGE);}
        public void onUpgrade(SQLiteDatabase _db, int oldVersion,int newVersion){
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_IMAGE);
            onCreate(_db);
        }
    }

}
