package com.geocube.graphics.cube;

import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;


public class Touch implements View.OnTouchListener {

    private final int DRAG = 1;
    private final int ZOOM = 2;
    private final int NONE = 0;

    private int mode = NONE;

    private float trans = 0.1f;
    private float totalTrans = 0.001f;

    public float getTotalTrans() {
        if (totalTrans > -200 && totalTrans < 0.1f)
          return totalTrans;
        else return 0;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float oldDist = 0;
        float newDist = 0;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
              mode = DRAG;
              break;

            case MotionEvent.ACTION_POINTER_UP:
               mode = NONE;
               break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                oldDist = spacing(event);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {

                } else if (mode == ZOOM) {
                  newDist = spacing(event);
                  trans = (oldDist - newDist) / 200;
                  totalTrans += trans;
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



}
