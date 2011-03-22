package glcore.tutorial05;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.media.opengl.GL4;

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
    
    public Program build(GL4 gl4) {
        int vertexShaderId = gl4.glCreateShader(GL4.GL_VERTEX_SHADER);
        gl4.glShaderSource(vertexShaderId, 1, new String[] { vertexShaderSource }, new int[] { vertexShaderSource.length() }, 0);
        gl4.glCompileShader(vertexShaderId);
        checkShaderStatus(gl4, vertexShaderId);
        
        int fragmentShaderId = gl4.glCreateShader(GL4.GL_FRAGMENT_SHADER);
        gl4.glShaderSource(fragmentShaderId, 1, new String[] { fragmentShaderSource }, new int[] { fragmentShaderSource.length() }, 0);
        gl4.glCompileShader(fragmentShaderId);
        checkShaderStatus(gl4, fragmentShaderId);
        
        int programId = gl4.glCreateProgram();
        gl4.glAttachShader(programId, vertexShaderId);
        gl4.glAttachShader(programId, fragmentShaderId);
        for (Entry<Integer, String> entry : attributes.entrySet()) {
            gl4.glBindAttribLocation(programId, entry.getKey(), entry.getValue());
        }
        gl4.glLinkProgram(programId);
        checkProgramLinkStatus(gl4, programId);
        return new Program(programId);
    }
    
    /**
     * Checks the compilation status for a shader and displays the log if a failure occurred.
     */
    private void checkShaderStatus(GL4 gl4, int shaderId) {
        int[] params = new int[1];
        gl4.glGetShaderiv(shaderId, GL4.GL_COMPILE_STATUS, params, 0);
        if (params[0] == GL4.GL_FALSE) {
            gl4.glGetShaderiv(shaderId, GL4.GL_INFO_LOG_LENGTH, params, 0);
            System.err.println("Shader compilation failed...");
            byte[] bytes = new byte[8192];
            int[] length = new int[1];
            gl4.glGetShaderInfoLog(shaderId, 8192, length, 0, bytes, 0);
            System.err.println(new String(bytes, 0, length[0]));
        }
    }
    
    /**
     * Checks the link status for a program and displays the log if a failure occurred.
     */
    private void checkProgramLinkStatus(GL4 gl4, int programId) {
        int[] params = new int[1];
        gl4.glGetProgramiv(programId, GL4.GL_LINK_STATUS, params, 0);
        if (params[0] == GL4.GL_FALSE) {
            gl4.glGetProgramiv(programId, GL4.GL_INFO_LOG_LENGTH, params, 0);
            System.err.println("Program link failed...");
            byte[] bytes = new byte[8192];
            int[] length = new int[1];
            gl4.glGetProgramInfoLog(programId, 8192, length, 0, bytes, 0);
            System.err.println(new String(bytes, 0, length[0]));
        }
    }
    
}
