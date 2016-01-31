package io.robrose.hoya.adoptme;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by Robert on 1/30/2016.
 */
public class SwipeAdapter extends CursorAdapter {

    Location mUserLocation;

    public static class ViewHolder{
        public final ImageView dogPicView;
        public final TextView nameAgeView;
        public final TextView shelterNameView;
        public final TextView distanceView;

        public ViewHolder(View view) {
            dogPicView = (ImageView) view.findViewById(R.id.swipe_portrait_imageview);
            nameAgeView = (TextView) view.findViewById(R.id.swipe_name_textview);
            shelterNameView = (TextView) view.findViewById(R.id.swipe_shelter_textview);
            distanceView = (TextView) view.findViewById(R.id.swipe_miles_textview);
        }
    }

    public SwipeAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public void setUserLocation(Location location) {
        mUserLocation = location;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.swipe_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String dogName = cursor.getString(MainFragment.COL_DOG_NAME);
        int dogAge = cursor.getInt(MainFragment.COL_AGE);
        String dogPicUrl = cursor.getString(MainFragment.COL_ALBUM); // This will eventually be an album rather than a
        String shelterName = cursor.getString(MainFragment.COL_SHELTER_NAME);
        String shelterCity = cursor.getString(MainFragment.COL_CITY);

        // Get the coordinates
        double dogCoordLat = cursor.getDouble(MainFragment.COL_COORD_LAT);
        double dogCoordLong = cursor.getDouble(MainFragment.COL_COORD_LONG);

        double userCoordLat = mUserLocation.getLatitude();
        double userCoordLong = mUserLocation.getLongitude();

        double distanceInMiles = Utility.convertToMiles(
                Utility.roughPointDistance(dogCoordLat, dogCoordLong, userCoordLat, userCoordLong));

        viewHolder.nameAgeView.setText(context.getString(R.string.format_name_age, dogName, Utility.getDisplayableAge(dogAge)));
        Picasso.with(context).load(dogPicUrl).centerCrop()
                .placeholder(R.drawable.dog_wdob)
                .into(viewHolder.dogPicView);
        viewHolder.shelterNameView.setText(context.getString(R.string.format_city_name, shelterName, shelterCity));
    }
}
