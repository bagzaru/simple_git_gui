/*
The MIT License

Copyright (c) 2015-2023 Valentyn Kolesnikov (https://github.com/javadev/file-manager)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package gui;

import java.awt.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.*;
import javax.swing.tree.*;

import gui.tablemodel.FileTableModel;
import file.SelectedFile;

/**
 * A basic File Manager. Requires 1.6+ for the Desktop &amp; SwingWorker classes, amongst other
 * minor things.
 *
 * <p>Includes support classes FileTableModel &amp; FileTreeCellRenderer.
 *
 * <p>TODO Bugs
 *
 * <ul>
 *   <li>Still throws occasional AIOOBEs and NPEs, so some update on the EDT must have been missed.
 *   <li>Fix keyboard focus issues - especially when functions like rename/delete etc. are called
 *       that update nodes &amp; file lists.
 *   <li>Needs more testing in general.
 *       <p>TODO Functionality
 *   <li>Implement Read/Write/Execute checkboxes
 *   <li>Implement Copy
 *   <li>Extra prompt for directory delete (camickr suggestion)
 *   <li>Add File/Directory fields to FileTableModel
 *   <li>Double clicking a directory in the table, should update the tree
 *   <li>Move progress bar?
 *   <li>Add other file display modes (besides table) in CardLayout?
 *   <li>Menus + other cruft?
 *   <li>Implement history/back
 *   <li>Allow multiple selection
 *   <li>Add file search
 * </ul>
 */
public class GitGUI {
    /**
     * Title of the application
     */
    public static final String APP_TITLE = "Simple Git GUI Application";

    /**
     * Provides nice icons and names for files.
     */
    public static FileSystemView fileSystemView;

    /**
     * Main GUI container
     */
    public static JPanel gui;

    /**
     * File-system tree. Built Lazily
     */
    public static DefaultTreeModel treeModel;

    private Tree tree = Tree.getInstance();

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

            fileSystemView = FileSystemView.getFileSystemView();

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

    public void showRootFile() {
        // ensure the main files are displayed
        SelectedFile.getInstance().setFile((File)PanelRefreshUtil.lastTreeNode.getUserObject());
        PanelRefreshUtil.refreshAll();
    }
}