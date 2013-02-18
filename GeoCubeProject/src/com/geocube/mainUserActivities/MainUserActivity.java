package com.geocube.mainUserActivities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.geocube.R;

/**
 * Created with IntelliJ IDEA.
 * User: maru
 * Date: 18.02.13
 * Time: 14:19
 * To change this template use File | Settings | File Templates.
 */
public class MainUserActivity extends Activity {
    private static String authToken;

    public static void setAuthToken(String authToken) {
        MainUserActivity.authToken = authToken;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Auth token: ", authToken);
        setContentView(R.layout.main_user_activity);
    }
}
