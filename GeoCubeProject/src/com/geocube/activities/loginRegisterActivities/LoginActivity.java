package com.geocube.activities.loginRegisterActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.geocube.ProjectConstants;
import com.geocube.R;
import com.geocube.activities.mainUserActivities.MainUserActivity;
import org.json.JSONObject;
import ru.spb.osll.json.JsonLoginRequest;
import ru.spb.osll.json.JsonLoginResponse;

import java.util.UUID;

public class LoginActivity extends Activity {
    private EditText loginText;
    private EditText passText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.login_activity);
        initViews();
    }

    public void initViews() {
      loginText = (EditText) findViewById(R.id.loginText);
      passText = (EditText) findViewById(R.id.passText);
      loginButton = (Button) findViewById(R.id.loginButton);

      loginButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String login = loginText.getText().toString();
              String pass = passText.getText().toString();

              StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
              StrictMode.setThreadPolicy(tp);

              JsonLoginRequest loginReques = new JsonLoginRequest(login, pass, ProjectConstants.SERVER_URL);
              JSONObject r = loginReques.doRequest();
              JsonLoginResponse responce = new JsonLoginResponse();

              if (r != null) {
                  responce.parseJson(r);
                  String authToken = responce.getAuthString();

                  MainUserActivity.setAuthToken(authToken);
                  MainUserActivity.setServerAvaliable(true);
              } else {
                  MainUserActivity.setAuthToken(UUID.randomUUID().toString());
                  MainUserActivity.setServerAvaliable(false);
              }

              Intent in = new Intent(LoginActivity.this, MainUserActivity.class);
              startActivity(in);
          }
      });
    }

}
