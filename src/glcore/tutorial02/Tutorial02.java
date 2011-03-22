package glcore.tutorial02;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
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
 * This tutorial builds on Tutorial01, and introduces compilation, link and usage
 * of two minimalistic vertex and fragment shaders.
 */
public class Tutorial02 implements GLEventListener {
    
    // Up to 16 attributes per vertex is allowed so any value between 0 and 15 will do.
    private static final int POSITION_ATTRIBUTE_INDEX = 12;
    
    private int triangleId;
    private int quadId;
    private int programId;
    
    public void init(GLAutoDrawable drawable) {
        GL4 gl4 = (GL4) drawable.getGL();
        createTriangle(gl4);
        createQuad(gl4);
        createSimpleProgram(gl4);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl4 = (GL4) drawable.getGL();
        gl4.glViewport(0, 0, width, height);
    }

    public void display(GLAutoDrawable drawable) {
        GL4 gl4 = (GL4) drawable.getGL();
        gl4.glClear(GL4.GL_COLOR_BUFFER_BIT);
        // tells OpenGL which shader program to use for rendering
        gl4.glUseProgram(programId); 
        renderTriangle(gl4);
        renderQuad(gl4);
        gl4.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }
    
    private void createTriangle(GL4 gl4) {
        int size = 4*3*3;
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
    
    private void createSimpleProgram(GL4 gl4) {
        int vertexShaderId = gl4.glCreateShader(GL4.GL_VERTEX_SHADER);
        String vertexShaderSource = loadTextResource("shader.vert");
        gl4.glShaderSource(vertexShaderId, 1, new String[] { vertexShaderSource }, new int[] { vertexShaderSource.length() }, 0);
        gl4.glCompileShader(vertexShaderId);
        checkShaderStatus(gl4, vertexShaderId);
        
        int fragmentShaderId = gl4.glCreateShader(GL4.GL_FRAGMENT_SHADER);
        String fragmentShaderSource = loadTextResource("shader.frag");
        gl4.glShaderSource(fragmentShaderId, 1, new String[] { fragmentShaderSource }, new int[] { fragmentShaderSource.length() }, 0);
        gl4.glCompileShader(fragmentShaderId);
        checkShaderStatus(gl4, fragmentShaderId);
        
        programId = gl4.glCreateProgram();
        gl4.glAttachShader(programId, vertexShaderId);
        gl4.glAttachShader(programId, fragmentShaderId);
        // associates the "inPosition" variable from the vertex shader with the position attribute
        // the variable and the attribute must be bound before the program is linked
        gl4.glBindAttribLocation(programId, POSITION_ATTRIBUTE_INDEX, "inPosition");
        gl4.glLinkProgram(programId);
        checkProgramLinkStatus(gl4, programId);
    }

    /**
     * Checks the compilation status for a shader and displays the log if a failure occurred.
     */
    private void checkShaderStatus(GL4 gl4, int shaderId) {
        int[] params = new int[1];
        gl4.glGetShaderiv(shaderId, GL4.GL_COMPILE_STATUS, params, 0);
        if (params[0] == GL4.GL_FALSE) {
            gl4.glGetShaderiv(shaderId, GL4.GL_INFO_LOG_LENGTH, params, 0);
            System.err.println("Shader compilation failed...");
            byte[] bytes = new byte[8192];
            int[] length = new int[1];
            gl4.glGetShaderInfoLog(shaderId, 8192, length, 0, bytes, 0);
            System.err.println(new String(bytes, 0, length[0]));
        }
    }
    
    /**
     * Checks the link status for a program and displays the log if a failure occurred.
     */
    private void checkProgramLinkStatus(GL4 gl4, int programId) {
        int[] params = new int[1];
        gl4.glGetProgramiv(programId, GL4.GL_LINK_STATUS, params, 0);
        if (params[0] == GL4.GL_FALSE) {
            gl4.glGetProgramiv(programId, GL4.GL_INFO_LOG_LENGTH, params, 0);
            System.err.println("Program link failed...");
            byte[] bytes = new byte[8192];
            int[] length = new int[1];
            gl4.glGetProgramInfoLog(programId, 8192, length, 0, bytes, 0);
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
