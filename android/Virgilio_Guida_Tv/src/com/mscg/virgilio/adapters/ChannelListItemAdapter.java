package com.mscg.virgilio.adapters;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mscg.virgilio.R;
import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.util.ChannelLinearLayout;

public class ChannelListItemAdapter extends GenericListItemAdapter<Channel> {

    public ChannelListItemAdapter(Context context, int textViewResourceId, Channel[] objects) {
        this(context, textViewResourceId, Arrays.asList(objects));
    }

    public ChannelListItemAdapter(Context context, int textViewResourceId, List<Channel> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChannelLinearLayout itemView = null;
        Channel channel = getItem(position);

        InfoHolder holder = null;

        if (convertView == null) {
            itemView = new ChannelLinearLayout(null, getContext());
            vi.inflate(textViewResourceId, itemView, true);
            holder = new InfoHolder();
            holder.textView = (TextView) itemView.findViewById(R.id.channelName);
            itemView.setTag(holder);
        } else {
            itemView = (ChannelLinearLayout) convertView;
            holder = (InfoHolder) itemView.getTag();
        }

        itemView.setChannel(channel);

        holder.textView.setText(channel.getName());

        return itemView;
    }

    private class InfoHolder {
        TextView textView;
    }

}
