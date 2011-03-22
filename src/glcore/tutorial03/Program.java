package glcore.tutorial03;

import javax.media.opengl.GL4;

public class Program {

    private final int programId;
    
    public Program(int programId) {
        this.programId = programId;
    }
    
    public int getProgramId() {
        return programId;
    }
    
    public void use(GL4 gl4) {
        gl4.glUseProgram(programId);         
    }
    
}
