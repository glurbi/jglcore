package glcore.tutorial04;

import java.nio.Buffer;

import javax.media.opengl.GL3;

public class GeometryBuilder {

    private Buffer buffer;
    private int bufferSize;
    private int attributeIndex;
    private int vertexCount;
    private int primitiveType;
    
    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }
    
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    
    public void setAttributeIndex(int attributeIndex) {
        this.attributeIndex = attributeIndex;
    }
    
    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public void setPrimitiveType(int primitiveType) {
        this.primitiveType = primitiveType;
    }
    
    public Geometry build(GL3 gl3) {
        int[] buffers = new int[1]; 
        gl3.glGenBuffers(1, buffers, 0);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[0]);
        gl3.glBufferData(GL3.GL_ARRAY_BUFFER, bufferSize, buffer, GL3.GL_STATIC_DRAW);
        return new Geometry(buffers[0], attributeIndex, vertexCount, primitiveType);
    }

}
