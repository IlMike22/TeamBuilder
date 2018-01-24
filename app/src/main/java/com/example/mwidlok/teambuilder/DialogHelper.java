package com.example.mwidlok.teambuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;

/**
 * This class offers some convenience methods for showing custom alert dialog with different user options.
 */

public class DialogHelper {

    // this dialog shows a title, message and - if you wish - a no button additional to the ok button.
    // After ok the current activity will close.
    // You can also put a ResultCode.
    public static void showStandardDialog(String title,
                                    String message,
                                    boolean isNegativeButton,
                                    final Activity currentActivity,
                                    final int resultCode)
    {
        try
        {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                builder = new AlertDialog.Builder(currentActivity,android.R.style.Theme_Material_Dialog_Alert);
            else
                builder = new AlertDialog.Builder(currentActivity);

            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("TeamBuilder","Ok clicked. Now turn back to overview.");
                            if (resultCode != -1)
                                currentActivity.setResult(resultCode);
                            currentActivity.finish();
                        }
                    });

            if (isNegativeButton)
            {
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }

            builder.setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
        catch(Exception exc)
        {
            Log.e("TeamBuilder","Error showing standard dialog. Details: " + exc.getMessage());
        }

    }

}
