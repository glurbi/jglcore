package glcore.tutorial05;

import javax.media.opengl.GL3;

public class Program {

    private final int programId;
    
    public Program(int programId) {
        this.programId = programId;
    }
    
    public int getProgramId() {
        return programId;
    }
    
    public void use(GL3 gl3) {
        gl3.glUseProgram(programId);         
    }
    
}
