package com.geocube.activities.loginRegisterActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.geocube.ProjectConstants;
import com.geocube.R;
import com.geocube.constants.ErrorMessages;
import org.json.JSONObject;
import ru.spb.osll.json.JsonRegisterUserReguest;
import ru.spb.osll.json.JsonRegisterUserResponse;

public class RegisterActivity extends Activity {
    private Button register;
    private EditText pass;
    private EditText login;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.register_activity);
        initViews();
    }

    public void initViews() {
        register = (Button) findViewById(R.id.registerButton);
        pass = (EditText) findViewById(R.id.passText);
        login = (EditText) findViewById(R.id.loginText);
        email = (EditText) findViewById(R.id.emailText);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString();
                String passString = pass.getText().toString();
                String loginString = login.getText().toString();

                StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
                StrictMode.setThreadPolicy(tp);

                JsonRegisterUserReguest regRequest = new JsonRegisterUserReguest(emailString, loginString,
                                    passString, ProjectConstants.SERVER_URL);
                JSONObject ob = regRequest.doRequest();
                JsonRegisterUserResponse resp = new JsonRegisterUserResponse();
                resp.parseJson(ob);


                if (resp.getErrno() == 0) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }  else {
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Error!")
                            .setMessage(ErrorMessages.getInstance().get(resp.getErrno()))
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            }).show();
                }

            }
        });
    }
}
