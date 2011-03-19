package glcore.tutorial04;

import static glcore.tutorial03.Utils.browse;
import static glcore.tutorial03.Utils.loadTextResource;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * This tutorial is built on top of Tutorial03. It introduces classes to manage
 * the matrix stack and allows to do transformations to the model.
 */
public class Tutorial04 implements GLEventListener {
    
    private static final int POSITION_ATTRIBUTE_INDEX = 0;
    
    // defines the orthographic projection volume
    private static final float left = -2.0f;
    private static final float right = 2.0f;
    private static final float bottom = -2.0f;
    private static final float top = 2.0f;
    private static final float near = 1.0f;
    private static final float far = -1.0f;
    
    private Program program;
    private Geometry triangle;
    private Geometry quad;
    
    private float aspectRatio;
    
    public void init(GLAutoDrawable drawable) {
        GL3 gl3 = (GL3) drawable.getGL();
        createTriangle(gl3);
        createQuad(gl3);
        createProgram(gl3);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl3 = (GL3) drawable.getGL();
        gl3.glViewport(0, 0, width, height);
        // we keep track of the aspect ratio to adjust the projection volume
        aspectRatio = 1.0f * width / height;
    }

    public void display(GLAutoDrawable drawable) {
    	Stack<Matrix44> mvp = new Stack<Matrix44>();
        GL3 gl3 = (GL3) drawable.getGL();
        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);
        program.use(gl3);

        mvp.push(Matrix44.identity());
        mvp.push(mvp.peek().ortho(left, right, bottom / aspectRatio, top / aspectRatio, near, far));
        int matrix = gl3.glGetUniformLocation(program.getProgramId(), "mvpMatrix");
        gl3.glUniformMatrix4fv(matrix, 1, false, mvp.peek().raw(), 0);
        
        // set the uniform for the global geometry color
        int color = gl3.glGetUniformLocation(program.getProgramId(), "color");
        gl3.glUniform4f(color, 0.0f, 1.0f, 0.0f, 1.0f);
        
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
        builder.setPrimitiveType(GL3.GL_TRIANGLES);
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
        builder.setPrimitiveType(GL3.GL_TRIANGLES);
        builder.setVertexCount(6);
        quad = builder.build(gl3);
    }
    
    private void createProgram(GL3 gl3) {
        ProgramBuilder builder = new ProgramBuilder();
        builder.setVertexShaderSource(loadTextResource("shader.vert", this));
        builder.setFragmentShaderSource(loadTextResource("shader.frag", this));
        Map<Integer, String> attributes = new HashMap<Integer, String>();
        attributes.put(POSITION_ATTRIBUTE_INDEX, "position");
        builder.setAttributes(attributes);
        program = builder.build(gl3);
    }
    
    public static void main(String[] args) {
        Tutorial04 tutorial = new Tutorial04();
        browse(tutorial);
    }

}
