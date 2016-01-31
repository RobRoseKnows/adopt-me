package io.robrose.hoya.adoptme;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.robrose.hoya.adoptme.data.DogContract;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SwipeAdapter mSwipeAdapter;

    private static final String[] DOG_COLUMNS = {
            DogContract.DogEntry.TABLE_NAME + "." + DogContract.DogEntry._ID,
            DogContract.DogEntry.COLUMN_NAME,
            DogContract.ShelterEntry.COLUMN_NAME,
            DogContract.DogEntry.COLUMN_BIO,
            DogContract.DogEntry.COLUMN_SHELTER_KEY,
            DogContract.ShelterEntry.COLUMN_SOCIALS_KEY,
            DogContract.ShelterEntry.COLUMN_LAT,
            DogContract.ShelterEntry.COLUMN_LONG,
            DogContract.DogEntry.COLUMN_AGE,
            DogContract.DogEntry.COLUMN_ENTERED,
            DogContract.DogEntry.COLUMN_ALBUM,
            DogContract.ShelterEntry.COLUMN_CITY,
            DogContract.DogEntry.COLUMN_INTEREST,
            DogContract.DogEntry.COLUMN_INTERNAL,
            DogContract.ShelterEntry.COLUMN_ABOUT,
            DogContract.ShelterEntry.COLUMN_ADDRESS,
            DogContract.SocialsEntry.COLUMN_FACEBOOK,
            DogContract.SocialsEntry.COLUMN_TWITTER,
            DogContract.SocialsEntry.COLUMN_INSTAGRAM,
            DogContract.SocialsEntry.COLUMN_SNAPCHAT,
            DogContract.SocialsEntry.COLUMN_HASHTAG
    };

    static final int COL_DOG_ID = 0;
    static final int COL_DOG_NAME = 1;
    static final int COL_SHELTER_NAME = 2;
    static final int COL_DOG_BIO = 3;
    static final int COL_SHELTER_KEY = 4;
    static final int COL_SOCIALS_KEY = 5;
    static final int COL_COORD_LAT = 6;
    static final int COL_COORD_LONG = 7;
    static final int COL_AGE = 8;
    static final int COL_ENTERED = 9;
    static final int COL_ALBUM = 10;
    static final int COL_CITY = 11;
    static final int COL_INTEREST = 12;
    static final int COL_INTERNAL = 13;
    static final int COL_ABOUT = 14;
    static final int COL_ADDRESS = 15;
    static final int COL_FACEBOOK = 16;
    static final int COL_TWITTER = 17;
    static final int COL_INSTAGRAM = 18;
    static final int COL_SNAPCHAT = 19;
    static final int COL_HASHTAG = 20;

    private final int PERMISSION_REQUEST_INTERNET = 9;
    private final int PERMISSION_REQUEST_LOCATION = 19;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        // RIP simple calling.
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.INTERNET)) {
                // Show rationale
                Utility.showRationale(R.string.internet_permission_rationale, getActivity());

                // Now request permission
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_INTERNET);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_INTERNET);
            }
        } else {
            //Update dogs
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_INTERNET: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Update dogs
                } else {
                    // Display something to acknowledge that we can retrieve no dogs.
                }

                return;
            }

            case PERMISSION_REQUEST_LOCATION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Get a users location.
                } else {
                    // TODO Allow user to define a location via text. Probably store that in settings
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mSwipeAdapter = new SwipeAdapter(getActivity(), null, 0);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = "RANDOM()";
        Uri getDogsUri = DogContract.DogEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                getDogsUri,
                DOG_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSwipeAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSwipeAdapter.swapCursor(null);
    }
}
