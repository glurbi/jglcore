package glcore.tutorial04;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL3;

public class GeometryBuilder {

	private static class GeometryAttribute {
		public Buffer buffer;
		public int bufferSize;
		public int attributeIndex;
	}
	
    private int vertexCount;
    private int primitiveType;
    private List<GeometryAttribute> attributes;
    
    public GeometryBuilder() {
    	reset();
    }
    
    public void reset() {
    	attributes = new ArrayList<GeometryAttribute>();
    }
    
    public void addAtribute(int attributeIndex, int bufferSize, Buffer buffer) {
    	GeometryAttribute attribute = new GeometryAttribute();
    	attribute.attributeIndex = attributeIndex;
    	attribute.bufferSize = bufferSize;
    	attribute.buffer = buffer;
    	attributes.add(attribute);
    }
    
    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public void setPrimitiveType(int primitiveType) {
        this.primitiveType = primitiveType;
    }
    
    public Geometry build(GL3 gl3) {
        int[] buffers = new int[attributes.size()];
        gl3.glGenBuffers(attributes.size(), buffers, 0);
        Map<Integer, Integer> attributeMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < attributes.size(); i++) {
        	GeometryAttribute attribute = attributes.get(i);
        	gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[i]);
        	gl3.glBufferData(GL3.GL_ARRAY_BUFFER, attribute.bufferSize, attribute.buffer, GL3.GL_STATIC_DRAW);
        	attributeMap.put(attribute.attributeIndex, buffers[i]);
        }
        return new Geometry(attributeMap, vertexCount, primitiveType);
    }

}
