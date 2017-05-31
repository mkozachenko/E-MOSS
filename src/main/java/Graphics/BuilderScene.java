package Graphics;

/**
 * Created by symph on 31.05.2017.
 */

import static java.nio.file.FileSystems.getDefault;

import GUI.BuilderWindow;
import GUI.onScreen;
import com.jogamp.opengl.util.texture.*;
import com.sun.prism.impl.BufferUtil;
import com.jogamp.opengl.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.Ray;
import com.jogamp.opengl.math.geom.AABBox;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import javax.swing.*;

import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.util.*;

public class BuilderScene extends GLCanvas implements GLEventListener{
    private static final long serialVersionUID = 1L;
    int mouse_x = 0, mouse_y = 0;
    boolean intersect = false;
    private GLU glu;
    private GLUT glut;
    public static AABBox box = new AABBox();
    private int fps = 60;
    private FPSAnimator animator;
    private Texture skyT, customBodyTexture;
    public static float xcam, ycam, Angle = 90, angleCel=90, bodyRad, transX, transY, mouseY, mouseX, mouseZ, lookY, lookX, lookZ, cameraY, cameraX, cameraZ, rota=50;
    public Body body, sky;
    int skySize, integ;
    public FileInputStream in;

    public Body[] bodyAr = new Body[20];
    public String map, absPath, bodyNames[];
    float step=3.0f;
    FileSystem fs = getDefault();
    Path path = fs.getPath("src//main//resources//graphics//maps");
    String SP = "\\stars2.jpg";
    String abs = path.toAbsolutePath().toString().replace("\\", "\\");
    String texName = "\\default.png";
    String[] texNames = new String[3];
    private static final int BUFSIZE = 512;
    private Point pickPoint = new Point();


    int viewport[] = new int[4];
    double mvmatrix[] = new double[16];
    double projmatrix[] = new double[16];
    int realy = 0;// GL y coord pos
    double wcoord[] = new double[4];// wx, wy, wz;// returned xyz coords


    public void loadTexture(String name){
        File texFile = new File(name);
        try {
            customBodyTexture = TextureIO.newTexture(texFile, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public BuilderScene(GLCapabilities capabilities, int width, int height) {
        addGLEventListener(this);

    }
    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();

        skySize=90000;
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        float SHINE_ALL_DIRECTIONS = 1;
        float[] lightPos = {0, 0, 0, SHINE_ALL_DIRECTIONS};
        float[] lightColorAmbient = {1f, 1f, 1f, 1f};
        float[] lightColorSpecular = {1f, 1f, 1f, 1f};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT_AND_DIFFUSE, lightColorAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);


        float[] rgba = {5f, 5f, 5f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.5f);

        gl.glEnable(GL2.GL_LIGHT1);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        BuilderWindow.zoom=40;


        File texFile = new File(abs+SP);
        try {
            skyT = TextureIO.newTexture(texFile, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadTexture(abs+texName);
        animator = new FPSAnimator(this, fps);
        animator.start();


    }

    public void display(GLAutoDrawable drawable) {
        if (!animator.isAnimating()) {
            return;
        }

        final GL2 gl = drawable.getGL().getGL2();
        //gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        //gl.glTranslatef(0, BuilderWindow.KBmoveY, BuilderWindow.KBmoveZ);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        setCamera(gl, glu, BuilderWindow.zoom, 0, 0);
        gl.glPushMatrix();
/***************/
        if(BuilderWindow.clicker){
            loadTexture(BuilderWindow.savePath);
            BuilderWindow.clicker =! BuilderWindow.clicker;
        }
        gl.glInitNames();
        bodyRad=BuilderWindow.bodySize;
        //gl.glLoadIdentity();

        sky = new Body(gl, glu, glut, skySize, skyT, "sky");

        Angle = (Angle + 50/100.1f*1.2f) % 360f;
        gl.glRotatef(Angle, 0, 0, 1f);
        gl.glRotatef(BuilderWindow.tilt, BuilderWindow.tilt, BuilderWindow.tilt, 1f);
        Ray ray = new Ray();
        body = new Body(gl, glu, glut, bodyRad, customBodyTexture, BuilderWindow.nameField.getText()+"1");
        pickingRay(gl, glu, (int) (BuilderWindow.xOrigin),(int) (BuilderWindow.yOrigin), ray);

        box.setSize(bodyRad*2, bodyRad*2, bodyRad*2, -bodyRad*2, -bodyRad*2, -bodyRad*2);
        String bz = Float.toString(box.getDepth());
        String by = Float.toString(box.getMaxY());
        String bx = Float.toString(box.getMaxX());
        intersect = box.intersectsRay(ray);

		        /**/
        gl.glPopMatrix();
        gl.glPushMatrix();

        /****/
/***************/
        gl.glLoadIdentity();
        //bodyNames[0]=Body.name[0];
        new onScreen(gl, drawable.getSurfaceWidth(), drawable.getSurfaceHeight(), Color.RED, "X",(int) (BuilderWindow.xOrigin),
                (int) (BuilderWindow.panel_A.getHeight()- BuilderWindow.yOrigin));
        //new OnScreen(gl, drawable.getSurfaceWidth(), drawable.getSurfaceHeight(), Color.WHITE, Float.toString(time),0,0);
        new onScreen(gl, drawable.getSurfaceWidth(), drawable.getSurfaceHeight(), Color.YELLOW, bx+"-"+by+"-"+bz,0,20);
        new onScreen(gl, drawable.getSurfaceWidth(), drawable.getSurfaceHeight(), Color.YELLOW, Boolean.toString(intersect),0,40);
    }
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        throw new UnsupportedOperationException("Changing display is not supported.");
    }
    private void setCamera(GL2 gl, GLU glu, float rotX, float rotY, float rotZ) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        float widthHeightRatio = (float) getWidth() / (float) getHeight();
        glu.gluPerspective(45, widthHeightRatio, 1, skySize*2);// (FoW in degrees y-axis, aspect ratio FoW x-axis, distance from the viewer to the near clipping plane, distance from the viewer to the far clipping plane)
        glu.gluLookAt(rotX,rotY,rotZ, 0, 0, 0, 0, 0,1f);
        gl.glRotatef(BuilderWindow.lookX, 0, 0f, 1f);
        gl.glRotatef(BuilderWindow.lookY, 0, 1f, 0f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub
    }
    public void pickingRay (GL2 gl, GLU glu, int x, int y, Ray ray)
    {
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);

        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix, 0);
	          /* note viewport[3] is height of window in pixels */
        realy = viewport[3] - (int) y - 1;
        System.out.println("Coordinates at cursor are (" + x + ", " + realy);
        glu.gluUnProject((double) x, (double) realy, 0.0, //
                mvmatrix, 0,
                projmatrix, 0,
                viewport, 0,
                wcoord, 0);
        System.out.println("World coords at z=0.0 are ( " //
                + wcoord[0] + ", " + wcoord[1] + ", " + wcoord[2]
                + ")");
        glu.gluUnProject((double) x, (double) realy, 1.0, //
                mvmatrix, 0,
                projmatrix, 0,
                viewport, 0,
                wcoord, 0);

        ray.dir[0]=(float)wcoord[0];
        ray.dir[1]=(float)wcoord[1];
        ray.dir[2]=(float)wcoord[2];

    }
}
