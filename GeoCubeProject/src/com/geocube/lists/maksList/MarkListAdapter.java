package com.geocube.lists.maksList;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import ru.spb.osll.objects.Mark;

import java.util.ArrayList;

public class MarkListAdapter extends ArrayAdapter<MarkItem> {

    private Context context;
    private int currentSelectedItem = -1;

    private ArrayList<MarkItem> items;

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        final MarkItem item = getItem(position);
        MarkListItemView vItem;

        ArrayList<MarkListItem> l;

        if (convertView == null) {
            vItem = (MarkListItemView) View.inflate(context, com.geocube.R.layout.my_list_item_view_marks, null);
        } else {
            vItem = (MarkListItemView) convertView;
        }


        Mark mark = item.getMark();
        vItem.setValues(mark.getTitle(), mark.getDescription(), mark.getLink(), mark.getLatitude() + ", " + mark.getLatitude(), mark.getTime());

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

    public MarkListAdapter(Context context, ArrayList<MarkItem> items) {
        super(context, R.layout.simple_list_item_1, items);
        this.context = context;
        this.items = items;
    }

    public int getCurrentSelectedItem() {
        if (currentSelectedItem != -1)
            return currentSelectedItem;
        else return 0;
    }

    public MarkItem getItemByName(String name) {
        int size = this.getCount();

        for (int i = 0; i < size; i++) {
            MarkItem temp = this.getItem(i);

            if (temp.getMark().getTitle().equals(name)) {
                return temp;
            }

        }
        return null;
    }

}
