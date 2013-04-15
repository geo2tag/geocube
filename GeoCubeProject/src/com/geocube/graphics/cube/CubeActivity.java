package com.geocube.graphics.cube;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import com.geocube.graphics.Touch;


public class CubeActivity extends Activity {
    private static GLSurfaceView mGLView;

    public static void setCoordinates(double x, double x1, double y, double y1, double z, double z1) {
        CubeGLRenderer.setCoordinates(x, x1, y, y1, z, z1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        mGLView = new GLSurfaceView(this);
        Touch touch = new Touch();
        mGLView.setRenderer(new CubeGLRenderer(touch));
        mGLView.setOnTouchListener(touch);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }
}
