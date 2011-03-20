package glcore.tutorial04;

public class Matrix44 {

    private final float[] m = new float[16];
    
    public static Matrix44 identity() {
    	Matrix44 matrix = new Matrix44();
    	matrix.m[0] = 1.0f;
    	matrix.m[5] = 1.0f;
    	matrix.m[10] = 1.0f;
    	matrix.m[15] = 1.0f;
    	return matrix;
    }
    
    public Matrix44 mult(Matrix44 that) {
    	Matrix44 matrix = new Matrix44();
    	for (int i = 0; i < 4; i++) {
    		for (int j = 0; j < 4; j++) {
    			matrix.m[i+j*4] =
    				this.m[i+0] * that.m[j*4+0] +
    				this.m[i+4] * that.m[j*4+1] +
    				this.m[i+8] * that.m[j*4+2] +
    				this.m[i+12] * that.m[j*4+3];
    		}
    	}
    	return matrix;
    }
    
    public Matrix44 ortho(float left,float right, float bottom, float top, float near, float far) {
    	// Generate a orthogonal projection matrix as defined at:
    	// http://www.opengl.org/sdk/docs/man/xhtml/glOrtho.xml
    	Matrix44 matrix = new Matrix44();
        matrix.m[0] = 2 / (right - left);
        matrix.m[1] = 0.0f;
        matrix.m[2] = 0.0f;
        matrix.m[3] = 0.0f;
        matrix.m[4] = 0.0f;
        matrix.m[5] = 2 / (top - bottom);
        matrix.m[6] = 0.0f;
        matrix.m[7] = 0.0f;
        matrix.m[8] = 0.0f;
        matrix.m[9] = 0.0f;
        matrix.m[10] = 2 / (far - near);
        matrix.m[11] = 0.0f;
        matrix.m[12] = - (right + left) / (right - left);
        matrix.m[13] = - (top + bottom) / (top - bottom);
        matrix.m[14] = - (far + near) / (far - near);
        matrix.m[15] = 1.0f;
        return mult(matrix);
    }
    
    public float[] raw() {
    	return m;
    }
    
    @Override
    public String toString() {
    	return "" + m[0] + "\t" + m[4] + "\t" + m[8] + "\t" + m[12] + "\n"
    	          + m[1] + "\t" + m[5] + "\t" + m[9] + "\t" + m[13] + "\n"
    	          + m[2] + "\t" + m[6] + "\t" + m[10] + "\t" + m[14] + "\n"
    	          + m[3] + "\t" + m[7] + "\t" + m[11] + "\t" + m[15] + "\n";
    }
    
}
