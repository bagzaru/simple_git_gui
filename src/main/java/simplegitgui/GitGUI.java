/*
 * $Id$
 *
 * Copyright 2015 Valentyn Kolesnikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package main.java.simplegitgui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileSystemView;

import javax.imageio.ImageIO;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import java.io.*;
import java.nio.channels.FileChannel;

import java.net.URL;

/**
A basic File Manager.  Requires 1.6+ for the Desktop &amp; SwingWorker
classes, amongst other minor things.

Includes support classes FileTableModel &amp; FileTreeCellRenderer.

TODO Bugs
<ul>
<li>Still throws occasional AIOOBEs and NPEs, so some update on
the EDT must have been missed.
<li>Fix keyboard focus issues - especially when functions like
rename/delete etc. are called that update nodes &amp; file lists.
<li>Needs more testing in general.

TODO Functionality
<li>Implement Read/Write/Execute checkboxes
<li>Implement Copy
<li>Extra prompt for directory delete (camickr suggestion)
<li>Add File/Directory fields to FileTableModel
<li>Double clicking a directory in the table, should update the tree
<li>Move progress bar?
<li>Add other file display modes (besides table) in CardLayout?
<li>Menus + other cruft?
<li>Implement history/back
<li>Allow multiple selection
<li>Add file search
</ul>

@author Andrew Thompson
@version 2011-06-01
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

    GitGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Significantly improves the look of the output in
                    // terms of the file names returned by FileSystemView!
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch(Exception weTried) {
                }
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

            JPanel gitMenuPanel = new JPanel(new FlowLayout()); //임시
            //JPanel gitMenuPanel = new JPanel(new BorderLayout(3,3));
            //gitMenuPanel.add(new fileGitMenu(),BorderLayout.CENTER);
            //gitMenuPanel.add(new GitMenu(), BorderLayout.SOUTH);
            gitMenuPanel.setPreferredSize(new Dimension(150, 200)); //임시로 크기 설정

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
                    setColumnWidth(3,60);
                    table.getColumnModel().getColumn(3).setMaxWidth(120);
                    setColumnWidth(4,-1);

                    cellSizesSet = true;
                }
            }
        });
    }

    public static void setColumnWidth(int column, int width) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        if (width<0) {
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
                File file = (File) node.getUserObject();
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

    public static boolean copyFile(File from, File to) throws IOException {

        boolean created = to.createNewFile();

        if (created) {
            FileChannel fromChannel = null;
            FileChannel toChannel = null;
            try {
                fromChannel = new FileInputStream(from).getChannel();
                toChannel = new FileOutputStream(to).getChannel();

                toChannel.transferFrom(fromChannel, 0, fromChannel.size());

                // set the flags of the to the same as the from
                to.setReadable(from.canRead());
                to.setWritable(from.canWrite());
                to.setExecutable(from.canExecute());
            } finally {
                if (fromChannel != null) {
                    fromChannel.close();
                }
                if (toChannel != null) {
                    toChannel.close();
                }
                return false;
            }
        }
        return created;
    }
}

class FileTree extends JScrollPane {
    FileTree(FileDetail fileDetail) {
        // the File tree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        GitGUI.treeModel = new DefaultTreeModel(root);

        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse){
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                GitGUI.showChildren(node);
                fileDetail.setFileDetails((File)node.getUserObject());
            }
        };

        // show the file system roots.
        File[] roots = GitGUI.fileSystemView.getRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add( node );
            //showChildren(node);
            //
            File[] files = GitGUI.fileSystemView.getFiles(fileSystemRoot, true);
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
            //
        }

        GitGUI.tree = new JTree(GitGUI.treeModel);
        GitGUI.tree.setRootVisible(false);
        GitGUI.tree.addTreeSelectionListener(treeSelectionListener);
        GitGUI.tree.setCellRenderer(new FileTreeCellRenderer());
        GitGUI.tree.expandRow(0);
        this.setViewportView(GitGUI.tree);

        // as per trashgod tip
        GitGUI.tree.setVisibleRowCount(15);

        Dimension preferredSize = getPreferredSize();
        Dimension widePreferred = new Dimension(
                200,
                (int)preferredSize.getHeight());
        setPreferredSize( widePreferred );
    }
}

class FileTable extends JScrollPane {
    FileTable(FileDetail fileDetail) {
        GitGUI.table = new JTable();
        GitGUI.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GitGUI.table.setAutoCreateRowSorter(true);
        GitGUI.table.setShowVerticalLines(false);

        GitGUI.listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int row = GitGUI.table.getSelectionModel().getLeadSelectionIndex();
                fileDetail.setFileDetails( ((FileTableModel)GitGUI.table.getModel()).getFile(row) );
            }
        };
        GitGUI.table.getSelectionModel().addListSelectionListener(GitGUI.listSelectionListener);

        this.setViewportView(GitGUI.table);
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension((int)d.getWidth(), (int)d.getHeight()/2));
    }
}

class FileToolBar extends JToolBar {
    /* File controls. */
    private JButton openFile;
    private JButton printFile;
    private JButton editFile;
    private JButton deleteFile;
    private JButton newFile;
    private JButton copyFile;

    /* GUI options/containers for new File/Directory creation.  Created lazily. */
    private JPanel newFilePanel;
    private JRadioButton newTypeFile;
    private JTextField name;

    FileToolBar() {
        setFloatable(false);

        openFile = new JButton("Open");
        openFile.setMnemonic('o');

        openFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                try {
                    GitGUI.desktop.open(GitGUI.currentFile);
                } catch(Throwable t) {
                    showThrowable(t);
                }
                GitGUI.gui.repaint();
            }
        });
        add(openFile);

        editFile = new JButton("Edit");
        editFile.setMnemonic('e');
        editFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                try {
                    GitGUI.desktop.edit(GitGUI.currentFile);
                } catch(Throwable t) {
                    showThrowable(t);
                }
            }
        });
        add(editFile);

        printFile = new JButton("Print");
        printFile.setMnemonic('p');
        printFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                try {
                    GitGUI.desktop.print(GitGUI.currentFile);
                } catch(Throwable t) {
                    showThrowable(t);
                }
            }
        });
        add(printFile);

        // Check the actions are supported on this platform!
        openFile.setEnabled(GitGUI.desktop.isSupported(Desktop.Action.OPEN));
        editFile.setEnabled(GitGUI.desktop.isSupported(Desktop.Action.EDIT));
        printFile.setEnabled(GitGUI.desktop.isSupported(Desktop.Action.PRINT));

        addSeparator();

        newFile = new JButton("New");
        newFile.setMnemonic('n');
        newFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                newFile();
            }
        });
        add(newFile);

        copyFile = new JButton("Copy");
        copyFile.setMnemonic('c');
        copyFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                showErrorMessage("'Copy' not implemented.", "Not implemented.");
            }
        });
        add(copyFile);

        JButton renameFile = new JButton("Rename");
        renameFile.setMnemonic('r');
        renameFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                renameFile();
            }
        });
        add(renameFile);

        deleteFile = new JButton("Delete");
        deleteFile.setMnemonic('d');
        deleteFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                deleteFile();
            }
        });
        add(deleteFile);
    }

    private void renameFile() {
        if (GitGUI.currentFile==null) {
            showErrorMessage("No file selected to rename.","Select File");
            return;
        }

        String renameTo = JOptionPane.showInputDialog(GitGUI.gui, "New Name");
        if (renameTo!=null) {
            try {
                boolean directory = GitGUI.currentFile.isDirectory();
                TreePath parentPath = findTreePath(GitGUI.currentFile.getParentFile());
                DefaultMutableTreeNode parentNode =
                        (DefaultMutableTreeNode)parentPath.getLastPathComponent();

                boolean renamed = GitGUI.currentFile.renameTo(new File(
                        GitGUI.currentFile.getParentFile(), renameTo));
                if (renamed) {
                    if (directory) {
                        // rename the node..

                        // delete the current node..
                        TreePath currentPath = findTreePath(GitGUI.currentFile);
                        System.out.println(currentPath);
                        DefaultMutableTreeNode currentNode =
                                (DefaultMutableTreeNode)currentPath.getLastPathComponent();

                        GitGUI.treeModel.removeNodeFromParent(currentNode);

                        // add a new node..
                    }

                    GitGUI.showChildren(parentNode);
                } else {
                    String msg = "The file '" +
                            GitGUI.currentFile +
                            "' could not be renamed.";
                    showErrorMessage(msg,"Rename Failed");
                }
            } catch(Throwable t) {
                showThrowable(t);
            }
        }
        GitGUI.gui.repaint();
    }

    private void deleteFile() {
        if (GitGUI.currentFile==null) {
            showErrorMessage("No file selected for deletion.","Select File");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                GitGUI.gui,
                "Are you sure you want to delete this file?",
                "Delete File",
                JOptionPane.ERROR_MESSAGE
        );
        if (result==JOptionPane.OK_OPTION) {
            try {
                System.out.println("currentFile: " + GitGUI.currentFile);
                TreePath parentPath = findTreePath(GitGUI.currentFile.getParentFile());
                System.out.println("parentPath: " + parentPath);
                DefaultMutableTreeNode parentNode =
                        (DefaultMutableTreeNode)parentPath.getLastPathComponent();
                System.out.println("parentNode: " + parentNode);

                boolean directory = GitGUI.currentFile.isDirectory();
                boolean deleted = GitGUI.currentFile.delete();
                if (deleted) {
                    if (directory) {
                        // delete the node..
                        TreePath currentPath = findTreePath(GitGUI.currentFile);
                        System.out.println(currentPath);
                        DefaultMutableTreeNode currentNode =
                                (DefaultMutableTreeNode)currentPath.getLastPathComponent();

                        GitGUI.treeModel.removeNodeFromParent(currentNode);
                    }

                    GitGUI.showChildren(parentNode);
                } else {
                    String msg = "The file '" +
                            GitGUI.currentFile +
                            "' could not be deleted.";
                    showErrorMessage(msg,"Delete Failed");
                }
            } catch(Throwable t) {
                showThrowable(t);
            }
        }
        GitGUI.gui.repaint();
    }

    private void newFile() {
        if (GitGUI.currentFile==null) {
            showErrorMessage("No location selected for new file.","Select Location");
            return;
        }

        if (newFilePanel==null) {
            newFilePanel = new JPanel(new BorderLayout(3,3));

            JPanel southRadio = new JPanel(new GridLayout(1,0,2,2));
            newTypeFile = new JRadioButton("File", true);
            JRadioButton newTypeDirectory = new JRadioButton("Directory");
            ButtonGroup bg = new ButtonGroup();
            bg.add(newTypeFile);
            bg.add(newTypeDirectory);
            southRadio.add( newTypeFile );
            southRadio.add( newTypeDirectory );

            name = new JTextField(15);

            newFilePanel.add( new JLabel("Name"), BorderLayout.WEST );
            newFilePanel.add( name );
            newFilePanel.add( southRadio, BorderLayout.SOUTH );
        }

        int result = JOptionPane.showConfirmDialog(
                GitGUI.gui,
                newFilePanel,
                "Create File",
                JOptionPane.OK_CANCEL_OPTION);
        if (result==JOptionPane.OK_OPTION) {
            try {
                boolean created;
                File parentFile = GitGUI.currentFile;
                if (!parentFile.isDirectory()) {
                    parentFile = parentFile.getParentFile();
                }
                File file = new File( parentFile, name.getText() );
                if (newTypeFile.isSelected()) {
                    created = file.createNewFile();
                } else {
                    created = file.mkdir();
                }
                if (created) {

                    TreePath parentPath = findTreePath(parentFile);
                    DefaultMutableTreeNode parentNode =
                            (DefaultMutableTreeNode)parentPath.getLastPathComponent();

                    if (file.isDirectory()) {
                        // add the new node..
                        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file);

                        TreePath currentPath = findTreePath(GitGUI.currentFile);
                        DefaultMutableTreeNode currentNode =
                                (DefaultMutableTreeNode)currentPath.getLastPathComponent();

                        GitGUI.treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
                    }

                    GitGUI.showChildren(parentNode);
                } else {
                    String msg = "The file '" +
                            file +
                            "' could not be created.";
                    showErrorMessage(msg, "Create Failed");
                }
            } catch(Throwable t) {
                showThrowable(t);
            }
        }
        GitGUI.gui.repaint();
    }

    private void showErrorMessage(String errorMessage, String errorTitle) {
        JOptionPane.showMessageDialog(
                GitGUI.gui,
                errorMessage,
                errorTitle,
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showThrowable(Throwable t) {
        t.printStackTrace();
        JOptionPane.showMessageDialog(
                GitGUI.gui,
                t.toString(),
                t.getMessage(),
                JOptionPane.ERROR_MESSAGE
        );
        GitGUI.gui.repaint();
    }

    private TreePath findTreePath(File find) {
        for (int ii=0; ii<GitGUI.tree.getRowCount(); ii++) {
            TreePath treePath = GitGUI.tree.getPathForRow(ii);
            Object object = treePath.getLastPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)object;
            File nodeFile = (File)node.getUserObject();

            if (nodeFile==find) {
                return treePath;
            }
        }
        // not found!
        return null;
    }
}

class StagedFileList extends JPanel {
    //아직 미구현
    StagedFileList() {
        super(new BorderLayout(4,2));
        setPreferredSize(new Dimension(100, 150));
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
        "Path/name",
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
                return file.getPath();
            case 3:
                return file.length();
            case 4:
                return file.lastModified();
            case 5:
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
            case 3:
                return Long.class;
            case 4:
                return Date.class;
            case 5:
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