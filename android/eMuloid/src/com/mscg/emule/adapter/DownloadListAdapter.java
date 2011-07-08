package com.mscg.emule.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mscg.emule.R;
import com.mscg.emule.bean.DownloadBean;

public class DownloadListAdapter extends GenericListItemAdapter<DownloadBean> {

	public DownloadListAdapter(Context context, int textViewResourceId, DownloadBean[] objects) {
		super(context, textViewResourceId, objects);
	}

	public DownloadListAdapter(Context context, int textViewResourceId, List<DownloadBean> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout itemView = null;
		DownloadBean downloadBean = getItem(position);

		InfoHolder holder = null;

		if(convertView == null) {
			itemView = (LinearLayout)vi.inflate(textViewResourceId, null, false);
			holder = new InfoHolder();
			holder.resourceType = (LinearLayout)itemView.findViewById(R.id.download_resource_type);
			holder.commentStatus = (ImageView)itemView.findViewById(R.id.download_comment_status);
			holder.downloadTitle = (TextView)itemView.findViewById(R.id.download_title);
			holder.size = (TextView)itemView.findViewById(R.id.download_size);
			holder.completed = (TextView)itemView.findViewById(R.id.download_completed);
			holder.speed = (TextView)itemView.findViewById(R.id.download_speed);
			itemView.setTag(holder);
		}
		else {
			itemView = (LinearLayout)convertView;
			holder = (InfoHolder)itemView.getTag();
		}

		holder.resourceType.setBackgroundResource(downloadBean.getTypeResource());
		if(downloadBean.getCommentStatusResource() == null) {
			holder.commentStatus.setVisibility(View.GONE);
		}
		else {
			holder.commentStatus.setVisibility(View.VISIBLE);
			holder.commentStatus.setImageResource(downloadBean.getCommentStatusResource());
		}
		holder.downloadTitle.setText(downloadBean.getTitle());
		holder.size.setText(downloadBean.getSize());
		holder.completed.setText(downloadBean.getCompleted());
		holder.speed.setText(downloadBean.getSpeed());

		return itemView;
	}

	private class InfoHolder {
		LinearLayout resourceType;
		ImageView commentStatus;
		TextView downloadTitle;
		TextView size;
		TextView completed;
		TextView speed;
	}

}
