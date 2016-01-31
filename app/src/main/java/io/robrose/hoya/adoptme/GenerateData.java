package io.robrose.hoya.adoptme;
import android.content.ContentValues;
import java.util.Random;
import java.util.Random;

import io.robrose.hoya.adoptme.data.DogContract;

/**
 * Created by Ati on 1/31/16.
 */
public class GenerateData {
    private static final String[] handles =
            {"dog","god","chucky","johnCena","champ","whoischamp",
                    "mlh","hoya","creative","jacob","dragonslayer","bob"};
    public static ContentValues[] generateSocials(int n) {
        ContentValues[] cvs = new ContentValues[n];
        for (int i = 0; i < n; i++) {
            int index = new Random().nextInt(handles.length);
            ContentValues socialValues = new ContentValues();
            socialValues.put(DogContract.SocialsEntry.COLUMN_FACEBOOK, handles[index]);
            socialValues.put(DogContract.SocialsEntry.COLUMN_HASHTAG, handles[index]);
            socialValues.put(DogContract.SocialsEntry.COLUMN_INSTAGRAM, handles[index]);
            socialValues.put(DogContract.SocialsEntry.COLUMN_SNAPCHAT, handles[index]);
            socialValues.put(DogContract.SocialsEntry.COLUMN_HASHTAG, handles[index]);
            socialValues.put(DogContract.SocialsEntry.COLUMN_TWITTER, handles[index]);
            cvs[i] = socialValues;
        }
        return cvs;
    }
    public static ContentValues[] generateShelters(int n) {
        ContentValues[] cvs = new ContentValues[n];
        for (int i = 0; i < n; i++) {
            int index = new Random().nextInt(handles.length);
            ContentValues shelterValues = new ContentValues();
            shelterValues.put(DogContract.ShelterEntry.COLUMN_NAME, handles[index]);
            shelterValues.put(DogContract.ShelterEntry.COLUMN_ABOUT, "About this shelter");
            shelterValues.put(DogContract.ShelterEntry.COLUMN_SOCIALS_KEY, i);
            shelterValues.put(DogContract.ShelterEntry.COLUMN_LAT, 37);
            shelterValues.put(DogContract.ShelterEntry.COLUMN_LONG, 35);
            shelterValues.put(DogContract.ShelterEntry.COLUMN_ADDRESS, "Funkytown");
            shelterValues.put(DogContract.ShelterEntry.COLUMN_CITY, "Georgetown");
            cvs[i] = shelterValues;
        }
        return cvs;
    }
    public static ContentValues[] generateDogs(int n, int shelters) {
        ContentValues[] cvs = new ContentValues[n];
        String[] pictures = {"http://i.imgur.com/yR4ykiA.jpg", "http://i.imgur.com/avPgJ9t.jpg",
                "http://i.imgur.com/By5US9k.jpg", "http://i.imgur.com/hCAJg1Z.jpg",
                "http://i.imgur.com/kbsllgP.jpg", "http://i.imgur.com/HyMOyQt.jpg",
                "http://i.imgur.com/mBAvAcQ.jpg", "http://i.imgur.com/AerijYH.jpg",
                "http://i.imgur.com/dhdYDYP.jpg", "http://i.imgur.com/vpi1OMb.jpg"};
        for (int i = 0; i < n; i++) {
            ContentValues dogValues = new ContentValues();
            int index = new Random().nextInt(handles.length);
            int indexPictures = new Random().nextInt(pictures.length);
            dogValues.put(DogContract.DogEntry.COLUMN_NAME, handles[index]);
            dogValues.put(DogContract.DogEntry.COLUMN_AGE, ((Math.random() * 143) + 1));
            dogValues.put(DogContract.DogEntry.COLUMN_ENTERED, 1453607252);
            dogValues.put(DogContract.DogEntry.COLUMN_BIO, "Playful");
            dogValues.put(DogContract.DogEntry.COLUMN_SHELTER_KEY, (Math.random() * shelters));
            dogValues.put(DogContract.DogEntry.COLUMN_ALBUM, pictures[indexPictures]);
            dogValues.put(DogContract.DogEntry.COLUMN_INTEREST, "TBD");
            dogValues.put(DogContract.DogEntry.COLUMN_INTERNAL, "Asteroids");
            cvs[i] = dogValues;
        }
        return cvs;
    }
}