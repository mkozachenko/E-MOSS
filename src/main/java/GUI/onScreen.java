package GUI;

/**
 * Created by symph on 31.05.2017.
 */
import java.awt.Color;
import java.awt.Font;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;

public class onScreen {
    TextRenderer Tr = new TextRenderer(new Font("Verdana", Font.BOLD, 12));
    public onScreen(GL2 gl, int screenX, int screenY, Color color, String text, int posX, int posY){

        Tr.beginRendering(screenX, screenY);
        Tr.setSmoothing(true);
        Tr.setColor(color);
        Tr.draw(text, posX, posY);

        Tr.endRendering();
    }
}