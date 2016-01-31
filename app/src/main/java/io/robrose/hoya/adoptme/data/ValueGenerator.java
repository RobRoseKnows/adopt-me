package io.robrose.hoya.adoptme.data;
import java.util.Random;
/**
 * Created by Ati on 1/30/16.
 */
public class ValueGenerator {

    ContentValues createShelterValues(long locationRowId)
    {
        ContentValues shelterValues = new ContentValues();

        shelterValues.put(DogContract.DogEntry.COLUMN_NAME, getName());
        shelterValues.put(DogContract.DogEntry.COLUMN_AGE, getAge());
        shelterValues.put(DogContract.DogEntry.COLUMN_ENTERED, 1422103500); //Default Value
        shelterValues.put(DogContract.DogEntry.COLUMN_BIO, "Playful");
        shelterValues.put(DogContract.DogEntry.COLUMN_SHELTER_KEY, 1.3);
        shelterValues.put(DogContract.DogEntry.COLUMN_ALBUM, 75);
        shelterValues.put(DogContract.DogEntry.COLUMN_INTEREST, 65);
        shelterValues.put(DogContract.DogEntry.COLUMN_INTERNAL, "Asteroids");

        return shelterValues;
    }
String getName(){ //Returns a random name from a pre-determined array

    String[] names ={"Vido", "Kevin", "Enzo", "Odin", "Justin", "Larry", "Bill", "Shaniqua"};
    String randomName = names[new Random().nextInt(names.length)];

    return randomName;
}
Integer getAge(){  //Returns a random integer from 1 - 144
    int randomNum = 0;
    randomNum = 1 + (int) (Math.random()*144);
    return randomNum;
}






}
