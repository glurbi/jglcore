package glcore.tutorial05;

import java.util.Stack;

public class MatrixStack {

    private Stack<Matrix44> modelViewProjection = new Stack<Matrix44>();
    private Stack<Matrix44> matrices = new Stack<Matrix44>();
    
    public MatrixStack() {
        Matrix44 identity = Matrix44.identity();
        modelViewProjection.push(identity);
        matrices.push(identity);
    }
    
    public void push(Matrix44 matrix) {
        modelViewProjection.push(modelViewProjection.peek().mult(matrix));
        matrices.push(matrix);
    }

    public void pop() {
        modelViewProjection.pop();
        matrices.pop();
    }

    public Matrix44 getModelViewProjection() {
        return modelViewProjection.peek();
    }
}
