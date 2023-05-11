package gui;

import file.SelectedFile;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;

public class FileTree extends JScrollPane {
    SelectedFile selectedFile;

    FileTree() {
        selectedFile = SelectedFile.getInstance();

        // the File tree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        GitGUI.treeModel = new DefaultTreeModel(root);

        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse){
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                GitGUI.showChildren(node);
                selectedFile.setFile((File)node.getUserObject());
            }
        };

        // show the file system roots.
        File[] roots = GitGUI.fileSystemView.getRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add(node);
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
        Dimension widePreferred = new Dimension(200, (int)preferredSize.getHeight());
        setPreferredSize(widePreferred);
    }
}