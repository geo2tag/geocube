package com.geocube.cube;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class CubeGLRenderer implements GLSurfaceView.Renderer {
    public float transX = 0;
    public float transY = 0;

    public float scaleX = 1f;
    public float scaleY = 1f;
    private float totalScale = 1f;

    private float totalTransX;
    private float totalTransY;

    private MyCube mCube = new MyCube(2);
    private float mCubeRotation;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
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
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void setTotalTransX(float totalTransX) {
        this.totalTransX = totalTransX;
    }

    public void setTotalTransY(float totalTransY) {
        this.totalTransY = totalTransY;
    }

    public CubeGLRenderer(Context context, final CubeGLSurfaceView mGLView) {
        totalTransY = 0;
        totalTransX = 0;
    }


    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glTranslatef(0.0f, 0.0f, -10.0f);
        gl.glRotatef(mCubeRotation, 1.0f, 1.0f, 1.0f);

        mCube.draw(gl);

        gl.glLoadIdentity();

        mCubeRotation -= 1.0f;

        totalScale *= scaleX;

        if (totalTransY > 1) {
            gl.glTranslatef(0, -0.05f, 0);
            totalTransY -= 0.05f;
            Log.d("ProblemTrans", "totalTransY = " + totalTransY);
        } else if (totalTransY < -1) {
            gl.glTranslatef(0, 0.05f, 0);
            totalTransY += 0.05f;
            Log.d("ProblemTrans", "totalTransY = " + totalTransY);
        } else if (totalTransX > 1) {
            gl.glTranslatef(-0.05f, 0, 0);
            totalTransX -= 0.05f;
            Log.d("ProblemTrans", "totalTransX = " + totalTransX);
        } else if (totalTransX < -1) {
            gl.glTranslatef(0.05f, 0, 0);
            totalTransX += 0.05f;
            Log.d("ProblemTrans", "totalTransX = " + totalTransX);
        } else {
            gl.glTranslatef(transX, transY, 0);
            totalTransX += transX;
            totalTransY += transY;
        }


        if (totalScale < 1.5f && totalScale > 0.5f)
            gl.glScalef(scaleX, scaleY, 0);


    }

}