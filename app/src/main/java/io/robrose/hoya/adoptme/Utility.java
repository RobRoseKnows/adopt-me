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
}
