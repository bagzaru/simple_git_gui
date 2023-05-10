package gui;

import gui.GitGUI;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileToolBar extends JToolBar {
    /* File controls. */
    private JButton openFile;
    private JButton newFile;
    private JButton deleteFile;
    private JButton renameFile;
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

        // Check the actions are supported on this platform!
        openFile.setEnabled(GitGUI.desktop.isSupported(Desktop.Action.OPEN));

        newFile = new JButton("New");
        newFile.setMnemonic('n');
        newFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                newFile();
            }
        });
        add(newFile);

        deleteFile = new JButton("Delete");
        deleteFile.setMnemonic('d');
        deleteFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                deleteFile();
            }
        });
        add(deleteFile);

        renameFile = new JButton("Rename");
        renameFile.setMnemonic('r');
        renameFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                renameFile();
            }
        });
        add(renameFile);

        copyFile = new JButton("Copy");
        copyFile.setMnemonic('c');
        copyFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                showErrorMessage("'Copy' not implemented.", "Not implemented.");
            }
        });
        add(copyFile);
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
            southRadio.add(newTypeFile);
            southRadio.add(newTypeDirectory);

            name = new JTextField(15);

            newFilePanel.add(new JLabel("Name"), BorderLayout.WEST);
            newFilePanel.add(name);
            newFilePanel.add(southRadio, BorderLayout.SOUTH);
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
                if (FileUtils.deleteQuietly(GitGUI.currentFile)) {
                    if (directory) {
                        // delete the node..
                        TreePath currentPath = findTreePath(GitGUI.currentFile);
                        System.out.println(currentPath);
                        DefaultMutableTreeNode currentNode =
                                (DefaultMutableTreeNode) currentPath.getLastPathComponent();

                        GitGUI.treeModel.removeNodeFromParent(currentNode);
                    }

                    GitGUI.showChildren(parentNode);
                } else {
                    String msg = "The file '" + GitGUI.currentFile + "' could not be deleted.";
                    showErrorMessage(msg, "Delete Failed");
                }
            } catch(Throwable t) {
                showThrowable(t);
            }
        }
        GitGUI.gui.repaint();
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

            if (nodeFile.equals(find)) {
                return treePath;
            }
        }
        // not found!
        return null;
    }
}