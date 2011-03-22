package glcore.tutorial05;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.media.opengl.GL3;

public class ProgramBuilder {

    private String vertexShaderSource;
    private String fragmentShaderSource;
    private Map<Integer, String> attributes;

    public ProgramBuilder() {
        reset();
    }
    
    public ProgramBuilder reset() {
        attributes = new HashMap<Integer, String>();
        return this;
    }
    
    public ProgramBuilder setVertexShaderSource(String vertexShaderSource) {
        this.vertexShaderSource = vertexShaderSource;
        return this;
    }

    public ProgramBuilder setFragmentShaderSource(String fragmentShaderSource) {
        this.fragmentShaderSource = fragmentShaderSource;
        return this;
    }
    
    public ProgramBuilder addAttribute(Integer index, String name) {
        attributes.put(index, name);
        return this;
    }
    
    public Program build(GL3 gl3) {
        int vertexShaderId = gl3.glCreateShader(GL3.GL_VERTEX_SHADER);
        gl3.glShaderSource(vertexShaderId, 1, new String[] { vertexShaderSource }, new int[] { vertexShaderSource.length() }, 0);
        gl3.glCompileShader(vertexShaderId);
        checkShaderStatus(gl3, vertexShaderId);
        
        int fragmentShaderId = gl3.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        gl3.glShaderSource(fragmentShaderId, 1, new String[] { fragmentShaderSource }, new int[] { fragmentShaderSource.length() }, 0);
        gl3.glCompileShader(fragmentShaderId);
        checkShaderStatus(gl3, fragmentShaderId);
        
        int programId = gl3.glCreateProgram();
        gl3.glAttachShader(programId, vertexShaderId);
        gl3.glAttachShader(programId, fragmentShaderId);
        for (Entry<Integer, String> entry : attributes.entrySet()) {
            gl3.glBindAttribLocation(programId, entry.getKey(), entry.getValue());
        }
        gl3.glLinkProgram(programId);
        checkProgramLinkStatus(gl3, programId);
        return new Program(programId);
    }
    
    /**
     * Checks the compilation status for a shader and displays the log if a failure occurred.
     */
    private void checkShaderStatus(GL3 gl3, int shaderId) {
        int[] params = new int[1];
        gl3.glGetShaderiv(shaderId, GL3.GL_COMPILE_STATUS, params, 0);
        if (params[0] == GL3.GL_FALSE) {
            gl3.glGetShaderiv(shaderId, GL3.GL_INFO_LOG_LENGTH, params, 0);
            System.err.println("Shader compilation failed...");
            byte[] bytes = new byte[8192];
            int[] length = new int[1];
            gl3.glGetShaderInfoLog(shaderId, 8192, length, 0, bytes, 0);
            System.err.println(new String(bytes, 0, length[0]));
        }
    }
    
    /**
     * Checks the link status for a program and displays the log if a failure occurred.
     */
    private void checkProgramLinkStatus(GL3 gl3, int programId) {
        int[] params = new int[1];
        gl3.glGetProgramiv(programId, GL3.GL_LINK_STATUS, params, 0);
        if (params[0] == GL3.GL_FALSE) {
            gl3.glGetProgramiv(programId, GL3.GL_INFO_LOG_LENGTH, params, 0);
            System.err.println("Program link failed...");
            byte[] bytes = new byte[8192];
            int[] length = new int[1];
            gl3.glGetProgramInfoLog(programId, 8192, length, 0, bytes, 0);
            System.err.println(new String(bytes, 0, length[0]));
        }
    }
    
}
