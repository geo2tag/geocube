package com.geocube.graphics.cylinder;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class MyCylinder {
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ByteBuffer  mIndexBuffer;

    private float vertices[];
    int NUM_VERTICES = 12;

    private float colors[] = new float[NUM_VERTICES * 4];

    private byte indices[] = new byte[NUM_VERTICES * 3];

    public MyCylinder(double radius, double height) {
        for (int i = 0; i < NUM_VERTICES * 4 - 1; ) {
           colors[i] = 0.5f;
           colors[i+1] = 0.0f;
           colors[i+2] = 0.5f;
           colors[i+3] = 0.5f;
           i = i + 4;
        }

        double R = 30;

        vertices = new float[NUM_VERTICES * 3];

        double delta = 360.0f / (NUM_VERTICES - 1);

        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;

        int k = 3;
        for (int i = 1; i < NUM_VERTICES - 1; i++) {
            float x = (float) (R * Math.cos(delta * (i - 1)));
            float y = (float) (R * Math.sin(delta * (i - 1)));

            vertices[k] = x; vertices[k+1] = y; vertices[k+2] = 0;
            k = k+3;
        }

        vertices[k] = vertices[3]; vertices[k+1] = vertices[4]; vertices[k+2] = 0;

        int i = 0;
        int j = 0;
        while (i < (NUM_VERTICES - 1) * 3) {
            indices[i] = 0;
            indices[i+1] = (byte) (j+1);
            indices[i+2] = (byte) (j+2);
            j = j+1;
            i = i+3;
        }

        indices[i] = 0; indices[i+1] = (byte) (NUM_VERTICES - 1); indices[i+2] = 1;
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

        gl.glDrawElements(GL10.GL_TRIANGLES, (NUM_VERTICES - 1) * 3, GL10.GL_UNSIGNED_BYTE,
                mIndexBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);


    }
}
