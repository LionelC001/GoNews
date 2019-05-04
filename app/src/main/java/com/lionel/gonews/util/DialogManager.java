package com.lionel.gonews.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.lionel.gonews.R;

public class DialogManager {

    public interface IDialogCallback {
        void onDialogPositiveButtonClick();
    }


    private static boolean isErrorDialogShowing = false;

    public static void showError(Context context, String title) {
        if (!isErrorDialogShowing) {
            isErrorDialogShowing = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
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

    public static void showAsking(Context context, final IDialogCallback callback, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.tips);
        builder.setMessage(message);
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onDialogPositiveButtonClick();
            }
        });
        builder.setNegativeButtonIcon(context.getDrawable(R.drawable.ic_cancel));
        builder.setPositiveButtonIcon(context.getDrawable(R.drawable.ic_check_circle));
        showDialog(builder);

    }

    private static void showDialog(AlertDialog.Builder builder) {
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_white);
        dialog.getWindow().setWindowAnimations(R.style.AnimDialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
