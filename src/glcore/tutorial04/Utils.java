package glcore.tutorial04;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

public class Utils {

    public static String loadTextResource(String name, Object o) {
        try {
            InputStream is = o.getClass().getResourceAsStream(name);
            byte[] bytes = new byte[8192];
            int len = is.read(bytes);
            return new String(bytes, 0, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte ubyte(int i) {
        return (byte)(i & 0xff);
    }
    
    public static void browse(GLEventListener glEventListener) {
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        final GLProfile profile = GLProfile.get(GLProfile.GL4);
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(glEventListener);
        frame.add(canvas);
        frame.setSize(300, 300);
        frame.setVisible(true);
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    canvas.display();
                    Thread.yield();
                }
            }
        }).start();
    }
    
}
