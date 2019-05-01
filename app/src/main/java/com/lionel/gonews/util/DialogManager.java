package com.lionel.gonews.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.lionel.gonews.R;

public class DialogManager {
    private static boolean isErrorDialogShowing = false;

    public static void showError(Context context, String errorMsg) {
        if (!isErrorDialogShowing) {
            isErrorDialogShowing = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(errorMsg);
            builder.setMessage(R.string.error_dialog_content);
            builder.setPositiveButtonIcon(context.getDrawable(R.drawable.ic_check_circle));
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isErrorDialogShowing = false;
                }
            });
            showDialog(builder);
        }
    }

    public static void showAbout(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_about, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(true);
        showDialog(builder);
    }

    private static void showDialog(AlertDialog.Builder builder) {
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_white);
        dialog.getWindow().setWindowAnimations(R.style.AnimDialog);
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
