package gui;

import gui.branchpanel.BranchPanel;
import gui.filepanel.FilePanel;
import gui.filepanel.component.GitMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;


public class SimpleGitGUIApplication extends JFrame {
    /** Title of the application */
    public static final String APP_TITLE = "Simple Git GUI Application";

    private JToolBar menuBar = new JToolBar();
    private JButton fileManagerMode = new JButton("File Manager");
    private JButton branchManagerMode = new JButton("Branch Manager");
    private JButton loginButton=new JButton("LOGIN");


    private JPanel currentPanel;
    private FilePanel fileManager;
    private BranchPanel branchManager;

    public SimpleGitGUIApplication() {
        super(APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            // Significantly improves the look of the output in
            // terms of the file names returned by FileSystemView!
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception weTried) {
        }
        try {
            URL urlBig = getClass().getResource("fm-icon-32x32.png");
            URL urlSmall = getClass().getResource("fm-icon-16x16.png");
            ArrayList<Image> images = new ArrayList<Image>();
            images.add(ImageIO.read(urlBig));
            images.add(ImageIO.read(urlSmall));
            setIconImages(images);
        } catch (Exception weTried) {
        }

        fileManagerMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel(fileManager);
            }
        });

        branchManagerMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel(branchManager);

                //테스트용입니다. 실제로는 branch List Panel에서 작동해야합니다.
                //branchManager.commitLogPane.Update("main");
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        menuBar.setFloatable(false);
        menuBar.add(fileManagerMode);
        menuBar.add(branchManagerMode);
        add(menuBar, BorderLayout.NORTH);

        fileManager = new FilePanel();
        branchManager = new BranchPanel();

        currentPanel = fileManager;
        add(currentPanel, BorderLayout.CENTER);

        pack();
        setLocationByPlatform(true);
        setMinimumSize(getSize());
        setVisible(true);
    }

    private void switchPanel(JPanel panel) {
        remove(currentPanel);
        currentPanel = panel;
        add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SimpleGitGUIApplication mainFrame = new SimpleGitGUIApplication();
                mainFrame.setVisible(true);
            }
        });
    }
}