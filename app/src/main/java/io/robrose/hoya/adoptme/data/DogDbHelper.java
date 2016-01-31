package io.robrose.hoya.adoptme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.robrose.hoya.adoptme.data.DogContract.DogEntry;
import io.robrose.hoya.adoptme.data.DogContract.ShelterEntry;
import io.robrose.hoya.adoptme.data.DogContract.SocialsEntry;

/**
 * Manages a local database for all the dog related data.
 * Borrowed from Udacity: https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/data/WeatherProvider.java
 * @author Rob Rose
 */
public class DogDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "adopt.db";

    public DogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_DOG_TABLE = "CREATE TABLE " + DogEntry.TABLE_NAME + " (" +
                DogEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DogEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DogEntry.COLUMN_ENTERED + " INTEGER NOT NULL, " +
                DogEntry.COLUMN_AGE + " INTEGER NOT NULL, " +
                DogEntry.COLUMN_BIO + " TEXT NOT NULL, " +
                DogEntry.COLUMN_SHELTER_KEY + " INTEGER NOT NULL, " +
                DogEntry.COLUMN_ALBUM + " TEXT NOT NULL, " +
                DogEntry.COLUMN_INTEREST + " TEXT NOT NULL, " +
                DogEntry.COLUMN_INTERNAL + " TEXT, " +
                // Set up the shelter column as a foreign key to location table.
                " FOREIGN KEY (" + DogEntry.COLUMN_SHELTER_KEY + ") REFERENCES " +
                ShelterEntry.TABLE_NAME + " (" + ShelterEntry._ID + ") " +
                " );";

        final String SQL_CREATE_SHELTER_TABLE = "CREATE TABLE " + ShelterEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ShelterEntry._ID + " INTEGER PRIMARY KEY," +

                // the ID of the location entry associated with this weather data
                ShelterEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ShelterEntry.COLUMN_ABOUT + " TEXT NOT NULL, " +
                ShelterEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                ShelterEntry.COLUMN_CITY + " TEXT NOT NULL," +

                ShelterEntry.COLUMN_LAT + " REAL NOT NULL, " +
                ShelterEntry.COLUMN_LONG + " REAL NOT NULL, " +

                ShelterEntry.COLUMN_SOCIALS_KEY + " INTEGER NOT NULL, " +

                " FOREIGN KEY (" + ShelterEntry.COLUMN_SOCIALS_KEY + ") REFERENCES " +
                SocialsEntry.TABLE_NAME + " (" + SocialsEntry._ID + ") " +

                // Make sure there's no duplicate shelters
                " UNIQUE (" + ShelterEntry.COLUMN_NAME + ", " +
                ShelterEntry.COLUMN_CITY + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_SOCIALS_TABLE = "CREATE TABLE " + ShelterEntry.TABLE_NAME + " (" +
                SocialsEntry._ID + " INTEGER PRIMARY KEY," +

                SocialsEntry.COLUMN_FACEBOOK + " TEXT, " +
                SocialsEntry.COLUMN_INSTAGRAM + " TEXT, " +
                SocialsEntry.COLUMN_TWITTER + " TEXT, " +
                SocialsEntry.COLUMN_SNAPCHAT + " TEXT, " +
                SocialsEntry.COLUMN_HASHTAG + " TEXT " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_SOCIALS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SHELTER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DogEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ShelterEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SocialsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}