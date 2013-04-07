package com.geocube.graphics.cube;

import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class CubeGLRenderer implements GLSurfaceView.Renderer {
    private static double x;
    private static double x1;
    private static double y;
    private static double y1;
    private static double z;
    private static double z1;

    private MyCube mCube;
    private PointsTags mPoints;

    private float mCubeRotation;
    private float mTotalTrans;

    private Touch touch;

    public static void setCoordinates(double x, double x1, double y, double y1, double z, double z1) {
        CubeGLRenderer.x = x;
        CubeGLRenderer.x1 = x1;
        CubeGLRenderer.y = y;
        CubeGLRenderer.y1 = y1;
        CubeGLRenderer.z = z;
        CubeGLRenderer.z1 = z1;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mCube = new MyCube(x, y, z, x1, y1, z1);
        mPoints = new PointsTags(x, y, z, x1, y1, z1);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_NICEST);

    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
//        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 200.0f);
        final float aspect = (float) width / (float) height;
        final float fh = 0.5f;
        final float fw = fh * aspect;
        gl.glFrustumf(-fw, fw, -fh, fh, 1.0f, 250.0f);
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public CubeGLRenderer(Touch touch) {
       this.touch = touch;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT |
                GL10.GL_DEPTH_BUFFER_BIT);

        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_BLEND );

        gl.glLoadIdentity();
        float recievedTrans = touch.getTotalTrans();
        mTotalTrans = -150.0f + recievedTrans;
        gl.glTranslatef(0.0f, 0.0f, mTotalTrans);
        Log.d("Translation: ", String.valueOf(mTotalTrans));
        Log.d("Recieved trans: ", String.valueOf(recievedTrans));

        gl.glRotatef(mCubeRotation, 1.0f, 1.0f, 1.0f);

        mPoints.draw(gl);
        mCube.draw(gl);

        gl.glLoadIdentity();
        mCubeRotation -= 1f;
    }



}