package gui;

import file.SelectedFile;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;

public class FileTree extends JScrollPane {
    JTree tree;
    SelectedFile selectedFile;

    public FileTree() {
        tree = Tree.getInstance();
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

        tree = new JTree(GitGUI.treeModel);
        tree.setRootVisible(false);
        tree.addTreeSelectionListener(treeSelectionListener);
        tree.setCellRenderer(new FileTreeCellRenderer());
        tree.expandRow(0);
        this.setViewportView(tree);

        // as per trashgod tip
        tree.setVisibleRowCount(15);

        Dimension preferredSize = getPreferredSize();
        Dimension widePreferred = new Dimension(200, (int)preferredSize.getHeight());
        setPreferredSize(widePreferred);
    }
}

class Tree extends JTree {
    private static Tree instance = null;

    public Tree() {
        super();
    }

    public static Tree getInstance() {
        if(instance == null) {
            instance = new Tree();
        }
        return instance;
    }
}

/** A TreeCellRenderer for a File. */
class FileTreeCellRenderer extends DefaultTreeCellRenderer {

    private FileSystemView fileSystemView;

    private JLabel label;

    public FileTreeCellRenderer() {
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