package glcore.tutorial02;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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
public class Tutorial02 implements GLEventListener {
    
    // Up to 16 attributes per vertex is allowed so any value between 0 and 15 will do.
    private static final int POSITION_ATTRIBUTE_INDEX = 15;
    
    private int triangleId;
    private int quadId;
    private int programId;
    
    public void init(GLAutoDrawable drawable) {
        GL3 gl3 = (GL3) drawable.getGL();
        createTriangle(gl3);
        createQuad(gl3);
        createSimpleProgram(gl3);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl3 = (GL3) drawable.getGL();
        gl3.glViewport(0, 0, width, height);
    }

    public void display(GLAutoDrawable drawable) {
        GL3 gl3 = (GL3) drawable.getGL();
        gl3.glUseProgram(programId); // tells OpenGL which shader to use for rendering
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
        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);
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
    
    private void createSimpleProgram(GL3 gl3) {
        int vertexShaderId = gl3.glCreateShader(GL3.GL_VERTEX_SHADER);
        String vertexShaderSource = loadTextResource("shader.vert");
        gl3.glShaderSource(vertexShaderId, 1, new String[] { vertexShaderSource }, new int[] { vertexShaderSource.length() }, 0);
        gl3.glCompileShader(vertexShaderId);
        
        int fragmentShaderId = gl3.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        String fragmentShaderSource = loadTextResource("shader.frag");
        gl3.glShaderSource(fragmentShaderId, 1, new String[] { fragmentShaderSource }, new int[] { fragmentShaderSource.length() }, 0);
        gl3.glCompileShader(fragmentShaderId);

        checkShaderStatus(gl3, vertexShaderId);
        checkShaderStatus(gl3, fragmentShaderId);
        
        programId = gl3.glCreateProgram();
        gl3.glAttachShader(programId, vertexShaderId);
        gl3.glAttachShader(programId, fragmentShaderId);
        gl3.glBindAttribLocation(programId, POSITION_ATTRIBUTE_INDEX, "inPosition");
        gl3.glLinkProgram(programId);
        checkProgramLinkStatus(gl3, programId);
    }

    /**
     * Checks the compilation status for a shader and displays the log if a failure occurred.
     */
    private void checkShaderStatus(GL3 gl3, int shaderId) {
        int[] params = new int[1];
        gl3.glGetShaderiv(shaderId, GL3.GL_COMPILE_STATUS, params, 0);
        if (params[0] == GL3.GL_FALSE) {
            gl3.glGetShaderiv(shaderId, GL3.GL_INFO_LOG_LENGTH, params, 0);
            System.err.println("Shader compilation failed...");
            byte[] bytes = new byte[8192];
            int[] length = new int[1];
            gl3.glGetShaderInfoLog(shaderId, 8192, length, 0, bytes, 0);
            System.err.println(new String(bytes, 0, length[0]));
        }
    }
    
    /**
     * Checks the link status for a program and displays the log if a failure occurred.
     */
    private void checkProgramLinkStatus(GL3 gl3, int programId) {
        int[] params = new int[1];
        gl3.glGetProgramiv(programId, GL3.GL_LINK_STATUS, params, 0);
        if (params[0] == GL3.GL_FALSE) {
            gl3.glGetProgramiv(programId, GL3.GL_INFO_LOG_LENGTH, params, 0);
            System.err.println("Program link failed...");
            byte[] bytes = new byte[8192];
            int[] length = new int[1];
            gl3.glGetProgramInfoLog(programId, 8192, length, 0, bytes, 0);
            System.err.println(new String(bytes, 0, length[0]));
        }
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
        Tutorial02 tutorial = new Tutorial02();
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
