package com.lionel.gonews.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lionel.gonews.R;

public class DialogManager {
    private static boolean isErrorDialogShowing = false;

    public static void showErrorDialog(Context context) {
        if (!isErrorDialogShowing) {
            isErrorDialogShowing = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.error_dialog_title);
            builder.setMessage(R.string.error_dialog_content);
            builder.setPositiveButtonIcon(context.getDrawable(R.drawable.ic_check_circle));
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isErrorDialogShowing = false;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_search_box);
            dialog.getWindow().setWindowAnimations(R.style.AnimDialog);
            dialog.show();
        }
    }
}
