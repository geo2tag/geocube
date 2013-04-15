package com.geocube.lists.filtersList;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class FilterListAdapter extends ArrayAdapter<FilterItem> {

    private Context context;
    private int currentSelectedItem = -1;

    private ArrayList<FilterItem> items;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FilterItem item = getItem(position);
        FilterListItemView vItem;

        ArrayList<FilterListItem> l;

        if (convertView == null) {
            vItem = (FilterListItemView) View.inflate(context, com.geocube.R.layout.my_list_item_view_filters, null);
        } else {
            vItem = (FilterListItemView) convertView;
        }

        String name = item.filterName;
        int resource = item.resource;
        vItem.setValues(name, resource);

        if (item.isSelected) {
            vItem.setBackgroundColor(Color.BLUE);
        } else {
            vItem.setBackgroundColor(Color.BLACK);
        }

        return vItem;
    }


    public void setItemSelected(int position) {
        if (currentSelectedItem > -1) {
            getItem(currentSelectedItem).isSelected = false;
        }
        getItem(position).isSelected = true;
        currentSelectedItem = position;
    }

    public FilterListAdapter(Context context, ArrayList<FilterItem> items) {
        super(context, R.layout.simple_list_item_1, items);
        this.context = context;
        this.items = items;
    }

    public int getCurrentSelectedItem() {
        if (currentSelectedItem != -1)
            return currentSelectedItem;
        else return 0;
    }

    public FilterItem getItemByName(String name) {
        int size = this.getCount();

        for (int i = 0; i < size; i++) {
            FilterItem temp = this.getItem(i);

            if (temp.filterName.equals(name)) {
                return temp;
            }

        }
        return null;
    }

}
