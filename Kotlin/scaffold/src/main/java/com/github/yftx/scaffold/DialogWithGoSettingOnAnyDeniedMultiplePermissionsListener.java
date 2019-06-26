package com.github.yftx.scaffold;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;

/**
 * Created by liuzl on 2018/9/13.
 */


public class DialogWithGoSettingOnAnyDeniedMultiplePermissionsListener extends BaseMultiplePermissionsListener {
    public interface OnConfirmClickListener {
        void onConfirm();
    }


    private final Context context;
    private final String title;
    private final String message;
    private final String positiveButtonText;
    private final Drawable icon;
    private final OnConfirmClickListener confirmClickListener;

    private DialogWithGoSettingOnAnyDeniedMultiplePermissionsListener(Context context, String title,
                                                                      String message, String positiveButtonText,
                                                                      Drawable icon, OnConfirmClickListener confirmClickListener) {
        this.context = context;
        this.title = title;
        this.message = message;
        this.positiveButtonText = positiveButtonText;
        this.icon = icon;
        this.confirmClickListener = confirmClickListener;
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        super.onPermissionsChecked(report);

        if (!report.areAllPermissionsGranted()) {
            showDialog();
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (confirmClickListener != null) {
                            confirmClickListener.onConfirm();
                        }
                        dialog.dismiss();
                    }
                })
                .setIcon(icon)
                .show();
    }

    /**
     * Builder class to configure the displayed dialog.
     * Non set fields will be initialized to an empty string.
     */
    public static class Builder {
        private final Context context;
        private String title;
        private String message;
        private String buttonText;
        private Drawable icon;
        private OnConfirmClickListener confirmClickListener;

        private Builder(Context context) {
            this.context = context;
        }

        public static Builder withContext(Context context) {
            return new Builder(context);
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withTitle(@StringRes int resId) {
            this.title = context.getString(resId);
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withMessage(@StringRes int resId) {
            this.message = context.getString(resId);
            return this;
        }

        public Builder withButtonText(String buttonText) {
            this.buttonText = buttonText;
            return this;
        }

        public Builder withButtonText(@StringRes int resId) {
            this.buttonText = context.getString(resId);
            return this;
        }

        public Builder withIcon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder withIcon(@DrawableRes int resId) {
            this.icon = context.getResources().getDrawable(resId);
            return this;
        }

        public Builder withConfirmClickListener(OnConfirmClickListener listener) {
            this.confirmClickListener = listener;
            return this;
        }

        public DialogWithGoSettingOnAnyDeniedMultiplePermissionsListener build() {
            String title = this.title == null ? "" : this.title;
            String message = this.message == null ? "" : this.message;
            String buttonText = this.buttonText == null ? "" : this.buttonText;
            OnConfirmClickListener listener = this.confirmClickListener;
            return new DialogWithGoSettingOnAnyDeniedMultiplePermissionsListener(context, title, message, buttonText, icon, listener);
        }
    }
}
