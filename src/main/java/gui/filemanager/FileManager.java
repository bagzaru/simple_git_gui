package gui.filemanager;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import datamodel.SelectedFile;
import gui.filemanager.component.*;

public class FileManager extends JPanel {
    /** Title of the application */
    public static final String APP_TITLE = "Simple Git GUI Application";

    /** Main GUI container */
    public static JPanel gui;

    public FileManager() {
        add(getGui());
        showRootFile();
    }

    //GUI의 component들을 생성 및 조립하는 메소드
    private Container getGui() {
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

    //프로그램 시작시 루트 폴더를 SelectedFile로 지정 후 새로고침
    private void showRootFile() {
        // ensure the main files are displayed
        SelectedFile.getInstance().setFile((File) PanelRefreshUtil.lastTreeNode.getUserObject());
        PanelRefreshUtil.refreshAll();
    }
}