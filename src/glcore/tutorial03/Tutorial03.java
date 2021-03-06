package glcore.tutorial03;

import static glcore.tutorial03.Utils.browse;
import static glcore.tutorial03.Utils.loadTextResource;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * This tutorial is built on top of Tutorial02. It introduces some classes
 * for managing shaders and geometries. As well, the shader now uses a projection matrix.
 */
public class Tutorial03 implements GLEventListener {
    
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
        GL4 gl4 = (GL4) drawable.getGL();
        createTriangle(gl4);
        createQuad(gl4);
        createProgram(gl4);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl4 = (GL4) drawable.getGL();
        gl4.glViewport(0, 0, width, height);
        // we keep track of the aspect ratio to adjust the projection volume
        aspectRatio = 1.0f * width / height;
    }

    public void display(GLAutoDrawable drawable) {
        GL4 gl4 = (GL4) drawable.getGL();
        gl4.glClear(GL4.GL_COLOR_BUFFER_BIT);
        program.use(gl4);

        // defines the model view projection matrix and set the corresponding uniform
        // NB: bottom and top are adjusted with the aspect ratio
        float[] mvp = ortho(left, right, bottom / aspectRatio, top / aspectRatio, near, far);
        int matrix = gl4.glGetUniformLocation(program.getProgramId(), "mvpMatrix");
        gl4.glUniformMatrix4fv(matrix, 1, false, mvp, 0);
        
        // set the uniform for the global geometry color
        int color = gl4.glGetUniformLocation(program.getProgramId(), "color");
        gl4.glUniform4f(color, 0.0f, 1.0f, 0.0f, 1.0f);
        
        triangle.render(gl4);
        quad.render(gl4);
        gl4.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }
    
    private void createTriangle(GL4 gl4) {
        GeometryBuilder builder = new GeometryBuilder();
        FloatBuffer buf = ByteBuffer.allocateDirect(4*3*3).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buf.put(new float[] { 0.0f, 0.0f, 0.0f });
        buf.put(new float[] { 1.0f, 0.0f, 0.0f });
        buf.put(new float[] { 0.0f, 1.0f, 0.0f });
        buf.flip();
        builder.setBuffer(buf);
        builder.setBufferSize(4*3*3);
        builder.setAttributeIndex(POSITION_ATTRIBUTE_INDEX);
        builder.setPrimitiveType(GL4.GL_TRIANGLES);
        builder.setVertexCount(3);
        triangle = builder.build(gl4);
    }
    
    private void createQuad(GL4 gl4) {
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
        builder.setPrimitiveType(GL4.GL_TRIANGLES);
        builder.setVertexCount(6);
        quad = builder.build(gl4);
    }
    
    private void createProgram(GL4 gl4) {
        ProgramBuilder builder = new ProgramBuilder();
        builder.setVertexShaderSource(loadTextResource("shader.vert", this));
        builder.setFragmentShaderSource(loadTextResource("shader.frag", this));
        Map<Integer, String> attributes = new HashMap<Integer, String>();
        attributes.put(POSITION_ATTRIBUTE_INDEX, "position");
        builder.setAttributes(attributes);
        program = builder.build(gl4);
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
