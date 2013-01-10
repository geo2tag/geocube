package com.geocube.cube;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;


public class CubeActivity extends Activity {
    private static CubeGLSurfaceView mGLView;

    private float x;
    private float y;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        mGLView = new CubeGLSurfaceView(this);
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
