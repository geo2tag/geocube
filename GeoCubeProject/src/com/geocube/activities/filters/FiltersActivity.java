package com.geocube.activities.filters;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.geocube.ProjectConstants;
import com.geocube.R;
import com.geocube.graphics.cube.CubeActivity;
import com.geocube.graphics.cylinder.CylinderActivity;
import com.geocube.graphics.cylinder.CylinderGLRenderer;
import com.geocube.lists.filtersList.FilterItem;
import com.geocube.lists.filtersList.FilterListAdapter;
import com.geocube.location.MyLocationManager;
import org.json.JSONObject;
import ru.spb.osll.json.JsonFilterBoxRequest;

import java.util.ArrayList;
import java.util.Date;

public class FiltersActivity extends Activity {
    private ArrayList<FilterItem> filterItems;
    private ListView filtersList;
    private FilterListAdapter mArrayAdapter;
    private Button okFilter;
    private Location location;

    private static String authToken;
    double latitude;
    double longitude;
    double radius = 500;

    double deltaX = 30;
    double deltaY = 30;

    String timeFrom = ProjectConstants.TIME_FROM;
    String timeTo = new Date().toString();


    public static void setAuthToken(String authToken) {
        FiltersActivity.authToken = authToken;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.filters_activity);

        location = MyLocationManager.getLocation(this);
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        initViews();
    }

    private View lastSelectedView = null;

    public void clearSelection()
    {
        if(lastSelectedView != null) lastSelectedView.setBackgroundColor(Color.BLACK);
    }

    private void initViews() {
        filtersList = (ListView) findViewById(R.id.filtersList);

        filtersList.setFocusableInTouchMode(true);
        filtersList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        filtersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                clearSelection();
                lastSelectedView = myView;

                myView.setBackgroundColor(Color.BLUE);
                ((FilterListAdapter) myAdapter.getAdapter()).setItemSelected(myItemInt);
            }
        });


        filterItems = new ArrayList<FilterItem>();


        FilterItem itemCylinder = new FilterItem("Cylinder", R.drawable.cylinder_icon);
        FilterItem itemBox = new FilterItem("Box", R.drawable.cube_icon);
        FilterItem itemCircle = new FilterItem("Circle", R.drawable.circle_icon);
        FilterItem itemRectangle = new FilterItem("Rectangle", R.drawable.rectangle_icon);
        filterItems.add(itemCylinder);
        filterItems.add(itemBox);
        filterItems.add(itemCircle);
        filterItems.add(itemRectangle);


        mArrayAdapter = new FilterListAdapter(this, filterItems);
        filtersList.setAdapter(mArrayAdapter);
        filtersList.setTextFilterEnabled(true);
        mArrayAdapter.setNotifyOnChange(true);

        okFilter = (Button) findViewById(R.id.okFilter);

        okFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterItem selectedItem = filterItems.get(mArrayAdapter.getCurrentSelectedItem());
                double x = latitude - deltaX;
                double x1 = latitude + deltaX;
                double y = longitude - deltaY;
                double y1 = longitude + deltaY;
                double z = location.getAltitude();
                double z1 = z + radius;

                if (selectedItem.filterName.equals("Box")) {
                    JsonFilterBoxRequest req = new JsonFilterBoxRequest(authToken, x, x1, y, y1, z, z1, timeFrom, timeTo, ProjectConstants.SERVER_URL);
                    JSONObject ob = req.doRequest();

                    CubeActivity.setCoordinates(x, x1, y, y1, z, z1);

                    Intent in = new Intent(FiltersActivity.this, CubeActivity.class);
                    startActivity(in);
                } else if (selectedItem.filterName.equals("Cylinder")) {
                    CylinderGLRenderer.setCoordinateForCylinder(radius / 2.0f, radius);
                    CylinderGLRenderer.setCoordinatesForPoints(x, y, z, x1, y1, z1);

                    Intent in = new Intent(FiltersActivity.this, CylinderActivity.class);
                    startActivity(in);
                } else if (selectedItem.filterName.equals("Circle")) {

                } else if (selectedItem.filterName.equals("Rectangle")) {

                }
            }
        });

    }

}
