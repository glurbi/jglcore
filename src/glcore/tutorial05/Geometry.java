package glcore.tutorial05;

import java.util.List;

import javax.media.opengl.GL3;

public class Geometry {

    public static class Attribute {
        public Attribute(int index, int bufferName, int components, int dataType) {
            this.index = index;
            this.bufferName = bufferName;
            this.components = components;
            this.dataType = dataType;
        }
        public final int index;
        public final int bufferName;
        public final int components;
        public final int dataType;
    }
    
    private final int vertexCount;
    private final int primitiveType;
    private final List<Attribute> attributes;
    
    public Geometry(List<Attribute> attributes, int vertexCount, int primitiveType) {
    	this.attributes = attributes;
        this.vertexCount = vertexCount;
        this.primitiveType = primitiveType;
    }
    
    public void render(GL3 gl3) {
    	for (Attribute attribute : attributes) {
    		gl3.glEnableVertexAttribArray(attribute.index);
    		gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, attribute.bufferName);
    		gl3.glVertexAttribPointer(attribute.index, attribute.components, attribute.dataType, false, 0, 0);
    	}
        gl3.glDrawArrays(primitiveType, 0, vertexCount);
    	for (Attribute attribute : attributes) {
    		gl3.glDisableVertexAttribArray(attribute.index);
    	}
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }
    
}
