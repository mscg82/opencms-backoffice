package com.mscg.virgilio.database;

import com.mscg.virgilio.programs.Channel;

public interface ChannelsManagement {

    public Channel getChannel(long programsID, long channelID);

    public Channel getChannel(long programsID, long channelID, boolean removeElement);

    public void saveChannel(long programsID, long channelID, Channel channel);

    public boolean removeChannel(long programsID, long channelID);

}
