package glcore.tutorial05;

import java.util.Stack;

public class MatrixStack {

    private Stack<Matrix44> modelViewProjectionMatrix = new Stack<Matrix44>();
    private Stack<Matrix44> modelViewMatrix = new Stack<Matrix44>();
    
    public MatrixStack() {
        Matrix44 identity = Matrix44.identity();
        modelViewProjectionMatrix.push(identity);
        modelViewMatrix.push(identity);
    }
    
    public void pushProjection(Matrix44 matrix) {
        modelViewProjectionMatrix.push(modelViewProjectionMatrix.peek().mult(matrix));
    }

    public void pushModelView(Matrix44 matrix) {
        modelViewProjectionMatrix.push(modelViewProjectionMatrix.peek().mult(matrix));
        modelViewMatrix.push(modelViewMatrix.peek().mult(matrix));
    }

    public void popProjection() {
        modelViewProjectionMatrix.pop();
    }

    public void popModelView() {
        modelViewProjectionMatrix.pop();
        modelViewMatrix.pop();
    }

    public Matrix44 getModelViewProjectionMatrix() {
        return modelViewProjectionMatrix.peek();
    }
    
    public Matrix44 getModelViewMatrix() {
        return modelViewMatrix.peek();
    }
}
