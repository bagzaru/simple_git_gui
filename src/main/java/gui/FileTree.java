package gui;

import file.SelectedFile;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;

public class FileTree extends JScrollPane {
    static JTree tree;
    SelectedFile selectedFile;


    //Tree.getInstance와 FileTree.tree가 달라서 부득이하게 수정하였습니다.
    public static JTree getInstance() {
        return tree;
    }

    public FileTree() {
        tree = Tree.getInstance();
        selectedFile = SelectedFile.getInstance();

        // the File tree
        CreateTreeModel();

        //좌측 fileTree에서 Node 선택 시 호출되는 이벤트 함수입니다.
        //현재 중앙 테이블의 파일을 그려냅니다.
        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse){
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                PanelRefreshUtil.currentDirectory=(File)node.getUserObject();
                GitGUI.showChildren(node);
                selectedFile.setFile((File)node.getUserObject());

                PanelRefreshUtil.refreshGitMenu();      //우측 git 패널 새로고침
            }
        };

        //좌측 fileTree에서 확장/축소 시 호출되는 이벤트 함수입니다.
        //축소 시 하위 파일의 focuse가 사라지면서 fileTree가 처음부터 다시 생성되는 오류를 방지합니다.
        TreeExpansionListener treeExpansionListener = new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {

            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                TreePath collapsedNodePath = event.getPath();
                DefaultMutableTreeNode nodeCollapsed = (DefaultMutableTreeNode) collapsedNodePath.getLastPathComponent();
                tree.setSelectionPath(collapsedNodePath);
            }
        };


        tree = new JTree(GitGUI.treeModel);
        tree.setRootVisible(false);
        tree.addTreeSelectionListener(treeSelectionListener);
        tree.addTreeExpansionListener(treeExpansionListener);
        tree.setCellRenderer(new FileTreeRenderer());
        tree.expandRow(0);
        this.setViewportView(tree);

        // as per trashgod tip
        tree.setVisibleRowCount(30);

        Dimension preferredSize = getPreferredSize();
        Dimension widePreferred = new Dimension(200, (int)preferredSize.getHeight());
        setPreferredSize(widePreferred);
    }

    //좌측 Filetree를 업데이트합니다.
    public static void CreateTreeModel(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        GitGUI.treeModel = new DefaultTreeModel(root);


        // show the file system roots.
        File[] roots = GitGUI.fileSystemView.getRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add(node);
            PanelRefreshUtil.lastTreeNode=node;
            PanelRefreshUtil.currentDirectory=(File)node.getUserObject();

            //showChildren(node);
            //
            File[] files = GitGUI.fileSystemView.getFiles(fileSystemRoot, true);
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
        }
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