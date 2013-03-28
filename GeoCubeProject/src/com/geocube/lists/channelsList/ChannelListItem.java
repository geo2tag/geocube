package com.geocube.lists.channelsList;

public class ChannelListItem {
    public Boolean isSelected;
    public String channelName;
    public String channelDesc;

    public ChannelListItem(String channelName, String channelDesc) {
        this.isSelected = false;
        this.channelDesc = channelDesc;
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelDesc() {
        return channelDesc;
    }
}
