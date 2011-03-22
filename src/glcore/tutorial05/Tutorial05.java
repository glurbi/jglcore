package glcore.tutorial05;

import static glcore.tutorial05.Utils.browse;
import static glcore.tutorial05.Utils.loadTextResource;

import javax.media.opengl.GL3;
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
        
        GL3 gl3 = drawable.getGL().getGL3();
        
        cube = new GeometryBuilder()
                    .addAtribute(POSITION_ATTRIBUTE_INDEX, 3, GL3.GL_FLOAT, cubeVertices)
                    .addAtribute(NORMAL_ATTRIBUTE_INDEX, 3, GL3.GL_FLOAT, cubeNormals)
                    .setPrimitiveType(GL3.GL_TRIANGLES)
                    .setVertexCount(36)
                    .build(gl3);
        
        program = new ProgramBuilder()
                    .setVertexShaderSource(loadTextResource("shader.vert", this))
                    .setFragmentShaderSource(loadTextResource("shader.frag", this))
                    .addAttribute(POSITION_ATTRIBUTE_INDEX, "position")
                    .addAttribute(NORMAL_ATTRIBUTE_INDEX, "color")
                    .build(gl3);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl3 = drawable.getGL().getGL3();
        gl3.glViewport(0, 0, width, height);
        // we keep track of the aspect ratio to adjust the projection volume
        aspectRatio = 1.0f * width / height;
    }

    public void display(GLAutoDrawable drawable) {
        long elapsed = System.currentTimeMillis() - start;
    	MatrixStack stack = new MatrixStack();
        GL3 gl3 = drawable.getGL().getGL3();
        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);
        gl3.glEnable(GL3.GL_CULL_FACE);
        gl3.glPolygonMode(GL3.GL_FRONT_AND_BACK, GL3.GL_LINE);
        program.use(gl3);

        int matrix = gl3.glGetUniformLocation(program.getProgramId(), "mvpMatrix");
        int color = gl3.glGetUniformLocation(program.getProgramId(), "color");
        
        stack.push(Matrix44.frustum(left, right, bottom / aspectRatio, top / aspectRatio, near, far));
        stack.push(Matrix44.translate(0.0f, 0.0f, -3.0f));
        stack.push(Matrix44.rotate(elapsed / 10, 1.0f, 0.0f, 0.0f));
        stack.push(Matrix44.rotate(elapsed / 5, 0.0f, 1.0f, 0.0f));
        gl3.glUniformMatrix4fv(matrix, 1, false, stack.getModelViewProjection().raw(), 0);
        gl3.glUniform3f(color, 0.0f, 1.0f, 0.0f);
        cube.render(gl3);
        stack.pop();
        stack.pop();
        stack.pop();
        stack.pop();
        
        gl3.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }
    
    public static void main(String[] args) {
        browse(new Tutorial05());
    }

}
