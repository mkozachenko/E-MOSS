package GUI;

/**
 * Created by symph on 31.05.2017.
 */

import Graphics.*;
import SQL.*;
import javax.swing.*;
import javax.swing.GroupLayout.Group;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import com.jogamp.opengl.*;
import javax.swing.event.*;
import com.jogamp.opengl.util.texture.*;
import static java.nio.file.FileSystems.getDefault;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.awt.event.*;


public class BuilderWindow {
    static Point pointOfInterest = new Point();
    public static boolean XY, ZY, ZX, mousePressed;
    public static Point mouse=new Point();
    public static float bodySize=20;
    public static float zoom, KBmoveX, KBmoveY, KBmoveZ;
    public static float mouseX=0, mouseY=0, mouseZ=0, mouseY2;
    private static int TexCnt=0;
    private static JLabel jlbLabel1, jlbLabel2, jlbLabel3, jlbLabel4, jlbLabel5, jlbLabel6, jlbLabel7, jlbLabel8;
    private static JButton click = new JButton("THING");
    public static JButton acceptSize = new JButton("Accept"), openBtn;
    static public JTextField nameField, bodySizeField, distanceField, tiltField, orbSpeedField, rotSpeedField, parentField;
    private static JFileChooser openFile = new JFileChooser("D:\\");
    static GLCapabilities capabilities = createGLCapabilities();
    public static JPanel panel_A = new JPanel();
    public static JPanel panel_B = new JPanel();
    public static JPanel panel_B_1 = new JPanel();
    public static JPanel panel_B_2 = new JPanel();
    public static JPanel panel_B_3 = new JPanel();
    public static JPanel panel6 = new JPanel();
    public static Dimension screenSize;
    public FileInputStream in;
    public Texture skyT, defBodyTex;
    public static float slide = 0f, rot = 0f, tilt = 0f, pos = 0f;
    public static boolean clicker=false, m_click = false;
    public File copyfile;
    public float angleY,angleZ;
    public static float deltaAngleY = 0.0f, xOrigin = 1, yOrigin = 1, deltaAngleX = 0.0f;
    public static float camX=0f, camY, camZ=1f, lookX=0f, lookY, lookZ=0f;
    private static String filename="null";
    public static BuilderScene canvas;
    public static String path, dragged="yes";
    public static String savePath="D:\\Projects\\Eclipse\\SOL\\res\\maps\\sunmap.jpg";
    public static String keyPressed;

    private static GLCapabilities createGLCapabilities() {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        capabilities.setRedBits(8);
        capabilities.setBlueBits(8);
        capabilities.setGreenBits(8);
        capabilities.setAlphaBits(8);
        return capabilities;
    }
    public void show() {
        ImageIcon img = new ImageIcon("./res/sun.jpg");
        JFrame frame = new JFrame("Body Builder");
        frame.setIconImage(img.getImage());
        frame.setLayout(new GridLayout(1, 2));
        /********/
        canvas = new BuilderScene(capabilities, 400, 400);
        /********/
        frame.add(panel_A);
        panel_A.setLayout(new GridLayout(1, 1));
        panel_A.add(canvas);
        canvas.requestFocus();
        canvas.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (zoom >= 10) {
                    zoom = zoom + e.getUnitsToScroll();
                } else {
                    zoom = 10;
                }
            }
        });
        canvas.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub
                xOrigin = e.getXOnScreen();
                yOrigin = e.getYOnScreen();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub
                lookX=e.getX();
                lookY=e.getY();
            }
        });
        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                camX = e.getX();
                camY = e.getY();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                switch (e.getButton()){
                    case MouseEvent.BUTTON1:
                        m_click = true;
                        mouseX = e.getX();
                        mouseY = e.getY();
                        break;
                }
            }
        });
        canvas.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

                switch (e.getKeyCode()){
                    case KeyEvent.VK_A:
                        KBmoveY=KBmoveY-10;
                        break;
                    case KeyEvent.VK_D:
                        KBmoveY=KBmoveY+10;
                        break;
                    case KeyEvent.VK_W:
                        KBmoveZ=KBmoveZ-10;
                        break;
                    case KeyEvent.VK_S:
                        KBmoveZ=KBmoveZ+10;
                        break;
                    case KeyEvent.VK_UP:
                        lookZ=+10;
                        break;
                    case KeyEvent.VK_DOWN:
                        lookZ=-10;
                        break;
                    case KeyEvent.VK_LEFT:
                        lookY=-10;
                        break;
                    case KeyEvent.VK_RIGHT:
                        lookY=+10;
                        break;
                    case KeyEvent.VK_SPACE:
                        KBmoveZ=0;
                        KBmoveY=0;
                        lookZ=0;
                        lookY=0;
                        break;
                }

            }
        });
        FileSystem fs = getDefault();
        Path path = fs.getPath("res\\temp\\");
        final String abs = path.toAbsolutePath().toString().replace("\\", "\\");
        BoundedRangeModel tiltRange = new DefaultBoundedRangeModel(0, 1, 0, 100);
        final JSlider sliderTilt = new JSlider(tiltRange);
        sliderTilt.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                tilt = ((JSlider) ce.getSource()).getValue();
            }
        });
        sliderTilt.setMinorTickSpacing(10); // step / interavl
        sliderTilt.setSnapToTicks(true); // should be activated for custom tick

        menuBar swingMenu = new menuBar();
        frame.setJMenuBar(swingMenu.menuBar()); // add Menu to the Main Window
        // frame.add(panel_A, BorderLayout.WEST);
        frame.add(panel_B);
        jlbLabel1 = new JLabel("Choose your texture: ");
        jlbLabel2 = new JLabel("Name:");
        jlbLabel3 = new JLabel("Size (medium radius in km):");
        jlbLabel4 = new JLabel("Medium distance from sun:");
        jlbLabel5 = new JLabel("Orbital rotation speed:");
        jlbLabel6 = new JLabel("Self rotation speed:");
        jlbLabel7 = new JLabel("Tilt of body (relatively to 0 z-axis):");
        nameField = new JTextField("YOUR PLANET",15);
        bodySizeField = new JTextField(5);
        distanceField = new JTextField(5);
        tiltField = new JTextField(5);
        orbSpeedField = new JTextField(5);
        rotSpeedField = new JTextField(5);
        parentField = new JTextField(5);
        /*******/
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = (int) Math.floor((screenSize.width / 100) * 95);
        final int height = (int) Math.floor((screenSize.height / 100) * 95);
        frame.setSize(width, height);
        openBtn = new JButton("Open");
        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                openFile.showOpenDialog(null);
                openFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (openFile.getSelectedFile()!=null){
                    filename = openFile.getSelectedFile().getName();
                    savePath = abs + "\\" + filename;
                    savePath.replace("\\", "\\\\");
                    InputStream inStream = null;
                    OutputStream outStream = null;
                    try {
                        copyfile = new File(savePath);
                        inStream = new FileInputStream(openFile.getSelectedFile());
                        outStream = new FileOutputStream(copyfile);
                        byte[] buffer = new byte[1024];
                        int length;
                        // copy the file content in bytes
                        while ((length = inStream.read(buffer)) > 0) {
                            outStream.write(buffer, 0, length);
                        }
                        inStream.close();
                        outStream.close();
                        System.out.println("File is copied successful!");
                        System.out.println(savePath+"_______"+TexCnt);
                        TexCnt++;
                        jlbLabel1.setText("__" + filename);
                        clicker=true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {System.out.println("Soryan");}
            }
        });
        click.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                try {
                    updateSQL.localUpdate(nameField.getText(), bodySizeField.getText(), "12", "11", "02", "1234", filename, "planet");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        acceptSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bodySize=Float.parseFloat(bodySizeField.getText());
                System.out.println(bodySize);
            }
        });
        panel_B.setLayout(new GridLayout(3, 5));
        panel_B.add(panel_B_1);
        panel_B.add(panel_B_2);
        //panel_B.add(panel_B_3);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        //Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        /**
         Object raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
         Border loweredbevel = BorderFactory.createLoweredBevelBorder();
         Border empty = BorderFactory.createEmptyBorder();
         **/
        panel_B_1.setBorder(blackline);
        panel_B_2.setBorder(loweredetched);
        //panel_B_3.setBorder(raisedbevel);
        //panel_B_1.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel_B_2.setLayout(new FlowLayout(FlowLayout.LEFT));
        //panel_B_3.setLayout(new FlowLayout(FlowLayout.LEFT));
        GroupLayout layout = new GroupLayout(panel_B_1);
        panel_B_1.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jlbLabel1)
                                        .addComponent(jlbLabel2)
                                        .addComponent(jlbLabel3)
                                        .addComponent(jlbLabel4)
                                        .addComponent(jlbLabel5)
                                        .addComponent(jlbLabel6)
                                        .addComponent(jlbLabel7)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(openBtn)
                                        .addComponent(nameField)
                                        .addComponent(bodySizeField)
                                        .addComponent(distanceField)
                                        .addComponent(orbSpeedField)
                                        .addComponent(rotSpeedField)
                                        .addComponent(sliderTilt)
                                )
                                //.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(acceptSize)
                        //)
                )
        );
        //layout.linkSize(SwingConstants.HORIZONTAL, findButton, cancelButton);

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbLabel1)
                                        .addComponent(openBtn)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbLabel2)
                                        .addComponent(nameField)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbLabel3)
                                        .addComponent(bodySizeField)
                                        .addComponent(acceptSize)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbLabel4)
                                        .addComponent(distanceField)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbLabel5)
                                        .addComponent(orbSpeedField)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbLabel6)
                                        .addComponent(rotSpeedField)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jlbLabel7)
                                        .addComponent(sliderTilt)
                                )
                        )
                )
        );
        panel_B_2.add(click);

        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new BuilderWindow().show();
    }

    public static void allign (JPanel pan,Component[] com){
		/*int i=0;
		GroupLayout layout = new GroupLayout(pan);
		pan.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		Group gr1 = layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING));
		for(i=0;i<com.length;i++){
			gr1.addComponent(com[i]);
		}
		layout.setHorizontalGroup(gr1);
		Group gr2 = layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE));
		for(i=0;i<com.length;i++){
			gr1.addComponent(com[i]);
		}
		layout.setVerticalGroup(gr1);*/
        GroupLayout layout = new GroupLayout(panel_B_1);
        panel_B_1.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        Group gr1 = layout.createSequentialGroup();
        for(int i=0;i<com.length;i++){
            gr1.addComponent(com[i]);
        }
        layout.setHorizontalGroup(gr1);

        Group gr2 = layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING));
        for(int i=0;i<com.length;i++){
            gr2.addComponent(com[i]);
        }
        layout.setVerticalGroup(gr2);
    }
}
