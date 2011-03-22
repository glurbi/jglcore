package glcore.tutorial05;

import static glcore.tutorial05.Utils.browse;
import static glcore.tutorial05.Utils.loadTextResource;

import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * This tutorial is built on top of Tutorial04. It demonstrates how to implement
 * lighting in the shader.
 */
public class Tutorial05 implements GLEventListener {
    
    private static final int POSITION_ATTRIBUTE_INDEX = 0;
    private static final int NORMAL_ATTRIBUTE_INDEX = 1;
    
    private static final float[] cubeVertices = {
        
        // back face
        1.0f, 1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        
        // front face
        -1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        
        // bottom face
        -1.0f, -1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f, 
        1.0f, -1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, 1.0f,
        
        // top face
        -1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, -1.0f, 
        -1.0f, 1.0f, -1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, -1.0f,
        
        // left face
        -1.0f, -1.0f, -1.0f, 
        -1.0f, -1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, 1.0f,
        
        // right face
        1.0f, 1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, -1.0f, 
        1.0f, -1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        1.0f, 1.0f, 1.0f
    };
    
    private static final float[] cubeNormals = {
        // back face
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        
        // front face
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f, 
        
        // bottom face
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        
        // top face
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        
        // left face
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        
        // right face
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f
    };
    
    private static long start = System.currentTimeMillis();
    
    // defines the projection volume
    private static final float left = -2.0f;
    private static final float right = 2.0f;
    private static final float bottom = -2.0f;
    private static final float top = 2.0f;
    private static final float near = 1.0f;
    private static final float far = 10.0f;
    
    private Program program;
    private Geometry cube;
    
    private float aspectRatio;
    
    public void init(GLAutoDrawable drawable) {
        
        GL4 gl4 = (GL4) drawable.getGL();
        
        cube = new GeometryBuilder()
                    .addAtribute(POSITION_ATTRIBUTE_INDEX, 3, GL4.GL_FLOAT, cubeVertices)
                    .addAtribute(NORMAL_ATTRIBUTE_INDEX, 3, GL4.GL_FLOAT, cubeNormals)
                    .setPrimitiveType(GL4.GL_TRIANGLES)
                    .setVertexCount(36)
                    .build(gl4);
        
        program = new ProgramBuilder()
                    .setVertexShaderSource(loadTextResource("shader.vert", this))
                    .setFragmentShaderSource(loadTextResource("shader.frag", this))
                    .addAttribute(POSITION_ATTRIBUTE_INDEX, "position")
                    .addAttribute(NORMAL_ATTRIBUTE_INDEX, "color")
                    .build(gl4);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl4 = (GL4) drawable.getGL();
        gl4.glViewport(0, 0, width, height);
        // we keep track of the aspect ratio to adjust the projection volume
        aspectRatio = 1.0f * width / height;
    }

    public void display(GLAutoDrawable drawable) {
        long elapsed = System.currentTimeMillis() - start;
    	MatrixStack stack = new MatrixStack();
        GL4 gl4 = (GL4) drawable.getGL();
        gl4.glClear(GL4.GL_COLOR_BUFFER_BIT);
        gl4.glEnable(GL4.GL_CULL_FACE);
        gl4.glPolygonMode(GL4.GL_FRONT_AND_BACK, GL4.GL_LINE);
        program.use(gl4);

        int matrix = gl4.glGetUniformLocation(program.getProgramId(), "mvpMatrix");
        int color = gl4.glGetUniformLocation(program.getProgramId(), "color");
        
        stack.push(Matrix44.frustum(left, right, bottom / aspectRatio, top / aspectRatio, near, far));
        stack.push(Matrix44.translate(0.0f, 0.0f, -3.0f));
        stack.push(Matrix44.rotate(elapsed / 10, 1.0f, 0.0f, 0.0f));
        stack.push(Matrix44.rotate(elapsed / 5, 0.0f, 1.0f, 0.0f));
        gl4.glUniformMatrix4fv(matrix, 1, false, stack.getModelViewProjection().raw(), 0);
        gl4.glUniform3f(color, 0.0f, 1.0f, 0.0f);
        cube.render(gl4);
        stack.pop();
        stack.pop();
        stack.pop();
        stack.pop();
        
        gl4.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }
    
    public static void main(String[] args) {
        browse(new Tutorial05());
    }

}
