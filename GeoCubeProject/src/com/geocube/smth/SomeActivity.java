package com.geocube.smth;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.geocube.R;
import com.geocube.graphics.cube.CubeActivity;
import com.geocube.location.MyLocationManager;

public class SomeActivity extends Activity {
    TextView geoCoord;
    TextView deviceOrient;
    Button seeCube;

    SensorManager mSensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.some_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setupUI();

        Location location = MyLocationManager.getLocation(this);

        if (location != null)
        {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();

            geoCoord.setText("Coordinates:" +
                             "\nLatitude = " + latitude +
                             "\nLongitude = " + longitude +
                             "\nAltitude = " + altitude +
                             "\n");
        } else {
          geoCoord.setText("Can't get location coordinates");
        }

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ALL);

        Accelerometer.setTextView(deviceOrient);
        Accelerometer mAcc = new Accelerometer();
        mSensorManager.registerListener(mAcc, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setupUI() {
      geoCoord = (TextView) findViewById(R.id.geoCoord);
      deviceOrient = (TextView) findViewById(R.id.deviceOrient);
      seeCube = (Button) findViewById(R.id.seeCube);

      seeCube.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent m = new Intent(SomeActivity.this, CubeActivity.class);
              startActivity(m);
          }
      });
    }
}
