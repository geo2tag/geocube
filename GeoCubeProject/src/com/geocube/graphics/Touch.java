package com.geocube.graphics;

import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class Touch implements View.OnTouchListener {

    private final int DRAG = 1;
    private final int ZOOM = 2;
    private final int NONE = 0;

    private int mode = NONE;

    private float trans = 0.1f;
    private float totalTrans = 0.001f;
    private float totalRotation = 0.1f;

    float oldDist = 0;
    float newDist;

    float oldRot;
    float newRot;

    public float getTotalTrans() {
          return totalTrans;
    }

    public float getTotalRotation() {
        return totalRotation;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
              mode = DRAG;
              oldRot = event.getX();
              break;

            case MotionEvent.ACTION_POINTER_UP:
               mode = NONE;
               totalRotation = 0;
               break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                oldDist = spacing(event);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                   newRot = event.getX();
                   totalRotation = (newRot - oldRot) / 20.0f;
                   oldRot = newRot;
                } else if (mode == ZOOM) {
                   newDist = spacing(event);

                    Log.d("Spacing: ", "OldDist = " + oldDist + ", newDist = " + newDist);

                   /*zoom out*/
                   if (newDist < oldDist) {
                       trans = (oldDist - newDist) / 200.0f;
                       float probTrans = totalTrans - trans - 150.0f;

                       if (probTrans > -200.0f) {
                           totalTrans -= trans;
                           Log.d("zoomOut-zoomIn", "Zoom out!!! " + "trans: " + trans + ", totalTrans: " + totalTrans);
                       }
                   /*zoom in*/
                   }  else if (newDist > oldDist){
                       trans = (newDist - oldDist) / 200.0f;
                       float probTrans = totalTrans + trans - 150.0f;

                       if (probTrans < 10.0f) {
                           totalTrans += trans;
                           Log.d("zoomOut-zoomIn", "Zoom In!!! " + "trans: " + trans + ", totalTrans: " + totalTrans);
                       }
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



}
