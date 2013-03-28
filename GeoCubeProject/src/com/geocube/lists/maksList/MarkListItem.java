package com.geocube.lists.maksList;

public class MarkListItem {
    public Boolean isSelected;
    public String channelName;
    public String channelDesc;

    public MarkListItem(String channelName, String channelDesc) {
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
