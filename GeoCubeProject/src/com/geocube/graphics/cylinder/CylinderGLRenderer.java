package com.geocube.graphics.cylinder;

import android.opengl.GLSurfaceView;
import android.util.Log;
import com.geocube.graphics.Touch;
import com.geocube.graphics.cube.PointsTags;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class CylinderGLRenderer implements GLSurfaceView.Renderer {
    private MyCylinder mCylinder;
    private PointsTags mPoints;

    private float mCubeRotation;
    private float mTotalTrans;

    private Touch touch;

    private static double height;
    private static double radius;

    private static double x;
    private static double y;
    private static double z;
    private static double x1;
    private static double y1;
    private static double z1;

    public static void setCoordinatesForPoints(double x, double y, double z, double x1, double y1, double z1) {
      CylinderGLRenderer.x = x;
      CylinderGLRenderer.y = y;
      CylinderGLRenderer.z = z;
      CylinderGLRenderer.x1 = x1;
      CylinderGLRenderer.y1 = y1;
      CylinderGLRenderer.z1 = z1;

    }

    public static void setCoordinateForCylinder(double radius, double height) {
        CylinderGLRenderer.height = height;
        CylinderGLRenderer.radius = radius;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mCylinder = new MyCylinder(radius, height);
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
        final float aspect = (float) width / (float) height;
        final float fh = 0.5f;
        final float fw = fh * aspect;
        gl.glFrustumf(-fw, fw, -fh, fh, 1.0f, 250.0f);
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public CylinderGLRenderer(Touch touch) {
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
        mCylinder.draw(gl);

        gl.glLoadIdentity();
        mCubeRotation += touch.getTotalRotation();
    }



}