package io.robrose.hoya.adoptme.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DogProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DogDbHelper mOpenHelper;

    static final int DOG = 100;
    static final int DOG_FROM_ID = 101;
    static final int SHELTER = 200;
    static final int SHELTER_FROM_DOG = 201;
    static final int SHELTER_FROM_CITY_NAME = 202;
    static final int SOCIALS = 300;
    static final int SOCIALS_FROM_SHELTER_ID = 301;

    private static final SQLiteQueryBuilder sSocialsFromShelterIdBuilder;
    private static final SQLiteQueryBuilder sShelterFromDogIdBuilder;

    static{
        sSocialsFromShelterIdBuilder = new SQLiteQueryBuilder();
        sShelterFromDogIdBuilder = new SQLiteQueryBuilder();

        sSocialsFromShelterIdBuilder.setTables(
                DogContract.ShelterEntry.TABLE_NAME + " INNER JOIN " +
                        DogContract.SocialsEntry.TABLE_NAME +
                        " ON " + DogContract.ShelterEntry.TABLE_NAME +
                        "." + DogContract.ShelterEntry.COLUMN_SOCIALS_KEY +
                        " = " + DogContract.SocialsEntry.TABLE_NAME +
                        "." + DogContract.SocialsEntry._ID);

        sShelterFromDogIdBuilder.setTables(
                DogContract.DogEntry.TABLE_NAME + " INNER JOIN " +
                        DogContract.ShelterEntry.TABLE_NAME +
                        " ON " + DogContract.DogEntry.TABLE_NAME +
                        "." + DogContract.DogEntry.COLUMN_SHELTER_KEY +
                        " = " + DogContract.ShelterEntry.TABLE_NAME +
                        "." + DogContract.ShelterEntry._ID);
    }

    private static final String sShelterByCityAndNameSelection =
            DogContract.ShelterEntry.TABLE_NAME +
                    "." + DogContract.ShelterEntry.COLUMN_CITY + " = ? AND " +
                    DogContract.ShelterEntry.COLUMN_NAME + " = ? ";

    //shelter._id = ?
    private static final String sShelterByIdSelection =
            DogContract.ShelterEntry.TABLE_NAME +
                    "." + DogContract.ShelterEntry._ID + " = ? ";

    //social._id = ?
    private static final String sSocialsByIdSelection =
            DogContract.SocialsEntry.TABLE_NAME+
                    "." + DogContract.SocialsEntry._ID + " = ? ";

    //dog._id = ?
    private static final String sDogByIdSelection =
            DogContract.DogEntry.TABLE_NAME +
                    "." + DogContract.DogEntry._ID + " = ? ";

    /**
     * Wrote this to give a generic handler for the base Uri calls ("dogs", "socials", etc.) so I
     * could condense the code in the query method. The table parameter defines which table to
     * query from.
     * @param table String representing the table name to query from.
     */
    private Cursor getGenericQuery(Uri uri, String[] projection, String selection, String[] selectionArgs,
                                   String sortOrder, String table) {
        return mOpenHelper.getReadableDatabase().query(
                table,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getDogById(Uri uri, String[] projection, String sortOrder) {
        long id = DogContract.DogEntry.getDogFromUri(uri);

        String[] selectionArgs = new String[] {Long.toString(id)};
        String selection = sDogByIdSelection;

        return mOpenHelper.getReadableDatabase().query(
                DogContract.DogEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getShelterFromCityName(Uri uri, String[] projection, String sortOrder) {
        String city = DogContract.ShelterEntry.getCityFromUri(uri);
        String name = DogContract.ShelterEntry.getNameFromUri(uri);

        String[] selectionArgs = new String[] {city, name};
        String selection = sShelterByCityAndNameSelection;

        return mOpenHelper.getReadableDatabase().query(
                DogContract.ShelterEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getSocialsFromShelterId(Uri uri, String[] projection, String sortOrder) {
        long id = DogContract.SocialsEntry.getIdFromUri(uri);

        return sSocialsFromShelterIdBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sSocialsByIdSelection,
                new String[]{Long.toString(id)},
                null,
                null,
                sortOrder);
    }

    private Cursor getShelterFromDogId(Uri uri, String[] projection, String sortOrder) {
        long id = DogContract.DogEntry.getDogFromUri(uri);

        return sShelterFromDogIdBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sShelterByIdSelection,
                new String[]{Long.toString(id)},
                null,
                null,
                sortOrder);
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(DogContract.CONTENT_AUTHORITY, DogContract.PATH_DOG, DOG);
        matcher.addURI(DogContract.CONTENT_AUTHORITY, DogContract.PATH_DOG + "/#", DOG_FROM_ID);

        matcher.addURI(DogContract.CONTENT_AUTHORITY, DogContract.PATH_SHELTER, SHELTER);
        matcher.addURI(DogContract.CONTENT_AUTHORITY, DogContract.PATH_SHELTER + "/#", SHELTER_FROM_DOG);
        matcher.addURI(DogContract.CONTENT_AUTHORITY, DogContract.PATH_SHELTER + "/*/*", SHELTER_FROM_CITY_NAME);

        matcher.addURI(DogContract.CONTENT_AUTHORITY, DogContract.PATH_SOCIALS, SOCIALS);
        matcher.addURI(DogContract.CONTENT_AUTHORITY, DogContract.PATH_SOCIALS + "/#", SOCIALS_FROM_SHELTER_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DogDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case DOG:
                return DogContract.DogEntry.CONTENT_TYPE;
            case DOG_FROM_ID:
                return DogContract.DogEntry.CONTENT_ITEM_TYPE;
            case SHELTER:
                return DogContract.ShelterEntry.CONTENT_TYPE;
            case SHELTER_FROM_DOG:
                return DogContract.ShelterEntry.CONTENT_ITEM_TYPE;
            case SHELTER_FROM_CITY_NAME:
                return DogContract.ShelterEntry.CONTENT_ITEM_TYPE;
            case SOCIALS:
                return DogContract.SocialsEntry.CONTENT_TYPE;
            case SOCIALS_FROM_SHELTER_ID:
                return DogContract.SocialsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "dog"
            case DOG:
            {
                retCursor = getGenericQuery(uri, projection, selection, selectionArgs,
                        sortOrder, DogContract.DogEntry.TABLE_NAME);
                break;
            }
            // "dog/#"
            case DOG_FROM_ID: {
                retCursor = getDogById(uri, projection, sortOrder);
                break;
            }
            // "shelter"
            case SHELTER: {
                retCursor = getGenericQuery(uri, projection, selection, selectionArgs,
                        sortOrder, DogContract.ShelterEntry.TABLE_NAME);
                break;
            }
            // "shelter/#"
            case SHELTER_FROM_DOG: {
                retCursor = getShelterFromDogId(uri, projection, sortOrder);
                break;
            }
            // "shelter/*/*"
            case SHELTER_FROM_CITY_NAME: {
                retCursor = getShelterFromCityName(uri, projection, sortOrder);
                break;
            }
            // "socials"
            case SOCIALS: {
                retCursor = getGenericQuery(uri, projection, selection, selectionArgs,
                        sortOrder, DogContract.SocialsEntry.TABLE_NAME);
                break;
            }
            case SOCIALS_FROM_SHELTER_ID: {
                retCursor = getSocialsFromShelterId(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case DOG: {
                long _id = db.insert(DogContract.DogEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DogContract.DogEntry.buildDogUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SHELTER: {
                long _id = db.insert(DogContract.ShelterEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DogContract.ShelterEntry.buildShelterUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SOCIALS: {
                long _id = db.insert(DogContract.SocialsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DogContract.SocialsEntry.buildSocialUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case DOG:
                rowsDeleted = db.delete(
                        DogContract.DogEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SHELTER:
                rowsDeleted = db.delete(
                        DogContract.ShelterEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SOCIALS:
                rowsDeleted = db.delete(
                        DogContract.SocialsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case DOG:
                rowsUpdated = db.update(
                        DogContract.DogEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SHELTER:
                rowsUpdated = db.update(
                        DogContract.ShelterEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SOCIALS:
                rowsUpdated = db.update(
                        DogContract.SocialsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case DOG:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DogContract.DogEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case SHELTER:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DogContract.ShelterEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case SOCIALS:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DogContract.SocialsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}