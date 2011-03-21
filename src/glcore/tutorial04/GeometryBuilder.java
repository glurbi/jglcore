package glcore.tutorial04;

import glcore.tutorial04.Geometry.Attribute;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;

public class GeometryBuilder {

	private static class AttributeData {
		public Buffer buffer;
		public int attributeIndex;
		public int dataType;
		public int components;
	}
	
    private int vertexCount;
    private int primitiveType;
    private List<AttributeData> attributesData;
    
    public GeometryBuilder() {
    	reset();
    }
    
    public GeometryBuilder reset() {
    	attributesData = new ArrayList<AttributeData>();
    	return this;
    }
    
    public GeometryBuilder addAtribute(int attributeIndex, int components, int dataType, Object data) {
    	AttributeData attribute = new AttributeData();
    	attribute.attributeIndex = attributeIndex;
    	attribute.components = components;
    	attribute.dataType = dataType;
    	attribute.buffer = makeBuffer(dataType, data);
    	attributesData.add(attribute);
        return this;
    }
    
    public GeometryBuilder setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
        return this;
    }

    public GeometryBuilder setPrimitiveType(int primitiveType) {
        this.primitiveType = primitiveType;
        return this;
    }
    
    public Geometry build(GL3 gl3) {
        int[] buffers = new int[attributesData.size()];
        gl3.glGenBuffers(attributesData.size(), buffers, 0);
        List<Attribute> attributes = new ArrayList<Attribute>();
        for (int i = 0; i < attributesData.size(); i++) {
        	AttributeData attribute = attributesData.get(i);
        	int size = componentSize(attribute.dataType) * attribute.buffer.limit();
        	gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[i]);
        	gl3.glBufferData(GL3.GL_ARRAY_BUFFER, size, attribute.buffer, GL3.GL_STATIC_DRAW);
        	attributes.add(new Attribute(attribute.attributeIndex, buffers[i], attribute.components, attribute.dataType));
        }
        return new Geometry(attributes, vertexCount, primitiveType);
    }

    private int componentSize(int dataType) {
        switch (dataType) {
        case GL3.GL_FLOAT: return 4;
        case GL3.GL_UNSIGNED_BYTE: return 1;
        default: throw new UnsupportedOperationException("Data type not supported");
        }
    }
    
    private Buffer makeBuffer(int dataType, Object data) {
        switch (dataType) {
        case GL3.GL_FLOAT: return makeFloatBuffer((float[]) data);
        case GL3.GL_UNSIGNED_BYTE: return makeUbyteBuffer((byte[]) data);
        default: throw new UnsupportedOperationException("Data type not supported");
        }
    }
    
    private Buffer makeFloatBuffer(float[] data) {
        return ByteBuffer.allocateDirect(4*data.length)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(data)
                .flip();
    }
    
    private Buffer makeUbyteBuffer(byte[] data) {
        return ByteBuffer.allocateDirect(data.length)
                .put(data)
                .flip();
    }
    
}

