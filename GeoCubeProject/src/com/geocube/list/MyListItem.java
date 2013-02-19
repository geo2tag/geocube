package com.geocube.list;

import android.bluetooth.BluetoothDevice;

public class MyListItem {
    public Boolean isSelected;
    public String channelName;
    public String channelDesc;

    public MyListItem(String channelName, String channelDesc) {
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
