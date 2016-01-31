package io.robrose.hoya.adoptme.data;
import android.content.ContentValues;
import java.util.Random;
import java.util.Random;
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
            cvs[index] = socialValues;
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
            cvs[index] = shelterValues;
        }
        return cvs;
    }
    public static ContentValues[] generateDogs(int n, int shelters)
    {
        ContentValues[] cvs = new ContentValues[n];
        for (int i = 0; i < n; i++){
            int index = new Random().nextInt(handles.length);
            shelterValues.put(DogContract.DogEntry.COLUMN_NAME, locationRowId);
        shelterValues.put(DogContract.DogEntry.COLUMN_AGE, TEST_DATE);
        shelterValues.put(DogContract.DogEntry.COLUMN_ENTERED, 1.1);
        shelterValues.put(DogContract.DogEntry.COLUMN_BIO, "Playful");
        shelterValues.put(DogContract.DogEntry.COLUMN_SHELTER_KEY, getRandomNumber());
        shelterValues.put(DogContract.DogEntry.COLUMN_ALBUM, //url);shelterValues.put(DogContract.DogEntry.COLUMN_INTEREST, 65);
        shelterValues.put(DogContract.DogEntry.COLUMN_INTERNAL, "Asteroids");
        }

}

}