package com.ironaviation.traveller.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ironaviation.traveller.R;


public class VersionUpdateDialog extends Dialog {

    public VersionUpdateDialog(Context context) {
        super(context);
    }

    public VersionUpdateDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public VersionUpdateDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final VersionUpdateDialog dialog = new VersionUpdateDialog(context, R.style.WaitDialog);
            View view = inflater.inflate(R.layout.dialog_prompt_layout, null);

            TextView titleTxt = (TextView) view.findViewById(R.id.dialogTitle);
            TextView contentTxt = (TextView) view.findViewById(R.id.dialogContent);
            TextView positive = (TextView) view.findViewById(R.id.positive);
            TextView negative = (TextView) view.findViewById(R.id.negative);

            //dialog 确定按钮
            if (positiveButtonText != null) {

                positive.setText(positiveButtonText);

                if (positiveButtonClickListener != null) {
                    positive.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                positive.setVisibility(View.GONE);
            }

            //dialog 取消按钮
            if (negativeButtonText != null) {

                negative.setText(negativeButtonText);

                if (negativeButtonClickListener != null) {
                    negative.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                negative.setVisibility(View.GONE);
            }

            if (message != null) {
                contentTxt.setText(message);
            }

            if (title != null) {
                titleTxt.setText(title);
            }


            dialog.setContentView(view);

            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int screenWidth = manager.getDefaultDisplay().getWidth();

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = (int) (screenWidth * 0.8);
            dialog.getWindow().setAttributes(params);

            return dialog;
        }
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return true;
    }
}
