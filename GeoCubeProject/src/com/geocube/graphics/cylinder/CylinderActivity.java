package com.geocube.graphics.cylinder;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import com.geocube.graphics.Touch;


public class CylinderActivity extends Activity {
    private static GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        mGLView = new GLSurfaceView(this);
        Touch touch = new Touch();
        mGLView.setRenderer(new CylinderGLRenderer(touch));
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
