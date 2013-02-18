package com.geocube.mainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.geocube.R;
import com.geocube.loginRegisterActivities.LoginActivity;
import com.geocube.loginRegisterActivities.RegisterActivity;
import ru.spb.osll.json.JsonLoginRequest;


/**
 * Created with IntelliJ IDEA.
 * User: maru
 * Date: 18.02.13
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */

public class MainStartActivity extends Activity {
    private Button registerButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_start_activity);
        initViews();
    }

    public void initViews() {
        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton = (Button) findViewById(R.id.loginButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  in = new Intent(MainStartActivity.this, RegisterActivity.class);
                startActivity(in);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainStartActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });
    }
}
