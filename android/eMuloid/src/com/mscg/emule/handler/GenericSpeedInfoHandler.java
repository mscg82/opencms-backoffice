package com.mscg.emule.handler;

import android.os.Message;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.mscg.emule.GenericSpeedInfoActivity;
import com.mscg.emule.bean.SpeedBean;
import com.mscg.emule.util.Constants;

public class GenericSpeedInfoHandler extends GenericHandler {

	public GenericSpeedInfoHandler(GenericSpeedInfoActivity context) {
		super(context);
	}

	@Override
	public void handleMessage(Message msg) {
		SpeedBean speedBean = null;
		LinearLayout wrapper = null;
		int width = -1;
		switch(msg.what) {
		case Constants.Messages.SpeedBox.UPDATE_SERVER:
			((GenericSpeedInfoActivity)context).getServerStatusImg().setImageResource(msg.arg1);
			((GenericSpeedInfoActivity)context).getServerName().setText((String)msg.obj);
			break;
		case Constants.Messages.SpeedBox.UPDATE_KAD:
			((GenericSpeedInfoActivity)context).getKadStatus().setText(msg.arg1);
			break;
		case Constants.Messages.SpeedBox.UPDATE_DOWNLOAD_SPEED:
			speedBean = (SpeedBean)msg.obj;
			((GenericSpeedInfoActivity)context).getDownloadSpeedTxt().setText(speedBean.getSpeedText());
			wrapper = ((GenericSpeedInfoActivity)context).getDownloadSpeedWrap();
			width = (int)Math.round(wrapper.getWidth() * (speedBean.getSpeedPerc() / 100.0));
			((GenericSpeedInfoActivity)context).getDownloadSpeed().setLayoutParams(
					new LinearLayout.LayoutParams(width, LayoutParams.FILL_PARENT));
			break;
		case Constants.Messages.SpeedBox.UPDATE_UPLOAD_SPEED:
			speedBean = (SpeedBean)msg.obj;
			((GenericSpeedInfoActivity)context).getUploadSpeedTxt().setText(speedBean.getSpeedText());
			wrapper = ((GenericSpeedInfoActivity)context).getUploadSpeedWrap();
			width = (int)Math.round(wrapper.getWidth() * (speedBean.getSpeedPerc() / 100.0));
			((GenericSpeedInfoActivity)context).getUploadSpeed().setLayoutParams(
					new LinearLayout.LayoutParams(width, LayoutParams.FILL_PARENT));
			break;
		default:
			super.handleMessage(msg);
		}
	}
}
