package com.geocube.activities.mainUserActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.geocube.ProjectConstants;
import com.geocube.R;
import com.geocube.channels.ChannelsActivity;
import com.geocube.graphics.cube.CubeActivity;
import com.geocube.location.MyLocationManager;
import org.json.JSONObject;
import ru.spb.osll.json.JsonFilterBoxRequest;
import ru.spb.osll.json.JsonLoadTagsRequest;
import ru.spb.osll.json.JsonLoadTagsResponse;
import ru.spb.osll.objects.Channel;

import java.util.ArrayList;
import java.util.Date;

public class MainUserActivity extends Activity {
    private static String authToken;
    private Button scanChannnels;
    private Button scanFilters;
    Location location;
    double latitude;
    double longitude;
    double radius = 500;

    double deltaX = 30;
    double deltaY = 30;

    String timeFrom = "01 01 1990 22:10:00.111";
    String timeTo = new Date().toString();

    public static void setAuthToken(String authToken) {
        MainUserActivity.authToken = authToken;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        location = MyLocationManager.getLocation(this);
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.d("Auth token: ", authToken);
        setContentView(R.layout.main_user_activity);

        initViews();
    }

    public void initViews() {
        scanChannnels = (Button) findViewById(R.id.channelButton);
        scanChannnels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAndShowChannels();
            }
        });

        scanFilters = (Button) findViewById(R.id.filterButton);
        scanFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double x = latitude - deltaX;
                double x1 = latitude + deltaX;
                double y = longitude - deltaY;
                double y1 = longitude + deltaY;
                double z = location.getAltitude();
                double z1 = z + radius;

                JsonFilterBoxRequest req = new JsonFilterBoxRequest(authToken, x, x1, y, y1, z, z1, timeFrom, timeTo, ProjectConstants.SERVER_URL);
                JSONObject ob = req.doRequest();

                CubeActivity.setCoordinates(x, x1, y, y1, z, z1);

                Intent in = new Intent(MainUserActivity.this, CubeActivity.class);
                startActivity(in);
            }
        });

    }

    public void loadAndShowChannels() {

        JsonLoadTagsRequest request = new JsonLoadTagsRequest(authToken, latitude, longitude, radius, ProjectConstants.SERVER_URL);
        JSONObject ob = request.doRequest();
        JsonLoadTagsResponse resp = new JsonLoadTagsResponse();
        resp.parseJson(ob);

        ArrayList<Channel> c = (ArrayList<Channel>) resp.getChannels();
        ChannelsActivity.setChannels(c);

        Intent in = new Intent(MainUserActivity.this, ChannelsActivity.class);
        startActivity(in);
    }
}
