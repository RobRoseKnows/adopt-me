package io.robrose.hoya.adoptme.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the Dog database.
 * @author Robert Rose
 */
public class DogContract {
    public static final String CONTENT_AUTHORITY = "io.robrose.hoya.adoptme";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DOG = "dog";
    public static final String PATH_SHELTER = "shelter";
    public static final String PATH_SOCIALS = "socials";

    public static final class DogEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DOG).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DOG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_DOG;

        public static final String TABLE_NAME = "dogs";

        // Column storing dog's name as a string.
        public static final String COLUMN_NAME = "name";
        // Column storing dog birthday
        public static final String COLUMN_AGE = "age";
        // Column with date in milliseconds that dog entered shelter
        public static final String COLUMN_ENTERED = "date_entered";
        // Column with short biography on the dog
        public static final String COLUMN_BIO = "bio";
        // Foreign key to shelter entry
        public static final String COLUMN_SHELTER_KEY = "shelter_id";
        // Imgur album url?
        public static final String COLUMN_ALBUM = "album";

        public static Uri buildDogUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ShelterEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHELTER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SHELTER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_SHELTER;

        public static final String TABLE_NAME = "shelter";

        public static final String COLUMN_NAME = "shelter_name";

        public static final String COLUMN_ABOUT = "about";

        public static final String COLUMN_SOCIALS_KEY = "social_id";

        public static final String COLUMN_LAT = "coord_lat";

        public static final String COLUMN_LONG = "coord_long";

        public static final String COLUMN_ADDRESS = "address";

        public static final String COLUMN_CITY = "city";

        public static Uri buildShelterUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class SocialsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SOCIALS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SOCIALS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_SOCIALS;

        public static final String TABLE_NAME = "socials";

        public static final String COLUMN_FACEBOOK = "facebook";

        public static final String COLUMN_TWITTER = "twitter";

        public static final String COLUMN_INSTAGRAM = "instagram";

        public static final String COLUMN_SNAPCHAT = "snapchat";

        public static final String COLUMN_HASH = "hashtag";

        public static Uri buildSocialUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
