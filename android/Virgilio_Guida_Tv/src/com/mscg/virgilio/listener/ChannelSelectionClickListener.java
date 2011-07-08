package com.mscg.virgilio.listener;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.mscg.virgilio.VirgilioGuidaTvChannelSelection;
import com.mscg.virgilio.VirgilioGuidaTvPrograms;
import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.util.CacheManager;
import com.mscg.virgilio.util.ChannelLinearLayout;
import com.mscg.virgilio.util.ContextAware;

public class ChannelSelectionClickListener extends ContextAware implements OnItemClickListener {

    public ChannelSelectionClickListener(VirgilioGuidaTvChannelSelection context) {
        super(context);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChannelLinearLayout cll = (ChannelLinearLayout) view;

        Channel channel = cll.getChannel();

        Intent intent = new Intent(context, VirgilioGuidaTvPrograms.class);
        intent.putExtra(VirgilioGuidaTvPrograms.CHANNEL, channel.getId());
        intent.putExtra(VirgilioGuidaTvPrograms.PROGRAMS, channel.getPrograms().getId());
        CacheManager.getInstance().saveChannel(channel.getPrograms().getId(), channel.getId(), channel);

        context.startActivity(intent);
    }

}
