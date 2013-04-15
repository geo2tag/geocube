package com.geocube.lists.filtersList;

public class FilterItem {
    public Boolean isSelected;
    public String filterName;
    public int resource;

    public FilterItem(String filterName, int resource) {
        this.isSelected = false;
        this.filterName = filterName;
        this.resource = resource;
    }
}
