package com.geocube.cube;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;

import static android.view.MotionEvent.*;

public class CubeGLSurfaceView extends GLSurfaceView {
    private CubeGLRenderer mRenderer;
    float startX;
    float startY;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    float oldDist;
    float newDist;

    float scale = 1f;

    float x;
    float y;

    public CubeGLSurfaceView(Context context) {
        super(context);
        mRenderer = new CubeGLRenderer(context, this);
        mRenderer.setTotalTransX(0);
        mRenderer.setTotalTransY(0);

        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                mode = DRAG;
                break;

            case ACTION_POINTER_UP:
                mode = NONE;
                break;

            case ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                mode = ZOOM;
                break;

            case ACTION_MOVE:
                if (mode == DRAG) {
                    float dX = event.getX() - startX;
                    float dY = startY - event.getY();

                    if (Math.abs(dX) > 0.5 && Math.abs(dY) > 0.5) {
                        if (mRenderer != null) {
                            mRenderer.transX = 0.005f * dX;
                            mRenderer.transY = 0.005f * dY;

                            mRenderer.scaleX = 1;
                            mRenderer.scaleY = 1;
                        }
                        requestRender();
                        startX = event.getX();
                        startY = event.getY();
                    }
                }

                if (mode == ZOOM) {
                    newDist = spacing(event);

                    if (newDist < oldDist && Math.abs(newDist - oldDist) > 0.8f) {
                        scale = scale - newDist * 0.0001f;

                        if (scale > 0.8f && scale < 1.2f) {
                            if (mRenderer != null) {
                                mRenderer.scaleX = scale;
                                mRenderer.scaleY = scale;

                                mRenderer.transX = mRenderer.transY = 0;

                                Log.d("Scaling", String.valueOf(scale));
                                requestRender();
                            }
                        }

                        scale = 1f;
                    } else if (newDist > oldDist && Math.abs(newDist - oldDist) > 0.8f) {
                        scale = scale + newDist * 0.0001f;

                        if (scale < 1.2f && scale > 0.8f) {
                            if (mRenderer != null) {
                                mRenderer.scaleX = scale;
                                mRenderer.scaleY = scale;

                                mRenderer.transX = mRenderer.transY = 0;

                                Log.d("Scaling", String.valueOf(scale));
                                requestRender();
                            }
                        }

                        scale = 1f;
                    }

                }

                break;
        }
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
