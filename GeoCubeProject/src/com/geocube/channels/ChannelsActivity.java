package com.geocube.channels;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.geocube.R;
import com.geocube.lists.channelsList.ChannelItem;
import com.geocube.lists.channelsList.ChannelListAdapter;
import ru.spb.osll.objects.Channel;

import java.util.ArrayList;

public class ChannelsActivity extends Activity {
    private static ArrayList<Channel> channels;
    private ArrayList<ChannelItem> channelItems;
    private ListView channelsList;
    private ChannelListAdapter mArrayAdapter;
    private Button channelMark;

    public static void setChannels(ArrayList<Channel> channels) {
        ChannelsActivity.channels = channels;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.channels_activity);
        initViews();
    }

    private void initViews() {
        channelsList = (ListView) findViewById(R.id.channelsList);

        channelsList.setFocusableInTouchMode(true);
        channelsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        channelsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                myView.setBackgroundColor(Color.BLUE);
                ((ChannelListAdapter) myAdapter.getAdapter()).setItemSelected(myItemInt);
            }
        });


        channelItems = new ArrayList<ChannelItem>();

        for (int i = 0; i < channels.size(); i++) {
           Channel c = channels.get(i);

           if (c.getName() == null || c.getName().equals(""))
               c.setName("Channel " + i);
           ChannelItem item = new ChannelItem(c);
           channelItems.add(item);
        }

        mArrayAdapter = new ChannelListAdapter(this, channelItems);
        channelsList.setAdapter(mArrayAdapter);
        channelsList.setTextFilterEnabled(true);
        mArrayAdapter.setNotifyOnChange(true);

        channelMark = (Button) findViewById(R.id.channelMarks);

        channelMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelItem selectedItem = channelItems.get(mArrayAdapter.getCurrentSelectedItem());

                MarkActivity.setMarks(selectedItem.getChannel().getMarks());

                Intent in = new Intent(ChannelsActivity.this, MarkActivity.class);
                startActivity(in);
            }
        });

    }

    private Activity getActivity() {
        return this;
    }
}
