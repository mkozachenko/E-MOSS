package Graphics;

/**
 * Created by symph on 31.05.2017.
 */

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.math.geom.AABBox;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.*;

public class Body {
    public static float radius;
    public static String[] name;
    public static int calls=0;
    public int i=0;

    public Body (GL2 gl, GLU glu, GLUT glut, float radius, Texture texture, String nameString) {
        //name[calls] = nameString+" array";
        final int slices = 24;
        final int stacks = 24;
        texture.enable(gl);
        texture.bind(gl);
        GLUquadric body = glu.gluNewQuadric();
        glu.gluQuadricTexture(body, true);
        glu.gluQuadricDrawStyle(body, GLU.GLU_FILL);
        glu.gluQuadricNormals(body, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(body, GLU.GLU_OUTSIDE);
        glu.gluSphere(body, radius, slices, stacks);
        //glut.glutWireCube(radius*2);
        //glut.AABBox();
        glu.gluDeleteQuadric(body);
        gl.glRasterPos3f(radius, radius, 1.3f * radius);
        glut.glutBitmapString(5, nameString);
        texture.disable(gl);

        //box.intersectsRay(arg0);
        //calls++;
    }
}
