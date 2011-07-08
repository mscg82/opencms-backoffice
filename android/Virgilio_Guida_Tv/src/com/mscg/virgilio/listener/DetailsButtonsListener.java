package com.mscg.virgilio.listener;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;

public class DetailsButtonsListener implements OnClickListener {

    private Dialog dialog;

    public DetailsButtonsListener(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void onClick(View v) {
        dialog.dismiss();
    }

}
