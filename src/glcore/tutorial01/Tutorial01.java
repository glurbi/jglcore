package glcore.tutorial01;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

/**
 * This first example aims at providing the bare bone code for drawing a triangle and a quad.
 * It doesn't use a shader but instead uses the fixed pipeline provided by the OpenGL 3.1 core profile.
 * According to the spec, there should not be any fixed functionality...
 */
public class Tutorial01 implements GLEventListener {
    
    // the fixed pipeline seems to expect the position at the following index...
    private static final int POSITION_ATTRIBUTE_INDEX = 0;
    
    private int triangleId;
    private int quadId;
    
    public void init(GLAutoDrawable drawable) {
        GL4 gl4 = (GL4) drawable.getGL();
        createTriangle(gl4);
        createQuad(gl4);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl4 = (GL4) drawable.getGL();
        // we want to draw on the entire window
        gl4.glViewport(0, 0, width, height);
    }

    public void display(GLAutoDrawable drawable) {
        GL4 gl4 = (GL4) drawable.getGL();
        gl4.glClear(GL4.GL_COLOR_BUFFER_BIT);
        renderTriangle(gl4);
        renderQuad(gl4);
        gl4.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }
    
    private void createTriangle(GL4 gl4) {
        int size = 4*3*3; // 4 bytes per float, 3 vertices per triangle, 3 coordinates per vertex
        FloatBuffer buf = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buf.put(new float[] {
                0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f });
        buf.flip();
        int[] buffers = new int[1]; 
        gl4.glGenBuffers(1, buffers, 0);
        gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers[0]);
        gl4.glBufferData(GL4.GL_ARRAY_BUFFER, size, buf, GL4.GL_STATIC_DRAW);
        triangleId = buffers[0];
    }
    
    private void createQuad(GL4 gl4) {
        // OpenGL 3.1 core profile does not support quads, so we have to create 2 triangles
        int size = 4*6*3;
        FloatBuffer buf = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buf.put(new float[] {
                0.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                -1.0f, 0.0f, 0.0f});
        buf.flip();
        int[] buffers = new int[1]; 
        gl4.glGenBuffers(1, buffers, 0);
        gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers[0]);
        gl4.glBufferData(GL4.GL_ARRAY_BUFFER, size, buf, GL4.GL_STATIC_DRAW);
        quadId = buffers[0];
    }
    
    private void renderTriangle(GL4 gl4) {
        gl4.glEnableVertexAttribArray(POSITION_ATTRIBUTE_INDEX);
        gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, triangleId);
        gl4.glVertexAttribPointer(POSITION_ATTRIBUTE_INDEX, 3, GL4.GL_FLOAT, false, 0, 0);
        gl4.glDrawArrays(GL4.GL_TRIANGLES, 0, 3);
        gl4.glDisableVertexAttribArray(POSITION_ATTRIBUTE_INDEX);
    }
    
    private void renderQuad(GL4 gl4) {
        gl4.glEnableVertexAttribArray(POSITION_ATTRIBUTE_INDEX);
        gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, quadId);
        gl4.glVertexAttribPointer(POSITION_ATTRIBUTE_INDEX, 3, GL4.GL_FLOAT, false, 0, 0);
        gl4.glDrawArrays(GL4.GL_TRIANGLES, 0, 6);
        gl4.glDisableVertexAttribArray(POSITION_ATTRIBUTE_INDEX);
    }
    
    public static void main(String[] args) {
        Tutorial01 tutorial = new Tutorial01();
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        final GLProfile profile = GLProfile.get(GLProfile.GL4);
    	GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(tutorial);
        frame.add(canvas);
        frame.setSize(300, 300);
        frame.setVisible(true);
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    canvas.display();
                    Thread.yield();
                }
            }
        }).start();
    }

}
