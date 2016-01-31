package io.robrose.hoya.adoptme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.robrose.hoya.adoptme.data.DogContract;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContentResolver().bulkInsert(DogContract.SocialsEntry.CONTENT_URI, GenerateData.generateSocials(5));
        getContentResolver().bulkInsert(DogContract.ShelterEntry.CONTENT_URI, GenerateData.generateShelters(5));
        getContentResolver().bulkInsert(DogContract.DogEntry.CONTENT_URI, GenerateData.generateDogs(30, 5));
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}