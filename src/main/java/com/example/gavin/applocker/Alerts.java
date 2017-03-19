package com.example.gavin.applocker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

/**
 * Created by Gavin on 2017-03-19.
 */

public class Alerts {
    public static AlertDialog locked(Context context) {

        final Context cont = context;
        // Pass context to AlertDialog.Builder
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setTitle("Application Locked");
        alert.setMessage("This application is locked. You may unlock it using Applocker.");
        alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in = new Intent();
                        in.setAction(Intent.ACTION_MAIN);
                        in.addCategory(Intent.CATEGORY_HOME);
                        cont.startActivity(in);
                    }
                });
        //alert.show();
        return alert;
    }
}
