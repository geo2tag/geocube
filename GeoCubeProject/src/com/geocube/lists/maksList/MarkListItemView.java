package com.geocube.lists.maksList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import com.geocube.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MarkListItemView extends TableLayout {
    private TextView mark_title;
    private TextView mark_description;
    private TextView mark_link;
    private TextView mark_coordinates;
    private TextView mark_date;
    private ImageView image;

    public MarkListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setValues(String title, String description, String link, String coord, String date) {
        setupViews();

        mark_title.setText(title);
        mark_description.setText(description);
        mark_link.setText(link);
        mark_coordinates.setText(coord);
        mark_date.setText(date);

        if (link != null && !link.equals("")) {
            try {
                URL url = new URL(link);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                image.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public void setupViews() {
       if (mark_title == null) {
           mark_title = (TextView) findViewById(R.id.markTitle);
       }

        if (mark_description == null) {
            mark_description = (TextView) findViewById(R.id.markDesc);
        }

        if (mark_link == null) {
            mark_link = (TextView) findViewById(R.id.markLink);
        }

        if (mark_coordinates == null) {
            mark_coordinates = (TextView) findViewById(R.id.markCoord);
        }

        if (mark_date == null) {
            mark_date = (TextView) findViewById(R.id.markDate);
        }

        if (image == null) {
            image = (ImageView) findViewById(R.id.image);
        }
    }




}
