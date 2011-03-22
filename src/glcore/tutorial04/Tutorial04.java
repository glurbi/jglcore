package glcore.tutorial04;

import static glcore.tutorial04.Utils.browse;
import static glcore.tutorial04.Utils.loadTextResource;
import static glcore.tutorial04.Utils.ubyte;

import java.util.Stack;

import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * This tutorial is built on top of Tutorial03. It introduces the Matrix44 class
 * to allow transformations of the Model View Projection matrix. It demonstrates
 * the difference between the orthogonal projection and the perspective projection
 * by swapping at regular interval. It also uses several attributes per vertex for
 * rendering (position and color).
 */
public class Tutorial04 implements GLEventListener {
    
    private static final int POSITION_ATTRIBUTE_INDEX = 0;
    private static final int COLOR_ATTRIBUTE_INDEX = 1;
    
    private static final float[] triangleVertices = {
        0.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f
    };
    
    private static final float[] triangleColors = {
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f
    };
    
    private static final float[] quadVertices = {
        0.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        -1.0f, -1.0f, 0.0f,
        -1.0f, 0.0f, 0.0f
    };
    
    private static final byte[] quadColors = {
        ubyte(255), ubyte(0), ubyte(0),
        ubyte(0), ubyte(255), ubyte(0),
        ubyte(0), ubyte(0), ubyte(255),
        ubyte(0), ubyte(0), ubyte(255),
        ubyte(255), ubyte(0), ubyte(0),
        ubyte(0), ubyte(255), ubyte(0)
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
    private Geometry triangle;
    private Geometry quad;
    
    private float aspectRatio;
    
    public void init(GLAutoDrawable drawable) {
        
        GL4 gl4 = (GL4) drawable.getGL();
        
        triangle = new GeometryBuilder()
                    .addAtribute(POSITION_ATTRIBUTE_INDEX, 3, GL4.GL_FLOAT, triangleVertices)
                    .addAtribute(COLOR_ATTRIBUTE_INDEX, 3, GL4.GL_FLOAT, triangleColors)
                    .setPrimitiveType(GL4.GL_TRIANGLES)
                    .setVertexCount(3)
                    .build(gl4);
        
        quad = new GeometryBuilder()
                    .addAtribute(POSITION_ATTRIBUTE_INDEX, 3, GL4.GL_FLOAT, quadVertices)
                    .addAtribute(COLOR_ATTRIBUTE_INDEX, 3, GL4.GL_UNSIGNED_BYTE, quadColors)
                    .setPrimitiveType(GL4.GL_TRIANGLES)
                    .setVertexCount(6)
                    .build(gl4);

        program = new ProgramBuilder()
                    .setVertexShaderSource(loadTextResource("shader.vert", this))
                    .setFragmentShaderSource(loadTextResource("shader.frag", this))
                    .addAttribute(POSITION_ATTRIBUTE_INDEX, "position")
                    .addAttribute(COLOR_ATTRIBUTE_INDEX, "color")
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
    	Stack<Matrix44> mvp = new Stack<Matrix44>();
        GL4 gl4 = (GL4) drawable.getGL();
        gl4.glClear(GL4.GL_COLOR_BUFFER_BIT);
        program.use(gl4);

        // every 5 seconds, we swap from orthogonal to perspective projection
        mvp.push(Matrix44.identity());
        if (elapsed / 5000 % 2 == 0) {
            mvp.push(mvp.peek().ortho(left, right, bottom / aspectRatio, top / aspectRatio, near, far));
        } else {
            mvp.push(mvp.peek().frustum(left, right, bottom / aspectRatio, top / aspectRatio, near, far));
        }
        
        int matrix = gl4.glGetUniformLocation(program.getProgramId(), "mvpMatrix");
        
        mvp.push(mvp.peek().translate(0.0f, 0.0f, -2.0f));
        mvp.push(mvp.peek().rotate(elapsed / 10, 0.0f, 1.0f, 0.0f));
        gl4.glUniformMatrix4fv(matrix, 1, false, mvp.peek().raw(), 0);
        triangle.render(gl4);
        mvp.pop();

        mvp.push(mvp.peek().rotate(elapsed / 5, 0.0f, 1.0f, 0.0f));
        gl4.glUniformMatrix4fv(matrix, 1, false, mvp.peek().raw(), 0);
        quad.render(gl4);
        mvp.pop();
        
        mvp.pop();
        
        gl4.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }
    
    public static void main(String[] args) {
        browse(new Tutorial04());
    }

}
