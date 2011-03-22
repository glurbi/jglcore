package glcore.tutorial03;

import javax.media.opengl.GL4;

public class Geometry {

    private final int bufferId;
    private final int attributeIndex;
    private final int vertexCount;
    private final int primitiveType;
    
    public Geometry(int bufferId, int attributeIndex, int vertexCount, int primitiveType) {
        this.bufferId = bufferId;
        this.attributeIndex = attributeIndex;
        this.vertexCount = vertexCount;
        this.primitiveType = primitiveType;
    }
    
    public void render(GL4 gl4) {
        gl4.glEnableVertexAttribArray(attributeIndex);
        gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, bufferId);
        gl4.glVertexAttribPointer(attributeIndex, 3, GL4.GL_FLOAT, false, 0, 0);
        gl4.glDrawArrays(primitiveType, 0, vertexCount);
        gl4.glDisableVertexAttribArray(attributeIndex);
    }
    
}
