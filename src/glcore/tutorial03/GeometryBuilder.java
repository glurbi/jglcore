package glcore.tutorial03;

import java.nio.Buffer;

import javax.media.opengl.GL4;

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
    
    public Geometry build(GL4 gl4) {
        int[] buffers = new int[1]; 
        gl4.glGenBuffers(1, buffers, 0);
        gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers[0]);
        gl4.glBufferData(GL4.GL_ARRAY_BUFFER, bufferSize, buffer, GL4.GL_STATIC_DRAW);
        return new Geometry(buffers[0], attributeIndex, vertexCount, primitiveType);
    }

}
