package com.geocube.channels;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.geocube.R;
import com.geocube.lists.maksList.MarkItem;
import com.geocube.lists.maksList.MarkListAdapter;
import ru.spb.osll.objects.Mark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maru
 * Date: 06.03.13
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class MarkActivity extends Activity {
    private static List<Mark> marks;
    private ArrayList<MarkItem> markItems;
    private ListView markList;
    private MarkListAdapter mArrayAdapter;


    public static void setMarks(List<Mark> marks) {
        MarkActivity.marks = marks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.mark_activity);
        initViews();
    }

    private void initViews() {
        markList = (ListView) findViewById(R.id.marksList);

        markList.setDivider(getResources().getDrawable(R.drawable.list_divider));
        markList.setDividerHeight(10);
        markList.setFocusableInTouchMode(true);
        markList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        markList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                myView.setBackgroundColor(Color.RED);
                ((MarkListAdapter) myAdapter.getAdapter()).setItemSelected(myItemInt);
            }
        });


        markItems = new ArrayList<MarkItem>();

        for (Mark m : marks) {
            MarkItem item = new MarkItem(m);
            markItems.add(item);
        }

        mArrayAdapter = new MarkListAdapter(this, markItems);
        markList.setAdapter(mArrayAdapter);
        markList.setTextFilterEnabled(true);
        mArrayAdapter.setNotifyOnChange(true);

    }

    private Activity getActivity() {
        return this;
    }
}
