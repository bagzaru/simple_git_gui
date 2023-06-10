package gui;

import file.GitBranchData;
import file.SelectedFile;
import file.SelectedFileChangedEventListener;
import gui.branchmanager.BranchManager;
import gui.filemanager.FileManager;
import jgitmanager.JGitManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;


public class SimpleGitGUIFrame extends JFrame implements SelectedFileChangedEventListener {
    /** Title of the application */
    public static final String APP_TITLE = "Simple Git GUI Application";

    private JToolBar menuBar = new JToolBar();
    private JButton fileManagerMode = new JButton("File Manager");
    private JButton branchManagerMode = new JButton("Branch Manager");
    private JButton loginButton=new JButton("LOGIN");//sj


    private JPanel currentPanel;
    private FileManager fileManager;
    private BranchManager branchManager;

    private GitBranchData gitBranchData = new GitBranchData();

    public SimpleGitGUIFrame() {
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
                gitBranchData.initGitBranchData();
                switchPanel(branchManager);

                //테스트용입니다. 실제로는 branch List Panel에서 작동해야합니다.
                //branchManager.commitLogPane.Update("main");
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginDialog dialog=new LoginDialog();

            }
        });//sj

        menuBar.setFloatable(false);
        menuBar.add(fileManagerMode);
        menuBar.add(branchManagerMode);
        menuBar.add(loginButton);
        add(menuBar, BorderLayout.NORTH);

        fileManager = new FileManager();
        branchManager = new BranchManager(gitBranchData);

        currentPanel = fileManager;
        add(currentPanel, BorderLayout.CENTER);

        //Git 상태에 따른 BranchManager 버튼 처리
        SelectedFile.getInstance().addSelectedFileChangedListener(this);
        setBranchManagerModeEnabledByGit(SelectedFile.getInstance().getFile());

        pack();
        setLocationByPlatform(true);
        setMinimumSize(getSize());
        setResizable(false);
        setVisible(true);
    }

    private void switchPanel(JPanel panel) {
        remove(currentPanel);
        currentPanel = panel;
        add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    public void selectedFileChanged(File selectedFile) {
        setBranchManagerModeEnabledByGit(selectedFile);
    }

    private void setBranchManagerModeEnabledByGit(File selectedFile){
        if(selectedFile==null){
            return;
        }
        if(JGitManager.findGitRepository(selectedFile)==1){
            branchManagerMode.setEnabled(true);
        }else{
            branchManagerMode.setEnabled(false);
        }

    }
}