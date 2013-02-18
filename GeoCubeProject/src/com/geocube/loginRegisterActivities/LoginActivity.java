package com.geocube.loginRegisterActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.geocube.ProjectConstants;
import com.geocube.R;
import com.geocube.mainUserActivities.MainUserActivity;
import org.json.JSONException;
import org.json.JSONObject;
import ru.spb.osll.json.JsonLoginRequest;
import ru.spb.osll.json.JsonLoginResponse;

/**
 * Created with IntelliJ IDEA.
 * User: maru
 * Date: 18.02.13
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends Activity {
    private EditText loginText;
    private EditText passText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

              JsonLoginRequest loginReques = new JsonLoginRequest(login, pass, ProjectConstants.SERVER_URL);
              JSONObject r = loginReques.doRequest();
              JsonLoginResponse responce = new JsonLoginResponse();
              responce.parseJson(r);
              String authToken = responce.getAuthString();

              MainUserActivity.setAuthToken(authToken);

              Intent in = new Intent(LoginActivity.this, MainUserActivity.class);
              startActivity(in);
          }
      });
    }

}
