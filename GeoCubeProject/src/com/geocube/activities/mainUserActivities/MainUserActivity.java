package com.geocube.activities.mainUserActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.geocube.ProjectConstants;
import com.geocube.R;
import com.geocube.activities.filters.FiltersActivity;
import com.geocube.channels.ChannelsActivity;
import com.geocube.location.MyLocationManager;
import org.json.JSONObject;
import org.mixare.MixView;
import org.mixare.data.convert.Geo2TagDataProcessor;
import ru.spb.osll.json.JsonLoadTagsRequest;
import ru.spb.osll.json.JsonLoadTagsResponse;
import ru.spb.osll.objects.Channel;
import ru.spb.osll.objects.Mark;

import java.util.ArrayList;

public class MainUserActivity extends Activity {
    private static String authToken;
    private Button scanChannnels;
    private Button scanFilters;
    private Button liveMode;

    Location location;
    double latitude;
    double longitude;
    double radius = 500;

    private static boolean isServerAvaliable = false;

    public static void setServerAvaliable(boolean serverAvaliable) {
        isServerAvaliable = serverAvaliable;
    }

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

        if (isServerAvaliable) {
            Toast.makeText(this, "Server is avaliable", 3000);
        } else {
            Toast.makeText(this, "Server is not avaliable", 3000);
        }
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
                FiltersActivity.setAuthToken(authToken);

                Intent in = new Intent(MainUserActivity.this, FiltersActivity.class);
                startActivity(in);
            }
        });

        liveMode = (Button) findViewById(R.id.liveModeButton);
        liveMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geo2TagDataProcessor.setAuthToken(authToken);
                Geo2TagDataProcessor.setLatitude(latitude);
                Geo2TagDataProcessor.setLongitude(longitude);
                Geo2TagDataProcessor.setRadius(radius);

                Intent in = new Intent(MainUserActivity.this, MixView.class);
                startActivity(in);
            }
        });

    }

    public void loadAndShowChannels() {

        JsonLoadTagsRequest request = new JsonLoadTagsRequest(authToken, latitude, longitude, radius, ProjectConstants.SERVER_URL);
        JSONObject ob = request.doRequest();
        if (ob != null) {
            JsonLoadTagsResponse resp = new JsonLoadTagsResponse();
            resp.parseJson(ob);

            ArrayList<Channel> c = (ArrayList<Channel>) resp.getChannels();
            ChannelsActivity.setChannels(c);
        } else {
            ArrayList<Channel> c = new ArrayList<Channel>();
            ChannelsActivity.setChannels(c);
        }

        Intent in = new Intent(MainUserActivity.this, ChannelsActivity.class);
        startActivity(in);
    }
}
