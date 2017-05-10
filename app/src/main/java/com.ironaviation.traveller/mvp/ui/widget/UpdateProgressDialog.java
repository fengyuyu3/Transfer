package com.ironaviation.traveller.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;

public class UpdateProgressDialog extends Dialog {

    private View mContentView;

    public UpdateProgressDialog(Builder builder) {
        super(builder.context,builder.animationStyle);
        this.mContentView = builder.contentView;
        initLayout();
    }

    private void initLayout(){
        setContentView(mContentView);
    }

    public static class Builder {
        private Context context;
        private String title;
        private View contentView;
        private int animationStyle;

        public Builder(Context context) {
            this.context = context;
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

        public Builder animationStyle(int animationStyle) {
            this.animationStyle = animationStyle;
            return this;
        }

        public UpdateProgressDialog create() {
            if (contentView == null)
                throw new IllegalStateException("contentView is required");
//            if (animationStyle == null)
//                throw new IllegalStateException("CustomPopupWindowStyle is required");

            return new UpdateProgressDialog(this);
        }
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return true;
    }
}
