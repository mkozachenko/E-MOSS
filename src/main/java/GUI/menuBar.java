package GUI;

/**
 * Created by symph on 31.05.2017.
 */

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

public class menuBar {
    public static void main(String[] args) {
        menuBar s = new menuBar();
    }

    public JMenuBar menuBar()
    {
        JMenuBar menubar = new JMenuBar();
        JMenu filemenu = new JMenu("File");
        filemenu.add(new JSeparator());
        JMenu editmenu = new JMenu("Edit");
        editmenu.add(new JSeparator());
        JMenu aboutmenu = new JMenu("About");
        filemenu.add(new JSeparator());
        JMenuItem fileItem1 = new JMenuItem("New body");
        ActionListener actNewBody = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BuilderWindow builder= new BuilderWindow();
                builder.show();

            }
        };
        JMenuItem fileItem2 = new JMenuItem("Open");
        JMenuItem fileItem3 = new JMenuItem("Close");
        fileItem3.add(new JSeparator());

        JMenuItem fileItem4 = new JMenuItem("Save");
        JMenuItem editItem1 = new JMenuItem("Cut");
        JMenuItem editItem2 = new JMenuItem("Copy");
        editItem2.add(new JSeparator());

        JMenuItem editItem3 = new JMenuItem("Paste");
        JMenuItem editItem4 = new JMenuItem("Insert");

        JMenuItem fileexit = new JMenuItem("Exit");
        fileexit.setMnemonic('x');
        ActionListener lst = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        JMenuItem aboutitem1 = new JMenuItem("About");
        fileexit.setMnemonic('A');
        ActionListener actAbout = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame aboutframe = new JFrame("About SOL-3");
                aboutframe.setResizable(false);
                aboutframe.setVisible(true);
                aboutframe.pack();
                aboutframe.setLocation(350, 300);
                aboutframe.setSize(500, 300);
                ImageIcon img = new ImageIcon("./res/sun.jpg");
                aboutframe.setIconImage(img.getImage());
            }
        };

        fileexit.addActionListener(lst);
        aboutitem1.addActionListener(actAbout);
        fileItem1.addActionListener(actNewBody);
        filemenu.add(fileexit);
        filemenu.add(fileItem1);
        filemenu.add(fileItem2);
        filemenu.add(fileItem3);
        filemenu.add(fileItem4);
        filemenu.add(fileexit);
        editmenu.add(editItem1);
        editmenu.add(editItem2);
        editmenu.add(editItem3);
        editmenu.add(editItem4);
        aboutmenu.add(aboutitem1);
        menubar.add(filemenu);
        menubar.add(editmenu);
        menubar.add(aboutmenu);

        return menubar;

    }


}
