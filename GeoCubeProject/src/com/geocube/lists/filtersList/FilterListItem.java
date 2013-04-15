package com.geocube.lists.filtersList;

public class FilterListItem {
    public Boolean isSelected;
    public String filterName;

    public FilterListItem(String filterName) {
        this.isSelected = false;
        this.filterName = filterName;
    }

    @Override
    public String toString() {
        return filterName;
    }

}
