package gui.filepanel.component;

import file.SelectedFile;
import gui.filepanel.PanelRefreshUtil;
import gui.filepanel.model.FileTreeRenderer;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.List;

public class FileTree extends JScrollPane {
    /** singleton instance variable */
    private static FileTree instance = null;

    /** singleton Lazy initialize */
    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private SelectedFile selectedFile = SelectedFile.getInstance();

    /** instance variable */
    private JTree tree;
    private DefaultTreeModel treeModel;

    public FileTree() {
        tree = new JTree();

        // the File tree
        CreateTreeModel();

        //좌측 fileTree에서 Node 선택 시 호출되는 이벤트 함수입니다.
        //현재 중앙 테이블의 파일을 그려냅니다.
        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse){
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                PanelRefreshUtil.currentDirectory=(File)node.getUserObject();
                showChildren(node);
                selectedFile.setFile((File)node.getUserObject());

                PanelRefreshUtil.refreshGitFilePanel();
                PanelRefreshUtil.refreshGitRepoStatusPanel();
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


        tree = new JTree(treeModel);
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

    public static FileTree getInstance() {
        if(instance == null) {
            instance = new FileTree();
        }
        return instance;
    }

    public JTree getTree() {
        return tree;
    }

    public void showChildren(final DefaultMutableTreeNode node) {
        tree.setEnabled(false);
        PanelRefreshUtil.lastTreeNode=node;

        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            public Void doInBackground() {
                File file = (File) node.getUserObject();
                if (file.isDirectory()) {
                    File[] files = fileSystemView.getFiles(file, true); //!!
                    //파일이 실행 중에 수정될 수 있으므로, Leafnode가 아니어도 매번 갱신해줍니다.
                    //if (node.isLeaf()) {
                    node.removeAllChildren();
                    for (File child : files) {
                        if (child.isDirectory()) {
                            publish(child);
                        }
                    }
                    //}
                    FileTable.getInstance().setTableData(files);
                    StagedFileList.getInstance().setStagedFileTableData();
                }

                return null;
            }

            @Override
            protected void process(List<File> chunks) {
                for (File child : chunks) {
                    if(child!=null){
                        node.add(new DefaultMutableTreeNode(child));
                    }
                    else{
                        System.out.println("failed to adding child to tree: file is null");
                    }
                }
            }

            @Override
            protected void done() {
                tree.setEnabled(true);
                //System.out.println("___reload: node is "+((File)node.getUserObject()).getName()+"___");
                //Tree를 갱신하여 node를 다시 그립니다.
                treeModel.reload(node);
            }
        };
        worker.execute();
    }

    //좌측 Filetree를 업데이트합니다.
    private void CreateTreeModel(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        treeModel = new DefaultTreeModel(root);


        // show the file system roots.
        File[] roots = fileSystemView.getRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add(node);
            PanelRefreshUtil.lastTreeNode=node;
            PanelRefreshUtil.currentDirectory=(File)node.getUserObject();

            //showChildren(node);
            //
            File[] files = fileSystemView.getFiles(fileSystemRoot, true);
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
        }
    }
}