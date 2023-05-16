package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import file.FileTableModel;
import file.SelectedFile;

public class FileTable extends JScrollPane {
    Table table;
    SelectedFile selectedFile;

    public FileTable() {
        table = Table.getInstance();
        selectedFile = SelectedFile.getInstance();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setShowVerticalLines(false);

        GitGUI.listSelectionListener = new ListSelectionListener() {
            //중앙 파일 테이블에서 파일 클릭 시 발생하는 이벤트
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int row = table.getSelectionModel().getLeadSelectionIndex();
                selectedFile.setFile(((FileTableModel) table.getModel()).getFile(row));

                //우측 git 패널을 업데이트합니다.
                PanelRefreshUtil.refreshGitFilePanel();
                PanelRefreshUtil.refreshGitRepoStatusPanel();
            }
        };

        //중앙 파일테이블의 더블클릭을 통해 다른 폴더로 이동을 구현하였습니다.
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        //1. selectedFile을 갱신해줍니다.
                        int row = table.getSelectionModel().getLeadSelectionIndex();
                        File selected = ((FileTableModel) table.getModel()).getFile(row);
                        selectedFile.setFile(selected);
                        //2. 좌측 Tree에서 내가 클릭한 파일을 찾습니다.
                        //  일단 찾으면 Tree쪽 EventListener에 의해 폴더가 이동됩니다.
                        int treeCount = PanelRefreshUtil.lastTreeNode.getChildCount();
                        for (int i = 0; i < treeCount; i++) {
                            TreeNode treeNode = PanelRefreshUtil.lastTreeNode.getChildAt(i);
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNode;
                            if (((File) node.getUserObject()).getName().equals(selected.getName())) {
                                FileTree.getInstance().setExpandsSelectedPaths(true);
                                FileTree.getInstance().setSelectionPath(new TreePath(node.getPath()));
                                break;
                            }
                        }

                    } catch (Exception exception) {
                        System.out.println("Something error occurred while mouseDoubleClick Event:" + exception.getMessage());
                    }

                }
            }
        });


        table.getSelectionModel().addListSelectionListener(GitGUI.listSelectionListener);

        this.setViewportView(table);
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension((int) d.getWidth(), (int) d.getHeight() / 2));
    }
}

class Table extends JTable {
    private static Table instance = null;

    public Table() {
        super();
    }

    public static Table getInstance() {
        if (instance == null) {
            instance = new Table();
        }
        return instance;
    }
}