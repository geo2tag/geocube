package com.geocube.list;

public class ChannelItem {
    private String name;
    private String descriotion;
    public boolean isSelected;

    public ChannelItem(String name, String description) {
        this.name = name;
        this.descriotion = description;
        isSelected = false;
    }

    public String getName() {
        return name;
    }

    public String getDescriotion() {
        return descriotion;
    }


}
