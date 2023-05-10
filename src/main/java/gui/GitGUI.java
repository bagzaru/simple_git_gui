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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Desktop;
import java.awt.Dimension;

import java.awt.Image;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.*;
import javax.swing.tree.*;

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
    /** Title of the application */
    public static final String APP_TITLE = "Simple Git GUI Application";
    /** Used to open/edit/print files. */
    public static Desktop desktop;
    /** Provides nice icons and names for files. */
    public static FileSystemView fileSystemView;

    /** currently selected File. */
    public static File currentFile;

    /** Main GUI container */
    public static JPanel gui;

    /** File-system tree. Built Lazily */
    public static JTree tree;
    public static DefaultTreeModel treeModel;

    /** Directory listing */
    public static JTable table;
    public static JProgressBar progressBar;

    /** Table model for File[]. */
    public static FileTableModel fileTableModel;
    public static ListSelectionListener listSelectionListener;
    public static boolean cellSizesSet = false;
    public static int rowIconPadding = 6;

    public GitGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Significantly improves the look of the output in
                    // terms of the file names returned by FileSystemView!
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch(Exception weTried) {}

                JFrame f = new JFrame(APP_TITLE);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setContentPane(getGui());

                try {
                    URL urlBig = getClass().getResource("fm-icon-32x32.png");
                    URL urlSmall = getClass().getResource("fm-icon-16x16.png");
                    ArrayList<Image> images = new ArrayList<Image>();
                    images.add( ImageIO.read(urlBig) );
                    images.add( ImageIO.read(urlSmall) );
                    f.setIconImages(images);
                } catch(Exception weTried) {}

                f.pack();
                f.setLocationByPlatform(true);
                f.setMinimumSize(f.getSize());
                f.setVisible(true);

                showRootFile();
            }
        });
    }

    public Container getGui() {
        if (gui==null) {
            gui = new JPanel(new BorderLayout(3,3));
            gui.setBorder(new EmptyBorder(5,5,5,5));

            fileSystemView = FileSystemView.getFileSystemView();
            desktop = Desktop.getDesktop();
            FileDetail fileDetail = new FileDetail();

            JPanel fileManage = new JPanel(new BorderLayout(3, 3));
            fileManage.add(new FileTable(fileDetail), BorderLayout.CENTER);
            fileManage.add(new FileToolBar(),BorderLayout.SOUTH);

            JPanel filePanel = new JPanel(new BorderLayout(3,3));
            filePanel.add(fileManage, BorderLayout.CENTER);
            filePanel.add(new StagedFileList(), BorderLayout.SOUTH);

            //JPanel gitMenuPanel = new JPanel(new FlowLayout());// 임시
            JPanel gitMenuPanel = new JPanel(new BorderLayout(3,3));

            //switch(/*filestatus*/){//파일 위치에 따른 패널 생성
                
             //   case 0 :
               // gitMenuPanel.add(new modified_fileGitMenu(),BorderLayout.CENTER);
               // break;

               // case 1 :
               // gitMenuPanel.add(new staged_fileGitMenu(),BorderLayout.CENTER);
               // break;

               // default:
               gitMenuPanel.add(new untracked_fileGitMenu(),BorderLayout.CENTER);
               //break;

        //}
            gitMenuPanel.add(new GitMenu(), BorderLayout.SOUTH);
            gitMenuPanel.setPreferredSize(new Dimension(300, 400)); //임시로 크기 설정
            
            JPanel gitPanel = new JPanel(new BorderLayout(3,3));
            gitPanel.add(filePanel, BorderLayout.CENTER);
            gitPanel.add(gitMenuPanel, BorderLayout.EAST);

            JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new FileTree(fileDetail),
                gitPanel);
            gui.add(splitPane, BorderLayout.CENTER);

            JPanel simpleOutput = new JPanel(new BorderLayout(3,3));
            progressBar = new JProgressBar();
            simpleOutput.add(progressBar, BorderLayout.EAST);
            progressBar.setVisible(false);

            gui.add(simpleOutput, BorderLayout.SOUTH);

        }
        return gui;
    }

    public void showRootFile() {
        // ensure the main files are displayed
        tree.setSelectionInterval(0,0);
    }

    /** Update the table on the EDT */
    public static void setTableData(final File[] files) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (fileTableModel==null) {
                    fileTableModel = new FileTableModel();
                    table.setModel(fileTableModel);
                }
                table.getSelectionModel().removeListSelectionListener(listSelectionListener);
                fileTableModel.setFiles(files);
                table.getSelectionModel().addListSelectionListener(listSelectionListener);
                if (!cellSizesSet) {
                    Icon icon = fileSystemView.getSystemIcon(files[0]);

                    // size adjustment to better account for icons
                    table.setRowHeight( icon.getIconHeight()+rowIconPadding );

                    setColumnWidth(0,-1);
                    setColumnWidth(2,60);
                    table.getColumnModel().getColumn(2).setMaxWidth(120);
                    setColumnWidth(3,-1);

                    cellSizesSet = true;
                }
            }
        });
    }

    public static void setColumnWidth(int column, int width) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        if (width < 0) {
            // use the preferred width of the header..
            JLabel label = new JLabel( (String)tableColumn.getHeaderValue() );
            Dimension preferred = label.getPreferredSize();
            // altered 10->14 as per camickr comment.
            width = (int)preferred.getWidth()+14;
        }
        tableColumn.setPreferredWidth(width);
        tableColumn.setMaxWidth(width);
        tableColumn.setMinWidth(width);
    }

    /** Add the files that are contained within the directory of this node.
    Thanks to Hovercraft Full Of Eels. */
    public static void showChildren(final DefaultMutableTreeNode node) {
        tree.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);

        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            public Void doInBackground() {
                File file = (File)node.getUserObject();
                if (file.isDirectory()) {
                    File[] files = fileSystemView.getFiles(file, true); //!!
                    if (node.isLeaf()) {
                        for (File child : files) {
                            if (child.isDirectory()) {
                                publish(child);
                            }
                        }
                    }
                    setTableData(files);
                }
                return null;
            }

            @Override
            protected void process(List<File> chunks) {
                for (File child : chunks) {
                    node.add(new DefaultMutableTreeNode(child));
                }
            }

            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                progressBar.setVisible(false);
                tree.setEnabled(true);
            }
        };
        worker.execute();
    }
}

class FileDetail {
    /* File details. */
    JLabel fileName;
    JTextField path;
    JLabel date;
    JLabel size;
    JCheckBox readable;
    JCheckBox writable;
    JCheckBox executable;
    JRadioButton isDirectory;
    JRadioButton isFile;

    FileDetail() {
        fileName = new JLabel();
        path = new JTextField(5);
        date = new JLabel();
        size = new JLabel();
        readable = new JCheckBox("Read ");
        writable = new JCheckBox("Write ");
        executable = new JCheckBox("Execute");
        isDirectory = new JRadioButton("Directory");
        isFile = new JRadioButton("File");
    }

    /** Update the File details view with the details of this File. */
    public void setFileDetails(File file) {
        GitGUI.currentFile = file;
        Icon icon = GitGUI.fileSystemView.getSystemIcon(file);
        fileName.setIcon(icon);
        fileName.setText(GitGUI.fileSystemView.getSystemDisplayName(file));
        path.setText(file.getPath());
        date.setText(new Date(file.lastModified()).toString());
        size.setText(file.length() + " bytes");
        readable.setSelected(file.canRead());
        writable.setSelected(file.canWrite());
        executable.setSelected(file.canExecute());
        isDirectory.setSelected(file.isDirectory());

        isFile.setSelected(file.isFile());

        JFrame f = (JFrame)GitGUI.gui.getTopLevelAncestor();
        if (f!=null) {
            f.setTitle(
                    GitGUI.APP_TITLE +
                            " :: " +
                            GitGUI.fileSystemView.getSystemDisplayName(file) );
        }

        GitGUI.gui.repaint();
    }
}

/** A TableModel to hold File[]. */
class FileTableModel extends AbstractTableModel {

    private File[] files;
    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private String[] columns = {
        "Icon",
        "File",
        "Size",
        "Last Modified",
        "File Status"
    };

    FileTableModel() {
        this(new File[0]);
    }

    FileTableModel(File[] files) {
        this.files = files;
    }

    public Object getValueAt(int row, int column) {
        File file = files[row];
        switch (column) {
            case 0:
                return fileSystemView.getSystemIcon(file);
            case 1:
                return fileSystemView.getSystemDisplayName(file);
            case 2:
                return file.length();
            case 3:
                return file.lastModified();
            case 4:
                return ""; //파일 상태(staged, tracked, untracked 등등을 호출하는 함수 들어가야함
            default:
                System.err.println("Logic Error");
        }
        return "";
    }

    public int getColumnCount() {
        return columns.length;
    }

    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return ImageIcon.class;
            case 2:
                return Long.class;
            case 3:
                return Date.class;
            case 4:
        }
        return String.class;
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    public int getRowCount() {
        return files.length;
    }

    public File getFile(int row) {
        return files[row];
    }

    public void setFiles(File[] files) {
        this.files = files;
        fireTableDataChanged();
    }
}



/** A TreeCellRenderer for a File. */
class FileTreeCellRenderer extends DefaultTreeCellRenderer {

    private FileSystemView fileSystemView;

    private JLabel label;

    FileTreeCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
        fileSystemView = FileSystemView.getFileSystemView();
    }

    @Override
    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean selected,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        File file = (File)node.getUserObject();
        label.setIcon(fileSystemView.getSystemIcon(file));
        label.setText(fileSystemView.getSystemDisplayName(file));
        label.setToolTipText(file.getPath());

        if (selected) {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }

        return label;
    }
}