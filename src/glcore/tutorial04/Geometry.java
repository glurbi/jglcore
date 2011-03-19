package glcore.tutorial04;

import javax.media.opengl.GL3;

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
    
    public void render(GL3 gl3) {
        gl3.glEnableVertexAttribArray(attributeIndex);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, bufferId);
        gl3.glVertexAttribPointer(attributeIndex, 3, GL3.GL_FLOAT, false, 0, 0);
        gl3.glDrawArrays(primitiveType, 0, vertexCount);
        gl3.glDisableVertexAttribArray(attributeIndex);
    }
    
}
