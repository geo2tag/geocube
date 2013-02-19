package com.geocube.loginRegisterActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.geocube.ProjectConstants;
import com.geocube.R;
import org.json.JSONObject;
import ru.spb.osll.json.JsonRegisterUserReguest;
import ru.spb.osll.json.JsonRegisterUserResponse;

/**
 * Created with IntelliJ IDEA.
 * User: maru
 * Date: 18.02.13
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class RegisterActivity extends Activity {
    private Button register;
    private EditText pass;
    private EditText login;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                JsonRegisterUserReguest regRequest = new JsonRegisterUserReguest(emailString, loginString,
                                    passString, ProjectConstants.SERVER_URL);
                JSONObject ob = regRequest.doRequest();
                JsonRegisterUserResponse resp = new JsonRegisterUserResponse();
                resp.parseJson(ob);

                if (resp.getErrno() != -1) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }  else {
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Error!")
                            .setMessage("Some error")
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
