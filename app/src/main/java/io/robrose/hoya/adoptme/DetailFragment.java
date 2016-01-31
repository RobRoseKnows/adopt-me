package io.robrose.hoya.adoptme;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.robrose.hoya.adoptme.data.DogContract;

/**
 * Created by Robert on 1/31/2016.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 101;

    // TODO: optimize the columns that are requested. I don't need all these but I'm getting them because I can.
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

    private ImageView mPortraitView;
    private TextView mNameAgeView;
    private TextView mShelterView;
    private TextView mDistanceView;
    private TextView mBioView;

    private Location mUserLocation;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mPortraitView = (ImageView) rootView.findViewById(R.id.portrait_imageview);
        mNameAgeView = (TextView) rootView.findViewById(R.id.name_age_textview);
        mShelterView = (TextView) rootView.findViewById(R.id.shelter_textview);
        mDistanceView = (TextView) rootView.findViewById(R.id.distance_textview);
        mBioView = (TextView) rootView.findViewById(R.id.bio_textview);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if(intent == null) return null;

        Loader<Cursor> cursorLoader = new CursorLoader(
                getActivity(),
                intent.getData(),
                DOG_COLUMNS,
                null,
                null,
                null
        );
        return cursorLoader;
    }

    public void setUserLocation(Location loc) {
        mUserLocation = loc;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()) {
            // TODO: allow albums to be uploaded from imgur
            String pictureUrl = data.getString(COL_ALBUM);
            Picasso.with(getActivity()).load(pictureUrl).fit().centerCrop()
                    .placeholder(R.drawable.dog_wdob)
                    .into(mPortraitView);

            // Populate the dog's name/age view
            String dogName = data.getString(COL_DOG_NAME);
            int dogAge = data.getInt(COL_AGE);
            String formattedNameAge = getString(R.string.format_name_age, dogName, dogAge);
            mNameAgeView.setText(formattedNameAge);

            String shelterName = data.getString(COL_SHELTER_NAME);
            String shelterCity = data.getString(COL_CITY);
            String formattedNameCity = getString(R.string.format_city_name, shelterName, shelterCity);
            mShelterView.setText(formattedNameCity);

            // Get all the coordinates.
            double dogCoordLat = data.getDouble(COL_COORD_LAT);
            double dogCoordLong = data.getDouble(COL_COORD_LONG);
            double userCoordLat = mUserLocation.getLatitude();
            double userCoordLong = mUserLocation.getLongitude();

            // Estimate the distance.
            double distanceInMiles = Utility.convertToMiles(
                    Utility.roughPointDistance(dogCoordLat, dogCoordLong, userCoordLat, userCoordLong));
            String distanceString = getString(R.string.format_distance_miles, distanceInMiles);
            mDistanceView.setText(distanceString);

            String dogBio = data.getString(COL_DOG_BIO);
            mBioView.setText(dogBio);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
