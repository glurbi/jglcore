package glcore.tutorial03;

import static glcore.tutorial03.Utils.browse;
import static glcore.tutorial03.Utils.loadTextResource;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * This tutorial is built on top of Tutorial02. It introduces some classes
 * for managing shaders and geometries. As well, the shader now uses a projection matrix.
 */
public class Tutorial03 implements GLEventListener {
    
    // Up to 16 attributes per vertex is allowed so any value between 0 and 15 will do.
    private static final int POSITION_ATTRIBUTE_INDEX = 12;
    
    private Program program;
    private Geometry triangle;
    private Geometry quad;
    
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
        triangle.render(gl3);
        quad.render(gl3);
        gl3.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }
    
    private void createTriangle(GL3 gl3) {
        GeometryBuilder builder = new GeometryBuilder();
        FloatBuffer buf = ByteBuffer.allocateDirect(4*3*3).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buf.put(new float[] { 0.0f, 0.0f, 0.0f });
        buf.put(new float[] { 1.0f, 0.0f, 0.0f });
        buf.put(new float[] { 0.0f, 1.0f, 0.0f });
        buf.flip();
        builder.setBuffer(buf);
        builder.setBufferSize(4*3*3);
        builder.setAttributeIndex(POSITION_ATTRIBUTE_INDEX);
        builder.setVertexCount(3);
        triangle = builder.build(gl3);
    }
    
    private void createQuad(GL3 gl3) {
        GeometryBuilder builder = new GeometryBuilder();
        FloatBuffer buf = ByteBuffer.allocateDirect(4*6*3).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buf.put(new float[] { 0.0f, 0.0f, 0.0f });
        buf.put(new float[] { -1.0f, 0.0f, 0.0f });
        buf.put(new float[] { 0.0f, -1.0f, 0.0f });
        buf.put(new float[] { 0.0f, -1.0f, 0.0f });
        buf.put(new float[] { -1.0f, -1.0f, 0.0f });
        buf.put(new float[] { -1.0f, 0.0f, 0.0f });
        buf.flip();
        builder.setBuffer(buf);
        builder.setBufferSize(4*6*3);
        builder.setAttributeIndex(POSITION_ATTRIBUTE_INDEX);
        builder.setVertexCount(6);
        quad = builder.build(gl3);
    }
    
    private void createProgram(GL3 gl3) {
        ProgramBuilder builder = new ProgramBuilder();
        builder.setVertexShaderSource(loadTextResource("shader.vert", this));
        builder.setFragmentShaderSource(loadTextResource("shader.frag", this));
        Map<Integer, String> attributes = new HashMap<Integer, String>();
        attributes.put(POSITION_ATTRIBUTE_INDEX, "inPosition");
        builder.setAttributes(attributes);
        program = builder.build(gl3);
    }
    
    public static void main(String[] args) {
        Tutorial03 tutorial = new Tutorial03();
        browse(tutorial);
    }

}
