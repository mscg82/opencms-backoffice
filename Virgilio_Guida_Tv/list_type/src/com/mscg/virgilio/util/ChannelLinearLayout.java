package com.mscg.virgilio.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mscg.virgilio.programs.Channel;

public class ChannelLinearLayout extends LinearLayout {

    private Channel channel;

    public ChannelLinearLayout(Channel channel, Context context) {
        super(context);
        setChannel(channel);
    }

    public ChannelLinearLayout(Channel channel, Context context, AttributeSet attrs) {
        super(context, attrs);
        setChannel(channel);
    }

    public ChannelLinearLayout(Context context) {
        this(null, context);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
