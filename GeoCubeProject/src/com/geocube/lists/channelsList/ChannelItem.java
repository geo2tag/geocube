package com.geocube.lists.channelsList;

import ru.spb.osll.objects.Channel;

public class ChannelItem {
    private Channel channel;
    public boolean isSelected;

    public ChannelItem(Channel channel) {
        this.channel = channel;
        isSelected = false;
    }

    public Channel getChannel() {
        return channel;
    }
}
