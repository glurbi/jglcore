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
    
    private static final int POSITION_ATTRIBUTE_INDEX = 0;
    
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
        program.use(gl3);

        // defines the model view projection matrix and set the corresponding uniform
        float[] mvp = ortho(-2.0f, 2.0f, -2.0f, 2.0f, 2.0f, -2.0f);
        int matrix = gl3.glGetUniformLocation(program.getProgramId(), "mvpMatrix");
        gl3.glUniformMatrix4fv(matrix, 1, false, mvp, 0);
        
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
    
    // Generate a orthogonal projection matrix as defined at:
    // http://www.opengl.org/sdk/docs/man/xhtml/glOrtho.xml
    private float[] ortho(float left,float right, float bottom, float top, float near, float far) {
        float[] matrix = new float[16];
        matrix[0] = 2 / (right - left);
        matrix[1] = 0.0f;
        matrix[2] = 0.0f;
        matrix[3] = 0.0f;
        matrix[4] = 0.0f;
        matrix[5] = 2 / (top - bottom);
        matrix[6] = 0.0f;
        matrix[7] = 0.0f;
        matrix[8] = 0.0f;
        matrix[9] = 0.0f;
        matrix[10] = 2 / (far - near);
        matrix[11] = 0.0f;
        matrix[12] = - (right + left) / (right - left);
        matrix[13] = - (top + bottom) / (top - bottom);
        matrix[14] = - (far + near) / (far - near);
        matrix[15] = 1.0f;
        return matrix;
    }
    
    public static void main(String[] args) {
        Tutorial03 tutorial = new Tutorial03();
        browse(tutorial);
    }

}
