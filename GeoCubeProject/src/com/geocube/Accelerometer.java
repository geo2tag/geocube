package com.geocube;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: Мару
 * Date: 14.12.12
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */

public class Accelerometer implements SensorEventListener {
    private static TextView textView;

    public static void setTextView(TextView textView) {
        Accelerometer.textView = textView;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            textView.setText(String.format("%.2f, %.2f, %.2f", x, y, z));
        }

    }
}