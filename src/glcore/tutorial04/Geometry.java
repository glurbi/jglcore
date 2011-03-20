package glcore.tutorial04;

import java.util.Map;
import java.util.Map.Entry;

import javax.media.opengl.GL3;

public class Geometry {

    private final Map<Integer, Integer> attributeMap;
    private final int vertexCount;
    private final int primitiveType;
    
    public Geometry(Map<Integer, Integer> attributeMap, int vertexCount, int primitiveType) {
    	this.attributeMap = attributeMap;
        this.vertexCount = vertexCount;
        this.primitiveType = primitiveType;
    }
    
    public void render(GL3 gl3) {
    	for (Entry<Integer, Integer> entry : attributeMap.entrySet()) {
    		gl3.glEnableVertexAttribArray(entry.getKey());
    		gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, entry.getValue());
    		gl3.glVertexAttribPointer(entry.getKey(), 3, GL3.GL_FLOAT, false, 0, 0);
    	}
        gl3.glDrawArrays(primitiveType, 0, vertexCount);
    	for (Entry<Integer, Integer> entry : attributeMap.entrySet()) {
    		gl3.glDisableVertexAttribArray(entry.getKey());
    	}
    }
    
}
