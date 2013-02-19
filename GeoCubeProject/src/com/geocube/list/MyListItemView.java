package com.geocube.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;
import android.widget.TextView;
import com.geocube.R;

public class MyListItemView extends TableLayout {
    private TextView channel_name;
    private TextView channel_description;

    public MyListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setValues(String name, String description) {
        setupViews();

        channel_name.setText(name);
        channel_description.setText(description);
    }


    public void setupViews() {
       if (channel_name == null) {
           channel_name = (TextView) findViewById(R.id.channelName);
       }

       if (channel_description == null) {
          channel_description = (TextView) findViewById(R.id.channelDesc);
       }

    }


}
