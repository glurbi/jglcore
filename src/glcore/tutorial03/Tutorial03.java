package glcore.tutorial03;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

/**
 * Draws a triangle and quad using a minimalistic vertex and fragment shader.
 */
public class Tutorial03 implements GLEventListener {
    
    // Up to 16 attributes per vertex is allowed so any value between 0 and 15 will do.
    private static final int POSITION_ATTRIBUTE_INDEX = 12;
    
    private Program program;
    private int triangleId;
    private int quadId;
    
    public void init(GLAutoDrawable drawable) {
        GL3 gl3 = (GL3) drawable.getGL();
        createTriangle(gl3);
        createQuad(gl3);
        createProgram(gl3);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl3 = (GL3) drawable.getGL();
        gl3.glViewport(0, 0, width, height);
    }

    public void display(GLAutoDrawable drawable) {
        GL3 gl3 = (GL3) drawable.getGL();
        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);
        // tells OpenGL which shader program to use for rendering
        program.use(gl3); 
        renderTriangle(gl3);
        renderQuad(gl3);
        gl3.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }
    
    private void createTriangle(GL3 gl3) {
        int size = 4*3*3;
        FloatBuffer buf = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buf.put(new float[] {
                0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f });
        buf.flip();
        int[] buffers = new int[1]; 
        gl3.glGenBuffers(1, buffers, 0);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[0]);
        gl3.glBufferData(GL3.GL_ARRAY_BUFFER, size, buf, GL3.GL_STATIC_DRAW);
        triangleId = buffers[0];
    }
    
    private void createQuad(GL3 gl3) {
        int size = 4*6*3;
        FloatBuffer buf = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
        // coordinates have to be between -1.0f and 1.0f
        // changing the z component doesn't change the rendered image
        buf.put(new float[] {
                0.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                -1.0f, 0.0f, 0.0f});
        buf.flip();
        int[] buffers = new int[1]; 
        gl3.glGenBuffers(1, buffers, 0);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[0]);
        gl3.glBufferData(GL3.GL_ARRAY_BUFFER, size, buf, GL3.GL_STATIC_DRAW);
        quadId = buffers[0];
    }
    
    private void renderTriangle(GL3 gl3) {
        gl3.glEnableVertexAttribArray(POSITION_ATTRIBUTE_INDEX);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, triangleId);
        gl3.glVertexAttribPointer(POSITION_ATTRIBUTE_INDEX, 3, GL3.GL_FLOAT, false, 0, 0);
        gl3.glDrawArrays(GL3.GL_TRIANGLES, 0, 3);
        gl3.glDisableVertexAttribArray(POSITION_ATTRIBUTE_INDEX);
    }
    
    private void renderQuad(GL3 gl3) {
        gl3.glEnableVertexAttribArray(POSITION_ATTRIBUTE_INDEX);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, quadId);
        gl3.glVertexAttribPointer(POSITION_ATTRIBUTE_INDEX, 3, GL3.GL_FLOAT, false, 0, 0);
        gl3.glDrawArrays(GL3.GL_TRIANGLES, 0, 6);
        gl3.glDisableVertexAttribArray(POSITION_ATTRIBUTE_INDEX);
    }
    
    private void createProgram(GL3 gl3) {
        ProgramBuilder builder = new ProgramBuilder();
        builder.setVertexShaderSource(loadTextResource("shader.vert"));
        builder.setFragmentShaderSource(loadTextResource("shader.frag"));
        Map<Integer, String> attributes = new HashMap<Integer, String>();
        attributes.put(POSITION_ATTRIBUTE_INDEX, "inPosition");
        builder.setAttributes(attributes);
        program = builder.build(gl3);
    }
    
    private String loadTextResource(String name) {
        try {
            InputStream is = getClass().getResourceAsStream(name);
            byte[] bytes = new byte[8192];
            int len = is.read(bytes);
            return new String(bytes, 0, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
        Tutorial03 tutorial = new Tutorial03();
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        final GLProfile profile = GLProfile.get(GLProfile.GL3);
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
