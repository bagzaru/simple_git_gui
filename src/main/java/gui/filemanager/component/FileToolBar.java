package gui.filemanager.component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import gui.filemanager.FileManager;
import gui.filemanager.PanelRefreshUtil;
import org.apache.commons.io.FileUtils;

import file.SelectedFile;
import jgitmanager.JGitManager;

public class FileToolBar extends JToolBar {
    private SelectedFile selectedFile;
    private Desktop desktop;

    /* File controls. */
    private JButton openFile;
    private JButton newFile;
    private JButton deleteFile;
    private JButton renameFile;
    private JButton copyFile;
    // 새로고침 버튼
    private JButton refreshButton;
    // StageAll 버튼
    private JButton stageAllButton;

    /* GUI options/containers for new File/Directory creation.  Created lazily. */
    private JPanel newFilePanel;
    private JRadioButton newTypeFile;
    private JTextField name;

    public FileToolBar() {
        selectedFile = SelectedFile.getInstance();
        desktop = Desktop.getDesktop();

        setFloatable(false);

        openFile = new JButton("Open");
        openFile.setMnemonic('o');

        openFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    desktop.open(selectedFile.getFile());
                } catch (Throwable t) {
                    showThrowable(t);
                }
                FileManager.gui.repaint();
            }
        });
        add(openFile);

        // Check the actions are supported on this platform!
        openFile.setEnabled(desktop.isSupported(Desktop.Action.OPEN));

        newFile = new JButton("New");
        newFile.setMnemonic('n');
        newFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                newFile();
            }
        });
        add(newFile);

        deleteFile = new JButton("Delete");
        deleteFile.setMnemonic('d');
        deleteFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                deleteFile();
            }
        });
        add(deleteFile);

//        copyFile = new JButton("Copy");
//        copyFile.setMnemonic('c');
//        copyFile.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent ae) {
//
//            }
//        });
//        add(copyFile);

        //stageAllButton
        stageAllButton = new JButton("StageAll");
        stageAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stageAll();
            }
        });
        add(stageAllButton);

        // refreshButton
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                refreshButton();
            }
        });
        add(refreshButton);
    }

    private void newFile() {
        if (selectedFile.getFile() == null) {
            showErrorMessage("No location selected for new file.", "Select Location");
            return;
        }

        if (newFilePanel == null) {
            newFilePanel = new JPanel(new BorderLayout(3, 3));

            JPanel southRadio = new JPanel(new GridLayout(1, 0, 2, 2));
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
                FileManager.gui,
                newFilePanel,
                "Create File",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                boolean created;
                File parentFile = selectedFile.getFile();
                if (!parentFile.isDirectory()) {
                    parentFile = parentFile.getParentFile();
                }
                File file = new File(parentFile, name.getText());
                if (newTypeFile.isSelected()) {
                    created = file.createNewFile();
                } else {
                    created = file.mkdir();
                }
                if (created) {
                    PanelRefreshUtil.refreshAll();
                } else {
                    String msg = "The file '" +
                            file +
                            "' could not be created. (it might exist)";
                    showErrorMessage(msg, "Create Failed");
                }
            } catch (Throwable t) {
                showThrowable(t);
            }
        }
        FileManager.gui.repaint();
    }

    private void deleteFile() {
        if (selectedFile.getFile() == null
                || selectedFile.getFile().getAbsolutePath().equals(PanelRefreshUtil.currentDirectory.getAbsolutePath())) {
            showErrorMessage("No file selected for deletion.", "Select File");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                FileManager.gui,
                "Are you sure you want to delete this file?",
                "Delete File",
                JOptionPane.ERROR_MESSAGE
        );
        if (result == JOptionPane.OK_OPTION) {
            try {
                System.out.println("currentFile: " + selectedFile.getFile());

                boolean directory = selectedFile.getFile().isDirectory();
                if (FileUtils.deleteQuietly(selectedFile.getFile())) {
                    SelectedFile.getInstance().setFile(SelectedFile.getInstance().getFile().getParentFile());
                    PanelRefreshUtil.refreshAll();
                } else {
                    String msg = "The file '" + selectedFile.getFile() + "' could not be deleted.";
                    showErrorMessage(msg, "Delete Failed");
                }
            } catch (Throwable t) {
                showThrowable(t);
            }
        }
        FileManager.gui.repaint();
    }

    /*
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
    */

    private void refreshButton() {
        PanelRefreshUtil.refreshAll();
    }

    private void stageAll() {
        try {
            JGitManager.gitAddAll(SelectedFile.getInstance().getFile());
        } catch (Exception e) {
            JPanel errorPanel = new JPanel(new BorderLayout(3, 3));
            errorPanel.add(new JLabel("Error: " + e.getMessage()), BorderLayout.WEST);
            JOptionPane.showConfirmDialog(FileManager.gui, errorPanel,"Error",JOptionPane.OK_OPTION);
            System.out.println("Toolbar: stageAll failed: " + e.toString() + ": " + e.getMessage());
        }
        PanelRefreshUtil.refreshAll();
    }

    private void showErrorMessage(String errorMessage, String errorTitle) {
        JOptionPane.showMessageDialog(
                FileManager.gui,
                errorMessage,
                errorTitle,
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showThrowable(Throwable t) {
        t.printStackTrace();
        JOptionPane.showMessageDialog(
                FileManager.gui,
                t.toString(),
                t.getMessage(),
                JOptionPane.ERROR_MESSAGE
        );
        FileManager.gui.repaint();
    }

    private TreePath findTreePath(File find) {
        for (int ii = 0; ii < FileTree.getInstance().getTree().getRowCount(); ii++) {
            TreePath treePath = FileTree.getInstance().getTree().getPathForRow(ii);
            Object object = treePath.getLastPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
            File nodeFile = (File) node.getUserObject();

            if (nodeFile.equals(find)) {
                return treePath;
            }
        }
        // not found!
        return null;
    }
}