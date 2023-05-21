package gui;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import file.SelectedFile;

public class GitGUI {
    /** Title of the application */
    public static final String APP_TITLE = "Simple Git GUI Application";

    /** Main GUI container */
    public static JPanel gui;

    public GitGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Significantly improves the look of the output in
                    // terms of the file names returned by FileSystemView!
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception weTried) {
                }

                JFrame f = new JFrame(APP_TITLE);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setContentPane(getGui());

                try {
                    URL urlBig = getClass().getResource("fm-icon-32x32.png");
                    URL urlSmall = getClass().getResource("fm-icon-16x16.png");
                    ArrayList<Image> images = new ArrayList<Image>();
                    images.add(ImageIO.read(urlBig));
                    images.add(ImageIO.read(urlSmall));
                    f.setIconImages(images);
                } catch (Exception weTried) {
                }

                f.pack();
                f.setLocationByPlatform(true);
                f.setMinimumSize(f.getSize());
                f.setVisible(true);

                showRootFile();
            }
        });
    }

    public Container getGui() {
        if (gui == null) {
            gui = new JPanel(new BorderLayout(3, 3));
            gui.setBorder(new EmptyBorder(5, 5, 5, 5));

            JPanel fileManage = new JPanel(new BorderLayout(3, 3));
            fileManage.add(FileTable.getInstance(), BorderLayout.CENTER);
            fileManage.add(new FileToolBar(), BorderLayout.SOUTH);

            JPanel filePanel = new JPanel(new BorderLayout(3, 3));
            filePanel.add(fileManage, BorderLayout.CENTER);
            filePanel.add(StagedFileList.getInstance(), BorderLayout.SOUTH);

            //깃 폴더 관리 패널(우하단)
            //JPanel gitRepoPanel = new JPanel(new BorderLayout(3,3));
            JPanel gitRepoPanel = new JPanel();
            gitRepoPanel.setLayout(new GridLayout(2,1));
            gitRepoPanel.add(GitRepositoryStatusPanel.getInstance());
            gitRepoPanel.add(GitMenu.getInstance());

            //gitMenuPanel: (우측 전체) git 명령어 관련 패널
            JPanel gitMenuPanel = new JPanel(new GridLayout(2, 1));
            gitMenuPanel.add(GitFilePanel.getInstance());
            gitMenuPanel.add(gitRepoPanel);
            gitMenuPanel.setPreferredSize(new Dimension(300, 400)); //임시로 크기 설정

            //파일트리 제외한 중앙 및 우측 부분
            JPanel gitPanel = new JPanel(new BorderLayout(3, 3));
            gitPanel.add(filePanel, BorderLayout.CENTER);
            gitPanel.add(gitMenuPanel, BorderLayout.EAST);

            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    new FileTree(),
                    gitPanel);
            gui.add(splitPane, BorderLayout.CENTER);
        }
        return gui;
    }

    private void showRootFile() {
        // ensure the main files are displayed
        SelectedFile.getInstance().setFile((File)PanelRefreshUtil.lastTreeNode.getUserObject());
        PanelRefreshUtil.refreshAll();
    }
}