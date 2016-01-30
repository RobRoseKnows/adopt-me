package io.robrose.hoya.adoptme;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Robert on 1/30/2016.
 */
public class SwipeAdapter extends CursorAdapter {


    public static class ViewHolder{
        public final ImageView dogPicView;
        public final TextView nameAgeView;
        public final TextView distanceView;

        public ViewHolder(View view) {

        }
    }

    public SwipeAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
