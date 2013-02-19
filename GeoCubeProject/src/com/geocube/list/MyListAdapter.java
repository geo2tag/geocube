package com.geocube.list;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.geocube.list.ChannelItem;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<ChannelItem> {

    private Context context;
    private int currentSelectedItem = -1;

    private ArrayList<ChannelItem> items;

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        final ChannelItem item = getItem(position);
        MyListItemView vItem;

        ArrayList<MyListItem> l;

        if (convertView == null) {
            vItem = (MyListItemView) View.inflate(context, com.geocube.R.layout.my_list_item_view, null);
        } else {
            vItem = (MyListItemView) convertView;
        }

        String name = item.getName();
        String desc = item.getDescriotion();

        vItem.setValues(name, desc);

        if (item.isSelected) {
            vItem.setBackgroundColor(Color.parseColor("#38e1ff"));
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

    public MyListAdapter(Context context, ArrayList<ChannelItem> items) {
        super(context, R.layout.simple_list_item_1, items);
        this.context = context;
        this.items = items;
    }

    public int getCurrentSelectedItem() {
        if (currentSelectedItem != -1)
            return currentSelectedItem;
        else return 0;
    }

    public ChannelItem getItemByName(String name) {
        int size = this.getCount();

        for (int i = 0; i < size; i++) {
            ChannelItem temp = this.getItem(i);

            if (temp.getName().equals(name)) {
                return temp;
            }

        }
        return null;
    }

}
