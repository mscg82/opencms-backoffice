/**
 *
 */
package com.mscg.emule.listener;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DialogDismisserListener implements OnClickListener {
	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	}
}