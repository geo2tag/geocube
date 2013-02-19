package com.geocube.mainUserActivities;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.geocube.ProjectConstants;
import com.geocube.R;
import com.geocube.list.MyListAdapter;
import com.geocube.list.MyListItem;
import com.geocube.location.MyLocationManager;
import org.json.JSONObject;
import ru.spb.osll.json.JsonAvailableChannelRequest;
import ru.spb.osll.json.JsonAvailableChannelResponse;
import ru.spb.osll.json.JsonLoadTagsRequest;
import ru.spb.osll.json.JsonLoadTagsResponse;
import ru.spb.osll.objects.Channel;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maru
 * Date: 18.02.13
 * Time: 14:19
 * To change this template use File | Settings | File Templates.
 */
public class MainUserActivity extends Activity {
    private static String authToken;
    private ListView mListView;
    private Button scanChannnels;
    private MyListAdapter mArrayAdapter;
    private ArrayList list;

    public static void setAuthToken(String authToken) {
        MainUserActivity.authToken = authToken;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Auth token: ", authToken);
        setContentView(R.layout.main_user_activity);
        initViews();
    }

    public void initViews() {
        mListView = (ListView) findViewById(R.id.mListView);
        scanChannnels = (Button) findViewById(R.id.channelButton);

        mListView.setFocusableInTouchMode(true);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                myView.setBackgroundColor(Color.RED);
                ((MyListAdapter) myAdapter.getAdapter()).setItemSelected(myItemInt);
            }
        });

        list = new ArrayList<MyListItem>();
        mArrayAdapter = new MyListAdapter(this, list);
        mListView.setAdapter(mArrayAdapter);
        mListView.setTextFilterEnabled(true);
        mArrayAdapter.setNotifyOnChange(true);

        scanChannnels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAndShowChannels();
            }
        });
    }

    public void loadAndShowChannels() {
//        JsonAvailableChannelRequest request = new JsonAvailableChannelRequest(authToken, ProjectConstants.SERVER_URL);
//        JSONObject ob = request.doRequest();
//        JsonAvailableChannelResponse resp = new JsonAvailableChannelResponse();
//        resp.parseJson(ob);
//
//        List<Channel> channels = resp.getChannels();
//
//        Iterator iterator = channels.iterator();

        Location location = MyLocationManager.getLocation(this);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double radius = 5000;

        JsonLoadTagsRequest request = new JsonLoadTagsRequest(authToken, latitude, longitude, radius, ProjectConstants.SERVER_URL);
        JSONObject ob = request.doRequest();
        JsonLoadTagsResponse resp = new JsonLoadTagsResponse();
        resp.parseJson(ob);

        ArrayList<Channel> c = (ArrayList<Channel>) resp.getChannels();
    }
}
