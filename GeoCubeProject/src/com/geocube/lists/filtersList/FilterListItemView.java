package com.geocube.lists.filtersList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import com.geocube.R;

public class FilterListItemView extends TableLayout {
    private TextView filter_name;
    private ImageView filter_icon;

    public FilterListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setValues(String name, int resource) {
        setupViews();

        filter_name.setText(name);
        filter_icon.setImageResource(resource);
    }


    public void setupViews() {
       if (filter_name == null) {
           filter_name = (TextView) findViewById(R.id.filterName);
       }

       if (filter_icon == null) {
          filter_icon = (ImageView) findViewById(R.id.filterIcon);
       }

    }




}
