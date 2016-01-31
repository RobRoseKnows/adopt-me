package io.robrose.hoya.adoptme;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.nirhart.parallaxscroll.views.ParallaxListView;

import io.robrose.hoya.adoptme.data.DogContract;


public class MainFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private SwipeAdapter mSwipeAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ParallaxListView mParallaxListView;

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    // I really wish I could minimize this.
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

        // RIP simple calling.
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.INTERNET)) {
                // Show rationale
                Utility.showRationale(R.string.internet_permission_rationale, getActivity());

                // Now request permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.INTERNET},
                        PERMISSION_REQUEST_INTERNET);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.INTERNET},
                        PERMISSION_REQUEST_INTERNET);
            }
        } else {
            //Update dogs
        }
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            // Requested permission for INTERNET
            case PERMISSION_REQUEST_INTERNET: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO Update dogs
                } else {
                    // Display something to acknowledge that we can retrieve no dogs.
                }

                return;
            }

            // Requested permission for ACCESS_COARSE_LOCATION
            case PERMISSION_REQUEST_LOCATION: {
                try {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // TODO Instruct user on how to turn on GPS.
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        mSwipeAdapter.setUserLocation(mLastLocation);
                    } else {
                        // TODO Allow user to define a location via text. Probably store that in settings
                    }
                } catch (SecurityException e) {
                    Log.d(LOG_TAG, "Permission response was wrong somehow.", e);
                }

                return;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mSwipeAdapter = new SwipeAdapter(getActivity(), null, 0);
        mParallaxListView = (ParallaxListView) rootView.findViewById(R.id.parallax_list_view);
        mParallaxListView.setAdapter(mSwipeAdapter);

        mParallaxListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                long _id = cursor.getLong(COL_DOG_ID);
                if(cursor != null) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .setData(DogContract.DogEntry.buildDogUri(_id));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    public Location getUserLocation() {
        return mLastLocation;
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

    @Override
    public void onConnected(Bundle bundle) {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show rationale
                Utility.showRationale(R.string.location_permission_rationale, getActivity());

                // Now request permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_LOCATION);

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_LOCATION);
            }
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mSwipeAdapter.setUserLocation(mLastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
