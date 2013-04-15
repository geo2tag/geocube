package com.geocube.graphics.cylinder;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class PointsTags {
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;

    private float vertices[];

    private float colors[] = {
            0.2f,  1.0f,  0.0f,  0.5f,
            0.2f,  1.0f,  0.0f,  0.5f,
            0.2f,  1.0f,  0.0f,  0.5f,
            0.2f,  1.0f,  0.0f,  0.5f,
            0.2f,  1.0f,  0.0f,  0.5f,
            0.2f,  1.0f,  0.0f,  0.5f,
            0.2f,  1.0f,  0.0f,  0.5f,
            0.2f,  1.0f,  0.0f,  0.5f,
    };

    private final byte indices[] = {
            0, 1, 2, 3, 4, 5,
            6, 7
    };

    public PointsTags(double x, double y, double z, double x1, double y1, double z1) {
        double h = (y1 - y);
        double w = (x1 - x);
        double d = (z1 - z) / 10;

        vertices = new float[]
                {
                        (float) (-1.0f * w / 4.0f), (float) (-1.0f * h / 4.0), (float) (-1.0f * d / 4.0),
                        (float) (1.0f * w / 4.0f), (float) (-1.0f * h / 4.0), (float) (-1.0f * d / 4.0),
                        (float) (1.0f * w / 4.0f), (float) (1.0f * h / 4.0), (float) (-1.0f * d / 4.0),
                        (float) (-1.0f * w / 4.0f), (float) (1.0f * h / 4.0), (float) (-1.0f * d / 4.0),
                        (float) (-1.0f * w / 4.0f), (float) (-1.0f * h / 4.0), (float) (1.0f * d / 4.0),
                        (float) (1.0f * w / 4.0f), (float) (-1.0f * h / 4.0), (float) (1.0f * d / 4.0),
                        (float) (1.0f * w / 4.0f), (float) (1.0f * h / 4.0), (float) (1.0f * d / 4.0),
                        (float) (-1.0f * w / 4.0f), (float) (1.0f * h / 4.0), (float) (1.0f * d / 4.0)
                };

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mColorBuffer = byteBuf.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CW);

        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_BLEND );

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glPointSize(10);
        gl.glDrawElements(GL10.GL_POINTS, 8, GL10.GL_UNSIGNED_BYTE,
                mIndexBuffer);


        //creates or uses a named texture at index 0
        gl.glBindTexture(GL10.GL_TEXTURE_2D,0);
//sets the texture environment.
        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_COLOR, GL10.GL_BLEND);

// setup texture parameters
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

//creates a Paint object
        Paint yellowPaint = new Paint();
//makes it yellow
        yellowPaint.setColor(Color.YELLOW);
//sets the anti-aliasing for texts
        yellowPaint.setAntiAlias(true);
//creates a new mutable bitmap, with 128px of width and height
        Bitmap textBitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
//creates a new canvas that will draw into a bitmap instead of rendering into the screen
        Canvas bitmapCanvas = new Canvas(textBitmap);

//draws a text at x=20px and y=20px in the canvas
        bitmapCanvas.drawText("41Post.com", 20, 20, yellowPaint);
//any other Canvas drawing methods goes here...
        //.
        //.
        //.

//assigns the OpenGL texture with the Bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textBitmap, 0);
//free memory resources associated with this texture
        textBitmap.recycle();

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);


    }
}