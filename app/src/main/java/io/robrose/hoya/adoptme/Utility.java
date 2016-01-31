package io.robrose.hoya.adoptme;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Robert on 1/30/2016.
 */
public class Utility {
    public static String getDisplayableAge(int months) {
        String display = "";
        if(months < 1) {
            display = "< 1 Month";
        } else if(months == 1) {
            display = months + " Month";
        } else if(months < 12) {
            display = months + " Months";
        } else if(months == 12) {
            display = (months / 12) + " Year";
        } else {
            display = (months / 12) + " Years";
        }
        return display;
    }

    public static void showRationale(int resId, Context context) {
        // Show them a dialog with the rationale.
        AlertDialog.Builder rationaleAlert = new AlertDialog.Builder(context);
        rationaleAlert.setMessage(resId);
        rationaleAlert.setTitle(R.string.app_name);
        rationaleAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        rationaleAlert.setCancelable(true);
        rationaleAlert.create().show();

    }

    /**
     * This function uses the Haversine formula to make a much faster (but slightly less accurate)
     * distance between two coordinates on a globe.
     * @param lat1 Latitude of Point 1
     * @param lon1 Longitude of Point 1
     * @param lat2 Latitude of Point 2
     * @param lon2 Longitude of Point 2
     * @return A double representing the distance in kilometers.
     */
    public static double roughPointDistance(double lat1, double lon1, double lat2, double lon2) {
        // Approximate Equirectangular -- works if (lat1,lon1) ~ (lat2,lon2)
        // Thanks to Laurent on SO: http://stackoverflow.com/a/4339615/1021259
        int R = 6371; // km
        double x = (lon2 - lon1) * Math.cos((lat1 + lat2) / 2);
        double y = (lat2 - lat1);
        return Math.sqrt(x * x + y * y) * R;
    }

    public static double convertToMiles(double kms) {
        return kms * 0.62137;
    }
}
