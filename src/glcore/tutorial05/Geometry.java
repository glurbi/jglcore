package glcore.tutorial05;

import java.util.List;

import javax.media.opengl.GL4;

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
    
    public void render(GL4 gl4) {
    	for (Attribute attribute : attributes) {
    		gl4.glEnableVertexAttribArray(attribute.index);
    		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, attribute.bufferName);
    		gl4.glVertexAttribPointer(attribute.index, attribute.components, attribute.dataType, false, 0, 0);
    	}
        gl4.glDrawArrays(primitiveType, 0, vertexCount);
    	for (Attribute attribute : attributes) {
    		gl4.glDisableVertexAttribArray(attribute.index);
    	}
        gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
    }
    
}
