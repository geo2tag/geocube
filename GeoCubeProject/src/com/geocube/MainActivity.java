package com.geocube;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {
    TextView geoCoord;
    TextView deviceOrient;
    SensorManager mSensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
    }
}
